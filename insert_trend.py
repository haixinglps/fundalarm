with open('/home/www/code/fundalarmcode/robotium-fundalarm-service/src/main/java/cn/exrick/manager/service/task/FundPriceUpdate2.java', 'r') as f:
    lines = f.readlines()

new_lines = lines[:1195]

new_content = '''

									// ===== 计算5分钟趋势并写入Redis（V2026.04.14：替代秒级tick趋势） =====
									try {
										if (candles != null && candles.size() >= 12) {
											BigDecimal recentSum = BigDecimal.ZERO;
											BigDecimal pastSum = BigDecimal.ZERO;
											int mid = candles.size() / 2;
											for (int i = 0; i < mid; i++) {
												Object c = candles.get(i);
												java.lang.reflect.Method getClose = c.getClass().getMethod("getClose");
												pastSum = pastSum.add(BigDecimal.valueOf(((Number) getClose.invoke(c)).doubleValue()));
											}
											for (int i = mid; i < candles.size(); i++) {
												Object c = candles.get(i);
												java.lang.reflect.Method getClose = c.getClass().getMethod("getClose");
												recentSum = recentSum.add(BigDecimal.valueOf(((Number) getClose.invoke(c)).doubleValue()));
											}
											BigDecimal recentAvg = recentSum.divide(new BigDecimal(mid), 8, RoundingMode.HALF_UP);
											BigDecimal pastAvg = pastSum.divide(new BigDecimal(mid), 8, RoundingMode.HALF_UP);
											String trend;
											BigDecimal threshold = pastAvg.multiply(new BigDecimal("0.001"));
											if (recentAvg.subtract(pastAvg).abs().compareTo(threshold) < 0) {
												trend = "sideway";
											} else if (recentAvg.compareTo(pastAvg) > 0) {
												trend = "up";
											} else {
												trend = "down";
											}
											jedisClient.setex("trend:5m:" + fund.getCode(), 300, trend);
										}
									} catch (Exception trendEx) {
										trendEx.printStackTrace();
									}
'''

new_lines.append(new_content)
new_lines.extend(lines[1195:])

with open('/home/www/code/fundalarmcode/robotium-fundalarm-service/src/main/java/cn/exrick/manager/service/task/FundPriceUpdate2.java', 'w') as f:
    f.writelines(new_lines)

print('Done')
