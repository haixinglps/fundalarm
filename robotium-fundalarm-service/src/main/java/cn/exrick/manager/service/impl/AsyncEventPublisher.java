package cn.exrick.manager.service.impl;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

//import com.insurance.order.domain.TInsuredOrderEvent;
//import com.insurance.order.service.NoticeService;

import cn.exrick.common.jedis.JedisClient;
import cn.exrick.manager.service.RobotService;
import cn.exrick.manager.dto.SearchResultDTO;
import cn.hutool.core.io.FileUtil;

@Component

public class AsyncEventPublisher {

//    @Autowired  
//    private ApplicationEventPublisher applicationEventPublisher;  

	@Autowired
	JedisClient jedisClient;

	@Resource
	RobotService robotService;

	@Async
	public void publishEventAsync(String info, Update update) {
		String re = robotService.getAllWork(update);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		String path = "/home/www/data/" + info + "_" + sdf.format(new Date()) + ".txt";
		// 写入带 BOM 的 UTF-8 文件（安卓兼容）
		writeUtf8WithBom(re, path);
		// 清理控制字符后推送
		String queueData = info + "," + path;
		queueData = queueData.replaceAll("[\\n\\r\\t]", "");
		jedisClient.rpush("videos", queueData);

	}
	
	/**
	 * QQ Bot 搜索后生成记事本并推送队列
	 * @param userId QQ 用户 ID
	 * @param keyword 搜索关键词
	 * @param result 搜索结果
	 * @param appId QQ Bot AppId
	 * @param clientSecret QQ Bot ClientSecret
	 */
	@Async
	public void publishQQBotSearchAsync(String userId, String keyword, SearchResultDTO result, String appId, String clientSecret) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		// 生成记事本内容
		StringBuilder re = new StringBuilder();
		re.append("🔐 QQ Bot 加密搜索 - 作品清单\n");
		re.append("==============================\n");
		re.append("搜索时间: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n");
		re.append("关键词: ").append(keyword).append("\n");
		re.append("总结果: ").append(result.getTotalCount()).append(" 条\n\n");
		
		// 各类结果
		appendSearchResult(re, "【网页搜索】", result.getZmqVideos(), "zm");
		appendSearchResult(re, "【最新作品】", result.getWaiwang2Videos(), "bc");
		appendSearchResult(re, "【玩物搜索】", result.getWanwuVideos(), "ww");
		appendSearchResult(re, "【淘露搜索】", result.getTaolu3Videos(), "tl");
		appendSearchResult(re, "【外网搜索】", result.getWaiwangVideos(), "tg");
		
		re.append("==============================\n");
		re.append("使用指令提取作品:\n");
		re.append("• ww + ID - 玩物\n");
		re.append("• tl + ID - 淘露\n");
		re.append("• bc + ID - 本地\n");
		re.append("• zm + ID - 网页\n");
		re.append("• tg + ID - 电报\n");
		
		// 生成文件名（使用 QQ_ 前缀区分）
		String safeKeyword = keyword.replaceAll("[^\\w\\u4e00-\\u9fa5]", "_");
		if (safeKeyword.length() > 20) {
			safeKeyword = safeKeyword.substring(0, 20);
		}
		String fileName = "QQ_" + userId + "_" + safeKeyword + "_" + sdf.format(new Date()) + ".txt";
		String path = "/home/www/data/" + fileName;
		
		// 写入带 BOM 的 UTF-8 文件（安卓兼容）
		writeUtf8WithBom(re.toString(), path);
		
		// 推送到队列（格式与作品任务一致，15个字段）
		// 字段: userId, url(文件路径), title, vid, chatid, cover, byString, tgLink, zhindex, author, vip, appId, clientSecret, feijiUser, feijiPass
		StringBuilder sb = new StringBuilder();
		sb.append(userId).append(",");           // [0] userId
		sb.append(path).append(",");              // [1] url (文件路径作为url)
		sb.append("QQ_").append(safeKeyword).append(","); // [2] title
		sb.append("notebook").append(",");        // [3] vid (标记为记事本)
		sb.append(userId).append(",");            // [4] chatid
		sb.append("").append(",");                // [5] cover
		sb.append("").append(",");                // [6] byString
		sb.append("").append(",");                // [7] tgLink
		sb.append("0").append(",");               // [8] zhindex
		sb.append("").append(",");                // [9] author
		sb.append("3").append(",");               // [10] vip (QQ用户固定为3)
		sb.append(appId != null ? appId : "").append(",");      // [11] appId
		sb.append(clientSecret != null ? clientSecret : "").append(","); // [12] clientSecret
		sb.append("").append(",");                // [13] feijiUser
		sb.append("");                             // [14] feijiPass
		
		String queueData = sb.toString();
		// 清理所有控制字符
		queueData = queueData.replaceAll("[\\n\\r\\t]", "");
		jedisClient.rpush("videos", queueData);
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
		
		sb.append(sectionName).append(" ").append(videos.size()).append("条\n");
		
		int index = 1;
		for (Object video : videos) {
			String vid = getVideoId(video);
			String title = getVideoTitle(video);
			String author = getVideoAuthor(video);
			String duration = getVideoDuration(video);
			
			sb.append(index).append(". ").append(title != null ? title : "无标题").append("\n");
			sb.append("   指令: ").append(cmd).append(vid).append("\n");
			if (author != null && !author.isEmpty()) {
				sb.append("   作者: ").append(author).append("\n");
			}
			if (duration != null && !duration.isEmpty()) {
				sb.append("   时长: ").append(duration).append("\n");
			}
			sb.append("\n");
			index++;
		}
		sb.append("\n");
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
	
	private String getVideoDuration(Object video) {
		if (video instanceof cn.exrick.manager.pojo.ZmqVideo) return ((cn.exrick.manager.pojo.ZmqVideo) video).getDuration();
		if (video instanceof cn.exrick.manager.pojo.Waiwang2Video) return ((cn.exrick.manager.pojo.Waiwang2Video) video).getDuration() + "分";
		if (video instanceof cn.exrick.manager.pojo.WanwuVideo) return ((cn.exrick.manager.pojo.WanwuVideo) video).getDuration();
		if (video instanceof cn.exrick.manager.pojo.Taolu3Video) return "";
		if (video instanceof cn.exrick.manager.pojo.WaiwangVideo) return "";
		return "";
	}

}