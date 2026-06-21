with open('/home/www/code/fundalarmcode/robotium-fundalarm-service/src/main/java/cn/exrick/manager/service/task/DailyProfitTManager.java', 'r') as f:
    lines = f.readlines()

# Replace lines 358-439 (0-indexed: 357-439)
new_lines = lines[:357]

new_content = """\t\ttotal += score.rsiScore;

\t\tContractConfig config = getConfig(symbol);
\t\tString trend = getTrendDirection(symbol);

\t\t// 2. 趋势评分（15分）- 下跌趋势改为扣分，不再一刀切禁止
\t\tif ("up".equals(trend)) {
\t\t\tscore.trendScore = 15;
\t\t\tscore.trendComment = "上升趋势";
\t\t} else if ("sideway".equals(trend)) {
\t\t\tscore.trendScore = 10;
\t\t\tscore.trendComment = "震荡";
\t\t} else if ("down".equals(trend)) {
\t\t\tscore.trendScore = -10;
\t\t\tscore.trendComment = "下跌趋势-10分";
\t\t}
\t\ttotal += score.trendScore;

\t\t// 3. 波动率评分（20分）
\t\tif (atrPercent != null) {
\t\t\tif (atrPercent.compareTo(new BigDecimal("0.005")) >= 0) {
\t\t\t\tscore.volatilityScore = 20;
\t\t\t\tscore.volComment = "波动率=" + atrPercent.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) + "% 理想";
\t\t\t} else if (atrPercent.compareTo(new BigDecimal("0.002")) >= 0) {
\t\t\t\tscore.volatilityScore = 15;
\t\t\t\tscore.volComment = "波动率=" + atrPercent.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) + "% 正常";
\t\t\t} else if (atrPercent.compareTo(new BigDecimal("0.001")) >= 0) {
\t\t\t\tscore.volatilityScore = 5;
\t\t\t\tscore.volComment = "波动率=" + atrPercent.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) + "% 偏低";
\t\t\t} else {
\t\t\t\tscore.volatilityScore = 0;
\t\t\t\tscore.volComment = "波动率=" + atrPercent.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) + "% 过低";
\t\t\t}
\t\t\ttotal += score.volatilityScore;
\t\t}

\t\tscore.totalScore = total;
\t\tscore.passed = (atrPercent.compareTo(config.minAtrForScore) >= 0) && total >= 35;
"""

new_lines.append(new_content)
new_lines.extend(lines[440:])

with open('/home/www/code/fundalarmcode/robotium-fundalarm-service/src/main/java/cn/exrick/manager/service/task/DailyProfitTManager.java', 'w') as f:
    f.writelines(new_lines)

print('Done')
