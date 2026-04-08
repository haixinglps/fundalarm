package cn.exrick.manager.service.task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.exrick.common.jedis.JedisClient;
import cn.exrick.common.utils.HttpUtil;
import cn.exrick.manager.pojo.Fund;
import cn.exrick.manager.pojo.Fund1Gaoduanzhuangbei2Ok;
import cn.exrick.manager.service.FundService;
import cn.exrick.manager.service.util.AlarmPlayer;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;;

//@Component

public class FundPriceUpdate {
	@Reference
	private FundService caiService;
//	AlarmPlayer alarmPlayer = new AlarmPlayer();
	@Autowired
	JedisClient jedisClient;

	@Autowired
	AlarmPlayer alarmPlayer;
	// key命名规范 项目名称
	private static final String PRIVATE_HASH_KEY_ARTICLES = "postc_set"; // 评论编号
	private static final String PRIVATE_HASH_KEY_ARTICLE_USERS = "postc_user_like_set"; // 评论踩人集合
//	private static final String PRIVATE_HASH_KEY_ARTICLE_USER_INFO = "post_user_like"; // 评论踩人踩详情
//	private static final String PRIVATE_HASH_KEY_POST_COUNTER = "post_counter"; // 缓存里的评论踩数

	@Scheduled(cron = "0 */1 * * * ?")
	public void run() {
		// 定义所有基金表名称。70个，目前6个。
		List<String> tableNames = new ArrayList<String>();
		tableNames.add("fund_1_gaoduanzhuangbei_2_ok");
		tableNames.add("fund_1_huangjin_5_ok");
		tableNames.add("fund_1_jungong_3_ok");
		tableNames.add("fund_1_meitan_4_ok");
		tableNames.add("fund_1_zhengquan_6_ok");
		tableNames.add("fund_1_fangdichan_1_ok");
		List<Fund> funds = caiService.getIndex();
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

				minP = fundInfo.getBuyprice().subtract(fundInfo.getWanggeprice());
				maxP = fundInfo.getSellprice();
				String codeString = fundInfo.getCode();
				// 获取交易所最新基金估值。
				// https://fundgz.1234567.com.cn/js/012212.js?rt=1694399168362
				String url = "https://fundgz.1234567.com.cn/js/" + codeString + ".js?rt=" + System.currentTimeMillis();
				String resultString = null;
				if (fund.getSinacode() == null) {
					try {
						resultString = cn.exrick.common.utils.HttpUtil.sendGet(url);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						System.out.println("网络异常，url:" + url);
						continue;
					}

				} else if (fund.getSinacode().equals("btc")) {
					/* JVM设置代理 */
//					System.getProperties().setProperty("https.proxyHost", "localhost");
//					System.getProperties().setProperty("https.proxyPort", "7890");
					Map<String, String> sinaheader = new HashMap<String, String>();
//					startDate: 2023-10-22
					url = "https://price.btcfans.com/zh-cn/coin/" + fund.getCode();
					sinaheader.put("Referer", "https://price.btcfans.com/");
					sinaheader.put("Host", "price.btcfans.com");
					String gupiaoRes = null;
					try {
						gupiaoRes = cn.exrick.common.utils.HttpUtil.sendGetWithHeader(url, sinaheader);
					} catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					}

					if (gupiaoRes == null) {
						System.out.println("=========网络异常，url:" + url);
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
					continue;
				} else {

					System.out.println("url:" + url);
					System.out.println(resultString);
					try {
						JSONObject jsonObject = new JSONObject(resultString.replace("jsonpgz(", "").replace(");", ""));
						BigDecimal jingzhi = jsonObject.getBigDecimal("gsz");

						String lastvalue = jedisClient.get("lastvalue_" + fund.getCode());
						if (lastvalue != null) {
							BigDecimal lBigDecimal = new BigDecimal(lastvalue);
							if (jingzhi.compareTo(lBigDecimal) > 0) {
								{
									newFund.setQushi("↑");
									caiService.updateFund(newFund);
								}
							} else if (jingzhi.compareTo(lBigDecimal) < 0) {
								newFund.setQushi("↓");
								caiService.updateFund(newFund);
							}

						} else {
							lastvalue = "0";
						}
						jedisClient.setex("lastvalue_" + fund.getCode(), 86400, jingzhi.toString());

						BigDecimal maxValue = new BigDecimal("0");
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
									System.currentTimeMillis() + "");
							jedisClient.expire("price_" + fund.getCode(), 600);

						} else {
							System.out.println("存在key:" + "price_" + fund.getCode());
							jedisClient.zadd("price_" + fund.getCode(), jingzhi.doubleValue(),
									System.currentTimeMillis() + "");
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

						System.out.println("成功更新基金价格：" + jingzhi + "(" + minP + "," + maxP + ")" + fundInfo.getName());
						if ((jingzhi.compareTo(maxP) >= 0 || jingzhi.compareTo(minP) <= 0)
								&& jingzhi.compareTo(new BigDecimal("0")) != 0) {
							// 提醒用户卖出或买入
							System.out.println("需要买入或卖出此基金：" + fundInfo.getName());
							if (jingzhi.compareTo(maxP) >= 0)
								caiService.updateCurrentPrice(tableName, jingzhi, 2, fundInfo.getBuypriceReal(),
										jsonObject.getBigDecimal("gszzl"), fund, fundInfo, lastvalue);
							else {
								caiService.updateCurrentPrice(tableName, jingzhi, 1, fundInfo.getBuypriceReal(),
										jsonObject.getBigDecimal("gszzl"), fund, fundInfo, lastvalue);

							}
							if (fundInfo.getName() != null && fundInfo.getName().indexOf("alarm") != -1) {
								String re = null;

								re = HttpUtil.sendGet("https://rpa.insfair.cn/api/rpa/message/forward?uid=80");

//								if (re == null) {
								HttpUtil.sendGet("http://192.168.188.190/api/rpa/message/forward?uid=80");
//								}
								alarmPlayer.playAlarm(2);

							}

						} else {
							// 更新数据库最新基金价格：
							BigDecimal catePriceItem0 = fundInfo.getBuypriceReal().multiply(new BigDecimal("1.0025"));
							BigDecimal catePriceItem1 = fundInfo.getBuypriceReal().multiply(new BigDecimal("0.9975"));
							BigDecimal chaju = jingzhi.subtract(fundInfo.getBuypriceReal());
							BigDecimal dxzzl = chaju.divide(fundInfo.getBuypriceReal(), 4, RoundingMode.DOWN)
									.multiply(new BigDecimal("100"));
							int alarmtag = 0;
							if (fundInfo.getName() != null && fundInfo.getName().indexOf("star") != -1
									&& (jingzhi.compareTo(catePriceItem0) >= 0)) {
								alarmtag = 3;

							}

							if (fundInfo.getName() != null && fundInfo.getName().indexOf("stop") != -1
									&& (jingzhi.compareTo(catePriceItem1) <= 0)) {
								alarmtag = 3;

							}

							caiService.updateCurrentPrice(tableName, jingzhi, alarmtag, fundInfo.getBuypriceReal(),
									jsonObject.getBigDecimal("gszzl"), fund, fundInfo, lastvalue);
//							if (alarmtag == 0) {

							// 探测止盈情况

							List<Fund1Gaoduanzhuangbei2Ok> cangweis = caiService.getCangweisByTableName(tableName);
							int clearMax = 1;

							for (Fund1Gaoduanzhuangbei2Ok cw : cangweis) {
								BigDecimal maxValueLine = new BigDecimal("0");
								int clearMaxLine = 1;
//									String linenameString=tableName+fundInfo.getLevel();

								if (!jedisClient.exists("maxvalue_" + tableName + cw.getLevel())) {
									jedisClient.set("maxvalue_" + tableName + cw.getLevel(), jingzhi.toString());
									maxValueLine = new BigDecimal(jingzhi.toString());
								} else {
									maxValueLine = new BigDecimal(
											jedisClient.get("maxvalue_" + tableName + cw.getLevel()));
									if (jingzhi.compareTo(maxValueLine) > 0) {
										jedisClient.set("maxvalue_" + tableName + cw.getLevel(), jingzhi.toString());
										maxValueLine = new BigDecimal(
												jedisClient.get("maxvalue_" + tableName + cw.getLevel()));
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
								cw.setMaxpriceniu(maxValueLine);
								BigDecimal zhiying = new BigDecimal("0");
								BigDecimal catePriceItem = cw.getBuypriceReal().multiply(new BigDecimal("1.0025"));
								BigDecimal chajuCw = jingzhi.subtract(cw.getBuypriceReal());
								BigDecimal dxzzlCw = chajuCw.divide(cw.getBuypriceReal(), 4, RoundingMode.DOWN)
										.multiply(new BigDecimal("100"));

								BigDecimal lirunBigDecimal = cw.getMoney();
								if (lirunBigDecimal == null)
									lirunBigDecimal = new BigDecimal("10");

								if (dxzzlCw.compareTo(new BigDecimal("0.15")) > 0) {
									clearMax = 0;
									clearMaxLine = 0;
								}
								if (dxzzlCw.compareTo(new BigDecimal("0.5")) > 0) {
									clearMax = 0;
									clearMaxLine = 0;
									// 开始设置止盈
									BigDecimal chajuMax = maxValueLine.subtract(cw.getBuypriceReal());
									BigDecimal chaju75 = chajuMax.multiply(new BigDecimal("0.4"));
									zhiying = cw.getBuypriceReal().add(chaju75);
									if (zhiying.compareTo(catePriceItem) <= 0) {
//										zhiying = catePriceItem;
										zhiying = new BigDecimal("0");// catePriceItem;//
									}
//									else {
//										zhiying = catePriceItem;// catePriceItem;//
//									}

									cw.setZhiying(zhiying);

								}

								cw.setCurrentprice(jingzhi);
								cw.setYangbencount(ybcount.intValue());
								cw.setComment(dxzzlCw.toString());
								if (cw.getZhiying() != null && cw.getZhiying().compareTo(new BigDecimal("0")) > 0
										&& cw.getZhiying().compareTo(jingzhi) > 0
										&& ((dxzzlCw.compareTo(new BigDecimal("2")) > 0 && fund.getSinacode() == null)
												|| (dxzzlCw.compareTo(new BigDecimal("0.15")) > 0
														&& fund.getSinacode() != null))) {

//									cw.setComment("止盈：" + dxzzlCw);
//									if (cw.getName().indexOf("sar") != -1) {
									if (alarmtag != 3)
										alarmtag = 3;
									cw.setComment("止盈：" + dxzzlCw);

//									}

									// 止盈操作
//									caiService.updateCurrentPrice(tableName, jingzhi, alarmtag, fundInfo.getBuypriceReal(),
//											jsonObject.getBigDecimal("gszzl"));
								}

								caiService.updatezhiying(cw, tableName, null, fund);

								if (clearMaxLine == 1) {
									try {
										jedisClient.del("maxvalue_" + tableName + cw.getLevel());
									} catch (Exception e) {
										// TODO: handle exception
									}

								}

							}
							if (clearMax == 1)
								jedisClient.del("maxvalue_" + tableName);

//							}
							if (alarmtag == 3) {
								if (fundInfo.getName() != null && fundInfo.getName().indexOf("alarm") != -1) {
									String re = null;

									re = HttpUtil.sendGet("https://rpa.insfair.cn/api/rpa/message/forward?uid=80");

//									if (re == null) {
									HttpUtil.sendGet("http://192.168.188.190/api/rpa/message/forward?uid=80");
//									}
									alarmPlayer.playAlarm(2);

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
