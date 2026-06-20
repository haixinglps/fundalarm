package cn.exrick.manager.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

//import com.insurance.order.domain.TInsuredOrderEvent;
//import com.insurance.order.service.NoticeService;

import cn.exrick.common.jedis.JedisClient;
import cn.exrick.manager.service.RobotService;
import cn.exrick.manager.dto.SearchResultDTO;
import cn.exrick.manager.mapper.TbWalletMapper;
import cn.exrick.manager.pojo.TbWallet;
import cn.exrick.manager.pojo.TbWalletExample;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.sql.DataSource;

import cn.hutool.core.io.FileUtil;

@Component

public class AsyncEventPublisher {

//    @Autowired  
//    private ApplicationEventPublisher applicationEventPublisher;  

	@Autowired
	JedisClient jedisClient;

	@Resource
	RobotService robotService;

	@Resource
	TbWalletMapper tbWalletMapper;

	@Autowired
	@Lazy
	cn.exrick.manager.service.qq.MultiQQBotManager multiQQBotManager;

	@Autowired
	private DataSource dataSource;

	@Async
	public void publishEventAsync(String info, Update update, String sourceBot) {
		System.out.println("[publishEventAsync] 开始生成记事本，sourceBot=" + sourceBot);
		String re = robotService.getAllWork(update);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		String path = "/home/www/data/" + info + "_" + sdf.format(new Date()) + ".txt";
		// 生成记事本文件
		writeUtf8WithBom(re, path);
		// 获取群号（私聊失败时 fallback 发群里）
		Long groupChatId = null;
		if (update.getMessage() != null) {
			groupChatId = update.getMessage().getChatId();
		} else if (update.getChannelPost() != null) {
			groupChatId = update.getChannelPost().getChatId();
		}
		// 获取用户真实数字 ID（用于 bot 私聊兜底）
		Long userId = null;
		Integer messageId = null;
		Integer messageThreadId = null;
		if (update.getMessage() != null) {
			userId = update.getMessage().getFrom() != null ? update.getMessage().getFrom().getId() : null;
			messageId = update.getMessage().getMessageId();
			messageThreadId = update.getMessage().getMessageThreadId();
		} else if (update.getChannelPost() != null) {
			userId = update.getChannelPost().getFrom() != null ? update.getChannelPost().getFrom().getId() : null;
			messageId = update.getChannelPost().getMessageId();
			messageThreadId = update.getChannelPost().getMessageThreadId();
		}
		// 清理控制字符后推送（6字段：username_keyword,path,chatId,userId,messageId,messageThreadId）
		String queueData = info + "," + path;
		if (groupChatId != null) {
			queueData = queueData + "," + groupChatId;
		} else {
			queueData = queueData + ","; // 占位符，保持字段位置
		}
		if (userId != null) {
			queueData = queueData + "," + userId;
		} else {
			queueData = queueData + ",";
		}
		queueData = queueData + "," + (messageId != null ? messageId : "") + "," + (messageThreadId != null ? messageThreadId : "") + "," + (sourceBot != null ? sourceBot : "0");
		queueData = queueData.replaceAll("[\\n\\r\\t]", "");
		// 直接推入 senderbot 的 SQLite 队列，跳过 Redis → donw 脚本环节
		pushToSenderBotQueue(userId, path, info, sourceBot);
	}
	
	/**
	 * 直接写入 MySQL pending_uploads 表（同 donw + forward_sender_bot 共享数据库）
	 * 格式与 donwloadFileAndSendToUser.py 的 push_bot_upload 一致，
	 * forward_sender_bot.py 会直接读取并发送。
	 */
	private void pushToSenderBotQueue(Long userId, String filePath, String caption, String sourceBot) {
		if (userId == null) {
			System.out.println("[SenderBotQueue] 跳过：userId 为空");
			return;
		}
		try {
			try (Connection conn = dataSource.getConnection()) {
				// 插入任务（表已由 Python 创建）
				try (PreparedStatement ps = conn.prepareStatement(
						"INSERT INTO pending_uploads (chat_id, file_path, caption, source_bot, message_thread_id, status) " +
						"VALUES (?, ?, ?, ?, ?, 'pending')")) {
					ps.setString(1, String.valueOf(userId));
					ps.setString(2, filePath);
					ps.setString(3, caption);
					ps.setString(4, sourceBot != null ? sourceBot : "0");
					ps.setNull(5, java.sql.Types.INTEGER);
					ps.executeUpdate();
				}
				System.out.println("[SenderBotQueue] 已推入MySQL: chat=" + userId + ", file=" + filePath + ", sourceBot=" + sourceBot);
			}
		} catch (Exception e) {
			System.err.println("[SenderBotQueue] 推入失败: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 将文本内容转换为 HTML 网页
	 */
	private void generateHtmlFromText(String textContent, String path) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timestamp = sdf.format(new Date());
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
			writer.write("<!DOCTYPE html>\n<html lang=\"zh-CN\">\n<head>\n");
			writer.write("<meta charset=\"UTF-8\">\n");
			writer.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
			writer.write("<title>作品清单</title>\n");
			writer.write("<style>");
			writer.write("* { margin: 0; padding: 0; box-sizing: border-box; }");
			writer.write("body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%); min-height: 100vh; color: #fff; padding: 20px; }");
			writer.write(".container { max-width: 800px; margin: 0 auto; }");
			writer.write(".header { text-align: center; padding: 30px 0; border-bottom: 2px solid #e94560; margin-bottom: 30px; }");
			writer.write(".header h1 { font-size: 2em; margin-bottom: 10px; color: #e94560; }");
			writer.write(".content { background: rgba(255,255,255,0.05); padding: 30px; border-radius: 15px; line-height: 1.8; white-space: pre-wrap; font-family: monospace; }");
			writer.write(".footer { text-align: center; padding: 30px; color: #666; margin-top: 30px; }");
			writer.write("</style>\n</head>\n<body>\n");
			writer.write("<div class=\"container\">\n");
			writer.write("<div class=\"header\">\n");
			writer.write("<h1>🔐 作品清单</h1>\n");
			writer.write("<div>生成时间: " + timestamp + "</div>\n");
			writer.write("</div>\n");
			writer.write("<div class=\"content\">\n");
			writer.write(escapeHtml(textContent));
			writer.write("\n</div>\n");
			writer.write("<div class=\"footer\">\n");
			writer.write("<p>发送指令提取作品</p>\n");
			writer.write("</div>\n</div>\n");
			writer.write("</body>\n</html>");
		} catch (Exception e) {
			// 失败时回退到原来的文本文件
			writeUtf8WithBom(textContent, path.replace(".html", ".txt"));
		}
	}
	
	/**
	 * QQ Bot 搜索后生成网页文件并推送队列
	 * @param userId QQ 用户 ID
	 * @param keyword 搜索关键词
	 * @param result 搜索结果
	 * @param appId QQ Bot AppId
	 * @param clientSecret QQ Bot ClientSecret
	 */
	@Async
	public void publishQQBotSearchAsync(String userId, String keyword, SearchResultDTO result, String appId, String clientSecret) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		// 生成文件名
		String safeKeyword = keyword.replaceAll("[^\\w\\u4e00-\\u9fa5]", "_");
		if (safeKeyword.length() > 20) {
			safeKeyword = safeKeyword.substring(0, 20);
		}
		String fileName = "QQ_" + userId + "_" + safeKeyword + "_" + sdf.format(new Date()) + ".txt";
		String path = "/home/www/data/" + fileName;
		
		// 生成记事本文件
		generateTxtFile(path, keyword, result, userId);
		
		// 直接通过 QQ Bot API 发送文件给用户，不再经过 Redis → Python 中转
		try {
			java.io.File txtFile = new java.io.File(path);
			if (txtFile.exists()) {
				cn.exrick.manager.service.qq.QQBotClient botClient = multiQQBotManager.getClient(appId);
				if (botClient != null) {
					// 调试：打印文件前200字符查看内容
					try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(txtFile))) {
						String firstLine = br.readLine();
						System.out.println("[QQBotNotepad] 文件首行: " + (firstLine != null ? firstLine : "(null)"));
					} catch (Exception ig) {}
					boolean sent = botClient.sendFileToUser(userId, txtFile, "📄 作品搜索清单 - " + keyword);
					if (sent) {
						System.out.println("[QQBotNotepad] 直接发送成功: userId=" + userId + ", file=" + path);
					} else {
						System.err.println("[QQBotNotepad] 发送失败: userId=" + userId);
					}
				} else {
					System.err.println("[QQBotNotepad] 找不到 bot 客户端: appId=" + appId);
				}
			} else {
				System.err.println("[QQBotNotepad] 记事本文件不存在: " + path);
			}
		} catch (Exception e) {
			System.err.println("[QQBotNotepad] 发送异常: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 生成记事本文件
	 */
	private void generateTxtFile(String path, String keyword, SearchResultDTO result, String userId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timestamp = sdf.format(new Date());
		
		StringBuilder sb = new StringBuilder();
		sb.append("=======================================\r\n");
		sb.append("🔐 作品搜索清单\r\n");
		sb.append("=======================================\r\n");
		sb.append("搜索时间: ").append(timestamp).append("\r\n");
		sb.append("关键词: ").append(keyword).append("\r\n");
		sb.append("用户: ").append(userId).append("\r\n");
		sb.append("---------------------------------------\r\n");
		sb.append("网页: ").append(result.getZmqVideos() != null ? result.getZmqVideos().size() : 0).append(" 条\r\n");
		sb.append("最新: ").append(result.getWaiwang2Videos() != null ? result.getWaiwang2Videos().size() : 0).append(" 条\r\n");
		sb.append("玩物: ").append(result.getWanwuVideos() != null ? result.getWanwuVideos().size() : 0).append(" 条\r\n");
		sb.append("淘露: ").append(result.getTaolu3Videos() != null ? result.getTaolu3Videos().size() : 0).append(" 条\r\n");
		sb.append("外网: ").append(result.getWaiwangVideos() != null ? result.getWaiwangVideos().size() : 0).append(" 条\r\n");
		sb.append("频道: ").append(result.getChannelVideos() != null ? result.getChannelVideos().size() : 0).append(" 条\r\n");
		sb.append("总计: ").append(result.getTotalCount()).append(" 条\r\n");
		sb.append("=======================================\r\n\r\n");
		
		// 混合所有结果按时间倒序排列
		List<NotepadEntry> mixed = new ArrayList<>();
		addVideoEntries(mixed, "zm", result.getZmqVideos());
		addVideoEntries(mixed, "bc", result.getWaiwang2Videos());
		addVideoEntries(mixed, "ww", result.getWanwuVideos());
		addVideoEntries(mixed, "tl", result.getTaolu3Videos());
		addVideoEntries(mixed, "tg", result.getWaiwangVideos());
		addChannelEntries(mixed, result.getChannelVideos());
		mixed.sort((a, b) -> {
			if (a.time == null && b.time == null) return 0;
			if (a.time == null) return 1;
			if (b.time == null) return -1;
			return b.time.compareTo(a.time); // 倒序
		});
		writeMixedSectionTxt(sb, mixed);
		
		sb.append("\r\n=======================================\r\n");
		sb.append("发送指令（如 ww12345, zm12345）即可提取作品\r\n");
		sb.append("=======================================\r\n");
		
		writeUtf8WithBom(sb.toString(), path);
	}
	
	/**
	 * 写入视频分类区块（记事本格式）
	 */
	private void writeVideoSectionTxt(StringBuilder sb, String typeName, String cmd, List<?> videos) {
		if (videos == null || videos.isEmpty()) return;
		
		sb.append(typeName).append(" (").append(videos.size()).append(" 条)\r\n");
		sb.append("---------------------------------------\r\n");
		
		int num = 1;
		for (Object video : videos) {
			String vid = getVideoId(video);
			String title = getVideoTitle(video);
			String time = getVideoTime(video);
			String duration = getVideoDuration(video);
			String author = getVideoAuthor(video);
			sb.append(num).append(". ").append(title).append("\r\n");
			if (author != null && !author.isEmpty()) {
				sb.append("   作者: ").append(author).append("\r\n");
			}
			if (duration != null && !duration.isEmpty()) {
				sb.append("   时长: ").append(duration).append("\r\n");
			}
			if (time != null && !time.isEmpty()) {
				sb.append("   时间: ").append(time).append("\r\n");
			}
			sb.append("   指令: ").append(cmd).append(vid).append("\r\n\r\n");
			num++;
		}
		sb.append("\r\n");
	}
	
	/**
	 * 写入频道搜索结果区块（记事本格式）
	 */
	private void writeChannelSectionTxt(StringBuilder sb, String typeName, List<Map<String, Object>> videos) {
		if (videos == null || videos.isEmpty()) return;
		
		sb.append(typeName).append(" (").append(videos.size()).append(" 条)\r\n");
		sb.append("---------------------------------------\r\n");
		
		int num = 1;
		for (Map<String, Object> video : videos) {
			String title = (String) video.get("title");
			Long vid = (Long) video.get("id");
			String time = video.get("dt") != null ? video.get("dt").toString() : "";
			String duration = video.get("cc") != null ? video.get("cc").toString() : "";
			sb.append(num).append(". ").append(title).append("\r\n");
			if (!duration.isEmpty()) {
				sb.append("   时长: ").append(duration).append("分\r\n");
			}
			if (!time.isEmpty()) {
				sb.append("   时间: ").append(time).append("\r\n");
			}
			sb.append("   指令: ch").append(vid).append("\r\n\r\n");
			num++;
		}
		sb.append("\r\n");
	}
	
	/**
	 * 生成 HTML 网页文件（已废弃，保留兼容性）
	 */
	private void generateHtmlFile(String path, String keyword, SearchResultDTO result, String userId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timestamp = sdf.format(new Date());
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
			writer.write("<!DOCTYPE html>\n<html lang=\"zh-CN\">\n<head>\n");
			writer.write("<meta charset=\"UTF-8\">\n");
			writer.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
			writer.write("<title>作品清单 - " + keyword + "</title>\n");
			writer.write("<style>");
			writer.write("* { margin: 0; padding: 0; box-sizing: border-box; }");
			writer.write("body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%); min-height: 100vh; color: #fff; }");
			writer.write(".container { max-width: 1400px; margin: 0 auto; padding: 20px; }");
			writer.write(".header { text-align: center; padding: 30px 0; border-bottom: 2px solid #e94560; margin-bottom: 30px; }");
			writer.write(".header h1 { font-size: 2.5em; margin-bottom: 10px; background: linear-gradient(45deg, #e94560, #ff6b6b); -webkit-background-clip: text; -webkit-text-fill-color: transparent; }");
			writer.write(".search-info { color: #aaa; font-size: 0.9em; margin-top: 10px; }");
			writer.write(".stats { display: flex; justify-content: center; gap: 20px; margin: 20px 0; flex-wrap: wrap; }");
			writer.write(".stat-item { background: rgba(233, 69, 96, 0.2); padding: 10px 20px; border-radius: 20px; border: 1px solid #e94560; font-size: 0.9em; }");
			writer.write(".type-section { margin-bottom: 50px; }");
			writer.write(".type-header { display: flex; align-items: center; gap: 15px; margin-bottom: 25px; padding: 20px; background: rgba(255,255,255,0.05); border-radius: 15px; border-left: 5px solid #e94560; }");
			writer.write(".type-badge { background: #e94560; color: #fff; padding: 8px 20px; border-radius: 25px; font-weight: bold; font-size: 1em; }");
			writer.write(".type-count { color: #aaa; font-size: 1em; }");
			writer.write(".video-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); gap: 25px; }");
			writer.write(".video-card { background: rgba(255,255,255,0.05); border-radius: 15px; overflow: hidden; transition: all 0.3s; border: 1px solid rgba(255,255,255,0.1); }");
			writer.write(".video-card:hover { transform: translateY(-8px); box-shadow: 0 15px 40px rgba(233, 69, 96, 0.4); }");
			writer.write(".video-cover { width: 100%; height: 300px; object-fit: cover; background: #2a2a4a; transition: opacity 0.3s; }");
			writer.write(".video-cover.lazyload { opacity: 0; }");
			writer.write(".video-cover.lazyloaded { opacity: 1; }");
			writer.write(".video-info { padding: 20px; }");
			writer.write(".video-title { font-size: 1em; line-height: 1.6; margin-bottom: 15px; display: -webkit-box; -webkit-line-clamp: 3; -webkit-box-orient: vertical; overflow: hidden; color: #fff; min-height: 75px; }");
			writer.write(".video-command { display: inline-block; background: linear-gradient(45deg, #e94560, #ff6b6b); color: #fff; padding: 8px 20px; border-radius: 25px; font-size: 0.95em; font-weight: bold; cursor: pointer; transition: all 0.2s; }");
			writer.write(".video-command:hover { transform: scale(1.05); box-shadow: 0 5px 20px rgba(233, 69, 96, 0.4); }");
			writer.write(".copy-hint { font-size: 0.8em; color: #888; margin-top: 10px; }");
			writer.write(".footer { text-align: center; padding: 40px; color: #666; border-top: 1px solid rgba(255,255,255,0.1); margin-top: 60px; }");
			writer.write("@media (max-width: 768px) { .video-grid { grid-template-columns: repeat(2, 1fr); } .video-cover { height: 200px; } .header h1 { font-size: 1.8em; } }");
			writer.write("@media (max-width: 480px) { .video-grid { grid-template-columns: 1fr; } }");
			writer.write("</style>\n</head>\n<body>\n");
			writer.write("<div class=\"container\">\n");
			writer.write("<div class=\"header\">\n");
			writer.write("<h1>🔐 作品搜索清单</h1>\n");
			writer.write("<div class=\"search-info\">搜索时间: " + timestamp + " | 关键词: " + keyword + " | 用户: " + userId + "</div>\n");
			writer.write("<div class=\"stats\">\n");
			writer.write("<div class=\"stat-item\">网页 " + (result.getZmqVideos() != null ? result.getZmqVideos().size() : 0) + " 条</div>\n");
			writer.write("<div class=\"stat-item\">最新 " + (result.getWaiwang2Videos() != null ? result.getWaiwang2Videos().size() : 0) + " 条</div>\n");
			writer.write("<div class=\"stat-item\">玩物 " + (result.getWanwuVideos() != null ? result.getWanwuVideos().size() : 0) + " 条</div>\n");
			writer.write("<div class=\"stat-item\">淘露 " + (result.getTaolu3Videos() != null ? result.getTaolu3Videos().size() : 0) + " 条</div>\n");
			writer.write("<div class=\"stat-item\">外网 " + (result.getWaiwangVideos() != null ? result.getWaiwangVideos().size() : 0) + " 条</div>\n");
			writer.write("<div class=\"stat-item\">总计 " + result.getTotalCount() + " 条</div>\n");
			writer.write("</div>\n</div>\n");
			
			// 各类结果
			writeVideoSection(writer, "网页", "zm", result.getZmqVideos(), "#3498db");
			writeVideoSection(writer, "最新", "bc", result.getWaiwang2Videos(), "#e74c3c");
			writeVideoSection(writer, "玩物", "ww", result.getWanwuVideos(), "#2ecc71");
			writeVideoSection(writer, "淘露", "tl", result.getTaolu3Videos(), "#f39c12");
			writeVideoSection(writer, "外网", "tg", result.getWaiwangVideos(), "#9b59b6");
			
			writer.write("<div class=\"footer\">\n");
			writer.write("<p>发送指令（如 ww12345, zm12345）即可提取作品</p>\n");
			writer.write("</div>\n</div>\n");
			writer.write("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/lazysizes/5.3.2/lazysizes.min.js\" async></script>\n");
			writer.write("<script>");
			writer.write("document.querySelectorAll('.video-command').forEach(cmd => {");
			writer.write("cmd.addEventListener('click', function() {");
			writer.write("const text = this.textContent;");
			writer.write("if (navigator.clipboard) {");
			writer.write("navigator.clipboard.writeText(text).then(() => {");
			writer.write("this.style.background = '#27ae60';");
			writer.write("setTimeout(() => this.style.background = '', 500);");
			writer.write("});");
			writer.write("}");
			writer.write("});");
			writer.write("});");
			writer.write("</script>\n</body>\n</html>");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 写入视频分类区块
	 */
	private void writeVideoSection(BufferedWriter writer, String typeName, String cmd, List<?> videos, String color) throws Exception {
		if (videos == null || videos.isEmpty()) return;
		
		writer.write("<div class=\"type-section\">\n");
		writer.write("<div class=\"type-header\">\n");
		writer.write("<span class=\"type-badge\" style=\"background: " + color + "\">" + typeName + "</span>\n");
		writer.write("<span class=\"type-count\">" + videos.size() + " 条结果</span>\n");
		writer.write("</div>\n");
		writer.write("<div class=\"video-grid\">\n");
		
		for (Object video : videos) {
			String vid = getVideoId(video);
			String title = getVideoTitle(video);
			String cover = getVideoCover(video);
			
			writer.write("<div class=\"video-card\">\n");
			writer.write("<img class=\"video-cover lazyload\" data-src=\"" + cover + "\" src=\"data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==\" alt=\"封面\">\n");
			writer.write("<div class=\"video-info\">\n");
			writer.write("<div class=\"video-title\">" + escapeHtml(title) + "</div>\n");
			writer.write("<span class=\"video-command\">" + cmd + vid + "</span>\n");
			writer.write("<div class=\"copy-hint\">点击复制指令</div>\n");
			writer.write("</div>\n</div>\n");
		}
		
		writer.write("</div>\n</div>\n");
	}
	
	/**
	 * 获取视频封面
	 */
	private String getVideoCover(Object video) {
		if (video instanceof cn.exrick.manager.pojo.ZmqVideo) {
			String cover = ((cn.exrick.manager.pojo.ZmqVideo) video).getCover();
			return cover != null ? cover : "";
		}
		if (video instanceof cn.exrick.manager.pojo.Waiwang2Video) {
			String cover = ((cn.exrick.manager.pojo.Waiwang2Video) video).getCover();
			return cover != null ? cover : "";
		}
		if (video instanceof cn.exrick.manager.pojo.WanwuVideo) {
			String cover = ((cn.exrick.manager.pojo.WanwuVideo) video).getCover();
			return cover != null ? cover : "";
		}
		if (video instanceof cn.exrick.manager.pojo.Taolu3Video) {
			String cover = ((cn.exrick.manager.pojo.Taolu3Video) video).getCover();
			return cover != null ? cover : "";
		}
		if (video instanceof cn.exrick.manager.pojo.WaiwangVideo) {
			// WaiwangVideo 没有 cover 字段，返回空
			return "";
		}
		return "";
	}
	
	/**
	 * HTML 转义
	 */
	private String escapeHtml(String text) {
		if (text == null) return "";
		return text.replace("&", "&amp;")
				   .replace("<", "&lt;")
				   .replace(">", "&gt;")
				   .replace("\"", "&quot;")
				   .replace("'", "&#x27;");
	}
	
	/**
	 * 写入带 BOM 的 UTF-8 文件（兼容安卓记事本）
	 * @param content 文件内容
	 * @param path 文件路径
	 */
	private void writeUtf8WithBom(String content, String path) {
		try (FileOutputStream fos = new FileOutputStream(path);
			 OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
			// 写入 UTF-8 BOM (EF BB BF)
			fos.write(0xEF);
			fos.write(0xBB);
			fos.write(0xBF);
			// 写入内容
			writer.write(content);
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// 失败时使用 hutool 的默认方法
			FileUtil.writeUtf8String(content, path);
		}
	}
	
	private void appendSearchResult(StringBuilder sb, String sectionName, java.util.List<?> videos, String cmd) {
		if (videos == null || videos.isEmpty()) {
			return;
		}
		
		sb.append(sectionName).append(" ").append(videos.size()).append("条\r\n");
		
		int index = 1;
		for (Object video : videos) {
			String vid = getVideoId(video);
			String title = getVideoTitle(video);
			String author = getVideoAuthor(video);
			String duration = getVideoDuration(video);
			String time = getVideoTime(video);
			
			sb.append(index).append(". ").append(title != null ? title : "无标题").append("\r\n");
			sb.append("   指令: ").append(cmd).append(vid).append("\r\n");
			if (author != null && !author.isEmpty()) {
				sb.append("   作者: ").append(author).append("\r\n");
			}
			if (duration != null && !duration.isEmpty()) {
				sb.append("   时长: ").append(duration).append("\r\n");
			}
			if (time != null && !time.isEmpty()) {
				sb.append("   时间: ").append(time).append("\r\n");
			}
			sb.append("\r\n");
			index++;
		}
		sb.append("\r\n");
	}
	
	private String getVideoId(Object video) {
		if (video instanceof cn.exrick.manager.pojo.ZmqVideo) return String.valueOf(((cn.exrick.manager.pojo.ZmqVideo) video).getVid());
		if (video instanceof cn.exrick.manager.pojo.Waiwang2Video) return String.valueOf(((cn.exrick.manager.pojo.Waiwang2Video) video).getId());
		if (video instanceof cn.exrick.manager.pojo.WanwuVideo) return String.valueOf(((cn.exrick.manager.pojo.WanwuVideo) video).getVid());
		if (video instanceof cn.exrick.manager.pojo.Taolu3Video) return String.valueOf(((cn.exrick.manager.pojo.Taolu3Video) video).getVid());
		if (video instanceof cn.exrick.manager.pojo.WaiwangVideo) return String.valueOf(((cn.exrick.manager.pojo.WaiwangVideo) video).getId());
		return "";
	}
	
	private String getVideoTitle(Object video) {
		if (video instanceof cn.exrick.manager.pojo.ZmqVideo) return ((cn.exrick.manager.pojo.ZmqVideo) video).getTitle();
		if (video instanceof cn.exrick.manager.pojo.Waiwang2Video) return ((cn.exrick.manager.pojo.Waiwang2Video) video).getTitle();
		if (video instanceof cn.exrick.manager.pojo.WanwuVideo) return ((cn.exrick.manager.pojo.WanwuVideo) video).getTitle();
		if (video instanceof cn.exrick.manager.pojo.Taolu3Video) return ((cn.exrick.manager.pojo.Taolu3Video) video).getTitle();
		if (video instanceof cn.exrick.manager.pojo.WaiwangVideo) return ((cn.exrick.manager.pojo.WaiwangVideo) video).getTitle();
		return "";
	}
	
	private String getVideoAuthor(Object video) {
		if (video instanceof cn.exrick.manager.pojo.ZmqVideo) return "";
		if (video instanceof cn.exrick.manager.pojo.Waiwang2Video) return ((cn.exrick.manager.pojo.Waiwang2Video) video).getNickname();
		if (video instanceof cn.exrick.manager.pojo.WanwuVideo) return ((cn.exrick.manager.pojo.WanwuVideo) video).getUrlkey2();
		if (video instanceof cn.exrick.manager.pojo.Taolu3Video) return ((cn.exrick.manager.pojo.Taolu3Video) video).getUrlkey2();
		if (video instanceof cn.exrick.manager.pojo.WaiwangVideo) return "";
		return "";
	}
	
	private String getVideoTime(Object video) {
		if (video instanceof cn.exrick.manager.pojo.ZmqVideo) return ((cn.exrick.manager.pojo.ZmqVideo) video).getDt();
		if (video instanceof cn.exrick.manager.pojo.Waiwang2Video) return ((cn.exrick.manager.pojo.Waiwang2Video) video).getDt();
		if (video instanceof cn.exrick.manager.pojo.WanwuVideo) return ((cn.exrick.manager.pojo.WanwuVideo) video).getAddtime();
		if (video instanceof cn.exrick.manager.pojo.Taolu3Video) return ((cn.exrick.manager.pojo.Taolu3Video) video).getDt();
		if (video instanceof cn.exrick.manager.pojo.WaiwangVideo) return ((cn.exrick.manager.pojo.WaiwangVideo) video).getDt();
		return "";
	}

	private String getVideoDuration(Object video) {
		if (video instanceof cn.exrick.manager.pojo.ZmqVideo) return ((cn.exrick.manager.pojo.ZmqVideo) video).getDuration();
		if (video instanceof cn.exrick.manager.pojo.Waiwang2Video) return ((cn.exrick.manager.pojo.Waiwang2Video) video).getDuration() + "分";
		if (video instanceof cn.exrick.manager.pojo.WanwuVideo) return ((cn.exrick.manager.pojo.WanwuVideo) video).getDuration();
		if (video instanceof cn.exrick.manager.pojo.Taolu3Video) return "";
		if (video instanceof cn.exrick.manager.pojo.WaiwangVideo) return secondsToHMS(((cn.exrick.manager.pojo.WaiwangVideo) video).getDuration());
		return "";
	}

	/**
	 * 秒数 → HH:MM:SS 格式
	 */
	private String secondsToHMS(String secondsStr) {
		if (secondsStr == null || secondsStr.isEmpty()) return "";
		try {
			int sec = Integer.parseInt(secondsStr);
			int h = sec / 3600;
			int m = (sec % 3600) / 60;
			int s = sec % 60;
			if (h > 0) return String.format("%d:%02d:%02d", h, m, s);
			return String.format("%d:%02d", m, s);
		} catch (NumberFormatException e) {
			return secondsStr;
		}
	}

	// ==================== 混合排序 ====================

	private static class NotepadEntry {
		String cmd;
		String vid;
		String title;
		String time;
		String duration;
		String author;
	}

	private void addVideoEntries(List<NotepadEntry> list, String cmd, List<?> videos) {
		if (videos == null) return;
		for (Object v : videos) {
			NotepadEntry e = new NotepadEntry();
			e.cmd = cmd;
			e.vid = getVideoId(v);
			e.title = getVideoTitle(v);
			e.time = getVideoTime(v);
			e.duration = getVideoDuration(v);
			e.author = getVideoAuthor(v);
			list.add(e);
		}
	}

	private void addChannelEntries(List<NotepadEntry> list, List<Map<String, Object>> videos) {
		if (videos == null) return;
		for (Map<String, Object> v : videos) {
			NotepadEntry e = new NotepadEntry();
			e.cmd = "ch";
			Object idObj = v.get("id");
			e.vid = idObj != null ? idObj.toString() : "";
			e.title = (String) v.get("title");
			Object dtObj = v.get("dt");
			e.time = dtObj != null ? dtObj.toString() : "";
			Object ccObj = v.get("cc");
			e.duration = ccObj != null ? ccObj.toString() : "";
			list.add(e);
		}
	}

	private void writeMixedSectionTxt(StringBuilder sb, List<NotepadEntry> list) {
		if (list.isEmpty()) return;
		int num = 1;
		for (NotepadEntry e : list) {
			String cleanTitle = e.title.replace("\n", "").replace("\r", "");
			sb.append(num).append(". ").append(cleanTitle).append("\r\n");
			if (e.author != null && !e.author.isEmpty()) {
				sb.append("   作者: ").append(e.author).append("\r\n");
			}
			if (e.duration != null && !e.duration.isEmpty()) {
				sb.append("   时长: ").append(e.duration).append("\r\n");
			}
			if (e.time != null && !e.time.isEmpty()) {
				sb.append("   时间: ").append(e.time).append("\r\n");
			}
			sb.append("   指令: ").append(e.cmd).append(e.vid).append("\r\n\r\n");
			num++;
		}
		sb.append("\r\n");
	}

}