package cn.exrick.manager.service.task;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.exrick.common.jedis.JedisClient;
import cn.exrick.manager.pojo.Taolu3Video;
import cn.exrick.manager.pojo.Waiwang2Video;
import cn.exrick.manager.pojo.WaiwangVideo;
import cn.exrick.manager.pojo.WanwuVideo;
import cn.exrick.manager.pojo.ZmqVideo;
import cn.exrick.manager.service.RobotService;;

@Component

public class VideoUoadCheck {

//	@Reference
//	@Qualifier("fundServiceKongImpl")
//	private FundKongService caiService;
//	AlarmPlayer alarmPlayer = new AlarmPlayer();

//	@Autowired
//	AlarmPlayer alarmPlayer;

	@Reference
	RobotService robotService;

	@Autowired
	JedisClient jedisClient;
	// key命名规范 项目名称
	private static final String PRIVATE_HASH_KEY_ARTICLES = "postc_set"; // 评论编号
	private static final String PRIVATE_HASH_KEY_ARTICLE_USERS = "postc_user_like_set"; // 评论踩人集合
//	private static final String PRIVATE_HASH_KEY_ARTICLE_USER_INFO = "post_user_like"; // 评论踩人踩详情
//	private static final String PRIVATE_HASH_KEY_POST_COUNTER = "post_counter"; // 缓存里的评论踩数

	@Scheduled(cron = "0 */1 * * * ?")
	public void run() {
//		System.out.println("=== 任务执行开始 ===");
//		System.out.println("线程: " + Thread.currentThread().getName());

		// 打印调用堆栈
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		for (StackTraceElement element : stackTrace) {
			if (element.getClassName().contains("scheduling")) {
				System.out.println("调度相关: " + element);
			}
		}
//		while (true) {

		String listKey = "videotoupload";
		// 获取列表长度
		long listLength = jedisClient.llen(listKey);
		System.out.println("列表长度: " + listLength);

		// 遍历列表
		for (int i = 0; i < listLength; i++) {
			try {
				String item = jedisClient.lindex(listKey, i);
				if (item == null)
					continue;
//				System.out.println("索引 " + i + ": " + item);
				// 对所有数据查询是否有数据库的连接，有连接放入新数据到队列，发给用户
				String[] itemStrings = item.split(",");
				String vid = itemStrings[1];
				int len = itemStrings.length;
//				System.out.println("len:" + len);

				if (vid.startsWith("bc")) {
					Waiwang2Video video = robotService.getVideoWW2(vid.substring(2));
					if (len == 4) {
						String skString = itemStrings[3];
//						video.setAuthor(skString);
						robotService.updateLocation(video.getVid() + "", skString);
						video.setPantag(skString);
						// System.out.println("更新成功");
					}
					if (video != null && video.getPantag() != null && video.getPantag().indexOf("http") != -1) {
						// jedisClient.lpush("videos", itemStrings[0] + "," + video.getPantag());
						jedisClient.lrem(listKey, 1, item);

						String title = itemStrings[0].substring(itemStrings[0].indexOf("_") + 1);
						if (itemStrings.length == 3) {
							title = itemStrings[2].substring(itemStrings[2].lastIndexOf("\\") + 1);

						}
						File file = new File(
								"K:\\upload\\Telegram_Restricted_Media_Downloader-main\\" + title + ".mp4");

//						File file = new File(itemStrings[2]);
						try {
							file.delete();
						} catch (Exception e) {
							e.printStackTrace();
							// TODO: handle exception
						}

					}
				} else if (vid.startsWith("tl")) {
					Taolu3Video video = robotService.getVideotl(vid.substring(2));
					if (len == 4) {
						String skString = itemStrings[3];
//						video.setAuthor(skString);
						robotService.updateLocationtl(vid.substring(2), skString);
						video.setTria(skString);
					}
					if (video != null && video.getTria() != null && video.getTria().indexOf("http") != -1) {
						// jedisClient.lpush("videos", itemStrings[0] + "," + video.getTria());
						jedisClient.lrem(listKey, 1, item);
						String title = itemStrings[0].substring(itemStrings[0].indexOf("_") + 1);

						File file = new File(
								"K:\\upload\\Telegram_Restricted_Media_Downloader-main\\" + title + ".mp4");
						File file2 = new File(
								"K:\\upload\\Telegram_Restricted_Media_Downloader-main\\Downloads\\" + title + ".mp4");

						try {
							if (file.exists())
								file.delete();
							if (file2.exists())
								file2.delete();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				} else if (vid.startsWith("zm")) {
					ZmqVideo video = robotService.getVideoZmq(vid.substring(2));
					if (len == 4) {
						String skString = itemStrings[3];
//						video.setAuthor(skString);
						robotService.updateLocationzmq(vid.substring(2), skString);
						video.setTria(skString);
					}
					if (video != null && video.getTria() != null && video.getTria().indexOf("http") != -1) {
						/// jedisClient.lpush("videos", itemStrings[0] + "," + video.getTria());
						jedisClient.lrem(listKey, 1, item);
						String title = itemStrings[0].substring(itemStrings[0].indexOf("_") + 1);

						File file = new File(
								"K:\\upload\\Telegram_Restricted_Media_Downloader-main\\" + title + ".mp4");
						File file2 = new File(
								"K:\\upload\\Telegram_Restricted_Media_Downloader-main\\Downloads\\" + title + ".mp4");

						try {
							if (file.exists())
								file.delete();
							if (file2.exists())
								file2.delete();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}

				else if (vid.startsWith("ww")) {
					WanwuVideo video = robotService.getVideoWw(vid.substring(2));
					if (len == 4) {
						String skString = itemStrings[3];
//						video.setAuthor(skString);
						robotService.updateLocationww(vid.substring(2), skString);
						video.setTria(skString);
					}
					if (video != null && video.getTria() != null && video.getTria().indexOf("http") != -1) {
						// jedisClient.lpush("videos", itemStrings[0] + "," + video.getTria());
						jedisClient.lrem(listKey, 1, item);
						String title = itemStrings[0].substring(itemStrings[0].indexOf("_") + 1);

						File file = new File(
								"K:\\upload\\Telegram_Restricted_Media_Downloader-main\\" + title + ".mp4");
						File file2 = new File(
								"K:\\upload\\Telegram_Restricted_Media_Downloader-main\\Downloads\\" + title + ".mp4");

						try {
							if (file.exists())
								file.delete();
							if (file2.exists())
								file2.delete();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}

				else if (vid.startsWith("tg")) {
					WaiwangVideo video = robotService.getVideoTG(vid.substring(2));
					if (len == 4) {
						String skString = itemStrings[3];
//						video.setAuthor(skString);
						robotService.updateLocationTG(vid.substring(2), skString);
						video.setAuthor(skString);
					}
					if (video != null && video.getAuthor() != null && video.getAuthor().indexOf("http") != -1) {
						// jedisClient.lpush("videos", itemStrings[0] + "," + video.getAuthor());
						jedisClient.lrem(listKey, 1, item);
						String title = itemStrings[0].substring(itemStrings[0].indexOf("_") + 1);

						File file = new File(
								"K:\\upload\\Telegram_Restricted_Media_Downloader-main\\" + title + ".mp4");
						File file2 = new File(
								"K:\\upload\\Telegram_Restricted_Media_Downloader-main\\Downloads\\" + title + ".mp4");

						try {
							if (file.exists())
								file.delete();
							if (file2.exists())
								file2.delete();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

		}

//		}

	}

}
