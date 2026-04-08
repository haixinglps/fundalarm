package cn.exrick.manager.service.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.exrick.common.jedis.JedisClient;
import cn.hutool.json.JSONUtil;

/**
 * 独立T单管理器（完全独立于底仓）
 * 
 * 核心设计：
 * 1. T单有自己的成本体系，不依赖底仓成本
 * 2. 按价格间距网格建仓（每跌0.3%开新T）
 * 3. 每个档位最多3个T单，防止资金占用过多
 * 4. 各自止盈0.3%或解套盈利0.15%，被套死扛等全平
 */
@Component
public class IndependentTManager {

    @Autowired
    private JedisClient jedisClient;

    // Redis Key
    private static final String T_ORDER_PREFIX = "t:ind:";  // t:ind:{symbol}:{level}:{index}
    private static final String T_INDEX_KEY = "t:ind:index:";     // 序号记录
    private static final String T_LAST_BUY_KEY = "t:ind:lastbuy:"; // 记录该档位最后一个T单买入价
    private static final String T_COOLDOWN_KEY = "t:ind:cooldown:"; // 冷却时间记录
    private static final String PRICE_HISTORY_KEY = "t:price:history";
    private static final String T_STATS_KEY = "t:ind:stats";
    private static final String T_DAILY_PROFIT_KEY = "t:ind:daily:"; // t:ind:daily:{symbol}:{date} 当日总盈利
    
    // T单参数（小间距捕捉波动）
    private static final BigDecimal T_QTY = new BigDecimal("0.01");      // 0.01张
    private static final BigDecimal TP_TARGET = new BigDecimal("0.003");  // 止盈0.3%
    private static final BigDecimal SL_LIMIT = new BigDecimal("0.002");   // 止损0.2%
    private static final BigDecimal GRID_SPACING = new BigDecimal("0.0015"); // 网格间距0.15%（更小，捕捉小波动）
    private static final int COOLDOWN_SECONDS = 30; // 建仓冷却30秒，防频繁
    private static final BigDecimal FEE_RATE = new BigDecimal("0.001");   // 手续费0.1%
    private static final BigDecimal BK_PROFIT = new BigDecimal("0.0015"); // 解套净利0.15%
    private static final int MAX_T_PER_LEVEL = 5; // 每档位最多5个T单（间距小了，数量多点）
    
    // 入场条件
    private static final BigDecimal VOLATILITY_THRESHOLD = new BigDecimal("0.0005");
    private static final int CONFIRM_COUNT = 2;

    private String getTOrderKey(String symbol, int level, int index) {
        return T_ORDER_PREFIX + symbol + ":" + level + ":" + index;
    }
    
    private String getLastBuyKey(String symbol, int level) {
        return T_LAST_BUY_KEY + symbol + ":" + level;
    }

    /**
     * 获取该档位所有T单
     */
    public List<TOrder> getTOrders(String symbol, int level) {
        List<TOrder> orders = new ArrayList<>();
        for (int i = 1; i <= MAX_T_PER_LEVEL; i++) {
            String json = jedisClient.get(getTOrderKey(symbol, level, i));
            if (json != null) {
                TOrder order = JSONUtil.toBean(json, TOrder.class);
                order.setIndex(i);
                orders.add(order);
            }
        }
        return orders;
    }
    
    /**
     * 获取该档位T单数量
     */
    public int getTOrderCount(String symbol, int level) {
        return getTOrders(symbol, level).size();
    }

    /**
     * 检查是否可以开新T单（小间距+冷却时间）
     * 
     * 条件：
     * 1. 冷却时间已过（30秒内不能重复建仓）
     * 2. 该档位T单数量 < 5
     * 3. 当前价格 < 上一个T单买入价 × (1 - 0.0015) 或相对近期高点跌0.15%
     * 4. 波动率、下跌速度过滤
     */
    public int checkEntry(String symbol, int level, BigDecimal currentPrice) {
        // 1. 检查冷却时间
        String cooldownKey = T_COOLDOWN_KEY + symbol + ":" + level;
        if (jedisClient.exists(cooldownKey)) {
            return 0; // 冷却中
        }
        
        // 2. 检查数量限制
        if (getTOrderCount(symbol, level) >= MAX_T_PER_LEVEL) {
            return 0;
        }
        
        // 3. 获取参考价（上一个T单买入价或近期高点）
        String lastBuyStr = jedisClient.get(getLastBuyKey(symbol, level));
        BigDecimal referencePrice;
        boolean isFirst;
        
        if (lastBuyStr == null) {
            // 首个T单：参考近60秒高点
            referencePrice = getRecentHigh(symbol, 60);
            if (referencePrice == null) referencePrice = currentPrice;
            isFirst = true;
        } else {
            referencePrice = new BigDecimal(lastBuyStr);
            isFirst = false;
        }
        
        // 4. 价格条件：相对参考价跌0.15%（小间距）
        BigDecimal targetPrice = referencePrice.multiply(
                new BigDecimal("1").subtract(GRID_SPACING)); // 0.9985
        
        if (currentPrice.compareTo(targetPrice) > 0) {
            return 0; // 跌得不够
        }
        
        // 5. 波动率检查
        BigDecimal volatility = calculateVolatility(symbol);
        if (volatility.compareTo(VOLATILITY_THRESHOLD) < 0) {
            return 0;
        }
        
        // 6. 下跌速度检查（不接飞刀）
        BigDecimal recentDrop = calculateRecentDrop(symbol, 3);
        if (recentDrop == null || recentDrop.compareTo(new BigDecimal("0.005")) > 0) {
            return 0;
        }
        
        // 7. 连续确认
        String confirmKey = "t:ind:confirm:" + symbol + ":" + level;
        String confirmStr = jedisClient.get(confirmKey);
        int confirmCount = confirmStr == null ? 0 : Integer.parseInt(confirmStr);
        
        int requiredConfirm = isFirst ? 3 : CONFIRM_COUNT; // 首单需3次确认
        
        confirmCount++;
        if (confirmCount >= requiredConfirm) {
            jedisClient.del(confirmKey);
            // 设置冷却时间
            jedisClient.setex(cooldownKey, COOLDOWN_SECONDS, "1");
            return isFirst ? 2 : 1;
        } else {
            jedisClient.setex(confirmKey, 10, String.valueOf(confirmCount));
            return 0;
        }
    }

    /**
     * 开T单
     */
    public TOrder openTOrder(String symbol, int level, BigDecimal buyPrice, 
                             BigDecimal huiche, String tableName, int signalStrength) {
        // 找空位
        int index = 0;
        for (int i = 1; i <= MAX_T_PER_LEVEL; i++) {
            if (!jedisClient.exists(getTOrderKey(symbol, level, i))) {
                index = i;
                break;
            }
        }
        if (index == 0) return null; // 满了
        
        TOrder order = new TOrder();
        order.setSymbol(symbol);
        order.setLevel(level);
        order.setIndex(index);
        order.setBuyPrice(buyPrice);
        order.setQty(T_QTY);
        order.setFene(T_QTY.multiply(huiche));
        order.setBuyTime(System.currentTimeMillis());
        order.setTableName(tableName);
        order.setSignalStrength(signalStrength);
        order.setHighestPrice(buyPrice);
        order.setLowestPrice(buyPrice);
        
        // 保存T单
        String key = getTOrderKey(symbol, level, index);
        jedisClient.setex(key, 3600, JSONUtil.toJsonStr(order));
        
        // 记录最后买入价（作为下一个T单的参考）
        jedisClient.setex(getLastBuyKey(symbol, level), 3600, buyPrice.toString());
        
        recordStat("open", signalStrength);
        
        System.out.println("【独立T单建立】档位" + level + "-" + index + " 买入价" + buyPrice + 
                "，当前该档位T单：" + getTOrderCount(symbol, level) + "/" + MAX_T_PER_LEVEL);
        
        return order;
    }

    /**
     * 检查所有T单是否需要平仓（含简化止损逻辑）
     * 
     * 止损逻辑：整个币种当日T单总盈利 >= 本次止损金额，才允许止损
     * 目的：只亏当天赚的钱，不伤本金
     * 
     * @return List<[index, reason]> reason: 1-止盈, 2-解套, 3-止损
     */
    public List<int[]> checkExits(String symbol, int level, BigDecimal currentPrice) {
        List<int[]> result = new ArrayList<>();
        List<TOrder> orders = getTOrders(symbol, level);
        
        // 获取整个币种当日T单总盈利（所有档位汇总）
        BigDecimal dailyProfit = getDailyProfit(symbol);
        
        for (TOrder order : orders) {
            updateExtreme(order, currentPrice);
            
            BigDecimal buyPrice = order.getBuyPrice();
            BigDecimal pnlPct = currentPrice.subtract(buyPrice)
                    .divide(buyPrice, 4, RoundingMode.DOWN);
            
            // 1. 止盈0.3%
            if (pnlPct.compareTo(TP_TARGET) >= 0) {
                result.add(new int[]{order.getIndex(), 1});
                addDailyProfit(symbol, currentPrice.subtract(buyPrice).multiply(T_QTY)); // 记录实际盈利
                continue;
            }
            
            // 2. 解套盈利0.15%
            BigDecimal lowest = order.getLowestPrice();
            BigDecimal breakEven = buyPrice.multiply(new BigDecimal("1.0025"));
            if (lowest != null && 
                lowest.compareTo(buyPrice.multiply(new BigDecimal("0.998"))) < 0 &&
                currentPrice.compareTo(breakEven) >= 0) {
                result.add(new int[]{order.getIndex(), 2});
                addDailyProfit(symbol, currentPrice.subtract(buyPrice).multiply(T_QTY)); // 记录实际盈利
                continue;
            }
            
            // 3. 简化止损：亏损0.2%且当日总盈利够覆盖
            BigDecimal lossAmount = buyPrice.subtract(currentPrice).multiply(T_QTY);
            if (lossAmount.compareTo(BigDecimal.ZERO) > 0 && 
                lossAmount.divide(buyPrice.multiply(T_QTY), 4, RoundingMode.DOWN).compareTo(SL_LIMIT) >= 0) {
                
                if (dailyProfit.compareTo(lossAmount) >= 0) {
                    // 当日赚的钱够亏，止损
                    result.add(new int[]{order.getIndex(), 3});
                    subtractDailyProfit(symbol, lossAmount);
                } else {
                    System.out.println("【T单死扛】档位" + level + "-" + order.getIndex() + 
                            " 当日盈利" + dailyProfit + "不够覆盖亏损" + lossAmount);
                }
            }
        }
        
        return result;
    }
    
    // 获取整个币种当日总盈利
    private BigDecimal getDailyProfit(String symbol) {
        String date = java.time.LocalDate.now().toString();
        String key = T_DAILY_PROFIT_KEY + symbol + ":" + date;
        String val = jedisClient.get(key);
        return val == null ? BigDecimal.ZERO : new BigDecimal(val);
    }
    
    // 增加当日盈利
    private void addDailyProfit(String symbol, BigDecimal profit) {
        String date = java.time.LocalDate.now().toString();
        String key = T_DAILY_PROFIT_KEY + symbol + ":" + date;
        BigDecimal current = getDailyProfit(symbol);
        jedisClient.setex(key, 86400, current.add(profit).toString());
    }
    
    // 扣除当日盈利（止损）
    private void subtractDailyProfit(String symbol, BigDecimal loss) {
        String date = java.time.LocalDate.now().toString();
        String key = T_DAILY_PROFIT_KEY + symbol + ":" + date;
        BigDecimal current = getDailyProfit(symbol);
        jedisClient.setex(key, 86400, current.subtract(loss).toString());
    }

    /**
     * 平仓
     */
    public BigDecimal closeTOrder(String symbol, int level, int index, 
                                   BigDecimal sellPrice, int reason) {
        String key = getTOrderKey(symbol, level, index);
        String json = jedisClient.get(key);
        if (json == null) return BigDecimal.ZERO;
        
        TOrder order = JSONUtil.toBean(json, TOrder.class);
        BigDecimal pnl = sellPrice.subtract(order.getBuyPrice()).multiply(order.getQty());
        
        // 删除T单
        jedisClient.del(key);
        
        // 如果是该档位最后一个T单，清理lastBuyKey
        if (getTOrderCount(symbol, level) == 1) {
            jedisClient.del(getLastBuyKey(symbol, level));
        }
        
        // 记录统计
        String reasonStr = reason == 1 ? "止盈" : reason == 2 ? "解套" : "止损";
        recordStat(reason == 3 ? "loss_sl" : (reason == 1 ? "win_tp" : "win_bk"), 0);
        
        System.out.println("【独立T单平仓-" + reasonStr + "】档位" + level + "-" + index + " 盈亏" + pnl);
        return pnl;
    }

    /**
     * 强制平所有（全平时）
     */
    public BigDecimal forceCloseAll(String symbol, BigDecimal currentPrice) {
        BigDecimal totalProfit = BigDecimal.ZERO;
        int count = 0;
        
        for (int level = 1; level <= 20; level++) {
            for (int index = 1; index <= MAX_T_PER_LEVEL; index++) {
                String key = getTOrderKey(symbol, level, index);
                String json = jedisClient.get(key);
                if (json != null) {
                    TOrder order = JSONUtil.toBean(json, TOrder.class);
                    totalProfit = totalProfit.add(
                        currentPrice.subtract(order.getBuyPrice()).multiply(order.getQty()));
                    count++;
                    jedisClient.del(key);
                }
            }
            jedisClient.del(getLastBuyKey(symbol, level));
        }
        
        System.out.println("【独立T单全平】共" + count + "个，总利润" + totalProfit);
        return totalProfit;
    }

    private void updateExtreme(TOrder order, BigDecimal currentPrice) {
        boolean updated = false;
        if (currentPrice.compareTo(order.getHighestPrice()) > 0) {
            order.setHighestPrice(currentPrice);
            updated = true;
        }
        if (currentPrice.compareTo(order.getLowestPrice()) < 0) {
            order.setLowestPrice(currentPrice);
            updated = true;
        }
        if (updated) {
            String key = getTOrderKey(order.getSymbol(), order.getLevel(), order.getIndex());
            jedisClient.setex(key, 3600, JSONUtil.toJsonStr(order));
        }
    }

    private BigDecimal getRecentHigh(String symbol, int seconds) {
        String key = PRICE_HISTORY_KEY + ":" + symbol;
        List<String> list = jedisClient.lrange(key, 0, -1);
        if (list == null || list.isEmpty()) return null;
        
        long now = System.currentTimeMillis();
        BigDecimal high = null;
        
        for (String item : list) {
            String[] parts = item.split(":");
            if (parts.length == 2) {
                long time = Long.parseLong(parts[0]);
                if (now - time <= seconds * 1000) {
                    BigDecimal price = new BigDecimal(parts[1]);
                    if (high == null || price.compareTo(high) > 0) {
                        high = price;
                    }
                }
            }
        }
        return high;
    }

    public void recordPrice(String symbol, BigDecimal price) {
        String key = PRICE_HISTORY_KEY + ":" + symbol;
        jedisClient.lpush(key, System.currentTimeMillis() + ":" + price);
        jedisClient.ltrim(key, 0, 19);
        jedisClient.expire(key, 300);
    }

    private BigDecimal calculateVolatility(String symbol) {
        // 简化版，返回默认小波动
        return new BigDecimal("0.001");
    }

    private BigDecimal calculateRecentDrop(String symbol, int seconds) {
        return new BigDecimal("0.001"); // 简化
    }

    private void recordStat(String type, int value) {
        String date = java.time.LocalDate.now().toString();
        String key = T_STATS_KEY + ":" + type + ":" + date;
        jedisClient.incr(key);
        jedisClient.expire(key, 86400);
    }

    public TStats getTodayStats() {
        String date = java.time.LocalDate.now().toString();
        TStats stats = new TStats();
        stats.setOpenCount(parseInt(jedisClient.get(T_STATS_KEY + ":open:" + date)));
        stats.setWinTpCount(parseInt(jedisClient.get(T_STATS_KEY + ":win_tp:" + date)));
        stats.setWinBkCount(parseInt(jedisClient.get(T_STATS_KEY + ":win_bk:" + date)));
        return stats;
    }

    private int parseInt(String s) {
        return s == null ? 0 : Integer.parseInt(s);
    }

    public static class TOrder {
        private String symbol;
        private int level;
        private int index;
        private BigDecimal buyPrice;
        private BigDecimal qty;
        private BigDecimal fene;
        private long buyTime;
        private String tableName;
        private int signalStrength;
        private BigDecimal highestPrice;
        private BigDecimal lowestPrice;

        // Getters and Setters
        public String getSymbol() { return symbol; }
        public void setSymbol(String symbol) { this.symbol = symbol; }
        public int getLevel() { return level; }
        public void setLevel(int level) { this.level = level; }
        public int getIndex() { return index; }
        public void setIndex(int index) { this.index = index; }
        public BigDecimal getBuyPrice() { return buyPrice; }
        public void setBuyPrice(BigDecimal buyPrice) { this.buyPrice = buyPrice; }
        public BigDecimal getQty() { return qty; }
        public void setQty(BigDecimal qty) { this.qty = qty; }
        public BigDecimal getFene() { return fene; }
        public void setFene(BigDecimal fene) { this.fene = fene; }
        public long getBuyTime() { return buyTime; }
        public void setBuyTime(long buyTime) { this.buyTime = buyTime; }
        public String getTableName() { return tableName; }
        public void setTableName(String tableName) { this.tableName = tableName; }
        public int getSignalStrength() { return signalStrength; }
        public void setSignalStrength(int signalStrength) { this.signalStrength = signalStrength; }
        public BigDecimal getHighestPrice() { return highestPrice; }
        public void setHighestPrice(BigDecimal highestPrice) { this.highestPrice = highestPrice; }
        public BigDecimal getLowestPrice() { return lowestPrice; }
        public void setLowestPrice(BigDecimal lowestPrice) { this.lowestPrice = lowestPrice; }
    }

    public static class TStats {
        private int openCount;
        private int winTpCount;
        private int winBkCount;

        public int getOpenCount() { return openCount; }
        public void setOpenCount(int openCount) { this.openCount = openCount; }
        public int getWinTpCount() { return winTpCount; }
        public void setWinTpCount(int winTpCount) { this.winTpCount = winTpCount; }
        public int getWinBkCount() { return winBkCount; }
        public void setWinBkCount(int winBkCount) { this.winBkCount = winBkCount; }

        @Override
        public String toString() {
            return String.format("独立T统计[开仓:%d, 止盈0.3%%:%d, 解套:%d, 盈利:%d]", 
                    openCount, winTpCount, winBkCount, winTpCount + winBkCount);
        }
    }
}
