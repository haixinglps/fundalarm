package cn.exrick.manager.service.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.exrick.common.jedis.JedisClient;
import cn.hutool.json.JSONUtil;

/**
 * 智能正T管理器（多T单并行，各套各的）
 * 
 * 核心设计：
 * 1. 每个档位独立做T，互不影响（一个被套不影响其他开新T）
 * 2. 多因子入场（价格+波动率+速度）
 * 3. 固定止盈0.3%（净利≈0.15%，抓小钱）
 * 4. 解套盈利0.15%（覆盖手续费+利润）
 * 5. 被套死扛等全平（不止损，各套各的等各自反弹）
 */
@Component
public class SmartTManager {

    @Autowired
    private JedisClient jedisClient;

    // Redis Key（按档位+序号存储，支持一个档位多个T单）
    private static final String T_ORDER_PREFIX = "t:order:";  // t:order:{symbol}:{level}:{index}
    private static final String T_INDEX_KEY = "t:index:";     // t:index:{symbol}:{level} 记录当前序号
    private static final String T_STATS_KEY = "t:global:stats";  // 统计
    private static final String PRICE_HISTORY_KEY = "t:price:history"; // 价格历史
    
    // T单参数
    private static final BigDecimal T_QTY = new BigDecimal("0.01");      // 0.01张
    private static final BigDecimal TP_TARGET = new BigDecimal("0.003");  // 止盈0.3%
    private static final BigDecimal FEE_RATE = new BigDecimal("0.001");   // 手续费0.1%
    private static final BigDecimal BK_PROFIT = new BigDecimal("0.0015"); // 解套净利0.15%
    private static final int MAX_T_ORDERS_PER_LEVEL = 3; // 一个档位最多3个T单，防止资金占用过多
    
    // 入场条件
    private static final BigDecimal DROP_THRESHOLD = new BigDecimal("0.002"); // 跌0.2%
    private static final BigDecimal VOLATILITY_THRESHOLD = new BigDecimal("0.0005"); // 波动率0.05%
    private static final int CONFIRM_COUNT = 2; // 连续2秒确认

    /**
     * 获取T单Redis Key（带序号）
     */
    private String getTOrderKey(String symbol, int level, int index) {
        return T_ORDER_PREFIX + symbol + ":" + level + ":" + index;
    }
    
    /**
     * 获取当前档位最新序号
     */
    private int getNextIndex(String symbol, int level) {
        String key = T_INDEX_KEY + symbol + ":" + level;
        String idx = jedisClient.get(key);
        if (idx == null) {
            jedisClient.setex(key, 3600, "1");
            return 1;
        }
        int next = Integer.parseInt(idx) + 1;
        jedisClient.setex(key, 3600, String.valueOf(next));
        return next;
    }

    /**
     * 检查指定档位是否有T单（任意序号）
     */
    public boolean hasTOrder(String symbol, int level) {
        // 遍历检查1-10号（一个档位最多10个T单）
        for (int i = 1; i <= 10; i++) {
            if (jedisClient.exists(getTOrderKey(symbol, level, i))) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 获取指定档位的所有T单
     */
    public List<TOrder> getTOrdersByLevel(String symbol, int level) {
        List<TOrder> orders = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            String json = jedisClient.get(getTOrderKey(symbol, level, i));
            if (json != null) {
                TOrder order = JSONUtil.toBean(json, TOrder.class);
                order.setIndex(i); // 设置序号
                orders.add(order);
            }
        }
        return orders;
    }
    
    /**
     * 获取指定档位的第一个T单
     */
    public TOrder getTOrder(String symbol, int level) {
        for (int i = 1; i <= 10; i++) {
            String json = jedisClient.get(getTOrderKey(symbol, level, i));
            if (json != null) {
                TOrder order = JSONUtil.toBean(json, TOrder.class);
                order.setIndex(i);
                return order;
            }
        }
        return null;
    }

    /**
     * 获取所有T单
     */
    public List<TOrder> getAllTOrders(String symbol) {
        List<TOrder> orders = new ArrayList<>();
        String pattern = T_ORDER_PREFIX + symbol + ":*";
        // 由于JedisClient可能不支持keys，改用遍历1-20档
        for (int level = 1; level <= 20; level++) {
            TOrder order = getTOrder(symbol, level);
            if (order != null) {
                orders.add(order);
            }
        }
        return orders;
    }

    /**
     * 多因子入场判断（每个档位独立）
     * 
     * @return 0-不满足，1-弱信号，2-强信号
     */
    public int checkEntrySignal(String symbol, int level, BigDecimal currentPrice, BigDecimal baseCost) {
        // 该档位已有T单，不新开（各档位独立）
        if (hasTOrder(symbol, level)) {
            return 0;
        }
        
        if (baseCost == null || baseCost.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }

        // 因子1：价格偏离度（当前价 < 底仓成本 × 0.998）
        BigDecimal dropPct = baseCost.subtract(currentPrice)
                .divide(baseCost, 4, RoundingMode.DOWN);
        if (dropPct.compareTo(DROP_THRESHOLD) < 0) {
            return 0;
        }
        
        // 因子2：波动率检查
        BigDecimal volatility = calculateVolatility(symbol);
        if (volatility.compareTo(VOLATILITY_THRESHOLD) < 0) {
            return 0;
        }
        
        // 因子3：下跌速度（不接飞刀）
        BigDecimal recentDrop = calculateRecentDrop(symbol, 3);
        if (recentDrop == null || recentDrop.compareTo(new BigDecimal("0.005")) > 0) {
            return 0;
        }
        
        // 因子4：确认次数
        String confirmKey = "t:confirm:" + symbol + ":" + level;
        String confirmStr = jedisClient.get(confirmKey);
        int confirmCount = confirmStr == null ? 0 : Integer.parseInt(confirmStr);
        
        if (dropPct.compareTo(DROP_THRESHOLD.multiply(new BigDecimal("1.5"))) >= 0) {
            confirmCount += 2; // 跌幅大，强信号
        } else {
            confirmCount += 1;
        }
        
        if (confirmCount >= CONFIRM_COUNT) {
            jedisClient.del(confirmKey);
            if (dropPct.compareTo(new BigDecimal("0.005")) > 0 && 
                volatility.compareTo(new BigDecimal("0.002")) > 0) {
                return 2; // 强信号
            }
            return 1; // 弱信号
        } else {
            jedisClient.setex(confirmKey, 10, String.valueOf(confirmCount));
            return 0;
        }
    }

    /**
     * 开T单（一个档位支持多个T单，各套各的，最多3个防止资金占用过多）
     */
    public TOrder openTOrder(String symbol, int level, BigDecimal buyPrice, 
                             BigDecimal huiche, String tableName, int signalStrength) {
        // 检查当前T单数量
        List<TOrder> existingOrders = getTOrdersByLevel(symbol, level);
        if (existingOrders.size() >= MAX_T_ORDERS_PER_LEVEL) {
            System.out.println("【T单限制】档位" + level + "已有" + existingOrders.size() + 
                    "个T单，达到上限" + MAX_T_ORDERS_PER_LEVEL + "，不再新开");
            return null;
        }
        
        // 获取新序号
        int index = getNextIndex(symbol, level);
        
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
        
        String key = getTOrderKey(symbol, level, index);
        jedisClient.setex(key, 3600, JSONUtil.toJsonStr(order)); // 1小时过期
        
        recordTStat("open", signalStrength);
        
        System.out.println("【T单建立】档位" + level + "-" + index + " 买入价" + buyPrice + 
                "，当前该档位T单数：" + getTOrdersByLevel(symbol, level).size());
        
        return order;
    }

    /**
     * 检查指定档位的所有T单，返回可平仓的列表
     * 
     * @return 可平仓的T单索引列表，每个元素是[index, exitSignal]
     */
    public List<int[]> checkExitSignals(String symbol, int level, BigDecimal currentPrice) {
        List<int[]> result = new ArrayList<>();
        List<TOrder> orders = getTOrdersByLevel(symbol, level);
        
        for (TOrder order : orders) {
            int index = order.getIndex();
            
            // 更新最高/最低价
            updateExtremePrices(order, currentPrice);
            
            BigDecimal buyPrice = order.getBuyPrice();
            
            // 计算盈亏比例
            BigDecimal pnlPct = currentPrice.subtract(buyPrice)
                    .divide(buyPrice, 4, RoundingMode.DOWN);
            
            // 1. 固定止盈：涨0.3%
            if (pnlPct.compareTo(TP_TARGET) >= 0) {
                result.add(new int[]{index, 1});
                continue;
            }
            
            // 2. 解套盈利：被套后反弹到覆盖手续费+0.15%净利
            BigDecimal lowestPrice = order.getLowestPrice();
            BigDecimal breakEvenWithProfit = buyPrice.multiply(
                    new BigDecimal("1").add(FEE_RATE).add(BK_PROFIT));
            if (lowestPrice != null && 
                lowestPrice.compareTo(buyPrice.multiply(new BigDecimal("0.998"))) < 0 &&
                currentPrice.compareTo(breakEvenWithProfit) >= 0) {
                result.add(new int[]{index, 2});
            }
        }
        
        return result;
    }

    /**
     * 平仓指定序号的T单
     */
    public BigDecimal closeTOrder(String symbol, int level, int index, BigDecimal sellPrice, int reason) {
        String key = getTOrderKey(symbol, level, index);
        String json = jedisClient.get(key);
        if (json == null) {
            return BigDecimal.ZERO;
        }
        
        TOrder order = JSONUtil.toBean(json, TOrder.class);
        
        // 计算利润
        BigDecimal profit = sellPrice.subtract(order.getBuyPrice())
                .multiply(order.getQty());
        
        // 记录统计
        if (reason == 1) {
            recordTStat("win_tp", 0);
        } else {
            recordTStat("win_bk", 0);
        }
        
        // 删除T单
        jedisClient.del(key);
        
        System.out.println("【T单平仓】档位" + level + "-" + index + " 利润" + profit);
        
        return profit;
    }

    /**
     * 强制平所有（全平时调用）
     */
    public BigDecimal forceCloseAll(String symbol, BigDecimal currentPrice) {
        BigDecimal totalProfit = BigDecimal.ZERO;
        int totalCount = 0;
        
        // 遍历所有档位1-20
        for (int level = 1; level <= 20; level++) {
            // 平掉该档位所有T单（1-10号）
            for (int index = 1; index <= 10; index++) {
                String key = getTOrderKey(symbol, level, index);
                String json = jedisClient.get(key);
                if (json != null) {
                    TOrder order = JSONUtil.toBean(json, TOrder.class);
                    BigDecimal profit = currentPrice.subtract(order.getBuyPrice())
                            .multiply(order.getQty());
                    totalProfit = totalProfit.add(profit);
                    totalCount++;
                    jedisClient.del(key);
                }
            }
            // 清理序号记录
            jedisClient.del(T_INDEX_KEY + symbol + ":" + level);
        }
        
        System.out.println("【全平T单】共平仓" + totalCount + "个T单，总利润" + totalProfit);
        return totalProfit;
    }

    /**
     * 更新最高/最低价
     */
    private void updateExtremePrices(TOrder order, BigDecimal currentPrice) {
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

    /**
     * 记录价格
     */
    public void recordPrice(String symbol, BigDecimal price) {
        String key = PRICE_HISTORY_KEY + ":" + symbol;
        String value = System.currentTimeMillis() + ":" + price.toString();
        jedisClient.lpush(key, value);
        jedisClient.ltrim(key, 0, 19);
        jedisClient.expire(key, 300);
    }

    /**
     * 计算波动率
     */
    private BigDecimal calculateVolatility(String symbol) {
        String key = PRICE_HISTORY_KEY + ":" + symbol;
        List<String> list = jedisClient.lrange(key, 0, -1);
        
        if (list == null || list.size() < 5) {
            return new BigDecimal("0.001");
        }
        
        long now = System.currentTimeMillis();
        List<BigDecimal> values = new ArrayList<>();
        
        for (String item : list) {
            String[] parts = item.split(":");
            if (parts.length == 2) {
                long time = Long.parseLong(parts[0]);
                if (now - time <= 10000) {
                    values.add(new BigDecimal(parts[1]));
                }
            }
        }
        
        if (values.size() < 5) {
            return new BigDecimal("0.001");
        }
        
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal v : values) {
            sum = sum.add(v);
        }
        BigDecimal avg = sum.divide(new BigDecimal(values.size()), 8, RoundingMode.DOWN);
        
        BigDecimal variance = BigDecimal.ZERO;
        for (BigDecimal v : values) {
            BigDecimal diff = v.subtract(avg);
            variance = variance.add(diff.multiply(diff));
        }
        variance = variance.divide(new BigDecimal(values.size()), 8, RoundingMode.DOWN);
        
        BigDecimal stdDev = new BigDecimal(Math.sqrt(variance.doubleValue()));
        return stdDev.divide(avg, 4, RoundingMode.DOWN);
    }

    /**
     * 计算近期跌幅
     */
    private BigDecimal calculateRecentDrop(String symbol, int seconds) {
        String key = PRICE_HISTORY_KEY + ":" + symbol;
        List<String> list = jedisClient.lrange(key, 0, -1);
        
        if (list == null || list.isEmpty()) {
            return null;
        }
        
        long now = System.currentTimeMillis();
        BigDecimal first = null;
        BigDecimal last = null;
        
        for (int i = list.size() - 1; i >= 0; i--) {
            String item = list.get(i);
            String[] parts = item.split(":");
            if (parts.length == 2) {
                long time = Long.parseLong(parts[0]);
                BigDecimal price = new BigDecimal(parts[1]);
                
                if (now - time <= seconds * 1000) {
                    if (last == null) last = price;
                    first = price;
                }
            }
        }
        
        if (first == null || last == null) {
            return null;
        }
        
        return first.subtract(last).divide(first, 4, RoundingMode.DOWN);
    }

    /**
     * 记录统计
     */
    private void recordTStat(String type, int value) {
        String date = java.time.LocalDate.now().toString();
        String key = null;
        
        if ("open".equals(type)) {
            key = T_STATS_KEY + ":open:" + date;
            jedisClient.incr(key);
            if (value == 2) {
                String strongKey = T_STATS_KEY + ":strong:" + date;
                jedisClient.incr(strongKey);
                jedisClient.expire(strongKey, 86400);
            }
        } else if ("win_tp".equals(type)) {
            key = T_STATS_KEY + ":win_tp:" + date;
            jedisClient.incr(key);
        } else if ("win_bk".equals(type)) {
            key = T_STATS_KEY + ":win_bk:" + date;
            jedisClient.incr(key);
        }
        
        if (key != null) {
            jedisClient.expire(key, 86400);
        }
    }

    /**
     * 获取今日统计
     */
    public TStats getTodayStats() {
        String date = java.time.LocalDate.now().toString();
        TStats stats = new TStats();
        stats.setOpenCount(parseInt(jedisClient.get(T_STATS_KEY + ":open:" + date)));
        stats.setWinTpCount(parseInt(jedisClient.get(T_STATS_KEY + ":win_tp:" + date)));
        stats.setWinBkCount(parseInt(jedisClient.get(T_STATS_KEY + ":win_bk:" + date)));
        stats.setStrongSignalCount(parseInt(jedisClient.get(T_STATS_KEY + ":strong:" + date)));
        return stats;
    }

    private int parseInt(String s) {
        if (s == null) return 0;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * T订单类（支持一个档位多个T单）
     */
    public static class TOrder {
        private String symbol;
        private int level;
        private int index;  // 序号，一个档位可以有多个T单：1,2,3...
        private BigDecimal buyPrice;
        private BigDecimal qty;
        private BigDecimal fene;
        private long buyTime;
        private String tableName;
        private int signalStrength;
        private BigDecimal highestPrice;
        private BigDecimal lowestPrice;

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

    /**
     * T统计类
     */
    public static class TStats {
        private int openCount;
        private int winTpCount;
        private int winBkCount;
        private int strongSignalCount;

        public int getOpenCount() { return openCount; }
        public void setOpenCount(int openCount) { this.openCount = openCount; }
        public int getWinTpCount() { return winTpCount; }
        public void setWinTpCount(int winTpCount) { this.winTpCount = winTpCount; }
        public int getWinBkCount() { return winBkCount; }
        public void setWinBkCount(int winBkCount) { this.winBkCount = winBkCount; }
        public int getStrongSignalCount() { return strongSignalCount; }
        public void setStrongSignalCount(int strongSignalCount) { this.strongSignalCount = strongSignalCount; }
        
        public int getTotalClose() {
            return winTpCount + winBkCount;
        }
        
        @Override
        public String toString() {
            return String.format("T统计[开仓:%d, 止盈0.3%%:%d, 解套盈利0.15%%:%d, 总盈利:%d, 强信号:%d]", 
                    openCount, winTpCount, winBkCount, winTpCount + winBkCount, strongSignalCount);
        }
    }
}
