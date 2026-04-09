package cn.exrick.manager.service.task;

import java.io.IOException;
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

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.exrick.common.jedis.JedisClient;
import cn.exrick.common.utils.CommonUtils;
import cn.exrick.manager.mapper.Fund1Gaoduanzhuangbei2OkMapper;
import cn.exrick.manager.pojo.Fund;
import cn.exrick.manager.pojo.Fund1Gaoduanzhuangbei2Ok;
import cn.exrick.manager.pojo.Fund1Gaoduanzhuangbei2OkExample;
import cn.exrick.manager.service.FundService;
import cn.exrick.manager.service.OkxService;
import cn.exrick.manager.service.util.AlarmPlayer;
import cn.exrick.manager.service.util.HttpsUrlValidator;
import cn.exrick.manager.service.util.okx2;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import redis.clients.jedis.Tuple;;

@Component
public class FundPriceUpdate2 {

//	headers.set("OK-ACCESS-KEY", okxConfig.getApikey());
//    headers.set("OK-ACCESS-SIGN", sign.getSign());
//    headers.set("OK-ACCESS-TIMESTAMP", sign.getTimestamp());
//    headers.set("OK-ACCESS-PASSPHRASE", okxConfig.getPassphrase());

	@Reference
//	@Qualifier("fundServiceImpl")
	private FundService caiService;
//	AlarmPlayer alarmPlayer = new AlarmPlayer();

	@Reference
	OkxService okxService;

	@Autowired
	AlarmPlayer alarmPlayer;

	@Autowired
	JedisClient jedisClient;

	@Autowired
	DailyProfitTManager dailyProfitTManager;

	@Autowired
	Fund1Gaoduanzhuangbei2OkMapper fund1Gaoduanzhuangbei2OkMapper;
	// key命名规范 项目名称
	private static final String PRIVATE_HASH_KEY_ARTICLES = "postc_set"; // 评论编号
	private static final String PRIVATE_HASH_KEY_ARTICLE_USERS = "postc_user_like_set"; // 评论踩人集合
//	private static final String PRIVATE_HASH_KEY_ARTICLE_USER_INFO = "post_user_like"; // 评论踩人踩详情
//	private static final String PRIVATE_HASH_KEY_POST_COUNTER = "post_counter"; // 缓存里的评论踩数

	public static Map<String, String> ck = new HashMap<String, String>();
	public static String cookieString = "";

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

		if (ck.size() == 0) {
//			String[] ckStrings = cookieString.split("; ");
//
//			for (String ckitem : ckStrings) {
//				if (ckitem != null && !ckitem.equals(""))
//					ck.put(ckitem.split("=")[0], ckitem.split("=")[1]);
// 
//			}
		}

		List<Fund> funds = caiService.getIndexBtc();

		for (Fund fund : funds) {

			// 根据表名获取基金编码和价格范围minP,maxP,code
			BigDecimal minP = new BigDecimal("0");
			BigDecimal maxP = new BigDecimal("0");
//		for (String tableName : tableNames) {
			String tableName = fund.getPlantable();
			// System.out.println("开始处理：" + tableName);

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
				// System.out.println("------------------------" + fund.getName() +
				// "------------------------");
				// System.out.println(resultStringa);
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

				minP = fundInfo.getBuyprice().subtract(fundInfo.getWanggeprice());
				maxP = fundInfo.getSellprice();
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

					url = "https://www.okx.com" + "/api/v5/market/ticker?instId=" + fund.getCode();
//					url = "/api/v5/market/ticker?instId=" + fund.getCode();
					String gupiaoRes = null;
//					String gupiaoRes = cn.exrick.common.utils.HttpUtil.sendGetWithHeader(url, sinaheader);

					try {

						/*
						 * 
						 * Sec-Ch-Ua:"Not A(Brand";v="99", "Google Chrome";v="121", "Chromium";v="121"
						 * Sec-Ch-Ua-Mobile: ?0 Sec-Ch-Ua-Platform: "Windows" Sec-Fetch-Dest: empty
						 * Sec-Fetch-Mode: cors Sec-Fetch-Site: none User-Agent: Mozilla/5.0 (Windows NT
						 * 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0
						 * Safari/537.36
						 * 
						 * 
						 * 
						 */

						Map<String, String> headers = new HashMap<String, String>();

						headers.put("sec-ch-ua",
								"\"Not A(Brand\";v=\"99\", \"Google Chrome\";v=\"121\", \"Chromium\";v=\"121\"");
						headers.put("sec-ch-ua-mobile", "?0");
						headers.put("sec-ch-ua-platform", "\"Windows\"");
						headers.put("Sec-Fetch-Dest", "document");
						headers.put("Sec-Fetch-Mode", "navigate");
						headers.put("Sec-Fetch-Site", "none");
						headers.put("sec-ch-ua-full-version", "\"121.0.6167.140\"");
						headers.put("sec-ch-ua-arch", "\"x86\"");
						headers.put("Host", "www.okx.com");
						headers.put("sec-ch-ua-platform-version", "\"15.0.0\"");
						headers.put("sec-ch-ua-model", "\"\"");
						headers.put("sec-ch-ua-bitness", "\"64\"");

						headers.put("sec-ch-ua-full-version-list",
								"\"Not A(Brand\";v=\"99.0.0.0\", \"Google Chrome\";v=\"121.0.6167.140\", \"Chromium\";v=\"121.0.6167.140\"");
						headers.put("Upgrade-Insecure-Requests", "1");
						headers.put("User-Agent",
								"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.37");
						headers.put("Sec-Fetch-User", "?1");

						headers.put("Accept",
								"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
						headers.put("Accept-Language", "zh-CN,zh;q=0.9");
						headers.put("Accept-Encoding", "gzip, deflate, br, zstd");
						headers.put("Cache-Control", "max-age=0");

						System.out.println("------------cookie--------------");// .cookies(ck)
//						System.out.println(ck);
						try {
							HttpsUrlValidator.trustAllHttpsCertificates();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//						Thread.sleep(100);.cookies(ck)  .cookies(ck).proxy("localhost", 7890)
						Response response = Jsoup.connect(url).userAgent(
								"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.37")
								.headers(headers).ignoreContentType(true).timeout(5000).ignoreHttpErrors(true)
								.execute();
////
						// 在收到响应后，解析并存储 cookie
						if (!response.headers("Set-Cookie").isEmpty()) {
							List<String> cookies = response.headers("set-cookie");
							for (String resCookie : cookies) {
								String[] ckStringsRes = resCookie.split("; ");
								for (String ckitemRes : ckStringsRes) {
									if (ckitemRes != null && !ckitemRes.equals("")) {
										System.out.println("set-cookie:" + ckitemRes);
										if (ckitemRes.split("=").length > 1)
											ck.put(ckitemRes.split("=")[0], ckitemRes.split("=")[1]);
										break;
									}

								}
							}

						}

						gupiaoRes = response.body();

					}

					catch (Exception e) {
						// TODO Auto-generated catch block

//						try {
//							gupiaoRes = Jsoup.connect(url).userAgent(
//									"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36")
//									.proxy("localhost", 7890).get().outerHtml();
//							System.out.println(gupiaoRes);
//						} catch (IOException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
						try {
							cookieString = CommonUtils.getProperties("cookie");
							ck = new HashMap<String, String>();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						e.printStackTrace();
					}
					// okxService.trade(url, "GET", "");
					// cn.exrick.common.utils.HttpUtil.sendGetWithHeader(url, sinaheader);
//				    String proxyHost = "localhost"; // 代理服务器地址
//			        int proxyPort = 8080; // 代理服务器端口
//			 
//			        // 设置代理
//			         Proxy proxy = HttpConnection.Proxy.set(proxyHost, proxyPort);

//					if (gupiaoRes == null) {
//						System.out.println("=========[jsoup]网络异常，url:" + url);
//						alarmPlayer.playAlarm2(1);
//						continue;
//					}
					System.out.println("欧易服务器响应jsoup：");
					System.out.println(gupiaoRes);
					if (gupiaoRes == null || gupiaoRes.indexOf("Just a moment") != -1) {
						// 从redis提取行情：
						System.out.println("ip被限制");
//						String hangqing = jedisClient.get(fund.getCode().replace("-SWAP", "") + "_tickers");
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
						System.out.println("网络异常，url:" + url);
						// alarmPlayer.playAlarm2(1);
						continue;
					}

					try {
						JSONObject sinaResJsonObject = new JSONObject(gupiaoRes);
//						JSONObject okxJson = new JSONObject(gupiaoRes);
						JSONArray ja = sinaResJsonObject.getJSONArray("data");
						BigDecimal currentValue = ja.getJSONObject(0).getBigDecimal("bidPx");
						BigDecimal volBigDecimal = ja.getJSONObject(0).getBigDecimal("lastSz");
						sinaResJsonObject.put("gsz", currentValue);
						sinaResJsonObject.put("gszzl", "0");
						sinaResJsonObject.put("vol", volBigDecimal);
						resultString = sinaResJsonObject.toString();
					} catch (Exception e) {
						e.printStackTrace();
//						try {
//							cookieString = CommonUtils.getProperties("cookie");
//							ck = new HashMap<String, String>();
//						} catch (IOException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
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
					// alarmPlayer.playAlarm2(1);
					continue;
				} else {
					System.out.println("url:" + url);
					System.out.println(resultString);
					int index = 0; // 初始化索引计数器
					try {
						JSONObject jsonObject = new JSONObject(resultString.replace("jsonpgz(", "").replace(");", ""));
						BigDecimal jingzhi = jsonObject.getBigDecimal("gsz");
						BigDecimal vol = new BigDecimal("0");
						if (fund.getSinacode().indexOf("okx") != -1) {
							vol = jsonObject.getBigDecimal("vol");
						}

						String lastvalue = "0";
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

						// System.out.println("成功更新基金价格：" + fundInfo.getName());

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
						int fanxiang = 0;
						int fanxiangBigZhang = 0;
						Date nowDate = new Date();
						Comparator<Tuple> comparator = new Comparator<Tuple>() {

							@Override
							public int compare(Tuple o1, Tuple o2) {
								// TODO Auto-generated method stub
								return o1.getElement().compareTo(o2.getElement());
							}
						};
						List<Tuple> sortedList = new ArrayList<>(zsetKeysPriceFinal);
						Collections.sort(sortedList, comparator);
						BigDecimal jizhun = new BigDecimal("0.1");
						BigDecimal jizhunFanxiang = new BigDecimal("-0.1");

						for (int i = 0; i < sortedList.size(); i++) {

							Tuple mem = sortedList.get(i);
							double priceInfoString = mem.getScore();

							if (i >= 1) {
								Double previous = sortedList.get(i - 1).getScore();
								double priceChange = (priceInfoString - previous) / previous; // 计算价格变化百分比
								BigDecimal pricechaBigDecimal = new BigDecimal(priceChange)
										.multiply(new BigDecimal("100"));

								if (pricechaBigDecimal.compareTo(fundInfo.getSellpriceReal()) > 0) {
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
								if (pricechaBigDecimal.compareTo(jizhun) > 0) {
									bigZhang += 1;
								}
								if (pricechaBigDecimal.compareTo(jizhunFanxiang) < 0) {
									fanxiangBigZhang += 1;
								}

								if (pricechaBigDecimal.compareTo(fundInfo.getSellpriceReal()) < 0) {
									fanxiang += 1;
								}

							}

//							indexP += 1;

						}

						BigDecimal tbl = new BigDecimal(changeCount).divide(new BigDecimal(ybcount), 3,
								RoundingMode.DOWN);
						BigDecimal dbl = new BigDecimal(bigZhang).divide(new BigDecimal(ybcount), 3, RoundingMode.DOWN);
						BigDecimal fxdbl = new BigDecimal(fanxiangBigZhang).divide(new BigDecimal(ybcount), 3,
								RoundingMode.DOWN);

						BigDecimal fxl = new BigDecimal(fanxiang).divide(new BigDecimal(ybcount), 3, RoundingMode.DOWN);

//						fundInfo.setUpcount(changeCount + "/" + ybcount + "=" + tbl);

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
//						System.out.println("长线方向：" + sartagLong);&& sartag == 1 && sartagLong == 1 && sartag == 1 && sartagLong == 1

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
								System.out.println("=====api调用时，ip被限制=========");
								// alarmPlayer.playAlarm2(1);
								continue;
//
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
						int cbt = currentbuytag;
//						if (currentbuytag == 1 && !jedisClient.exists("cbt_" + fund.getCode())) {
//							jedisClient.setex("cbt_" + fund.getCode(), 60, currentbuytag + "");
//							cbt = 1;
//						}

						int tempbuyTagPeriod = buyTagMap.get("tempbuytag");
						if (tempbuyTagPeriod != 0)
							jedisClient.set("period_" + fund.getCode(), tempbuyTagPeriod + "");

						Integer chajufangxiangInteger = buyTagMap.get("chajufangxiang");

						String buyTagPeriodStr = jedisClient.get("period_" + fund.getCode());
						if (buyTagPeriodStr == null) {
							buyTagPeriodStr = "0";
						}
						// && buyTagPeriod == 1
						if (buyTagPeriod == 1 && chajufangxiangInteger == 1) {
//							if (tbl.compareTo(new BigDecimal("0.5")) > 0) {
//								buyTag = 1;
//							} else if (tbl.compareTo(fxl) > 0 && tbl.compareTo(new BigDecimal("0.4")) >= 0
//									&& fxl.compareTo(new BigDecimal("0.1")) <= 0) {
//
//								// && bigZhang >= 1 &&
//								// fanxiangBigZhang == 0
//								// &&
//								buyTag = 1;
//							}
							buyTag = 1;
						}

						if (buyTag == 1) {
//							BigDecimal changeBigDecimal = new BigDecimal(changeCount + "");
//							BigDecimal allCountBigDecimal = new BigDecimal(sortedList.size() + "");
//							BigDecimal biliBigDecimal = changeBigDecimal.divide(allCountBigDecimal, 3,
//									RoundingMode.DOWN);

							try {
								FileUtil.appendUtf8String(dFormat.format(nowDate) + ":【突发上涨】 记录数：" + changeCount
										+ " 总数：" + sortedList.size() + " 突变占比：" + tbl + "反向占比: " + fxl + " 大单数: "
										+ bigZhang + " table:" + tableName + " level:" + fundInfo.getLevel() + "  现价:"
										+ jingzhi.toString() + " 买入价："

										+ fundInfo.getBuypriceReal() + " 成交量：" + sumVol

										+ "\n----\n", "d:\\突发上涨日志.txt");
							} catch (Exception e) {
								e.printStackTrace();

							}
//							if (changeCount >= 2)
//								buyTag = 1;  && sartag == -1 && sartagLong == -1
						} else {
							// && buyTagPeriod == -1

							if (buyTagPeriod == -1 && chajufangxiangInteger == 1) {
//								if (fxl.compareTo(new BigDecimal("0.5")) > 0) {
//									buyTag = -1;
//								} else if (fxl.compareTo(tbl) > 0 && fxl.compareTo(new BigDecimal("0.4")) >= 0
//										&& tbl.compareTo(new BigDecimal("0.1")) <= 0) {
//									//
//
//									// && fanxiangBigZhang >= 1
//									// && bigZhang == 0 &&
//									buyTag = -1;
//								}
								buyTag = -1;
							}

						}

						// 修改为连续10次符合条件设置为-1
						jedisClient.lpush("buytag_" + fund.getCode(), buyTag + "");
						jedisClient.lpush("buytag30_" + fund.getCode(), buyTag + "");
						// 只保留最新推入的10个元素，删除其他元素
						jedisClient.ltrim("buytag_" + fund.getCode(), 0, 100);
						jedisClient.ltrim("buytag30_" + fund.getCode(), 0, 30);

						// 获取最新推入的10个元素
						int realBuyTag = 0;
//						int fxRealBuyTag = 0;
						String buyTagLiString = "";
						List<String> buytagHistory = jedisClient.lrange("buytag_" + fund.getCode(), 0, -1);
						int idx = 0;
						int okCount = 0;
						int fanxiangOkCount = 0;

						for (String item : buytagHistory) {

//							if (item.equals("0") || item.equals("-1")) {
//								realBuyTag = 0;
//
//							}
//							if (item.equals("0") || item.equals("1")) {
//								fxRealBuyTag = 0;
//
//							}
							if (item.equals("1"))
								okCount += 1;
							if (item.equals("-1"))
								fanxiangOkCount += 1;

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
								okCount2 += 1;

							}
							if (item.equals("-1")) {
								fanxiangOkCount2 += 1;

							}

							idx += 1;

						}

//						float bili = okCount / buytagHistory.size();
						BigDecimal bili = new BigDecimal(okCount).divide(new BigDecimal(buytagHistory.size()), 2,
								RoundingMode.DOWN);
						BigDecimal bili2 = new BigDecimal(fanxiangOkCount).divide(new BigDecimal(buytagHistory.size()),
								2, RoundingMode.DOWN);

						BigDecimal bili3 = new BigDecimal(okCount2).divide(new BigDecimal(buytagHistory30.size()), 2,
								RoundingMode.DOWN);
						BigDecimal bili4 = new BigDecimal(fanxiangOkCount2)
								.divide(new BigDecimal(buytagHistory30.size()), 2, RoundingMode.DOWN);

						// fundInfo.setUpcount("↑ " + tbl + " , " + fxl + " ; " + dbl + " , " + fxdbl +
						// " ; "
						// + buyTagLiString + "," + bili3 + "," + bili + "," + bili4 + "," + bili2);
						// bugtag 为-1的时候需要修改方向，假如 目前没有持仓：
						if (bili.compareTo(new BigDecimal("0.7")) > 0 && buytagHistory.get(0).equals("1")
								&& bili3.compareTo(new BigDecimal("0.99")) > 0) {
							realBuyTag = 1;
						} else if (bili2.compareTo(new BigDecimal("0.7")) > 0 && buytagHistory.get(0).equals("-1")
								&& bili4.compareTo(new BigDecimal("0.99")) > 0) {
							realBuyTag = -1;
						} else {
							realBuyTag = 0;
						}
						fundInfo.setBuytag(buyTag);// realBuyTag
						System.out.println("bugtag:" + buyTag);

						// 探测止盈情况09
						List<Fund1Gaoduanzhuangbei2Ok> cangweis = caiService.getCangweisByTableName(tableName);
						System.out.println("[DEBUG] tableName=" + tableName + ", cangweis.size=" + cangweis.size());

//						int cate = buyTagMap.get("cate"); cate == 0
						if (cangweis.size() == 0 && buyTag == -1) {//
//							fund.setCate("1");
//							caiService.updateFund(fund);
//							continue;

						}
						if (cangweis.size() > 0 && (buyTag == -1)) {
							String tipsVar = "";
//							alarmPlayer.playAlarm(1);
//							if (buyTag == -1) {
//								System.out.println("需要平仓");
//								tipsVar = "需要平仓";
//							}
//							if (fxRealBuyTag == -1) {
							System.out.println("需要立即平仓");
							tipsVar = "需要立即平仓";
							System.out.println("buytag:" + fundInfo.getBuytag());
//							}
							// fundInfo.getUpcount() +
							// fundInfo.setUpcount(tipsVar);

						}
//						if (cangweis.size() > 0 && cangweis.get(0).getLevel() < fundInfo.getLevel()) {
//							fundInfo.setBuytag(0);
//						}
						if (cangweis.size() > 0)
							lastvalue = cangweis.get(0).getBuypriceReal().toString();
						int clearMax = 1;
//						if (alarmtag == 0)
						int zyzstag = 0;
						Fund1Gaoduanzhuangbei2Ok cwLast = null;
						Fund1Gaoduanzhuangbei2Ok cwFirst = null;
						BigDecimal sumYl = new BigDecimal("0");
						BigDecimal sumKuisun = new BigDecimal("0");
						BigDecimal sumPrice = new BigDecimal("0");
						BigDecimal sumFene = new BigDecimal("0");

						String keyString = "?instType=SWAP" + "&instId=" + fund.getCode();

						String ykp = okxService.trade("/api/v5/account/positions" + keyString, "GET", "");
						System.out.println("[OKX持仓响应] " + fund.getCode() + ": " + ykp);
						BigDecimal ykPrice = new BigDecimal("0");
						try {
							if (ykp == null || ykp.trim().isEmpty()) {
								System.out.println("[OKX持仓] 返回为空: " + fund.getCode());
								continue;
							}
							JSONObject ykpJsonObject = new JSONObject(ykp);
							JSONArray dts = ykpJsonObject.getJSONArray("data");
							if (dts == null || dts.isEmpty()) {
								System.out.println("[OKX持仓] data为空: " + fund.getCode() + " 返回: " + ykp);
								continue;
							}
							for (int m = 0; m < dts.size(); m++) {
								JSONObject dt = dts.getJSONObject(m);
								if (dt == null) {
									continue;
								}
								String posSide = dt.getStr("posSide");
								if (posSide != null && posSide.contentEquals("long")) {
									ykPrice = dt.getBigDecimal("bePx");
									break;
								}

							}

						} catch (Exception e) {
							e.printStackTrace();
							continue;
							// TODO: handle exception
						}

						// ========== 分批止盈逻辑 ==========
						// 检查是否达到盈亏平衡价全平（立即卖出，不移动止盈）
						boolean shouldCloseAll = false;
						System.out.println("【全平检查】" + fund.getCode() + " ykPrice=" + ykPrice + ", jingzhi=" + jingzhi);
						if (ykPrice != null && ykPrice.compareTo(BigDecimal.ZERO) > 0
								&& jingzhi.compareTo(BigDecimal.ZERO) > 0) {
							// 盈亏平衡价 * 1.002 作为全平触发价（0.2%利润）
							BigDecimal closeAllPrice = ykPrice.multiply(new BigDecimal("1.002"));

							if (jingzhi.compareTo(closeAllPrice) >= 0) {
								// 达到利润目标，立即全平（不跟踪，不回撤）
								shouldCloseAll = true;
								System.out.println(
										"【底仓全平触发】当前价" + jingzhi + " >= 盈亏平衡价+0.2%(" + closeAllPrice + ")，立即全平");

								// 清理可能存在的旧移动止盈状态
								jedisClient.del("closeall:highest:" + fund.getCode());
								jedisClient.del("closeall:activated:" + fund.getCode());
							}
						}

						if (shouldCloseAll) {
							// 【全平逻辑】使用OKX一键平仓接口（不依赖数据库fene）
							System.out.println("【全平触发】使用OKX close-position接口一键全平");

							// 1. 【合约全平】先平掉OKX所有仓位（底仓+T仓），然后直接清理T仓Redis记录
							// OKX close-position 会平掉该品种所有仓位，不需要逐个平仓
							dailyProfitTManager.clearAllPositionsRedis(fund.getCode());
							System.out.println("【全平】已清理T仓位Redis记录");

							// 2. 【重要】设置T交易暂停标记，防止全平后立即重新开仓（暂停5分钟）
							jedisClient.setex("t:pause:" + fund.getCode(), 300, "1");
							System.out.println("【全平】已暂停T交易5分钟，防止立即重新开仓");

							// 3. 清理分批止盈Redis记录
							for (Fund1Gaoduanzhuangbei2Ok cwClean : cangweis) {
								String levelStr = String.valueOf(cwClean.getLevel());
								jedisClient.del("batch:sold:" + tableName + ":" + levelStr);
								jedisClient.del("batch:tp:" + tableName + ":" + levelStr);
								jedisClient.del("highest:" + tableName + ":" + levelStr);
								jedisClient.del("buyprice:" + tableName + ":" + levelStr);
							}
							System.out.println("【全平】清理所有分批止盈Redis记录");

							// 3. 标记所有底仓清仓（Service层会用OKX接口全平，不依赖fene计算）
							// 只取第一个档位调用updatezhiying（OKX一键平仓会平掉所有仓位）
							System.out.println("【全平诊断】cangweis.size=" + (cangweis == null ? "null" : cangweis.size())
									+ " for " + fund.getCode());
							if (cangweis != null && !cangweis.isEmpty()) {
								Fund1Gaoduanzhuangbei2Ok cwMain = cangweis.get(0);
								System.out.println("【全平诊断】cwMain.level=" + cwMain.getLevel() + ", id=" + cwMain.getId()
										+ ", fene=" + cwMain.getFene());
								cwMain.setComment("全平平仓：OKX一键全平");
								cwMain.setZhiying(BigDecimal.ZERO);
								// 清仓时重置最高价/最低价
								cwMain.setMaxprice5(BigDecimal.ZERO);
								cwMain.setMinprice5(BigDecimal.ZERO);
								cwMain.setMaxpriceniu(BigDecimal.ZERO);
								cwMain.setMaxzhangfu5(BigDecimal.ZERO);
								cwMain.setMaxdiefu5(BigDecimal.ZERO);
								cwMain.setFene(BigDecimal.ZERO); // 清空fene
								System.out.println("【全平诊断】准备调用updatezhiying，comment=" + cwMain.getComment()
										+ ", tableName=" + tableName);
								try {
									caiService.updatezhiying(cwMain, tableName, cangweis, fund);
									System.out.println("【全平诊断】updatezhiying调用完成");
								} catch (Exception e) {
									System.err.println("【全平诊断】updatezhiying调用异常：" + e.getMessage());
									e.printStackTrace();
								}

								// 【已移至Service层事务】其他档位数据库更新由 updatezhiying 在事务中统一处理
							}

							alarmtag = 5;
							zyzstag = 1;
							continue; // 跳过正常止盈检查
						}
						// ========== 分批止盈逻辑结束 ==========

						// 【公共】计算ATR（供底仓止盈和T交易共用）
						BigDecimal atrPercent = null;
						List<cn.exrick.manager.service.util.Candle> candles = null;
						try {
							// 获取15根1分钟K线（用于计算14周期ATR）
							String klineUrl = "https://www.okx.com/api/v5/market/candles?instId=" + fund.getCode()
									+ "&bar=1m&limit=15";
							System.out.println("【诊断】开始获取K线: " + fund.getCode());
							candles = cn.exrick.manager.service.util.okx.getline(klineUrl);
							System.out.println("【诊断】K线获取结果: " + fund.getCode() + " candles="
									+ (candles == null ? "null" : candles.size()));
							if (candles != null && candles.size() >= 15) {
								atrPercent = dailyProfitTManager.calculateATRFromKlines(candles, jingzhi);

								// ===== 计算RSI并写入Redis =====
								try {
									BigDecimal rsiValue = calculateRSIFromCandles(candles);
									if (jedisClient == null) {
										System.out.println("【诊断】jedisClient 为 null!");
									}
									System.out.println("【诊断】准备写入Redis: " + fund.getCode() + "=" + rsiValue);
									String result = jedisClient.setex("rsi:" + fund.getCode(), 60, rsiValue.toString());
									System.out.println("【诊断】Redis setex 结果: " + result);
									System.out.println("【诊断】Redis写入完成: " + fund.getCode());
									System.out.println("【RSI计算】" + fund.getCode() + "=" + rsiValue);
								} catch (Exception rsiEx) {
									System.out.println("【RSI计算】失败: " + rsiEx.getMessage());
									rsiEx.printStackTrace();
								}
							} else {
								System.out.println("【诊断】K线数量不足15根: " + fund.getCode() + " size="
										+ (candles == null ? "null" : candles.size()));
							}
						} catch (Exception e) {
							System.out.println("【诊断】K线获取异常: " + fund.getCode() + " " + e.getMessage());
							e.printStackTrace();
						}
						// 备用：使用tick数据
						if (atrPercent == null) {
							dailyProfitTManager.updatePrice(fund.getCode(), jingzhi, jingzhi, jingzhi);
							atrPercent = dailyProfitTManager.getATRPercent(fund.getCode(), jingzhi);
						}

						if (atrPercent == null) {
							atrPercent = new BigDecimal("0.005"); // 默认0.5%
							System.out.println("【ATR计算】数据不足，使用默认值0.5% for " + fund.getCode());
						} else {
							System.out.println("【ATR计算】K线ATR="
									+ atrPercent.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP)
									+ "% for " + fund.getCode());
						}

						// ========== T交易逻辑 ==========

						// 1. 检查每日状态
						DailyProfitTManager.DailyState state = dailyProfitTManager.getDailyState(fund.getCode());
						if (!state.canTrade()) {
							System.out.println("【T状态】" + fund.getCode() + " " + state.getStatus());
						} else {
							// 2. 计算入场评分
							String trend = "sideway";

							// ===== 获取RSI =====
							BigDecimal rsi = getRSIFromRedis(fund.getCode());
							if (rsi == null) {
								rsi = new BigDecimal("50"); // 默认中性
							}
							System.out.println("【T交易】RSI=" + rsi + " for " + fund.getCode());

							// ===== 计算量比（当前成交量/20周期均量）=====
							BigDecimal volumeRatio = new BigDecimal("1.0"); // 默认中性
							try {
								Set<String> volMembers = jedisClient.zrange("vol_" + fund.getCode());
								if (volMembers != null && volMembers.size() >= 10) {
									List<BigDecimal> volList = new ArrayList<>();
									for (String mem : volMembers) {
										// 解析score（成交量值）
										Double score = jedisClient.zscore("vol_" + fund.getCode(), mem);
										if (score != null) {
											volList.add(new BigDecimal(score.toString()));
										}
									}
									if (volList.size() >= 10) {
										// 当前量是最后一个
										BigDecimal currentVol = volList.get(volList.size() - 1);
										// 计算前N-1个的平均
										BigDecimal sumVolLocal = BigDecimal.ZERO;
										for (int i = 0; i < volList.size() - 1; i++) {
											sumVolLocal = sumVolLocal.add(volList.get(i));
										}
										BigDecimal avgVol = sumVolLocal.divide(new BigDecimal(volList.size() - 1), 4,
												RoundingMode.HALF_UP);
										if (avgVol.compareTo(BigDecimal.ZERO) > 0) {
											volumeRatio = currentVol.divide(avgVol, 2, RoundingMode.HALF_UP);
										}
									}
								}
							} catch (Exception e) {
								System.out.println("【T交易】量比计算失败: " + e.getMessage());
							}
							System.out.println("【T交易】量比=" + volumeRatio + " for " + fund.getCode());
							// 设置盈亏平衡价（使用OKX返回的bePx盈亏平衡价）
							if (ykPrice != null && ykPrice.compareTo(BigDecimal.ZERO) > 0) {
								dailyProfitTManager.setBreakevenPrice(fund.getCode(), ykPrice);
								System.out.println("【T交易】盈亏平衡价（OKX）: " + ykPrice + " for " + fund.getCode());
							} else {
								System.out.println("【T交易】警告：OKX盈亏平衡价无效，T交易可能偏离成本");
							}

							DailyProfitTManager.TradeScore score = dailyProfitTManager
									.calculateTradeScore(fund.getCode(), jingzhi, rsi, atrPercent, trend, volumeRatio);

							// 3. 检查是否可以开仓（传入fund.money作为张数）
							// 【修改】T仓位买入量：DOGE=0.02张，XAUT=2张（与底仓区分）
							BigDecimal zhang;
//							if (fund.getMoney() != null && fund.getMoney().compareTo(BigDecimal.ZERO) > 0) {
//								zhang = fund.getMoney();
//							} else {
							// 默认T仓位买入量（与底仓不同）
							if (fund.getCode().contains("DOGE")) {
								zhang = new BigDecimal("0.02"); // DOGE每次0.02张
							} else if (fund.getCode().contains("XAU")) {
								zhang = new BigDecimal("2"); // XAUT每次2张
							} else {
								zhang = new BigDecimal("0.05"); // 其他默认0.05张
							}
//							}

							DailyProfitTManager.CanTradeResult canTrade = dailyProfitTManager.canOpen(fund.getCode(),
									jingzhi, zhang, atrPercent);
							if (canTrade.allowed && score.passed) {
								System.out.println("开始下单t单");
								// 【先下单到OKX，成功后再记录Redis】避免状态不一致
								String posId = "T" + (System.currentTimeMillis() % 100000);
								boolean orderSuccess = false;

								try {
									// 1. 创建临时档位对象
									Fund1Gaoduanzhuangbei2Ok cwTemp = new Fund1Gaoduanzhuangbei2Ok();
									int tId = -1 * (posId.hashCode() % 100000);
									cwTemp.setId(tId);
									int uniqueLevel = 900 + (posId.hashCode() % 100);
									cwTemp.setLevel(9999999);
									cwTemp.setCode(fund.getCode());
									cwTemp.setName(fund.getName() + "_bs"); // 加bs标记，避免被拦截
									cwTemp.setFene(canTrade.zhang.multiply(fund.getHuiche()));
									cwTemp.setBuypriceReal(jingzhi);
									cwTemp.setBuyprice(jingzhi);
									cwTemp.setCurrentprice(jingzhi);
									cwTemp.setDt(new Date());
									cwTemp.setIscurrent(0);
									cwTemp.setComment("T交易开仓：" + posId);
									cwTemp.setZhisun(0);
									cwTemp.setWangge(BigDecimal.ZERO);
									cwTemp.setZhiying(BigDecimal.ZERO);

									// 2. 调用底仓接口下单（alarmtag=2表示买入）
									int re = caiService.updateCurrentPrice(tableName, jingzhi, 2, jingzhi,
											canTrade.zhang, fund, cwTemp, lastvalue);
									if (re == 1) {

										orderSuccess = true;
										System.out
												.println("【T开仓-下单成功】" + posId + " " + canTrade.zhang + "张 @" + jingzhi);

									}
								} catch (Exception e) {
									System.err.println("【T开仓00000-下单失败】" + e.getMessage());
									e.printStackTrace();
								}

								// 3. 下单成功后，查询posId并记录到Redis
								if (orderSuccess) {
									DailyProfitTManager.TPosition pos = dailyProfitTManager.openPosition(fund.getCode(),
											jingzhi, score, canTrade.zhang, atrPercent);
									if (pos != null) {
										System.out.println("【T开仓】Redis记录成功 " + pos.id + " @" + jingzhi + " 张数="
												+ pos.zhang + " 评分=" + score.totalScore);

										// 记录张数到Redis
										String posKey = "t:position:" + fund.getCode() + ":" + pos.id;
										jedisClient.setex(posKey + ":zhang", 86400, canTrade.zhang.toString());

										// 【双向持仓】查询并保存OKX posId
//										try {
//											// 使用下单时的clOrdId查询订单获取posId
//											String okxPosId = queryOkxPosId(fund.getCode(), posId);
//											if (okxPosId != null) {
//												jedisClient.setex(posKey + ":okxPosId", 86400, okxPosId);
//												System.out.println("【T开仓】保存OKX posId: " + okxPosId);
//											}
//										} catch (Exception e) {
//											System.err.println("【T开仓】查询posId失败: " + e.getMessage());
//										}
									}
								} else {
									System.out.println("【T000开仓】因下单失败，不记录Redis仓位");
								}

							} else if (!canTrade.allowed && score.passed) {
								System.out.println("【T不可开】" + fund.getCode() + " " + canTrade.reason);
							}

							// 4. 检查退出信号（先下单OKX，成功后再清Redis）
							List<DailyProfitTManager.TExitSignal> exitSignals = dailyProfitTManager
									.checkExits(fund.getCode(), jingzhi);
							for (DailyProfitTManager.TExitSignal signal : exitSignals) {

								// 【先下单到OKX，成功后再清Redis】避免状态不一致
								String posKey2 = "t:position:" + fund.getCode() + ":" + signal.positionId;
								String zhangStr2 = jedisClient.get(posKey2 + ":zhang");

								if (zhangStr2 == null) {
									System.err.println("【T平仓】无法找到仓位张数，跳过：" + signal.positionId);
									continue;
								}

								BigDecimal closeZhang = new BigDecimal(zhangStr2);
								boolean closeSuccess = false;

								try {
									// 1. 创建临时档位对象
									Fund1Gaoduanzhuangbei2Ok cwTemp2 = new Fund1Gaoduanzhuangbei2Ok();
									int tId2 = -1 * (signal.positionId.hashCode() % 100000);
									cwTemp2.setId(tId2);
									int uniqueLevel2 = 900 + (signal.positionId.hashCode() % 100);
									cwTemp2.setLevel(9999999);
									cwTemp2.setCode(fund.getCode());
									cwTemp2.setName(fund.getName() + "_bs"); // 加bs标记，避免被拦截
									cwTemp2.setFene(closeZhang.multiply(fund.getHuiche()));
									cwTemp2.setBuypriceReal(signal.price);
									cwTemp2.setBuyprice(signal.price);
									cwTemp2.setCurrentprice(signal.price);
									cwTemp2.setDt(new Date());
									cwTemp2.setIscurrent(0);
									cwTemp2.setComment("T交易平仓：" + signal.type + " " + signal.reason);
									cwTemp2.setZhisun(0);
									cwTemp2.setWangge(BigDecimal.ZERO);
									cwTemp2.setZhiying(BigDecimal.ZERO);

//									// 2. 获取保存的posId（双向持仓需要）
//									String closePosId = jedisClient.get(posKey2 + ":okxPosId");
//									String lastValueWithPosId = "T" + signal.positionId;
//									if (closePosId != null && !closePosId.isEmpty()) {
//										lastValueWithPosId = "T" + signal.positionId + ":" + closePosId;
//										cwTemp2.setFirsttime(closePosId); // 【关键】设置posId到firsttime字段，供卖出时使用
//										System.out.println("【T平仓】使用posId: " + closePosId);
//									}

									// 调用底仓接口下单（alarmtag=3表示卖出）
									int re = caiService.updateCurrentPrice(tableName, signal.price, 3, signal.price,
											closeZhang, fund, cwTemp2, lastvalue);
									if (re == 1) {
										closeSuccess = true;
										System.out.println("【T平仓-下单成功】" + signal.positionId + " " + closeZhang + "张 @"
												+ signal.price);
									}
								} catch (Exception e) {
									System.err.println("【T平仓-下单失败】" + signal.positionId + "：" + e.getMessage());
									e.printStackTrace();
								}

								// 3. 下单成功后，才清理Redis和更新状态
								if (closeSuccess) {
									dailyProfitTManager.closePosition(fund.getCode(), signal, jingzhi);
									jedisClient.del(posKey2 + ":zhang");
									System.out.println("【T平仓】Redis清理成功：" + signal.positionId);
								} else {
									System.out.println("【T平仓】因下单失败，保留Redis仓位：" + signal.positionId);
								}
							}

							// 5. 日终强制平仓检查（每天23:55）
							dailyProfitTManager.checkDailyForceClose(fund.getCode(), jingzhi);

							// 6. 每30秒输出一次T仓位报告（周期性自动打印）
							String reportKey = "t:report:last:" + fund.getCode();
							String lastReport = jedisClient.get(reportKey);
							long now = System.currentTimeMillis();
							if (lastReport == null || now - Long.parseLong(lastReport) > 30000) {
								String report = dailyProfitTManager.getPositionReport(fund.getCode());
								System.out.println(report);

								// 【可选】同时输出到文件，方便查看历史
								// try {
								// FileUtil.appendUtf8String(report, "d:\\T仓位报告_" + fund.getCode() + ".txt");
								// } catch (Exception e) {
								// // 忽略文件写入错误
								// }

								jedisClient.setex(reportKey, 60, String.valueOf(now));
							}

							// 7. 每5分钟输出一次全局资金报告
							if (now % (5 * 60 * 1000) < 3000) {
								System.out.println(dailyProfitTManager.getGlobalMarginReport());
							}
						}
						// ========== T交易逻辑结束 ==========

						// 初始化对冲列表
						List<Fund1Gaoduanzhuangbei2Ok> duichongList = new ArrayList<>();

						for (Fund1Gaoduanzhuangbei2Ok cw : cangweis) {
							index = index + 1;

							// 先计算利润（供分批止盈使用）
							BigDecimal dxzzlCw = BigDecimal.ZERO;
							if (cw.getBuypriceReal() != null && cw.getBuypriceReal().compareTo(BigDecimal.ZERO) > 0) {
								BigDecimal chajuCw = jingzhi.subtract(cw.getBuypriceReal());
								dxzzlCw = chajuCw.divide(cw.getBuypriceReal(), 4, RoundingMode.DOWN)
										.multiply(new BigDecimal("100"));
							}

							// 【计算止盈价】根据RSI和ATR计算Tier1止盈价格
							BigDecimal zhiyingPrice = BigDecimal.ZERO;
							if (cw.getBuypriceReal() != null && cw.getBuypriceReal().compareTo(BigDecimal.ZERO) > 0) {
								BigDecimal rsi = getRSIFromRedis(fund.getCode());
								int trend = rsi.compareTo(new BigDecimal("60")) > 0 ? 2
										: rsi.compareTo(new BigDecimal("40")) > 0 ? 1 : 0;

								// ATR自适应乘数
								BigDecimal atrMultiplier = new BigDecimal("1.0");
								if (atrPercent != null && atrPercent.compareTo(BigDecimal.ZERO) > 0) {
									BigDecimal atrPctValue = atrPercent.multiply(new BigDecimal("100"));
									if (atrPctValue.compareTo(new BigDecimal("0.3")) < 0) {
										atrMultiplier = new BigDecimal("0.5");
									} else if (atrPctValue.compareTo(new BigDecimal("0.8")) > 0) {
										atrMultiplier = new BigDecimal("1.5");
									}
								}

								// 基础止盈比例
								BigDecimal baseTp1 = trend == 2 ? new BigDecimal("8")
										: trend == 1 ? new BigDecimal("5") : new BigDecimal("3");
								BigDecimal tp1 = baseTp1.multiply(atrMultiplier);

								// 计算Tier1止盈价
								zhiyingPrice = cw.getBuypriceReal().multiply(new BigDecimal("1")
										.add(tp1.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)));
							}

							// ========== 分批止盈检查（ATR自适应）==========
							int batchTpResult = checkBatchTakeProfit(cw, jingzhi, fund, tableName, atrPercent);
							if (batchTpResult > 0) {
								alarmtag = 5;
								zyzstag = 1;

								// 获取原始总张数和已卖出张数
								BigDecimal originalFene = cw.getFene() != null ? cw.getFene() : fund.getMoney();
								if (originalFene == null)
									originalFene = new BigDecimal("0.03"); // 默认0.03张

								// 从Redis读取已卖出张数
								String soldKey = "batch:sold:" + tableName + ":" + cw.getLevel();
								String soldStr = jedisClient.get(soldKey);
								BigDecimal soldFene = soldStr == null ? BigDecimal.ZERO : new BigDecimal(soldStr);

								// 计算剩余张数
								BigDecimal remainingFene = originalFene.subtract(soldFene);
								if (remainingFene.compareTo(new BigDecimal("0.01")) < 0) {
									System.out.println("【分批止盈】剩余张数不足0.01，跳过批次=" + batchTpResult);
									break;
								}

								BigDecimal sellFene;
								if (batchTpResult == 1) {
									// 第一批：卖出剩余张数的1/3（最小0.01张）
									sellFene = remainingFene.divide(new BigDecimal("3"), 2, RoundingMode.DOWN);
									if (sellFene.compareTo(new BigDecimal("0.01")) < 0)
										sellFene = new BigDecimal("0.01");
									if (sellFene.compareTo(remainingFene) > 0)
										sellFene = remainingFene;
									cw.setComment("第一批ATR止盈卖" + sellFene + "张，剩" + remainingFene.subtract(sellFene)
											+ "张：" + dxzzlCw);
								} else if (batchTpResult == 2) {
									// 第二批：卖出剩余张数的1/2（最小0.01张）
									sellFene = remainingFene.divide(new BigDecimal("2"), 2, RoundingMode.DOWN);
									if (sellFene.compareTo(new BigDecimal("0.01")) < 0)
										sellFene = new BigDecimal("0.01");
									if (sellFene.compareTo(remainingFene) > 0)
										sellFene = remainingFene;
									cw.setComment("第二批ATR止盈卖" + sellFene + "张，剩" + remainingFene.subtract(sellFene)
											+ "张：" + dxzzlCw);
								} else if (batchTpResult == 4) {
									// 保本止损：卖出全部剩余
									sellFene = remainingFene;
									cw.setComment("保本止损清仓" + sellFene + "张（第一批已卖" + soldFene + "张）：" + dxzzlCw);
									System.out.println("【保本止损】触发！第一批已卖" + soldFene + "张，现清仓剩余" + sellFene + "张");
								} else if (batchTpResult == 5) {
									// 开仓止损：卖出全部
									sellFene = remainingFene;
									cw.setComment("开仓止损清仓" + sellFene + "张（未触发止盈）：" + dxzzlCw);
									System.out.println("【开仓止损】触发！未达第一批止盈，现止损清仓" + sellFene + "张");
								} else {
									// 第三批：卖出剩余全部
									sellFene = remainingFene;
									cw.setComment("第三批ATR移动止盈清仓" + sellFene + "张：" + dxzzlCw);
								}

								// 更新已卖出张数到Redis
								BigDecimal newSoldFene = soldFene.add(sellFene);
								jedisClient.setex(soldKey, 86400 * 30, newSoldFene.toString());

								// 设置本次卖出张数
								cw.setFene(sellFene);
								System.out.println("【分批止盈】批次=" + batchTpResult + " 原始=" + originalFene + "张 已卖="
										+ soldFene + "张 剩余=" + remainingFene + "张 本次卖=" + sellFene + "张");

								try {
									caiService.updatezhiying(cw, tableName, duichongList, fund);
								} catch (Exception e) {
									e.printStackTrace();
								}

								// 清仓后清理所有Redis记录（包含止盈3/4/5）
								if (batchTpResult == 3 || batchTpResult == 4 || batchTpResult == 5
										|| remainingFene.subtract(sellFene).compareTo(new BigDecimal("0.01")) < 0) {
									jedisClient.del(soldKey);
									jedisClient.del("batch:tp:" + tableName + ":" + cw.getLevel());
									jedisClient.del("highest:" + tableName + ":" + cw.getLevel());
									jedisClient.del("buyprice:" + tableName + ":" + cw.getLevel());
									String stopType = batchTpResult == 4 ? "保本止损"
											: (batchTpResult == 5 ? "开仓止损" : "止盈");
									System.out.println("【" + stopType + "】清仓完成，清理所有Redis记录");

									// 【修复】清仓时重置数据库中的最高价/最低价
									try {
										Fund1Gaoduanzhuangbei2OkExample example = new Fund1Gaoduanzhuangbei2OkExample();
										example.createCriteria().andIdEqualTo(cw.getId());
										example.setTableName(tableName);

										Fund1Gaoduanzhuangbei2Ok record = new Fund1Gaoduanzhuangbei2Ok();
										record.setMaxprice5(BigDecimal.ZERO);
										record.setMinprice5(BigDecimal.ZERO);
										record.setMaxpriceniu(BigDecimal.ZERO);
										record.setMaxzhangfu5(BigDecimal.ZERO);
										record.setMaxdiefu5(BigDecimal.ZERO);
										record.setZhiying(BigDecimal.ZERO);

										fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record, example);
										System.out.println("【" + stopType + "】重置最高价/最低价完成，level=" + cw.getLevel());
									} catch (Exception e) {
										System.err.println("【清仓重置高低价失败】" + tableName + " level=" + cw.getLevel() + ": "
												+ e.getMessage());
									}
								}

								break; // 一次只处理一批
							}
							// ========== 分批止盈检查结束 ==========

							BigDecimal maxValueLine = new BigDecimal("0");
//								String linenameString=tableName+fundInfo.getLevel();

							if (!jedisClient.exists("maxvalue_" + tableName + cw.getLevel())) {
								jedisClient.set("maxvalue_" + tableName + cw.getLevel(), jingzhi.toString());
								maxValueLine = new BigDecimal(jingzhi.toString());
							} else {
								maxValueLine = new BigDecimal(jedisClient.get("maxvalue_" + tableName + cw.getLevel()));
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

//							if (buyTagPeriodStr.equals("1") && chajufangxiangInteger == 1) {
							if (fund.getWendu() != null && fund.getWendu().compareTo(new BigDecimal("3")) != 0
									&& fund.getWendu().compareTo(new BigDecimal("4")) != 0
									&& fund.getWendu().compareTo(new BigDecimal("5")) != 0
									&& fund.getWendu().compareTo(new BigDecimal("6")) != 0) {// 产生了趋势1；
								// 横盘微弱波动0
								if (buyTagPeriodStr.equals("1") && chajufangxiangInteger == 1) {
//									if (cw.getZhiying().compareTo(catePriceItem) <= 0)
//									cw.setZhiying(new BigDecimal("0"));
								}
								if (buyTagPeriodStr.equals("-1") && chajufangxiangInteger == -1) {
//									if (cw.getZhiying().compareTo(catePriceItem) <= 0)
//									cw.setZhiying(new BigDecimal("0"));

								}
							}

							cw.setCurrentprice(jingzhi);
							cw.setYangbencount(ybcount.intValue());
							cw.setComment(dxzzlCw.toString());

							// 【修复】更新comment、止盈价、最高价/最低价到数据库
							try {
								Fund1Gaoduanzhuangbei2OkExample example = new Fund1Gaoduanzhuangbei2OkExample();
								example.createCriteria().andIdEqualTo(cw.getId());
								example.setTableName(tableName);

								Fund1Gaoduanzhuangbei2Ok record = new Fund1Gaoduanzhuangbei2Ok();
								record.setComment(dxzzlCw.toString());
								record.setCurrentprice(jingzhi);
								if (zhiyingPrice.compareTo(BigDecimal.ZERO) > 0) {
									record.setZhiying(zhiyingPrice);
								}
								// 保存最高价/最低价（如果是iscurrent=1的档位）
								if (cw.getIscurrent() != null && cw.getIscurrent() == 1) {
									if (cw.getMaxprice5() != null)
										record.setMaxprice5(cw.getMaxprice5());
									if (cw.getMinprice5() != null)
										record.setMinprice5(cw.getMinprice5());
									if (cw.getMaxzhangfu5() != null)
										record.setMaxzhangfu5(cw.getMaxzhangfu5());
									if (cw.getMaxdiefu5() != null)
										record.setMaxdiefu5(cw.getMaxdiefu5());
								}
								if (cw.getMaxpriceniu() != null)
									record.setMaxpriceniu(cw.getMaxpriceniu());

								fund1Gaoduanzhuangbei2OkMapper.updateByExampleSelective(record, example);
							} catch (Exception e) {
								System.err.println("【更新comment/zhiying/高低价失败】" + tableName + " level=" + cw.getLevel()
										+ ": " + e.getMessage());
							}

							// 对冲判断：
							duichongList = cangweis.subList(0, index);

						}

						if (zyzstag == 1) {

							break;
						}

						if (ykPrice != null)
							fundInfo.setUpcount(ykPrice.toString());

						if (clearMax == 1)
							jedisClient.del("maxvalue_" + tableName);

//						if (zyzstag == 1) {
//							System.out.print("止盈止损:");
////							
//
//							continue;
//						}
//						caiService.updateCurrentPrice(tableName, jingzhi, alarmtag, fundInfo.getBuypriceReal(),
//								jsonObject.getBigDecimal("gszzl"), fund, fundInfo, lastvalue);
						if ((jingzhi.compareTo(maxP) >= 0 || jingzhi.compareTo(minP) <= 0)
								&& jingzhi.compareTo(new BigDecimal("0")) != 0) {
							// 提醒用户卖出或买入
							System.out.println("需要买入或卖出此基金：" + fundInfo.getName());

							try {
								if (jingzhi.compareTo(maxP) >= 0) {
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

									} else {

										// 【RSI判断】触及maxP后在level-1买入前检查RSI，避免超买追高
										String instIdForRSI = fundInfo.getCode();
										BigDecimal rsiValue = null;
										try {
											String rsiStr = jedisClient.get("rsi:" + instIdForRSI);
											if (rsiStr != null && !rsiStr.isEmpty()) {
												rsiValue = new BigDecimal(rsiStr);
											}
										} catch (Exception e) {
											System.err.println(
													"【Service RSI获取失败】" + instIdForRSI + ": " + e.getMessage());
										}

										// RSI>70：超买，跳过买入（但指针已移动）
										if (rsiValue != null && rsiValue.compareTo(new BigDecimal("70")) > 0) {
											System.out.println("【Service建仓阻止】" + instIdForRSI + " RSI=" + rsiValue
													+ " 超买，level-" + fundInfo.getLevel() + " 跳过买入");
											// 不执行买入，但iscurrent指针已移动，等待RSI回落或价格继续下跌
										} else {
											alarmtag = 8;

										}
									}
									fundInfo.setFene(fund.getMoney().multiply(fund.getHuiche()));
									caiService.updateCurrentPrice(tableName, jingzhi, alarmtag,
											fundInfo.getBuypriceReal(),

											jsonObject.getBigDecimal("gszzl"), fund, fundInfo, lastvalue);

								} else {
									// ========== 建仓限制检查 ==========
									boolean canBuild = checkBuildLimit(fund.getCode());
									if (!canBuild) {
										System.out.println("【建仓限制】" + fund.getCode() + " 今日建仓次数已满，跳过买入");
										// 不建仓，但移动指针记录档位
										alarmtag = 7;
										caiService.updateCurrentPrice(tableName, jingzhi, alarmtag,
												fundInfo.getBuypriceReal(), jsonObject.getBigDecimal("gszzl"), fund,
												fundInfo, lastvalue);
									} else {

										// ========== RSI判断（智能建仓）==========
										BigDecimal rsi = getRSIFromRedis(fund.getCode());
										if (rsi == null) {
											rsi = new BigDecimal("50");
										}

										// RSI>70：超买，不建仓（避免追高）
										if (rsi.compareTo(new BigDecimal("70")) > 0) {
											System.out
													.println("【建仓阻止】" + fund.getCode() + " RSI=" + rsi + " 超买，避免高位接盘");
											// 不建仓，但移动指针记录档位
											alarmtag = 7;
											caiService.updateCurrentPrice(tableName, jingzhi, alarmtag,
													fundInfo.getBuypriceReal(), jsonObject.getBigDecimal("gszzl"), fund,
													fundInfo, lastvalue);
										}
										// RSI 30-70：正常区间，可以建仓
										else if (rsi.compareTo(new BigDecimal("30")) >= 0) {
											// 【全平后暂停检查】防止全平后立即重新建仓
											if (jedisClient.exists("t:pause:" + fund.getCode())) {
												System.out.println("【建仓暂停】" + fund.getCode() + " 全平后暂停期内，跳过建仓");
												alarmtag = 7;
												caiService.updateCurrentPrice(tableName, jingzhi, alarmtag,
														fundInfo.getBuypriceReal(), jsonObject.getBigDecimal("gszzl"),
														fund, fundInfo, lastvalue);
												continue;
											}

											System.out
													.println("【建仓确认】" + fund.getCode() + " RSI=" + rsi + " 正常区间，执行建仓");
											// ========== 检查通过，可以建仓 ==========
											// 【重要】先记录建仓次数，再执行买入，确保异常时次数也被记录
											recordBuild(fund.getCode());
											fundInfo.setFene(fund.getMoney().multiply(fund.getHuiche()));
											if (cbt == 1) {
												alarmtag = 1;
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
														fundInfo.getBuypriceReal(), jsonObject.getBigDecimal("gszzl"),
														fund, fundInfo, lastvalue);
											} else {
												alarmtag = 1;
												caiService.updateCurrentPrice(tableName, jingzhi, alarmtag,
														fundInfo.getBuypriceReal(), jsonObject.getBigDecimal("gszzl"),
														fund, fundInfo, lastvalue);
											}
										}
										// RSI<30：超卖，最佳建仓时机
										else {
											fundInfo.setFene(fund.getMoney().multiply(fund.getHuiche()));

											// 【全平后暂停检查】防止全平后立即重新建仓
											if (jedisClient.exists("t:pause:" + fund.getCode())) {
												System.out.println("【建仓暂停】" + fund.getCode() + " 全平后暂停期内，跳过建仓");
												alarmtag = 7;
												caiService.updateCurrentPrice(tableName, jingzhi, alarmtag,
														fundInfo.getBuypriceReal(), jsonObject.getBigDecimal("gszzl"),
														fund, fundInfo, lastvalue);
												continue;
											}

											System.out.println(
													"【建仓确认】" + fund.getCode() + " RSI=" + rsi + " 超卖区间，技术性反弹概率高，执行建仓");
											// ========== 检查通过，可以建仓 ==========
											// 【重要】先记录建仓次数，再执行买入，确保异常时次数也被记录
											recordBuild(fund.getCode());
											if (cbt == 1) {
												alarmtag = 1;
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
														fundInfo.getBuypriceReal(), jsonObject.getBigDecimal("gszzl"),
														fund, fundInfo, lastvalue);
											} else {
												alarmtag = 1;
												caiService.updateCurrentPrice(tableName, jingzhi, alarmtag,
														fundInfo.getBuypriceReal(), jsonObject.getBigDecimal("gszzl"),
														fund, fundInfo, lastvalue);
											}
										}
									}
								}

							} catch (Exception e) {
								e.printStackTrace();
								// TODO: handle exception
//								if (!e.getMessage().startsWith("return")) {
//									alarmPlayer.playAlarm2(1800);
//									Thread.sleep(1800000);
//								}
							}

//							if (fundInfo.getName() != null && fundInfo.getName().indexOf("alarm") != -1) {
//								System.out.print("买入卖出:");
////								Thread.sleep(10000);
//								String re = null;

//								re = HttpUtil.sendGet("https://rpa.insfair.cn/api/rpa/message/forward?uid=80");
//
//								if (re == null) {
//									HttpUtil.sendGet("http://192.168.188.190/api/rpa/message/forward?uid=80");
//								}
////								if(fundInfo.getName().indexOf("ab")!=-1||fundInfo.getName().indexOf("as")!=-1)
//								alarmPlayer.playAlarm(1);
//								else {
//									alarmPlayer.playAlarm(1);
//								}

//							}

						} else {
							// 更新数据库最新基金价格：
							BigDecimal chaju = jingzhi.subtract(fundInfo.getBuypriceReal());
							BigDecimal dxzzl = chaju.divide(fundInfo.getBuypriceReal(), 4, RoundingMode.DOWN)
									.multiply(new BigDecimal("100"));

//							int alarmtag = 0;
							BigDecimal catePrice = fundInfo.getBuypriceReal().multiply(new BigDecimal("1.0025"));
							BigDecimal catePrice2 = fundInfo.getBuypriceReal().multiply(fund.getZhangdiefu());
//							fundInfo.setCate(catePrice.toString());

							if (fund.getWendu() != null && fund.getWendu().compareTo(new BigDecimal("6")) == 0) {// &&
								// fundInfo.getFene()
								// !=
								// null

								if (fundInfo.getName() != null && fundInfo.getName().indexOf("stop") != -1
										&& (jingzhi.compareTo(fundInfo.getBuyprice().subtract(
												fundInfo.getWanggeprice().multiply(new BigDecimal("0.5")))) <= 0)) {
									alarmtag = 7;

								}

							}

							else if (fund.getWendu() != null && fund.getWendu().compareTo(new BigDecimal("7")) == 0) {
//								&& cbt == 1
								if (fundInfo.getName() != null && fundInfo.getName().indexOf("stop") != -1
										&& (jingzhi.compareTo(fundInfo.getBuyprice().subtract(
												fundInfo.getWanggeprice().multiply(new BigDecimal("0.5")))) <= 0)) {
									alarmtag = 7;

								}
							} else if (fund.getWendu() != null
									&& fund.getWendu().compareTo(new BigDecimal("6.5")) == 0) {
//									&& cbt == 1
								if (fundInfo.getName() != null && fundInfo.getName().indexOf("stop") != -1
										&& (jingzhi.compareTo(fundInfo.getBuyprice().subtract(
												fundInfo.getWanggeprice().multiply(new BigDecimal("0.5")))) <= 0)) {
									alarmtag = 7;

								}

							}

							else if (fundInfo.getName() != null && fundInfo.getName().indexOf("star") != -1
									&& (jingzhi.compareTo(catePrice) >= 0)) {// && fundInfo.getFene() !=
																				// null//|| buyTag == 1

								alarmtag = 3;

								if (fund.getWendu() != null && fund.getWendu().compareTo(new BigDecimal("6")) == 0) {

									alarmtag = 8;
								} else if (fund.getWendu() != null
										&& fund.getWendu().compareTo(new BigDecimal("6.5")) == 0) {
									alarmtag = 8;
								} else if (fund.getWendu() != null
										&& fund.getWendu().compareTo(new BigDecimal("7")) == 0) {
									alarmtag = 8;

								}

							}

							else if (fundInfo.getName() != null && fundInfo.getName().indexOf("stop") != -1
									&& (jingzhi.compareTo(fundInfo.getBuyprice().subtract(
											fundInfo.getWanggeprice().multiply(new BigDecimal("0.5")))) <= 0)) {// &&recipient_id
								// fundInfo.getFene()
								// !=
								// null
								alarmtag = 4;

							}

							else if (cbt == 1
									&& (fund.getWendu() != null && fund.getWendu().compareTo(new BigDecimal("0")) > 0)
									&& jingzhi.compareTo(fundInfo.getBuyprice()) <= 0) {

								alarmtag = 4;
							} else if ((fund.getWendu() != null && fund.getWendu().compareTo(new BigDecimal("4")) == 0)
									&& jingzhi.compareTo(fundInfo.getBuyprice()) <= 0) {

								alarmtag = 4;
							} else if ((fund.getWendu() != null && fund.getWendu().compareTo(new BigDecimal("5")) == 0)
									&& jingzhi.compareTo(catePrice2) <= 0) {

								alarmtag = 4;
							} else if ((fund.getWendu() != null
									&& fund.getWendu().compareTo(new BigDecimal("4.5")) == 0)
									&& jingzhi.compareTo(fundInfo.getBuyprice()) <= 0) {

								alarmtag = 4;
							} else if ((fund.getWendu() != null
									&& fund.getWendu().compareTo(new BigDecimal("5.5")) == 0)
									&& jingzhi.compareTo(catePrice2) <= 0) {

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
								System.out.println("发生异常：" + e.getMessage());
								e.printStackTrace();
								// TODO: handle exception
								if (!e.getMessage().startsWith("return")) {
									alarmPlayer.playAlarm2(1800);
									Thread.sleep(1800000);
								}
							}

//							if (alarmtag == 0) {

//							}
							if (alarmtag == 3 || alarmtag == 4 || alarmtag == 6 || alarmtag == 7) {// 小涨幅卖出，小跌幅买入，有止盈情况发生

								System.out.print("快速追涨和抄底:");
//								Thread.sleep(10000);

//								if (fundInfo.getName() != null && fundInfo.getName().indexOf("alarm") != -1) {
//									String re = null;

//									re = HttpUtil.sendGet("https://rpa.insfair.cn/api/rpa/message/forward?uid=80");

//									if (re == null) {
//										HttpUtil.sendGet("http://192.168.188.190/api/rpa/message/forward?uid=80");
//									}
//									if (alarmtag == 5) {
//
// 
//										alarmPlayer.playAlarm(1);
//									} else {
//
//										alarmPlayer.playAlarm(1);
//									}

//								}
							}

						}
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("处理价格数据异常：" + e.getMessage());
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

	// ========== 新增：分批止盈和正T方法 ==========

	/**
	 * 动态分批止盈检查（ATR自适应版本）
	 * 
	 * 分配策略（根据总张数money动态调整）： - money=0.03张(小仓): 0.01+0.01+0.01 (均分三批) -
	 * money=0.04张(中仓): 0.01+0.01+0.02 (第三批留大仓位奔跑) - money=0.05张(大仓): 0.01+0.02+0.02
	 * (后两批加大)
	 * 
	 * 止盈比例动态调整（RSI + ATR双因子）： 基础比例（RSI）： - 弱势(RSI<40): 第一批3%, 第二批6% - 震荡(RSI
	 * 40-60): 第一批5%, 第二批10% - 强势(RSI>60): 第一批8%, 第二批15%
	 * 
	 * ATR自适应乘数： - ATR<0.3%: 比例×0.5 (低波动，降低目标) - ATR 0.3-0.8%: 比例×1.0 (标准) -
	 * ATR>0.8%: 比例×1.5 (高波动，提高目标)
	 * 
	 * 移动止盈回撤也自适应： - ATR<0.5%: 回撤3% (收紧) - ATR 0.5-1.0%: 回撤5% (标准) - ATR>1.0%: 回撤8%
	 * (放宽)
	 * 
	 * @param atrPercent 当前ATR百分比，用于自适应调整
	 * @return 0-不满足, 1-第一批, 2-第二批, 3-第三批移动止盈
	 */
	private int checkBatchTakeProfit(Fund1Gaoduanzhuangbei2Ok cw, BigDecimal currentPrice, Fund fund, String tableName,
			BigDecimal atrPercent) {
		BigDecimal buyPrice = cw.getBuypriceReal();
		if (buyPrice == null || buyPrice.compareTo(BigDecimal.ZERO) == 0) {
			return 0;
		}

		// 计算利润率
		BigDecimal profitPct = currentPrice.subtract(buyPrice).divide(buyPrice, 4, RoundingMode.DOWN)
				.multiply(new BigDecimal("100"));

		// 获取RSI判断趋势强度
		BigDecimal rsi = getRSIFromRedis(fund.getCode());
		int trend = rsi.compareTo(new BigDecimal("60")) > 0 ? 2 : rsi.compareTo(new BigDecimal("40")) > 0 ? 1 : 0; // 2强
																													// 1震荡
																													// 0弱

		// ===== ATR自适应乘数计算 =====
		BigDecimal atrMultiplier = new BigDecimal("1.0"); // 默认标准
		BigDecimal trailingPct = new BigDecimal("5"); // 默认回撤5%

		if (atrPercent != null && atrPercent.compareTo(BigDecimal.ZERO) > 0) {
			BigDecimal atrPctValue = atrPercent.multiply(new BigDecimal("100")); // 转为百分比数值

			// ATR自适应乘数
			if (atrPctValue.compareTo(new BigDecimal("0.3")) < 0) {
				atrMultiplier = new BigDecimal("0.5"); // 低波动：目标减半
				trailingPct = new BigDecimal("3"); // 收紧回撤
			} else if (atrPctValue.compareTo(new BigDecimal("0.8")) > 0) {
				atrMultiplier = new BigDecimal("1.5"); // 高波动：目标增加50%
				trailingPct = new BigDecimal("8"); // 放宽回撤
			}
			// 0.3%-0.8%之间保持默认1.0倍，回撤5%
		}

		// 基础止盈比例（RSI决定）
		BigDecimal baseTp1 = trend == 2 ? new BigDecimal("8") : trend == 1 ? new BigDecimal("5") : new BigDecimal("3");
		BigDecimal baseTp2 = trend == 2 ? new BigDecimal("15")
				: trend == 1 ? new BigDecimal("10") : new BigDecimal("6");

		// ATR自适应后的止盈比例
		BigDecimal tp1 = baseTp1.multiply(atrMultiplier);
		BigDecimal tp2 = baseTp2.multiply(atrMultiplier);

		// 只在趋势变化或ATR乘数变化时打印（减少日志频率）
		String lastTrendKey = "last:tp:log:trend:" + tableName + ":" + cw.getLevel();
		String lastMultKey = "last:tp:log:mult:" + tableName + ":" + cw.getLevel();
		String lastTrend = jedisClient.get(lastTrendKey);
		String lastMult = jedisClient.get(lastMultKey);
		String currentTrend = String.valueOf(trend);
		String currentMult = atrMultiplier.toString();

		if (!currentTrend.equals(lastTrend) || !currentMult.equals(lastMult)) {
			System.out.println("【底仓止盈】ATR=" + atrPercent + " 乘数=" + atrMultiplier + " TP1=" + tp1 + "% TP2=" + tp2
					+ "% 回撤=" + trailingPct + "%");
			jedisClient.setex(lastTrendKey, 3600, currentTrend);
			jedisClient.setex(lastMultKey, 3600, currentMult);
		}

		// 从Redis获取分批状态
		String batchKey = "batch:tp:" + tableName + ":" + cw.getLevel();
		String batchStatus = jedisClient.get(batchKey);
		if (batchStatus == null) {
			batchStatus = "0"; // 初始状态：未触发任何批次
		}
		int status = Integer.parseInt(batchStatus);

		// 第一批止盈（动态比例）- 价格达标立即执行
		if (status == 0 && profitPct.compareTo(tp1) >= 0) {
			System.out.println("【底仓止盈-Tier1】价格达标" + profitPct.setScale(2, RoundingMode.HALF_UP) + "% >= "
					+ tp1.setScale(2, RoundingMode.HALF_UP) + "%，执行卖出25%");
			jedisClient.setex(batchKey, 86400 * 30, "1");
			return 1;
		}

		// 第二批止盈（动态比例）- 价格达标立即执行
		if (status == 1 && profitPct.compareTo(tp2) >= 0) {
			System.out.println("【底仓止盈-Tier2】价格达标" + profitPct.setScale(2, RoundingMode.HALF_UP) + "% >= "
					+ tp2.setScale(2, RoundingMode.HALF_UP) + "%，执行卖出25%");
			jedisClient.setex(batchKey, 86400 * 30, "2");
			return 2;
		}

		// 第三批移动止盈（ATR自适应回撤）
		if (status == 2) {
			String highestKey = "highest:" + tableName + ":" + cw.getLevel();
			String highestStr = jedisClient.get(highestKey);
			BigDecimal highest = highestStr == null ? currentPrice : new BigDecimal(highestStr);

			// 【重要】检查是否是新仓位（compare buyPrice with Redis stored price）
			String buyPriceKey = "buyprice:" + tableName + ":" + cw.getLevel();
			String storedBuyPrice = jedisClient.get(buyPriceKey);
			if (storedBuyPrice == null || new BigDecimal(storedBuyPrice).compareTo(buyPrice) != 0) {
				// 新仓位，重置最高价记录
				highest = currentPrice;
				jedisClient.setex(buyPriceKey, 86400 * 30, buyPrice.toString());
				System.out.println("【移动止盈】检测到新仓位，重置最高价记录: " + currentPrice);
			}

			// 更新最高价
			if (currentPrice.compareTo(highest) > 0) {
				highest = currentPrice;
				jedisClient.setex(highestKey, 86400 * 30, highest.toString());
			}

			// 计算从最高价的回撤（使用自适应回撤比例）
			BigDecimal triggerPct = tp2.multiply(new BigDecimal("0.8")); // 第二批止盈的80%作为触发线
			BigDecimal triggerPrice = buyPrice.multiply(BigDecimal.ONE.add(triggerPct.divide(new BigDecimal("100"))));

			if (highest.compareTo(triggerPrice) > 0) {
				// 曾经涨超过触发线，启用移动止盈
				BigDecimal trailingMultiplier = BigDecimal.ONE.subtract(trailingPct.divide(new BigDecimal("100")));
				BigDecimal trailingPrice = highest.multiply(trailingMultiplier); // 自适应回撤价格
				BigDecimal actualPullback = highest.subtract(currentPrice).divide(highest, 4, RoundingMode.HALF_UP)
						.multiply(new BigDecimal("100"));

				System.out.println("【移动止盈】最高=" + highest + " 回撤阈值=" + trailingPct + "% 回撤价格=" + trailingPrice + " 当前="
						+ currentPrice + " 实际回撤=" + actualPullback + "%");

				if (currentPrice.compareTo(trailingPrice) <= 0) {
					jedisClient.setex(batchKey, 86400 * 30, "3"); // 标记第三批已完成
					return 3;
				}
			}
		}

		// ===== 保本止损：第一批已触发，但价格跌回成本价-2% =====
		if (status == 1) {
			// 读取已卖出张数（确认第一批已执行）
			String soldKey = "batch:sold:" + tableName + ":" + cw.getLevel();
			String soldStr = jedisClient.get(soldKey);
			BigDecimal soldFene = soldStr == null ? BigDecimal.ZERO : new BigDecimal(soldStr);

			// 已卖出部分，且当前价跌破成本价-2%
			BigDecimal stopLossPrice = buyPrice.multiply(new BigDecimal("0.98"));
			if (soldFene.compareTo(BigDecimal.ZERO) > 0 && currentPrice.compareTo(stopLossPrice) < 0) {
				System.out.println(
						"【保本止损】第一批已卖" + soldFene + "张，当前价" + currentPrice + "跌破成本价-2%(" + stopLossPrice + ")，清仓剩余");
				jedisClient.setex(batchKey, 86400 * 30, "4"); // 标记为止损清仓
				return 4; // 保本止损清仓
			}
		}

		return 0;
	}

	/**
	 * 获取当前量比（当前成交量/近期均量）
	 * 
	 * @param symbol 品种代码
	 * @return 量比，无数据时返回1.0（中性）
	 */
	private BigDecimal getVolumeRatio(String symbol) {
		try {
			Set<String> volMembers = jedisClient.zrange("vol_" + symbol);
			if (volMembers == null || volMembers.size() < 5) {
				return new BigDecimal("1.0"); // 数据不足，中性处理
			}

			List<BigDecimal> volList = new ArrayList<>();
			for (String mem : volMembers) {
				Double score = jedisClient.zscore("vol_" + symbol, mem);
				if (score != null && score > 0) {
					volList.add(new BigDecimal(score.toString()));
				}
			}

			if (volList.size() < 5) {
				return new BigDecimal("1.0");
			}

			// 当前量是最后一个
			BigDecimal currentVol = volList.get(volList.size() - 1);

			// 计算前N-1个的平均
			BigDecimal sumVol = BigDecimal.ZERO;
			for (int i = 0; i < volList.size() - 1; i++) {
				sumVol = sumVol.add(volList.get(i));
			}
			BigDecimal avgVol = sumVol.divide(new BigDecimal(volList.size() - 1), 4, RoundingMode.HALF_UP);

			if (avgVol.compareTo(BigDecimal.ZERO) > 0) {
				return currentVol.divide(avgVol, 2, RoundingMode.HALF_UP);
			}

		} catch (Exception e) {
			System.out.println("【量比计算】失败: " + e.getMessage());
		}
		return new BigDecimal("1.0"); // 异常时中性处理
	}

	/**
	 * 检查建仓限制（每日3次）
	 */
	private boolean checkBuildLimit(String symbol) {
		String key = "build:count:" + symbol + ":" + getToday();
		String count = jedisClient.get(key);
		if (count == null)
			return true;
		return Integer.parseInt(count) < 3;
	}

	/**
	 * 记录建仓
	 */
	private void recordBuild(String symbol) {
		String key = "build:count:" + symbol + ":" + getToday();
		String count = jedisClient.get(key);
		if (count == null) {
			jedisClient.setex(key, 86400, "1");
		} else {
			jedisClient.incr(key);
		}
	}

	private String getToday() {
		return new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
	}

	/**
	 * 从Redis获取RSI值
	 */
	private BigDecimal getRSIFromRedis(String symbol) {
		String rsiStr = jedisClient.get("rsi:" + symbol);
		if (rsiStr == null) {
			// RSI获取失败，返回超买状态(99)以阻止买入，避免在高位追涨
			System.out.println("【RSI获取失败】" + symbol + " Redis中无数据，阻止买入以防止高位接盘");
			return new BigDecimal("99"); // 默认超买，阻止买入
		}
		try {
			return new BigDecimal(rsiStr);
		} catch (Exception e) {
			System.out.println("【RSI解析失败】" + symbol + " 值:" + rsiStr + "，阻止买入");
			return new BigDecimal("99"); // 解析失败也阻止买入
		}
	}

	/**
	 * 动态计算分批卖出份额
	 * 
	 * 根据总张数（money）动态分配三批的比例： - 小仓(0.03): 0.01+0.01+0.01 (均分，稳健) - 中仓(0.04):
	 * 0.01+0.01+0.02 (第三批多，博反弹) - 大仓(0.05): 0.01+0.02+0.02 (后两批多，趋势强时多拿)
	 * 
	 * @param totalMoney 总张数(money字段)
	 * @param batchNum   批次(1,2,3)
	 * @param huiche     合约乘数
	 * @return 该批次的份额(fene)
	 */
	private BigDecimal calcBatchFene(BigDecimal totalMoney, int batchNum, BigDecimal huiche) {
		// 最小单位0.01张
		BigDecimal minUnit = new BigDecimal("0.01");
		BigDecimal batchZhang = BigDecimal.ZERO;

		// 根据总张数确定分配策略
		if (totalMoney.compareTo(new BigDecimal("0.035")) <= 0) {
			// 小仓 <=0.035张: 均分三批
			if (batchNum == 1 || batchNum == 2) {
				batchZhang = minUnit; // 0.01张
			} else {
				batchZhang = totalMoney.subtract(minUnit.multiply(new BigDecimal("2"))); // 剩余
			}
		} else if (totalMoney.compareTo(new BigDecimal("0.045")) <= 0) {
			// 中仓 0.035-0.045张: 0.01+0.01+剩余
			if (batchNum == 1 || batchNum == 2) {
				batchZhang = minUnit; // 0.01张
			} else {
				batchZhang = totalMoney.subtract(minUnit.multiply(new BigDecimal("2"))); // 剩余给第三批
			}
		} else {
			// 大仓 >0.045张: 第一批0.01，后两批均分剩余
			if (batchNum == 1) {
				batchZhang = minUnit;
			} else {
				BigDecimal remaining = totalMoney.subtract(minUnit);
				batchZhang = remaining.divide(new BigDecimal("2"), 2, RoundingMode.DOWN);
			}
		}

		// 确保最小0.01张
		if (batchZhang.compareTo(minUnit) < 0) {
			batchZhang = minUnit;
		}

		// 转换为份额(fene = 张数 × huiche)
		return batchZhang.multiply(huiche);
	}

	/**
	 * 从K线计算RSI（14周期）
	 * 
	 * @param candles K线列表
	 * @return RSI值 0-100
	 */
	private BigDecimal calculateRSIFromCandles(List<cn.exrick.manager.service.util.Candle> candles) {
		if (candles == null || candles.size() < 15) {
			return new BigDecimal("50"); // 数据不足返回中性值
		}

		BigDecimal gainSum = BigDecimal.ZERO;
		BigDecimal lossSum = BigDecimal.ZERO;

		// 计算最近14个周期的涨跌
		int startIdx = Math.max(1, candles.size() - 14);
		for (int i = startIdx; i < candles.size(); i++) {
			BigDecimal close = BigDecimal.valueOf(candles.get(i).getClose());
			BigDecimal prevClose = BigDecimal.valueOf(candles.get(i - 1).getClose());
			BigDecimal change = close.subtract(prevClose);

			if (change.compareTo(BigDecimal.ZERO) > 0) {
				gainSum = gainSum.add(change);
			} else {
				lossSum = lossSum.add(change.abs());
			}
		}

		int period = candles.size() - startIdx;
		if (period == 0)
			return new BigDecimal("50");

		BigDecimal avgGain = gainSum.divide(new BigDecimal(period), 6, RoundingMode.HALF_UP);
		BigDecimal avgLoss = lossSum.divide(new BigDecimal(period), 6, RoundingMode.HALF_UP);

		if (avgLoss.compareTo(BigDecimal.ZERO) == 0) {
			return new BigDecimal("100"); // 无下跌，RSI=100
		}

		BigDecimal rs = avgGain.divide(avgLoss, 6, RoundingMode.HALF_UP);
		BigDecimal rsi = new BigDecimal("100")
				.subtract(new BigDecimal("100").divide(BigDecimal.ONE.add(rs), 6, RoundingMode.HALF_UP));

		return rsi.setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * 【双向持仓】查询OKX订单获取posId 根据clOrdId查询订单详情，获取posId用于指定平仓，避免FIFO问题
	 */
	private String queryOkxPosId(String symbol, String clOrdId) {
		try {
			// 使用OKX订单查询接口获取posId
			String keyString = "?clOrdId=" + clOrdId + "&instId=" + symbol;
			String transOrder = okxService.trade("/api/v5/trade/order" + keyString, "GET", "");

			if (transOrder == null || transOrder.isEmpty()) {
				System.err.println("【查询posId】订单查询返回空: " + clOrdId);
				return null;
			}

			// 解析JSON获取posId
			JSONObject json = new JSONObject(transOrder);
			if (!"0".equals(json.getStr("code"))) {
				System.err.println("【查询posId】订单查询失败: " + json.getStr("msg"));
				return null;
			}

			JSONArray data = json.getJSONArray("data");
			if (data == null || data.isEmpty()) {
				System.err.println("【查询posId】订单数据为空: " + clOrdId);
				return null;
			}

			// 获取订单中的posId
			JSONObject orderData = data.getJSONObject(0);
			String posId = orderData.getStr("posId");

			if (posId != null && !posId.isEmpty()) {
				System.out.println("【查询posId】成功: " + symbol + " clOrdId=" + clOrdId + " posId=" + posId);
			} else {
				System.err.println("【查询posId】订单中无posId: " + clOrdId);
			}
			return posId;

		} catch (Exception e) {
			System.err.println("【查询posId】异常: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
