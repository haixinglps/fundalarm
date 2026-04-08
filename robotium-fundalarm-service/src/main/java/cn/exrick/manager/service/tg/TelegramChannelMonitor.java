package cn.exrick.manager.service.tg;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import cn.exrick.manager.service.RobotService;
import cn.exrick.manager.service.impl.RobotServiceImpl;
import cn.exrick.manager.service.util.ThreadSafeFileWriter;

//@Component
public class TelegramChannelMonitor extends TelegramLongPollingBot {
	private final String targetChannel;
	private volatile boolean running = true;

	private RobotService robotService;
	private final Executor taskExecutor; // 声明线程池依赖

	static ThreadSafeFileWriter writerLog = null;

	public TelegramChannelMonitor(String botToken, String channelUsername, DefaultBotOptions botOptions,
			RobotService robotService, Executor taskExecutor) {

		super(botOptions, botToken);
		this.robotService = robotService;
		this.taskExecutor = taskExecutor;

		this.targetChannel = channelUsername.startsWith("@") ? channelUsername : "@" + channelUsername;
		this.startMonitoring();
		try {
			writerLog = new ThreadSafeFileWriter("/tmp/robot.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public static void main(String[] args) {

		String botToken = "8485884288:AAFQj7kI1xPSk6HZDPK4LT18LFd5b50C8zQ";
		String channelName = "taoluwanwuzhibo";
		DefaultBotOptions botOptions = new DefaultBotOptions();
		botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
		botOptions.setProxyHost("localhost");
		botOptions.setProxyPort(7890);
		RobotServiceImpl robotServicea = new RobotServiceImpl();

		TelegramChannelMonitor bot = new TelegramChannelMonitor(botToken, channelName, botOptions, robotServicea, null);

		bot.startMonitoring();
		System.out.println("结束了");

	}

	private static volatile boolean started = false;

	public synchronized void startMonitoring() {
		if (started) {
			System.out.println("机器人已启动，跳过重复启动");
			return;
		}
		try {
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(this);
			started = true;
			System.out.println("开始监控频道: {}" + targetChannel);
			System.out.println("测试API连通性: {}" + (getBaseUrl().contains("api.telegram.org") ? "成功" : "失败"));
		} catch (TelegramApiException e) {
			System.out.println("启动机器人失败: {}" + e.getMessage());
		}

//		System.out.println("测试API连通性: " + (this.getBaseUrl().contains("api.telegram.org") ? "成功" : "失败"));
		System.out.println("开始监控频道: " + targetChannel);
//		while (running) {
//			try {
//				Thread.sleep(1000); // 防止CPU空转
//			} catch (InterruptedException e) {
//				Thread.currentThread().interrupt();
//			}
//		}
	}

	@Override
	public void onUpdateReceived(Update update) {

		CompletableFuture.runAsync(() -> {
			// 异步处理逻辑

			System.out.println("new message");
			System.out.println(update.hasChannelPost());

			if (update.hasChannelPost()
					&& targetChannel.equals("@" + update.getChannelPost().getChat().getUserName())) {
				Message channelMsg = update.getChannelPost();
				System.out.println(update.getChannelPost().getChat().getUserName());
				Long chatId = channelMsg.getChatId(); // 频道ID
				String text = channelMsg.getText(); // 消息文本
				System.out.println("新消息---: " + update.getChannelPost().getText());
				// 处理媒体消息
				if (update.getChannelPost().hasPhoto()) {
					System.out.println("检测到图片附件");
				}
				String identifier;

//			identifier = channelMsg.getFrom().getFirstName() + channelMsg.getFrom().getLastName() + " (ID:"
//					+ channelMsg.getFrom().getId() + ")";

				System.out.println("消息来自: " + channelMsg.toString());
				writerLog.write(channelMsg.toString() + "\t" + text);

//			sendChannelReply(chatId, "已收到消息,搜索结果是：一茶夜晚调教大学生:vid33", channelMsg.getMessageId());
				robotService.dealSearch(update);

			} else if (update.hasMessage()) {
				// 确保更新包含消息
				if (!update.getMessage().hasText()) {
					System.out.println("没有文本");
					return;
				}

				Message receivedMessage = update.getMessage();
				Long chatId = receivedMessage.getChatId();
				String userName = receivedMessage.getFrom().getUserName();
				String receivedText = receivedMessage.getText();
				String identifier;
				if (receivedMessage.getFrom().getUserName() != null) {
					identifier = "@" + receivedMessage.getFrom().getUserName();
				} else {
					identifier = receivedMessage.getFrom().getFirstName() + receivedMessage.getFrom().getLastName()
							+ " (ID:" + receivedMessage.getFrom().getId() + ")";
				}

				System.out.println(receivedMessage);
				String chatType = receivedMessage.getChat().getType();

				// 方法1：检查 chatType
				boolean isGroupByType = "group".equals(chatType) || "supergroup".equals(chatType);
				if (isGroupByType) {
					System.out.println("群组消息：");
					try {

						System.out.println("type3:---------------------:lps::::" + chatType);

						boolean isGroup = "supergroup".equals(chatType);// "group".equals(chatType) ||
						if (!isGroup) {
							System.out.println("不是私有群组消息，忽略:" + chatType);
							// return;
						} else {
							System.out.println("发现群类型:" + chatType);

						}

						int vip = 0;

						// ========== 2. 判断是否为指定群组 ==========
						Long targetGroupId = -1003867299066L; // 替换为你的群组ID（带 -100 前缀）
						int groupok = 0;
						int topicok = 0;
						if (!chatId.equals(targetGroupId)) {
							System.out.println("333不是目标群组，当前群组ID: " + chatId);

						} else {
							groupok = 1;
						}

						// ========== 3. 判断是否为话题消息及话题ID ==========
						// 关键：message_thread_id 字段标识话题
						if (groupok == 1) {
							Integer messageThreadId = receivedMessage.getMessageThreadId();

							if (messageThreadId == null) {
								System.out.println("这是普通消息（非话题）");
							} else {
								System.out.println("话题IDs: " + messageThreadId);

								// 判断是否为指定话题
								Integer targetTopicId = 206; // 替换为目标话题ID

								if (messageThreadId.equals(targetTopicId)) {
									System.out.println("✅ 来自目标群组的目标话题，必须响应。");
									topicok = 1;
								} else if (messageThreadId.intValue() == 2564) {
									System.out.println("✅ 来自目标群组的 小飞机网盘 话题，必须响应。");
									topicok = 2;
								}

								else {

									System.out.println("来自目标群组的其他话题,不响应。");
									// return;
								}
							}

						}

						if (topicok == 1 || topicok == 2) {
							System.out.println("vip群 搜索和提取");

							robotService.dealGetWork(update);
						} else if (groupok == 1) {
							System.out.println("vip群内部非搜索和提取");
						}

						else {

							robotService.dealSearch(update);
						}
					} catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					}

					return;
				} else {
					System.out.println("私聊");
				}

				System.out.println("消息来自: " + identifier);
				if (writerLog != null) {
					try {
						writerLog.write(identifier + "\t" + receivedText);
					} catch (Exception e) {
						System.err.println("writerLog 写入失败: " + e.getMessage());
					}
				}

//			// 1. 创建回复消息实例
//			SendMessage replyMessage = new SendMessage();
//			replyMessage.setChatId(chatId.toString()); // 设置回复对象
//
//			// 2. 设置回复内容
//			String replyText = "👋 你好 " + identifier + "!\n";
//			replyText += "我收到了你的消息: " + receivedText + "\n";
//			replyText += "为你提取到的作品链接是：\nhttp://video.playback.tencent.taolu2.cn//live/20250301/11740798171367212617/95af0860-d10e-420b-bf9b-ad6cd5625d29.mp4";
//
//			replyMessage.setText(replyText);
//
//			// 3. 可选：设置回复格式（Markdown）
//			replyMessage.enableMarkdown(true);
//
//			// 4. 可选：设置为引用回复
//			replyMessage.setReplyToMessageId(receivedMessage.getMessageId());
//
//			try {
//				// 5. 发送回复
//				execute(replyMessage);
//				System.out.println("成功回复消息给: " + identifier);
//			} catch (TelegramApiException e) {
//				System.err.println("发送回复失败: " + e.getMessage());
//			}

				System.out.println("调用 dealGetWork, robotService=" + (robotService != null));
				try {
					robotService.dealGetWork(update);
					System.out.println("dealGetWork 调用完成");
				} catch (Exception e) {
					System.err.println("dealGetWork 异常: " + e.getMessage());
					e.printStackTrace();
					// 发送错误消息给用户
					try {
						SendMessage errorMsg = new SendMessage();
						errorMsg.setChatId(chatId.toString());
						errorMsg.setText("❌ 处理失败: " + e.getMessage());
						execute(errorMsg);
					} catch (Exception e2) {
						System.err.println("发送错误消息失败: " + e2.getMessage());
					}
				}

			}

		}, taskExecutor); // 使用注入的线程池
	}

	@Override
	public String getBotUsername() {
		return "summer0011999bot";
	}

//	@Override
//	public void onUpdateReceived(Update update) {
//		// TODO Auto-generated method stub
//		
//	}

//    @Override
//    public String getBotUsername() {
//        if(botUsername == null) {
//            try {
//                User botInfo = execute(new GetMe());
//                botUsername = botInfo.getUserName();
//            } catch (TelegramApiException e) {
//                throw new RuntimeException("获取机器人信息失败", e);
//            }
//        }
//        return botUsername;
//    }

	public void sendChannelReply(Long chatId, String replyText, Integer replyToMsgId) {
		SendMessage reply = new SendMessage();
		reply.setChatId(chatId.toString()); // 频道ID必须转换为字符串

		// 关键设置：启用引用回复
		reply.setReplyToMessageId(replyToMsgId);
		reply.setText(replyText);

		// 可选：Markdown格式
		reply.enableMarkdownV2(true);

		try {
			execute(reply); // 发送消息
			System.out.println("✅ 回复频道消息成功");
		} catch (TelegramApiException e) {
			System.err.println("❌ 回复失败: " + e.getMessage());
		}
	}

	public int sendChannelReplyWithPhoto(Long chatId, String caption, Integer replyToMsgId, String photoUrl) {
		SendPhoto reply = new SendPhoto();
		reply.setChatId(chatId.toString());
		reply.setReplyToMessageId(replyToMsgId);
		reply.setPhoto(new InputFile(photoUrl)); // 支持HTTP URL或本地文件
		reply.setCaption(caption); // 图片的描述文本
//	    reply.enableMarkdownV2(true); // 可选：Markdown格式化

		try {
			execute(reply);
			System.out.println("✅ 带图片的回复发送成功");
			return 1;
		} catch (TelegramApiException e) {
			System.err.println("❌ 发送失败: " + e.getMessage());
			return 0;
		}
	}

	public void sendPhotos(Long chatId, Integer replyToMsgId, List<InputMedia> mediaList) {

//		List<InputMedia> mediaList = new ArrayList<InputMedia>();
//		mediaList.add(new InputMediaPhoto("图片1_URL").setCaption("描述1"));
//		mediaList.add(new InputMediaPhoto("图片2_URL").setCaption("描述2"));

		SendMediaGroup mediaGroup = new SendMediaGroup();
		mediaGroup.setChatId(chatId.toString());
		mediaGroup.setMedias(mediaList);
		mediaGroup.setReplyToMessageId(replyToMsgId);

		// 设置引用回复
//	    ReplyPa replyParams = new ReplyParameters();
//	    replyParams.setMessageId(replyToMsgId);
//	    mediaGroup.setReplyParameters(replyParams);

//		execute(mediaGroup);

		try {
			execute(mediaGroup);
			System.out.println("✅ 带图片的回复发送成功");
		} catch (TelegramApiException e) {
			System.err.println("❌ 发送失败: " + e.getMessage());
		}

	}

}
