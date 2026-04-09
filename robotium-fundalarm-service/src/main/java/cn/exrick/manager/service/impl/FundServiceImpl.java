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

import com.alibaba.dubbo.config.annotation.Service;
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
import cn.exrick.manager.service.FundService;
import cn.exrick.manager.service.OkxService;
import cn.exrick.manager.service.util.AlarmPlayer;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@Service
public class FundServiceImpl implements FundService {
	private static final Logger log = LoggerFactory.getLogger(FundServiceImpl.class);
	@Autowired
	private TbUserMapper tbUserMapper;
	@Autowired
	private TbCommandsDescMapper tbCommandsDescMapper;
	@Autowired
	private TbCommandsMapper tbCommandsMapper;
	@Autowired
	AlarmPlayer alarmPlayer;
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
		Fund1Gaoduanzhuangbei2OkExample example = new Fund1Gaoduanzhuangbei2OkExample();
		example.createCriteria().andIscurrentEqualTo(Integer.valueOf(1));
		example.setTableName(tableName);
		List<Fund1Gaoduanzhuangbei2Ok> results = this.fund1Gaoduanzhuangbei2OkMapper.selectByExampleDynamic(example);
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	@Override
	public Fund1Gaoduanzhuangbei2Ok getInfoByTableNameAndLevel(String tableName, int level) {
		Fund1Gaoduanzhuangbei2OkExample example = new Fund1Gaoduanzhuangbei2OkExample();
		example.createCriteria().andLevelEqualTo(Integer.valueOf(level));
		example.setTableName(tableName);
		List<Fund1Gaoduanzhuangbei2Ok> results = this.fund1Gaoduanzhuangbei2OkMapper.selectByExampleDynamic(example);
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	@Override
	@Transactional(rollbackFor = { Exception.class })
	public Integer updateCurrentPrice(String tableName, BigDecimal price, int tag, BigDecimal realbuyprice,
			BigDecimal money, Fund fundindex, Fund1Gaoduanzhuangbei2Ok fundItem, String lastvalue) {
		Fund1Gaoduanzhuangbei2OkExample exampleOri = new Fund1Gaoduanzhuangbei2OkExample();
		String trans = null;
		int jcTag = 1;
		int cwTag = 1;
		exampleOri.createCriteria().andIdEqualTo(fundItem.getId());
		exampleOri.setTableName(tableName);

		Fund1Gaoduanzhuangbei2OkExample example = new Fund1Gaoduanzhuangbei2OkExample();
		example.createCriteria().andIscurrentEqualTo(Integer.valueOf(1));
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
			dxzzlShichang = chajuShichang.divide(new BigDecimal(lastvalue), 5, RoundingMode.DOWN)
					.multiply(new BigDecimal("100"));
		}
		BigDecimal dxzzl = new BigDecimal("0");
		if ((tag == 1) || (tag == 2)) {
			BigDecimal chaju = price.subtract(realbuyprice);
			dxzzl = chaju.divide(realbuyprice, 4, RoundingMode.DOWN).multiply(new BigDecimal("100"));
			record.setComment(dxzzl.toString());
			String tips = "";
			if (tag == 1) {
				tips = "买入:";
			} else {
				tips = "卖出:";
			}
			record.setComment(tips + record.getComment());
			record.setWangge(dxzzlShichang);

			updateIndex(tableName, 1, money, record.getComment());
		} else {
			BigDecimal chaju = price.subtract(realbuyprice);
			dxzzl = chaju.divide(realbuyprice, 4, RoundingMode.DOWN).multiply(new BigDecimal("100"));

			record.setComment(dxzzl.toString());
			record.setWangge(dxzzlShichang);

			String tips = "";
			if (tag == 3) {
				tips = "快速卖出:";
			} else if (tag == 4) {
				tips = "快速买入:";
			} else if (tag == 7) {
				tips = "回调中:";
			} else if (tag == 8) {
				tips = "谨慎追涨:";
			}
			record.setComment(tips + record.getComment());
			if ((tag == 3) || (tag == 4) || (tag == 5) || (tag == 6)) {
				updateIndex(tableName, 1, money, record.getComment());
			} else {
				updateIndex(tableName, 0, money, record.getComment());
			}
		}
		this.fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record, example);
		if ((fundindex.getSinacode() != null) && (fundindex.getSinacode().equals("okx"))) {
			if ((tag == 2) || (tag == 3) || (tag == 8)) {
				BigDecimal realBuyPriceHistoryBigDecimal = fundItem.getBuypriceReal();
				int level = fundItem.getLevel().intValue() - 1;
				Fund1Gaoduanzhuangbei2Ok funditemNext = getInfoByTableNameAndLevel(tableName, level);
				if (funditemNext == null) {
//					return;
				}
				Integer historytag = Integer.valueOf(1);
				String pk = fundindex.getCode();
				int lianxuCount = 1;
				try {
					lianxuCount = new BigDecimal(fundItem.getCate()).intValue();
				} catch (Exception localException1) {
				}
				long dtHistory = System.currentTimeMillis();

				SimpleDateFormat sdfs = new SimpleDateFormat("yyyyMMddHHmm");

				// T交易不移动iscurrent指针
				Fund1Gaoduanzhuangbei2Ok record22 = new Fund1Gaoduanzhuangbei2Ok();

				record22.setIscurrent(Integer.valueOf(0));
				this.fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record22, exampleOri);

				if (funditemNext != null && (funditemNext.getFene() != null)
						&& (funditemNext.getFene().compareTo(new BigDecimal("0")) > 0)) {
					Fund1Gaoduanzhuangbei2OkExample example3 = new Fund1Gaoduanzhuangbei2OkExample();
					example3.createCriteria().andLevelEqualTo(Integer.valueOf(level));
					example3.setTableName(tableName);
					Fund1Gaoduanzhuangbei2Ok record3 = new Fund1Gaoduanzhuangbei2Ok();

					record3.setIscurrent(Integer.valueOf(1));
//					record3.setZhiying(new BigDecimal("0"));
					this.fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record3, example3);
				} else {
					if (funditemNext != null) {
						Fund1Gaoduanzhuangbei2OkExample example3 = new Fund1Gaoduanzhuangbei2OkExample();
						example3.createCriteria().andLevelEqualTo(Integer.valueOf(level));
						example3.setTableName(tableName);
						Fund1Gaoduanzhuangbei2Ok record3 = new Fund1Gaoduanzhuangbei2Ok();

						record3.setIscurrent(Integer.valueOf(1));
						record3.setBuypriceReal(funditemNext.getBuyprice());
						record3.setZhiying(new BigDecimal("0"));
						this.fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record3, example3);
					}

					List<Fund1Gaoduanzhuangbei2Ok> cangweis = getCangweisByTableName(tableName);

					// T交易不移动iscurrent指针
					if ((tag == 2 && fundItem.getName().indexOf("_bs") != -1) // T交易开仓买入（通过_bs标记区分）
							|| (tag == 8)) {

//					this.alarmPlayer.playAlarm(1);

						String instId = fundItem.getCode();
						String tdMode = "cash";
						if (instId.indexOf("SWAP") != -1)
							tdMode = "isolated";
						String clOrdId = UUID.randomUUID().toString().replace("-", "");
						String side = "buy";

						String ordType = "market";

						String sz = fundindex.getMoney().toString();

						int bt = 1;
						BigDecimal realfene = new BigDecimal("100");

						if (instId.indexOf("SWAP") != -1) {
							realfene = fundindex.getHuiche().multiply(fundindex.getMoney());
						}

//						else if (instId.equals("NAVX-USDT")) {
//							sz = "2";
//							realfene = new BigDecimal("100");
//						} 

//						else if (instId.equals("PEPE-USDT")) {
//							sz = "0.1";
//							realfene = new BigDecimal("1000000");
//						} else if (instId.equals("SOL-USDT")) {
//							sz = "2";
//							realfene = new BigDecimal("0.01");
//						} else if (instId.equals("NOT-USDT")) {
//							sz = "2";
//							realfene = new BigDecimal("100");
//						}
//						 else if (instId.equals("BTC-USDT")) {
//								sz = "2";
//								realfene = new BigDecimal("100");
//							}
//						 else if (instId.equals("XAUT-USDT")) {
//								sz = "4";
//								realfene = new BigDecimal("100");
//							}
//						 else if (instId.equals("TRUMP-USDT")) {
//								sz = "4";
//								realfene = new BigDecimal("100");
//							}
//						 else if (instId.equals("BERA-USDT")) {
//								sz = "2";
//								realfene = new BigDecimal("100");
//							}
//						else {
//							bt = 0;
//						}
						if (bt == 1) {
							JSONObject requestData = new JSONObject();
							requestData.put("sz", sz);
							// requestData.put("tgtCcy", "base_ccy");
							requestData.put("instId", instId);
							if (instId.indexOf("SWAP") != -1)
								requestData.put("posSide", "long");
							requestData.put("tdMode", tdMode);
							requestData.put("clOrdId", clOrdId);

							requestData.put("side", side);
							requestData.put("ordType", ordType);
							System.out.println("requestdata:" + requestData.toString());
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String dt = sdf.format(new Date());
							String zhuizhangTrans = this.okxService.trade("/api/v5/trade/order", "POST",
									requestData.toString());
							System.out.println(zhuizhangTrans);
							int restag = 1;
							if (zhuizhangTrans == null) {
								try {
									FileUtil.appendUtf8String(dt + "【追涨】买入异常（请核对）：" + "  level:" + level + "  "
											+ requestData + "\n----\n", "d:\\okxError.txt");
								} catch (Exception e) {
									e.printStackTrace();
									log.info("写入日志异常");
								}
								restag = 0;
//								throw new XmallException("OKX服务异常:" + requestData.toString());
							}
//							JSONObject okxtransJsonObject = new JSONObject(zhuizhangTrans);
//							if (okxtransJsonObject.getStr("code").equals("0")) {
							String keyString = "?clOrdId=" + clOrdId + "&instId=" + instId;
//							try {
							String transOrder = this.okxService.trade("/api/v5/trade/order" + keyString, "GET", "");
							int k = 0;
							while ((transOrder == null || (transOrder != null
									&& !JSONUtil.parseObj(transOrder).getStr("code").contentEquals("51603")
									&& !JSONUtil.parseObj(transOrder).getStr("code").contentEquals("0")))) {
								System.out.println(keyString);
								System.out.println(transOrder);
								try {
									FileUtil.appendUtf8String(dt + "【追涨】买入后获取订单异常：" + "  level:" + level + "  "
											+ keyString + transOrder + "\n----\n", "d:\\orderInfoError.txt");
								} catch (Exception e) {
									e.printStackTrace();
									log.info("写入日志异常");
								}
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								k++;
								transOrder = this.okxService.trade("/api/v5/trade/order" + keyString, "GET", "");
							}
							if (transOrder == null || (transOrder != null
									&& !JSONUtil.parseObj(transOrder).getStr("code").contentEquals("51603")
									&& !JSONUtil.parseObj(transOrder).getStr("code").contentEquals("0"))) {
								System.out.println(transOrder);
								try {
									FileUtil.appendUtf8String(dt + "【追涨】买入后获取订单异常：" + "  level:" + level + "  "
											+ keyString + transOrder + "\n----\n", "d:\\orderInfoError.txt");
								} catch (Exception e) {
									e.printStackTrace();
									log.info("写入日志异常");
								}
								// throw new XmallException("OKX服务异常:" + requestData.toString());
								return 0;
							}
							JSONObject orderInfo = JSONUtil.parseObj(transOrder);
							if (orderInfo.getStr("code").equals("0")) {
								try {
									FileUtil.appendUtf8String(dt + "【追涨】买入：" + " level:" + level + " " + orderInfo
											+ "\n" + keyString + "\n----\n", "d:\\orderInfo.txt");
								} catch (Exception e) {
									e.printStackTrace();
									log.info("写入日志异常");
								}
								System.out.println("订单信息：" + orderInfo);
								JSONArray ol = orderInfo.getJSONArray("data");
								BigDecimal dpBigDecimal = ol.getJSONObject(Integer.valueOf(0)).getBigDecimal("avgPx");
								if (instId.indexOf("SWAP") == -1)
									realfene = ol.getJSONObject(Integer.valueOf(0)).getBigDecimal("accFillSz")
											.multiply(new BigDecimal("0.999"));

								Fund1Gaoduanzhuangbei2OkExample example5 = new Fund1Gaoduanzhuangbei2OkExample();
								example5.createCriteria().andLevelEqualTo(Integer.valueOf(level));
								example5.setTableName(tableName);
								Fund1Gaoduanzhuangbei2Ok record5 = new Fund1Gaoduanzhuangbei2Ok();

								record5.setIscurrent(Integer.valueOf(1));
								record5.setBuypriceReal(dpBigDecimal);
								record5.setZhiying(new BigDecimal("0"));
								record5.setFene(realfene);

								// 【双向持仓】保存posId到firsttime字段
								try {
									String posId = ol.getJSONObject(0).getStr("posId");
									if (posId != null && !posId.isEmpty()) {
										record5.setFirsttime(posId);
										System.out.println("【追涨买入】保存posId: " + posId);
									}
								} catch (Exception e) {
									System.err.println("【追涨买入】提取posId失败: " + e.getMessage());
								}

								this.fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record5, example5);
								String buylog = pk + "_buyprice_long_" + sdfs.format(new Date()) + "=" + dpBigDecimal
										+ "\n";
								try {
									FileUtil.appendUtf8String(buylog, "D:\\signal.property");
								} catch (Exception e) {
									e.printStackTrace();
									log.info("写入日志异常");
								}
							} else {

								// throw new XmallException("return【追涨】买入后获取订单异常");
								return 0;

							}

						}

					}
				}
				if ((fundItem.getFene() != null) && (fundItem.getFene().compareTo(new BigDecimal("0")) > 0)) {
					if (fundItem.getName().indexOf("_bs") == -1 || tag != 3) {
						Fund1Gaoduanzhuangbei2Ok record2 = new Fund1Gaoduanzhuangbei2Ok();

						record2.setIscurrent(Integer.valueOf(0));
						this.fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record2, exampleOri);
						return 0;
					}
					String instId = fundItem.getCode() + "";
					String tdMode = "cash";
					if (instId.indexOf("SWAP") != -1)
						tdMode = "isolated";
					String clOrdId = UUID.randomUUID().toString().replace("-", "");
					String side = "sell";

					String ordType = "market";

					String sz = "0";
					sz = fundItem.getFene().toString();

					if (instId.indexOf("SWAP") != -1)
						sz = fundItem.getFene().divide(fundindex.getHuiche()).toString();

//					if (instId.contentEquals("DOGE-USDT")) {
//						sz = fundItem.getFene().toString();//ivide(new BigDecimal("1000"), RoundingMode.DOWN).toString();
//					} else if (instId.contentEquals("PEPE-USDT")) {
//						sz = fundItem.getFene().divide(new BigDecimal("10000000"), RoundingMode.DOWN).toString();
//					} else if (instId.contentEquals("SOL-USDT")) {
//						sz = fundItem.getFene().divide(new BigDecimal("1"), RoundingMode.DOWN).toString();
//					} else if (instId.contentEquals("NOT-USDT")) {
//						sz = fundItem.getFene().toString();//.divide(new BigDecimal("100"), RoundingMode.DOWN).toString();
//					}
//					else if (instId.contentEquals("BTC-USDT")) {
//						sz = fundItem.getFene().toString();//.divide(new BigDecimal("100"), RoundingMode.DOWN).toString();
//					}
//					else if (instId.contentEquals("BERA-USDT")) {
//						sz = fundItem.getFene().toString();//.divide(new BigDecimal("100"), RoundingMode.DOWN).toString();
//					}
//					else if (instId.contentEquals("NAVX-USDT")) {
//						sz = fundItem.getFene().toString();//.divide(new BigDecimal("100"), RoundingMode.DOWN).toString();
//					}
//					else if (instId.contentEquals("XAUT-USDT")) {
//						sz = fundItem.getFene().toString();//.divide(new BigDecimal("100"), RoundingMode.DOWN).toString();
//					}
//					else if (instId.contentEquals("TRUMP-USDT")) {
//						sz = fundItem.getFene().toString();//.divide(new BigDecimal("100"), RoundingMode.DOWN).toString();
//					}
//				
//					else {
//						Fund1Gaoduanzhuangbei2Ok record4 = new Fund1Gaoduanzhuangbei2Ok();
//
//						record4.setIscurrent(Integer.valueOf(0));
//						this.fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record4, exampleOri);
//						return;
//					}
					Fund1Gaoduanzhuangbei2Ok record2 = new Fund1Gaoduanzhuangbei2Ok();

					this.fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelectiveReset(record2, exampleOri);

					JSONObject requestData = new JSONObject();
					requestData.put("sz", sz);
					requestData.put("instId", instId);
					requestData.put("tdMode", tdMode);
					requestData.put("tgtCcy", "base_ccy");
					if (instId.indexOf("SWAP") != -1)
						requestData.put("posSide", "long");

					requestData.put("clOrdId", clOrdId);

					requestData.put("side", side);
					requestData.put("ordType", ordType);

					trans = this.okxService.trade("/api/v5/trade/order", "POST", requestData.toString());
//					if (trans == null) {
//						throw new XmallException("OKX服务异常:as异常:" + requestData.toString());
//					}
					System.out.println("requestData:" + requestData);
					System.out.println("res:" + trans);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String dt = sdf.format(new Date());

//					JSONObject okxtransJsonObject = new JSONObject(trans);
//					if (okxtransJsonObject.getStr("code").equals("0")) {
					String orderkeyString = "?clOrdId=" + clOrdId + "&instId=" + instId;
					String keyString = clOrdId + "_sell=" + realBuyPriceHistoryBigDecimal.toString() + "\n";
					try {
						FileUtil.appendUtf8String(keyString, "D:\\project\\python\\btc\\config.ini");
					} catch (Exception e) {
						e.printStackTrace();
						log.info("写入日志异常");
					}
					String transOrder = this.okxService.trade("/api/v5/trade/order" + orderkeyString, "GET", "");
					System.out.println(transOrder);
					int k = 0;
					while ((transOrder == null || (transOrder != null
							&& !JSONUtil.parseObj(transOrder).getStr("code").contentEquals("51603")
							&& !JSONUtil.parseObj(transOrder).getStr("code").contentEquals("0")))) {
						System.out.println(orderkeyString);
						System.out.println(transOrder);
						try {
							FileUtil.appendUtf8String(dt + "【as卖出】as卖出后获取订单异常：" + "  level:" + level + "  "
									+ orderkeyString + "\t" + (transOrder) + "\n----\n", "d:\\orderInfoError.txt");
						} catch (Exception e) {
							e.printStackTrace();
							log.info("写入日志异常");
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						k++;
						transOrder = this.okxService.trade("/api/v5/trade/order" + orderkeyString, "GET", "");
					}
					if (transOrder == null || (transOrder != null
							&& !JSONUtil.parseObj(transOrder).getStr("code").contentEquals("51603")
							&& !JSONUtil.parseObj(transOrder).getStr("code").contentEquals("0"))) {
						try {
							FileUtil.appendUtf8String(dt + "【as卖出】as卖出后获取订单异常：" + "  level:" + level + "  "
									+ orderkeyString + "\t" + (transOrder) + "\n----\n", "d:\\orderInfoError.txt");
						} catch (Exception e) {
							e.printStackTrace();
							log.info("写入日志异常");
						}
						// throw new XmallException("卖出后获取订单异常");
						return 0;
					} else {
						JSONObject orderInfo = JSONUtil.parseObj(transOrder);
						if (orderInfo.getStr("code").equals("0")) {
							JSONArray ol = orderInfo.getJSONArray("data");
							BigDecimal dpBigDecimal = ol.getJSONObject(Integer.valueOf(0)).getBigDecimal("avgPx");

							price = dpBigDecimal;

							System.out.println("平均成交价：" + price);
							System.out.println("买入价：" + fundItem.getBuypriceReal());
							BigDecimal chajuCw = price.subtract(fundItem.getBuypriceReal());
							BigDecimal dxzzlCw = chajuCw.divide(fundItem.getBuypriceReal(), 4, RoundingMode.DOWN)
									.multiply(new BigDecimal("100"));
							try {
								FileUtil.appendUtf8String(dt + "【卖出】okx卖出： " + "真实收益:" + dxzzlCw + " level:" + level
										+ " " + requestData + "\n" + trans + "\n----\n", "d:\\okx.txt");
							} catch (Exception e3) {
								e3.printStackTrace();
								log.info("写入日志异常");
							}
							try {
								FileUtil.appendUtf8String(dt + "【成功止盈as-做多平仓】：tab:" + tableName + " 【" + dxzzlCw
										+ "】   level:" + level + " " + "\n" + "\n----\n", "d:\\止盈日志.txt");
							} catch (Exception e3) {
								e3.printStackTrace();
								log.info("写入日志异常");
							}
							this.alarmPlayer.playAlarm(5);
						} else {

//							try {
//								FileUtil.appendUtf8String(dt + "【as卖出】as卖出后获取订单异常：" + "  level:" + level + "  "
//										+ orderkeyString + "\n----\n", "d:\\orderInfoError.txt");
//							} catch (Exception e) {
//								e.printStackTrace();
//								log.info("写入日志异常");
//							}
							// throw new XmallException("return【as卖出】as卖出后获取订单异常");
							return 0;

						}
					}
//					} else {
//						try {
//							FileUtil.appendUtf8String(dt + "【卖出】okx接口无法卖出：" + " level:" + level + " " + requestData
//									+ "\n" + trans + "\n----\n", "d:\\okxError.txt");
//						} catch (Exception e3) {
//							e3.printStackTrace();
//							log.info("写入日志异常");
//						}
//						throw new XmallException("OKX服务异常:" + okxtransJsonObject.getStr("msg"));
//					}
				} else {
					Fund1Gaoduanzhuangbei2Ok record2 = new Fund1Gaoduanzhuangbei2Ok();

					this.fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelectiveReset(record2, exampleOri);
				}
			} else if ((tag == 1) || (tag == 4) || (tag == 7)) {
				int level = fundItem.getLevel().intValue() + 1;
				Fund1Gaoduanzhuangbei2Ok funditemNext = getInfoByTableNameAndLevel(tableName, level);
				if (funditemNext == null) {
					return 0;
				}
				Fund1Gaoduanzhuangbei2Ok record2 = new Fund1Gaoduanzhuangbei2Ok();

				record2.setIscurrent(Integer.valueOf(0));
				this.fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record2, exampleOri);

				int buyTag = 0;
				if ((funditemNext.getFene() != null) && (funditemNext.getFene().compareTo(new BigDecimal("0")) > 0)) {
					Fund1Gaoduanzhuangbei2OkExample example3 = new Fund1Gaoduanzhuangbei2OkExample();
					example3.createCriteria().andLevelEqualTo(Integer.valueOf(level));
					example3.setTableName(tableName);
					Fund1Gaoduanzhuangbei2Ok record3 = new Fund1Gaoduanzhuangbei2Ok();
//					record3.setZhiying(new BigDecimal("0"));
					record3.setIscurrent(Integer.valueOf(1));
					this.fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record3, example3);
				} else {
					buyTag = 1;

					Fund1Gaoduanzhuangbei2OkExample example3 = new Fund1Gaoduanzhuangbei2OkExample();
					example3.createCriteria().andLevelEqualTo(Integer.valueOf(level));
					example3.setTableName(tableName);
					Fund1Gaoduanzhuangbei2Ok record3 = new Fund1Gaoduanzhuangbei2Ok();

					record3.setIscurrent(Integer.valueOf(1));
					record3.setZhiying(new BigDecimal("0"));
					record3.setBuypriceReal(funditemNext.getBuyprice());

					this.fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record3, example3);
					if (tag == 1) {// &&
									// (tag
									// ==
									// 4)
//						this.alarmPlayer.playAlarm(1);

						String instId = fundItem.getCode() + "";
						String tdMode = "cash";
						if (instId.indexOf("SWAP") != -1)
							tdMode = "isolated";
						String clOrdId = UUID.randomUUID().toString().replace("-", "");
						String side = "buy";

						String ordType = "market";

						String sz = fundindex.getMoney().toString();// "0";
						BigDecimal realfene = fundItem.getFene();

						if (instId.indexOf("SWAP") != -1) {
							realfene = fundindex.getHuiche().multiply(fundindex.getMoney());
						}

//						if (instId.equals("DOGE-USDT")) {
//							sz = "2";
//							realfene = new BigDecimal("100");
//						} 
//						else if (instId.equals("NAVX-USDT")) {
//							sz = "2";
//							realfene = new BigDecimal("100");
//						}
//						else if (instId.equals("PEPE-USDT")) {
//							sz = "0.1";
//							realfene = new BigDecimal("1000000");
//						} else if (instId.equals("SOL-USDT")) {
//							sz = "0.01";
//							realfene = new BigDecimal("0.01");
//						} else if (instId.equals("NOT-USDT")) {
//							sz = "2";
//							realfene = new BigDecimal("100");
//						} 
//						else if (instId.equals("BTC-USDT")) {
//							sz = "2";
//							realfene = new BigDecimal("100");
//						}
//						else if (instId.equals("BERA-USDT")) {
//							sz = "2";
//							realfene = new BigDecimal("100");
//						}
//						else if (instId.equals("XAUT-USDT")) {
//							sz = "4";
//							realfene = new BigDecimal("100");
//						}
//						else if (instId.equals("TRUMP-USDT")) {
//							sz = "4";
//							realfene = new BigDecimal("100");
//						}
//						else {
//							return;
//						}
						JSONObject requestData = new JSONObject();
						requestData.put("sz", sz);
						requestData.put("instId", instId);
						requestData.put("tdMode", tdMode);
						if (instId.indexOf("SWAP") != -1)
							requestData.put("posSide", "long");
						requestData.put("clOrdId", clOrdId);
						// requestData.put("tgtCcy", "base_ccy");

						requestData.put("side", side);
						requestData.put("ordType", ordType);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String dt = sdf.format(new Date());
						trans = this.okxService.trade("/api/v5/trade/order", "POST", requestData.toString());
						if (trans == null) {
							try {
								FileUtil.appendUtf8String(
										dt + "【补仓】买入异常（请核对）：" + "  level:" + level + "  " + requestData + "\n----\n",
										"d:\\okxError.txt");
							} catch (Exception e) {
								e.printStackTrace();
								log.info("写入日志异常");
							}
//							throw new XmallException("OKX服务异常:买入异常：" + requestData.toString());
						}
						System.out.println(trans);
//						JSONObject okxtransJsonObject = new JSONObject(trans);
//						if (okxtransJsonObject.getStr("code").equals("0")) {
						String keyString = "?clOrdId=" + clOrdId + "&instId=" + instId;

						String transOrder = this.okxService.trade("/api/v5/trade/order" + keyString, "GET", "");
						int k = 0;

						while ((transOrder == null || (transOrder != null
								&& !JSONUtil.parseObj(transOrder).getStr("code").contentEquals("51603")
								&& !JSONUtil.parseObj(transOrder).getStr("code").contentEquals("0")))) {
							System.out.println(keyString);
							System.out.println(transOrder);
							try {
								FileUtil.appendUtf8String(dt + "【补仓】买入后获取订单异常(请核对）：" + "  level:" + level + "  "
										+ keyString + transOrder + "\n----\n", "d:\\orderInfoError.txt");
							} catch (Exception e) {
								e.printStackTrace();
								log.info("写入日志异常");
							}
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							k++;
							transOrder = this.okxService.trade("/api/v5/trade/order" + keyString, "GET", "");
						}
						if (transOrder == null || (transOrder != null
								&& !JSONUtil.parseObj(transOrder).getStr("code").contentEquals("51603")
								&& !JSONUtil.parseObj(transOrder).getStr("code").contentEquals("0"))) {
							try {
								FileUtil.appendUtf8String(dt + "【补仓】买入后获取订单异常(请核对）：" + "  level:" + level + "  "
										+ keyString + transOrder + "\n----\n", "d:\\orderInfoError.txt");
							} catch (Exception e) {
								e.printStackTrace();
								log.info("写入日志异常");
							}
							throw new XmallException("【补仓买入后】okx订单接口异常:" + keyString);
						}
						JSONObject orderInfo = JSONUtil.parseObj(transOrder);
						if (orderInfo.getStr("code").equals("0")) {
							JSONArray ol = orderInfo.getJSONArray("data");
							System.out.println("订单信息：" + orderInfo);
							BigDecimal dpBigDecimal = ol.getJSONObject(Integer.valueOf(0)).getBigDecimal("avgPx");
							if (instId.indexOf("SWAP") == -1)
								realfene = ol.getJSONObject(Integer.valueOf(0)).getBigDecimal("accFillSz")
										.multiply(new BigDecimal("0.999"));

							Fund1Gaoduanzhuangbei2OkExample example4 = new Fund1Gaoduanzhuangbei2OkExample();
							example4.createCriteria().andLevelEqualTo(Integer.valueOf(level));
							example4.setTableName(tableName);
							Fund1Gaoduanzhuangbei2Ok record4 = new Fund1Gaoduanzhuangbei2Ok();

							record4.setIscurrent(Integer.valueOf(1));
							record4.setBuypriceReal(dpBigDecimal);
							record4.setFene(realfene);
							record4.setZhiying(new BigDecimal("0"));

							// 【双向持仓】保存posId到firsttime字段
							try {
								String posId = ol.getJSONObject(0).getStr("posId");
								if (posId != null && !posId.isEmpty()) {
									record4.setFirsttime(posId);
									System.out.println("【补仓买入】保存posId: " + posId);
								}
							} catch (Exception e) {
								System.err.println("【补仓买入】提取posId失败: " + e.getMessage());
							}

							this.fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record4, example4);
							try {
								FileUtil.appendUtf8String(
										dt + "【补仓】买入：" + "  level:" + level + "  " + keyString + "\n----\n",
										"d:\\orderInfo.txt");
							} catch (Exception e) {
								e.printStackTrace();
								log.info("写入日志异常");
							}
						} else {
//							try {
//								FileUtil.appendUtf8String(dt + "【补仓】买入后获取订单异常：" + " level:" + level + " " + orderInfo
//										+ "\n" + keyString + "\n----\n", "d:\\orderInfoError.txt");
//							} catch (Exception e) {
//								e.printStackTrace();
//								log.info("写入日志异常");
//							}
//							throw new XmallException("【补仓买入后】okx订单接口异常:" + keyString);

							// throw new XmallException("return【补仓买入后】okx订单接口异常:" + keyString);
							return 0;

						}
//						} else {
//							try {
//								FileUtil.appendUtf8String(dt + "【补仓】okx接口无法买入：" + " level:" + level + " " + requestData
//										+ "\n" + trans + "\n----\n", "d:\\okxError.txt");
//							} catch (Exception e3) {
//								e3.printStackTrace();
//								log.info("写入日志异常");
//							}
//							throw new XmallException("OKX服务异常:" + okxtransJsonObject.getStr("msg"));
//						}
					}
				}
				int levelLast = funditemNext.getLevel().intValue() - funditemNext.getZhisun();
				Fund1Gaoduanzhuangbei2Ok funditemLast = getInfoByTableNameAndLevel(tableName, levelLast);
				if (funditemLast == null) {
				}
			}
		}
		return 1;
	}

	@Override
	@Transactional(rollbackFor = { Exception.class })
	public void updatezhiying(Fund1Gaoduanzhuangbei2Ok cw, String tableName,
			List<Fund1Gaoduanzhuangbei2Ok> duichongList, Fund fundindex) {
		Fund1Gaoduanzhuangbei2Ok cwLocal = new Fund1Gaoduanzhuangbei2Ok();
		cwLocal.setMaxdiefu5(cw.getMaxdiefu5());
		cwLocal.setMaxzhangfu5(cw.getMaxzhangfu5());
		cwLocal.setMinprice5(cw.getMinprice5());
		cwLocal.setMaxprice5(cw.getMaxprice5());
		cwLocal.setComment(cw.getComment());
		cwLocal.setCurrentprice(cw.getCurrentprice());
		cwLocal.setMaxpriceniu(cw.getMaxpriceniu());
		cwLocal.setYangbencount(cw.getYangbencount());
		cwLocal.setZhiying(cw.getZhiying());
		cwLocal.setUpcount(cw.getUpcount());
		cwLocal.setFene(cw.getFene()); // 添加fene字段复制
		Fund1Gaoduanzhuangbei2OkExample example = new Fund1Gaoduanzhuangbei2OkExample();
		example.createCriteria().andIdEqualTo(cw.getId());
		example.setTableName(tableName);
		this.fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(cwLocal, example);
		if ((cw.getComment().indexOf("止盈") != -1) || (cw.getComment().indexOf("止损") != -1)
				|| (cw.getComment().indexOf("平仓") != -1) || (cw.getComment().indexOf("对冲") != -1)) {
			updateIndex(tableName, 1, new BigDecimal("0"), cw.getComment());

			String instId = cw.getCode() + "";
			String tdMode = "cash";
			if (instId.indexOf("SWAP") != -1)
				tdMode = "isolated";
			String clOrdId = UUID.randomUUID().toString().replace("-", "");
			String side = "sell";

			String ordType = "market";

			BigDecimal szBigDecimal = cw.getFene();

			// ====== 全平场景：使用OKX一键平仓接口（不依赖数据库fene） ======
			if (cw.getComment().indexOf("全平") != -1) {
				String closeAllKey = "closeall:done:" + instId;
				String closeAllFailKey = "closeall:fail:" + instId;
				String closeAllResultKey = "closeall:result:" + instId; // 存储结果供外层查询

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dt = sdf.format(new Date());

				// 【事务内】清空所有有持仓的底仓档位（fene>0 的才需要清空）
				int clearedCount = 0;
				for (Fund1Gaoduanzhuangbei2Ok position : duichongList) {
					// 只处理有持仓的档位（fene > 0）
					if (position.getFene() == null || position.getFene().compareTo(BigDecimal.ZERO) <= 0) {
						continue; // 跳过已空仓的档位
					}

					Fund1Gaoduanzhuangbei2OkExample posExample = new Fund1Gaoduanzhuangbei2OkExample();
					posExample.createCriteria().andIdEqualTo(position.getId());
					posExample.setTableName(tableName);

					Fund1Gaoduanzhuangbei2Ok posUpdate = new Fund1Gaoduanzhuangbei2Ok();
					// 【清空fene】设置为0表示无持仓
					posUpdate.setFene(BigDecimal.ZERO);
					posUpdate.setZhiying(BigDecimal.ZERO);
					posUpdate.setComment(position.getId().equals(cw.getId()) ? "全平平仓：OKX一键全平" : "全平清仓：同批次已平");
					posUpdate.setMaxprice5(BigDecimal.ZERO);
					posUpdate.setMinprice5(BigDecimal.ZERO);
					posUpdate.setMaxpriceniu(BigDecimal.ZERO);
					posUpdate.setMaxzhangfu5(BigDecimal.ZERO);
					posUpdate.setMaxdiefu5(BigDecimal.ZERO);

					this.fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(posUpdate, posExample);
					clearedCount++;
					System.out.println("【全平事务】清空档位 " + position.getLevel() + " fene=" + position.getFene() + " -> 0");
				}
				System.out.println("【全平事务】共清空 " + clearedCount + "/" + duichongList.size() + " 个档位的fene");
				System.out.println("【全平事务】所有档位fene清空完成");

				// 检查是否已成功全平
				if (jedisClient.exists(closeAllKey)) {
					System.out.println("【全平跳过】已处理，档位=" + cw.getLevel());
					return;
				}

				// 检查是否已连续失败3次
				String failCountStr = jedisClient.get(closeAllFailKey);
				int failCount = failCountStr == null ? 0 : Integer.parseInt(failCountStr);
				if (failCount >= 3) {
					String alarmMsg = dt + "【全平告警】" + instId + " 已连续失败3次，请手动检查仓位！";
					System.err.println(alarmMsg);
					// 记录到文件
					try {
						FileUtil.appendUtf8String(alarmMsg + "\n", "d:\\okxError.txt");
					} catch (Exception e) {
						e.printStackTrace();
					}
					// 存储结果到Redis供外层查询
					jedisClient.setex(closeAllResultKey, 300, "ALARM:连续失败3次");
					return;
				}

				// 调用OKX一键平仓接口（带重试）
				String closeResult = null;
				boolean success = false;
				String errorMsg = "";
				int restag = 0; // 0-失败, 1-成功

				for (int retry = 0; retry < 3 && !success; retry++) {
					try {
						if (retry > 0) {
							System.out.println("【全平重试】第" + retry + "次尝试...");
							Thread.sleep(1000);
						}

						JSONObject closeParams = new JSONObject();
						closeParams.put("instId", instId);
						closeParams.put("mgnMode", tdMode);
						closeParams.put("posSide", "long");

						closeResult = this.okxService.trade("/api/v5/trade/close-position", "POST",
								closeParams.toString());
						System.out.println("【OKX全平】尝试" + (retry + 1) + "结果：" + closeResult);

						// 使用JSONUtil解析返回码（项目标准方式）
						if (closeResult != null) {
							JSONObject resultJson = JSONUtil.parseObj(closeResult);
							String code = resultJson.getStr("code");
							if ("0".equals(code)) {
								success = true;
								restag = 1;
							} else {
								errorMsg = "code=" + code + ",msg=" + resultJson.getStr("msg");
							}
						} else {
							errorMsg = "返回null";
						}
					} catch (Exception e) {
						errorMsg = e.getMessage();
						System.err.println("【全平异常】尝试" + (retry + 1) + "失败：" + errorMsg);
						// 记录到文件
						try {
							FileUtil.appendUtf8String(
									dt + "【全平异常】尝试" + (retry + 1) + "失败：" + errorMsg + "  instId:" + instId + "\n",
									"d:\\okxError.txt");
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}

				if (success) {
					// 查询订单状态确认（项目标准做法）
					String checkKey = "?instId=" + instId;
					String positionInfo = this.okxService.trade("/api/v5/account/positions" + checkKey, "GET", "");
					System.out.println("【全平确认】仓位查询：" + positionInfo);

					jedisClient.setex(closeAllKey, 60, "1");
					jedisClient.del(closeAllFailKey);
					jedisClient.del("t:global:order");
					// 存储成功结果供外层查询
					jedisClient.setex(closeAllResultKey, 60, "SUCCESS:" + closeResult);
					System.out.println("【全平成功】" + instId + " 仓位已全部平仓");
				} else {
					int newFailCount = failCount + 1;
					jedisClient.setex(closeAllFailKey, 300, String.valueOf(newFailCount));
					// 存储失败结果供外层查询
					jedisClient.setex(closeAllResultKey, 300, "FAIL:" + errorMsg);

					String failMsg = dt + "【全平失败】" + instId + " 失败次数=" + newFailCount + "/3，错误：" + errorMsg;
					System.err.println(failMsg);

					// 记录到文件
					try {
						FileUtil.appendUtf8String(failMsg + "\n", "d:\\okxError.txt");
					} catch (Exception e) {
						e.printStackTrace();
					}

					// 如果连续失败3次，抛出异常（项目标准做法）
					if (newFailCount >= 3) {
						throw new XmallException("全平失败3次，请手动检查仓位：" + instId);
					}
				}

				return;
			}
			// ====== 对冲场景：累加fene ======
			else if (cw.getComment().indexOf("对冲") != -1) {
				BigDecimal dcSum = new BigDecimal("0");
				szBigDecimal = new BigDecimal("0");
				for (Fund1Gaoduanzhuangbei2Ok item : duichongList) {
					dcSum = dcSum.add(item.getBuypriceReal().multiply(item.getFene()));
					szBigDecimal = szBigDecimal.add(item.getFene());
				}
				cw.setFene(szBigDecimal);
				cw.setBuypriceReal(dcSum.divide(szBigDecimal, 5, RoundingMode.DOWN));
			}
			String sz = szBigDecimal.toString();
			if (instId.indexOf("SWAP") != -1)
				sz = szBigDecimal.divide(fundindex.getHuiche()).toString();
//      if (instId.contentEquals("DOGE-USDT")) {
//        sz = cw.getFene().toString();//.divide(new BigDecimal("1000"), RoundingMode.DOWN).toString();
//      }
//      else if (instId.contentEquals("NAVX-USDT")) {
//          sz = cw.getFene().toString();//.divide(new BigDecimal("1000"), RoundingMode.DOWN).toString();
//        }
//      else if (instId.contentEquals("PEPE-USDT")) {
//        sz = cw.getFene().divide(new BigDecimal("10000000"), RoundingMode.DOWN).toString();
//      } else if (instId.contentEquals("SOL-USDT")) {
//        sz = cw.getFene().divide(new BigDecimal("1"), RoundingMode.DOWN).toString();
//      } else if (instId.contentEquals("NOT-USDT")) {
//        sz = cw.getFene().toString();//.divide(new BigDecimal("100"), RoundingMode.DOWN).toString();
//      }
//      else if (instId.contentEquals("BTC-USDT")) {
//          sz = cw.getFene().toString();//.divide(new BigDecimal("100"), RoundingMode.DOWN).toString();
//        }
//      else if (instId.contentEquals("BERA-USDT")) {
//          sz = cw.getFene().toString();//.divide(new BigDecimal("100"), RoundingMode.DOWN).toString();
//        }
//      else if (instId.contentEquals("XAUT-USDT")) {
//          sz = cw.getFene().toString();//.divide(new BigDecimal("100"), RoundingMode.DOWN).toString();
//        }
//      else if (instId.contentEquals("TRUMP-USDT")) {
//          sz = cw.getFene().toString();//.divide(new BigDecimal("100"), RoundingMode.DOWN).toString();
//        }
//      else {
//        return;
//      }
			System.out.println("止盈或者平仓的张数[lps]：" + sz);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dt = sdf.format(new Date());

			JSONObject requestData = new JSONObject();
			requestData.put("sz", sz);
			requestData.put("instId", instId);
			requestData.put("tdMode", tdMode);
			if (instId.indexOf("SWAP") != -1)
				requestData.put("posSide", "long");
			requestData.put("tgtCcy", "base_ccy");

			requestData.put("clOrdId", clOrdId);

			requestData.put("side", side);

			// 【双向持仓】读取posId并指定平仓（避免FIFO）
			String posId = cw.getFirsttime();
			if (posId != null && !posId.isEmpty() && !"null".equals(posId)) {
				requestData.put("posId", posId);
				requestData.put("reduceOnly", true); // 仅减仓
				System.out.println("【卖出指定posId】posId=" + posId + " 避免FIFO卖出其他仓位");
			}
			requestData.put("ordType", ordType);

			String trans = this.okxService.trade("/api/v5/trade/order", "POST", requestData.toString());
			if (trans == null) {
				try {
					FileUtil.appendUtf8String(
							dt + "【止盈，止损，平仓，对冲-做多】异常：" + "  level:" + cw.getLevel() + "  " + requestData + "\n----\n",
							"d:\\okxError.txt");
				} catch (Exception e) {
					e.printStackTrace();
					log.info("写入日志异常");
				}
//				throw new XmallException("OKX服务异常:交易失败：" + requestData);
			}
			System.out.println("requestData:" + requestData);
			System.out.println("res:" + trans);

//			JSONObject okxtransJsonObject = new JSONObject(trans);
//			if (okxtransJsonObject.getStr("code").equals("0")) {
			BigDecimal dpBigDecimal = cw.getCurrentprice();

			BigDecimal price = dpBigDecimal;
			BigDecimal realbuypriceCw = cw.getBuypriceReal();
			BigDecimal chajuCw = price.subtract(realbuypriceCw);

			BigDecimal dxzzlCw = chajuCw.divide(realbuypriceCw, 4, RoundingMode.DOWN).multiply(new BigDecimal("100"));

			String keyString = "?clOrdId=" + clOrdId + "&instId=" + instId;
//				try {
			String transOrder = this.okxService.trade("/api/v5/trade/order" + keyString, "GET", "");
			int k = 0;
			while ((transOrder == null
					|| (transOrder != null && !JSONUtil.parseObj(transOrder).getStr("code").contentEquals("51603")
							&& !JSONUtil.parseObj(transOrder).getStr("code").contentEquals("0")))) {
				System.out.println(keyString);
				System.out.println(transOrder);
				try {
					FileUtil.appendUtf8String(dt + "【止盈，止损，平仓，对冲-做多】后获取订单异常：" + "  level:" + cw.getLevel() + "  "
							+ keyString + transOrder + "\n----\n", "d:\\orderInfoError.txt");
				} catch (Exception e) {
					e.printStackTrace();
					log.info("写入日志异常");
				}
				k++;
				transOrder = this.okxService.trade("/api/v5/trade/order" + keyString, "GET", "");
			}
			if (transOrder == null
					|| (transOrder != null && !JSONUtil.parseObj(transOrder).getStr("code").contentEquals("51603")
							&& !JSONUtil.parseObj(transOrder).getStr("code").contentEquals("0"))) {
				try {
					FileUtil.appendUtf8String(dt + "【止盈，止损，平仓，对冲-做多】后获取订单异常：" + "  level:" + cw.getLevel() + "  "
							+ keyString + transOrder + "\n----\n", "d:\\orderInfoError.txt");
				} catch (Exception e) {
					e.printStackTrace();
					log.info("写入日志异常");
				}
				throw new XmallException("【止盈，止损，平仓，对冲-做多】后获取订单异常");
			} else {
				JSONObject orderInfo = JSONUtil.parseObj(transOrder);
				if (orderInfo.getStr("code").equals("0")) {
					JSONArray ol = orderInfo.getJSONArray("data");
					dpBigDecimal = ol.getJSONObject(Integer.valueOf(0)).getBigDecimal("avgPx");

					price = dpBigDecimal;
					chajuCw = price.subtract(realbuypriceCw);
					dxzzlCw = chajuCw.divide(realbuypriceCw, 4, RoundingMode.DOWN).multiply(new BigDecimal("100"));
					try {
						FileUtil.appendUtf8String(
								dt + "【止盈，止损，平仓，对冲-做多】：" + cw.getComment() + " 真实收益：" + dxzzlCw + " level:"
										+ cw.getLevel() + " " + orderInfo + "\n" + keyString + "\n----\n",
								"d:\\orderInfo.txt");
					} catch (Exception e) {
						e.printStackTrace();
						log.info("写入日志异常");
					}
				} else {
					try {
						FileUtil.appendUtf8String(dt + "【止盈，止损，平仓，对冲-做多】后获取订单异常：" + " level:" + cw.getLevel() + " "
								+ orderInfo + "\n" + keyString + "\n----\n", "d:\\orderInfoError.txt");
					} catch (Exception e) {
						e.printStackTrace();
						log.info("写入日志异常");
					}
					throw new XmallException("return【止盈，止损，平仓，对冲-做多】后获取订单异常");
				}
			}

//				} catch (Exception e2) {
//					try {
//						FileUtil.appendUtf8String(dt + "【止盈，止损，平仓，对冲-做多】后获取订单异常：" + " level:" + cw.getLevel() + " "
//								+ keyString + "\n" + e2.getMessage() + "\n----\n", "d:\\netError.txt");
//					} catch (Exception e3) {
//						e3.printStackTrace();
//						log.info("写入日志异常");
//					}
//				}
			if (cw.getComment().indexOf("对冲") != -1) {
				for (Fund1Gaoduanzhuangbei2Ok item : duichongList) {
					BigDecimal realbuyprice = cw.getBuypriceReal();
					BigDecimal chaju = price.subtract(realbuyprice);
					BigDecimal dxzzl = chaju.divide(realbuyprice, 4, RoundingMode.DOWN).multiply(new BigDecimal("100"));

					Fund1Gaoduanzhuangbei2Ok recordCw = new Fund1Gaoduanzhuangbei2Ok();
					recordCw.setBuypriceReal(item.getBuyprice());
					Fund1Gaoduanzhuangbei2OkExample exampleCw = new Fund1Gaoduanzhuangbei2OkExample();
					exampleCw.createCriteria().andIdEqualTo(item.getId());
//          if (cw.getLevel().intValue() == item.getLevel().intValue()) {
					recordCw.setComment("对冲:" + dxzzl.toString() + "ok");
//          }
					exampleCw.setTableName(tableName);
					this.fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelectiveResetZhiying(recordCw, exampleCw);
				}
			} else {

				Fund1Gaoduanzhuangbei2Ok recordCw = new Fund1Gaoduanzhuangbei2Ok();
				recordCw.setBuypriceReal(cw.getBuyprice());
				Fund1Gaoduanzhuangbei2OkExample exampleCw = new Fund1Gaoduanzhuangbei2OkExample();
				exampleCw.createCriteria().andIdEqualTo(cw.getId());
				if (cw.getComment().indexOf("止盈") != -1) {
					recordCw.setComment("止盈:" + dxzzlCw.toString() + "ok");
				}
				if (cw.getComment().indexOf("止损") != -1) {
					recordCw.setComment("止损:" + dxzzlCw.toString() + "ok");
				}
				if (cw.getComment().indexOf("平仓") != -1) {
					recordCw.setName(cw.getName().replace("pc", ""));
					recordCw.setComment("平仓:" + dxzzlCw.toString() + "ok");
				}
				exampleCw.setTableName(tableName);
				this.fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelectiveResetZhiying(recordCw, exampleCw);

			}

			if (cw.getComment().indexOf("平仓") != -1) {
				try {
					FileUtil.appendUtf8String(dt + "【成功平仓-做多】：tab:" + tableName + " 【" + dxzzlCw + "】   level:"
							+ cw.getLevel() + " " + "\n" + "\n----\n", "d:\\对冲日志.txt");
				} catch (Exception e3) {
					e3.printStackTrace();
					log.info("写入日志异常");
				}
			}
			if (cw.getComment().indexOf("止损") != -1) {
				try {
					FileUtil.appendUtf8String(dt + "【成功止损-做多】：tab:" + tableName + " 【" + dxzzlCw + "】   level:"
							+ cw.getLevel() + " " + "\n" + "\n----\n", "d:\\止损日志.txt");
				} catch (Exception e3) {
					e3.printStackTrace();
					log.info("写入日志异常");
				}
			}
			if (cw.getComment().indexOf("止盈") != -1) {
				try {
					FileUtil.appendUtf8String(dt + "【成功止盈-做多】：tab:" + tableName + " 【" + dxzzlCw + "】   level:"
							+ cw.getLevel() + " " + "\n" + "\n----\n", "d:\\止盈日志.txt");
				} catch (Exception e3) {
					e3.printStackTrace();
					log.info("写入日志异常");
				}
			}
			if (cw.getComment().indexOf("对冲") != -1) {
				try {
					FileUtil.appendUtf8String(dt + "【成功对冲-做多】：tab:" + tableName + " 【" + dxzzlCw + "】   level:"
							+ cw.getLevel() + "  数目：" + sz.toString() + "\n" + "\n----\n", "d:\\对冲日志.txt");
				} catch (Exception e3) {
					e3.printStackTrace();
					log.info("写入日志异常");
				}
			}
			BigDecimal realBuyPriceHistoryCwBigDecimal = cw.getBuypriceReal();

			String keyString2 = clOrdId + "_sell=" + realBuyPriceHistoryCwBigDecimal.toString() + "\n";
			try {
				FileUtil.appendUtf8String(keyString2, "D:\\project\\python\\btc\\config.ini");
			} catch (Exception e) {
				e.printStackTrace();
				log.info("写入日志异常");
			}
//			} else {
//				try {
//					FileUtil.appendUtf8String(dt + "【止盈，止损，平仓，对冲-做多】okx接口无法卖出(已回滚）：" + cw.getComment() + " level:"
//							+ cw.getLevel() + " " + requestData + "\n" + trans + "\n----\n", "d:\\okxError.txt");
//				} catch (Exception e3) {
//					e3.printStackTrace();
//					log.info("写入日志异常");
//				}
//				throw new XmallException("OKX服务异常:" + okxtransJsonObject.getStr("msg"));
//			}
		}
	}

	@Override
	public List<Fund1Gaoduanzhuangbei2Ok> getCangweisByTableName(String tableName) {
		Fund1Gaoduanzhuangbei2OkExample example = new Fund1Gaoduanzhuangbei2OkExample();
		// 【修复】只查询有实际持仓的仓位（fene > 0），排除已清仓的仓位（fene = 0 或 null）
		example.createCriteria().andFeneIsNotNull().andFeneGreaterThan(new BigDecimal("0"));
		example.setTableName(tableName);
		example.setOrderByClause("id asc");
		List<Fund1Gaoduanzhuangbei2Ok> results = this.fund1Gaoduanzhuangbei2OkMapper.selectByExampleDynamic(example);

		return results;
	}

	@Override
	public Fund1Gaoduanzhuangbei2Ok getInfoByTableNameAndDt(String tableName, long dt, int level) {
		Fund1Gaoduanzhuangbei2OkExample example = new Fund1Gaoduanzhuangbei2OkExample();
		example.createCriteria().andDtLessThan(new Date(dt)).andLevelNotEqualTo(Integer.valueOf(level));
		example.setTableName(tableName);
		example.setOrderByClause("dt desc");
		PageHelper.startPage(1, 1);
		List<Fund1Gaoduanzhuangbei2Ok> results = this.fund1Gaoduanzhuangbei2OkMapper.selectByExampleDynamic(example);
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	@Override
	public void updateFund(Fund fund) {
		this.fundMapper.updateByPrimaryKeySelective(fund);
	}

	@Override
	public void updateIndex(String tableName, int tag, BigDecimal zzl, String comment) {
		FundExample example = new FundExample();
		example.createCriteria().andPlantableEqualTo(tableName);
		Fund record = new Fund();
		record.setCansell(Integer.valueOf(tag));
		record.setZhangdiefu(zzl);
		record.setCreated(new Date());
		record.setComments(comment);
		this.fundMapper.updateByExampleSelective(record, example);
	}

	@Override
	public List<Fund> getIndex() {
		FundExample example = new FundExample();
		example.createCriteria().andBodongshuIsNotNull().andPrepareEqualTo(Integer.valueOf(0));

		List<Fund> funds = this.fundMapper.selectByExample(example);
		return funds;
	}

	@Override
	public List<Fund> getIndexBtc() {
		FundExample example = new FundExample();
		example.createCriteria().andBodongshuIsNotNull().andPrepareEqualTo(Integer.valueOf(3)).andCateEqualTo("0");

		List<Fund> funds = this.fundMapper.selectByExample(example);
		return funds;
	}
}
