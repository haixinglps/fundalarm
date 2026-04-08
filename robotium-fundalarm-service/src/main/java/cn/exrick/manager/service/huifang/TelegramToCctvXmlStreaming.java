package cn.exrick.manager.service.huifang;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TelegramToCctvXmlStreaming {

	private static final ObjectMapper mapper = new ObjectMapper();
	private static final JsonFactory jsonFactory = new JsonFactory();
	private static final XMLOutputFactory xmlFactory = XMLOutputFactory.newInstance();

	// 每个XML文件最多1000条消息
	private static final int MESSAGES_PER_FILE = 1000;

	private final String inputFile;
	private final String outputDir;
	private int totalChats = 0;
	private int totalMessages = 0;
	private int emptyChatCount = 0;
	private long startTime;

	// 文件切分相关
	private int fileIndex = 0;
	private CctvXmlWriter currentWriter;
	private int messagesInCurrentFile = 0;

	public TelegramToCctvXmlStreaming(String inputFile, String outputDir) {
		this.inputFile = inputFile;
		this.outputDir = outputDir;
	}

	public void convert() throws Exception {
		startTime = System.currentTimeMillis();

		Path outputPath = Paths.get(outputDir);
		Files.createDirectories(outputPath);

		System.out.println("开始处理: " + inputFile);
		System.out.println("输出目录: " + outputPath.toAbsolutePath());
		System.out.println("每个文件最多: " + MESSAGES_PER_FILE + " 条视频");
		System.out.println("========================================");

		// 创建第一个文件
		currentWriter = new CctvXmlWriter(outputDir, fileIndex++);

		try (JsonParser parser = jsonFactory.createParser(new File(inputFile))) {
			skipToChatsList(parser);

			System.out.println("找到 chats list，开始流式解析...");
			System.out.println("========================================");

			int chatIndex = 0;

			while (parser.nextToken() == JsonToken.START_OBJECT) {
				chatIndex++;

				int msgsInChat = processChatStreaming(parser, chatIndex);

				if (msgsInChat > 0) {
					totalChats++;
					totalMessages += msgsInChat;
					System.out.println(
							"  ✓ 完成: " + msgsInChat + " 条视频 | 累计: " + totalMessages + " 视频, " + totalChats + " 频道");
				} else {
					emptyChatCount++;
					if (emptyChatCount % 10 == 0) {
						System.out.println("  ... 已跳过 " + emptyChatCount + " 个无视频频道 ...");
					}
				}
			}

			System.out.println("\n========================================");
			System.out.println("所有频道处理完成，关闭最后文件...");
			currentWriter.close();
		}

		long duration = (System.currentTimeMillis() - startTime) / 1000;
		System.out.println("\n========================================");
		System.out.println("处理完成!");
		System.out.println("总文件数: " + fileIndex);
		System.out.println("总频道数: " + (totalChats + emptyChatCount));
		System.out.println("有视频的频道: " + totalChats);
		System.out.println("无视频的频道: " + emptyChatCount);
		System.out.println("总视频数: " + totalMessages);
		System.out.println("耗时: " + duration + " 秒 (" + duration / 60 + " 分 " + duration % 60 + " 秒)");
	}

	private void skipToChatsList(JsonParser parser) throws Exception {
		while (parser.nextToken() != null) {
			if (parser.currentToken() == JsonToken.FIELD_NAME && "list".equals(parser.getCurrentName())) {
				parser.nextToken();
				if (parser.currentToken() == JsonToken.START_ARRAY) {
					return;
				}
			}
		}
		throw new Exception("未找到 chats.list 数组");
	}

	/**
	 * 检查是否需要切分新文件，并执行切分
	 */
	private void checkAndRotateFile() throws Exception {
		if (messagesInCurrentFile >= MESSAGES_PER_FILE) {
			System.out.println("  -> 切分新文件 (当前文件已满 " + MESSAGES_PER_FILE + " 条)");
			currentWriter.close();
			currentWriter = new CctvXmlWriter(outputDir, fileIndex++);
			messagesInCurrentFile = 0;
		}
	}

	/**
	 * 流式处理单个 chat
	 */
	private int processChatStreaming(JsonParser parser, int chatIndex) throws Exception {
		String chatName = null;
		Long chatId = null;
		int videoCount = 0;

		while (parser.nextToken() != JsonToken.END_OBJECT) {
			if (parser.currentToken() != JsonToken.FIELD_NAME) {
				continue;
			}

			String fieldName = parser.getCurrentName();
			parser.nextToken();

			switch (fieldName) {
			case "name":
				chatName = parser.getValueAsString();
				break;
			case "id":
				chatId = parser.getValueAsLong();
				break;
			case "messages":
				lasttag = 0;
				if (parser.currentToken() == JsonToken.START_ARRAY) {
					if (chatName != null) {
						System.out.println("\n[" + chatIndex + "] " + chatName);
					}
					videoCount = processMessagesStreaming(parser, chatId, chatName);
				} else {
					parser.skipChildren();
				}
				break;
			default:
				if (parser.currentToken() == JsonToken.START_OBJECT || parser.currentToken() == JsonToken.START_ARRAY) {
					parser.skipChildren();
				}
				break;
			}
		}

		if (videoCount > 0 && chatName != null) {
			System.out.println("  小计: " + videoCount + " 条视频");
		}

		return videoCount;
	}

	/**
	 * 流式处理 messages 数组，每1000条切分文件
	 */

	String lasttitleString = "";
	String lastrqString = "";
	int lasttag = 0;

	private int processMessagesStreaming(JsonParser parser, Long chatId, String chatName) throws Exception {
		int videoCount = 0;

		while (parser.nextToken() == JsonToken.START_OBJECT) {
			// 检查是否需要切分（在读取消息前检查）
			checkAndRotateFile();

			JsonNode msg = mapper.readTree(parser);

			StringBuffer tx = new StringBuffer();
			tx.append("");
			int tag = 0;
			if (msg.has("text_entities") && msg.get("text_entities").isArray()) {
				for (JsonNode ent : msg.get("text_entities")) {
					if (ent.has("text")) {
						tag = 1;
						String text = ent.get("text").asText().trim();
						if (!text.isEmpty()) {
							tx.append(" ").append(text);
						}
					}
				}
			}

			String aString = lastrqString;
			String bString = lasttitleString;
			int ctag = lasttag;
			lasttitleString = tx.toString();
			lastrqString = msg.get("date").asText();
			lasttag = tag;
			// 只处理视频消息
			if (!msg.has("duration_seconds")) {
				continue;
			}

			// 修补代码。正常不用这个。补充遗漏的空视频需要。避免把正常视频重复生成一遍。
//			if (msg.has("file_name")) {
//				if (!noChineseByRegex(msg.get("file_name").asText()))
//					continue;
//			}
//			if (!aString.contentEquals(msg.get("date").asText())) {
//				continue;
//			}
//			if (tag == 1)
//				continue;
//			if (ctag == 0)
//				continue;

			JsonNode durationNode = msg.get("duration_seconds");
			if (durationNode == null || durationNode.isNull()) {
				continue;
			}
			int sec = Integer.parseInt(durationNode.asText());
			if (sec < 600)
				continue;

			ChatInfo chatInfo = new ChatInfo(chatId, chatName, aString, bString);

			currentWriter.writeArticle(chatInfo, msg);

			videoCount++;
			messagesInCurrentFile++;
			totalMessages++;

			// 每1000条打印一次
			if (videoCount % 1000 == 0) {
				System.out
						.println("    已处理 " + videoCount + " 条视频 (文件" + fileIndex + ": " + messagesInCurrentFile + ")");
			}

		}

		return videoCount;
	}

	static class ChatInfo {
		final Long id;
		final String name;
		String lastrqString = "";
		String lasttitleString = "";

		ChatInfo(Long id, String name, String lastrqString, String lasttitString) {
			this.id = id;
			this.name = name;
			this.lastrqString = lastrqString;
			this.lasttitleString = lasttitString;
		}
	}

	static class CctvXmlWriter {
		private final String outputDir;
		private final int fileIndex;
		private FileWriter fileWriter;
		private XMLStreamWriter xmlWriter;
		private int messageCount = 0;

		public CctvXmlWriter(String outputDir, int fileIndex) throws Exception {
			this.outputDir = outputDir;
			this.fileIndex = fileIndex;
			createNewFile();
		}

		private void createNewFile() throws Exception {
			String filename = String.format("telegram20260313_export_%03d.xml", fileIndex);
			Path filepath = Paths.get(outputDir, filename);

			System.out.println("创建 XML: " + filepath.getFileName());

			fileWriter = new FileWriter(filepath.toFile());
			xmlWriter = xmlFactory.createXMLStreamWriter(fileWriter);

			xmlWriter.writeStartDocument("UTF-8", "1.0");
			xmlWriter.writeCharacters("\n");
			xmlWriter.writeStartElement("articles");
			xmlWriter.writeCharacters("\n");
		}

		public void writeArticle(ChatInfo chat, JsonNode msg) throws Exception {
			long chatId = chat.id != null ? chat.id : 0;
			int msgId = msg.get("id").asInt();

			xmlWriter.writeStartElement("article");
			xmlWriter.writeCharacters("\n");

			xmlWriter.writeStartElement("RQ");
			xmlWriter.writeCharacters(formatRQ(msg));
			xmlWriter.writeEndElement();
			xmlWriter.writeCharacters("\n");

			xmlWriter.writeStartElement("TX");
			xmlWriter.writeCData(formatTX(msg, chat));
			xmlWriter.writeEndElement();
			xmlWriter.writeCharacters("\n");

			xmlWriter.writeStartElement("TI");
			xmlWriter.writeCharacters(formatTI(chat, msg));
			xmlWriter.writeEndElement();
			xmlWriter.writeCharacters("\n");

			xmlWriter.writeStartElement("UR");
			xmlWriter.writeCharacters(formatUR(chatId, msgId, msg));
			xmlWriter.writeEndElement();
			xmlWriter.writeCharacters("\n");

			xmlWriter.writeStartElement("DM");
			xmlWriter.writeCharacters("t.me");
			xmlWriter.writeEndElement();
			xmlWriter.writeCharacters("\n");

			String cc = formatCC(msg);
			if (!cc.isEmpty()) {
				xmlWriter.writeStartElement("CC");
				xmlWriter.writeCharacters(cc);
				xmlWriter.writeEndElement();
				xmlWriter.writeCharacters("\n");
			}

			xmlWriter.writeStartElement("CH");
			xmlWriter.writeCharacters("kaikai");
			xmlWriter.writeEndElement();
			xmlWriter.writeCharacters("\n");

			xmlWriter.writeEndElement();
			xmlWriter.writeCharacters("\n");

			messageCount++;
		}

		private String formatRQ(JsonNode msg) {
			if (msg.has("date_unixtime")) {
				long seconds = msg.get("date_unixtime").asLong();
				return String.valueOf(seconds * 1000);
			}
			if (msg.has("date")) {
				try {
					String dateStr = msg.get("date").asText();
					java.time.Instant instant = java.time.Instant.parse(dateStr + "Z");
					return String.valueOf(instant.toEpochMilli());
				} catch (Exception e) {
					return String.valueOf(System.currentTimeMillis());
				}
			}
			return String.valueOf(System.currentTimeMillis());
		}

		private String formatTX(JsonNode msg, ChatInfo chatInfo) {
			StringBuilder tx = new StringBuilder();
			if (msg.has("file_name") && !msg.get("file_name").isNull()) {
				tx.append(msg.get("file_name").asText().trim());
			}
			int tag = 0;
			if (msg.has("text_entities") && msg.get("text_entities").isArray()) {
				for (JsonNode ent : msg.get("text_entities")) {
					if (ent.has("text")) {
						tag = 1;
						String text = ent.get("text").asText().trim();
						if (!text.isEmpty()) {
							tx.append(" ").append(text);
						}
					}
				}
			}
			if (tag == 0) {
				if (chatInfo.lastrqString.contentEquals(msg.get("date").asText())) {

					tx.append(" ");
					tx.append(chatInfo.lasttitleString);
				}
			}

			return tx.toString().trim();
		}

		private String formatTI(ChatInfo chat, JsonNode msg) {
			String chatName = chat.name != null ? chat.name : "Unknown";

			String dateStr = "";
			if (msg.has("date")) {
				String date = msg.get("date").asText();
				dateStr = date.substring(0, 10).replace("-", "");
			}

			String replyMark = "";
			if (msg.has("reply_to_message_id") && !msg.get("reply_to_message_id").isNull()) {
				replyMark = "[回复] ";
			}

			String sizeInfo = "";
			if (msg.has("file_size")) {
				long size = msg.get("file_size").asLong();
				sizeInfo = String.format(" [%.1fMB]", size / 1024.0 / 1024.0);
			}

			return String.format("《%s》 %s %s视频%d%s", chatName, dateStr, replyMark, msg.get("id").asInt(), sizeInfo);
		}

		private String formatUR(long chatId, int msgId, JsonNode msg) {

//			if (msg.has("photo_file_size")) {
//				msgId = msgId + 1;
//			}
			long realChatId = chatId;
			if (chatId < 0 && String.valueOf(chatId).startsWith("-100")) {
				realChatId = Long.parseLong(String.valueOf(chatId).substring(4));
			}

			String replyId = null;
			if (msg.has("reply_to_message_id")) {
				JsonNode replyNode = msg.get("reply_to_message_id");
				if (!replyNode.isNull()) {
					replyId = replyNode.asText();
				}
			}

			if (replyId != null && !replyId.isEmpty()) {
				return String.format("https://t.me/c/%d/%s/%d", realChatId, replyId, msgId);
			} else {
				return String.format("https://t.me/c/%d/%d", realChatId, msgId);
			}
		}

		private String formatCC(JsonNode msg) {
			if (msg.has("duration_seconds")) {
				int seconds = msg.get("duration_seconds").asInt();
				return String.format("%02d:%02d", seconds / 60, seconds % 60);
			}
			return "";
		}

		public int getMessageCount() {
			return messageCount;
		}

		public void close() throws Exception {
			xmlWriter.writeEndElement();
			xmlWriter.writeCharacters("\n");
			xmlWriter.writeEndDocument();
			xmlWriter.close();
			fileWriter.close();
			System.out.println("  文件完成: " + messageCount + " 条视频");
		}
	}

	public static boolean noChineseByRegex(String str) {
		return str == null || !Pattern.compile("[\u4e00-\u9fa5]").matcher(str).find();
	}

	public static void main(String[] args) {
		String inputFile = args.length > 0 ? args[0] : "D:\\Download\\DataExport_2026-03-13 (1)\\result.json";
		String outputDir = args.length > 1 ? args[1] : "D:\\telegram_cctv_xml";

		try {
			TelegramToCctvXmlStreaming converter = new TelegramToCctvXmlStreaming(inputFile, outputDir);
			converter.convert();
		} catch (Exception e) {
			System.err.println("错误: " + e.getMessage());
			e.printStackTrace();
		}
	}
}