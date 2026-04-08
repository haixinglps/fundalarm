package cn.exrick.manager.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import cn.exrick.manager.isearch.Isearch;
import cn.exrick.manager.isearch.query.Search;
import cn.exrick.manager.isearch.query.SearchFactory;
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

	// 由 TelegramBotConfig 手动设置，解决循环依赖
	public void setTelegramChannelMonitor(TelegramChannelMonitor monitor) {
		this.telegramChannelMonitor = monitor;
	}

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
			if (text.indexOf(" ") != -1) {
				String[] textArr = text.split(" ");
				for (String item : textArr) {
					criteriazmq.andTitleLike("%" + item + "%");

				}
			} else
				criteriazmq.andTitleLike("%" + text + "%");

		}
//		criteriaWw2.andTitleNotEqualTo("");
		criteriazmq.andDurationIsNotNull();
		exampleWaiwangzmq.setOrderByClause("vid desc");
		PageHelper.startPage(1, 10);
		List<ZmqVideo> listWaiwangzmq = zmqVideoMapper.selectByExample(exampleWaiwangzmq);
		PageInfo<ZmqVideo> pizmqInfo = new PageInfo<ZmqVideo>(listWaiwangzmq);
		System.out.println("zmq查询完成，结果数: " + listWaiwangzmq.size() + ", 总数: " + pizmqInfo.getTotal());

		re.append("网页搜索 " + pizmqInfo.getTotal() + "条\n");
		mediaList = new ArrayList<InputMedia>();

		for (int i = 0; i < listWaiwangzmq.size(); i++) {
			String link = TelegramDeepLink.generateLink(telegramChannelMonitor.getBotUsername(),
					"zm" + listWaiwangzmq.get(i).getVid());
			String tt = listWaiwangzmq.get(i).getTitle();
//			if (tt.length() > 60)
//				tt = tt.substring(0, 60);
			// re.append(i + "\t" + tt + "\t" + "机器人口令：bc" + listWaiwang2.get(i).getId() +
			// "\t" + link + "\n");
			if (listWaiwangzmq.get(i).getCover() == null)
				listWaiwangzmq.get(i).setCover(
						"https://s3imgqnv1.ikzuo.com/app/user/117431_20250825050414_8c56e54391d88227e0081a266509c952.png?imageView2/2/w/56");
			InputMediaPhoto photo = new InputMediaPhoto(listWaiwangzmq.get(i).getCover());
			photo.setCaption(i + "\t" + tt + "\t时长：" + listWaiwangzmq.get(i).getDuration() + "\t" + "机器人口令：zm"
					+ listWaiwangzmq.get(i).getVid() + "\t" + link); // 每张图片独立描述
			mediaList.add(photo);
//			if (!UrlValidator.isUrlValid(listWaiwang2.get(i).getCover())) {
//
//				listWaiwang2.get(i).setCover(beiyongUrl);
//				re.append(photo.getCaption() + "\n");
//			} else {

			int robotRe = telegramChannelMonitor.sendChannelReplyWithPhoto(chatId, mediaList.get(i).getCaption(),
					channelMsg.getMessageId(), listWaiwangzmq.get(i).getCover());
//			try {
////				Thread.sleep(5000);
//			} catch (InterruptedException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
//			}
			if (robotRe == 0)
				re.append(photo.getCaption() + "\n");
//			}

		}
		} catch (Exception e) {
			System.err.println("【搜索异常】zmq查询或发送失败: " + e.getMessage());
			e.printStackTrace();
		}

		Waiwang2VideoExample exampleWaiwang2 = new Waiwang2VideoExample();
		cn.exrick.manager.pojo.Waiwang2VideoExample.Criteria criteriaWw2 = exampleWaiwang2.createCriteria();
		if (text != null) {
			if (text.indexOf(" ") != -1) {
				String[] textArr = text.split(" ");
				for (String item : textArr) {
					criteriaWw2.andTitleLike("%" + item + "%");
				}
			} else
				criteriaWw2.andTitleLike("%" + text + "%");

		}
//		criteriaWw2.andTitleNotEqualTo("");
		criteriaWw2.andDurationIsNotNull();
		exampleWaiwang2.setOrderByClause("dt desc");
		PageHelper.startPage(1, 10);
		List<Waiwang2Video> listWaiwang2 = waiwang2VideoMapper.selectByExample(exampleWaiwang2);
		PageInfo<Waiwang2Video> pibcInfo = new PageInfo<Waiwang2Video>(listWaiwang2);

		re.append("最新作品搜索 " + pibcInfo.getTotal() + "条\n");
		mediaList = new ArrayList<InputMedia>();

		for (int i = 0; i < listWaiwang2.size(); i++) {
			String link = TelegramDeepLink.generateLink(telegramChannelMonitor.getBotUsername(),
					"bc" + listWaiwang2.get(i).getId());
			String tt = listWaiwang2.get(i).getTitle().split("_")[0];
//			if (tt.length() > 60)
//				tt = tt.substring(0, 60);
			// re.append(i + "\t" + tt + "\t" + "机器人口令：bc" + listWaiwang2.get(i).getId() +
			// "\t" + link + "\n");
			if (listWaiwang2.get(i).getCover() == null)
				listWaiwang2.get(i).setCover(
						"https://s3imgqnv1.ikzuo.com/app/user/117431_20250825050414_8c56e54391d88227e0081a266509c952.png?imageView2/2/w/56");
			InputMediaPhoto photo = new InputMediaPhoto(listWaiwang2.get(i).getCover());
			photo.setCaption(i + "\t" + tt + "\tid:" + listWaiwang2.get(i).getUid() + "\t作者："
					+ listWaiwang2.get(i).getNickname() + "\t时长：" + listWaiwang2.get(i).getDuration() + "分\t时间："
					+ listWaiwang2.get(i).getDt() + "\t" + "机器人口令：bc" + listWaiwang2.get(i).getId() + "\t" + link); // 每张图片独立描述
			mediaList.add(photo);
//			if (!UrlValidator.isUrlValid(listWaiwang2.get(i).getCover())) {
//
//				listWaiwang2.get(i).setCover(beiyongUrl);
//				re.append(photo.getCaption() + "\n");
//			} else {

			int robotRe = telegramChannelMonitor.sendChannelReplyWithPhoto(chatId, mediaList.get(i).getCaption(),
					channelMsg.getMessageId(), listWaiwang2.get(i).getCover());
//			try {
////				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			if (robotRe == 0)
				re.append(photo.getCaption() + "\n");
//			}

		}

		WanwuVideoExample example = new WanwuVideoExample();

		cn.exrick.manager.pojo.WanwuVideoExample.Criteria criteria = example.createCriteria();
		if (text != null) {
			if (text.indexOf(" ") != -1) {
				String[] textArr = text.split(" ");
				for (String item : textArr) {
					criteria.andTitleLike("%" + item + "%");
				}
			} else
				criteria.andTitleLike("%" + text + "%");
		}
//		criteriaTaolu.andTitleNotEqualTo("");

		example.setOrderByClause("sales desc");
		PageHelper.startPage(1, 10);
		List<WanwuVideo> list = wanwuVideoMapper.selectByExample(example);
		PageInfo<WanwuVideo> piwwInfo = new PageInfo<WanwuVideo>(list);

		re.append("玩物搜索 " + piwwInfo.getTotal() + "条（只返回前10条，如果需要所有的找客服要）\n");
		mediaList = new ArrayList<InputMedia>();

		for (int i = 0; i < list.size(); i++) {
			String link = TelegramDeepLink.generateLink(telegramChannelMonitor.getBotUsername(),
					"ww" + list.get(i).getVid());

//			if (i == 3 || i == 4)
//				continue;

//			System.out.println("--------------------index:" + i + "\t" + list.get(i).getTitle() + "\t"
//					+ list.get(i).getAddtime() + "\t" + "机器人口令：ww" + list.get(i).getVid() + "\t" + link + "");

			InputMediaPhoto photo = new InputMediaPhoto(list.get(i).getCover());
			photo.setCaption(i + "\t" + list.get(i).getTitle().split("_")[0] + "\tid:" + list.get(i).getAuthor()
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

			int robotResult = telegramChannelMonitor.sendChannelReplyWithPhoto(chatId, mediaList.get(i).getCaption(),
					channelMsg.getMessageId(), list.get(i).getCover());

			if (robotResult == 0)
				re.append(photo.getCaption() + "\n");
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
			if (text.indexOf(" ") != -1) {
				String[] textArr = text.split(" ");
				for (String item : textArr) {
					criteriaTaolu.andTitleLike("%" + item + "%");
				}
			} else
				criteriaTaolu.andTitleLike("%" + text + "%");

		}
//		criteriaTaolu.andTitleNotEqualTo("");

		exampleTaolu.setOrderByClause("vid desc");
		PageHelper.startPage(1, 10);
		List<Taolu3Video> listTaolu = taolu3VideoMapper.selectByExample(exampleTaolu);
		PageInfo<Taolu3Video> pitlInfo = new PageInfo<Taolu3Video>(listTaolu);

		re.append("淘露搜索 " + pitlInfo.getTotal() + "条（部分不能播放的url找客服要mp4，新作品也找客服要）\n");
		mediaList = new ArrayList<InputMedia>();

		for (int i = 0; i < listTaolu.size(); i++) {
			String link = TelegramDeepLink.generateLink(telegramChannelMonitor.getBotUsername(),
					"tl" + listTaolu.get(i).getVid());
			InputMediaPhoto photo = new InputMediaPhoto(listTaolu.get(i).getCover());
			photo.setCaption(i + "\t" + listTaolu.get(i).getTitle().split("_")[0] + "\tid:"
					+ listTaolu.get(i).getAuthor() + "\t作者：" + listTaolu.get(i).getUrlkey2() + "\t时间："
					+ listTaolu.get(i).getDt() + "\t" + "机器人口令：tl" + listTaolu.get(i).getVid() + "\t" + link); // 每张图片独立描述
			mediaList.add(photo);

//			if (!UrlValidator.isUrlValid(listTaolu.get(i).getCover())) {
//
//				listTaolu.get(i).setCover(beiyongUrl);
//				re.append(i + "\t" + listTaolu.get(i).getTitle() + "\t" + listTaolu.get(i).getVid() + "\t" + "机器人口令：tl"
//						+ listTaolu.get(i).getVid() + "\t" + link + "\n");
//			} else {

			int robotRe = telegramChannelMonitor.sendChannelReplyWithPhoto(chatId, mediaList.get(i).getCaption(),
					channelMsg.getMessageId(), listTaolu.get(i).getCover());

			if (robotRe == 0)
				re.append(photo.getCaption() + "\n");
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
			if (text.indexOf(" ") != -1) {
				String[] textArr = text.split(" ");
				for (String item : textArr) {
					criteriaWw.andTitleLike("%" + item + "%");

				}
			} else
				criteriaWw.andTitleLike("%" + text + "%");

		}
		criteriaWw.andTitleNotEqualTo("");
		criteriaWw.andDurationGreaterThan("600");
		exampleWaiwang.setOrderByClause("dt desc");
		PageHelper.startPage(1, 10);
		List<WaiwangVideo> listWaiwang = waiwangVideoMapper.selectByExample(exampleWaiwang);
		PageInfo<WaiwangVideo> pitgInfo = new PageInfo<WaiwangVideo>(listWaiwang);

		re.append("电报搜索 " + pitgInfo.getTotal() + "条\n");
		mediaList = new ArrayList<InputMedia>();
		StringBuffer ttt = new StringBuffer();

		for (int i = 0; i < listWaiwang.size(); i++) {
			String link = TelegramDeepLink.generateLink(telegramChannelMonitor.getBotUsername(),
					"tg" + listWaiwang.get(i).getId());
			String tt = listWaiwang.get(i).getTitle().replace("#", "");
			if (tt.length() > 60)
				tt = tt.substring(0, 60);
			String duration = listWaiwang.get(i).getDuration();
			int sec = Integer.parseInt(duration);
			int min = sec / 60;
			int second = sec % 60;
			re.append(i + "\t" + tt + "\t时间：" + listWaiwang.get(i).getDt() + "\t时长：" + min + ":" + second + "分 \t"
					+ "机器人口令：tg" + listWaiwang.get(i).getId() + "\t" + link + "\n");

			InputMediaPhoto photo = new InputMediaPhoto();
			photo.setCaption(i + "\t" + tt + "\t" + listWaiwang.get(i).getDuration() + "秒\t"
					+ listWaiwang.get(i).getDt() + "\t" + "机器人口令：tg" + listWaiwang.get(i).getId() + "\t" + link); // 每张图片独立描述
			mediaList.add(photo);

//			int robotRe = telegramChannelMonitor.sendChannelReplyWithPhoto(chatId, photo.getCaption(),
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
				search.andCategory("DM", "t.me");
				search.andText("TX", text);
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
			re.append("频道搜索 " + hits.getTotal() + "条\n");
			mediaList = new ArrayList<InputMedia>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			for (int i = 0; i < hits.size(); i++) {
				Hit hit = hits.get(i);
				String link = TelegramDeepLink.generateLink(telegramChannelMonitor.getBotUsername(),
						"ch" + hit.getId());
				String tt = hit.getArticle().getString("TX").replace("#", "");
				if (tt.length() > 60)
					tt = tt.substring(0, 60);
				long rq = hit.getArticle().getLong("RQ");
				String dt = sdf.format(new Date());

				re.append(i + "\t" + tt + "\t时间：" + dt + "\t时长：" + hit.getArticle().getString("CC") + "分 \t"
						+ "机器人口令：ch" + hit.getId() + "\t" + link + "\n");

//				InputMediaPhoto photo = new InputMediaPhoto();
//				photo.setCaption(i + "\t" + tt + "\t" + listWaiwang.get(i).getDuration() + "秒\t"
//						+ dt + "\t" + "机器人口令：ch" + hit.getId() + "\t" + link); // 每张图片独立描述
//				mediaList.add(photo);

//			int robotRe = telegramChannelMonitor.sendChannelReplyWithPhoto(chatId, photo.getCaption(),
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
		String validRes = escapeMarkdown(responseT);

//		telegramChannelMonitor.sendChannelReply(chatId, validRes, channelMsg.getMessageId());

		int maxLength = 4096;
		for (int i = 0; i < validRes.length(); i += maxLength) {
			String chunk = validRes.substring(i, Math.min(i + maxLength, validRes.length()));
//			SendMessage message = new SendMessage(chatId.toString(), chunk);

			telegramChannelMonitor.sendChannelReply(chatId, chunk, channelMsg.getMessageId());

//		 telegramChannelMonitor.sendChannelReply(chatId, validRes,
//		 channelMsg.getMessageId());

		}
		if (channelMsg.getFrom().getUserName() != null) {
			if (text == null) {
				text = "";
			}
			String info = channelMsg.getFrom().getUserName() + "_" + text.replace("_", "").replace(" ", "").replace("\n", "").replace("\r", "").replace("\t", "");
			// 注册事务提交后的回调
			org.springframework.transaction.support.TransactionSynchronizationManager
					.registerSynchronization(new TransactionSynchronizationAdapter() {
						@Override
						public void afterCommit() {
							// 使用异步服务发送消息
							// 推送数据到redis队列里进行计算。

							publisher.publishEventAsync(info, update);
//							jedisClient.lpush("videos", info);
						}
					});
		}

		return;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void dealGetWork(Update update) {

		//
		Integer messageThreadId = null;
		// 确保更新包含消息
		if (!update.getMessage().hasText()) {
			System.out.println("没有文本");
			return;
		}

		Message receivedMessage = update.getMessage();
		Long chatId = receivedMessage.getChatId();

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
		Long targetGroupId = -1003867299066L; // 替换为你的群组ID（带 -100 前缀）
		int groupok = 0;
		int topicok = 0;
		if (!chatId.equals(targetGroupId)) {
			System.out.println("不是目标群组，当前群组ID: " + chatId);

		} else {
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
			wallet.setBalance(5);
			wallet.setCreated(new Date());
			wallet.setNickname(identifier);
			wallet.setUid(receivedMessage.getFrom().getId() + "");
			wallet.setVersion(0);
			tbWalletMapper.insertSelective(wallet);
			wallets.add(wallet);

		}
		if (topicok == 0 || topicok == 2)
			if (wallets.size() == 0 || wallets.get(0).getBalance() == null || wallets.get(0).getBalance() == 0) {
				replyText += "请找客服qq2167485304充值\n";
				replyMessage.setText(replyText);
				try {
					telegramChannelMonitor.execute(replyMessage);
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
		int zhindex = 0;
		int yc = 0;

		String table = "wanwu_video";
//		if (receivedText.length() < 3) {
//			replyText += "格式有误，正确格式是：ww12345 ww44322 tl33445 tl12423 ww4789 ww1989333\n";
//			replyMessage.setText(replyText);
//
//			try {
//				telegramChannelMonitor.execute(replyMessage);
//			} catch (TelegramApiException e) {
//				// TODO Auto-generated catch block
//				log.error("服务器异常");
//				e.printStackTrace();
//			}
//			return;
//		}
		String pri = receivedText.substring(0, 2);
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
					telegramChannelMonitor.execute(replyMessage);
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
//							telegramChannelMonitor.execute(replyMessage);
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
					telegramChannelMonitor.execute(replyMessage);
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
					telegramChannelMonitor.execute(replyMessage);
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
							telegramChannelMonitor.execute(replyMessage);
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

				if (byString.contentEquals("sk")) {

					if (receivedMessage.getFrom().getUserName() == null) {
						// TODO: handle exception
						replyText += "请到电报app左上角-设置，设置一个用户名，这个作品需要下载后发给你，全自动发送，无需找客服";
						replyMessage.setText(replyText);

						try {
							telegramChannelMonitor.execute(replyMessage);
						} catch (TelegramApiException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							log.error("服务器异常");
						}
						return;

					}

				}

				wpString = listZmq.get(0).getUptag3() + "";
				// 如果url中含有过期标记，则执行一个再次采集操作 54545采集。
				ZmqVideo record = new ZmqVideo();
				record.setGoodtag(1);
				// 更新数据库：
				zmqVideoMapper.updateByExampleSelective(record, examplezmq);

			} else {
				replyText += "id可能有误\n";
				replyMessage.setText(replyText);

				try {
					telegramChannelMonitor.execute(replyMessage);
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
					telegramChannelMonitor.execute(replyMessage);
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

				if (byString.contentEquals("sk")) {

					if (receivedMessage.getFrom().getUserName() == null) {
						// TODO: handle exception
						replyText += "请到电报app左上角-设置，设置一个用户名，这个作品需要下载后发给你，全自动发送，无需找客服";
						replyMessage.setText(replyText);

						try {
							telegramChannelMonitor.execute(replyMessage);
						} catch (TelegramApiException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							log.error("服务器异常");
						}
						return;

					}

				}
				// 更新数据库：
				taolu3VideoMapper.updateByExampleSelective(record, exampletl);

			} else {
				replyText += "id可能有误\n";
				replyMessage.setText(replyText);

				try {
					telegramChannelMonitor.execute(replyMessage);
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
					telegramChannelMonitor.execute(replyMessage);
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
					telegramChannelMonitor.execute(replyMessage);
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
					telegramChannelMonitor.execute(replyMessage);
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					log.error("服务器异常");
				}
				return;
			}
			if (hits != null) {
				url = hits.get(0).getArticle().getString("UR");
				title = hits.get(0).getArticle().getString("TX");
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

				if (byString == null || byString.contentEquals("sk") || byString.contentEquals("")
						|| byString.contentEquals("null")) {

					if (receivedMessage.getFrom().getUserName() == null) {
						// TODO: handle exception
						replyText += "请到电报app左上角-设置，设置一个用户名，这个作品需要下载后发给你，全自动发送，无需找客服";
						replyMessage.setText(replyText);

						try {
							telegramChannelMonitor.execute(replyMessage);
						} catch (TelegramApiException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							log.error("服务器异常");
						}
						return;

					}

				}
				// 更新数据库：
				// taolu3VideoMapper.updateByExampleSelective(record, exampletl);

			} else {
				replyText += "id可能有误\n";
				replyMessage.setText(replyText);

				try {
					telegramChannelMonitor.execute(replyMessage);
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
					telegramChannelMonitor.execute(replyMessage);
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

					if (receivedMessage.getFrom().getUserName() == null) {
						// TODO: handle exception
						replyText += "请到电报app左上角-设置，设置一个用户名，这个作品需要下载后发给你，全自动发送，无需找客服";
						replyMessage.setText(replyText);

						try {
							telegramChannelMonitor.execute(replyMessage);
						} catch (TelegramApiException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							log.error("服务器异常");
						}
						return;

					}

				}
				System.out.println("---------------------------------------------搜索成功3");

				video.setGoodtag(1);
				// 更新数据库：
				waiwangVideoMapper.updateByPrimaryKeySelective(video);

			} else {
				replyText += "id可能有误\n";
				replyMessage.setText(replyText);

				try {
					telegramChannelMonitor.execute(replyMessage);
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
					telegramChannelMonitor.execute(replyMessage);
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
					telegramChannelMonitor.execute(replyMessage);
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
					telegramChannelMonitor.execute(replyMessage);
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					log.error("服务器异常");
				}
				return;
			}
			Waiwang2VideoExample examplezb = new Waiwang2VideoExample();
			examplezb.createCriteria().andUidEqualTo(vid + "");
			examplezb.setOrderByClause("dt desc");
			PageHelper.startPage(1, 1);
			List<Waiwang2Video> videozbs = waiwang2VideoMapper.selectByExample(examplezb);
			if (videozbs.size() > 0) {
				Waiwang2Video videozb = videozbs.get(0);
				url = videozb.getType();
				title = videozb.getTitle();
//				videozb.setGoodtag(1);
//				// 更新数据库：
//				waiwang2VideoMapper.updateByPrimaryKeySelective(video2);

			} else {
				replyText += "id可能有误\n";
				replyMessage.setText(replyText);

				try {
					telegramChannelMonitor.execute(replyMessage);
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					log.error("服务器异常");
				}
				return;
			}
			break;
		default:
//			replyText += "指令可能有误 ww tl tg开头才行\n";
//			replyMessage.setText(replyText);
//
//			try {
//				telegramChannelMonitor.execute(replyMessage);
//			} catch (TelegramApiException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//				log.error("服务器异常");
//			}
			dealSearch(update);
			return;
		}
		title = title.replace(",", "").replace("___", "");

		if (pri.contentEquals("bc")) {
			replyText += "为你提取到的作品链接是：\n" + title + "\n" + url + "\n本地路径：" + byString + "\n所在网盘：" + wpString;
			if (byString != null) {

				File file = new File(byString);
				if (file.exists() || byString.indexOf("t.me") != -1) {

					if (receivedMessage.getFrom().getUserName() == null) {
						// TODO: handle exception
						replyText += "请到电报app左上角-设置，设置一个用户名，这个作品需要下载后发给你，全自动发送，无需找客服";
						replyMessage.setText(replyText);

						try {
							telegramChannelMonitor.execute(replyMessage);
						} catch (TelegramApiException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							log.error("服务器异常");
						}
						return;

					}

					replyText += "\n" + "稍后会自动发送你视频文件，因为这个文件刚采集的";
//					String info = identifier + "," + byString + "," + receivedText+","+;
					String info = identifier + "," + byString + "," + title + "," + receivedText + "," + chatId + ","
							+ coverString;
					info = info + "," + byString + ",0," + author + "," + zhindex + "," + topicok;
					final String info2 = info;
					// 注册事务提交后的回调
					org.springframework.transaction.support.TransactionSynchronizationManager
							.registerSynchronization(new TransactionSynchronizationAdapter() {
								@Override
								public void afterCommit() {
									// 使用异步服务发送消息
									// 推送数据到redis队列里进行计算。

//								publisher.publishEventAsync(nt.getEventContent(), transactionCode);
									jedisClient.rpush("videos", info2);
								}
							});
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
			// 校验rtmp链接是否报废：

			if (!UrlValidator.validateWithFFmpeg(url)) {
				replyText += "主播还未开播或直播已结束";
				replyMessage.setText(replyText);

				try {
					telegramChannelMonitor.execute(replyMessage);
				} catch (TelegramApiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					log.error("服务器异常");
				}
				return;
			}
			replyText += "为你提取到的直播地址是：\n" + title + "\n" + url;

			replyText += "\n(直播链接需要用手机qq浏览器打开【万一打不开，赶快找客服，可能改版了或者你手机网络问题】)";
		} else {

			String url2 = url;
			String pan2 = byString;

			if (identifier.contentEquals("@kaikak09818")) {
				yc = 0;
			}
			if (identifier.contentEquals("@linyuan56")) {
				yc = 0;
			}
			if (yc == 1) {
				url2 = "";
				pan2 = "";

			}

			replyText += "为你提取到的作品链接是：\n" + title + "\n" + url2 + "\n网盘分享链接：" + pan2 + "\n所在网盘：" + wpString;
			if ((pri.contentEquals("tl") && (byString.contentEquals("sk") || byString.indexOf("t.me") != -1
					|| byString.indexOf("pikpak") != -1))) {

				if (receivedMessage.getFrom().getUserName() == null) {
					// TODO: handle exception
					replyText += "请到电报app左上角-设置，设置一个用户名，这个作品需要下载后发给你，全自动发送，无需找客服";
					replyMessage.setText(replyText);

					try {
						telegramChannelMonitor.execute(replyMessage);
					} catch (TelegramApiException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						log.error("服务器异常");
					}
					return;

				}

				replyText += "\n" + "稍后会自动发送你视频文件，因为没有网盘分享链接";
				String info = identifier + "," + url + "," + title + "," + receivedText + "," + chatId + ","
						+ coverString;
//				if (byString.indexOf("pikpak") != -1) {
				info += "," + byString + "," + wpString + "," + author + "," + zhindex + "," + topicok;
//				}
				final String finalInfo = info; // 创建 final 副本

				// 注册事务提交后的回调
				org.springframework.transaction.support.TransactionSynchronizationManager
						.registerSynchronization(new TransactionSynchronizationAdapter() {
							@Override
							public void afterCommit() {
								// 使用异步服务发送消息
								// 推送数据到redis队列里进行计算。

//								publisher.publishEventAsync(nt.getEventContent(), transactionCode);
								jedisClient.rpush("videos", finalInfo);
							}
						});

			}

			if ((pri.contentEquals("zm") && (byString.contentEquals("sk") || byString.indexOf("t.me") != -1
					|| byString.indexOf("pikpak") != -1))) {

				if (receivedMessage.getFrom().getUserName() == null) {
					// TODO: handle exception
					replyText += "请到电报app左上角-设置，设置一个用户名，这个作品需要下载后发给你，全自动发送，无需找客服";
					replyMessage.setText(replyText);

					try {
						telegramChannelMonitor.execute(replyMessage);
					} catch (TelegramApiException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						log.error("服务器异常");
					}
					return;

				}

				replyText += "\n" + "稍后会自动发送你视频文件，因为没有网盘分享链接";
				String info = identifier + "," + url + "," + title + "," + receivedText + "," + chatId + ","
						+ coverString;
//				if (byString.indexOf("pikpak") != -1) {
//					info += "," + byString + "," + wpString;
//				}
				info += "," + byString + "," + wpString + "," + author + "," + zhindex + "," + topicok;

				final String finalInfo = info; // 创建 final 副本

				// 注册事务提交后的回调
				org.springframework.transaction.support.TransactionSynchronizationManager
						.registerSynchronization(new TransactionSynchronizationAdapter() {
							@Override
							public void afterCommit() {
								// 使用异步服务发送消息
								// 推送数据到redis队列里进行计算。

//								publisher.publishEventAsync(nt.getEventContent(), transactionCode);
								jedisClient.rpush("videos", finalInfo);
							}
						});

			}

			if ((pri.contentEquals("ww") && (byString.contentEquals("sk") || byString.indexOf("t.me") != -1
					|| byString.indexOf("pikpak") != -1))) {

				if (receivedMessage.getFrom().getUserName() == null) {
					// TODO: handle exception
					replyText += "请到电报app左上角-设置，设置一个用户名，这个作品需要下载后发给你，全自动发送，无需找客服";
					replyMessage.setText(replyText);

					try {
						telegramChannelMonitor.execute(replyMessage);
					} catch (TelegramApiException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						log.error("服务器异常");
					}
					return;

				}

				replyText += "\n" + "稍后会自动发送你视频文件，因为没有网盘分享链接";
				String info = identifier + "," + url + "," + title + "," + receivedText + "," + chatId + ","
						+ coverString;
//				if (byString.indexOf("pikpak") != -1) {
//				info += "," + byString + "," + wpString + "," + author;
				info += "," + byString + "," + wpString + "," + author + "," + zhindex + "," + topicok;

//				}
				final String finalInfo = info; // 创建 final 副本

				// 注册事务提交后的回调
				org.springframework.transaction.support.TransactionSynchronizationManager
						.registerSynchronization(new TransactionSynchronizationAdapter() {
							@Override
							public void afterCommit() {
								// 使用异步服务发送消息
								// 推送数据到redis队列里进行计算。

//								publisher.publishEventAsync(nt.getEventContent(), transactionCode);
								jedisClient.rpush("videos", finalInfo);
							}
						});

			}
			if ((pri.contentEquals("tg"))
					&& (byString == null || byString.indexOf("t.me") != -1 || url.indexOf("t.me") != -1)) {// &&
				// byString
				// == null

				if (receivedMessage.getFrom().getUserName() == null) {
					// TODO: handle exception
					replyText += "请到电报app左上角-设置，设置一个用户名，这个作品需要下载后发给你，全自动发送，无需找客服";
					replyMessage.setText(replyText);

					try {
						telegramChannelMonitor.execute(replyMessage);
					} catch (TelegramApiException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						log.error("服务器异常");
					}
					return;

				}

				replyText += "\n" + "稍后会自动发送你视频文件，因为没有网盘分享链接";
				String info = identifier + "," + url + "," + title + "," + receivedText + "," + chatId + ","
						+ coverString;
				info += "," + byString + "," + wpString + "," + author + "," + zhindex + "," + topicok;
				final String info2 = info;
				// 注册事务提交后的回调
				org.springframework.transaction.support.TransactionSynchronizationManager
						.registerSynchronization(new TransactionSynchronizationAdapter() {
							@Override
							public void afterCommit() {
								// 使用异步服务发送消息
								// 推送数据到redis队列里进行计算。

//								publisher.publishEventAsync(nt.getEventContent(), transactionCode);
								jedisClient.rpush("videos", info2);
							}
						});

			}
			if ((pri.contentEquals("ch")) && (byString == null || byString.contentEquals("")
					|| byString.contentEquals("null") || byString.indexOf("t.me") != -1 || url.indexOf("t.me") != -1)) {// &&
				// byString
				// == null

				if (receivedMessage.getFrom().getUserName() == null) {
					// TODO: handle exception
					replyText += "请到电报app左上角-设置，设置一个用户名，这个作品需要下载后发给你，全自动发送，无需找客服";
					replyMessage.setText(replyText);

					try {
						telegramChannelMonitor.execute(replyMessage);
					} catch (TelegramApiException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						log.error("服务器异常");
					}
					return;

				}

				replyText += "\n" + "稍后会自动发送你视频文件，因为没有网盘分享链接";
				String info = identifier + "," + url + "," + title + "," + receivedText + "," + chatId + ","
						+ coverString;
				info += "," + byString + "," + wpString + "," + author + "," + zhindex + "," + topicok;
				final String info2 = info;
				// 注册事务提交后的回调
				org.springframework.transaction.support.TransactionSynchronizationManager
						.registerSynchronization(new TransactionSynchronizationAdapter() {
							@Override
							public void afterCommit() {
								// 使用异步服务发送消息
								// 推送数据到redis队列里进行计算。

//								publisher.publishEventAsync(nt.getEventContent(), transactionCode);
								jedisClient.rpush("videos", info2);
							}
						});

			}

		}

		replyMessage.setText(replyText);

		// 3. 可选：设置回复格式（Markdown）
//		replyMessage.enableMarkdown(true);
//
//		// 4. 可选：设置为引用回复
//		replyMessage.setReplyToMessageId(receivedMessage.getMessageId());
		// 开始修改用户余额

		// 注意版本号不能有误：
		if (topicok == 0 || topicok == 2) {
			wallet.setBalance(wallet.getBalance() - 1);
			wallet.setNickname(identifier);
			tbWalletMapper.updateByPrimaryKeySelective(wallet);
		}

		try {
			// 5. 发送回复
			telegramChannelMonitor.execute(replyMessage);
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
		telegramChannelMonitor.execute(editMsg);
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
		example.createCriteria().andVidEqualTo(Integer.parseInt(vid));

		Waiwang2Video record = new Waiwang2Video();
		record.setDuration(duration);
		record.setChannel("fj1");
		waiwang2VideoMapper.updateByExampleSelective(record, example);

		// TODO Auto-generated method stub

	}

	@Override
	public void updateLocation(String vid, String location) {

		Waiwang2VideoExample example = new Waiwang2VideoExample();
		example.createCriteria().andVidEqualTo(Integer.parseInt(vid));

		Waiwang2Video record = new Waiwang2Video();
		record.setPantag(location);
//		record.setChannel("fj1");
		waiwang2VideoMapper.updateByExampleSelective(record, example);

		// TODO Auto-generated method stub

	}

	@Override
	public void updateLocationtl(String vid, String location) {

		Taolu3VideoExample example = new Taolu3VideoExample();
		example.createCriteria().andVidEqualTo(Integer.parseInt(vid));

		Taolu3Video record = new Taolu3Video();
		record.setTria(location);
//		record.setChannel("fj1");
		taolu3VideoMapper.updateByExampleSelective(record, example);

		// TODO Auto-generated method stub

	}

	@Override
	public void updateLocationww(String vid, String location) {

		WanwuVideoExample example = new WanwuVideoExample();
		example.createCriteria().andVidEqualTo(Integer.parseInt(vid));

		WanwuVideo record = new WanwuVideo();
		record.setTria(location);
//		record.setChannel("fj1");
		wanwuVideoMapper.updateByExampleSelective(record, example);

		// TODO Auto-generated method stub

	}

	@Override
	public void updateUrl(String vid, String url) {

		Waiwang2VideoExample example = new Waiwang2VideoExample();
		example.createCriteria().andVidEqualTo(Integer.parseInt(vid));

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
		example.createCriteria().andVidEqualTo(Integer.parseInt(vid));

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
		example.createCriteria().andVidEqualTo(Integer.parseInt(vid));

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
		example.createCriteria().andIdEqualTo(Integer.parseInt(vid));

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
		example.createCriteria().andVidEqualTo(Integer.parseInt(vid));

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
		example.createCriteria().andVidEqualTo(Integer.parseInt(vid));

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
		example.createCriteria().andVidEqualTo(Integer.parseInt(vid));

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
		example.createCriteria().andIdEqualTo(Integer.parseInt(vid));

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
			if (text.indexOf(" ") != -1) {
				String[] textArr = text.split(" ");
				for (String item : textArr) {
					criteriazmq.andTitleLike("%" + item + "%");

				}
			} else
				criteriazmq.andTitleLike("%" + text + "%");

		}
//		criteriaWw2.andTitleNotEqualTo("");
		criteriazmq.andDurationIsNotNull();
		exampleWaiwangzmq.setOrderByClause("vid desc");
		PageHelper.startPage(1, 1000);
		List<ZmqVideo> listWaiwangzmq = zmqVideoMapper.selectByExample(exampleWaiwangzmq);
		PageInfo<ZmqVideo> pizmqInfo = new PageInfo<ZmqVideo>(listWaiwangzmq);

		re.append("网页搜索 " + pizmqInfo.getTotal() + "条\r\n");
		mediaList = new ArrayList<InputMedia>();

		for (int i = 0; i < listWaiwangzmq.size(); i++) {
			String link = TelegramDeepLink.generateLink(telegramChannelMonitor.getBotUsername(),
					"zm" + listWaiwangzmq.get(i).getVid());
			String tt = listWaiwangzmq.get(i).getTitle();
//			if (tt.length() > 60)
//				tt = tt.substring(0, 60);
			// re.append(i + "\t" + tt + "\t" + "机器人口令：bc" + listWaiwang2.get(i).getId() +
			// "\t" + link + "\n");
			if (listWaiwangzmq.get(i).getCover() == null)
				listWaiwangzmq.get(i).setCover(
						"https://s3imgqnv1.ikzuo.com/app/user/117431_20250825050414_8c56e54391d88227e0081a266509c952.png?imageView2/2/w/56");
			InputMediaPhoto photo = new InputMediaPhoto(listWaiwangzmq.get(i).getCover());
			String tit = i + "\t" + tt + "\t时长：" + listWaiwangzmq.get(i).getDuration() + "\t" + "机器人口令：zm"
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
			if (text.indexOf(" ") != -1) {
				String[] textArr = text.split(" ");
				for (String item : textArr) {
					criteriaWw2.andTitleLike("%" + item + "%");

				}
			} else
				criteriaWw2.andTitleLike("%" + text + "%");

		}
//		criteriaWw2.andTitleNotEqualTo("");
		criteriaWw2.andDurationIsNotNull();
		exampleWaiwang2.setOrderByClause("dt desc");
		PageHelper.startPage(1, 1000);
		List<Waiwang2Video> listWaiwang2 = waiwang2VideoMapper.selectByExample(exampleWaiwang2);
		PageInfo<Waiwang2Video> pibcInfo = new PageInfo<Waiwang2Video>(listWaiwang2);

		re.append("最新作品搜索 " + pibcInfo.getTotal() + "条\r\n");
		mediaList = new ArrayList<InputMedia>();

		for (int i = 0; i < listWaiwang2.size(); i++) {
			String link = TelegramDeepLink.generateLink(telegramChannelMonitor.getBotUsername(),
					"bc" + listWaiwang2.get(i).getId());
			String tt = listWaiwang2.get(i).getTitle().split("_")[0];
//			if (tt.length() > 60)
//				tt = tt.substring(0, 60);
			// re.append(i + "\t" + tt + "\t" + "机器人口令：bc" + listWaiwang2.get(i).getId() +
			// "\t" + link + "\n");
			if (listWaiwang2.get(i).getCover() == null)
				listWaiwang2.get(i).setCover(
						"https://s3imgqnv1.ikzuo.com/app/user/117431_20250825050414_8c56e54391d88227e0081a266509c952.png?imageView2/2/w/56");

			InputMediaPhoto photo = new InputMediaPhoto(listWaiwang2.get(i).getCover());
			String tit = i + "\t" + tt + "\tid:" + listWaiwang2.get(i).getUid() + "\t网名："
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
			if (text.indexOf(" ") != -1) {
				String[] textArr = text.split(" ");
				for (String item : textArr) {
					criteria.andTitleLike("%" + item + "%");

				}
			} else
				criteria.andTitleLike("%" + text + "%");
		}
//		criteriaTaolu.andTitleNotEqualTo("");

		example.setOrderByClause("sales desc");
		PageHelper.startPage(1, 1000);
		List<WanwuVideo> list = wanwuVideoMapper.selectByExample(example);
		PageInfo<WanwuVideo> piwwInfo = new PageInfo<WanwuVideo>(list);

		re.append("玩物搜索 " + piwwInfo.getTotal() + "条（只返回前1000条，如果需要所有的找客服要）\r\n");
		mediaList = new ArrayList<InputMedia>();

		for (int i = 0; i < list.size(); i++) {
			String link = TelegramDeepLink.generateLink(telegramChannelMonitor.getBotUsername(),
					"ww" + list.get(i).getVid());

//			if (i == 3 || i == 4)
//				continue;

//			System.out.println("--------------------index:" + i + "\t" + list.get(i).getTitle() + "\t"
//					+ list.get(i).getAddtime() + "\t" + "机器人口令：ww" + list.get(i).getVid() + "\t" + link + "");

			InputMediaPhoto photo = new InputMediaPhoto(list.get(i).getCover());

			String tt = list.get(i).getTitle().split("_")[0];

			String tit = i + "\t" + tt + "\tid:" + list.get(i).getAuthor() + "\t网名：" + list.get(i).getUrlkey2()
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

//			int robotResult = telegramChannelMonitor.sendChannelReplyWithPhoto(chatId, mediaList.get(i).getCaption(),
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
			if (text.indexOf(" ") != -1) {
				String[] textArr = text.split(" ");
				for (String item : textArr) {
					criteriaTaolu.andTitleLike("%" + item + "%");

				}
			} else
				criteriaTaolu.andTitleLike("%" + text + "%");

		}
//		criteriaTaolu.andTitleNotEqualTo("");

		exampleTaolu.setOrderByClause("vid desc");
		PageHelper.startPage(1, 1000);
		List<Taolu3Video> listTaolu = taolu3VideoMapper.selectByExample(exampleTaolu);
		PageInfo<Taolu3Video> pitlInfo = new PageInfo<Taolu3Video>(listTaolu);

		re.append("淘露搜索 " + pitlInfo.getTotal() + "\r\n");
		mediaList = new ArrayList<InputMedia>();

		for (int i = 0; i < listTaolu.size(); i++) {
			String link = TelegramDeepLink.generateLink(telegramChannelMonitor.getBotUsername(),
					"tl" + listTaolu.get(i).getVid());
			InputMediaPhoto photo = new InputMediaPhoto(listTaolu.get(i).getCover());

			String tt = listTaolu.get(i).getTitle().split("_")[0];

			String tit = i + "\t" + tt + "\tid:" + listTaolu.get(i).getAuthor() + "\t网名:"
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

//			int robotRe = telegramChannelMonitor.sendChannelReplyWithPhoto(chatId, mediaList.get(i).getCaption(),
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
			if (text.indexOf(" ") != -1) {
				String[] textArr = text.split(" ");
				for (String item : textArr) {
					criteriaWw.andTitleLike("%" + item + "%");

				}
			} else
				criteriaWw.andTitleLike("%" + text + "%");

		}
		criteriaWw.andTitleNotEqualTo("");
		criteriaWw.andDurationGreaterThan("600");
		exampleWaiwang.setOrderByClause("dt desc");
		PageHelper.startPage(1, 1000);
		List<WaiwangVideo> listWaiwang = waiwangVideoMapper.selectByExample(exampleWaiwang);
		PageInfo<WaiwangVideo> pitgInfo = new PageInfo<WaiwangVideo>(listWaiwang);

		re.append("电报搜索 " + pitgInfo.getTotal() + "条\r\n");
		mediaList = new ArrayList<InputMedia>();
		StringBuffer ttt = new StringBuffer();

		for (int i = 0; i < listWaiwang.size(); i++) {
			String link = TelegramDeepLink.generateLink(telegramChannelMonitor.getBotUsername(),
					"tg" + listWaiwang.get(i).getId());
			String tt = listWaiwang.get(i).getTitle().replace("#", "");
			if (tt.length() > 60)
				tt = tt.substring(0, 60);

			String tit = i + "\t" + tt + "\t时长:" + listWaiwang.get(i).getDuration() + "秒\t时间："
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

//			int robotRe = telegramChannelMonitor.sendChannelReplyWithPhoto(chatId, photo.getCaption(),
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
				search.andCategory("DM", "t.me");
				search.andText("TX", text);
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.YEAR, -10);
				search.greatThan("RQ", calendar.getTimeInMillis());
				search.setMaxResults(1000);
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
				String link = TelegramDeepLink.generateLink(telegramChannelMonitor.getBotUsername(),
						"ch" + hit.getId());
				String tt = hit.getArticle().getString("TX").replace("#", "");
				if (tt.length() > 60)
					tt = tt.substring(0, 60);
				if (channelMsg.getFrom().getUserName() != null
						&& channelMsg.getFrom().getUserName().contentEquals("kaikak09818")) {
					tt = tt + "\t" + "电报链接：" + hit.getArticle().getString("UR");
				}
				long rq = hit.getArticle().getLong("RQ");
				String dt = sdf.format(new Date());

				re.append(i + "\t" + tt + "\t时间：" + dt + "\t时长：" + hit.getArticle().getString("CC") + "分 \t"
						+ "机器人口令：ch" + hit.getId() + "\t" + link + "\r\n");

//				InputMediaPhoto photo = new InputMediaPhoto();
//				photo.setCaption(i + "\t" + tt + "\t" + listWaiwang.get(i).getDuration() + "秒\t"
//						+ dt + "\t" + "机器人口令：ch" + hit.getId() + "\t" + link); // 每张图片独立描述
//				mediaList.add(photo);

//			int robotRe = telegramChannelMonitor.sendChannelReplyWithPhoto(chatId, photo.getCaption(),
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
		String validRes = (responseT);

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
		example.createCriteria().andIdEqualTo(Integer.parseInt(vid));

		WaiwangVideo record = new WaiwangVideo();
		record.setAuthor(location);
//		record.setChannel("fj1");
		waiwangVideoMapper.updateByExampleSelective(record, example);

		// TODO Auto-generated method stub

	}

	@Override
	public void updateLocationzmq(String vid, String location) {

		ZmqVideoExample example = new ZmqVideoExample();
		example.createCriteria().andVidEqualTo(Integer.parseInt(vid));

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
		result.setZmqVideos(searchZmq(keyword, page, pageSize));
		result.setWaiwang2Videos(searchWaiwang2(keyword, page, pageSize));
		result.setWanwuVideos(searchWanwu(keyword, page, pageSize));
		result.setTaolu3Videos(searchTaolu3(keyword, page, pageSize));
		result.setWaiwangVideos(searchWaiwang(keyword, page, pageSize));
		
		return result;
	}
	
	@Override
	public List<ZmqVideo> searchZmq(String keyword, int page, int pageSize) {
		ZmqVideoExample example = new ZmqVideoExample();
		ZmqVideoExample.Criteria criteria = example.createCriteria();
		
		if (keyword != null && !keyword.isEmpty()) {
			if (keyword.contains(" ")) {
				String[] keywords = keyword.split(" ");
				for (String k : keywords) {
					criteria.andTitleLike("%" + k + "%");
				}
			} else {
				criteria.andTitleLike("%" + keyword + "%");
			}
		}
		
		criteria.andDurationIsNotNull();
		example.setOrderByClause("vid desc");
		
		PageHelper.startPage(page, pageSize);
		return zmqVideoMapper.selectByExample(example);
	}
	
	@Override
	public List<Waiwang2Video> searchWaiwang2(String keyword, int page, int pageSize) {
		Waiwang2VideoExample example = new Waiwang2VideoExample();
		Waiwang2VideoExample.Criteria criteria = example.createCriteria();
		
		if (keyword != null && !keyword.isEmpty()) {
			if (keyword.contains(" ")) {
				String[] keywords = keyword.split(" ");
				for (String k : keywords) {
					criteria.andTitleLike("%" + k + "%");
				}
			} else {
				criteria.andTitleLike("%" + keyword + "%");
			}
		}
		
		criteria.andDurationIsNotNull();
		example.setOrderByClause("dt desc");
		
		PageHelper.startPage(page, pageSize);
		return waiwang2VideoMapper.selectByExample(example);
	}
	
	@Override
	public List<WanwuVideo> searchWanwu(String keyword, int page, int pageSize) {
		WanwuVideoExample example = new WanwuVideoExample();
		WanwuVideoExample.Criteria criteria = example.createCriteria();
		
		if (keyword != null && !keyword.isEmpty()) {
			if (keyword.contains(" ")) {
				String[] keywords = keyword.split(" ");
				for (String k : keywords) {
					criteria.andTitleLike("%" + k + "%");
				}
			} else {
				criteria.andTitleLike("%" + keyword + "%");
			}
		}
		
		example.setOrderByClause("sales desc");
		
		PageHelper.startPage(page, pageSize);
		return wanwuVideoMapper.selectByExample(example);
	}
	
	@Override
	public List<Taolu3Video> searchTaolu3(String keyword, int page, int pageSize) {
		Taolu3VideoExample example = new Taolu3VideoExample();
		Taolu3VideoExample.Criteria criteria = example.createCriteria();
		
		if (keyword != null && !keyword.isEmpty()) {
			if (keyword.contains(" ")) {
				String[] keywords = keyword.split(" ");
				for (String k : keywords) {
					criteria.andTitleLike("%" + k + "%");
				}
			} else {
				criteria.andTitleLike("%" + keyword + "%");
			}
		}
		
		example.setOrderByClause("vid desc");
		
		PageHelper.startPage(page, pageSize);
		return taolu3VideoMapper.selectByExample(example);
	}
	
	@Override
	public List<WaiwangVideo> searchWaiwang(String keyword, int page, int pageSize) {
		WaiwangVideoExample example = new WaiwangVideoExample();
		WaiwangVideoExample.Criteria criteria = example.createCriteria();
		
		if (keyword != null && !keyword.isEmpty()) {
			if (keyword.contains(" ")) {
				String[] keywords = keyword.split(" ");
				for (String k : keywords) {
					criteria.andTitleLike("%" + k + "%");
				}
			} else {
				criteria.andTitleLike("%" + keyword + "%");
			}
		}
		
		criteria.andTitleNotEqualTo("");
		criteria.andDurationGreaterThan("600");
		example.setOrderByClause("dt desc");
		
		PageHelper.startPage(page, pageSize);
		return waiwangVideoMapper.selectByExample(example);
	}
	
	@Override
	public List<Waiwang2Video> getLatestWorks(int page, int pageSize) {
		Waiwang2VideoExample example = new Waiwang2VideoExample();
		example.createCriteria().andDurationIsNotNull();
		example.setOrderByClause("dt desc");
		
		PageHelper.startPage(page, pageSize);
		return waiwang2VideoMapper.selectByExample(example);
	}

}
