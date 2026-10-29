package cn.exrick.manager.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhongsou.search.core.query.Hit;
import com.zhongsou.search.core.query.Hits;

import cn.exrick.common.exception.XmallException;
import cn.exrick.common.jedis.JedisClient;
import cn.exrick.manager.dto.SearchResultDTO;
import cn.exrick.manager.event.VideoPushEvent;
import cn.exrick.manager.isearch.Isearch;
import cn.exrick.manager.isearch.query.Search;
import cn.exrick.manager.isearch.query.SearchFactory;
import cn.exrick.manager.isearch.util.ArticleUtil;
import cn.exrick.manager.mapper.Fund1Gaoduanzhuangbei2OkMapper;
import cn.exrick.manager.mapper.FundMapper;
import cn.exrick.manager.mapper.Taolu3VideoMapper;
import cn.exrick.manager.mapper.TbDouyinGzMapper;
import cn.exrick.manager.mapper.TbDouyinMessageMapper;
import cn.exrick.manager.mapper.TbUserMapper;
import cn.exrick.manager.mapper.TbWalletMapper;
import cn.exrick.manager.mapper.Waiwang2VideoMapper;
import cn.exrick.manager.mapper.WaiwangVideoMapper;
import cn.exrick.manager.mapper.WanwuMapper;
import cn.exrick.manager.mapper.WanwuVideoMapper;
import cn.exrick.manager.mapper.ZmqVideoMapper;
import cn.exrick.manager.pojo.Taolu3Video;
import cn.exrick.manager.pojo.Taolu3VideoExample;
import cn.exrick.manager.pojo.TbWallet;
import cn.exrick.manager.pojo.TbWalletExample;
import cn.exrick.manager.pojo.Waiwang2Video;
import cn.exrick.manager.pojo.Waiwang2VideoExample;
import cn.exrick.manager.pojo.Waiwang2VideoExample.Criteria;
import cn.exrick.manager.pojo.WaiwangVideo;
import cn.exrick.manager.pojo.WaiwangVideoExample;
import cn.exrick.manager.pojo.Wanwu;
import cn.exrick.manager.pojo.WanwuVideo;
import cn.exrick.manager.pojo.WanwuVideoExample;
import cn.exrick.manager.pojo.ZmqVideo;
import cn.exrick.manager.pojo.ZmqVideoExample;
import cn.exrick.manager.service.OkxService;
import cn.exrick.manager.service.RobotService;
import cn.exrick.manager.service.tg.TelegramChannelMonitor;
import cn.exrick.manager.service.tg.TelegramDeepLink;
import cn.exrick.manager.service.tg.UrlValidator;
import cn.exrick.manager.service.util.getSign;

@Service
public class RobotServiceImpl implements RobotService {

	// 2026-10-28 动态定价（角单位，1元=10角）。存量余额已×10迁移，新用户默认50角=5元免费额度（仅普通档可用）
	private static final int NORMAL_DEDUCT_JIAO = 30;   // 普通已缓存资源 30角=3元（维持原1次=3元定价）

	/** 冷门资源(tria=sk)扣款：原价(price金币≈元) + 1元利润，单位角；库价缺失按3金币计 */
	private static int coldDeductJiao(Integer price) {
		int p = (price != null && price > 0) ? price : 3;
		return (p + 1) * 10;
	}

	/** 2026-10-29：vid 非数字（rec 录制任务/带前缀）返回 null，防搜索链路 parseInt 雪崩 */
	private static Integer parseVidOrNull(String vid) {
		try {
			return Integer.parseInt(vid);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	private static final Logger log = LoggerFactory.getLogger(RobotServiceImpl.class);
	@Autowired
	private TbUserMapper tbUserMapper;

	String beiyongUrl = "https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png";

	@Autowired
	private JedisClient jedisClient;
	@Value("${NOTIFYURL}")
	private String NOTIFYURL;
	@Value("${RETURN_URL}")
	private String RETURN_URL;
	@Value("${NOTIFY_URL}")
	private String NOTIFY_URL;
	@Value("${MCH_NAME}")
	private String MCH_NAME;
	@Value("${telegram.bot.username}")
	private String telegramBotUsername;
	@Value("${telegram.bot2.username}")
	private String telegramBot2Username;
	@Value("${extract.daily.limit:1000}")
	private int extractDailyLimit;
	@Autowired
	private TbDouyinGzMapper tbDouyinGzMapper;
	@Autowired
	private TbDouyinMessageMapper tbDouyinMessageMapper;
	@Autowired
	private Fund1Gaoduanzhuangbei2OkMapper fund1Gaoduanzhuangbei2OkMapper;
	@Autowired
	private FundMapper fundMapper;
	@Autowired
	private OkxService okxService;

	@Autowired
	private WanwuMapper wanwuMapper;

	@Autowired
	private WanwuVideoMapper wanwuVideoMapper;
	@Autowired
	private Taolu3VideoMapper taolu3VideoMapper;
	@Autowired
	private WaiwangVideoMapper waiwangVideoMapper;

	@Autowired
	private Waiwang2VideoMapper waiwang2VideoMapper;
	@Autowired
	private ZmqVideoMapper zmqVideoMapper;

	@Lazy
	@Autowired
	TelegramChannelMonitor telegramChannelMonitor;

	@Lazy
	@Resource
	AsyncEventPublisher publisher;

	@Autowired
	private VideoPushEventService videoPushEventService;

	// 由 TelegramBotConfig 手动设置，解决循环依赖
	public static void setCurrentBot(TelegramChannelMonitor monitor) {
		CURRENT_BOT.set(monitor);
	}

	public static void clearCurrentBot() {
		CURRENT_BOT.remove();
	}

	private TelegramChannelMonitor getSender() {
		TelegramChannelMonitor current = CURRENT_BOT.get();
		String botName = current != null ? current.getBotUsername() : telegramChannelMonitor.getBotUsername();
		System.out.println("[getSender] currentBot=" + botName + ", thread=" + Thread.currentThread().getName());
		return current != null ? current : telegramChannelMonitor;
	}

	/**
	 * 写入视频推送事件表并发布 Spring 事件，由监听器在业务事务提交后推入 Redis。
	 * 注意：outbox 写入必须和业务事务同成败，所以异常直接向上抛出。
	 * <p>
	 * 由共用的 {@link VideoPushEventService} 实现，便于 QQ Bot、GroupNotepad 等模块复用。
	 */
	private void publishVideoPushEvent(String vid, String userId, Long sourceChatId, String sourceBot, String taskInfo, String queueName) {
		videoPushEventService.publish(vid, userId, sourceChatId, sourceBot, taskInfo, queueName);
	}

	private String resolveSourceBot(Long chatId) {
		TelegramChannelMonitor current = CURRENT_BOT.get();
		if (current != null) {
			String botUsername = current.getBotUsername();
			System.out.println("[resolveSourceBot] currentBot=" + botUsername + ", chatId=" + chatId);
			if (botUsername != null && botUsername.equals(telegramBotUsername)) {
				return "0";
			} else if (botUsername != null && botUsername.equals(telegramBot2Username)) {
				return "3";
			} else {
				// 会员bot返回自己的完整token，便于下游用自己的bot发送
				return current.getBotToken();
			}
		}
		System.out.println("[resolveSourceBot] currentBot=null, chatId=" + chatId + ", fallback to chatId");
		if (chatId != null) {
			if (chatId.equals(-1003867299066L)) return "0";
			if (chatId.equals(-1003992613609L)) return "3";
		}
		// 默认按搜索bot/会员bot处理
		return "3";
	}

	@Override
	public String handleZhiboCommand(int shortId, String identifier, Long userId) {
		String url = null;
		String title = null;
		String coverString = null;
		String author = null;
		String avatarUrl = null;
		String roomId = null;
		int videoDbId = 0;
		boolean isHls = false;
		boolean apiOk = false;
		String apiErr = null;
		org.json.JSONObject json = null;

		// 每次调用都通过 Python 获取当前真实直播间，避免复用旧记录的 room_id / bc{id}
		for (int attempt = 0; attempt < 2; attempt++) {
			try {
				Process process = Runtime.getRuntime().exec(new String[]{
						"python3", "/home/www/code/ww/get_room_by_shortid.py", String.valueOf(shortId)
				});
				process.waitFor();
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				StringBuilder output = new StringBuilder();
				String lastLine = "";
				String line;
				while ((line = reader.readLine()) != null) {
					output.append(line);
					lastLine = line;
				}
				json = new org.json.JSONObject(lastLine.isEmpty() ? output.toString() : lastLine);
				if (json.getInt("code") == 0) {
					String tmpUrl = json.getString("pull_url");
					boolean tmpHls = json.optBoolean("is_hls", false);
					// RTMP 直播校验链接有效性，HLS 由 hls_recorder 自行处理
					if (!tmpHls && !UrlValidator.validateWithFFmpeg(tmpUrl)) {
						System.out.println("[ZB_API] RTMP失效，尝试重新获取: shortId=" + shortId + ", attempt=" + attempt);
						apiErr = "RTMP链接无效";
						continue;
					}
					url = tmpUrl;
					title = json.getString("title");
					coverString = json.optString("cover", "");
					author = json.optString("nickname", "");
					avatarUrl = json.optString("photo", "");
					isHls = tmpHls;
					roomId = String.valueOf(json.getInt("room_id"));
					apiOk = true;
					System.out.println("[ZB_API] API获取成功: room=" + roomId + ", title=" + title + ", isHls=" + isHls);
					break;
				} else {
					apiErr = json.optString("error", "未知错误");
					System.out.println("[ZB_API] API获取失败: " + apiErr);
					break; // API 已返回明确错误，不再重试
				}
			} catch (Exception e) {
				e.printStackTrace();
				apiErr = e.getMessage();
				System.out.println("[ZB_API] 调用API异常: " + apiErr);
				break;
			}
		}
		if (!apiOk) {
			if (apiErr != null && (apiErr.contains("未开播") || apiErr.contains("已下播") || apiErr.contains("RTMP链接无效"))) {
				return "主播还未开播或直播已结束";
			}
			return "id可能有误：" + (apiErr != null ? apiErr : "未知错误");
		}

		// Python 脚本 get_room_by_shortid.py 在 INSERT 后已查询自增 ID
		// 并随 JSON 返回 video_db_id 字段，Java 直接读取，避免跨连接查询不一致
		videoDbId = json.optInt("video_db_id", 0);
		System.out.println("[ZB_API] waiwang2_video id=" + videoDbId + ", vid=" + roomId + " (from Python)");
		if (videoDbId == 0) {
			return "创建直播记录失败，未获取到自增ID";
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String tmStr = sdf.format(new Date());
		String roomTitleOnly = title.contains("_") ? title.substring(0, title.indexOf("_")) : title;
		String safeTitle = roomTitleOnly.replaceAll("[^\\w\\u4e00-\\u9fa5-]", "").replaceAll("[-_]{2,}", "_");
		String safeNick = (author != null ? author : "").replaceAll("[^\\w\\u4e00-\\u9fa5-]", "").replaceAll("[-_]{2,}", "_");
		if (safeNick.isEmpty()) {
			safeNick = "unknown";
		}
		if (safeTitle.isEmpty()) {
			safeTitle = safeNick;
		}
		String tt = safeTitle + "_" + safeNick + "_" + tmStr;
		String up = "/root/data/disk/" + tt + ".mp4";
		// Telegram 用户传入真实 UID，否则保持 135 占位（livemonitor 等非 Telegram 来源）
		String targetUid = (userId != null && userId > 0) ? String.valueOf(userId) : "135";
		String info = identifier + "," + up + "," + tt + ",bc" + videoDbId + "," + targetUid + "," + (coverString != null ? coverString : "") + "," + up + ",0," + (author != null ? author : "") + ",0,1";
		String message = url + "," + info;

		boolean existsInQueue = false;
		String recordingKey = "recording_room_" + roomId;
		try {
			long lockResult = jedisClient.setnx(recordingKey, "1");
			if (lockResult == 0L) {
				existsInQueue = true;
				System.out.println("[ZB_LUZHI] Redis SETNX发现重复: room=" + roomId);
			} else {
				jedisClient.expire(recordingKey, 3600);
				// 按 roomId 去重，避免同一直播间重复录制
				String roomMarker = "room_" + roomId;
				String targetQueue = isHls ? "luzhi_hls" : "luzhi";
				String targetBakQueue = isHls ? "luzhi_hls_bak" : "luzhi_bak";
				List<String> luzhiItems = jedisClient.lrange(targetQueue, 0, -1);
				List<String> luzhiBakItems = jedisClient.lrange(targetBakQueue, 0, -1);
				for (String qi : luzhiItems) {
					if (qi == null || !qi.contains(roomMarker)) continue;
					existsInQueue = true;
					break;
				}
				if (!existsInQueue) {
					for (String qi : luzhiBakItems) {
						if (qi == null || !qi.contains(roomMarker)) continue;
						existsInQueue = true;
						break;
					}
				}
				if (existsInQueue) {
					jedisClient.del(recordingKey);
				}
			}
		} catch (Exception e) {
			System.out.println("[ZB_LUZHI] 去重检查异常: " + e.getMessage());
		}

		if (existsInQueue) {
			System.out.println("[ZB_LUZHI] 队列中已存在相同直播间，跳过: room=" + roomId);
			return "🎥 该直播间已在录制队列中，请稍候。\n\n标题: " + title;
		}
		String targetQueue = isHls ? "luzhi_hls" : "luzhi";
		jedisClient.lpush(targetQueue, message);
		System.out.println("[ZB_LUZHI] 推入" + (isHls ? "HLS" : "RTMP") + "录制队列: room=" + roomId + ", bc" + videoDbId + ", nick=" + author + ", user=" + identifier);
		return "🎥 主播正在直播中，已开始" + (isHls ? "HLS" : "RTMP") + "录制！\n\n标题: " + title + "\n\n录制完成后会自动发送视频文件，请稍候。";
	}

	public void setTelegramChannelMonitor(TelegramChannelMonitor monitor) {
		this.telegramChannelMonitor = monitor;
	}

	private static final ThreadLocal<TelegramChannelMonitor> CURRENT_BOT = new ThreadLocal<>();

	@Autowired
	private TbWalletMapper tbWalletMapper;

	@Override
	@Transactional
	public void dealSearch(Update update) {
		try {
		dealSearchInternal(update);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("【dealSearch 异常】" + e.getMessage());
		}
	}
	
	private void dealSearchInternal(Update update) {
		Isearch search = null;
		// 检查搜索引擎是否可用
		if (SearchFactory.isIsearchAvailable()) {
			try {
				search = new Isearch(Search.SORT_DATE, Search.CACHE_MODE_BASE);
			} catch (Exception e2) {
				log.warn("创建 Isearch 失败，将使用数据库搜索: {}", e2.getMessage());
			}
		} else {
			log.info("搜索引擎不可用，使用数据库搜索");
		}

		List<InputMedia> mediaList = new ArrayList<InputMedia>();
// 		mediaList.add(new InputMediaPhoto("图片1_URL").setCaption("描述1"));

		// 搜索数据库
		Message channelMsg = update.getChannelPost();
		if (channelMsg == null)
			channelMsg = update.getMessage();
		System.out.println(channelMsg.getChat().getUserName());
		Long chatId = channelMsg.getChatId(); // 频道ID

		String text = channelMsg.getText(); // 消息文本

		System.out.println("新消息b: " + channelMsg.getText());
		// 处理媒体消息
		if (channelMsg.hasPhoto()) {
			System.out.println("检测到图片附件");
		}
		StringBuffer re = new StringBuffer();
		if (text.contentEquals("最新作品")) {
			text = null;
		}

		// 发送加载动画消息（跳过，避免卡住）
		System.out.println("开始搜索...");
		int messageId = 0;
		System.out.println("开始搜索 zmq...");

		try {
		ZmqVideoExample exampleWaiwangzmq = new ZmqVideoExample();
		cn.exrick.manager.pojo.ZmqVideoExample.Criteria criteriazmq = exampleWaiwangzmq.createCriteria();
		if (text != null) {
			if (!text.trim().isEmpty()) {
					String[] textArr = text.trim().split("\\p{javaWhitespace}+");
					for (String item : textArr) {
						if (item.length() > 0) {
							criteriazmq.andTitleLike("%" + item + "%");
						}
					}
				}

		}
//		criteriaWw2.andTitleNotEqualTo("");
		criteriazmq.andDurationIsNotNull();
		exampleWaiwangzmq.setOrderByClause("addtime desc");
		PageHelper.startPage(1, 5);
		List<ZmqVideo> listWaiwangzmq = zmqVideoMapper.selectByExample(exampleWaiwangzmq);
		removeDeadWckbot(listWaiwangzmq);   // 2026-10-28 wckbot 站报废：type91-99 且 tria=sk 不出现在搜索
		PageInfo<ZmqVideo> pizmqInfo = new PageInfo<ZmqVideo>(listWaiwangzmq);
		System.out.println("zmq查询完成，结果数: " + listWaiwangzmq.size() + ", 总数: " + pizmqInfo.getTotal());

		re.append("网页搜索 " + pizmqInfo.getTotal() + "条\r\n");
		mediaList = new ArrayList<InputMedia>();

		for (int i = 0; i < listWaiwangzmq.size(); i++) {
			String link = TelegramDeepLink.generateLink(getSender().getBotUsername(),
					"zm" + listWaiwangzmq.get(i).getVid());
			String tt = cleanSearchTitle(listWaiwangzmq.get(i).getTitle());
//			if (tt.length() > 60)
//				tt = tt.substring(0, 60);
			// re.append(i + "\t" + tt + "\t" + "机器人口令：bc" + listWaiwang2.get(i).getId() +
			// "\t" + link + "\n");
			if (listWaiwangzmq.get(i).getCover() == null)
				listWaiwangzmq.get(i).setCover(
						"https://s3imgqnv1.ikzuo.com/app/user/117431_20250825050414_8c56e54391d88227e0081a266509c952.png?imageView2/2/w/56");
			InputMediaPhoto photo = new InputMediaPhoto(listWaiwangzmq.get(i).getCover());
			String zmqTag = getVipFreeTag(listWaiwangzmq.get(i));
			photo.setCaption(i + "\t" + zmqTag + tt + "\t时长：" + listWaiwangzmq.get(i).getDuration() + "\t" + "机器人口令：zm"
					+ listWaiwangzmq.get(i).getVid() + "\t" + link); // 每张图片独立描述
			mediaList.add(photo);
//			if (!UrlValidator.isUrlValid(listWaiwang2.get(i).getCover())) {
//
//				listWaiwang2.get(i).setCover(beiyongUrl);
//				re.append(photo.getCaption() + "\n");
//			} else {

			int robotRe = getSender().sendChannelReplyWithPhoto(chatId, mediaList.get(i).getCaption(),
					channelMsg.getMessageId(), listWaiwangzmq.get(i).getCover());
//			try {
////				Thread.sleep(5000);
//			} catch (InterruptedException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
//			}
			if (robotRe == 0)
				re.append(photo.getCaption() + "\r\n");
//			}

		}
		} catch (Exception e) {
			System.err.println("【搜索异常】zmq查询或发送失败: " + e.getMessage());
			e.printStackTrace();
		}

		Waiwang2VideoExample exampleWaiwang2 = new Waiwang2VideoExample();
		cn.exrick.manager.pojo.Waiwang2VideoExample.Criteria criteriaWw2 = exampleWaiwang2.createCriteria();
		if (text != null) {
			if (!text.trim().isEmpty()) {
					String[] textArr = text.trim().split("\\p{javaWhitespace}+");
					for (String item : textArr) {
						if (item.length() > 0) {
							criteriaWw2.andTitleLike("%" + item + "%");
						}
					}
				}

		}
//		criteriaWw2.andTitleNotEqualTo("");
		criteriaWw2.andDurationIsNotNull();
		// 排除 pantag 不含 http 的结果
		criteriaWw2.andPantagLike("%http%");
		exampleWaiwang2.setOrderByClause("dt desc");
		PageHelper.startPage(1, 5);
		List<Waiwang2Video> listWaiwang2 = waiwang2VideoMapper.selectByExample(exampleWaiwang2);
		PageInfo<Waiwang2Video> pibcInfo = new PageInfo<Waiwang2Video>(listWaiwang2);

		re.append("最新作品搜索 " + pibcInfo.getTotal() + "条\r\n");
		mediaList = new ArrayList<InputMedia>();

		for (int i = 0; i < listWaiwang2.size(); i++) {
			String link = TelegramDeepLink.generateLink(getSender().getBotUsername(),
					"bc" + listWaiwang2.get(i).getId());
			String tt = cleanSearchTitle(listWaiwang2.get(i).getTitle()).split("_")[0];
//			if (tt.length() > 60)
//				tt = tt.substring(0, 60);
			// re.append(i + "\t" + tt + "\t" + "机器人口令：bc" + listWaiwang2.get(i).getId() +
			// "\t" + link + "\n");
			if (listWaiwang2.get(i).getCover() == null)
				listWaiwang2.get(i).setCover(
						"https://s3imgqnv1.ikzuo.com/app/user/117431_20250825050414_8c56e54391d88227e0081a266509c952.png?imageView2/2/w/56");
			InputMediaPhoto photo = new InputMediaPhoto(listWaiwang2.get(i).getCover());
			String bcTag = getVipFreeTag(listWaiwang2.get(i));
			photo.setCaption(i + "\t" + bcTag + tt + "\tid:" + listWaiwang2.get(i).getUid() + "\t作者："
					+ listWaiwang2.get(i).getNickname() + "\t时长：" + listWaiwang2.get(i).getDuration() + "分\t时间："
					+ listWaiwang2.get(i).getDt() + "\t" + "机器人口令：bc" + listWaiwang2.get(i).getId() + "\t" + link); // 每张图片独立描述
			mediaList.add(photo);
//			if (!UrlValidator.isUrlValid(listWaiwang2.get(i).getCover())) {
//
//				listWaiwang2.get(i).setCover(beiyongUrl);
//				re.append(photo.getCaption() + "\n");
//			} else {

			int robotRe = getSender().sendChannelReplyWithPhoto(chatId, mediaList.get(i).getCaption(),
					channelMsg.getMessageId(), listWaiwang2.get(i).getCover());
//			try {
////				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			if (robotRe == 0)
				re.append(photo.getCaption() + "\r\n");
//			}

		}

		WanwuVideoExample example = new WanwuVideoExample();

		cn.exrick.manager.pojo.WanwuVideoExample.Criteria criteria = example.createCriteria();
		if (text != null) {
			if (!text.trim().isEmpty()) {
					String[] textArr = text.trim().split("\\p{javaWhitespace}+");
					for (String item : textArr) {
						if (item.length() > 0) {
							criteria.andTitleLike("%" + item + "%");
						}
					}
				}
		}
//		criteriaTaolu.andTitleNotEqualTo("");

		example.setOrderByClause("addtime desc");
		PageHelper.startPage(1, 5);
		List<WanwuVideo> list = wanwuVideoMapper.selectByExample(example);
		PageInfo<WanwuVideo> piwwInfo = new PageInfo<WanwuVideo>(list);

		re.append("玩物搜索 " + piwwInfo.getTotal() + "条（只返回前5条，如果需要所有的找客服要）\r\n");
		mediaList = new ArrayList<InputMedia>();

		for (int i = 0; i < list.size(); i++) {
			String link = TelegramDeepLink.generateLink(getSender().getBotUsername(),
					"ww" + list.get(i).getVid());

			// 检查封面是否为空
			String cover = list.get(i).getCover();
			if (cover == null || cover.trim().isEmpty()) {
				cover = "https://s3imgqnv1.ikzuo.com/app/user/117431_20250825050414_8c56e54391d88227e0081a266509c952.png?imageView2/2/w/56";
			}

			InputMediaPhoto photo = new InputMediaPhoto(cover);
			String wwTag = getVipFreeTag(list.get(i));
			photo.setCaption(i + "\t" + wwTag + cleanSearchTitle(list.get(i).getTitle()).split("_")[0] + "\tid:" + list.get(i).getAuthor()
					+ "\t作者：" + list.get(i).getUrlkey2() + "\t时长:" + list.get(i).getDuration() + "\t时间："
					+ list.get(i).getAddtime() + "\t" + "机器人口令：ww" + list.get(i).getVid() + "\t" + link + ""); // 每张图片独立描述
			// photo.setParseMode("HTML");

			// 设置消息实体，禁用网页预览
//			List<MessageEntity> entities = new ArrayList<>();
//			MessageEntity urlEntity = new MessageEntity();
//			urlEntity.setType("text_link");
//			urlEntity.setOffset(photo.getCaption().indexOf(link));
//			urlEntity.setLength(link.length());
//			urlEntity.setUrl(link);
//			entities.add(urlEntity);
//			photo.setCaptionEntities(entities);
			mediaList.add(photo);

//			if (!UrlValidator.isUrlValid(list.get(i).getCover())) {
////
//				list.get(i).setCover(beiyongUrl);
//				re.append(photo.getCaption() + "\n");
//
//			} else {
//			System.out.println("--------------------index:" + i + " " + list.get(i).getCover());

			int robotResult = getSender().sendChannelReplyWithPhoto(chatId, mediaList.get(i).getCaption(),
					channelMsg.getMessageId(), list.get(i).getCover());

			if (robotResult == 0)
				re.append(photo.getCaption() + "\r\n");
//			}

		}
//		if (mediaList.size() > 0) {
//			if (mediaList.size() >= 2)
//				telegramChannelMonitor.sendPhotos(chatId, channelMsg.getMessageId(), mediaList);
//			else {
//				telegramChannelMonitor.sendChannelReplyWithPhoto(chatId, mediaList.get(0).getCaption(),
//						channelMsg.getMessageId(), list.get(0).getCover());
//
//			}
//		}
//		try {
//			updateSearchStatus(chatId, messageId, "🛒 正在搜索淘露...");
//		} catch (TelegramApiException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Taolu3VideoExample exampleTaolu = new Taolu3VideoExample();
		cn.exrick.manager.pojo.Taolu3VideoExample.Criteria criteriaTaolu = exampleTaolu.createCriteria();
		if (text != null) {
			if (!text.trim().isEmpty()) {
					String[] textArr = text.trim().split("\\p{javaWhitespace}+");
					for (String item : textArr) {
						if (item.length() > 0) {
							criteriaTaolu.andTitleLike("%" + item + "%");
						}
					}
				}

		}
//		criteriaTaolu.andTitleNotEqualTo("");

		exampleTaolu.setOrderByClause("dt desc");
		PageHelper.startPage(1, 5);
		List<Taolu3Video> listTaolu = taolu3VideoMapper.selectByExample(exampleTaolu);
		PageInfo<Taolu3Video> pitlInfo = new PageInfo<Taolu3Video>(listTaolu);

		re.append("淘露搜索 " + pitlInfo.getTotal() + "条（部分不能播放的url找客服要mp4，新作品也找客服要）\r\n");
		mediaList = new ArrayList<InputMedia>();

		for (int i = 0; i < listTaolu.size(); i++) {
			String link = TelegramDeepLink.generateLink(getSender().getBotUsername(),
					"tl" + listTaolu.get(i).getVid());
			// 检查封面是否为空
			String cover = listTaolu.get(i).getCover();
			if (cover == null || cover.trim().isEmpty()) {
				cover = "https://s3imgqnv1.ikzuo.com/app/user/117431_20250825050414_8c56e54391d88227e0081a266509c952.png?imageView2/2/w/56";
			}
			InputMediaPhoto photo = new InputMediaPhoto(cover);
			String tlTag = getVipFreeTag(listTaolu.get(i));
			photo.setCaption(i + "\t" + tlTag + cleanSearchTitle(listTaolu.get(i).getTitle()).split("_")[0] + "\tid:"
					+ listTaolu.get(i).getAuthor() + "\t作者：" + listTaolu.get(i).getUrlkey2() + "\t时间："
					+ listTaolu.get(i).getDt() + "\t" + "机器人口令：tl" + listTaolu.get(i).getVid() + "\t" + link); // 每张图片独立描述
			mediaList.add(photo);

//			if (!UrlValidator.isUrlValid(listTaolu.get(i).getCover())) {
//
//				listTaolu.get(i).setCover(beiyongUrl);
//				re.append(i + "\t" + listTaolu.get(i).getTitle() + "\t" + listTaolu.get(i).getVid() + "\t" + "机器人口令：tl"
//						+ listTaolu.get(i).getVid() + "\t" + link + "\n");
//			} else {

			int robotRe = getSender().sendChannelReplyWithPhoto(chatId, mediaList.get(i).getCaption(),
					channelMsg.getMessageId(), listTaolu.get(i).getCover());

			if (robotRe == 0)
				re.append(photo.getCaption() + "\r\n");
//			}
		}
//		if (mediaList.size() > 0) {
//			if (mediaList.size() >= 2)
//				telegramChannelMonitor.sendPhotos(chatId, channelMsg.getMessageId(), mediaList);
//			else {
//				telegramChannelMonitor.sendChannelReplyWithPhoto(chatId, mediaList.get(0).getCaption(),
//						channelMsg.getMessageId(), listTaolu.get(0).getCover());
//			}
//
//		}
//		try {
//			updateSearchStatus(chatId, messageId, "🛒 正在搜索电报各大频道...");
//		} catch (TelegramApiException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		WaiwangVideoExample exampleWaiwang = new WaiwangVideoExample();
		cn.exrick.manager.pojo.WaiwangVideoExample.Criteria criteriaWw = exampleWaiwang.createCriteria();
		if (text != null) {
			if (!text.trim().isEmpty()) {
					String[] textArr = text.trim().split("\\p{javaWhitespace}+");
					for (String item : textArr) {
						if (item.length() > 0) {
							criteriaWw.andTitleLike("%" + item + "%");
						}
					}
				}

		}
		criteriaWw.andTitleNotEqualTo("");
		criteriaWw.andDurationGreaterThan("60");
		// 排除 friendindex = 1 的记录
		criteriaWw.andFriendindexNotEqualTo(1);
		exampleWaiwang.setOrderByClause("dt desc");
		PageHelper.startPage(1, 5);
		List<WaiwangVideo> listWaiwang = waiwangVideoMapper.selectByExample(exampleWaiwang);
		PageInfo<WaiwangVideo> pitgInfo = new PageInfo<WaiwangVideo>(listWaiwang);

		re.append("电报搜索 " + pitgInfo.getTotal() + "条\r\n");
		mediaList = new ArrayList<InputMedia>();
		StringBuffer ttt = new StringBuffer();

		for (int i = 0; i < listWaiwang.size(); i++) {
			String link = TelegramDeepLink.generateLink(getSender().getBotUsername(),
					"tg" + listWaiwang.get(i).getId());
			String tt = cleanSearchTitle(listWaiwang.get(i).getTitle());
			if (tt.length() > 60)
				tt = tt.substring(0, 60);
			String duration = listWaiwang.get(i).getDuration();
			int sec = Integer.parseInt(duration);
			int min = sec / 60;
			int second = sec % 60;
			String tgTag = getVipFreeTag(listWaiwang.get(i));
			re.append(i + "\t" + tgTag + tt + "\t时间：" + listWaiwang.get(i).getDt() + "\t时长：" + min + ":" + second + "分 \t"
					+ "机器人口令：tg" + listWaiwang.get(i).getId() + "\t" + link + "\r\n");

			InputMediaPhoto photo = new InputMediaPhoto();
			photo.setCaption(i + "\t" + tgTag + tt + "\t" + listWaiwang.get(i).getDuration() + "秒\t"
					+ listWaiwang.get(i).getDt() + "\t" + "机器人口令：tg" + listWaiwang.get(i).getId() + "\t" + link); // 每张图片独立描述
			mediaList.add(photo);

//			int robotRe = getSender().sendChannelReplyWithPhoto(chatId, photo.getCaption(),
//					channelMsg.getMessageId(), "6127453610561357706");
//			if (robotRe == 0) {
////				re.append(i + "\t" + tt + "\t" + listWaiwang.get(i).getDt() + "\t" + "机器人口令：tg" + listWaiwang.get(i).getId()
////						+ "\t" + link + "\n");
//			}

		}
		if (mediaList.size() > 0) {
			// String vr = escapeMarkdown(ttt.toString());

//			telegramChannelMonitor.sendPhotos(chatId, channelMsg.getMessageId(), mediaList);
			// telegramChannelMonitor.sendChannelReply(chatId, vr,
			// channelMsg.getMessageId());
		}

		Hits hits = null;
		// 只有当 search 成功创建后才执行 isearch 搜索
		if (search != null) {
			try {
				// DM字段过滤已移除
				search.andText("TX", text);
				// 使用 notIndexedText 过滤 CH 字段
				search.andNotIndexedText("CH", "kaikai");
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.YEAR, -10);
				search.greatThan("RQ", calendar.getTimeInMillis());
				search.setMaxResults(10);
				hits = search.queryHits();
			} catch (Exception e1) {
				log.warn("Isearch 查询失败: {}", e1.getMessage());
			}
		}

		if (hits != null) {
			re.append("频道搜索 " + hits.getTotal() + "条\r\n");
			mediaList = new ArrayList<InputMedia>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			for (int i = 0; i < hits.size(); i++) {
				Hit hit = hits.get(i);
				String link = TelegramDeepLink.generateLink(getSender().getBotUsername(),
						"ch" + hit.getId());
				String tt = ArticleUtil.buildChannelTitle(hit.getArticle());
				long rq = hit.getArticle().getLong("RQ");
				String dt = sdf.format(new Date(rq));
				java.util.Map<String, Object> chMap = new java.util.HashMap<>();
				chMap.put("url", hit.getArticle().getString("UR"));
				String chTag = getVipFreeTagForChannel(chMap);

				re.append(i + "\t" + chTag + tt + "\t时间：" + dt + "\t时长：" + hit.getArticle().getString("CC") + "分 \t"
						+ "机器人口令：ch" + hit.getId() + "\t" + link + "\r\n");

//				InputMediaPhoto photo = new InputMediaPhoto();
//				photo.setCaption(i + "\t" + tt + "\t" + listWaiwang.get(i).getDuration() + "秒\t"
//						+ dt + "\t" + "机器人口令：ch" + hit.getId() + "\t" + link); // 每张图片独立描述
//				mediaList.add(photo);

//			int robotRe = getSender().sendChannelReplyWithPhoto(chatId, photo.getCaption(),
//					channelMsg.getMessageId(), "6127453610561357706");
//			if (robotRe == 0) {
////				re.append(i + "\t" + tt + "\t" + listWaiwang.get(i).getDt() + "\t" + "机器人口令：tg" + listWaiwang.get(i).getId()
////						+ "\t" + link + "\n");
//			}

			}

		}

//		try {
//			updateSearchStatus(chatId, messageId, "🛒 正在搜索本地库...");
//		} catch (TelegramApiException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		if (mediaList.size() > 0) {
//			if (mediaList.size() >= 2)
//				telegramChannelMonitor.sendPhotos(chatId, channelMsg.getMessageId(), mediaList);
//			else {
//				telegramChannelMonitor.sendChannelReplyWithPhoto(chatId, mediaList.get(0).getCaption(),
//						channelMsg.getMessageId(), listWaiwang2.get(0).getCover());
//			}
//
//		}
		// 跳过删除提示（未发送）
		String responseT = re.toString();
		String validRes = responseT;

		// 如果没有搜索结果，发送提示
		if (validRes.trim().isEmpty()) {
			validRes = "❌ 未找到相关作品: " + text;
		}

//		telegramChannelMonitor.sendChannelReply(chatId, validRes, channelMsg.getMessageId());

		int maxLength = 4096;
		for (int i = 0; i < validRes.length(); i += maxLength) {
			String chunk = validRes.substring(i, Math.min(i + maxLength, validRes.length()));
//			SendMessage message = new SendMessage(chatId.toString(), chunk);

			getSender().sendChannelReply(chatId, chunk, channelMsg.getMessageId());

//		 telegramChannelMonitor.sendChannelReply(chatId, validRes,
//		 channelMsg.getMessageId());

		}
		if (text == null) {
			text = "";
		}
		String info;
		if (channelMsg.getFrom().getUserName() != null) {
			info = channelMsg.getFrom().getUserName() + "_" + text.replace("_", "").replace(" ", "").replace("\n", "").replace("\r", "").replace("\t", "").replace("/", "");
		} else {
			info = "uid" + channelMsg.getFrom().getId() + "_" + text.replace("_", "").replace(" ", "").replace("\n", "").replace("\r", "").replace("\t", "").replace("/", "");
		}
		// 注册事务提交后的回调
		org.springframework.transaction.support.TransactionSynchronizationManager
				.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						// 使用异步服务发送消息
						// 推送数据到redis队列里进行计算。

						String sourceBot = resolveSourceBot(chatId);
		System.out.println("[DEBUG] 直接调用 publishEventAsync 生成记事本，sourceBot=" + sourceBot);
		publisher.publishEventAsync(info, update, sourceBot);
//						jedisClient.lpush("videos", info);
					}
				});

		return;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void dealGetWork(Update update) {

		//
		Integer messageThreadId = null;
		// 确保更新包含消息
		if (!update.getMessage().hasText()) {
			// System.out.println("没有文本");
			return;
		}

		Message receivedMessage = update.getMessage();
		Long chatId = receivedMessage.getChatId();
		String sourceBotForQQ = resolveSourceBot(chatId);
		String rechargeQQ = "0".equals(sourceBotForQQ) ? "2167485304" : "3097758477";

		// https://t.me/c/3867299066/206 https://t.me/c/3867299066/206/417
		// ========== 1. 判断是否为群组/频道 ==========
		Chat chat = receivedMessage.getChat();

		// 群组类型: "group"(普通群), "supergroup"(超级群), "channel"(频道), "private"(私聊)
		String chatType = chat.getType();
		System.out.println("type3:---------------------:lps::::" + chatType);

		// 支持私聊和超级群组
		boolean isGroup = "supergroup".equals(chatType) || "group".equals(chatType);
		boolean isPrivate = "private".equals(chatType);
		
		if (!isGroup && !isPrivate) {
			System.out.println("不是支持的群组/私聊类型，忽略: " + chatType);
			return;
		}
		
		if (isPrivate) {
			System.out.println("发现私聊消息，用户ID: " + chatId);
		} else {
			System.out.println("发现群类型:" + chatType);
		}

		int vip = 0;

		// ========== 2. 判断是否为指定群组 ==========
		Long targetGroupId = -1003867299066L; // 386 VIP群
		Long targetGroupId399 = -1003992613609L; // 399群
		int groupok = 0;
		int topicok = 0;
		// \u4f1a\u5458\u7fa4\uff08386\u3001399\uff09\u53ef\u4ee5\u5728\u7279\u5b9a\u8bdd\u9898\u63d0\u53d6\uff0c\u5176\u4ed6\u7fa4\u53ea\u80fd\u641c\u7d22
		if (isGroup && !chatId.equals(targetGroupId) && !chatId.equals(targetGroupId399)) {
			System.out.println("\u3010\u975e\u4f1a\u5458\u7fa4\u3011chatId=" + chatId + " \u53ea\u8d70\u641c\u7d22\uff0c\u4e0d\u63d0\u53d6");
			dealSearch(update);
			return;
		}
		if (chatId.equals(targetGroupId)) {
			groupok = 1;
		}

		// ========== 3. 判断是否为话题消息及话题ID ==========
		// 关键：message_thread_id 字段标识话题
		messageThreadId = receivedMessage.getMessageThreadId();

		if (groupok == 1) {

			if (messageThreadId == null) {
				System.out.println("这是普通消息（非话题）");
			} else {
				System.out.println("话题IDs: " + messageThreadId);

				// 判断是否为指定话题
				Integer targetTopicId = 206; // 替换为目标话题ID
				Integer xfjht = 2564; // 替换为目标话题ID
				System.out.println("messageThreadId=" + messageThreadId + ", xfjht=" + xfjht);
				System.out.println("equals结果: " + (messageThreadId.intValue() == xfjht.intValue()));

				if (messageThreadId.equals(targetTopicId)) {
					System.out.println("✅ 来自目标群组的目标话题，必须响应。");
					topicok = 1;
				} else if (messageThreadId.intValue() == xfjht.intValue()) {
					System.out.println("✅ 来自目标群组的 小飞机网盘 话题，必须响应。");
					topicok = 2;
				}

				else {

					System.out.println("来自目标群组的其他话题,不响应。");
					// return;
				}
			}

		} else if (chatId.equals(targetGroupId399)) {
			// 399 群：话题1视为搜索话题；普通消息（非话题）TelegramChannelMonitor 也视为目标话题，需保持一致，走每日10次限制
			if (messageThreadId == null || messageThreadId.intValue() == 1) {
				System.out.println("✅ 来自399群目标话题，必须响应。");
				topicok = 1;
			}
		}
		if (groupok == 1 && topicok == 0) {
			System.out.println("私有群其他话题不处理");

			return;
		}
		// 正常扣费，如果topicok==1 代表vip，不扣费

//		access_hash = receivedMessage.getFrom().g   # Bot API 直接给
		String userName = receivedMessage.getFrom().getUserName();
		String receivedText = receivedMessage.getText();
		String identifier;
		if (receivedMessage.getFrom().getUserName() != null) {
			identifier = "@" + receivedMessage.getFrom().getUserName();
		} else {
			identifier = receivedMessage.getFrom().getFirstName() + receivedMessage.getFrom().getLastName() + " (ID:"
					+ receivedMessage.getFrom().getId() + ")";
		}

		System.out.println("消息来自: " + identifier);

		// 查询余额

		Long uid = receivedMessage.getFrom().getId();

		// 1. 创建回复消息实例
		SendMessage replyMessage = new SendMessage();
		replyMessage.setChatId(chatId.toString()); // 设置回复对象

		// ⚠️ 关键：如果有话题ID，必须设置，否则消息会发到主频道
		if (messageThreadId != null) {
			replyMessage.setMessageThreadId(messageThreadId);
		}

		// 3. 可选：设置回复格式（Markdown）
//		replyMessage.enableMarkdown(true);

		// 4. 可选：设置为引用回复
//		replyMessage.setReplyToMessageId(receivedMessage.getMessageId());

		// 2. 设置回复内容
		String replyText = "👋 你好 " + identifier + "!\n";
		replyText += "我收到了你的消息: " + receivedText + "\n";

		TbWalletExample exampleWallet = new TbWalletExample();
		exampleWallet.createCriteria().andUidEqualTo(uid + "");
		List<TbWallet> wallets = tbWalletMapper.selectByExample(exampleWallet);
		System.out.println("钱包个数：" + wallets.size());

		if (wallets.size() > 0) {
			replyText += "余额【这是提取作品前余额，提取后此数字会减1，如果提取失败不会减1的】：" + wallets.get(0).getBalance() + "\n";
			System.out.println("余额：" + wallets.get(0).getBalance());
		} else {

			TbWallet wallet = new TbWallet();
			wallet.setBalance(50);
			wallet.setCreated(new Date());
			wallet.setNickname(identifier);
			wallet.setUid(receivedMessage.getFrom().getId() + "");
			wallet.setVersion(0);
			try {
				tbWalletMapper.insertSelective(wallet);
				wallets.add(wallet);
			} catch (org.springframework.dao.DuplicateKeyException e) {
				// 并发场景下其他线程已插入该 uid，重新查询即可
				System.out.println("[dealGetWork] uid=" + wallet.getUid() + " 并发插入冲突，重新查询");
				wallets = tbWalletMapper.selectByExample(exampleWallet);
				if (wallets == null || wallets.size() == 0) {
					throw e;
				}
			}

		}
		// 提取口令（ww/zm/tl/ch/tg/bc/zb）才检测余额；会员群（386/399）提取不扣余额
		String pri = "";
		if (receivedText != null && receivedText.length() >= 2) {
			pri = receivedText.substring(0, 2);
		}
		java.util.List<String> extractCommands = java.util.Arrays.asList("ww", "zm", "tl", "ch", "tg", "bc", "zb");
		boolean isVipGroup = chatId.equals(-1003867299066L) || chatId.equals(-1003992613609L);
		if ((topicok == 0 || topicok == 2) && extractCommands.contains(pri) && !isVipGroup)
			if (wallets.size() == 0 || wallets.get(0).getBalance() == null || wallets.get(0).getBalance() < NORMAL_DEDUCT_JIAO) {
				replyText += "请找客服qq" + rechargeQQ + "充值（3元/次，单次可充3元）\n";
				replyMessage.setText(replyText);
				try {
					getSender().execute(replyMessage);
				} catch (TelegramApiException e) {
					// TODO Auto-generated catch block
					log.error("服务器异常");
					e.printStackTrace();
				}
				return;
			}
		TbWallet wallet = wallets.get(0);

		String url = "";
		String title = "";
		String byString = "";
		String wpString = "";
		String coverString = "";
		String author = "";
		String avatarUrl = "";
		int zhindex = 0;
		Integer workPrice = null;   // 2026-10-28 zmq_video.price，冷门动态定价用
		int deductAmount = NORMAL_DEDUCT_JIAO;   // 2026-10-28 本次提取扣款（角），冷门分支会改
		int yc = 0;

		String table = "wanwu_video";
//		if (receivedText.length() < 3) {
//			replyText += "格式有误，正确格式是：ww12345 ww44322 tl33445 tl12423 ww4789 ww1989333\n";
//			replyMessage.setText(replyText);
//
//			try {
//				getSender().execute(replyMessage);
//			} catch (TelegramApiException e) {
//				// TODO Auto-generated catch block
//				log.error("服务器异常");
//				e.printStackTrace();
//			}
//			return;
//		}
		int vid = 0;
		switch (pri) {
		case "ww":
			WanwuVideoExample example = new WanwuVideoExample();
			vid = 0;
			yc = 1;
			try {
				vid = Integer.parseInt(receivedText.substring(2));
			} catch (Exception e) {
				// TODO: handle exception
				replyText += "格式有误，正确格式是：ww12345 ww44322\n";
				replyMessage.setText(replyText);

				try {
					getSender().execute(replyMessage);
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					log.error("服务器异常");
				}
				return;
			}
			example.createCriteria().andVidEqualTo(vid);
			List<WanwuVideo> listWW = wanwuVideoMapper.selectByExample(example);
			if (listWW.size() > 0) {
				url = listWW.get(0).getUrl();
				title = listWW.get(0).getTitle();
				byString = listWW.get(0).getTria();
				wpString = listWW.get(0).getUptag3() + "";
				coverString = listWW.get(0).getCover();
				author = listWW.get(0).getUrlkey2();

				// 如果url中含有过期标记，则执行一个再次采集操作 54545采集。
				if (url.indexOf("&us=") != -1 || 1 == 1) {
					String articleUrlString = "http://127.0.0.1:54545/appapi/?service=MissVideo.getVideoDetail&uid=120978&token=e2d2d6429dfeacbf7be5efa6b89c8afb&version=137&platform=2&video_id="
							+ vid;
					Response articleRes;
					try {
						articleRes = Jsoup.connect(articleUrlString)
								.header("Content-Type", "application/json; charset=utf-8")
								.header("User-Agent",
										"Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1")
								.header("Accept", "application/json, text/javascript, */*; q=0.01")
								.ignoreContentType(true).execute();
						String sourceString = articleRes.body();
						System.out.println(sourceString);
						JSONObject jsonObject = new JSONObject(sourceString).getJSONObject("data")
								.getJSONObject("info");

						url = jsonObject.getString("url");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						replyText += "注意：请使用网盘分享链接,直接观看链接暂时故障\n";
//						replyMessage.setText(replyText);

//						try {
//							getSender().execute(replyMessage);
//						} catch (TelegramApiException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//							log.error("服务器异常");
//						}
//						okxService.connectWanwuConnect();
//						return;
					}

				}
				WanwuVideo record = new WanwuVideo();
				record.setGoodtag(1);
				// 更新数据库：
				wanwuVideoMapper.updateByExampleSelective(record, example);

			} else {
				replyText += "id可能有误\n";
				replyMessage.setText(replyText);

				try {
					getSender().execute(replyMessage);
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					log.error("服务器异常");
				}
				return;
			}

			break;
		case "zm":
			ZmqVideoExample examplezmq = new ZmqVideoExample();
			vid = 0;
			yc = 1;
			try {
				vid = Integer.parseInt(receivedText.substring(2));
			} catch (Exception e) {
				// TODO: handle exception
				replyText += "格式有误，正确格式是：zm12345 zm44322\n";
				replyMessage.setText(replyText);

				try {
					getSender().execute(replyMessage);
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					log.error("服务器异常");
				}
				return;
			}
			examplezmq.createCriteria().andVidEqualTo(vid);
			List<ZmqVideo> listZmq = zmqVideoMapper.selectByExample(examplezmq);
			if (listZmq.size() > 0) {
				url = listZmq.get(0).getUrl();
				if (url.indexOf("51player1.com") != -1) {
					// 追加sign
					try {
						String sign = getSign.getStr();
						url += "?sign=" + sign;
					} catch (Exception e) {
						replyText += "网络故障，联系群主\n";
						replyMessage.setText(replyText);

						try {
							getSender().execute(replyMessage);
						} catch (TelegramApiException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							log.error("服务器异常");
						}
						return;
						// TODO: handle exception
					}

				}
				title = listZmq.get(0).getTitle();
				byString = listZmq.get(0).getTria();
				coverString = listZmq.get(0).getCover();
				System.out.println("[DEBUG-ZM] vid=" + vid + ", url=" + url + ", byString=" + byString + ", wpString=" + listZmq.get(0).getUptag3());


				wpString = listZmq.get(0).getUptag3() + "";
				workPrice = listZmq.get(0).getPrice();   // 2026-10-28 冷门动态定价
				// 如果url中含有过期标记，则执行一个再次采集操作 54545采集。
				ZmqVideo record = new ZmqVideo();
				record.setGoodtag(1);
				// 更新数据库：
				zmqVideoMapper.updateByExampleSelective(record, examplezmq);

			} else {
				replyText += "id可能有误\n";
				replyMessage.setText(replyText);

				try {
					getSender().execute(replyMessage);
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					log.error("服务器异常");
				}
				return;
			}

			break;
		case "tl":
			yc = 1;
			Taolu3VideoExample exampletl = new Taolu3VideoExample();
			vid = 0;
			try {
				vid = Integer.parseInt(receivedText.substring(2));
			} catch (Exception e) {
				// TODO: handle exception
				replyText += "格式有误，正确格式是：tl33445 tl12423\n";
				replyMessage.setText(replyText);

				try {
					getSender().execute(replyMessage);
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					log.error("服务器异常");
				}
				return;
			}
			exampletl.createCriteria().andVidEqualTo(vid);
			List<Taolu3Video> listTL = taolu3VideoMapper.selectByExample(exampletl);
			if (listTL.size() > 0) {
				url = listTL.get(0).getUrl();
				title = listTL.get(0).getTitle();
				byString = listTL.get(0).getTria();
				wpString = listTL.get(0).getUptag3() + "";
				coverString = listTL.get(0).getCover();
				author = listTL.get(0).getUrlkey2();
				Taolu3Video record = new Taolu3Video();
				record.setGoodtag(1);


				// 更新数据库：
				taolu3VideoMapper.updateByExampleSelective(record, exampletl);

			} else {
				replyText += "id可能有误\n";
				replyMessage.setText(replyText);

				try {
					getSender().execute(replyMessage);
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					log.error("服务器异常");
				}
				return;
			}
			break;
		case "ch":
			yc = 1;
			Isearch search = null;
			;
			try {
				search = new Isearch();
			} catch (Exception e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
				replyText += "搜索服务故障";
				replyMessage.setText(replyText);
				try {
					getSender().execute(replyMessage);
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					log.error("服务器异常");
				}
				return;
			}
			long vids = 0;
			try {
				vids = Long.parseLong(receivedText.substring(2));
			} catch (Exception e) {
				// TODO: handle exception
				replyText += "格式有误，正确格式是：ch33445 \n";
				replyMessage.setText(replyText);

				try {
					getSender().execute(replyMessage);
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					log.error("服务器异常");
				}
				return;
			}
			Hits hits = null;
			try {
				search.in("ID", new long[] { vids });
				hits = search.queryHits();

			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				replyText += "搜索服务故障";
				replyMessage.setText(replyText);
				try {
					getSender().execute(replyMessage);
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					log.error("服务器异常");
				}
				return;
			}
			if (hits != null) {
				url = hits.get(0).getArticle().getString("UR");
				title = ArticleUtil.buildChannelTitle(hits.get(0).getArticle());
				byString = hits.get(0).getArticle().getString("DL");
				wpString = "";
				coverString = "";
				author = "";
				String channel = hits.get(0).getArticle().getString("CH");
				switch (channel) {
				case "kaikai":
					zhindex = 2;

					break;
				case "zuoyou":
					zhindex = 1;

				default:
					break;
				}


				// 更新数据库：
				// taolu3VideoMapper.updateByExampleSelective(record, exampletl);

			} else {
				replyText += "id可能有误\n";
				replyMessage.setText(replyText);

				try {
					getSender().execute(replyMessage);
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					log.error("服务器异常");
				}
				return;
			}
			break;
		case "tg":

			vid = 0;
			yc = 1;
			try {
				vid = Integer.parseInt(receivedText.substring(2));
			} catch (Exception e) {
				// TODO: handle exception
				replyText += "格式有误，正确格式是：tg33445 tg12423\n";
				replyMessage.setText(replyText);

				try {
					getSender().execute(replyMessage);
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					log.error("服务器异常");
				}
				return;
			}
			System.out.println("---------------------------------------------搜索成功1");

			WaiwangVideo video = waiwangVideoMapper.selectByPrimaryKey(vid);
			System.out.println("---------------------------------------------搜索成功2");

			if (video != null) {
				// 排除 friendindex = 1 的记录
				if (video.getFriendindex() == 1) {
					replyText += "该作品暂不可提取\n";
					replyMessage.setText(replyText);
					try {
						getSender().execute(replyMessage);
					} catch (TelegramApiException e1) {
						e1.printStackTrace();
						log.error("服务器异常");
					}
					return;
				}
				url = video.getUrl();
				String mid = url.substring(url.lastIndexOf("/") + 1);
				String prix = url.substring(0, url.lastIndexOf("/") + 1);
				if (video.getDuration().contentEquals("3600")) {
					mid = (Integer.parseInt(mid) + 1) + "";
					url = prix + mid;
				}
				title = video.getTitle();
				byString = video.getAuthor();
				System.out.println("---------------------------------------------搜索成功2.1");

				zhindex = video.getFriendindex();
				System.out.println("---------------------------------------------搜索成功2.2");

				if (byString == null || byString.indexOf("feijipan.com") == -1) {
					System.out.println("---------------------------------------------搜索成功2.3");

				}
				System.out.println("---------------------------------------------搜索成功3");

				video.setGoodtag(1);
				// 更新数据库：
				waiwangVideoMapper.updateByPrimaryKeySelective(video);

			} else {
				replyText += "id可能有误\n";
				replyMessage.setText(replyText);

				try {
					getSender().execute(replyMessage);
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					log.error("服务器异常");
				}
				return;
			}
			break;

		case "bc":

			vid = 0;
			try {
				vid = Integer.parseInt(receivedText.substring(2));
			} catch (Exception e) {
				// TODO: handle exception
				replyText += "格式有误，正确格式是：bc33445 bc12423\n";
				replyMessage.setText(replyText);

				try {
					getSender().execute(replyMessage);
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					log.error("服务器异常");
				}
				return;
			}
			Waiwang2Video video2 = waiwang2VideoMapper.selectByPrimaryKey(vid);
			if (video2 != null) {
				url = video2.getUrl();
				title = video2.getTitle() + "_" + video2.getVid();
				video2.setGoodtag(1);
				byString = video2.getPantag();
				wpString = video2.getChannel();
				coverString = video2.getCover();
				author = video2.getNickname();

				// 更新数据库：
				waiwang2VideoMapper.updateByPrimaryKeySelective(video2);

			} else {
				replyText += "id可能有误\n";
				replyMessage.setText(replyText);

				try {
					getSender().execute(replyMessage);
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					log.error("服务器异常");
				}
				return;
			}
			break;
		case "zb":

			vid = 0;
			try {
				vid = Integer.parseInt(receivedText.substring(2));
			} catch (Exception e) {
				// TODO: handle exception
				replyText += "格式有误，正确格式是：zb33445 zb12423\n";
				replyMessage.setText(replyText);

				try {
					getSender().execute(replyMessage);
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					log.error("服务器异常");
				}
				return;
			}
			// 不再查 waiwang2_video 表，由 handleZhiboCommand 调用 Python 脚本自行校验并自动入库
			break;
		default:
//			replyText += "指令可能有误 ww tl tg开头才行\n";
//			replyMessage.setText(replyText);
//
//			try {
//				getSender().execute(replyMessage);
//			} catch (TelegramApiException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//				log.error("服务器异常");
//			}
			dealSearch(update);
			return;
		}
		title = title.replace(",", "").replace("___", "");

		// 每日提取次数限制：topicok=1（386话题206 / 399话题1）每日限 extractDailyLimit 次
		// bc/tg 类型及 VIP 频道缓存链接（秒发）不限制
		if (topicok == 1) {
			if (!checkDailyExtractLimit(uid, topicok, pri, byString)) {
				replyText += "\n⚠️ 今日搜索话题提取次数已达上限（" + extractDailyLimit + "次），请明日再试。";
				replyMessage.setText(replyText);
				try {
					getSender().execute(replyMessage);
				} catch (TelegramApiException e1) {
					e1.printStackTrace();
					log.error("服务器异常");
				}
				return;
			}
		}

		if (pri.contentEquals("bc")) {
			// 链接展示规则：仅 feijipan/quark 网盘链接展示，其他一切链接不展示（随后发视频）
			boolean urlIsPan = url != null && (url.contains("feijipan.com") || url.contains("feijipan.cn")
					|| url.contains("quark.cn") || url.contains("quark.com"));
			boolean byIsPan = byString != null && (byString.contains("feijipan.com") || byString.contains("feijipan.cn")
					|| byString.contains("quark.cn") || byString.contains("quark.com"));
			String displayUrl = urlIsPan ? url : "";
			String displayByString = byIsPan ? byString : "";
			replyText += "为你提取到的作品链接是：\n" + title + "\n" + displayUrl + "\n本地路径：" + displayByString + "\n所在网盘：" + wpString;
			if (byString != null) {

				File file = new File(byString);
				if (file.exists() || byString.indexOf("t.me") != -1) {

					replyText += "\n" + "稍后会自动发送你视频文件，因为这个文件刚采集的";
//					String info = identifier + "," + byString + "," + receivedText+","+;
					String info = identifier + "," + byString + "," + title + "," + receivedText + "," + chatId + ","
							+ coverString;
					String sourceBot = resolveSourceBot(chatId);
					info = info + "," + byString + ",0," + author + "," + zhindex + "," + topicok + "," + receivedMessage.getFrom().getId() + "," + receivedMessage.getMessageId() + "," + (messageThreadId != null ? messageThreadId : "") + "," + sourceBot + "," + (wallet.getFeijiUsername() != null ? wallet.getFeijiUsername() : "") + "," + (wallet.getFeijiPassword() != null ? wallet.getFeijiPassword() : "");
					// 写入 outbox 并发布事件，事务提交后由监听器推入 Redis
					publishVideoPushEvent(receivedText, String.valueOf(receivedMessage.getFrom().getId()), chatId, sourceBot, info, "videos");
				} else {
					if (!byString.startsWith("http")) {
						replyText += "\n" + "请找客服索要mp4，在网盘里,会给你一个网盘分享链接";
					}

				}

			} else {
				replyText += "\n" + "请找客服索要mp4，在网盘里,会给你一个网盘分享链接";
			}

//			replyText += "\n(这是个秘钥，发给客服，会给你mp4)";
		} else if (pri.contentEquals("zb")) {
			Long fromUserId = receivedMessage.getFrom() != null ? Long.valueOf(receivedMessage.getFrom().getId()) : null;
			replyText += this.handleZhiboCommand(vid, identifier, fromUserId);
			replyMessage.setText(replyText);
			try {
				getSender().execute(replyMessage);
				return;
			} catch (TelegramApiException e1) {
				e1.printStackTrace();
				log.error("服务器异常");
			}
			return;
		} else {

			// wckbot 类型特殊处理：推入 wckbot_extract 队列
			// 2026-10-29：wckbot 站已死(clientHold)，必须 tria=sk 才走冷队列——
			// 已提取(tria=频道链)的 wckbot 资源走正常 zm 秒发流程，否则会被 worker 拿着死链白跑
			boolean isWckbot = pri.contentEquals("zm") && url != null && url.contains("wckbot")
					&& byString != null && byString.equals("sk");
			// 2026-10-28 永慕(yongmufoot.org, type=60) 同冷门规则：未提取(tria=sk)时打标/新用户限制/禁群内提取，
			// 推 ymf_extract 队列由 ymf worker 支付购买(3-4金币/帖)；已提取后 tria=频道链自动脱离冷门
			boolean isYmf = pri.contentEquals("zm") && url != null && url.contains("yongmufoot")
					&& byString != null && byString.equals("sk");
			String coldQueue = isYmf ? "ymf_extract" : "wckbot_extract";
			// 2026-10-28 动态定价（角单位，1元=10角）：普通资源30角(3元)；冷门(tria=sk)=(price金币+1元)*10，库价缺失按3金币计
			if ((isWckbot || isYmf) && byString != null && byString.equals("sk")) {
				deductAmount = coldDeductJiao(workPrice);
				// 冷门扣费前余额校验，不足则直接拒绝（此时还未推队列，不会产生白嫖单）
				if (wallet.getBalance() == null || wallet.getBalance() < deductAmount) {
					SendMessage poorReply = new SendMessage();
					poorReply.setChatId(chatId.toString());
					poorReply.setReplyToMessageId(receivedMessage.getMessageId());
					if (messageThreadId != null) {
						poorReply.setMessageThreadId(messageThreadId);
					}
					poorReply.setText("⚠️ 余额不足：该冷门作品需 " + (deductAmount/10) + " 元，你的余额 "
							+ (wallet.getBalance() != null ? wallet.getBalance()/10 : 0) + " 元\n请联系客服QQ" + rechargeQQ + "充值（3元/次，单次可充3元）");
					try {
						getSender().execute(poorReply);
					} catch (TelegramApiException e1) {
						e1.printStackTrace();
						log.error("服务器异常");
					}
					return;
				}
			}

			if (isWckbot || isYmf) {
				// 2026-09-30 新用户免费额度限制：version=deductBalance 扣费成功次数（每次+1，初始0）。
				// 注册赠送的前5次提取（version<5）谁都不能提冷门资源——充值、VIP 均不解锁；用完5次（version>=5）才解锁。
				// 冷门判据与打标一致：wckbot/ymf 且 tria=sk
				if (byString != null && byString.equals("sk")
						&& (wallet.getVersion() == null || wallet.getVersion() < 5)) {
					SendMessage coldReply = new SendMessage();
					coldReply.setChatId(chatId.toString());
					coldReply.setReplyToMessageId(receivedMessage.getMessageId());
					if (messageThreadId != null) {
						coldReply.setMessageThreadId(messageThreadId);
					}
					coldReply.setText("【新用户免费额度说明】注册赠送的前5次提取仅限普通资源，暂不能提取冷门资源（充值也不能提前解锁）；5次用完后即可提取，充值可加提取次数（3元/次）\n客服qq" + rechargeQQ + "充值");
					try {
						getSender().execute(coldReply);
					} catch (TelegramApiException e1) {
						e1.printStackTrace();
						log.error("服务器异常");
					}
					return;
				}
				// 2026-09-22 冷门资源群内禁止提取：会员群（399/386/3205013648）内 wckbot 任务不 enqueue，提示私聊提取
				if (chatId.equals(-1003992613609L) || chatId.equals(-1003867299066L) || chatId.equals(-1003205013648L)) {
					SendMessage wckbotGroupReply = new SendMessage();
					wckbotGroupReply.setChatId(chatId.toString());
					wckbotGroupReply.setReplyToMessageId(receivedMessage.getMessageId());
					// ⚠️ 关键：如果有话题ID，必须设置，否则消息会发到主频道
					if (messageThreadId != null) {
						wckbotGroupReply.setMessageThreadId(messageThreadId);
					}
					wckbotGroupReply.setText("冷门资源不能在群里提取，请私聊机器人提取");
					try {
						getSender().execute(wckbotGroupReply);
						return;
					} catch (TelegramApiException e1) {
						e1.printStackTrace();
						log.error("服务器异常");
					}
					return;
				}
				// 2026-09-22 冷门资源前缀：wckbot 且 tria=sk（尚未提取出真实网盘链接）时标题加前缀
				if (byString != null && byString.equals("sk")) {
					title = "【冷门资源，3元一个，私聊机器人提取】" + title;
				}
				// 推入 wckbot_extract 队列，但对外提示与普通 zm 保持一致，不暴露 wckbot 特殊处理
				String sourceBot = resolveSourceBot(chatId);
				// 复用原有 videos 队列的 17 字段格式，保持与 donwloadFileAndSendToUser.py / baidu_feiji_bridge.py 兼容
				String wckbotTask = identifier + "," + url + "," + title + "," + receivedText + "," + chatId + ","
						+ coverString + "," + byString + "," + wpString + "," + author + "," + zhindex + "," + topicok + ","
						+ receivedMessage.getFrom().getId() + "," + receivedMessage.getMessageId() + ","
						+ (messageThreadId != null ? messageThreadId : "") + "," + sourceBot + ","
						+ (wallet.getFeijiUsername() != null ? wallet.getFeijiUsername() : "") + ","
						+ (wallet.getFeijiPassword() != null ? wallet.getFeijiPassword() : "");
				// 复用 outbox + 事件机制：事务提交后由监听器推入 wckbot_extract 队列
				videoPushEventService.publish(receivedText, String.valueOf(receivedMessage.getFrom().getId()), chatId, sourceBot, wckbotTask, coldQueue);

				// 复用普通 zm 的回复样式，URL 按原规则隐藏
				String url2 = url;
				String pan2 = byString;

				// 管理员判断
				boolean isAdmin = identifier != null &&
						(identifier.contentEquals("@kaikak09818") || identifier.contentEquals("@linyuan56"));

				// URL展示规则：仅 feijipan/quark 网盘链接允许展示，其他一切链接不展示，管理员例外
				if (url2 != null && !isAdmin) {
					if (!url2.contains("feijipan.com") && !url2.contains("feijipan.cn")
							&& !url2.contains("quark.cn") && !url2.contains("quark.com")) {
						url2 = "";
					}
				}

				// 网盘链接展示规则：仅 feijipan/quark 允许，其他不展示
				boolean isValidPan = pan2 != null && (
						pan2.contains("feijipan.com") || pan2.contains("feijipan.cn")
						|| pan2.contains("quark.cn") || pan2.contains("quark.com"));
				if (!isValidPan) {
					pan2 = "";
				}

				replyText += "为你提取到的作品链接是：\n" + title + "\n" + url2 + "\n网盘分享链接：" + pan2 + "\n所在网盘：" + wpString;
				replyText += "\n" + "稍后会自动发送你视频文件，因为没有网盘分享链接";
			} else {

				String url2 = url;
				String pan2 = byString;

				// 管理员判断
				boolean isAdmin = identifier != null &&
						(identifier.contentEquals("@kaikak09818") || identifier.contentEquals("@linyuan56"));

				// URL展示规则：仅 feijipan/quark 网盘链接允许展示，其他一切链接不展示，管理员例外
				if (url2 != null && !isAdmin) {
					if (!url2.contains("feijipan.com") && !url2.contains("feijipan.cn")
							&& !url2.contains("quark.cn") && !url2.contains("quark.com")) {
						url2 = "";
					}
				}

				// 网盘链接展示规则：仅 feijipan/quark 允许，其他不展示
				boolean isValidPan = pan2 != null && (
						pan2.contains("feijipan.com") || pan2.contains("feijipan.cn")
						|| pan2.contains("quark.cn") || pan2.contains("quark.com"));
				System.out.println("[DEBUG-ZM-REPLY] url2=" + url2 + ", pan2=" + pan2 + ", isValidPan=" + isValidPan + ", isAdmin=" + isAdmin);
				if (!isValidPan) {
					pan2 = "";
				}
				// tg命令的byString不是网盘链接，不展示
				if (pri.contentEquals("tg")) {
					pan2 = "";
				}

				replyText += "为你提取到的作品链接是：\n" + title + "\n" + url2 + "\n网盘分享链接：" + pan2 + "\n所在网盘：" + wpString;

				// 推队列规则：feijipan/quark不推，其他（pikpak/sk/t.me等）推
				boolean isFeijipanQuark = byString != null && (
						byString.contains("feijipan.com") || byString.contains("feijipan.cn")
						|| byString.contains("quark.cn") || byString.contains("quark.com"));

				if (!isFeijipanQuark) {

					replyText += "\n" + "稍后会自动发送你视频文件，因为没有网盘分享链接";
					String sourceBot = resolveSourceBot(chatId);
					String info = identifier + "," + url + "," + title + "," + receivedText + "," + chatId + ","
							+ coverString;
					info += "," + byString + "," + wpString + "," + author + "," + zhindex + "," + topicok + "," + receivedMessage.getFrom().getId() + "," + receivedMessage.getMessageId() + "," + (messageThreadId != null ? messageThreadId : "") + "," + sourceBot + "," + (wallet.getFeijiUsername() != null ? wallet.getFeijiUsername() : "") + "," + (wallet.getFeijiPassword() != null ? wallet.getFeijiPassword() : "");
							// 写入 outbox 并发布事件，事务提交后由监听器推入 Redis
							publishVideoPushEvent(receivedText, String.valueOf(receivedMessage.getFrom().getId()), chatId, sourceBot, info, "videos");
				}
			}
		}

		replyMessage.setText(replyText);

		// 3. 可选：设置回复格式（Markdown）
//		replyMessage.enableMarkdown(true);
//
//		// 4. 可选：设置为引用回复
//		replyMessage.setReplyToMessageId(receivedMessage.getMessageId());
		// 开始修改用户余额
		// 原子扣费，防止并发 Lost Update；会员群（386/399）提取不扣余额
		if ((topicok == 0 || topicok == 2) && !isVipGroup) {
			int deductRows = tbWalletMapper.deductBalanceAmount("" + uid, deductAmount);
			if (deductRows == 0) {
				replyText += "\n⚠️ 余额不足，请联系客服QQ" + rechargeQQ + "充值（3元/次，单次可充3元）";
				replyMessage.setText(replyText);
				try {
					getSender().execute(replyMessage);
				} catch (TelegramApiException e1) {
					e1.printStackTrace();
					log.error("服务器异常");
				}
				return;
			}
			wallet.setBalance(wallet.getBalance() - deductAmount);
			wallet.setNickname(identifier);
		}

		// 搜索话题提取成功，递增每日计数（bc/tg 及 VIP 频道缓存链接不计数）
		if (topicok == 1) {
			incrDailyExtractCount(uid, topicok, pri, byString);
		}

		try {
			// 5. 发送回复
			getSender().execute(replyMessage);
			System.out.println("成功回复消息给: " + identifier);
		} catch (TelegramApiException e) {
			System.err.println("发送回复失败: " + e.getMessage());
			throw new XmallException("回复失败");
		}

		return;
	}

	public String escapeMarkdown(String text) {
//		return text.replace("-", "\\-").replace("_", "\\_").replace("*", "\\*").replace("[", "\\$").replace("]", "\\$")
//				.replace(".", "\\.").replace("!", "\\!").replace(">", "\\>").replace("#", "\\#").replace("+", "\\+")
//				.replace("=", "\\=").replace("|", "\\|").replace("(", "\\$").replace(")", "\\$");

		return text.replace("_", "\\_").replace("*", "\\*").replace("[", "\\$").replace("]", "\\$").replace("(", "\\$")
				.replace(")", "\\$").replace("~", "\\~").replace("`", "\\`").replace(">", "\\>").replace("#", "\\#")
				.replace("+", "\\+").replace("-", "\\-").replace("=", "\\=").replace("|", "\\|").replace("{", "\\{")
				.replace("}", "\\}").replace(".", "\\.").replace("!", "\\!");

	}

	private void updateSearchStatus(long chatId, int messageId, String text) throws TelegramApiException {
		EditMessageText editMsg = new EditMessageText();
		editMsg.setChatId(String.valueOf(chatId));
		editMsg.setMessageId(messageId);
		editMsg.setText(text);
		getSender().execute(editMsg);
	}

	@Override
//	@Async
	public void addVideo(Waiwang2Video video) {
		waiwang2VideoMapper.insertSelective(video);
		// TODO Auto-generated method stub

	}

	@Override
//	@Async
	public void addTiteDate(Wanwu wanwu) {
		wanwuMapper.insertSelective(wanwu);
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDuration(String vid, String duration) {

		Waiwang2VideoExample example = new Waiwang2VideoExample();
		if (parseVidOrNull(vid) == null) return;
		example.createCriteria().andVidEqualTo(parseVidOrNull(vid));

		Waiwang2Video record = new Waiwang2Video();
		record.setDuration(duration);
		record.setChannel("fj1");
		waiwang2VideoMapper.updateByExampleSelective(record, example);

		// TODO Auto-generated method stub

	}

	@Override
	public void updateLocation(String vid, String location) {

		Waiwang2VideoExample example = new Waiwang2VideoExample();
		if (parseVidOrNull(vid) == null) return;
		example.createCriteria().andVidEqualTo(parseVidOrNull(vid));

		Waiwang2Video record = new Waiwang2Video();
		record.setPantag(location);
//		record.setChannel("fj1");
		waiwang2VideoMapper.updateByExampleSelective(record, example);

		// TODO Auto-generated method stub

	}

	@Override
	public void updateLocationtl(String vid, String location) {

		Taolu3VideoExample example = new Taolu3VideoExample();
		if (parseVidOrNull(vid) == null) return;
		example.createCriteria().andVidEqualTo(parseVidOrNull(vid));

		Taolu3Video record = new Taolu3Video();
		record.setTria(location);
//		record.setChannel("fj1");
		taolu3VideoMapper.updateByExampleSelective(record, example);

		// TODO Auto-generated method stub

	}

	@Override
	public void updateLocationww(String vid, String location) {

		WanwuVideoExample example = new WanwuVideoExample();
		if (parseVidOrNull(vid) == null) return;
		example.createCriteria().andVidEqualTo(parseVidOrNull(vid));

		WanwuVideo record = new WanwuVideo();
		record.setTria(location);
//		record.setChannel("fj1");
		wanwuVideoMapper.updateByExampleSelective(record, example);

		// TODO Auto-generated method stub

	}

	@Override
	public void updateUrl(String vid, String url) {

		Waiwang2VideoExample example = new Waiwang2VideoExample();
		if (parseVidOrNull(vid) == null) return;
		example.createCriteria().andVidEqualTo(parseVidOrNull(vid));

		Waiwang2Video record = new Waiwang2Video();
		record.setType(url);
		waiwang2VideoMapper.updateByExampleSelective(record, example);

		// TODO Auto-generated method stub

	}

	@Override
	public void updateTitle(Waiwang2Video video) {

		Waiwang2VideoExample example = new Waiwang2VideoExample();
		example.createCriteria().andVidEqualTo(video.getVid());

		waiwang2VideoMapper.updateByExampleSelective(video, example);

		// TODO Auto-generated method stub

	}

	@Override
	public void updateUrl(String vid, String url, String item) {

		Waiwang2Video video = getVideo(vid);
		if (video == null)
			return;
		String title = video.getTitle();
		Waiwang2VideoExample example = new Waiwang2VideoExample();
		if (parseVidOrNull(vid) == null) return;
		example.createCriteria().andVidEqualTo(parseVidOrNull(vid));

		Waiwang2Video record = new Waiwang2Video();
		record.setType(url);
//		record.setAuthor(item.split("\t")[3]);
//		record.setProvince(item.split("\t")[7]);
//		record.setCity(item.split("\t")[8]);
//		record.setLocationcity(item.split("\t")[9]);
//
//		record.setPhone(item.split("\t")[5]);
//		record.setBio(item.split("\t")[6]);
//
//		record.setUid(item.split("\t")[4]);
//		record.setReg(item.split("\t")[10]);
//		String tt = item.split("\t")[3];
		String tt = "未知";

//		record.setTitle(title.split("_")[0] + "_" + tt + "_" + title.split("_")[2]);
		waiwang2VideoMapper.updateByExampleSelective(record, example);

		// TODO Auto-generated method stub

	}

	@Override
	public Waiwang2Video getVideo(String vid) {

		Waiwang2VideoExample example = new Waiwang2VideoExample();
		if (parseVidOrNull(vid) == null) return null;
		example.createCriteria().andVidEqualTo(parseVidOrNull(vid));

		List<Waiwang2Video> videos = waiwang2VideoMapper.selectByExample(example);
		if (videos.size() > 0)
			return videos.get(0);
		else {
			return null;
		}

		// TODO Auto-generated method stub

	}

	@Override
	public Waiwang2Video getVideoWW2(String vid) {

		Waiwang2VideoExample example = new Waiwang2VideoExample();
		if (parseVidOrNull(vid) == null) return null;
		example.createCriteria().andIdEqualTo(parseVidOrNull(vid));

		List<Waiwang2Video> videos = waiwang2VideoMapper.selectByExample(example);
		if (videos.size() > 0)
			return videos.get(0);
		else {
			return null;
		}

		// TODO Auto-generated method stub

	}

	@Override
	public Waiwang2Video getAuthorFirst(String authorId) {

		Waiwang2VideoExample example = new Waiwang2VideoExample();
		example.createCriteria().andAuthorEqualTo(authorId).andNicknameIsNotNull();
		example.setOrderByClause("id asc");
		PageHelper.startPage(1, 1);
		List<Waiwang2Video> videos = waiwang2VideoMapper.selectByExample(example);
		if (videos.size() > 0)
			return videos.get(0);
		else {
			return null;
		}

		// TODO Auto-generated method stub

	}

	@Override
	public WanwuVideo getVideoWw(String vid) {

		WanwuVideoExample example = new WanwuVideoExample();
		if (parseVidOrNull(vid) == null) return null;
		example.createCriteria().andVidEqualTo(parseVidOrNull(vid));

		List<WanwuVideo> videos = wanwuVideoMapper.selectByExample(example);
		if (videos.size() > 0)
			return videos.get(0);
		else {
			return null;
		}

		// TODO Auto-generated method stub

	}

	@Override
	public Taolu3Video getVideotl(String vid) {

		Taolu3VideoExample example = new Taolu3VideoExample();
		if (parseVidOrNull(vid) == null) return null;
		example.createCriteria().andVidEqualTo(parseVidOrNull(vid));

		List<Taolu3Video> videos = taolu3VideoMapper.selectByExample(example);
		if (videos.size() > 0)
			return videos.get(0);
		else {
			return null;
		}

		// TODO Auto-generated method stub

	}

	@Override
	public ZmqVideo getVideoZmq(String vid) {

		ZmqVideoExample example = new ZmqVideoExample();
		if (parseVidOrNull(vid) == null) return null;
		example.createCriteria().andVidEqualTo(parseVidOrNull(vid));

		List<ZmqVideo> videos = zmqVideoMapper.selectByExample(example);
		if (videos.size() > 0)
			return videos.get(0);
		else {
			return null;
		}

		// TODO Auto-generated method stub

	}

	@Override
	public WaiwangVideo getVideoTG(String vid) {

		WaiwangVideoExample example = new WaiwangVideoExample();
		if (parseVidOrNull(vid) == null) return null;
		example.createCriteria().andIdEqualTo(parseVidOrNull(vid));

		List<WaiwangVideo> videos = waiwangVideoMapper.selectByExample(example);
		if (videos.size() > 0)
			return videos.get(0);
		else {
			return null;
		}

		// TODO Auto-generated method stub

	}

//	@Override
//	public WaiwangVideo getVideoTGTitle(String tt) {
//
//		WaiwangVideoExample example = new WaiwangVideoExample();
//		example.createCriteria().andTitleEqualTo(tt);
//
//		List<WaiwangVideo> videos = waiwangVideoMapper.selectByExample(example);
//		if (videos.size() > 0)
//			return videos.get(0);
//		else {
//			return null;
//		}
//
//		// TODO Auto-generated method stub
//
//	}

	@Override
	public List<Waiwang2Video> getAllVideo(int page, int size) {

		Waiwang2VideoExample example = new Waiwang2VideoExample();
		Criteria criteria = example.createCriteria();
		criteria.andPantagIsNull().andDurationIsNotNull();
		example.setOrderByClause("id desc");
		PageHelper.startPage(page, size);
		List<Waiwang2Video> videos = waiwang2VideoMapper.selectByExample(example);

		return videos;

		// TODO Auto-generated method stub

	}

	@Override
	public List<WanwuVideo> getAllVideo2(int page, int size) {

		WanwuVideoExample example = new WanwuVideoExample();
		cn.exrick.manager.pojo.WanwuVideoExample.Criteria criteria = example.createCriteria();
		criteria.andUptag3IsNull().andTriaNotLike("%picpak%").andTriaNotLike("%quark%");
		example.setOrderByClause("vid desc");
		PageHelper.startPage(page, size);

		List<WanwuVideo> videos = wanwuVideoMapper.selectByExample(example);

		return videos;

		// TODO Auto-generated method stub

	}

	@Override
	public void dealSearchGroup(Update update) {
		// TODO Auto-generated method stub

	}

	@Override
	public Waiwang2Video getValidRecord(String author) {

		Waiwang2VideoExample examplezb = new Waiwang2VideoExample();
		examplezb.createCriteria().andAuthorEqualTo(author + "").andNicknameNotEqualTo("");
		examplezb.setOrderByClause("dt asc");
		PageHelper.startPage(1, 1);
		List<Waiwang2Video> videozbs = waiwang2VideoMapper.selectByExample(examplezb);
		if (videozbs.size() > 0) {
			return videozbs.get(0);
		} else {
			return null;
		}

	}

//	public void sendLongMessage(Long chatId, String text) {
//		int maxLength = 4096;
//		for (int i = 0; i < text.length(); i += maxLength) {
//			String chunk = text.substring(i, Math.min(i + maxLength, text.length()));
//			SendMessage message = new SendMessage(chatId.toString(), chunk);
//			try {
//				telegramChannelMonitor.execute(message);
//			} catch (TelegramApiException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	@Override
	@Transactional
	// 拼接超长字符串
	public String getAllWork(Update update) {

		List<InputMedia> mediaList = new ArrayList<InputMedia>();
// 		mediaList.add(new InputMediaPhoto("图片1_URL").setCaption("描述1"));

		// 搜索数据库
		Message channelMsg = update.getChannelPost();
		if (channelMsg == null)
			channelMsg = update.getMessage();
		System.out.println(channelMsg.getChat().getUserName());
		Long chatId = channelMsg.getChatId(); // 频道ID

		String text = channelMsg.getText(); // 消息文本

		System.out.println("新消息b: " + channelMsg.getText());
		// 处理媒体消息
		if (channelMsg.hasPhoto()) {
			System.out.println("检测到图片附件");
		}
		StringBuffer re = new StringBuffer();
		if (text.contentEquals("最新作品")) {
			text = null;
		}

		// 发送加载动画消息
//		SendMessage loadingMsg = new SendMessage(chatId + "", "⏳ 搜索中，请稍候...（万一不能播放找客服）");
//		loadingMsg.setReplyToMessageId(channelMsg.getMessageId());

//		Message sentMessage = null;
//		try {
//			sentMessage = telegramChannelMonitor.execute(loadingMsg);
//		} catch (TelegramApiException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//			System.out.println("搜索异常 电报服务器异常");
//			return;
//		}

//		int messageId = sentMessage.getMessageId();

//		try {
//		//	updateSearchStatus(chatId, messageId, "🛒 正在搜索玩物...");
//		} catch (TelegramApiException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		ZmqVideoExample exampleWaiwangzmq = new ZmqVideoExample();
		cn.exrick.manager.pojo.ZmqVideoExample.Criteria criteriazmq = exampleWaiwangzmq.createCriteria();
		if (text != null) {
			if (!text.trim().isEmpty()) {
					String[] textArr = text.trim().split("\\p{javaWhitespace}+");
					for (String item : textArr) {
						if (item.length() > 0) {
							criteriazmq.andTitleLike("%" + item + "%");
						}
					}
				}

		}
//		criteriaWw2.andTitleNotEqualTo("");
		criteriazmq.andDurationIsNotNull();
		exampleWaiwangzmq.setOrderByClause("addtime desc");
		PageHelper.startPage(1, 10000);
		List<ZmqVideo> listWaiwangzmq = zmqVideoMapper.selectByExample(exampleWaiwangzmq);
		removeDeadWckbot(listWaiwangzmq);   // 2026-10-28 wckbot 站报废：搜索结果剔除 wckbot 分类(type 91-99) 且未交付(tria='sk') 的记录
		PageInfo<ZmqVideo> pizmqInfo = new PageInfo<ZmqVideo>(listWaiwangzmq);

		re.append("网页搜索 " + pizmqInfo.getTotal() + "条\r\n");
		mediaList = new ArrayList<InputMedia>();

		for (int i = 0; i < listWaiwangzmq.size(); i++) {
			String link = TelegramDeepLink.generateLink(getSender().getBotUsername(),
					"zm" + listWaiwangzmq.get(i).getVid());
			String tt = cleanSearchTitle(listWaiwangzmq.get(i).getTitle());
//			if (tt.length() > 60)
//				tt = tt.substring(0, 60);
			// re.append(i + "\t" + tt + "\t" + "机器人口令：bc" + listWaiwang2.get(i).getId() +
			// "\t" + link + "\n");
			if (listWaiwangzmq.get(i).getCover() == null)
				listWaiwangzmq.get(i).setCover(
						"https://s3imgqnv1.ikzuo.com/app/user/117431_20250825050414_8c56e54391d88227e0081a266509c952.png?imageView2/2/w/56");
			InputMediaPhoto photo = new InputMediaPhoto(listWaiwangzmq.get(i).getCover());
			String zmqTag2 = getVipFreeTag(listWaiwangzmq.get(i));
			String tit = i + "\t" + zmqTag2 + tt + "\t时长：" + listWaiwangzmq.get(i).getDuration() + "\t" + "机器人口令：zm"
					+ listWaiwangzmq.get(i).getVid() + "\t" + link;

			if (channelMsg.getFrom().getUserName() != null
					&& channelMsg.getFrom().getUserName().contentEquals("kaikak09818")) {
				tit = tit + "\t" + "网盘链接：" + listWaiwangzmq.get(i).getTria();
			}

			photo.setCaption(tit); // 每张图片独立描述
			mediaList.add(photo);
//			if (!UrlValidator.isUrlValid(listWaiwang2.get(i).getCover())) {
//
//				listWaiwang2.get(i).setCover(beiyongUrl);
//				re.append(photo.getCaption() + "\n");
//			} else {

			int robotRe = 0;// telegramChannelMonitor.sendChannelReplyWithPhoto(chatId,
							// mediaList.get(i).getCaption(),
			// channelMsg.getMessageId(), listWaiwangzmq.get(i).getCover());
//			try {
////				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

			if (robotRe == 0)
				re.append(photo.getCaption() + "\r\n");
//			}

		}

		Waiwang2VideoExample exampleWaiwang2 = new Waiwang2VideoExample();
		cn.exrick.manager.pojo.Waiwang2VideoExample.Criteria criteriaWw2 = exampleWaiwang2.createCriteria();
		if (text != null) {
			if (!text.trim().isEmpty()) {
					String[] textArr = text.trim().split("\\p{javaWhitespace}+");
					for (String item : textArr) {
						if (item.length() > 0) {
							criteriaWw2.andTitleLike("%" + item + "%");
						}
					}
				}

		}
//		criteriaWw2.andTitleNotEqualTo("");
		criteriaWw2.andDurationIsNotNull();
		// 排除 pantag 不含 http 的结果
		criteriaWw2.andPantagLike("%http%");
		exampleWaiwang2.setOrderByClause("dt desc");
		PageHelper.startPage(1, 10000);
		List<Waiwang2Video> listWaiwang2 = waiwang2VideoMapper.selectByExample(exampleWaiwang2);
		PageInfo<Waiwang2Video> pibcInfo = new PageInfo<Waiwang2Video>(listWaiwang2);

		re.append("最新作品搜索 " + pibcInfo.getTotal() + "条\r\n");
		mediaList = new ArrayList<InputMedia>();

		for (int i = 0; i < listWaiwang2.size(); i++) {
			String link = TelegramDeepLink.generateLink(getSender().getBotUsername(),
					"bc" + listWaiwang2.get(i).getId());
			String tt = cleanSearchTitle(listWaiwang2.get(i).getTitle()).split("_")[0];
//			if (tt.length() > 60)
//				tt = tt.substring(0, 60);
			// re.append(i + "\t" + tt + "\t" + "机器人口令：bc" + listWaiwang2.get(i).getId() +
			// "\t" + link + "\n");
			if (listWaiwang2.get(i).getCover() == null)
				listWaiwang2.get(i).setCover(
						"https://s3imgqnv1.ikzuo.com/app/user/117431_20250825050414_8c56e54391d88227e0081a266509c952.png?imageView2/2/w/56");

			InputMediaPhoto photo = new InputMediaPhoto(listWaiwang2.get(i).getCover());
			String bcTag2 = getVipFreeTag(listWaiwang2.get(i));
			String tit = i + "\t" + bcTag2 + tt + "\tid:" + listWaiwang2.get(i).getUid() + "\t网名："
					+ listWaiwang2.get(i).getNickname() + "\t时长：" + listWaiwang2.get(i).getDuration() + "\t时间："
					+ listWaiwang2.get(i).getDt() + "\t封面：" + listWaiwang2.get(i).getCover();

			if (channelMsg.getFrom().getUserName() != null
					&& channelMsg.getFrom().getUserName().contentEquals("kaikak09818")) {
				tit = tit + "\t" + "网盘链接：" + listWaiwang2.get(i).getPantag();
			}
			tit = tit + "\t" + "机器人口令：bc" + listWaiwang2.get(i).getId() + "\t提取地址：" + link;
			photo.setCaption(tit); // 每张图片独立描述
			mediaList.add(photo);
//			if (!UrlValidator.isUrlValid(listWaiwang2.get(i).getCover())) {
//
//				listWaiwang2.get(i).setCover(beiyongUrl);
//				re.append(photo.getCaption() + "\n");
//			} else {

			int robotRe = 0;// telegramChannelMonitor.sendChannelReplyWithPhoto(chatId,
							// mediaList.get(i).getCaption(),
			// channelMsg.getMessageId(), listWaiwang2.get(i).getCover());
//			try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			if (robotRe == 0)
				re.append(photo.getCaption() + "\r\n");
//			}

		}

		WanwuVideoExample example = new WanwuVideoExample();

		cn.exrick.manager.pojo.WanwuVideoExample.Criteria criteria = example.createCriteria();
		if (text != null) {
			if (!text.trim().isEmpty()) {
					String[] textArr = text.trim().split("\\p{javaWhitespace}+");
					for (String item : textArr) {
						if (item.length() > 0) {
							criteria.andTitleLike("%" + item + "%");
						}
					}
				}
		}
//		criteriaTaolu.andTitleNotEqualTo("");

		example.setOrderByClause("addtime desc");
		PageHelper.startPage(1, 10000);
		List<WanwuVideo> list = wanwuVideoMapper.selectByExample(example);
		PageInfo<WanwuVideo> piwwInfo = new PageInfo<WanwuVideo>(list);

		re.append("玩物搜索 " + piwwInfo.getTotal() + "条（只返回前10000条，如果需要所有的找客服要）\r\n");
		mediaList = new ArrayList<InputMedia>();

		for (int i = 0; i < list.size(); i++) {
			String link = TelegramDeepLink.generateLink(getSender().getBotUsername(),
					"ww" + list.get(i).getVid());

//			if (i == 3 || i == 4)
//				continue;

//			System.out.println("--------------------index:" + i + "\t" + list.get(i).getTitle() + "\t"
//					+ list.get(i).getAddtime() + "\t" + "机器人口令：ww" + list.get(i).getVid() + "\t" + link + "");

			// 检查封面是否为空
			String cover = list.get(i).getCover();
			if (cover == null || cover.trim().isEmpty()) {
				cover = "https://s3imgqnv1.ikzuo.com/app/user/117431_20250825050414_8c56e54391d88227e0081a266509c952.png?imageView2/2/w/56";
			}
			InputMediaPhoto photo = new InputMediaPhoto(cover);

			String tt = cleanSearchTitle(list.get(i).getTitle()).split("_")[0];

			String wwTag2 = getVipFreeTag(list.get(i));
			String tit = i + "\t" + wwTag2 + tt + "\tid:" + list.get(i).getAuthor() + "\t网名：" + list.get(i).getUrlkey2()
					+ "\t时长：" + list.get(i).getDuration() + "\t时间：" + list.get(i).getAddtime() + "\t封面："
					+ list.get(i).getCover();

			if (channelMsg.getFrom().getUserName() != null
					&& channelMsg.getFrom().getUserName().contentEquals("kaikak09818")) {
				tit = tit + "\t" + "网盘链接：" + list.get(i).getTria();
			}

			tit = tit + "\t" + "机器人口令：ww" + list.get(i).getVid() + "\t提取地址：" + link;

//			i + "\t" + list.get(i).getTitle() + "\t时间：" + list.get(i).getAddtime() + "\t" + "机器人口令：ww"
//			+ list.get(i).getVid() + "\t" + link + ""

			photo.setCaption(tit); // 每张图片独立描述
			// photo.setParseMode("HTML");

			// 设置消息实体，禁用网页预览
//			List<MessageEntity> entities = new ArrayList<>();
//			MessageEntity urlEntity = new MessageEntity();
//			urlEntity.setType("text_link");
//			urlEntity.setOffset(photo.getCaption().indexOf(link));
//			urlEntity.setLength(link.length());
//			urlEntity.setUrl(link);
//			entities.add(urlEntity);
//			photo.setCaptionEntities(entities);
			mediaList.add(photo);

//			if (!UrlValidator.isUrlValid(list.get(i).getCover())) {
////
//				list.get(i).setCover(beiyongUrl);
//				re.append(photo.getCaption() + "\n");
//
//			} else {
//			System.out.println("--------------------index:" + i + " " + list.get(i).getCover());

//			int robotResult = getSender().sendChannelReplyWithPhoto(chatId, mediaList.get(i).getCaption(),
//					channelMsg.getMessageId(), list.get(i).getCover());
//
//			if (robotResult == 0)
			re.append(photo.getCaption() + "\r\n");
//			}

		}
//		if (mediaList.size() > 0) {
//			if (mediaList.size() >= 2)
//				telegramChannelMonitor.sendPhotos(chatId, channelMsg.getMessageId(), mediaList);
//			else {
//				telegramChannelMonitor.sendChannelReplyWithPhoto(chatId, mediaList.get(0).getCaption(),
//						channelMsg.getMessageId(), list.get(0).getCover());
//
//			}
//		}
//		try {
//			updateSearchStatus(chatId, messageId, "🛒 正在搜索淘露...");
//		} catch (TelegramApiException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Taolu3VideoExample exampleTaolu = new Taolu3VideoExample();
		cn.exrick.manager.pojo.Taolu3VideoExample.Criteria criteriaTaolu = exampleTaolu.createCriteria();
		if (text != null) {
			if (!text.trim().isEmpty()) {
					String[] textArr = text.trim().split("\\p{javaWhitespace}+");
					for (String item : textArr) {
						if (item.length() > 0) {
							criteriaTaolu.andTitleLike("%" + item + "%");
						}
					}
				}

		}
//		criteriaTaolu.andTitleNotEqualTo("");

		exampleTaolu.setOrderByClause("dt desc");
		PageHelper.startPage(1, 10000);
		List<Taolu3Video> listTaolu = taolu3VideoMapper.selectByExample(exampleTaolu);
		PageInfo<Taolu3Video> pitlInfo = new PageInfo<Taolu3Video>(listTaolu);

		re.append("淘露搜索 " + pitlInfo.getTotal() + "\r\n");
		mediaList = new ArrayList<InputMedia>();

		for (int i = 0; i < listTaolu.size(); i++) {
			String link = TelegramDeepLink.generateLink(getSender().getBotUsername(),
					"tl" + listTaolu.get(i).getVid());
			// 检查封面是否为空
			String cover = listTaolu.get(i).getCover();
			if (cover == null || cover.trim().isEmpty()) {
				cover = "https://s3imgqnv1.ikzuo.com/app/user/117431_20250825050414_8c56e54391d88227e0081a266509c952.png?imageView2/2/w/56";
			}
			InputMediaPhoto photo = new InputMediaPhoto(cover);

			String tt = cleanSearchTitle(listTaolu.get(i).getTitle()).split("_")[0];

			String tlTag2 = getVipFreeTag(listTaolu.get(i));
			String tit = i + "\t" + tlTag2 + tt + "\tid:" + listTaolu.get(i).getAuthor() + "\t网名:"
					+ listTaolu.get(i).getUrlkey2() + "\t时间：" + listTaolu.get(i).getDt() + "\t封面："
					+ listTaolu.get(i).getCover();

			if (channelMsg.getFrom().getUserName() != null
					&& channelMsg.getFrom().getUserName().contentEquals("kaikak09818")) {
				tit = tit + "\t" + "网盘链接：" + listTaolu.get(i).getTria();
			}

			tit = tit + "\t" + "机器人口令：tl" + listTaolu.get(i).getVid() + "\t提取地址：" + link;

			photo.setCaption(tit); // 每张图片独立描述
			mediaList.add(photo);

//			if (!UrlValidator.isUrlValid(listTaolu.get(i).getCover())) {
//
//				listTaolu.get(i).setCover(beiyongUrl);
//				re.append(i + "\t" + listTaolu.get(i).getTitle() + "\t" + listTaolu.get(i).getVid() + "\t" + "机器人口令：tl"
//						+ listTaolu.get(i).getVid() + "\t" + link + "\n");
//			} else {

//			int robotRe = getSender().sendChannelReplyWithPhoto(chatId, mediaList.get(i).getCaption(),
//					channelMsg.getMessageId(), listTaolu.get(i).getCover());
//
//			if (robotRe == 0)
			re.append(photo.getCaption() + "\r\n");
//			}
		}
//		if (mediaList.size() > 0) {
//			if (mediaList.size() >= 2)
//				telegramChannelMonitor.sendPhotos(chatId, channelMsg.getMessageId(), mediaList);
//			else {
//				telegramChannelMonitor.sendChannelReplyWithPhoto(chatId, mediaList.get(0).getCaption(),
//						channelMsg.getMessageId(), listTaolu.get(0).getCover());
//			}
//
//		}
//		try {
//			updateSearchStatus(chatId, messageId, "🛒 正在搜索电报各大频道...");
//		} catch (TelegramApiException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		WaiwangVideoExample exampleWaiwang = new WaiwangVideoExample();
		cn.exrick.manager.pojo.WaiwangVideoExample.Criteria criteriaWw = exampleWaiwang.createCriteria();
		if (text != null) {
			if (!text.trim().isEmpty()) {
					String[] textArr = text.trim().split("\\p{javaWhitespace}+");
					for (String item : textArr) {
						if (item.length() > 0) {
							criteriaWw.andTitleLike("%" + item + "%");
						}
					}
				}

		}
		criteriaWw.andTitleNotEqualTo("");
		criteriaWw.andDurationGreaterThan("60");
		// 排除 friendindex = 1 的记录
		criteriaWw.andFriendindexNotEqualTo(1);
		exampleWaiwang.setOrderByClause("dt desc");
		PageHelper.startPage(1, 10000);
		List<WaiwangVideo> listWaiwang = waiwangVideoMapper.selectByExample(exampleWaiwang);
		PageInfo<WaiwangVideo> pitgInfo = new PageInfo<WaiwangVideo>(listWaiwang);

		re.append("电报搜索 " + pitgInfo.getTotal() + "条\r\n");
		mediaList = new ArrayList<InputMedia>();
		StringBuffer ttt = new StringBuffer();

		for (int i = 0; i < listWaiwang.size(); i++) {
			String link = TelegramDeepLink.generateLink(getSender().getBotUsername(),
					"tg" + listWaiwang.get(i).getId());
			String tt = cleanSearchTitle(listWaiwang.get(i).getTitle());
			if (tt.length() > 60)
				tt = tt.substring(0, 60);

			String tgTag2 = getVipFreeTag(listWaiwang.get(i));
			String tit = i + "\t" + tgTag2 + tt + "\t时长:" + listWaiwang.get(i).getDuration() + "秒\t时间："
					+ listWaiwang.get(i).getDt();

			if (channelMsg.getFrom().getUserName() != null
					&& channelMsg.getFrom().getUserName().contentEquals("kaikak09818")) {
				tit = tit + "\t" + "电报链接：" + listWaiwang.get(i).getUrl();
			}

			tit = tit + "\t" + "机器人口令：tg" + listWaiwang.get(i).getId() + "\t提取地址：" + link;

			re.append(tit + "\r\n");

//			InputMediaPhoto photo = new InputMediaPhoto();
//			photo.setCaption(i + "\t" + tt + "\t" + listWaiwang.get(i).getDuration() + "秒\t"
//					+ listWaiwang.get(i).getDt() + "\t" + "机器人口令：tg" + listWaiwang.get(i).getId() + "\t" + link); // 每张图片独立描述
//			mediaList.add(photo);

//			int robotRe = getSender().sendChannelReplyWithPhoto(chatId, photo.getCaption(),
//					channelMsg.getMessageId(), "6127453610561357706");
//			if (robotRe == 0) {
////				re.append(i + "\t" + tt + "\t" + listWaiwang.get(i).getDt() + "\t" + "机器人口令：tg" + listWaiwang.get(i).getId()
////						+ "\t" + link + "\n");
//			}

		}

		Hits hits = null;

		// 检查搜索引擎是否可用
		if (SearchFactory.isIsearchAvailable()) {
			try {
				Isearch search = new Isearch();
				// DM字段过滤已移除
				search.andText("TX", text);
				// 使用 notIndexedText 过滤 CH 字段
				search.andNotIndexedText("CH", "kaikai");
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.YEAR, -10);
				search.greatThan("RQ", calendar.getTimeInMillis());
				search.setMaxResults(10000);
				hits = search.queryHits();
			} catch (Exception e1) {
				log.warn("Isearch 搜索失败: {}", e1.getMessage());
			}
		} else {
			log.info("搜索引擎不可用，跳过频道搜索");
		}

		if (hits != null) {
			re.append("频道搜索 " + hits.getTotal() + "条\r\n");
			mediaList = new ArrayList<InputMedia>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			for (int i = 0; i < hits.size(); i++) {
				Hit hit = hits.get(i);
				String link = TelegramDeepLink.generateLink(getSender().getBotUsername(),
						"ch" + hit.getId());
				String tt = ArticleUtil.buildChannelTitle(hit.getArticle());
				if (channelMsg.getFrom().getUserName() != null
						&& channelMsg.getFrom().getUserName().contentEquals("kaikak09818")) {
					tt = tt + "\t" + "电报链接：" + hit.getArticle().getString("UR");
				}
				long rq = hit.getArticle().getLong("RQ");
				String dt = sdf.format(new Date(rq));
				java.util.Map<String, Object> chMap2 = new java.util.HashMap<>();
				chMap2.put("url", hit.getArticle().getString("UR"));
				String chTag2 = getVipFreeTagForChannel(chMap2);

				re.append(i + "\t" + chTag2 + tt + "\t时间：" + dt + "\t时长：" + hit.getArticle().getString("CC") + "分 \t"
						+ "机器人口令：ch" + hit.getId() + "\t" + link + "\r\n");

//				InputMediaPhoto photo = new InputMediaPhoto();
//				photo.setCaption(i + "\t" + tt + "\t" + listWaiwang.get(i).getDuration() + "秒\t"
//						+ dt + "\t" + "机器人口令：ch" + hit.getId() + "\t" + link); // 每张图片独立描述
//				mediaList.add(photo);

//			int robotRe = getSender().sendChannelReplyWithPhoto(chatId, photo.getCaption(),
//					channelMsg.getMessageId(), "6127453610561357706");
//			if (robotRe == 0) {
////				re.append(i + "\t" + tt + "\t" + listWaiwang.get(i).getDt() + "\t" + "机器人口令：tg" + listWaiwang.get(i).getId()
////						+ "\t" + link + "\n");
//			}

			}

		}

		if (mediaList.size() > 0) {
			// String vr = escapeMarkdown(ttt.toString());

//			telegramChannelMonitor.sendPhotos(chatId, channelMsg.getMessageId(), mediaList);
			// telegramChannelMonitor.sendChannelReply(chatId, vr,
			// channelMsg.getMessageId());
		}

//		try {
//			updateSearchStatus(chatId, messageId, "🛒 正在搜索本地库...");
//		} catch (TelegramApiException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		if (mediaList.size() > 0) {
//			if (mediaList.size() >= 2)
//				telegramChannelMonitor.sendPhotos(chatId, channelMsg.getMessageId(), mediaList);
//			else {
//				telegramChannelMonitor.sendChannelReplyWithPhoto(chatId, mediaList.get(0).getCaption(),
//						channelMsg.getMessageId(), listWaiwang2.get(0).getCover());
//			}
//
//		}
//		try {
//			// updateSearchStatus(chatId, messageId, "🛒 搜索成功...");
//			// 完成搜索后删除提示
//			DeleteMessage deleteMsg = new DeleteMessage(chatId + "", sentMessage.getMessageId());
//			telegramChannelMonitor.execute(deleteMsg);
//		} catch (TelegramApiException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String responseT = re.toString();
		String validRes = responseT;

//		telegramChannelMonitor.sendChannelReply(chatId, validRes, channelMsg.getMessageId());

//		int maxLength = 4096;
//		for (int i = 0; i < validRes.length(); i += maxLength) {
//			String chunk = validRes.substring(i, Math.min(i + maxLength, validRes.length()));
////			SendMessage message = new SendMessage(chatId.toString(), chunk);
//
//			telegramChannelMonitor.sendChannelReply(chatId, chunk, channelMsg.getMessageId());
//
////		 telegramChannelMonitor.sendChannelReply(chatId, validRes,
////		 channelMsg.getMessageId());
//
//		}

		return validRes;
	}

	@Override
	public void updateLocationTG(String vid, String location) {

		WaiwangVideoExample example = new WaiwangVideoExample();
		if (parseVidOrNull(vid) == null) return;
		example.createCriteria().andIdEqualTo(parseVidOrNull(vid));

		WaiwangVideo record = new WaiwangVideo();
		record.setAuthor(location);
//		record.setChannel("fj1");
		waiwangVideoMapper.updateByExampleSelective(record, example);

		// TODO Auto-generated method stub

	}

	@Override
	public void updateLocationzmq(String vid, String location) {

		ZmqVideoExample example = new ZmqVideoExample();
		if (parseVidOrNull(vid) == null) return;
		example.createCriteria().andVidEqualTo(parseVidOrNull(vid));

		ZmqVideo record = new ZmqVideo();
		record.setTria(location);
//		record.setChannel("fj1");
		zmqVideoMapper.updateByExampleSelective(record, example);

		// TODO Auto-generated method stub

	}

	// ==================== 批量查询方法实现（供 QQ Bot 使用） ====================
	
	@Override
	public SearchResultDTO searchAll(String keyword, int page, int pageSize) {
		SearchResultDTO result = new SearchResultDTO(keyword);
		
		// 并行查询提高效率
		// 使用 new ArrayList<>() 包裉，避免 PageHelper 返回的 Page 对象导致 Dubbo hessian2 序列化问题
		result.setZmqVideos(new ArrayList<>(searchZmq(keyword, page, pageSize)));
		result.setWaiwang2Videos(new ArrayList<>(searchWaiwang2(keyword, page, pageSize)));
		result.setWanwuVideos(new ArrayList<>(searchWanwu(keyword, page, pageSize)));
		result.setTaolu3Videos(new ArrayList<>(searchTaolu3(keyword, page, pageSize)));
		result.setWaiwangVideos(new ArrayList<>(searchWaiwang(keyword, page, pageSize)));
		
		// isearch频道搜索（CH=kaikai）
		result.setChannelVideos(searchChannelVideos(keyword));
		
		log.info("searchAll 完成: keyword={}, 总结果数={}", keyword, result.getTotalCount());
		return result;
	}
	
	/**
	 * 搜索频道视频（isearch）
	 * 只返回 CH 为 "kaikai" 的结果
	 */
	private List<Map<String, Object>> searchChannelVideos(String keyword) {
		List<Map<String, Object>> channelVideos = new ArrayList<>();
		
		if (!SearchFactory.isIsearchAvailable()) {
			log.info("搜索引擎不可用，跳过频道搜索");
			return channelVideos;
		}
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Isearch search = new Isearch();
			search.andText("TX", keyword);
			// 使用 notIndexedText 过滤 CH 字段，只返回 kaikai 频道
			search.andNotIndexedText("CH", "kaikai");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.YEAR, -10);
			search.greatThan("RQ", calendar.getTimeInMillis());
			search.setMaxResults(10000);
			
			Hits hits = search.queryHits();
			if (hits != null) {
				for (int i = 0; i < hits.size(); i++) {
					Hit hit = hits.get(i);
					Map<String, Object> video = new HashMap<>();
					video.put("id", hit.getId());
					video.put("title", ArticleUtil.buildChannelTitle(hit.getArticle()));
					video.put("channel", hit.getArticle().getString("CH"));
					video.put("duration", hit.getArticle().getString("CC"));
					video.put("url", hit.getArticle().getString("UR"));
					long rqTs = hit.getArticle().getLong("RQ");
					video.put("dt", sdf.format(new Date(rqTs)));
					channelVideos.add(video);
				}
			}
			log.info("频道搜索完成: keyword={}, 结果数={}", keyword, channelVideos.size());
		} catch (Exception e) {
			log.warn("频道搜索失败: {}", e.getMessage());
		}
		
		return channelVideos;
	}
	
	@Override
	public List<ZmqVideo> searchZmq(String keyword, int page, int pageSize) {
		ZmqVideoExample example = new ZmqVideoExample();
		ZmqVideoExample.Criteria criteria = example.createCriteria();
		
		if (keyword != null && !keyword.isEmpty()) {
			if (!keyword.trim().isEmpty()) {
			String[] keywords = keyword.trim().split("\\p{javaWhitespace}+");
			for (String k : keywords) {
				if (k.length() > 0) {
					criteria.andTitleLike("%" + k + "%");
				}
			}
		}
		}
		
		criteria.andDurationIsNotNull();
		example.setOrderByClause("addtime desc");
		
		PageHelper.startPage(page, pageSize);
		List<ZmqVideo> result = zmqVideoMapper.selectByExample(example);
		removeDeadWckbot(result);   // 2026-10-28 wckbot 站报废，搜索统一过滤：type 91-99 且 tria='sk'（未交付）的记录不展示
		log.info("ZM搜索: keyword={}, 结果数={}", keyword, result.size());
		return result;
	}
	
	/** 2026-10-28 wckbot 站报废：type 91-99 且 tria=sk（未交付）的记录从搜索/列表剔除 */
	private static void removeDeadWckbot(java.util.List<ZmqVideo> list) {
		if (list == null) {
			return;
		}
		list.removeIf(v -> v.getType() != null && v.getType() >= 91 && v.getType() <= 99 && "sk".equals(v.getTria()));
	}

	public List<Waiwang2Video> searchWaiwang2(String keyword, int page, int pageSize) {
		Waiwang2VideoExample example = new Waiwang2VideoExample();
		Waiwang2VideoExample.Criteria criteria = example.createCriteria();
		
		if (keyword != null && !keyword.isEmpty()) {
			if (!keyword.trim().isEmpty()) {
			String[] keywords = keyword.trim().split("\\p{javaWhitespace}+");
			for (String k : keywords) {
				if (k.length() > 0) {
					criteria.andTitleLike("%" + k + "%");
				}
			}
		}
		}
		
		criteria.andDurationIsNotNull();
		// 排除 pantag 不含 http 的结果
		criteria.andPantagLike("%http%");
		example.setOrderByClause("dt desc");
		
		PageHelper.startPage(page, pageSize);
		List<Waiwang2Video> result = waiwang2VideoMapper.selectByExample(example);
		log.info("BC搜索: keyword={}, 结果数={}", keyword, result.size());
		return result;
	}
	
	@Override
	public List<WanwuVideo> searchWanwu(String keyword, int page, int pageSize) {
		WanwuVideoExample example = new WanwuVideoExample();
		WanwuVideoExample.Criteria criteria = example.createCriteria();
		
		if (keyword != null && !keyword.isEmpty()) {
			if (!keyword.trim().isEmpty()) {
			String[] keywords = keyword.trim().split("\\p{javaWhitespace}+");
			for (String k : keywords) {
				if (k.length() > 0) {
					criteria.andTitleLike("%" + k + "%");
				}
			}
		}
		}
		
		example.setOrderByClause("addtime desc");
		
		PageHelper.startPage(page, pageSize);
		List<WanwuVideo> result = wanwuVideoMapper.selectByExample(example);
		log.info("WW搜索: keyword={}, 结果数={}", keyword, result.size());
		return result;
	}
	
	@Override
	public List<Taolu3Video> searchTaolu3(String keyword, int page, int pageSize) {
		Taolu3VideoExample example = new Taolu3VideoExample();
		Taolu3VideoExample.Criteria criteria = example.createCriteria();
		
		if (keyword != null && !keyword.isEmpty()) {
			if (!keyword.trim().isEmpty()) {
			String[] keywords = keyword.trim().split("\\p{javaWhitespace}+");
			for (String k : keywords) {
				if (k.length() > 0) {
					criteria.andTitleLike("%" + k + "%");
				}
			}
		}
		}
		
		example.setOrderByClause("dt desc");
		
		PageHelper.startPage(page, pageSize);
		List<Taolu3Video> result = taolu3VideoMapper.selectByExample(example);
		log.info("TL搜索: keyword={}, 结果数={}", keyword, result.size());
		return result;
	}
	
	@Override
	public List<WaiwangVideo> searchWaiwang(String keyword, int page, int pageSize) {
		WaiwangVideoExample example = new WaiwangVideoExample();
		WaiwangVideoExample.Criteria criteria = example.createCriteria();
		
		if (keyword != null && !keyword.isEmpty()) {
			if (!keyword.trim().isEmpty()) {
			String[] keywords = keyword.trim().split("\\p{javaWhitespace}+");
			for (String k : keywords) {
				if (k.length() > 0) {
					criteria.andTitleLike("%" + k + "%");
				}
			}
		}
		}
		
		criteria.andTitleNotEqualTo("");
		criteria.andDurationGreaterThan("60");
		// 排除 friendindex = 1 的记录
		criteria.andFriendindexNotEqualTo(1);
		example.setOrderByClause("dt desc");
		
		PageHelper.startPage(page, pageSize);
		List<WaiwangVideo> result = waiwangVideoMapper.selectByExample(example);
		log.info("TG搜索: keyword={}, 结果数={}", keyword, result.size());
		return result;
	}
	
	@Override
	public List<Waiwang2Video> getLatestWorks(int page, int pageSize) {
		Waiwang2VideoExample example = new Waiwang2VideoExample();
		Waiwang2VideoExample.Criteria criteria = example.createCriteria();
		criteria.andDurationIsNotNull();
		// 排除 pantag 不含 http 的结果
		criteria.andPantagLike("%http%");
		example.setOrderByClause("dt desc");
		
		PageHelper.startPage(page, pageSize);
		return waiwang2VideoMapper.selectByExample(example);
	}

	/**
	 * 判断是否为 VIP 频道缓存链接（秒发，不计入每日限制）
	 */
	private boolean isVipChannelLink(String url) {
		return url != null && (url.contains("t.me/c/3576154874") || url.contains("telegram.me/c/3576154874"));
	}

	/**
	 * 判断该次提取是否属于会员免费秒发资源
	 * @param pri 指令前缀
	 * @param byString tria/网盘/频道链接等实际资源地址
	 */
	private boolean isVipFreeInstant(String pri, String byString) {
		// bc 无条件；tg 仅当 author（byString）有值时才属于会员免费秒发
		return "bc".equals(pri) || ("tg".equals(pri) && byString != null && !byString.trim().isEmpty()) || isVipChannelLink(byString);
	}

	/**
	 * 获取搜索结果/记事本中的会员免费秒发标记
	 */
	private String getVipFreeTag(Object video) {
		if (video instanceof cn.exrick.manager.pojo.Waiwang2Video) {
			return "【会员免费】【秒发】"; // bc
		}
		if (video instanceof cn.exrick.manager.pojo.WaiwangVideo) {
			// tg：author（实际资源地址）有值才属于会员免费秒发，否则不打标
			String author = ((cn.exrick.manager.pojo.WaiwangVideo) video).getAuthor();
			return author != null && !author.trim().isEmpty() ? "【会员免费】【秒发】" : "";
		}
		if (video instanceof cn.exrick.manager.pojo.ZmqVideo) {
			cn.exrick.manager.pojo.ZmqVideo zv = (cn.exrick.manager.pojo.ZmqVideo) video;
			String tria = zv.getTria();
			if (isVipChannelLink(tria)) return "【会员免费】【秒发】";
			// 2026-09-22 wckbot 且 tria=sk（未提取）标注冷门资源
			String zurl = zv.getUrl();
			// 2026-09-22 wckbot 且 tria=sk（未提取）标注冷门资源；2026-10-28 永慕(yongmufoot) 同规则
			if (zurl != null && (zurl.contains("wckbot") || zurl.contains("yongmufoot")) && "sk".equals(tria)) return "【冷门资源，3元一个，私聊机器人提取】";
			return "";
		}
		if (video instanceof cn.exrick.manager.pojo.WanwuVideo) {
			String tria = ((cn.exrick.manager.pojo.WanwuVideo) video).getTria();
			return isVipChannelLink(tria) ? "【会员免费】【秒发】" : "";
		}
		if (video instanceof cn.exrick.manager.pojo.Taolu3Video) {
			String tria = ((cn.exrick.manager.pojo.Taolu3Video) video).getTria();
			return isVipChannelLink(tria) ? "【会员免费】【秒发】" : "";
		}
		return "";
	}

	private String getVipFreeTagForChannel(Map<String, Object> video) {
		if (video == null) return "";
		String url = video.get("url") != null ? video.get("url").toString() : "";
		return isVipChannelLink(url) ? "【会员免费】【秒发】" : "";
	}

	private boolean checkDailyExtractLimit(Long userId, int topicok, String pri, String byString) {
		if (userId == null) {
			return true;
		}
		// bc/tg 类型及 VIP 频道缓存链接不限制次数
		if (isVipFreeInstant(pri, byString)) {
			return true;
		}
		String today = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
		String dailyKey = "extract:daily:tg_search:" + userId + ":" + today;
		String countStr = jedisClient.get(dailyKey);
		int count = 0;
		if (countStr != null && !countStr.isEmpty()) {
			try {
				count = Integer.parseInt(countStr);
			} catch (NumberFormatException e) {
				count = 0;
			}
		}
		return count < extractDailyLimit;
	}

	private void incrDailyExtractCount(Long userId, int topicok, String pri, String byString) {
		if (userId == null) {
			return;
		}
		// bc/tg 类型及 VIP 频道缓存链接不计数
		if (isVipFreeInstant(pri, byString)) {
			return;
		}
		String today = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
		String dailyKey = "extract:daily:tg_search:" + userId + ":" + today;
		long count = jedisClient.incr(dailyKey);
		if (count == 1) {
			// 第一次设置，添加当天24:00过期
			try {
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
				java.util.Date now = new java.util.Date();
				java.util.Date midnight = sdf.parse(sdf.format(now));
				midnight = new java.util.Date(midnight.getTime() + 24 * 60 * 60 * 1000);
				long ttl = (midnight.getTime() - now.getTime()) / 1000;
				jedisClient.expire(dailyKey, (int) ttl);
			} catch (Exception e) {
				jedisClient.expire(dailyKey, 86400);
			}
		}
	}

	private String sortNotepadByTime(String content) {
		if (content == null || content.isEmpty()) {
			return content;
		}
		String[] lines = content.split("\r\n");
		ArrayList<String> headers = new ArrayList<String>();
		ArrayList<String[]> entries = new ArrayList<String[]>();
		for (String line : lines) {
			if (line.isEmpty()) continue;
			if (Character.isDigit(line.charAt(0)) && line.contains("\t")) {
				String time = extractTimeFromLine(line);
				entries.add(new String[]{line, time != null ? time : ""});
				continue;
			}
			headers.add(line);
		}
		entries.sort((a, b) -> {
			if (a[1].isEmpty() && b[1].isEmpty()) {
				return 0;
			}
			if (a[1].isEmpty()) {
				return 1;
			}
			if (b[1].isEmpty()) {
				return -1;
			}
			return b[1].compareTo(a[1]);
		});
		StringBuilder sb = new StringBuilder();
		for (String h : headers) {
			sb.append(h).append("\r\n");
		}
		int num = 1;
		for (String[] e : entries) {
			String line = e[0];
			line = line.replace("\n", "").replace("\r", "");
			int firstTab = line.indexOf("\t");
			if (firstTab > 0) {
				line = num + line.substring(firstTab);
			}
			sb.append(line).append("\r\n");
			num++;
		}
		return sb.toString();
	}

	private String extractTimeFromLine(String line) {
		int idx = line.indexOf("\t时间：");
		if (idx == -1) {
			idx = line.indexOf("\t时间:");
		}
		if (idx == -1) {
			return "";
		}
		int start = idx + 4;
		int end = line.indexOf("\t", start);
		if (end == -1) {
			end = line.length();
		}
		return line.substring(start, end).trim();
	}

	private String cleanSearchTitle(String title) {
		if (title == null) {
			return "";
		}
		String s = title.replace("\r", "").replace("\n", "").replace("#", "");
		s = s.replace("!$CDATA$", "").replace("![CDATA[", "").replace("]]", "");
		s = s.replace("【视频】", "").replace("【图片】", "");
		int idx = s.toLowerCase().indexOf(".mp4");
		if (idx >= 0) {
			s = s.substring(0, idx);
		}
		return s.trim();
	}


	private String secondsToHMS(String secondsStr) {
		if (secondsStr == null || secondsStr.isEmpty()) {
			return "";
		}
		try {
			int sec = Integer.parseInt(secondsStr);
			int h = sec / 3600;
			int m = sec % 3600 / 60;
			int s = sec % 60;
			if (h > 0) {
				return String.format("%d:%02d:%02d", h, m, s);
			}
			return String.format("%d:%02d", m, s);
		} catch (NumberFormatException e) {
			return secondsStr;
		}
	}

}
