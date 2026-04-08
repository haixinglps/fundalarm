package cn.exrick.manager.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;

import cn.exrick.common.exception.XmallException;
import cn.exrick.common.jedis.JedisClient;
import cn.exrick.manager.mapper.DyLogMapper;
import cn.exrick.manager.mapper.Fund1Gaoduanzhuangbei2OkMapper;
import cn.exrick.manager.mapper.FundMapper;
import cn.exrick.manager.mapper.TbAlltaskFeedbackMapper;
import cn.exrick.manager.mapper.TbAlltaskMapper;
import cn.exrick.manager.mapper.TbAlltaskitemMapper;
import cn.exrick.manager.mapper.TbAlltasklogMapper;
import cn.exrick.manager.mapper.TbCommandsDescMapper;
import cn.exrick.manager.mapper.TbCommandsMapper;
import cn.exrick.manager.mapper.TbDestinationMapper;
import cn.exrick.manager.mapper.TbDouyinGzMapper;
import cn.exrick.manager.mapper.TbDouyinMessageMapper;
import cn.exrick.manager.mapper.TbFapiaoMapper;
import cn.exrick.manager.mapper.TbFeedbackSystemMapper;
import cn.exrick.manager.mapper.TbGroupofhaveaddMapper;
import cn.exrick.manager.mapper.TbGroupofhaveaddlogMapper;
import cn.exrick.manager.mapper.TbGroupofhavezanMapper;
import cn.exrick.manager.mapper.TbGroupofhavezanlogMapper;
import cn.exrick.manager.mapper.TbGroupsMapper;
import cn.exrick.manager.mapper.TbGroupscategoryMapper;
import cn.exrick.manager.mapper.TbGroupscountMapper;
import cn.exrick.manager.mapper.TbGroupsforpolicyMapper;
import cn.exrick.manager.mapper.TbGroupsmemberlogMapper;
import cn.exrick.manager.mapper.TbItemPriceMapper;
import cn.exrick.manager.mapper.TbMemberMapper;
import cn.exrick.manager.mapper.TbNewsMapper;
import cn.exrick.manager.mapper.TbOrderMapper;
import cn.exrick.manager.mapper.TbOrganBankCardMapper;
import cn.exrick.manager.mapper.TbOrganizationMapper;
import cn.exrick.manager.mapper.TbPanelContentMapper;
import cn.exrick.manager.mapper.TbPanelMapper;
import cn.exrick.manager.mapper.TbPermissionMapper;
import cn.exrick.manager.mapper.TbPolicysmemberMapper;
import cn.exrick.manager.mapper.TbRechargeManageMapper;
import cn.exrick.manager.mapper.TbRoleMapper;
import cn.exrick.manager.mapper.TbRolePermMapper;
import cn.exrick.manager.mapper.TbRouteMapper;
import cn.exrick.manager.mapper.TbSubjectMapper;
import cn.exrick.manager.mapper.TbSubjectalarmMapper;
import cn.exrick.manager.mapper.TbSummeryMapper;
import cn.exrick.manager.mapper.TbTraveltypeMapper;
import cn.exrick.manager.mapper.TbUserGateidMapper;
import cn.exrick.manager.mapper.TbUserMapper;
import cn.exrick.manager.mapper.TbUserinfoMapper;
import cn.exrick.manager.mapper.TbUsersAccountChangeMapper;
import cn.exrick.manager.mapper.TbUsersAccountChangeTichengMapper;
import cn.exrick.manager.mapper.TbWalletMapper;
import cn.exrick.manager.mapper.TbWalletTichengMapper;
import cn.exrick.manager.mapper.TbWeixinMapper;
import cn.exrick.manager.mapper.TbXgecustomerMapper;
import cn.exrick.manager.pojo.Fund;
import cn.exrick.manager.pojo.Fund1Gaoduanzhuangbei2Ok;
import cn.exrick.manager.pojo.Fund1Gaoduanzhuangbei2OkExample;
import cn.exrick.manager.pojo.FundExample;
import cn.exrick.manager.service.OkxService;
import cn.exrick.manager.service.util.AlarmPlayer;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

/**
 * @author Exrickx
 */
//@com.alibaba.dubbo.config.annotation.Service
public class FundServiceKongOldImpl {
//implements FundKongService
	private static final Logger log = LoggerFactory.getLogger(FundServiceKongOldImpl.class);

//	static AlarmPlayer alarmPlayer = new AlarmPlayer();

	@Autowired
	private TbUserMapper tbUserMapper;

	@Autowired
	AlarmPlayer alarmPlayer;

	@Autowired
	private TbCommandsDescMapper tbCommandsDescMapper;
	@Autowired
	private TbCommandsMapper tbCommandsMapper;

	@Autowired
	private TbPanelContentMapper tbPanelContentMapper;
	@Autowired
	private TbPanelMapper tbPanelMapper;

	@Autowired
	private TbUserGateidMapper tbUserGateidMapper;

	@Autowired
	private TbGroupsforpolicyMapper tbGroupsforpolicyMapper;
	@Autowired
	private TbWeixinMapper tbWeixinMapper;

	@Autowired
	private TbGroupsmemberlogMapper tbGroupsmemberlogMapper;
	@Autowired
	private TbAlltaskitemMapper tbAlltaskitemMapper;

	@Autowired
	private TbAlltasklogMapper tbAlltasklogMapper;
	@Autowired
	private TbSubjectMapper tbSubjectMapper;

	@Autowired
	private TbXgecustomerMapper tbXgecustomerMapper;

	@Autowired
	private TbGroupscategoryMapper groupscategoryMapper;

	@Autowired
	private TbSubjectalarmMapper tbSubjectalarmMapper;
	@Autowired
	private TbAlltaskMapper tbAlltaskMapper;

	@Autowired
	private TbGroupofhavezanMapper tbGroupofhavezanMapper;
	@Autowired
	private TbGroupofhavezanlogMapper tbGroupofhavezanlogMapper;

	@Autowired
	private TbGroupofhaveaddMapper tbGroupofhaveaddMapper;
	@Autowired
	private TbGroupofhaveaddlogMapper tbGroupofhaveaddlogMapper;

	@Autowired
	private TbGroupsMapper tbGroupsMapper;
	@Autowired
	private TbGroupscountMapper tbGroupscountMapper;
	@Autowired
	private TbDestinationMapper tbDestinationMapper;
	@Autowired
	private TbRouteMapper tbRouteMapper;
	@Autowired
	private TbRoleMapper tbRoleMapper;
	@Autowired
	private TbPermissionMapper tbPermissionMapper;
	@Autowired
	private TbNewsMapper tbNewsMapper;
	@Autowired
	private TbRolePermMapper tbRolePermMapper;
	@Autowired
	private TbOrganizationMapper tbOrganizationMapper;

	@Autowired
	private TbItemPriceMapper tbItemPriceMapper;
	@Autowired
	private TbOrderMapper tbOrderMapper;
	@Autowired
	private TbOrganBankCardMapper tbOrganBankCardMapper;

	@Autowired
	private TbUserinfoMapper tbUserinfoMapper;

	@Autowired
	private TbFapiaoMapper tbFapiaoMapper;
	@Autowired
	private TbUsersAccountChangeMapper tbUsersAccountChangeMapper;
	@Autowired
	private TbWalletMapper tbWalletMapper;
	@Autowired
	private TbTraveltypeMapper tbTraveltypeMapper;
	@Autowired
	private TbMemberMapper tbMemberMapper;

	@Autowired
	private TbAlltaskFeedbackMapper tbAlltaskFeedbackMapper;
	@Autowired
	private TbPolicysmemberMapper tbPolicysmemberMapper;
	@Autowired
	private TbUsersAccountChangeTichengMapper tbUsersAccountChangeTichengMapper;

	@Autowired
	private TbRechargeManageMapper tbRechargeManageMapper;
	@Autowired
	private DyLogMapper dyLogMapper;
	@Autowired
	private TbFeedbackSystemMapper tbFeedbackSystemMapper;
	@Autowired
	private TbSummeryMapper tbSummeryMapper;

	@Autowired
	private TbWalletTichengMapper tbWalletTichengMapper;

	@Autowired
	private JedisClient jedisClient;
//	@Autowired
//	private AlipayService alipayService;
//	@Autowired
//	private WeixinPayService weixinPayService;
//	@Autowired
//	private ShortVideoyoubaoService shortVideoyoubaoService;
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

	@Override
	public Fund1Gaoduanzhuangbei2Ok getInfoByTableName(String tableName) {
		// TODO Auto-generated method stub
//		url = "http://47.104.93.44:9000";

		Fund1Gaoduanzhuangbei2OkExample example = new Fund1Gaoduanzhuangbei2OkExample();
		example.createCriteria().andIscurrentEqualTo(1);
		example.setTableName(tableName);
		List<Fund1Gaoduanzhuangbei2Ok> results = fund1Gaoduanzhuangbei2OkMapper.selectByExampleDynamic(example);

		if (results.size() > 0)
			return results.get(0);
		else {
			return null;
		}
	}

	@Override
	public Fund1Gaoduanzhuangbei2Ok getInfoByTableNameAndLevel(String tableName, int level) {
		// TODO Auto-generated method stub
//		url = "http://47.104.93.44:9000";

		Fund1Gaoduanzhuangbei2OkExample example = new Fund1Gaoduanzhuangbei2OkExample();
		example.createCriteria().andLevelEqualTo(level);
		example.setTableName(tableName);
		List<Fund1Gaoduanzhuangbei2Ok> results = fund1Gaoduanzhuangbei2OkMapper.selectByExampleDynamic(example);

		if (results.size() > 0)
			return results.get(0);
		else {
			return null;
		}
	}

	@Override
	public Fund1Gaoduanzhuangbei2Ok getInfoByTableNameAndDt(String tableName, long dt, int level) {
		// TODO Auto-generated method stub
//		url = "http://47.104.93.44:9000";

		Fund1Gaoduanzhuangbei2OkExample example = new Fund1Gaoduanzhuangbei2OkExample();
		example.createCriteria().andDtLessThan(new Date(dt)).andLevelNotEqualTo(level);
		example.setTableName(tableName);
		example.setOrderByClause("dt desc");
		PageHelper.startPage(1, 1);
		List<Fund1Gaoduanzhuangbei2Ok> results = fund1Gaoduanzhuangbei2OkMapper.selectByExampleDynamic(example);

		if (results.size() > 0)
			return results.get(0);
		else {
			return null;
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateCurrentPrice(String tableName, BigDecimal price, int tag, BigDecimal realbuyprice,
			BigDecimal money, Fund fundindex, Fund1Gaoduanzhuangbei2Ok fundItem, String lastvalue) {
		Fund1Gaoduanzhuangbei2OkExample exampleOri = new Fund1Gaoduanzhuangbei2OkExample();
		exampleOri.createCriteria().andIdEqualTo(fundItem.getId());
		exampleOri.setTableName(tableName);

		// TODO Auto-generated method stub
//		url = "http://47.104.93.44:9000";
//		try {
		Fund1Gaoduanzhuangbei2OkExample example = new Fund1Gaoduanzhuangbei2OkExample();
		example.createCriteria().andIscurrentEqualTo(1);
		example.setTableName(tableName);
		Fund1Gaoduanzhuangbei2Ok record = new Fund1Gaoduanzhuangbei2Ok();
		record.setCurrentprice(price);
		record.setDt(new Date());
		record.setBuytag(fundItem.getBuytag());
		record.setSuccesscount(fundItem.getSuccesscount());
		record.setUpcount(fundItem.getUpcount());

		BigDecimal chajuShichang = price.subtract(new BigDecimal(lastvalue));
		BigDecimal dxzzlShichang = new BigDecimal("0");
		if (!lastvalue.equals("0")) {
			dxzzlShichang = chajuShichang.divide(new BigDecimal(lastvalue), 4, RoundingMode.DOWN)
					.multiply(new BigDecimal("100"));
		}

		BigDecimal dxzzl = new BigDecimal("0");
		if (tag == 1 || tag == 2) {
//				record.setFene(money.divide(realbuyprice, 4, BigDecimal.ROUND_DOWN));
//				record.set
			BigDecimal chaju = price.subtract(realbuyprice);
			dxzzl = chaju.divide(realbuyprice, 4, RoundingMode.DOWN).multiply(new BigDecimal("100"));
			record.setComment(dxzzl.toString());
			String tips = "";
			if (tag == 1)
				tips = "做空补仓";
			else
				tips = "做空清仓";

//				if (price.compareTo(realbuyprice) < 0)
			record.setComment(tips + record.getComment());
			record.setWangge(dxzzlShichang);

//				else {
//					record.setComment(tips+ record.getComment());
//				}
			// 更新索引目录
			updateIndex(tableName, 1, money, record.getComment());

		}

		else {
			BigDecimal chaju = price.subtract(realbuyprice);
			dxzzl = chaju.divide(realbuyprice, 4, RoundingMode.DOWN).multiply(new BigDecimal("100"));
//				if (fundInfo.getName().indexOf("alarm") != -1)
//					AlarmPlayer.playAlarm();
			record.setComment(dxzzl.toString());
			record.setWangge(dxzzlShichang);

//				if (fundInfo.getName() != null && fundInfo.getName().indexOf("star") != -1
//						&& (jingzhi.compareTo(new BigDecimal(fundInfo.getCate())) >= 0))
//					AlarmPlayer.playAlarm();
//
//				if (fundInfo.getName() != null && fundInfo.getName().indexOf("stop") != -1
//						&& (jingzhi.compareTo(new BigDecimal(fundInfo.getCatedown())) <= 0))
//					AlarmPlayer.playAlarm();

			String tips = "";
			if (tag == 3)
				tips = "快速清仓:";
			else if (tag == 4)
				tips = "快速建仓:";
			else if (tag == 7)
				tips = "回调中:";
			else if (tag == 8)
				tips = "谨慎追涨:";

//			else if (tag == 6)
//				tips = "行情反转:";

//				if (price.compareTo(realbuyprice) < 0)
			record.setComment(tips + record.getComment());

			if (tag == 3 || tag == 4 || tag == 5 || tag == 6)
				updateIndex(tableName, 1, money, record.getComment());
			else
				updateIndex(tableName, 0, money, record.getComment());

		}
		// 修改涨幅情况；最新价，提醒标记。
		fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record, example);

		// 根据tag 2,1,3进行买卖。如果成功则数据库写入；否则回滚。

		if (fundindex.getSinacode() != null && fundindex.getSinacode().equals("okx")) {
			// && fundItem.getFene() != null&& fundItem.getFene().compareTo(new
			// BigDecimal("0")) > 0)
			String trans = null;
			if (tag == 2 || tag == 3 || tag == 8) {// || tag == 3
				// 买入虚拟币，直接调用API【下降途中，继续做空】买入当前仓位，卖出下层仓位。
				// 定位下一条item:
				BigDecimal realBuyPriceHistoryBigDecimal = fundItem.getBuypriceReal();
				int level = fundItem.getLevel().intValue() + 1;
				Fund1Gaoduanzhuangbei2Ok funditemNext = getInfoByTableNameAndLevel(tableName, level);
				if (funditemNext == null) {
					return;
				}

				Integer historytag = 1;

				int lianxuCount = 1;
				try {
					lianxuCount = new BigDecimal(fundItem.getCate()).intValue();
				} catch (Exception e) {
					// TODO: handle exception
				}
				long dtHistory = System.currentTimeMillis();// fundItem.getDt().getTime();//

				for (int lv = 1; lv <= lianxuCount; lv++) {
					Fund1Gaoduanzhuangbei2Ok funditemLast = getInfoByTableNameAndLevel(tableName,
							fundItem.getLevel() - lv);

					Fund1Gaoduanzhuangbei2Ok funditemRealLast = getInfoByTableNameAndDt(tableName, dtHistory,
							fundItem.getLevel());

					if (funditemRealLast != null && funditemLast != null) {
						if (funditemRealLast.getId().intValue() != funditemLast.getId().intValue()) {
//							funditemLast.setBuytag(0);
							historytag = 0;
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String dt = sdf.format(new Date());
							String dtReal = sdf.format(funditemRealLast.getDt());
							String dtLast = sdf.format(funditemLast.getDt());

							FileUtil.appendUtf8String(
									dt + "【做空分析不符合】：" + "  level:" + fundItem.getLevel() + "   最近的日期的level:"
											+ funditemRealLast.getLevel() + "  最近日期的时间：" + dtReal + "  上层level："
											+ funditemLast.getLevel() + "  上层时间:" + dtLast + "\n----\n",
									"d:\\历史数据分析.txt");
							break;
						} else {

							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String dt = sdf.format(new Date());
							String dtReal = sdf.format(funditemRealLast.getDt());
							String dtLast = sdf.format(funditemLast.getDt());

							FileUtil.appendUtf8String(
									dt + "【做空分析符合】：" + "  level:" + fundItem.getLevel() + "   最近的日期的level:"
											+ funditemRealLast.getLevel() + "  最近日期的时间：" + dtReal + "  上层level："
											+ funditemLast.getLevel() + "  上层时间:" + dtLast + "\n----\n",
									"d:\\历史数据分析.txt");

						}
					}
//					if (funditemLast != null && funditemLast.getBuytag() != -1) {
//						historytag = 0;
//						break;
//					}
					dtHistory = funditemLast.getDt().getTime();

				}

				SimpleDateFormat sdfs = new SimpleDateFormat("yyyyMMddHHmm");
				String pk = fundindex.getCode();
				if (historytag == 1 && fundItem.getBuytag() == -1) {
					String signalString = pk + "_historytag_short_" + sdfs.format(new Date()) + "=" + historytag + "\n";
					signalString += pk + "_buytag_short_" + sdfs.format(new Date()) + "=" + fundItem.getBuytag() + "\n";

					try {
						FileUtil.appendUtf8String(signalString, "D:\\signal.property");
					} catch (Exception e) {
						e.printStackTrace();
						log.info("写入日志异常");
					}
				}

//				Fund1Gaoduanzhuangbei2Ok funditemNextNext = getInfoByTableNameAndLevel(tableName, level + 1);

//				Fund1Gaoduanzhuangbei2Ok funditemLast = getInfoByTableNameAndLevel(tableName, level - 2);
//
//				Fund1Gaoduanzhuangbei2Ok funditemRealLast = getInfoByTableNameAndDt(tableName,
//						fundItem.getDt().getTime());
//				if (funditemRealLast != null && funditemLast != null) {
//					if (funditemRealLast.getId().intValue() != funditemLast.getId().intValue()) {
//						funditemLast.setBuytag(0);
//					}
//				}

//				if (funditemNextNext == null) {
//					return;
//				}

				// 设置游标到上一个条目
				if (funditemNext.getFene() != null && funditemNext.getFene().compareTo(new BigDecimal("0")) > 0) {
					Fund1Gaoduanzhuangbei2OkExample example3 = new Fund1Gaoduanzhuangbei2OkExample();
					example3.createCriteria().andLevelEqualTo(level);
					example3.setTableName(tableName);
					Fund1Gaoduanzhuangbei2Ok record3 = new Fund1Gaoduanzhuangbei2Ok();

					record3.setIscurrent(1);
					record3.setZhiying(new BigDecimal("0"));
					fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record3, example3);
					// 上层有份额，直接设置为选中即可。
				} else {

					// 需要考虑为条目买入一个份额。【下层没份额】【暂时让游标移动过去，不做处理】
					Fund1Gaoduanzhuangbei2OkExample example3 = new Fund1Gaoduanzhuangbei2OkExample();
					example3.createCriteria().andLevelEqualTo(level);
					example3.setTableName(tableName);
					Fund1Gaoduanzhuangbei2Ok record3 = new Fund1Gaoduanzhuangbei2Ok();

					record3.setIscurrent(1);
					record3.setBuypriceReal(funditemNext.getSellprice());
					record3.setZhiying(new BigDecimal("0"));

					fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record3, example3);// ResetNext
					// 发布通知，为该tablename,level买入一个份额。 快速买入。

					// funditemNext.getName().indexOf("auto") !=
					// -1)这种情况下自动买入一份；趋势市场；需要有2或者4个梯度才行。level-3或者level-5;波动大用5，波动小用3；
//当前仓位的增长率，涨幅为：dxzzl;需要大于这个涨幅：fundItem.getSellpriceReal()
					// dxzzlShichang.compareTo(fundInfo.getSellpriceReal()) > 0

					// dxzzlShichang.compareTo(fundInfo.getSellpriceReal()) > 0

					// dxzzlShichang.compareTo(fundItem.getSellpriceReal()) > 0
					BigDecimal yuzhi = new BigDecimal("-5");
					BigDecimal yuzhiFanxiang = new BigDecimal("5");
					if (fundindex.getZhenfureal() != null) {
						yuzhiFanxiang = fundindex.getZhenfureal();
						yuzhi = new BigDecimal("0").subtract(yuzhi);
					}
					int jcTag = 1;
//					if (lastvalue.equals("0"))
//						jcTag = 1;
//					else if (dxzzlShichang.compareTo(yuzhi) < 0) {
//						jcTag = 1;
//					}

					if (dxzzlShichang.compareTo(new BigDecimal("0")) < 0) {
						if (dxzzlShichang.compareTo(yuzhi) > 0) {
							jcTag = 0;
						}
					}

					List<Fund1Gaoduanzhuangbei2Ok> cangweis = getCangweisByTableName(tableName);
					int lastindex = cangweis.size() - 1;
					for (int m = 0; m < cangweis.size(); m++) {
						Fund1Gaoduanzhuangbei2Ok item = cangweis.get(m);
						if (item.getLevel() >= fundItem.getLevel()) {

							lastindex = m;
						}
					}

					int cwTag = 1;
					if (cangweis.size() > 0 && cangweis.get(0).getLevel() > fundItem.getLevel()) {
						cwTag = 0;
					}
					int bwtag = 0;
					int jianju = 0;
					if (fundindex.getZhenfureal() != null)
						jianju = fundindex.getZhenfureal().intValue();

//					if (cangweis.size() > 0
//							&& cangweis.get(cangweis.size() - 1).getLevel() + jianju > fundItem.getLevel()) {
//						bwtag = 1;
//					}

					if (cangweis.size() > 0) {
						// 仓位底部高于自己的操作位置。高出jianju个行记录。0代表可以随意加仓。其他数字代表需要间隔一定梯度，利于分仓，不同币种间距不同比较好。基于涨跌幅。
//						
						BigDecimal chajuht = price.subtract(cangweis.get(lastindex).getBuypriceReal());

						BigDecimal dxzzlht = new BigDecimal("0");

						dxzzlht = chajuht.divide(cangweis.get(lastindex).getBuypriceReal(), 5, RoundingMode.DOWN)
								.multiply(new BigDecimal("100"));

						if (dxzzlht.compareTo(yuzhiFanxiang) > 0) {
							bwtag = 1;
						}

					}

					// && fundItem.getFene() ==null&& (funditemNextNext != null
					// &&funditemNextNext.getFene() == null) && jcTag == 1 && fundItem.getBuytag()
					// == -1 && fundItem.getBuytag() == -1

					if ((funditemNext.getName().indexOf("up") != -1 && funditemNext.getName().indexOf("ab") != -1
							&& funditemNext.getName().indexOf("sar") != -1

							&& cwTag == 1 && historytag == 1 && jcTag == 1)
							|| (funditemNext.getName().indexOf("up") != -1 && funditemNext.getName().indexOf("ab") != -1
									&& funditemNext.getName().indexOf("sar") != -1

									&& cwTag == 0 && historytag == 1 && bwtag == 1)
							|| (funditemNext.getName().indexOf("up") == -1 && funditemNext.getName().indexOf("ab") != -1
									&& tag != 8)

							|| (funditemNext.getName().indexOf("up") == -1 && funditemNext.getName().indexOf("ab") != -1
									&& tag == 8 && historytag == 1)

					)

					{
						// && funditemNext.getName().indexOf("sar") != -1

						alarmPlayer.playAlarm(1);
						// sar正常时候可以自动买入;同时需要满足下层涨幅不低于某个值：
						// 需要高于这个增长率：fundItem.getSellpriceReal()

						String instId = fundItem.getCode() + "-SWAP";
						String tdMode = "isolated";
						String clOrdId = UUID.randomUUID().toString().replace("-", "");
						String side = "sell";
//					        # posSide="long",
						//
						String ordType = "market";
//					        # ordType="limit",
//					        # px="0.19579",
						String sz = "0";
						int bt = 1;
						BigDecimal realfene = new BigDecimal("0");
						if (instId.equals("DOGE-USDT-SWAP"))
						//
						{
							sz = "0.1";// fundItem.getFene().intValue() / 1000 + "";
							realfene = new BigDecimal("100");
						} else if (instId.equals("PEPE-USDT-SWAP"))
						//
						{
							sz = "0.1";// fundItem.getFene().intValue() / 1000 + "";
							realfene = new BigDecimal("1000000");
						} else if (instId.equals("SOL-USDT-SWAP"))
						//
						{
							sz = "0.01";// fundItem.getFene().intValue() / 1000 + "";
							realfene = new BigDecimal("0.01");
						}

						else if (instId.equals("NOT-USDT-SWAP"))
						//
						{
							sz = "1";// fundItem.getFene().intValue() / 1000 + "";
							realfene = new BigDecimal("100");
						}

//						else if (instId.equals("PEOPLE-USDT-SWAP"))
//						//
//						{
//							sz = "1";// fundItem.getFene().intValue() / 1000 + "";
//							realfene = new BigDecimal("100");
//						}
//
//						else if (instId.equals("RSR-USDT-SWAP")) {
//							sz = "4";// fundItem.getFene().intValue() / 100 + "";
//							realfene = new BigDecimal("400");
//						} else if (instId.equals("CORE-USDT-SWAP")) {
//							sz = "1";// fundItem.getFene().intValue() + "";
//							realfene = new BigDecimal("1");
//						}
//
//						else if (instId.equals("ETHFI-USDT-SWAP")) {
//							sz = "1";// fundItem.getFene().intValue() + "";
//							realfene = new BigDecimal("1");
//						}

						else {
							// 无需自动化追涨：不买入

							bt = 0;

						}
						if (bt == 1) {
							// 前提是funditem的份额必须是空，避免买卖同时触发。
							JSONObject requestData = new JSONObject();
							requestData.put("sz", sz);
							requestData.put("instId", instId);
							requestData.put("tdMode", tdMode);
							requestData.put("clOrdId", clOrdId);

							requestData.put("side", side);
							requestData.put("ordType", ordType);
							System.out.println("requestdata:" + requestData.toString());

							String zhuizhangTrans = okxService.trade("/api/v5/trade/order", "POST",
									requestData.toString());
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String dt = sdf.format(new Date());
							if (zhuizhangTrans == null) {

								try {
									FileUtil.appendUtf8String(dt + "【做空】卖出后异常（请核对netError.txt）：" + "  level:"
											+ fundItem.getLevel() + "  " + requestData + "\n----\n",
											"d:\\okxError.txt");
								} catch (Exception e) {
									e.printStackTrace();
									log.info("写入日志异常");
								}
								throw new XmallException("【做空】卖出后异常（请核对netError.txt）：" + requestData);
							}
							JSONObject okxtransJsonObject = new JSONObject(zhuizhangTrans);

							if (okxtransJsonObject.getStr("code").equals("0")) {
								String keyString = "?clOrdId=" + clOrdId + "&instId=" + instId;
								try {
									String transOrder = okxService.trade("/api/v5/trade/order" + keyString, "GET", "");
									if (transOrder == null) {
										transOrder = okxService.trade("/api/v5/trade/order" + keyString, "GET", "");
									}
									if (transOrder == null) {

										try {
											FileUtil.appendUtf8String(dt + "【做空】卖出后获取订单异常（请核对）：" + "  level:" + level
													+ "  " + keyString + "\n----\n", "d:\\orderInfoError.txt");
										} catch (Exception e) {
											e.printStackTrace();
											log.info("写入日志异常");
										}
										throw new XmallException("【做空】卖出后获取订单异常（请核对）:" + requestData);
									} else {
										JSONObject orderInfo = new JSONObject(transOrder);
										if (orderInfo.getStr("code").equals("0")) {
											try {
												FileUtil.appendUtf8String(dt + "【做空】卖出：" + " level:" + level + " "
														+ orderInfo + "\n" + keyString + "\n----\n",
														"d:\\orderInfo.txt");
												// 异常订单需要保存起来后期使用。
											} catch (Exception e) {
												e.printStackTrace();
												log.info("写入日志异常");
											}
											JSONArray ol = orderInfo.getJSONArray("data");
											BigDecimal dpBigDecimal = ol.getJSONObject(0).getBigDecimal("avgPx");

											Fund1Gaoduanzhuangbei2OkExample example5 = new Fund1Gaoduanzhuangbei2OkExample();
											example5.createCriteria().andLevelEqualTo(level);
											example5.setTableName(tableName);
											Fund1Gaoduanzhuangbei2Ok record5 = new Fund1Gaoduanzhuangbei2Ok();

											record5.setIscurrent(1);
											record5.setBuypriceReal(dpBigDecimal);
											record5.setZhiying(new BigDecimal("0"));

											record5.setFene(realfene);
											fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record5, example5);
											String buylog = pk + "_buyprice_short_" + sdfs.format(new Date()) + "="
													+ dpBigDecimal + "\n";

											try {
												FileUtil.appendUtf8String(buylog, "D:\\signal.property");
											} catch (Exception e) {
												e.printStackTrace();
												log.info("写入日志异常");
											}

										} else {
											try {
												FileUtil.appendUtf8String(
														dt + "【做空】卖出后获取订单异常：" + " level:" + level + " " + orderInfo
																+ "\n" + keyString + "\n----\n",
														"d:\\orderInfoError.txt");
												// 异常订单需要保存起来后期使用。
											} catch (Exception e) {
												e.printStackTrace();
												log.info("写入日志异常");
											}

											throw new XmallException("【做空】卖出后获取订单异常:" + orderInfo);

										}

									}

								} catch (Exception e2) {
									try {
										FileUtil.appendUtf8String(dt + "【做空】卖出后获取订单异常：" + " level:" + level + " "
												+ keyString + "\n" + e2.getMessage() + "\n----\n",
												"d:\\unknowError.txt");

									} catch (Exception e3) {
										e3.printStackTrace();
										log.info("写入日志异常");
									}
								}

							}

							else {
								try {
									try {
										FileUtil.appendUtf8String(
												dt + "【做空】卖出异常：" + " level:" + level + "请求数据： " + requestData + "\n"
														+ okxtransJsonObject.toString() + "\n----\n",
												"d:\\okxError.txt");

									} catch (Exception e3) {
										e3.printStackTrace();
										log.info("写入日志异常");
									}
								} catch (Exception e) {
									// TODO: handle exception
								}
								throw new XmallException("OKX服务异常:" + okxtransJsonObject.getStr("msg"));
							}
						} else {
							// 未知类型虚拟币，不处理。
						}

					}

				}

				// 判断SAR指标是否适合卖出（sar适合等价于auto存在自动化）;而且当前必须有份额，没份额则不需要卖出。

				if (fundItem.getFene() != null && fundItem.getFene().compareTo(new BigDecimal("0")) > 0) {

					// fundItem.getName().indexOf("auto") != -1)这种情况下自动卖出一份；震荡市场

					if (fundItem.getName().indexOf("as") == -1)

					{

						// 无需自动化：继续持有；游标清空即可。
						Fund1Gaoduanzhuangbei2Ok record2 = new Fund1Gaoduanzhuangbei2Ok();
						// 清空当前条目份额。
						record2.setIscurrent(0);
						fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record2, exampleOri);
						return;
					}

					String instId = fundItem.getCode() + "-SWAP";
					String tdMode = "isolated";
					String clOrdId = UUID.randomUUID().toString().replace("-", "");
					String side = "buy";
//				        # posSide="long",
//
					String ordType = "market";
//				        # ordType="limit",
//				        # px="0.19579",
					String sz = "0";
					if (instId.contentEquals("DOGE-USDT-SWAP"))
//
					{
						sz = fundItem.getFene().divide(new BigDecimal("1000"), RoundingMode.DOWN).toString();
					} else if (instId.contentEquals("PEPE-USDT-SWAP"))
					//
					{
						sz = fundItem.getFene().divide(new BigDecimal("10000000"), RoundingMode.DOWN).toString();
					} else if (instId.contentEquals("SOL-USDT-SWAP"))
					//
					{
						sz = fundItem.getFene().divide(new BigDecimal("1"), RoundingMode.DOWN).toString();
					} else if (instId.contentEquals("NOT-USDT-SWAP"))
					//
					{
						sz = fundItem.getFene().divide(new BigDecimal("100"), RoundingMode.DOWN).toString();
					}
//					else if (instId.equals("PEOPLE-USDT-SWAP"))
//					//
//					{
//						sz = "1";// fundItem.getFene().intValue() / 1000 + "";
////							realfene = new BigDecimal("2000");
//					}
//
//					else if (instId.equals("RSR-USDT-SWAP")) {
//						sz = "4";
//					} else if (instId.contentEquals("CORE-USDT-SWAP")) {
//						sz = "1";
//					} else if (instId.contentEquals("ETHFI-USDT-SWAP")) {
//						sz = "1";
//					} 

					else {
						// 无需自动化：继续持有；游标清空即可。
						Fund1Gaoduanzhuangbei2Ok record4 = new Fund1Gaoduanzhuangbei2Ok();
						// 清空当前条目份额。
						record4.setIscurrent(0);
						fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record4, exampleOri);
						return;

					}

					Fund1Gaoduanzhuangbei2Ok record2 = new Fund1Gaoduanzhuangbei2Ok();
					// 清空当前条目份额。
//					record.setIscurrent(0);
					fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelectiveReset(record2, exampleOri);

					JSONObject requestData = new JSONObject();
					requestData.put("sz", sz);
					requestData.put("instId", instId);
					requestData.put("tdMode", tdMode);
					requestData.put("clOrdId", clOrdId);

					requestData.put("side", side);
					requestData.put("ordType", ordType);

					trans = okxService.trade("/api/v5/trade/order", "POST", requestData.toString());
					if (trans == null) {
						throw new XmallException("OKX服务异常:" + requestData.toString());
					}
					System.out.println("requestData:" + requestData);
					System.out.println("res:" + trans);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String dt = sdf.format(new Date());

					JSONObject okxtransJsonObject = new JSONObject(trans);
					if (okxtransJsonObject.getStr("code").equals("0")) {
						// 价格写入python配置文件，订单号和对应买入价。
						String keyString = clOrdId + "_buy=" + realBuyPriceHistoryBigDecimal.toString() + "\n";
						try {
							FileUtil.appendUtf8String(keyString, "D:\\project\\python\\btc\\config.ini");
						} catch (Exception e) {
							e.printStackTrace();
							log.info("写入日志异常");
						}

						String orderkeyString = "?clOrdId=" + clOrdId + "&instId=" + instId;

						String transOrder = okxService.trade("/api/v5/trade/order" + orderkeyString, "GET", "");

						if (transOrder == null) {
							transOrder = okxService.trade("/api/v5/trade/order" + orderkeyString, "GET", "");
						}
						if (transOrder == null) {

							try {
								FileUtil.appendUtf8String(dt + "【as做空平仓】as做空平仓后获取订单异常：(请核对）" + "  level:" + level + "  "
										+ orderkeyString + "\n----\n", "d:\\orderInfoError.txt");
							} catch (Exception e) {
								e.printStackTrace();
								log.info("写入日志异常");
							}
							throw new XmallException("【as做空平仓】as做空平仓后获取订单异常：" + orderkeyString);
						} else {

							JSONObject orderInfo = new JSONObject(transOrder);
							if (orderInfo.getStr("code").equals("0")) {
								JSONArray ol = orderInfo.getJSONArray("data");
								BigDecimal dpBigDecimal = ol.getJSONObject(0).getBigDecimal("avgPx");

								price = dpBigDecimal;
								BigDecimal chajuCw = price.subtract(fundItem.getBuypriceReal());
								BigDecimal dxzzlCw = chajuCw.divide(fundItem.getBuypriceReal(), 4, RoundingMode.DOWN)
										.multiply(new BigDecimal("100"));

								try {
									FileUtil.appendUtf8String(dt + "【做空平仓】okx买入：" + " level:" + level + " "
											+ requestData + "\n" + trans + "\n----\n", "d:\\okx.txt");

								} catch (Exception e3) {
									e3.printStackTrace();
									log.info("写入日志异常");
								}

								try {
									FileUtil.appendUtf8String(dt + "【成功止盈as-做空平仓】：tab:" + tableName + " 【" + dxzzlCw
											+ "】   level:" + level + " " + "\n" + "\n----\n", "d:\\止盈日志.txt");

								} catch (Exception e3) {
									e3.printStackTrace();
									log.info("写入日志异常");
								}
								alarmPlayer.playAlarm(5);
							} else {
								try {
									FileUtil.appendUtf8String(dt + "【as做空平仓】获取订单异常：" + "  level:" + level + "  "
											+ orderkeyString + "\n----\n", "d:\\orderInfoError.txt");
								} catch (Exception e) {
									e.printStackTrace();
									log.info("写入日志异常");
								}
								throw new XmallException("【as做空平仓】as做空平仓后获取订单异常：" + orderkeyString);
							}

						}

					} else {
						try {
							FileUtil.appendUtf8String(dt + "【做空平仓】okx接口无法买入：" + " level:" + level + " " + requestData
									+ "\n" + trans + "\n----\n", "d:\\okxError.txt");

						} catch (Exception e3) {
							e3.printStackTrace();
							log.info("写入日志异常");
						}
						throw new XmallException("OKX服务异常:" + okxtransJsonObject.getStr("msg"));
					}

				} else {
					// 不做处理，不卖出，当前仓位无份额。清空iscurrent fene buyprice_real
					Fund1Gaoduanzhuangbei2Ok record2 = new Fund1Gaoduanzhuangbei2Ok();
					// 清空当前条目份额。
//					record.setIscurrent(0);
					fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelectiveReset(record2, exampleOri);
				}

			} else if (tag == 1 || tag == 4 || tag == 7) {
				// 买入虚拟币
				// 定位下一条item:
				int level = fundItem.getLevel().intValue() - 1;
				Fund1Gaoduanzhuangbei2Ok funditemNext = getInfoByTableNameAndLevel(tableName, level);
				if (funditemNext == null) {
					return;
				}

				// 定位下一条item:
//				BigDecimal realBuyPriceHistoryBigDecimal = fundItem.getBuypriceReal();

				Fund1Gaoduanzhuangbei2Ok record2 = new Fund1Gaoduanzhuangbei2Ok();
				// 设置为非选中状态
				record2.setIscurrent(0);
				fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record2, exampleOri);

				// 设置游标到下一个条目
				int buyTag = 0;
				if (funditemNext.getFene() != null && funditemNext.getFene().compareTo(new BigDecimal("0")) > 0) {
					// 非空则修改iscurrent为1

					Fund1Gaoduanzhuangbei2OkExample example3 = new Fund1Gaoduanzhuangbei2OkExample();
					example3.createCriteria().andLevelEqualTo(level);
					example3.setTableName(tableName);
					Fund1Gaoduanzhuangbei2Ok record3 = new Fund1Gaoduanzhuangbei2Ok();

					record3.setIscurrent(1);
					record3.setZhiying(new BigDecimal("0"));
					fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record3, example3);
					// 无需买入份额
				} else {

					// 需要考虑为条目立刻买入一个份额。
					buyTag = 1;
					// 下一层禁止自动化；直接设置即可。
					Fund1Gaoduanzhuangbei2OkExample example3 = new Fund1Gaoduanzhuangbei2OkExample();
					example3.createCriteria().andLevelEqualTo(level);
					example3.setTableName(tableName);
					Fund1Gaoduanzhuangbei2Ok record3 = new Fund1Gaoduanzhuangbei2Ok();

					record3.setIscurrent(1);
					record3.setZhiying(new BigDecimal("0"));
					record3.setBuypriceReal(funditemNext.getSellprice());

					fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record3, example3);

					if (funditemNext.getName().indexOf("ab") != -1 && funditemNext.getName().indexOf("up") == -1
							&& tag == 4) {// &&
						// fundItem.getFene()
						// ==
						// null
						// && funditemNext.getName().indexOf("sar") != -1
						alarmPlayer.playAlarm(1);
						String instId = fundItem.getCode() + "-SWAP";
						String tdMode = "isolated";
						String clOrdId = UUID.randomUUID().toString().replace("-", "");
						String side = "sell";
//					        # posSide="long",
						//
						String ordType = "market";
//					        # ordType="limit",
//					        # px="0.19579",
						String sz = "0";
						BigDecimal realfene = fundItem.getFene();
						if (instId.equals("DOGE-USDT-SWAP"))
						//
						{
							sz = "0.1";// fundItem.getFene().intValue() / 1000 + "";
							realfene = new BigDecimal("100");
						} else if (instId.equals("PEPE-USDT-SWAP"))
						//
						{
							sz = "0.1";// fundItem.getFene().intValue() / 1000 + "";
							realfene = new BigDecimal("1000000");
						} else if (instId.equals("SOL-USDT-SWAP"))
						//
						{
							sz = "0.01";// fundItem.getFene().intValue() / 1000 + "";
							realfene = new BigDecimal("0.01");
						} else if (instId.equals("NOT-USDT-SWAP"))
						//
						{
							sz = "1";// fundItem.getFene().intValue() / 1000 + "";
							realfene = new BigDecimal("100");
						}

//						else if (instId.equals("PEOPLE-USDT-SWAP"))
//						//
//						{
//							sz = "1";// fundItem.getFene().intValue() / 1000 + "";
//							realfene = new BigDecimal("100");
//						}
//
//						else if (instId.equals("RSR-USDT-SWAP")) {
//							sz = "4";// fundItem.getFene().intValue() / 100 + "";
//							realfene = new BigDecimal("400");
//						} else if (instId.equals("CORE-USDT-SWAP")) {
//							sz = "1";// fundItem.getFene().intValue() + "";
//							realfene = new BigDecimal("1");
//						} else if (instId.equals("ETHFI-USDT-SWAP")) {
//							sz = "1";// fundItem.getFene().intValue() + "";
//							realfene = new BigDecimal("1");
//						}

						else {
//							throw new XmallException("不支持");
							// 不支持买入，直接修改状态即可：
							// 下一层禁止自动化；直接设置即可。
//							Fund1Gaoduanzhuangbei2OkExample example3 = new Fund1Gaoduanzhuangbei2OkExample();
//							example3.createCriteria().andLevelEqualTo(level);
//							example3.setTableName(tableName);
//							Fund1Gaoduanzhuangbei2Ok record3 = new Fund1Gaoduanzhuangbei2Ok();
//
//							record3.setIscurrent(1);
//							record3.setBuypriceReal(funditemNext.getBuyprice());
//
//							fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record3, example3);
							return;
						}

						JSONObject requestData = new JSONObject();
						requestData.put("sz", sz);
						requestData.put("instId", instId);
						requestData.put("tdMode", tdMode);
						requestData.put("clOrdId", clOrdId);

						requestData.put("side", side);
						requestData.put("ordType", ordType);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String dt = sdf.format(new Date());
						trans = okxService.trade("/api/v5/trade/order", "POST", requestData.toString());
						if (trans == null) {
							try {
								FileUtil.appendUtf8String(dt + "【做空补仓】卖出（请核对netError.txt）：" + "  level:"
										+ fundItem.getLevel() + "  " + requestData + "\n----\n", "d:\\okxError.txt");
							} catch (Exception e) {
								e.printStackTrace();
								log.info("写入日志异常");
							}
							throw new XmallException("OKX服务异常:" + requestData.toString());
						}
						JSONObject okxtransJsonObject = new JSONObject(trans);

						if (okxtransJsonObject.getStr("code").equals("0")) {
							// 买入成功： 1，当前funditem的level对应的下一条份额查看是否为空，为空则买入；2 为空则把成交价写入数据库；

							String keyString = "?clOrdId=" + clOrdId + "&instId=" + instId;
							try {
								String transOrder = okxService.trade("/api/v5/trade/order" + keyString, "GET", "");
								if (transOrder == null) {
									transOrder = okxService.trade("/api/v5/trade/order" + keyString, "GET", "");
								}
								if (transOrder == null) {

									try {
										FileUtil.appendUtf8String(dt + "【做空补仓】卖出后获取订单异常(请核对）：" + "  level:" + level
												+ "  " + keyString + "\n----\n", "d:\\orderInfoError.txt");
									} catch (Exception e) {
										e.printStackTrace();
										log.info("写入日志异常");
									}
									throw new XmallException("【做空补仓】卖出后获取订单异常:" + keyString);
								} else {
									JSONObject orderInfo = new JSONObject(transOrder);
									if (orderInfo.getStr("code").equals("0")) {
										JSONArray ol = orderInfo.getJSONArray("data");
										BigDecimal dpBigDecimal = ol.getJSONObject(0).getBigDecimal("avgPx");

										Fund1Gaoduanzhuangbei2OkExample example4 = new Fund1Gaoduanzhuangbei2OkExample();
										example4.createCriteria().andLevelEqualTo(level);
										example4.setTableName(tableName);
										Fund1Gaoduanzhuangbei2Ok record4 = new Fund1Gaoduanzhuangbei2Ok();

										record4.setIscurrent(1);
										record4.setBuypriceReal(dpBigDecimal);
										record4.setFene(realfene);
										record4.setZhiying(new BigDecimal("0"));
										fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record4, example4);
										try {
											FileUtil.appendUtf8String(dt + "【做空补仓】卖出：" + "  level:" + level + "  "
													+ keyString + "\n----\n", "d:\\orderInfo.txt");
										} catch (Exception e) {
											e.printStackTrace();
											log.info("写入日志异常");
										}
									} else {
										try {
											FileUtil.appendUtf8String(dt + "【做空补仓】卖出后获取订单异常：" + " level:" + level + " "
													+ orderInfo + "\n" + keyString + "\n----\n",
													"d:\\orderInfoError.txt");
											// 异常订单需要保存起来后期使用。
										} catch (Exception e) {
											e.printStackTrace();
											log.info("写入日志异常");
										}

										throw new XmallException("【做空补仓】卖出后获取订单异常:" + keyString);
									}

								}

							} catch (Exception e2) {
								try {
									FileUtil.appendUtf8String(dt + "【做空补仓】卖出后获取订单异常：" + " level:" + level + " "
											+ keyString + "\n" + e2.getMessage() + "\n----\n", "d:\\unknowError.txt");

								} catch (Exception e3) {
									e3.printStackTrace();
									log.info("写入日志异常");
								}
								throw new XmallException("【做空补仓】卖出后获取订单异常:" + keyString + e2.getMessage());
							}

						} else {

							try {
								FileUtil.appendUtf8String(dt + "【做空补仓】okx接口无法卖出：" + " level:" + level + " "
										+ requestData + "\n" + trans + "\n----\n", "d:\\okxError.txt");

							} catch (Exception e3) {
								e3.printStackTrace();
								log.info("写入日志异常");
							}
							throw new XmallException("OKX服务异常:" + okxtransJsonObject.getStr("msg"));
						}

					} else {
						// 下一层禁止自动化；直接设置即可。
//						Fund1Gaoduanzhuangbei2OkExample example3 = new Fund1Gaoduanzhuangbei2OkExample();
//						example3.createCriteria().andLevelEqualTo(level);
//						example3.setTableName(tableName);
//						Fund1Gaoduanzhuangbei2Ok record3 = new Fund1Gaoduanzhuangbei2Ok();
//
//						record3.setIscurrent(1);
//						record3.setBuypriceReal(funditemNext.getBuyprice());
//
//						fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record3, example3);

						// 如果上层有仓位，且仓位跌幅超过1%，检查是否有下层仓位，如果有，两个仓位一起卖出；//止盈方法。
						// 如果SAR异常，则直接抛售所有仓位。

					}
					//
				}
				// 继续判断level-2是否有仓位和份额，有则卖出：
				// 暂不使用：

				int levelLast = funditemNext.getLevel().intValue() - funditemNext.getZhisun();
				Fund1Gaoduanzhuangbei2Ok funditemLast = getInfoByTableNameAndLevel(tableName, levelLast);
				if (funditemLast == null) {
					return;
				}

				if (1 == 0 && funditemLast.getFene() != null
						&& funditemLast.getFene().compareTo(new BigDecimal("0")) > 0) {

					// fundItem.getName().indexOf("auto") != -1)这种情况下自动卖出一份；震荡市场

//					if (fundItem.getName().indexOf("as") == -1)
//
//					{
//
//						// 无需自动化：继续持有；游标清空即可。
//						Fund1Gaoduanzhuangbei2Ok record2 = new Fund1Gaoduanzhuangbei2Ok();
//						// 清空当前条目份额。
//						record2.setIscurrent(0);
//						fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record2, exampleOri);
//						return;
//					}

					String instId = fundItem.getCode() + "-SWAP";
					String tdMode = "isolated";
					String clOrdId = UUID.randomUUID().toString().replace("-", "");
					String side = "sell";
//				        # posSide="long",
//
					String ordType = "market";
//				        # ordType="limit",
//				        # px="0.19579",
					String sz = "0";
					if (instId.contentEquals("DOGE-USDT-SWAP"))
//
					{
						sz = "0.1";
					} else if (instId.contentEquals("PEPE-USDT-SWAP"))
					//
					{
						sz = "0.1";
					} else if (instId.equals("PEOPLE-USDT-SWAP"))
					//
					{
						sz = "1";// fundItem.getFene().intValue() / 1000 + "";
//							realfene = new BigDecimal("2000");
					}

					else if (instId.equals("RSR-USDT-SWAP")) {
						sz = "4";
					} else if (instId.contentEquals("CORE-USDT-SWAP")) {
						sz = "1";
					} else if (instId.contentEquals("ETHFI-USDT-SWAP")) {
						sz = "1";
					} else {
						// 无需自动化：继续持有；游标清空即可。
//						Fund1Gaoduanzhuangbei2Ok record4 = new Fund1Gaoduanzhuangbei2Ok();
//						// 清空当前条目份额。
//						record4.setIscurrent(0);
//						fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record4, exampleOri);
						return;

					}

					Fund1Gaoduanzhuangbei2Ok recordLasst = new Fund1Gaoduanzhuangbei2Ok();
					Fund1Gaoduanzhuangbei2OkExample exampleLast = new Fund1Gaoduanzhuangbei2OkExample();
					exampleLast.createCriteria().andLevelEqualTo(levelLast);
					exampleLast.setTableName(tableName);
					// 清空当前条目份额。
//					record.setIscurrent(0);
					fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelectiveReset(recordLasst, exampleLast);

					JSONObject requestData = new JSONObject();
					requestData.put("sz", sz);
					requestData.put("instId", instId);
					requestData.put("tdMode", tdMode);
					requestData.put("clOrdId", clOrdId);

					requestData.put("side", side);
					requestData.put("ordType", ordType);

					trans = okxService.trade("/api/v5/trade/order", "POST", requestData.toString());
					if (trans == null) {
						throw new XmallException("OKX服务异常");
					}
					System.out.println("requestData:" + requestData);
					System.out.println("res:" + trans);

					JSONObject okxtransJsonObject = new JSONObject(trans);
					if (okxtransJsonObject.getStr("code").equals("0")) {
						BigDecimal realBuyPriceHistoryLastBigDecimal = funditemLast.getBuypriceReal();
						// 价格写入python配置文件，订单号和对应买入价。
						String keyString = clOrdId + "_sell=" + realBuyPriceHistoryLastBigDecimal.toString() + "\n";
						try {
							FileUtil.appendUtf8String(keyString, "D:\\project\\python\\btc\\config.ini");
						} catch (Exception e) {
							e.printStackTrace();
							log.info("写入日志异常");
						}

					} else {
						try {
							FileUtil.appendUtf8String("【止损】okx接口无法卖出：" + " level:" + levelLast + " " + requestData
									+ "\n" + trans + "\n----\n", "d:\\okxError.txt");

						} catch (Exception e3) {
							e3.printStackTrace();
							log.info("写入日志异常");
						}
						throw new XmallException("OKX服务异常:" + okxtransJsonObject.getStr("msg"));
					}

				}

			} else {
				// 其他情况卡住，人工处理
			}
		}

//		} catch (Exception e) {
//			e.printStackTrace();
//			// TODO: handle exception
//		}

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updatezhiying(Fund1Gaoduanzhuangbei2Ok cw, String tableName,
			List<Fund1Gaoduanzhuangbei2Ok> duichongList) {

		// 更新其他仓位的止盈数据：
		// 无紧急事件通知，更新所有仓位的止盈数据，并检测是否低于止盈价格。

		// 遍历有份额的仓位，更新最高价，止盈价，最大最小价；判断是否应该预警止盈。
		Fund1Gaoduanzhuangbei2Ok cwLocal = new Fund1Gaoduanzhuangbei2Ok();
		cwLocal.setMaxdiefu5(cw.getMaxdiefu5());
		cwLocal.setMaxzhangfu5(cw.getMaxzhangfu5());
		cwLocal.setMinprice5(cw.getMinprice5());
		cwLocal.setMaxprice5(cw.getMaxprice5());
		cwLocal.setComment(cw.getComment());
		cwLocal.setCurrentprice(cw.getCurrentprice());
		cwLocal.setMaxpriceniu(cw.getMaxpriceniu());
		cwLocal.setBuytag(cw.getBuytag());
		cwLocal.setYangbencount(cw.getYangbencount());
		cwLocal.setZhiying(cw.getZhiying());
		Fund1Gaoduanzhuangbei2OkExample example = new Fund1Gaoduanzhuangbei2OkExample();
		example.createCriteria().andIdEqualTo(cw.getId());
		example.setTableName(tableName);
		fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(cwLocal, example);

//		try {
//			FileUtil.appendUtf8String(
//					"【准备对冲】：tab:" + tableName + cw.getComment() + "   level:" + cw.getLevel() + " " + "\n" + "\n----\n",
//					"d:\\对冲信息.txt");
//
//		} catch (Exception e3) {
//			e3.printStackTrace();
//			log.info("写入日志异常");
//		}

		if (cw.getComment().indexOf("止盈") != -1 || cw.getComment().indexOf("止损") != -1
				|| cw.getComment().indexOf("平仓") != -1 || cw.getComment().indexOf("对冲") != -1) {
			updateIndex(tableName, 1, new BigDecimal("0"), cw.getComment());

			// 及时止盈，卖出虚拟币：

			// fundItem.getName().indexOf("auto") != -1)这种情况下自动卖出一份；震荡市场

//			if (fundItem.getName().indexOf("as") == -1)
//
//			{
//
//				// 无需自动化：继续持有；游标清空即可。
//				Fund1Gaoduanzhuangbei2Ok record2 = new Fund1Gaoduanzhuangbei2Ok();
//				// 清空当前条目份额。
//				record2.setIscurrent(0);
//				fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record2, exampleOri);
//				return;
//			}

			String instId = cw.getCode() + "-SWAP";
			String tdMode = "isolated";
			String clOrdId = UUID.randomUUID().toString().replace("-", "");
			String side = "buy";
//		        # posSide="long",
//
			String ordType = "market";
//		        # ordType="limit",
//		        # px="0.19579",

			BigDecimal szBigDecimal = cw.getFene();
			if (cw.getComment().indexOf("对冲") != -1) {
				szBigDecimal = new BigDecimal("0");
				BigDecimal dcSum = new BigDecimal("0");
				for (Fund1Gaoduanzhuangbei2Ok item : duichongList) {
//					 dc
					dcSum = dcSum.add(item.getBuypriceReal().multiply(item.getFene()));
					szBigDecimal = szBigDecimal.add(item.getFene());
				}
				cw.setFene(szBigDecimal);
//				sz = (duichongList.size() * Float.parseFloat(sz)) + "";
//
//				sz = sz.replaceAll("\\.0*$", "");
				cw.setBuypriceReal(dcSum.divide(szBigDecimal, 5, RoundingMode.DOWN));

			}

			String sz = "0";
			if (instId.contentEquals("DOGE-USDT-SWAP"))
//
			{
				sz = cw.getFene().divide(new BigDecimal("1000"), RoundingMode.DOWN).toString();
			} else if (instId.contentEquals("PEPE-USDT-SWAP"))
			//
			{
				sz = cw.getFene().divide(new BigDecimal("10000000"), RoundingMode.DOWN).toString();
			} else if (instId.contentEquals("SOL-USDT-SWAP"))
			//
			{
				sz = cw.getFene().divide(new BigDecimal("1"), RoundingMode.DOWN).toString();
			} else if (instId.contentEquals("NOT-USDT-SWAP"))
			//
			{
				sz = cw.getFene().divide(new BigDecimal("100"), RoundingMode.DOWN).toString();
			}

//			else if (instId.equals("PEOPLE-USDT-SWAP"))
//			//
//			{
//				sz = "1";// fundItem.getFene().intValue() / 1000 + "";
////					realfene = new BigDecimal("2000");
//			}
//
//			else if (instId.equals("RSR-USDT-SWAP")) {
//				sz = "4";
//			} else if (instId.contentEquals("CORE-USDT-SWAP")) {
//				sz = "1";
//			} else if (instId.contentEquals("ETHFI-USDT-SWAP")) {
//				sz = "1";
//			} 
			else {
				// 无需自动化：继续持有；游标清空即可。
//				Fund1Gaoduanzhuangbei2Ok record4 = new Fund1Gaoduanzhuangbei2Ok();
//				// 清空当前条目份额。
//				record4.setIscurrent(0);
//				fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record4, exampleOri);
				return;

			}

//			sz = cw.getFene().intValue() + "";

			// 清空当前条目份额。
//			record.setIscurrent(0);
			BigDecimal szDuichong = new BigDecimal("0");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dt = sdf.format(new Date());

			JSONObject requestData = new JSONObject();
			requestData.put("sz", sz);
			requestData.put("instId", instId);
			requestData.put("tdMode", tdMode);
			requestData.put("clOrdId", clOrdId);

			requestData.put("side", side);
			requestData.put("ordType", ordType);

			String trans = okxService.trade("/api/v5/trade/order", "POST", requestData.toString());
			if (trans == null) {
//				
//				alarmPlayer.playAlarm2(1800);
//				try {
//					Thread.sleep(1800);
//				} catch (Exception e) {
//					// TODO: handle exception
//				}

				try {
					FileUtil.appendUtf8String(dt + "【止盈，止损，平仓，对冲-做空】异常(请核对）：" + "  level:" + cw.getLevel() + "  "
							+ requestData + "\n----\n", "d:\\okxError.txt");
				} catch (Exception e) {
					e.printStackTrace();
					log.info("写入日志异常");
				}

				throw new XmallException("OKX服务异常:" + requestData.toString());
//				return;

			}
			System.out.println("requestData:" + requestData);
			System.out.println("res:" + trans);

			JSONObject okxtransJsonObject = new JSONObject(trans);
			if (okxtransJsonObject.getStr("code").equals("0")) {

				BigDecimal dpBigDecimal = cw.getCurrentprice();

				BigDecimal price = dpBigDecimal;
				BigDecimal realbuypriceCw = cw.getBuypriceReal();
				BigDecimal chajuCw = price.subtract(realbuypriceCw);

				BigDecimal dxzzlCw = chajuCw.divide(realbuypriceCw, 4, RoundingMode.DOWN)
						.multiply(new BigDecimal("100"));

				String keyString = "?clOrdId=" + clOrdId + "&instId=" + instId;
				try {
					String transOrder = okxService.trade("/api/v5/trade/order" + keyString, "GET", "");
					if (transOrder == null) {
						transOrder = okxService.trade("/api/v5/trade/order" + keyString, "GET", "");
					}
					if (transOrder == null) {

						try {
							FileUtil.appendUtf8String(dt + "【止盈，止损，平仓，对冲-做空】后获取订单异常：" + "  level:" + cw.getLevel()
									+ "  " + keyString + "\n----\n", "d:\\orderInfoError.txt");
						} catch (Exception e) {
							e.printStackTrace();
							log.info("写入日志异常");
						}
					} else {
						JSONObject orderInfo = new JSONObject(transOrder);
						if (orderInfo.getStr("code").equals("0")) {

							JSONArray ol = orderInfo.getJSONArray("data");
							dpBigDecimal = ol.getJSONObject(0).getBigDecimal("avgPx");

							price = dpBigDecimal;
							chajuCw = price.subtract(realbuypriceCw);
							dxzzlCw = chajuCw.divide(realbuypriceCw, 4, RoundingMode.DOWN)
									.multiply(new BigDecimal("100"));

							try {
								FileUtil.appendUtf8String(
										dt + "【止盈，止损，平仓，对冲-做空】：" + cw.getComment() + " 真实收益：" + dxzzlCw + " level:"
												+ cw.getLevel() + " " + orderInfo + "\n" + keyString + "\n----\n",
										"d:\\orderInfo.txt");
								// 异常订单需要保存起来后期使用。
							} catch (Exception e) {
								e.printStackTrace();
								log.info("写入日志异常");
							}

//							Fund1Gaoduanzhuangbei2OkExample example5 = new Fund1Gaoduanzhuangbei2OkExample();
//							example5.createCriteria().andLevelEqualTo(level);
//							example5.setTableName(tableName);
//							Fund1Gaoduanzhuangbei2Ok record5 = new Fund1Gaoduanzhuangbei2Ok();
//
//							record5.setIscurrent(1);
//							record5.setBuypriceReal(dpBigDecimal);
//							record5.setZhiying(new BigDecimal("0"));
//
//							record5.setFene(realfene);
//							fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record5, example5);
						} else {
							try {
								FileUtil.appendUtf8String(dt + "【止盈，止损，平仓，对冲-做空】后获取订单异常：" + " level:" + cw.getLevel()
										+ " " + orderInfo + "\n" + keyString + "\n----\n", "d:\\orderInfoError.txt");
								// 异常订单需要保存起来后期使用。
							} catch (Exception e) {
								e.printStackTrace();
								log.info("写入日志异常");
							}
						}

					}

				} catch (Exception e2) {
					try {
						FileUtil.appendUtf8String(dt + "【止盈，止损，平仓，对冲-做空】后获取订单异常：" + " level:" + cw.getLevel() + " "
								+ keyString + "\n" + e2.getMessage() + "\n----\n", "d:\\netError.txt");

					} catch (Exception e3) {
						e3.printStackTrace();
						log.info("写入日志异常");
					}
				}

				if (cw.getComment().indexOf("对冲") != -1) {

					for (Fund1Gaoduanzhuangbei2Ok item : duichongList) {
//						szDuichong = szDuichong.add(item.getFene());

//						BigDecimal price = cw.getCurrentprice();
						BigDecimal realbuyprice = cw.getBuypriceReal();
						BigDecimal chaju = price.subtract(realbuyprice);
						BigDecimal dxzzl = chaju.divide(realbuyprice, 4, RoundingMode.DOWN)
								.multiply(new BigDecimal("100"));

						Fund1Gaoduanzhuangbei2Ok recordCw = new Fund1Gaoduanzhuangbei2Ok();
						recordCw.setBuypriceReal(item.getSellprice());
						Fund1Gaoduanzhuangbei2OkExample exampleCw = new Fund1Gaoduanzhuangbei2OkExample();
						exampleCw.createCriteria().andIdEqualTo(item.getId());
//						if (cw.getLevel().intValue() == item.getLevel().intValue())
						recordCw.setComment("对冲:" + dxzzl.toString() + "ok");
						exampleCw.setTableName(tableName);
						fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelectiveResetZhiying(recordCw, exampleCw);
					}

					sz = (duichongList.size() * Float.parseFloat(sz)) + "";

					sz = sz.replaceAll("\\.0*$", "");

				} else {

					Fund1Gaoduanzhuangbei2Ok recordCw = new Fund1Gaoduanzhuangbei2Ok();
					recordCw.setBuypriceReal(cw.getSellprice());
					Fund1Gaoduanzhuangbei2OkExample exampleCw = new Fund1Gaoduanzhuangbei2OkExample();
					exampleCw.createCriteria().andIdEqualTo(cw.getId());
//					recordCw.setComment(cw.getComment() + "ok");
//					if (cw.getComment().indexOf("平仓") != -1)
//						recordCw.setName(cw.getName().replace("pc", ""));

					if (cw.getComment().indexOf("止盈") != -1)
						recordCw.setComment("止盈:" + dxzzlCw.toString() + "ok");
					if (cw.getComment().indexOf("止损") != -1)
						recordCw.setComment("止损:" + dxzzlCw.toString() + "ok");

					if (cw.getComment().indexOf("平仓") != -1) {
						recordCw.setName(cw.getName().replace("pc", ""));
						recordCw.setComment("平仓:" + dxzzlCw.toString() + "ok");
					}

					exampleCw.setTableName(tableName);
					fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelectiveResetZhiying(recordCw, exampleCw);

				}

				if (cw.getComment().indexOf("平仓") != -1) {

					try {
						FileUtil.appendUtf8String(dt + "【成功平仓-做空】：tab:" + tableName + " 【" + dxzzlCw + "】   level:"
								+ cw.getLevel() + " " + "\n" + "\n----\n", "d:\\对冲日志.txt");

					} catch (Exception e3) {
						e3.printStackTrace();
						log.info("写入日志异常");
					}
				}
				if (cw.getComment().indexOf("止损") != -1) {

					try {
						FileUtil.appendUtf8String(dt + "【成功止损-做空】：tab:" + tableName + " 【" + dxzzlCw + "】   level:"
								+ cw.getLevel() + " " + "\n" + "\n----\n", "d:\\止损日志.txt");

					} catch (Exception e3) {
						e3.printStackTrace();
						log.info("写入日志异常");
					}
				}
				if (cw.getComment().indexOf("止盈") != -1) {

					try {
						FileUtil.appendUtf8String(dt + "【成功止盈-做空】：tab:" + tableName + " 【" + dxzzlCw + "】   level:"
								+ cw.getLevel() + " " + "\n" + "\n----\n", "d:\\止盈日志.txt");

					} catch (Exception e3) {
						e3.printStackTrace();
						log.info("写入日志异常");
					}
				}
				if (cw.getComment().indexOf("对冲") != -1) {

					try {
						FileUtil.appendUtf8String(dt + "【成功对冲-做空】：tab:" + tableName + " 【" + dxzzlCw + "】   level:"
								+ cw.getLevel() + " " + "\n" + "\n----\n", "d:\\对冲日志.txt");

					} catch (Exception e3) {
						e3.printStackTrace();
						log.info("写入日志异常");
					}
				}
				BigDecimal realBuyPriceHistoryCwBigDecimal = cw.getBuypriceReal();
				// 价格写入python配置文件，订单号和对应买入价。
				String keyString2 = clOrdId + "_buy=" + realBuyPriceHistoryCwBigDecimal.toString() + "\n";
				try {
					FileUtil.appendUtf8String(keyString2, "D:\\project\\python\\btc\\config.ini");
				} catch (Exception e) {
					e.printStackTrace();
					log.info("写入日志异常");
				}

			} else {
				try {
					FileUtil.appendUtf8String(dt + "【止盈，止损，平仓，对冲】okx接口无法卖出(已回滚）：" + cw.getComment() + " level:"
							+ cw.getLevel() + " " + requestData + "\n" + trans + "\n----\n", "d:\\okxError.txt");

				} catch (Exception e3) {
					e3.printStackTrace();
					log.info("写入日志异常");
				}
				throw new XmallException("OKX服务异常:" + okxtransJsonObject.getStr("msg"));
			}

		}

//		else if (cw.getComment().indexOf("对冲") != -1 && duichongList.size() > 0) {
//			try {
//				FileUtil.appendUtf8String("【开始设置对冲】：tab:" + tableName + cw.getComment() + "   level:" + cw.getLevel()
//						+ " " + "\n" + "\n----\n", "d:\\对冲信息.txt");
//
//			} catch (Exception e3) {
//				e3.printStackTrace();
//				log.info("写入日志异常");
//			}

		// 前两层盈利只剩下80%，上层损失金额是总利润的20%的时候，平仓掉。设置平仓标记即可
//
//			for (Fund1Gaoduanzhuangbei2Ok item : duichongList) {
//				Fund1Gaoduanzhuangbei2Ok recordCw = new Fund1Gaoduanzhuangbei2Ok();
//				Fund1Gaoduanzhuangbei2OkExample exampleCw = new Fund1Gaoduanzhuangbei2OkExample();
//				exampleCw.createCriteria().andIdEqualTo(item.getId());
//				item.setComment(cw.getComment());
//				item.setName(cw.getName() + "pc");
//				exampleCw.setTableName(tableName);
//				fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(recordCw, exampleCw);
//
//				updatezhiying(item, tableName, duichongList);
//
//			}

//			try {
//				FileUtil.appendUtf8String("【成功设置对冲（即将平仓）】 tablename：" + tableName + " level:" + cw.getLevel()
//						+ "   levellast:" + cwLast.getLevel() + "\n" + "\n----\n", "d:\\对冲日志.txt");
//
//			} catch (Exception e3) {
//				e3.printStackTrace();
//				log.info("写入日志异常");
//			}

//		}

	}

	@Override
	public List<Fund1Gaoduanzhuangbei2Ok> getCangweisByTableName(String tableName) {
		// TODO Auto-generated method stub
//		url = "http://47.104.93.44:9000";

		Fund1Gaoduanzhuangbei2OkExample example = new Fund1Gaoduanzhuangbei2OkExample();
		example.createCriteria().andFeneIsNotNull();
		example.setTableName(tableName);
		example.setOrderByClause("id desc");
		List<Fund1Gaoduanzhuangbei2Ok> results = fund1Gaoduanzhuangbei2OkMapper.selectByExampleDynamic(example);

		return results;

	}

	@Override
	public void updateFund(Fund fund) {

		fundMapper.updateByPrimaryKeySelective(fund);

	}

	@Override
	public void updateIndex(String tableName, int tag, BigDecimal zzl, String comment) {
		// TODO Auto-generated method stub
//		url = "http://47.104.93.44:9000";

		FundExample example = new FundExample();
		example.createCriteria().andPlantableEqualTo(tableName);
		Fund record = new Fund();
		record.setCansell(tag);
		record.setZhangdiefu(zzl);
		record.setCreated(new Date());
		record.setComments(comment);
		fundMapper.updateByExampleSelective(record, example);

	}

	@Override
	public List<Fund> getIndex() {
		// TODO Auto-generated method stub
//		url = "http://47.104.93.44:9000";
		FundExample example = new FundExample();
		example.createCriteria().andBodongshuIsNotNull().andPrepareEqualTo(0);

		List<Fund> funds = fundMapper.selectByExample(example);
		return funds;

	}

	@Override
	public List<Fund> getIndexBtc() {
		// TODO Auto-generated method stub
//		url = "http://47.104.93.44:9000";
		FundExample example = new FundExample();
		example.createCriteria().andBodongshuIsNotNull().andPrepareEqualTo(3).andCateEqualTo("1");

		List<Fund> funds = fundMapper.selectByExample(example);
		return funds;

	}

	@Override
	public void updatezhiying(Fund1Gaoduanzhuangbei2Ok cw, String tableName,
			List<Fund1Gaoduanzhuangbei2Ok> duichongList, Fund fundindex) {
		// TODO Auto-generated method stub

	}

}
