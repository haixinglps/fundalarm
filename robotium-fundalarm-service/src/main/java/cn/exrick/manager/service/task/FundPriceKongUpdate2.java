package cn.exrick.manager.service.task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.exrick.common.jedis.JedisClient;
import cn.exrick.manager.pojo.Fund;
import cn.exrick.manager.pojo.Fund1Gaoduanzhuangbei2Ok;
import cn.exrick.manager.service.FundKongService;
import cn.exrick.manager.service.OkxService;
import cn.exrick.manager.service.util.AlarmPlayer;
import cn.exrick.manager.service.util.okx2;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import redis.clients.jedis.Tuple;;

//@Component

public class FundPriceKongUpdate2 {

//	headers.set("OK-ACCESS-KEY", okxConfig.getApikey());
//    headers.set("OK-ACCESS-SIGN", sign.getSign());
//    headers.set("OK-ACCESS-TIMESTAMP", sign.getTimestamp());
//    headers.set("OK-ACCESS-PASSPHRASE", okxConfig.getPassphrase());

	@Reference
//	@Qualifier("fundServiceKongImpl")
	private FundKongService caiService;
//	AlarmPlayer alarmPlayer = new AlarmPlayer();

	@Autowired
	AlarmPlayer alarmPlayer;

	@Reference
	OkxService okxService;

	@Autowired
	JedisClient jedisClient;
	// key命名规范 项目名称
	private static final String PRIVATE_HASH_KEY_ARTICLES = "postc_set"; // 评论编号
	private static final String PRIVATE_HASH_KEY_ARTICLE_USERS = "postc_user_like_set"; // 评论踩人集合
//	private static final String PRIVATE_HASH_KEY_ARTICLE_USER_INFO = "post_user_like"; // 评论踩人踩详情
//	private static final String PRIVATE_HASH_KEY_POST_COUNTER = "post_counter"; // 缓存里的评论踩数

	@Scheduled(cron = "*/1 * * * * ?")
	public void run() {
		// 定义所有基金表名称。70个，目前6个。
		List<String> tableNames = new ArrayList<String>();
		tableNames.add("fund_1_gaoduanzhuangbei_2_ok");
		tableNames.add("fund_1_huangjin_5_ok");
		tableNames.add("fund_1_jungong_3_ok");
		tableNames.add("fund_1_meitan_4_ok");
		tableNames.add("fund_1_zhengquan_6_ok");
		tableNames.add("fund_1_fangdichan_1_ok");
		List<Fund> funds = caiService.getIndexBtc();
		for (Fund fund : funds) {

			// 根据表名获取基金编码和价格范围minP,maxP,code
			BigDecimal minP = new BigDecimal("0");
			BigDecimal maxP = new BigDecimal("0");
//		for (String tableName : tableNames) {
			String tableName = fund.getPlantable();
			System.out.println("开始处理：" + tableName);

			String urla = "https://www.glpomelo.cn/data/kpi_analysis";
			Map<String, String> data = new HashMap<String, String>();
//			startDate: 2023-10-22
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String dtString = sdfDateFormat.format(calendar.getTime());
			calendar.add(Calendar.MONTH, -1);
			String dtStringStart = sdfDateFormat.format(calendar.getTime());
			data.put("endDate", dtString);
			data.put("startDate", dtStringStart);
			data.put("code", fund.getCode());

			Map<String, String> datayear = new HashMap<String, String>();
//			startDate: 2023-10-22

			datayear.put("period", "oneYear");

			datayear.put("code", fund.getCode());

			boolean fundana = jedisClient.exists("fundana_" + fund.getCode());
			Fund newFund = new Fund();
			newFund.setId(fund.getId());
			if (!fundana && fund.getName().indexOf("[") == -1 && fund.getName().indexOf("【") == -1) {
				String resultStringa = cn.exrick.common.utils.HttpUtil.sendPost(urla, data);
				System.out.println("------------------------" + fund.getName() + "------------------------");
				System.out.println(resultStringa);
				if (resultStringa == null) {
					System.out.println("网络异常，url:" + urla);
					alarmPlayer.playAlarm2(1);
					jedisClient.setex("fundana_" + fund.getCode(), 86400, "1");
//				continue;
				} else {

					JSONObject fenxidata = new JSONObject(resultStringa);
					if (!fenxidata.getStr("status").contentEquals("success")) {
						System.out.println("server 异常，url:" + urla);
						jedisClient.setex("fundana_" + fund.getCode(), 86400, "1");
					} else {
//				kpi_fallAvgEveryDay
//				kpi_fallPer1Rate
//				kpi_fallPer2Rate
//				kpi_fallPer3Rate
//				kpi_incAvgEveryDay
//				kpi_incPer1Rate
//				kpi_incPer2Rate
//				kpi_incPer3Rate

						String resultStringayear = cn.exrick.common.utils.HttpUtil.sendPost(urla, datayear);

						if (resultStringayear != null) {
							JSONObject fenxidatayear = new JSONObject(resultStringayear);
							if (fenxidatayear.getStr("status").contentEquals("success")) {

								newFund.setZhenfurealyear(fenxidatayear.getBigDecimal("kpi_incAvgEveryDay"));
								newFund.setDiefurealyear(fenxidatayear.getBigDecimal("kpi_fallAvgEveryDay"));

								newFund.setBodonglv3year(
										fenxidatayear.getBigDecimal("kpi_incPer3Rate").multiply(new BigDecimal("0.2")));
								newFund.setBodonglv2year(
										fenxidatayear.getBigDecimal("kpi_incPer2Rate").multiply(new BigDecimal("0.2")));
								newFund.setBodonglvyear(
										fenxidatayear.getBigDecimal("kpi_incPer1Rate").multiply(new BigDecimal("0.2")));

								if (fenxidatayear.containsKey("price")) {

									JSONArray priceArr = fenxidatayear.getJSONArray("price");
									JSONArray netArr = fenxidatayear.getJSONArray("net_value");
									if (priceArr.size() > 0) {

										int size = priceArr.size();
										Object firstData = priceArr.get(0);
										Object endData = priceArr.get(size - 1);
//										BigDecimal shouyilv = (new BigDecimal(endData.toString())
//												.subtract(new BigDecimal(firstData.toString()))).divide(
//														new BigDecimal(firstData.toString()), 4, RoundingMode.DOWN);
//										
										BigDecimal netFirstBigDecimal = new BigDecimal(netArr.get(0).toString());
										BigDecimal netEndBigDecimal = new BigDecimal(
												netArr.get(netArr.size() - 1).toString());
										JSONArray dateLine = fenxidatayear.getJSONArray("date_Line");
										JSONArray profitAry = fenxidatayear.get("profit_ary") != null
												&& !fenxidatayear.get("profit_ary").toString().contentEquals("null")
														? fenxidatayear.getJSONArray("profit_ary")
														: null;
										BigDecimal tmpRate = getCurGainRate(new BigDecimal(firstData.toString()),
												new BigDecimal(endData.toString()), netFirstBigDecimal,
												netEndBigDecimal, dateLine.get(0).toString(),
												dateLine.get(dateLine.size() - 1).toString(), profitAry);
										newFund.setShouyilvyear(tmpRate);
									}

								}

							}

						}

						newFund.setZhenfureal(fenxidata.getBigDecimal("kpi_incAvgEveryDay"));
						newFund.setDiefureal(fenxidata.getBigDecimal("kpi_fallAvgEveryDay"));

//						fund.setBodonglv3(fenxidata.getBigDecimal("kpi_fallPer3Rate")
//								.add(fenxidata.getBigDecimal("kpi_incPer3Rate")));
//						fund.setBodonglv2(fenxidata.getBigDecimal("kpi_fallPer2Rate")
//								.add(fenxidata.getBigDecimal("kpi_incPer2Rate")));
//						fund.setBodonglv(fenxidata.getBigDecimal("kpi_fallPer1Rate")
//								.add(fenxidata.getBigDecimal("kpi_incPer1Rate")));

						newFund.setBodonglv3(
								fenxidata.getBigDecimal("kpi_incPer3Rate").multiply(new BigDecimal("0.2")));
						newFund.setBodonglv2(
								fenxidata.getBigDecimal("kpi_incPer2Rate").multiply(new BigDecimal("0.2")));
						newFund.setBodonglv(fenxidata.getBigDecimal("kpi_incPer1Rate").multiply(new BigDecimal("0.2")));

						if (fenxidata.containsKey("price")) {

							JSONArray priceArr = fenxidata.getJSONArray("price");
							JSONArray netArr = fenxidata.getJSONArray("net_value");

							if (priceArr.size() > 0) {

								int size = priceArr.size();
								Object firstData = priceArr.get(0);
								Object endData = priceArr.get(size - 1);
//								BigDecimal shouyilv = (new BigDecimal(endData.toString())
//										.subtract(new BigDecimal(firstData.toString())))
//												.divide(new BigDecimal(firstData.toString()), 4, RoundingMode.DOWN);
//
//								fund.setShouyilv(shouyilv);
								BigDecimal netFirstBigDecimal = new BigDecimal(netArr.get(0).toString());
								BigDecimal netEndBigDecimal = new BigDecimal(netArr.get(netArr.size() - 1).toString());
								JSONArray dateLine = fenxidata.getJSONArray("date_Line");
								JSONArray profitAry = fenxidata.get("profit_ary") != null
										&& !fenxidata.get("profit_ary").toString().contentEquals("null")
												? fenxidata.getJSONArray("profit_ary")
												: null;
								BigDecimal tmpRate = getCurGainRate(new BigDecimal(firstData.toString()),
										new BigDecimal(endData.toString()), netFirstBigDecimal, netEndBigDecimal,
										dateLine.get(0).toString(), dateLine.get(dateLine.size() - 1).toString(),
										profitAry);
								newFund.setShouyilv(tmpRate);
							}

						}

						caiService.updateFund(newFund);
						jedisClient.setex("fundana_" + fund.getCode(), 86400, "1");
					}
				}
			}

			caiService.updateIndex(tableName, 0, new BigDecimal("0"), "");
			Fund1Gaoduanzhuangbei2Ok fundInfo = caiService.getInfoByTableName(tableName);
			if (fundInfo == null) {
				System.out.println(tableName + ": 此基金已卖出，请尽快买入");
				continue;
			} else {

				minP = fundInfo.getBuyprice();
				maxP = fundInfo.getSellprice().add(fundInfo.getWanggeprice());
				;
				String codeString = fundInfo.getCode();
				// 获取交易所最新基金估值。
				// https://fundgz.1234567.com.cn/js/012212.js?rt=1694399168362
				String url = "https://fundgz.1234567.com.cn/js/" + codeString + ".js?rt=" + System.currentTimeMillis();
				String resultString = null;
				if (fund.getSinacode() == null) {
					resultString = cn.exrick.common.utils.HttpUtil.sendGet(url);
				} else if (fund.getSinacode().equals("btc")) {
					/* JVM设置代理 */
//					System.getProperties().setProperty("https.proxyHost", "localhost");
//					System.getProperties().setProperty("https.proxyPort", "7890");
					Map<String, String> sinaheader = new HashMap<String, String>();
//					startDate: 2023-10-22
					url = "https://price.btcfans.com/zh-cn/coin/" + fund.getCode();
					sinaheader.put("Referer", "https://price.btcfans.com/");
					sinaheader.put("Host", "price.btcfans.com");
					String gupiaoRes = cn.exrick.common.utils.HttpUtil.sendGetWithHeader(url, sinaheader);
					if (gupiaoRes == null) {
						System.out.println("=========网络异常，url:" + url);
						alarmPlayer.playAlarm2(1);
						continue;
					}

					JSONObject sinaResJsonObject = new JSONObject();
					try {
//						USDT</span></div><div class="sub">欧易OKEx</div></div></div></div><div class="table-col col2" data-last="2.0695861814814"> 2.069 </div>
//						int sindex = gupiaoRes.indexOf("data-none-unit=\"1\">");
//						String newcontentString = gupiaoRes.substring(sindex);
//						int endindex = newcontentString.indexOf("</span>");
//						String currentValue = newcontentString.substring(19, endindex).replace(",", "");
						String keyString = "USDT</span></div><div class=\"sub\">欧易OKEx</div></div></div></div><div class=\"table-col col2\" data-last=";
//						int len = keyString.length();

						int sindex = gupiaoRes.indexOf(
								"USDT</span></div><div class=\"sub\">欧易OKEx</div></div></div></div><div class=\"table-col col2\" data-last=");
						String newcontentString = gupiaoRes.substring(sindex);
						int begindex = newcontentString.indexOf("\"> ");
						int endindex = newcontentString.indexOf(" </div>");
						String currentValue = newcontentString.substring(begindex + 3, endindex).replace(",", "");

//						String yestordayValue = gupiaoRes.split(",")[2];
//						String currentValue = gupiaoRes.split(",")[3];
//						if (fund.getSinacode().indexOf("fu") != -1) {
//							yestordayValue = gupiaoRes.split(",")[3];
//							currentValue = gupiaoRes.split(",")[2];
//						}
//						BigDecimal zzl = (new BigDecimal(currentValue).subtract(new BigDecimal(yestordayValue)))
//								.divide(new BigDecimal(yestordayValue), 4, RoundingMode.DOWN);
						sinaResJsonObject.put("gsz", new BigDecimal(currentValue).toString());
						sinaResJsonObject.put("gszzl", "0");
						resultString = sinaResJsonObject.toString();
					} catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					}

				} else if (fund.getSinacode().equals("okx")) {

					Map<String, String> sinaheader = new HashMap<String, String>();

					url = "https://www.okx.com/api/v5/market/ticker?instId=" + fund.getCode();
//					url = "/api/v5/market/ticker?instId=" + fund.getCode();
//					String gupiaoRes = okxService.trade(url, "GET", "");

					String gupiaoRes = cn.exrick.common.utils.HttpUtil.sendGetWithHeader(url, sinaheader);
					System.out.println("欧易服务器响应：");
					System.out.println(gupiaoRes);

					if (gupiaoRes == null || gupiaoRes.indexOf("Just a moment") != -1) {
						// 从redis提取行情：
//						String hangqing = jedisClient.get(fund.getCode() + "_tickers");
//						System.out.println("redis服务器响应：");
//						System.out.println(hangqing);
//						if (hangqing != null) {
//							gupiaoRes = hangqing;
//							JSONObject hangqingJson = new JSONObject(gupiaoRes);
//							Long dt = hangqingJson.getJSONArray("data").getJSONObject(0).getLong("ts");
//							long cdtLong = System.currentTimeMillis();
//							long chajuLog = cdtLong - dt.longValue();
//							long sec = chajuLog / 1000;
//							if (sec < 60) {
//								System.out.println("websocket正常运行中: ws延迟:" + sec + "s " + hangqing);
//							} else {
//								System.out.println("websocket延迟过大，ws延迟:" + sec + "s");
//								gupiaoRes = "";
//							}
//						}
					}

					if (gupiaoRes == null) {
						System.out.println("=========网络异常，url:" + url);
						alarmPlayer.playAlarm2(1);
						continue;
					}

					try {
						JSONObject sinaResJsonObject = new JSONObject(gupiaoRes);
						JSONObject okxJson = new JSONObject(gupiaoRes);
						JSONArray ja = okxJson.getJSONArray("data");
						BigDecimal currentValue = ja.getJSONObject(0).getBigDecimal("bidPx");
						BigDecimal volBigDecimal = ja.getJSONObject(0).getBigDecimal("lastSz");
						sinaResJsonObject.put("gsz", currentValue);
						sinaResJsonObject.put("gszzl", "0");
						sinaResJsonObject.put("vol", volBigDecimal);
						resultString = sinaResJsonObject.toString();
					} catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					}

				}

				else {
					Map<String, String> sinaheader = new HashMap<String, String>();
//					startDate: 2023-10-22
					url = "https://hq.sinajs.cn/list=" + fund.getSinacode();
					sinaheader.put("Referer", "https://finance.sina.com.cn/");
					sinaheader.put("Host", "hq.sinajs.cn");
					String gupiaoRes = cn.exrick.common.utils.HttpUtil.sendGetWithHeader(url, sinaheader);
					if (gupiaoRes == null) {
						System.out.println("网络异常，url:" + url);
						alarmPlayer.playAlarm2(1);
						continue;
					}

					JSONObject sinaResJsonObject = new JSONObject();
					try {
						String yestordayValue = gupiaoRes.split(",")[2];
						String currentValue = gupiaoRes.split(",")[3];
						if (fund.getSinacode().indexOf("fu") != -1) {
							yestordayValue = gupiaoRes.split(",")[3];
							currentValue = gupiaoRes.split(",")[2];
						}
						BigDecimal zzl = (new BigDecimal(currentValue).subtract(new BigDecimal(yestordayValue)))
								.divide(new BigDecimal(yestordayValue), 4, RoundingMode.DOWN);
						sinaResJsonObject.put("gsz", new BigDecimal(currentValue).toString());
						sinaResJsonObject.put("gszzl", zzl.multiply(new BigDecimal("100")).toString());
						resultString = sinaResJsonObject.toString();
					} catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					}

				}
				if (resultString == null) {
					System.out.println("网络异常，url:" + url);
					alarmPlayer.playAlarm2(1);
					continue;
				} else {
					System.out.println("url:" + url);
					System.out.println(resultString);
					try {
						JSONObject jsonObject = new JSONObject(resultString.replace("jsonpgz(", "").replace(");", ""));
						BigDecimal jingzhi = jsonObject.getBigDecimal("gsz");
						BigDecimal vol = new BigDecimal("0");
						if (fund.getSinacode().indexOf("okx") != -1) {
							vol = jsonObject.getBigDecimal("vol");
						}

//						String lastvalue = jedisClient.get("lastvalue_" + fund.getCode());
//						if (lastvalue != null) {
//							BigDecimal lBigDecimal = new BigDecimal(lastvalue);
//							if (jingzhi.compareTo(lBigDecimal) > 0) {
//								{
//									newFund.setQushi("↑");
//									caiService.updateFund(newFund);
//								}
//							} else if (jingzhi.compareTo(lBigDecimal) < 0) {
//								newFund.setQushi("↓");
//								caiService.updateFund(newFund);
//							}
//
//						}
//						jedisClient.setex("lastvalue_" + fund.getCode(), 86400, jingzhi.toString());

						BigDecimal maxValue = new BigDecimal("0");
//						String linenameString=tableName+fundInfo.getLevel();

						if (!jedisClient.exists("maxvalue_" + tableName)) {
							jedisClient.set("maxvalue_" + tableName, jingzhi.toString());
							maxValue = new BigDecimal(jingzhi.toString());
						} else {
							maxValue = new BigDecimal(jedisClient.get("maxvalue_" + tableName));
							if (jingzhi.compareTo(maxValue) > 0) {
								jedisClient.set("maxvalue_" + tableName, jingzhi.toString());
								maxValue = new BigDecimal(jedisClient.get("maxvalue_" + tableName));
							}
						}
						if (!jedisClient.exists("price_" + fund.getCode())) {
							System.out.println("不存在key:" + "price_" + fund.getCode());

							jedisClient.zadd("price_" + fund.getCode(), jingzhi.doubleValue(),
									(System.currentTimeMillis() + 60000) + "");
							jedisClient.expire("price_" + fund.getCode(), 86400);

						} else {
							System.out.println("存在key:" + "price_" + fund.getCode());
							jedisClient.zadd("price_" + fund.getCode(), jingzhi.doubleValue(),
									(System.currentTimeMillis() + 60000) + "");
							long ttl = jedisClient.ttl("price_" + fund.getCode());

							System.out.println("key ttl:   " + ttl);
//							if (fund.getCode().equals("DOGE-USDT")) {
//								Thread.sleep(60000);
//							}

							if (ttl == -1) {
								jedisClient.del("price_" + fund.getCode());
								continue;
							}
						}

						if (!jedisClient.exists("vol_" + fund.getCode())) {
							System.out.println("不存在key:" + "vol_" + fund.getCode());

							jedisClient.zadd("vol_" + fund.getCode(), vol.doubleValue(),
									(System.currentTimeMillis() + 60000) + "");
							jedisClient.expire("vol_" + fund.getCode(), 86400);

						} else {
							System.out.println("存在key:" + "vol_" + fund.getCode());
							jedisClient.zadd("vol_" + fund.getCode(), vol.doubleValue(),
									(System.currentTimeMillis() + 60000) + "");
							long ttl = jedisClient.ttl("vol_" + fund.getCode());

							System.out.println("key ttl:   " + ttl);
//							if (fund.getCode().equals("DOGE-USDT")) {
//								Thread.sleep(60000);
//							}

							if (ttl == -1) {
								jedisClient.del("vol_" + fund.getCode());
								continue;
							}
						}

						// 清理zset中的过期元素

						Set<String> zsetKeys = jedisClient.zrange("price_" + fund.getCode());
						for (String mem : zsetKeys) {
							Long expLong = Long.parseLong(mem);
							if (expLong.longValue() < System.currentTimeMillis()) {
								jedisClient.zrem("price_" + fund.getCode(), mem);
							}
						}
						// 清理zset中的过期元素

						Set<String> zsetKeysVol = jedisClient.zrange("vol_" + fund.getCode());
						for (String mem : zsetKeysVol) {
							Long expLong = Long.parseLong(mem);
							if (expLong.longValue() < System.currentTimeMillis()) {
								jedisClient.zrem("vol_" + fund.getCode(), mem);
							}
						}

						Long ybcount = jedisClient.zcard("price_" + fund.getCode());

						// 最大分数
						Set<String> maxset = jedisClient.zrangeMinMax("price_" + fund.getCode(), 1);
						List<String> list = new ArrayList<>(maxset);
						String maxScoreMember = list.get(0);

						Double maxScore = jedisClient.zscore("price_" + fund.getCode(), maxScoreMember);

						// 最小分数
						Set<String> minset = jedisClient.zrangeMinMax("price_" + fund.getCode(), 0);
						List<String> listMin = new ArrayList<>(minset);
						String minScoreMember = listMin.get(0);

						Double minScore = jedisClient.zscore("price_" + fund.getCode(), minScoreMember);

//						

						int alarmtag = 0;

						System.out.println("成功更新基金价格：" + fundInfo.getName());

						// lastvalue
						int buyTag = 0;
//						jedisClient.lpush("okxzzl", lastvalue);
//						 
//						List<String> zzlList = jedisClient.lrange("okxzzl", 0, 4);

//						if(zzlList.size()==5)
//						{
//							
//							
						// 计算成交量和涨跌幅

						Set<String> zsetKeysVolFinal = jedisClient.zrange("vol_" + fund.getCode());
						BigDecimal sumVol = new BigDecimal("0");
						for (String mem : zsetKeysVolFinal) {

							Double volInfoString = jedisClient.zscore("vol_" + fund.getCode(), mem);

							BigDecimal volBigDecimal = new BigDecimal(volInfoString);
							sumVol = sumVol.add(volBigDecimal);

						}
						fundInfo.setSuccesscount(sumVol.intValue());

						Set<Tuple> zsetKeysPriceFinal = jedisClient.zrangeWithScore("price_" + fund.getCode(), 0, -1);
//						List<Double> priceList = new ArrayList<Double>();
//						int indexP = 0;
						DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						int changeCount = 0;
						int bigZhang = 0;
						Date nowDate = new Date();
						int fanxiang = 0;
						int fanxiangBigZhang = 0;

						Comparator<Tuple> comparator = new Comparator<Tuple>() {

							@Override
							public int compare(Tuple o1, Tuple o2) {
								// TODO Auto-generated method stub
								return o1.getElement().compareTo(o2.getElement());
							}
						};
						List<Tuple> sortedList = new ArrayList<>(zsetKeysPriceFinal);
						Collections.sort(sortedList, comparator);

						for (int i = 0; i < sortedList.size(); i++) {
//							Long expLong = Long.parseLong(mem);
//							if (expLong.longValue() < System.currentTimeMillis()) {
//								jedisClient.zrem("price_" + fund.getCode(), mem);
//							}
							Tuple mem = sortedList.get(i);
							double priceInfoString = mem.getScore();
							BigDecimal jizhun = new BigDecimal("-0.1");
							BigDecimal jizhunFanxiang = new BigDecimal("0.1");

							if (i >= 1) {
								Double previous = sortedList.get(i - 1).getScore();
								double priceChange = (priceInfoString - previous) / previous; // 计算价格变化百分比
								BigDecimal pricechaBigDecimal = new BigDecimal(priceChange)
										.multiply(new BigDecimal("100"));
								BigDecimal fufeilv = new BigDecimal("0").subtract(fundInfo.getSellpriceReal().abs());

								if (pricechaBigDecimal.compareTo(fufeilv) < 0) {
//									try {
//										FileUtil.appendUtf8String(dFormat.format(dtDate) + ":【突发上涨】  价格变化率:"
//												+ priceChange + " table:" + tableName + " level:" + fundInfo.getLevel()
//												+ "前值:" + previous + "  探测价:" + priceInfoString + " 买入价："
//												+ fundInfo.getBuypriceReal() + "\n----\n", "d:\\突发上涨日志.txt");
//									} catch (Exception e) {
//										e.printStackTrace();
//
//									}

									changeCount += 1;
								}

								if (pricechaBigDecimal.compareTo(jizhun) < 0) {
									bigZhang += 1;
								}
								if (pricechaBigDecimal.compareTo(fufeilv) > 0) {
									fanxiang += 1;
								}
								if (pricechaBigDecimal.compareTo(jizhunFanxiang) > 0) {
									fanxiangBigZhang += 1;
								}
							}

//							indexP += 1;

						}

						BigDecimal tbl = new BigDecimal(changeCount).divide(new BigDecimal(ybcount), 3,
								RoundingMode.DOWN);
						BigDecimal dbl = new BigDecimal(bigZhang).divide(new BigDecimal(ybcount), 3, RoundingMode.DOWN);
						BigDecimal fxl = new BigDecimal(fanxiang).divide(new BigDecimal(ybcount), 3, RoundingMode.DOWN);
						BigDecimal fxdbl = new BigDecimal(fanxiangBigZhang).divide(new BigDecimal(ybcount), 3,
								RoundingMode.DOWN);

//						String kline = "https://www.okx.com/api/v5/market/candles?instId=" + fund.getCode() + "&bar=1m";
//
//						List<Candle> klineInfo = okx.getline(kline);
//						int sartag = 0;
//						if (klineInfo.size() > 0) {
//							double price1 = klineInfo.get(klineInfo.size() - 4).getSar();
//							double price2 = klineInfo.get(klineInfo.size() - 3).getSar();
//							double price3 = klineInfo.get(klineInfo.size() - 2).getSar();
//							double price4 = klineInfo.get(klineInfo.size() - 1).getSar();
//							if (price4 > price3 && price3 > price2 && price2 > price1)
//								sartag = 1;
//							if (price4 < price3 && price3 < price2 && price2 < price1)
//								sartag = -1;
//
//						}
//						System.out.println("短线方向：" + sartag);
//
						String klineLong = "https://www.okx.com/api/v5/market/candles?instId=" + fund.getCode()
								+ "&bar=" + fund.getBodongshu() + "m";
						klineLong = klineLong.replace("60m", "1H");
						klineLong = klineLong.replace("240m", "4H");
						klineLong = klineLong.replace("1440m", "1d");

//
//						List<Candle> klineInfoLong = okx.getline(klineLong);
//						int sartagLong = 0;
//						if (klineInfoLong.size() > 0) {
//							double price1 = klineInfoLong.get(klineInfoLong.size() - 4).getSar();
//							double price2 = klineInfoLong.get(klineInfoLong.size() - 3).getSar();
//							double price3 = klineInfoLong.get(klineInfoLong.size() - 2).getSar();
//							double price4 = klineInfoLong.get(klineInfoLong.size() - 1).getSar();
//							if (price4 > price3 && price3 > price2 && price2 > price1)
//								sartagLong = 1;
//							if (price4 < price3 && price3 < price2 && price2 < price1)
//								sartagLong = -1;
//
//						}
//						System.out.println("长线方向：" + sartagLong);
						// && sartagLong == -1 && sartagLong == -1 && sartag == -1 && sartag == -1
						Map<String, Integer> buyTagMap = null;
						try {
							buyTagMap = okx2.getBuyTag(fund.getCode(), klineLong, "0.15", fundInfo.getZhisun() / 2,
									"0.15", fundInfo.getZhisun() / 2, null);

							if (buyTagMap == null) {
//								Set<String> candleKeys = jedisClient.zrange(fund.getCode() + "_candle1m");
//								JSONArray jArray = new JSONArray();
//								for (String candleKey : candleKeys) {
//									if (jedisClient.exists(candleKey)) {
//										JSONObject obj = new JSONObject(jedisClient.get(candleKey));
//										jArray.add(obj);
//									}
//								}
//								buyTagMap = okx2.getBuyTag(fund.getCode(), klineLong, "0.15", fundInfo.getZhisun() / 2,
//										"0.15", fundInfo.getZhisun() / 2, jArray);
//								System.out.println(buyTagMap);
								System.out.println("======ip被限制========");

								alarmPlayer.playAlarm2(1);
//								Thread.sleep(1800000);
								continue;

							}

						} catch (Exception e) {

							System.out.println("lpslog:");
							e.printStackTrace();
							alarmPlayer.playAlarm2(1);
//							Thread.sleep(1800000);
							continue;
							// TODO: handle exception
						}

						int buyTagPeriod = buyTagMap.get("buytag");
						int currentbuytag = buyTagMap.get("currentbuytag");
						int cbt = 0;
						if (currentbuytag == -1 && !jedisClient.exists("cbt_" + fund.getCode())) {
							jedisClient.setex("cbt_" + fund.getCode(), 60, currentbuytag + "");
							cbt = -1;
						}

						int tempbuyTagPeriod = buyTagMap.get("tempbuytag");
						if (tempbuyTagPeriod != 0)
							// if (buyTagPeriod != 0)
							jedisClient.set("period_" + fund.getCode(), tempbuyTagPeriod + "");

						Integer chajufangxiangInteger = buyTagMap.get("chajufangxiang");

						String buyTagPeriodStr = jedisClient.get("period_" + fund.getCode());
						if (buyTagPeriodStr == null) {
							buyTagPeriodStr = "0";
						}
						if (buyTagPeriod == -1 && chajufangxiangInteger == 1) {
							// && buyTagPeriod == -1

//							if (tbl.compareTo(new BigDecimal("0.5")) > 0) {
//								buyTag = -1;
//							} else if (tbl.compareTo(fxl) > 0 && tbl.compareTo(new BigDecimal("0.4")) >= 0
//									&& fxl.compareTo(new BigDecimal("0.1")) <= 0) {
//
//								//
//								// tbl.compareTo(new BigDecimal("0.3")) >= 0 && bigZhang >= 1 &&
//								// fanxiangBigZhang == 0
//								// &&
//								buyTag = -1;
//							}
							buyTag = -1;

						}

						if (buyTag == -1) {
//							BigDecimal changeBigDecimal = new BigDecimal(changeCount + "");
//							BigDecimal allCountBigDecimal = new BigDecimal(sortedList.size() + "");
//							BigDecimal biliBigDecimal = changeBigDecimal.divide(allCountBigDecimal, 3,
//									RoundingMode.DOWN);

							try {
								FileUtil.appendUtf8String(dFormat.format(nowDate) + ":【突发下跌ok】 记录数：" + changeCount
										+ " 总数：" + sortedList.size() + " 突变占比：" + tbl + " 反向占比: " + fxl + " 大单数: "
										+ bigZhang + " table:" + tableName + " level:" + fundInfo.getLevel() + "  现价:"
										+ jingzhi.toString() + " 买入价："

										+ fundInfo.getBuypriceReal() + " 成交量：" + sumVol

										+ "\n----\n", "d:\\突发下跌日志.txt");
							} catch (Exception e) {
								e.printStackTrace();

							}
//							if (changeCount >= 2)
//							buyTag = -1;&& sartag == 1 && sartagLong == 1)
						} else {
							if (buyTagPeriod == 1 && chajufangxiangInteger == 1) {
								// && buyTagPeriod == 1
//								if (fxl.compareTo(new BigDecimal("0.5")) > 0) {
//									buyTag = 1;
//								} else if (fxl.compareTo(tbl) > 0 && fxl.compareTo(new BigDecimal("0.4")) >= 0
//										&& tbl.compareTo(new BigDecimal("0.1")) <= 0) {
//									//
//
//									// && fanxiangBigZhang >= 1
//									// && bigZhang == 0 &&
//									buyTag = 1;
//								}
								buyTag = 1;

							}

						}
//						Map<String, Integer> buyTagMap = okx2.getBuyTag(klineLong);
//						buyTag = buyTagMap.get("buytag");
//						if (buyTag != 0)
//							jedisClient.set("period_" + fund.getCode(), buyTag + "");
//						Integer chajufangxiangInteger = buyTagMap.get("chajufangxiang");
						// 修改为连续10次符合条件设置为-1
						jedisClient.lpush("buytag_" + fund.getCode(), buyTag + "");

						// 修改为连续10次符合条件设置为-1
						jedisClient.lpush("buytag30_" + fund.getCode(), buyTag + "");
//						jedisClient.lpush("buytag600_" + fund.getCode(), buyTag + "");

						// 只保留最新推入的10个元素，删除其他元素
						jedisClient.ltrim("buytag_" + fund.getCode(), 0, 100);
						jedisClient.ltrim("buytag30_" + fund.getCode(), 0, 30);
//						jedisClient.ltrim("buytag600_" + fund.getCode(), 0, 600);

//						37/60=0.616 , 22/60=0.366 , 18/60=0.300 , 10/60=0.166 0/0/0/0/0/,1.00,1.30,1.00,0.00

						// 获取最新推入的10个元素

						int realBuyTag = 0;
//						int fxRealBuyTag = 0;
						int fanxiangOkCount = 0;

						List<String> buytagHistory = jedisClient.lrange("buytag_" + fund.getCode(), 0, -1);
						String buyTagLiString = "";
						int idx = 0;
						int okCount = 0;
						for (String item : buytagHistory) {
							if (item.equals("1")) {
//								realBuyTag = 0;
								fanxiangOkCount += 1;

							}
							if (item.equals("-1")) {
								okCount += 1;

							}
//							if (item.equals("-1"))
//								okCount += 1;
							if (idx < 5)
								buyTagLiString += item + "/";
							idx += 1;

						}

						idx = 0;
						int okCount2 = 0;
						int fanxiangOkCount2 = 0;
						List<String> buytagHistory30 = jedisClient.lrange("buytag30_" + fund.getCode(), 0, -1);
						for (String item : buytagHistory30) {
							if (item.equals("1")) {
//								realBuyTag = 0;
								fanxiangOkCount2 += 1;

							}
							if (item.equals("-1")) {
								okCount2 += 1;

							}

							idx += 1;

						}
//						List<String> buytagHistory600 = jedisClient.lrange("buytag600_" + fund.getCode(), 0, -1);

						BigDecimal bili = new BigDecimal(okCount).divide(new BigDecimal(buytagHistory.size()), 2,
								RoundingMode.DOWN);

						BigDecimal bili2 = new BigDecimal(fanxiangOkCount).divide(new BigDecimal(buytagHistory.size()),
								2, RoundingMode.DOWN);

						BigDecimal bili3 = new BigDecimal(okCount2).divide(new BigDecimal(buytagHistory30.size()), 2,
								RoundingMode.DOWN);
						BigDecimal bili4 = new BigDecimal(fanxiangOkCount2)
								.divide(new BigDecimal(buytagHistory30.size()), 2, RoundingMode.DOWN);

						if (bili.compareTo(new BigDecimal("0.7")) > 0 && buytagHistory.get(0).equals("-1")
								&& bili3.compareTo(new BigDecimal("0.99")) > 0) {
							realBuyTag = -1;
						} else if (bili2.compareTo(new BigDecimal("0.7")) > 0 && buytagHistory.get(0).equals("1")
								&& bili4.compareTo(new BigDecimal("0.99")) > 0) {
							realBuyTag = 1;
						}
//						37/60=0.616 , 22/60=0.366 , 18/60=0.300 , 10/60=0.166 0/0/0/0/0/,1.00,1.30,1.00,0.00
						fundInfo.setUpcount("↓ " + fxl + " , " + tbl + " ; " + fxdbl + " , " + dbl + " ; "
								+ buyTagLiString + "," + bili4 + "," + bili2 + "," + bili3 + "," + bili);
						fundInfo.setBuytag(buyTag);
						System.out.println("bugtag:" + buyTag);

						// 探测止盈情况

						List<Fund1Gaoduanzhuangbei2Ok> cangweis = caiService.getCangweisByTableName(tableName);
						if (cangweis.size() == 0 && buyTag == 1) {
//							fund.setCate("0");
//							caiService.updateFund(fund);
//							continue;

						}
						if (cangweis.size() > 0 && (buyTag == 1)) {
							String tipsVar = "";
//							alarmPlayer.playAlarm(1);
//							if (buyTag == 1) {
//								System.out.println("需要平仓");
//								tipsVar = "需要平仓";
//							}
//							if (fxRealBuyTag == 1) {
							System.out.println("需要立即平仓");
							tipsVar = "需要立即平仓";
//							}
							fundInfo.setUpcount(fundInfo.getUpcount() + tipsVar);

						}

//						if (cangweis.size() > 0 && cangweis.get(0).getLevel() > fundInfo.getLevel()) {
//							fundInfo.setBuytag(0);
//						}
						String lastvalue = "0";
						if (cangweis.size() > 0)
							lastvalue = cangweis.get(0).getBuypriceReal().toString();

						int clearMax = 1;
//						if (alarmtag == 0)
						int zyzstag = 0;
						Fund1Gaoduanzhuangbei2Ok cwLast = null;
						Fund1Gaoduanzhuangbei2Ok cwFirst = null;
						int index = 0;
						BigDecimal sumYl = new BigDecimal("0");
						BigDecimal sumKuisun = new BigDecimal("0");
						BigDecimal ykBigDecimal = new BigDecimal("0");
						BigDecimal sumPrice = new BigDecimal("0");
						BigDecimal sumFene = new BigDecimal("0");

						String keyString = "?instType=SWAP" + "&instId=" + fund.getCode();

						String ykp = okxService.trade("/api/v5/account/positions" + keyString, "GET", "");
						BigDecimal ykPrice = new BigDecimal("0");
						try {
							JSONObject ykpJsonObject = new JSONObject(ykp);
							JSONArray dts = ykpJsonObject.getJSONArray("data");
							for (int m = 0; m < dts.size(); m++) {
								JSONObject dt = dts.getJSONObject(m);
								if (dt.getStr("posSide").contentEquals("short")) {
									ykPrice = dt.getBigDecimal("bePx");
									break;
								}

							}

						} catch (Exception e) {
							e.printStackTrace();
							continue;
							// TODO: handle exception
						}
						// if()

						for (Fund1Gaoduanzhuangbei2Ok cw : cangweis) {
							index = index + 1;
							BigDecimal minValueLine = new BigDecimal("0");
							int clearMaxLine = 1;
//								String linenameString=tableName+fundInfo.getLevel();

							if (!jedisClient.exists("minvalue_" + tableName + cw.getLevel())) {
								jedisClient.set("minvalue_" + tableName + cw.getLevel(), jingzhi.toString());
								minValueLine = new BigDecimal(jingzhi.toString());
							} else {
								minValueLine = new BigDecimal(jedisClient.get("minvalue_" + tableName + cw.getLevel()));
								if (jingzhi.compareTo(minValueLine) < 0) {
									jedisClient.set("minvalue_" + tableName + cw.getLevel(), jingzhi.toString());
									minValueLine = new BigDecimal(
											jedisClient.get("minvalue_" + tableName + cw.getLevel()));
								}
							}

							if (cw.getIscurrent() != null && cw.getIscurrent() == 1) {
								// 最大最小价，最大涨跌幅。
								cw.setMinprice5(new BigDecimal(minScore));
								cw.setMaxprice5(new BigDecimal(maxScore));

								BigDecimal maxZdf = (new BigDecimal(maxScore).subtract(cw.getBuypriceReal()))
										.divide(cw.getBuypriceReal(), 5, RoundingMode.DOWN)
										.multiply(new BigDecimal("100"));
								;
								BigDecimal minZdf = (new BigDecimal(minScore).subtract(cw.getBuypriceReal()))
										.divide(cw.getBuypriceReal(), 5, RoundingMode.DOWN)
										.multiply(new BigDecimal("100"));
								;

								cw.setMaxzhangfu5(maxZdf);
								cw.setMaxdiefu5(minZdf);

							}
							// 更新止盈价,最高价
							cw.setMaxpriceniu(minValueLine);
							BigDecimal zhiying = new BigDecimal("0");
							BigDecimal chajuCw = jingzhi.subtract(cw.getBuypriceReal());
							BigDecimal chajuCw2 = jingzhi.subtract(ykPrice);

							BigDecimal dxzzlCw2 = chajuCw2.divide(ykPrice, 4, RoundingMode.DOWN)
									.multiply(new BigDecimal("100"));

							BigDecimal dxzzlCw = chajuCw.divide(cw.getBuypriceReal(), 4, RoundingMode.DOWN)
									.multiply(new BigDecimal("100"));
							BigDecimal moneyFeilv = new BigDecimal("0").subtract(fund.getZhangdiefu().abs());

							BigDecimal lirunBigDecimal = moneyFeilv;
							if (lirunBigDecimal == null)
								lirunBigDecimal = new BigDecimal("-0.15");
							if (dxzzlCw.compareTo(new BigDecimal("-0.15")) < 0) {
								clearMax = 0;
								clearMaxLine = 0;
							}
							BigDecimal catePriceItem = cw.getBuypriceReal().multiply(new BigDecimal("0.998"));
							BigDecimal catePriceItemBig = cw.getBuypriceReal().multiply(new BigDecimal("0.995"));

							if (dxzzlCw.compareTo(lirunBigDecimal) < 0) {
								// || (buyTagPeriodStr.equals("1") && chajufangxiangInteger == 1)
//								|| (buyTagPeriodStr.equals("-1") && chajufangxiangInteger == -1)
								clearMax = 0;
								clearMaxLine = 0;
								// 开始设置止盈

								BigDecimal chajuMax = minValueLine.subtract(cw.getBuypriceReal());
								BigDecimal chajuMaxZdf = chajuMax.divide(cw.getBuypriceReal(), 4, RoundingMode.DOWN)
										.multiply(new BigDecimal("100"));
								BigDecimal chaju75 = chajuMax.multiply(new BigDecimal("0.4"));
								zhiying = cw.getBuypriceReal().add(chaju75);
								if (zhiying.compareTo(catePriceItem) >= 0) {
									if (fund.getWendu() != null
											&& fund.getWendu().compareTo(new BigDecimal("1")) == 0) {
										zhiying = new BigDecimal("0");// catePriceItem;//
//										if (chajuMaxZdf.compareTo(new BigDecimal("-0.5")) < 0)
//											zhiying = catePriceItem;

									} else if (fund.getWendu() != null
											&& fund.getWendu().compareTo(new BigDecimal("7")) == 0) {
										zhiying = new BigDecimal("0");// catePriceItem;//
//										if (chajuMaxZdf.compareTo(new BigDecimal("-0.5")) < 0)
//											zhiying = catePriceItem;

									}

									else {
										zhiying = catePriceItem;// catePriceItem;//
									}
								}

								else if (zhiying.compareTo(catePriceItemBig) >= 0) {
									if (fund.getWendu() != null
											&& fund.getWendu().compareTo(new BigDecimal("1")) == 0) {
										zhiying = new BigDecimal("0");// catePriceItem;//
//										if (chajuMaxZdf.compareTo(new BigDecimal("-0.5")) < 0)
//											zhiying = catePriceItem;

									} else if (fund.getWendu() != null
											&& fund.getWendu().compareTo(new BigDecimal("7")) == 0) {
										zhiying = new BigDecimal("0");// catePriceItem;//
//										if (chajuMaxZdf.compareTo(new BigDecimal("-0.5")) < 0)
//											zhiying = catePriceItem;

									}

								}

//								System.out.println("更新止盈价：" + zhiying);

							}
							cw.setZhiying(zhiying);
							if (fund.getWendu() != null && fund.getWendu().compareTo(new BigDecimal("3")) != 0
									&& fund.getWendu().compareTo(new BigDecimal("4")) != 0
									&& fund.getWendu().compareTo(new BigDecimal("5")) != 0
									&& fund.getWendu().compareTo(new BigDecimal("6")) != 0) {///// 产生了趋势1；
								// 横盘微弱波动0
								if (buyTagPeriodStr.equals("-1") && chajufangxiangInteger == 1) {
//									if (cw.getZhiying().compareTo(catePriceItem) >= 0)
//									cw.setZhiying(new BigDecimal("0"));
								}
								if (buyTagPeriodStr.equals("1") && chajufangxiangInteger == -1) {
//									if (cw.getZhiying().compareTo(catePriceItem) >= 0)
//									cw.setZhiying(new BigDecimal("0"));
								}
							}

							cw.setCurrentprice(jingzhi);
							cw.setYangbencount(ybcount.intValue());
							cw.setComment(dxzzlCw.toString());
							// 对冲判断：
							List<Fund1Gaoduanzhuangbei2Ok> duichongList = cangweis.subList(0, index);

							if (1 == 1) {
//
//								if (index == 2) {
//									BigDecimal kuisunNow = jingzhi.multiply(new BigDecimal("0.9995"))
//											.multiply(cwLast.getFene());
//
//									BigDecimal kuisunOri = cwLast.getBuypriceReal().multiply(new BigDecimal("1.0005"))
//											.multiply(cwLast.getFene());
//
//									BigDecimal kuisunBigDecimal = kuisunNow.subtract(kuisunOri);
//
//									BigDecimal yingliNow = jingzhi.multiply(new BigDecimal("0.9995"))
//											.multiply(cw.getFene());
//
//									BigDecimal yingliOri = cw.getBuypriceReal().multiply(new BigDecimal("1.0005"))
//											.multiply(cw.getFene());
//
//									BigDecimal yingliBigDecimal = yingliNow.subtract(yingliOri);
//									BigDecimal sumfund = yingliBigDecimal.add(kuisunBigDecimal);
//									// &&sumfund.compareTo(maxValue)
//
//									BigDecimal yingkuibi = new BigDecimal("0");
//									if (kuisunBigDecimal.compareTo(new BigDecimal("0")) < 0
//											&& yingliBigDecimal.compareTo(new BigDecimal("0")) > 0) {
//
//										yingkuibi = kuisunBigDecimal.abs().divide(yingliBigDecimal,
//												RoundingMode.HALF_DOWN);
//
//										if (yingkuibi.compareTo(new BigDecimal("0.4")) > 0
//												&& yingkuibi.compareTo(new BigDecimal("0.7")) < 0) {
//											alarmtag = 5;
//											cw.setComment("对冲：" + dxzzlCw);
//											zyzstag = 1;
//											try {
//												FileUtil.appendUtf8String("【对冲信息-2层仓位】 zytag:" + zyzstag
//														+ "  kuiyingbi:" + yingkuibi + " kuisune" + kuisunBigDecimal
//														+ " yinglie:" + yingliBigDecimal + "  table:" + tableName
//														+ "  level:" + cw.getLevel() + "  " + "levellast:"
//														+ cwLast.getLevel() + " " + kuisunBigDecimal + " --- "
//														+ yingliBigDecimal + "\n" + cw.toString() + "\n"
//														+ cwLast.toString() + "\n----\n", "d:\\对冲日志.txt");
//											} catch (Exception e) {
//												e.printStackTrace();
//
//											}
//
//										}
//
//									}
//
//								}
//
//								if (index == 3) {
//									BigDecimal kuisunNow2 = jingzhi.multiply(new BigDecimal("0.9995"))
//											.multiply(cwLast.getFene());
//
//									BigDecimal kuisunOri2 = cwLast.getBuypriceReal().multiply(new BigDecimal("1.0005"))
//											.multiply(cwLast.getFene());
//
//									BigDecimal kuisunNow1 = jingzhi.multiply(new BigDecimal("0.9995"))
//											.multiply(cangweis.get(0).getFene());
//
//									BigDecimal kuisunOri1 = cangweis.get(0).getBuypriceReal()
//											.multiply(new BigDecimal("1.0005")).multiply(cangweis.get(0).getFene());
//
//									BigDecimal kuisunBigDecimal1 = kuisunNow1.subtract(kuisunOri1);
//
//									BigDecimal kuisunBigDecimal2 = kuisunNow2.subtract(kuisunOri2);
//
//									BigDecimal yingliNow = jingzhi.multiply(new BigDecimal("0.9995"))
//											.multiply(cw.getFene());
//
//									BigDecimal yingliOri = cw.getBuypriceReal().multiply(new BigDecimal("1.0005"))
//											.multiply(cw.getFene());
//
//									BigDecimal yingliBigDecimal = yingliNow.subtract(yingliOri);
////									BigDecimal sumfund = yingliBigDecimal.add(kuisunBigDecimal);
//
//									if (kuisunBigDecimal2.compareTo(new BigDecimal("0")) >= 0) {
//										yingliNow = yingliNow.add(kuisunBigDecimal2);
//
//									} else {
//										kuisunBigDecimal1 = kuisunBigDecimal1.add(kuisunBigDecimal2);
//									}
//
//									// &&sumfund.compareTo(maxValue)
//									cwFirst = cangweis.get(0);
//
//									BigDecimal yingkuibi = new BigDecimal("0");
//									if (kuisunBigDecimal1.compareTo(new BigDecimal("0")) < 0
//											&& yingliBigDecimal.compareTo(new BigDecimal("0")) > 0) {
//
//										yingkuibi = kuisunBigDecimal1.abs().divide(yingliBigDecimal,
//												RoundingMode.HALF_DOWN);
//
//										if (yingkuibi.compareTo(new BigDecimal("0.4")) > 0
//												&& yingkuibi.compareTo(new BigDecimal("0.7")) < 0) {
//											alarmtag = 5;
//											cw.setComment("对冲：" + dxzzlCw);
//											zyzstag = 1;
//											try {
//												FileUtil.appendUtf8String("【对冲信息-3层仓位】 zytag:" + zyzstag
//														+ "  kuiyingbi:" + yingkuibi + " kuisune" + kuisunBigDecimal1
//														+ " yinglie:" + yingliBigDecimal + "  table:" + tableName
//														+ "  level:" + cw.getLevel() + "  " + "levellast:"
//														+ cwLast.getLevel() + " " + kuisunBigDecimal1 + " --- "
//														+ yingliBigDecimal + "\n" + cw.toString() + "\n"
//														+ cwLast.toString() + "\n----\n", "d:\\对冲日志.txt");
//											} catch (Exception e) {
//												e.printStackTrace();
//
//											}
//
//										}
//
//									}
//
//								}

//								BigDecimal itemNowFund = jingzhi.multiply(new BigDecimal("1.0005"))
//										.multiply(cw.getFene());
//								BigDecimal itemOriFund = cw.getBuypriceReal().multiply(new BigDecimal("0.9995"))
//										.multiply(cw.getFene());
//								BigDecimal itemShouYi = itemOriFund.subtract(itemNowFund);
//
//								if (itemShouYi.compareTo(new BigDecimal("0")) > 0)
//									sumYl = sumYl.add(itemShouYi);
//								else {
//									sumKuisun = sumKuisun.add(itemShouYi);
//								}

								BigDecimal yingkuibi = new BigDecimal("0");
//
//								if (sumKuisun.compareTo(new BigDecimal("0")) < 0
//										&& sumYl.compareTo(new BigDecimal("0")) > 0) {

//								yingkuibi = sumKuisun.abs().divide(sumYl, RoundingMode.HALF_DOWN);

								sumPrice = sumPrice.add(cw.getBuypriceReal().multiply(cw.getFene()));
								sumFene = sumFene.add(cw.getFene());
								BigDecimal avg = sumPrice.divide(sumFene, 10, RoundingMode.UP);
//									BigDecimal avgY/uqi = avg.multiply(new BigDecimal("1.0015"));

								BigDecimal chajuCwAll = jingzhi.subtract(avg);
								BigDecimal dxzzlCwAll = chajuCwAll.divide(avg, 4, RoundingMode.DOWN)
										.multiply(new BigDecimal("100"));

								if (dxzzlCwAll.compareTo(new BigDecimal("-0.25")) <= 0 && index > 1
										&& cangweis.get(0).getBuypriceReal().compareTo(jingzhi) < 0
										&& fund.getZhenfurealyear() != null
										&& fund.getZhenfurealyear().compareTo(new BigDecimal("0")) > 0) {

//									if (yingkuibi.compareTo(new BigDecimal("0.2")) > 0
//											&& yingkuibi.compareTo(new BigDecimal("0.3")) < 0 && 1 == 0) {
									// yingkuibi.compareTo(new BigDecimal("0.4")) < 0
									alarmtag = 5;
									cw.setComment("对冲：" + dxzzlCwAll);
									zyzstag = 1;
									try {
										FileUtil.appendUtf8String("【对冲信息-" + index + "层仓位】 收益：" + dxzzlCwAll + " zytag:"
												+ zyzstag + "  kuiyingbi:" + yingkuibi + " kuisune" + sumKuisun
												+ " yinglie:" + sumYl + "  table:" + tableName + "  level:"
												+ cw.getLevel() + "\n" + cw.toString() + "\n" + cwLast.toString()
												+ "\n----\n", "d:\\对冲日志.txt");
									} catch (Exception e) {
										e.printStackTrace();

									}

								}

								if (dxzzlCw2.compareTo(new BigDecimal("-0.15")) < 0) {

									alarmtag = 5;
									cw.setComment("对冲：" + dxzzlCw2);
									zyzstag = 1;
									duichongList = cangweis;

								}

//								}

							}

							if (zyzstag == 0) {

								BigDecimal zhisunBigDecimal = new BigDecimal(cw.getZhisun() * 0.5 + "")
										.multiply(new BigDecimal("1"));
								if (dxzzlCw.compareTo(zhisunBigDecimal) > 0 && index == 1) {
									alarmtag = 6;
									cw.setComment("止损：" + dxzzlCw);
									zyzstag = 1;
								}

								else if (cw.getName().indexOf("pc") != -1) {
									alarmtag = 5;
									cw.setComment("平仓：" + dxzzlCw);
									zyzstag = 1;
								}

								else if (cw.getZhiying() != null && cw.getZhiying().compareTo(new BigDecimal("0")) > 0
										&& cw.getZhiying().compareTo(jingzhi) < 0
										&& dxzzlCw.compareTo(new BigDecimal("-0.15")) < 0) {
									// && index == 1多层仓位也需要止盈。

//								if (alarmtag != 3)
									if (cw.getName().indexOf("sar") != -1) {
										alarmtag = 5;
										cw.setComment("止盈：" + dxzzlCw);
										zyzstag = 1;
									}
									// 止盈操作
//								caiService.updateCurrentPrice(tableName, jingzhi, alarmtag, fundInfo.getBuypriceReal(),
//										jsonObject.getBigDecimal("gszzl"));

								} else if (buyTag == 1 && cangweis.size() == 1) {
									System.out.println("需要立即平仓");
//									alarmtag = 5;
//									cw.setComment("平仓：" + dxzzlCw);
//									zyzstag = 1;
//									alarmPlayer.playAlarm(1);
								}

							}

							if (clearMaxLine == 1) {
								try {
									jedisClient.del("minvalue_" + tableName + cw.getLevel());
								} catch (Exception e) {
									// TODO: handle exception
								}

							}
//							if (cwFirst == null)
							cw.setBuytag(buyTag);
							try {
								caiService.updatezhiying(cw, tableName, duichongList, fund);
							} catch (Exception e) {
								e.printStackTrace();
								// TODO: handle exception
//								alarmPlayer.playAlarm2(1800);
								if (!e.getMessage().startsWith("return")) {
									alarmPlayer.playAlarm2(1800);
									Thread.sleep(1800000);
								}
//								Thread.sleep(1800000);
							}

//							else {
//								caiService.updatezhiying(cw, tableName, cwLast, cwFirst);
//							}
//
//							cwLast = cw;

							if (zyzstag == 1) {

								break;
							}

//								}

						}
						if (ykPrice != null)
							fundInfo.setUpcount(ykPrice.toString());
						if (sumYl.compareTo(new BigDecimal("0")) > 0) {

//							BigDecimal ykb = sumKuisun.abs().divide(sumYl, 2, RoundingMode.DOWN);
//
//							fundInfo.setUpcount(fundInfo.getUpcount() + " 盈亏比：" + sumKuisun + "/" + sumYl + "=" + ykb);
//						

						}

						if (clearMax == 1)
							jedisClient.del("minvalue_" + tableName);

						if (zyzstag == 1) {
							System.out.print("止盈止损:");
//							Thread.sleep(10000);

							// 小涨幅卖出，小跌幅买入，有止盈情况发生
							if (fundInfo.getName() != null && fundInfo.getName().indexOf("alarm") != -1) {
								String re = null;

//								re = HttpUtil.sendGet("https://rpa.insfair.cn/api/rpa/message/forward?uid=80");
//
//								if (re == null) {
//									HttpUtil.sendGet("http://192.168.188.190/api/rpa/message/forward?uid=80");
//								}
								if (alarmtag == 5) {

									alarmPlayer.playAlarm(5);
								} else {
									alarmPlayer.playAlarm2(5);
								}

							}

							continue;
						}
//						caiService.updateCurrentPrice(tableName, jingzhi, alarmtag, fundInfo.getBuypriceReal(),
//								jsonObject.getBigDecimal("gszzl"), fund, fundInfo, lastvalue);
						if ((jingzhi.compareTo(maxP) >= 0 || jingzhi.compareTo(minP) <= 0)
								&& jingzhi.compareTo(new BigDecimal("0")) != 0) {
							// 提醒用户卖出或买入
							System.out.println("需要买入或卖出此基金：" + fundInfo.getName());
							try {
								if (jingzhi.compareTo(maxP) >= 0) {
									alarmtag = 1;
									if (cbt == -1) {
//										alarmtag = 1;
										if (fund.getWendu() != null
												&& fund.getWendu().compareTo(new BigDecimal("6")) == 0) {

											alarmtag = 7;
										} else if (fund.getWendu() != null
												&& fund.getWendu().compareTo(new BigDecimal("6.5")) == 0) {
											alarmtag = 7;
										} else if (fund.getWendu() != null
												&& fund.getWendu().compareTo(new BigDecimal("7")) == 0) {
											alarmtag = 7;

										}

										caiService.updateCurrentPrice(tableName, jingzhi, alarmtag,
												fundInfo.getBuypriceReal(), jsonObject.getBigDecimal("gszzl"), fund,
												fundInfo, lastvalue);

									} else {
										caiService.updateCurrentPrice(tableName, jingzhi, alarmtag,
												fundInfo.getBuypriceReal(), jsonObject.getBigDecimal("gszzl"), fund,
												fundInfo, lastvalue);
									}
								} else {

									alarmtag = 2;
									if (fund.getWendu() != null
											&& fund.getWendu().compareTo(new BigDecimal("6")) == 0) {

										alarmtag = 8;
									} else if (fund.getWendu() != null
											&& fund.getWendu().compareTo(new BigDecimal("6.5")) == 0) {
										alarmtag = 8;
									} else if (fund.getWendu() != null
											&& fund.getWendu().compareTo(new BigDecimal("7")) == 0) {
										alarmtag = 8;

									}

									caiService.updateCurrentPrice(tableName, jingzhi, alarmtag,
											fundInfo.getBuypriceReal(), jsonObject.getBigDecimal("gszzl"), fund,
											fundInfo, lastvalue);

								}

							} catch (Exception e) {
								e.printStackTrace();
								// TODO: handle exception
//								alarmPlayer.playAlarm2(1800);
//								Thread.sleep(1800000);
								if (!e.getMessage().startsWith("return")) {
									alarmPlayer.playAlarm2(1800);
									Thread.sleep(1800000);
								}
							}

							if (fundInfo.getName() != null && fundInfo.getName().indexOf("alarm") != -1) {
								System.out.print("买入卖出:");
//								Thread.sleep(10000);
								String re = null;

//								re = HttpUtil.sendGet("https://rpa.insfair.cn/api/rpa/message/forward?uid=80");
//
//								if (re == null) {
//									HttpUtil.sendGet("http://192.168.188.190/api/rpa/message/forward?uid=80");
//								}
//								if(fundInfo.getName().indexOf("ab")!=-1||fundInfo.getName().indexOf("as")!=-1)
//								alarmPlayer.playAlarm(1);
//								else {
//									alarmPlayer.playAlarm(1);
//								}

							}

						} else {
							// 更新数据库最新基金价格：
							BigDecimal chaju = jingzhi.subtract(fundInfo.getBuypriceReal());
							BigDecimal dxzzl = chaju.divide(fundInfo.getBuypriceReal(), 4, RoundingMode.DOWN)
									.multiply(new BigDecimal("100"));

//							int alarmtag = 0;
							BigDecimal catePrice = fundInfo.getBuypriceReal().multiply(new BigDecimal("0.9975"));
							BigDecimal catePrice2 = fundInfo.getBuypriceReal()
									.multiply(new BigDecimal("2").subtract(fund.getZhangdiefu()));
//							fundInfo.setCate(catePrice.toString());

							if ((fund.getWendu() != null && fund.getWendu().compareTo(new BigDecimal("6")) == 0)
//									&& cbt == 1
							) {// &&
								// fundInfo.getFene()
								// !=
								// null

								if (fundInfo.getName() != null && fundInfo.getName().indexOf("stop") != -1
										&& (jingzhi.compareTo(fundInfo.getSellprice().add(
												fundInfo.getWanggeprice().multiply(new BigDecimal("0.5")))) >= 0)) {
									alarmtag = 7;

								}

							} else if ((fund.getWendu() != null
									&& fund.getWendu().compareTo(new BigDecimal("6.5")) == 0)
//									&& cbt == 1
							) {// &&
								// fundInfo.getFene()
								// !=
								// null

								if (fundInfo.getName() != null && fundInfo.getName().indexOf("stop") != -1
										&& (jingzhi.compareTo(fundInfo.getSellprice().add(
												fundInfo.getWanggeprice().multiply(new BigDecimal("0.5")))) >= 0)) {
									alarmtag = 7;

								}

							} else if ((fund.getWendu() != null && fund.getWendu().compareTo(new BigDecimal("7")) == 0)
//									&& cbt == 1
							) {// &&
								// fundInfo.getFene()
								// !=
								// null

								if (fundInfo.getName() != null && fundInfo.getName().indexOf("stop") != -1
										&& (jingzhi.compareTo(fundInfo.getSellprice().add(
												fundInfo.getWanggeprice().multiply(new BigDecimal("0.5")))) >= 0)) {
									alarmtag = 7;

								}

							}

							else if (fundInfo.getName() != null && fundInfo.getName().indexOf("star") != -1
									&& (jingzhi.compareTo(catePrice) <= 0)) {// && fundInfo.getFene() !=
																				// null|| buyTag == -1

								if (fund.getWendu() != null && fund.getWendu().compareTo(new BigDecimal("6")) == 0) {

									alarmtag = 8;
								} else if (fund.getWendu() != null
										&& fund.getWendu().compareTo(new BigDecimal("6.5")) == 0) {
									alarmtag = 8;
								} else if (fund.getWendu() != null
										&& fund.getWendu().compareTo(new BigDecimal("7")) == 0) {
									alarmtag = 8;

								}

								alarmtag = 3;

							}

							else if (fundInfo.getName() != null && fundInfo.getName().indexOf("stop") != -1
									&& (jingzhi.compareTo(fundInfo.getSellprice()
											.add(fundInfo.getWanggeprice().multiply(new BigDecimal("0.5")))) >= 0)) {// &&&&
																														// cbt
																														// ==
																														// -1
								// fundInfo.getFene()
								// !=
								// null
								alarmtag = 4;

							}

							else if (cbt == -1
									&& (fund.getWendu() != null && fund.getWendu().compareTo(new BigDecimal("0")) > 0)
									&& jingzhi.compareTo(fundInfo.getSellprice()) >= 0) {

								alarmtag = 4;
							}

							else if ((fund.getWendu() != null && fund.getWendu().compareTo(new BigDecimal("4")) == 0)
									&& jingzhi.compareTo(fundInfo.getSellprice()) >= 0) {

								alarmtag = 4;
							}

							else if ((fund.getWendu() != null && fund.getWendu().compareTo(new BigDecimal("5")) == 0)
									&& jingzhi.compareTo(catePrice2) >= 0) {

								alarmtag = 4;
							}

							else if ((fund.getWendu() != null && fund.getWendu().compareTo(new BigDecimal("4.5")) == 0)
									&& jingzhi.compareTo(fundInfo.getSellprice()) >= 0) {

								alarmtag = 4;
							} else if ((fund.getWendu() != null
									&& fund.getWendu().compareTo(new BigDecimal("5.5")) == 0)
									&& jingzhi.compareTo(catePrice2) >= 0) {

								alarmtag = 4;
							}

							// 判断1秒内涨幅是否超过0.2，并且最新价高于上一次价格：
//							else if (fundInfo.getSellpriceReal() != null
//									&& dxzzlShichang.compareTo(fundInfo.getSellpriceReal()) > 0) {
//								alarmtag = 6;
//							}
							try {
								caiService.updateCurrentPrice(tableName, jingzhi, alarmtag, fundInfo.getBuypriceReal(),
										jsonObject.getBigDecimal("gszzl"), fund, fundInfo, lastvalue);

							} catch (Exception e) {
								e.printStackTrace();
								// TODO: handle exception
//								alarmPlayer.playAlarm2(1800);
//								Thread.sleep(1800000);
								if (!e.getMessage().startsWith("return")) {
									alarmPlayer.playAlarm2(1800);
									Thread.sleep(1800000);
								}
							}

//							if (alarmtag == 0) {

//							}
							if (alarmtag == 3 || alarmtag == 4 || alarmtag == 6) {// 小涨幅卖出，小跌幅买入，有止盈情况发生

								System.out.print("快速追涨和抄底:");
//								Thread.sleep(10000);

								if (fundInfo.getName() != null && fundInfo.getName().indexOf("alarm") != -1) {
									String re = null;

//									re = HttpUtil.sendGet("https://rpa.insfair.cn/api/rpa/message/forward?uid=80");
//
//									if (re == null) {
//										HttpUtil.sendGet("http://192.168.188.190/api/rpa/message/forward?uid=80");
//									}
//									if (alarmtag == 5) {
//
//										alarmPlayer.playAlarm(1);
//									} else {
//
//										alarmPlayer.playAlarm(1);
//									}

								}
							}

						}
						// 更新今日涨幅
//						fund.setZhangdiefu(jsonObject.getBigDecimal("gszzl"));
//						caiService.updateIndex(tableName, tag);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						System.out.println("更新基金价格失败：" + fundInfo.getName());
					}
				}

			}

		}

	}

	public static BigDecimal getCurGainRate(BigDecimal lastPrice, BigDecimal curPrice, BigDecimal lastNetValue,
			BigDecimal curNetValue, String lastDate, String curDate, JSONArray profitAry) {

		BigDecimal curGainRate = BigDecimal.ZERO; // 本次收益率

		if (profitAry == null || profitAry.size() < 1) {
			curGainRate = curPrice.subtract(lastPrice).divide(lastPrice, 4, BigDecimal.ROUND_HALF_UP);
			return curGainRate;
		}

		if (lastNetValue.subtract(lastPrice).multiply(BigDecimal.valueOf(1000)).setScale(0, RoundingMode.HALF_UP)
				.compareTo(curNetValue.subtract(curPrice).multiply(BigDecimal.valueOf(1000)).setScale(0,
						RoundingMode.HALF_UP)) == 0) {
			curGainRate = curPrice.subtract(lastPrice).divide(lastPrice, 4, BigDecimal.ROUND_HALF_UP);
			return curGainRate;
		}

		JSONObject tmpProfitObj = profitAry.getJSONObject(profitAry.size() - 1);
		String profitDate = tmpProfitObj.getStr("profit_date");
		if (profitDate.compareTo(lastDate) <= 0) {
			curGainRate = curPrice.subtract(lastPrice).divide(lastPrice, 4, BigDecimal.ROUND_HALF_UP);
			return curGainRate;
		}

		curGainRate = BigDecimal.ONE;
		BigDecimal tmpLastPrice = lastPrice;
		for (int i = 0; i < profitAry.size(); i++) {
			tmpProfitObj = profitAry.getJSONObject(i);
			profitDate = tmpProfitObj.getStr("profit_date");

			if (profitDate.compareTo(lastDate) > 0 && profitDate.compareTo(curDate) <= 0) {
				BigDecimal lastDayPrice = new BigDecimal(tmpProfitObj.getStr("lastday_price"));
				BigDecimal price = new BigDecimal(tmpProfitObj.getStr("price"));
				BigDecimal rate = new BigDecimal(tmpProfitObj.getStr("rate")).divide(BigDecimal.valueOf(100), 4,
						BigDecimal.ROUND_HALF_UP); // 分红当天的收益率

				BigDecimal tmpRate = lastDayPrice.subtract(tmpLastPrice).divide(tmpLastPrice, 4,
						BigDecimal.ROUND_HALF_UP);
				curGainRate = curGainRate.multiply(BigDecimal.ONE.add(tmpRate));
				tmpLastPrice = price;

				curGainRate = curGainRate.multiply(BigDecimal.ONE.add(rate));
			}
		}

		BigDecimal tmpRate = curPrice.subtract(tmpLastPrice).divide(tmpLastPrice, 4, BigDecimal.ROUND_HALF_UP);
		curGainRate = curGainRate.multiply(BigDecimal.ONE.add(tmpRate));

		return curGainRate.subtract(BigDecimal.ONE).setScale(4, RoundingMode.HALF_UP);
	}
}
