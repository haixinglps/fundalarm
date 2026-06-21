#!/bin/bash
# 清空DOGE仓位脚本

echo "================================"
echo "清空 DOGE 仓位"
echo "================================"

# MySQL清理 - 清空fene（份额）和重置状态
echo "1. 清理数据库中的DOGE仓位..."
mysql -uroot -e "
USE robotium_test;
-- 显示清理前的状态
SELECT '清理前' as status, COUNT(*) as total, SUM(CASE WHEN fene > 0 THEN fene ELSE 0 END) as total_fene 
FROM fund_doge_swap;

-- 清空所有仓位的fene（份额）
UPDATE fund_doge_swap SET 
    fene = 0,
    iscurrent = 0,
    buyprice_real = 0,
    maxprice5 = 0,
    minprice5 = 0,
    maxpriceniu = 0,
    zhiying = 0,
    comment = CONCAT(comment, ' [已清仓]');

-- 显示清理后的状态
SELECT '清理后' as status, COUNT(*) as total, SUM(CASE WHEN fene > 0 THEN fene ELSE 0 END) as total_fene 
FROM fund_doge_swap;
" 2>/dev/null

if [ $? -eq 0 ]; then
    echo "✓ 数据库清理成功"
else
    echo "✗ 数据库清理失败，请检查MySQL连接"
fi

# Redis清理
echo ""
echo "2. 清理Redis中的DOGE相关数据..."

# 获取所有DOGE相关的key
echo "   查找DOGE相关的Redis key..."
REDIS_KEYS=$(redis-cli KEYS "*DOGE*" 2>/dev/null)

if [ -n "$REDIS_KEYS" ]; then
    echo "   找到以下key，正在删除:"
    echo "$REDIS_KEYS" | while read key; do
        echo "     删除: $key"
        redis-cli DEL "$key" > /dev/null 2>&1
    done
    echo "✓ Redis清理完成"
else
    echo "   没有找到DOGE相关的Redis key"
fi

# 额外清理T交易相关key
echo ""
echo "3. 清理T交易相关Redis数据..."
T_KEYS=$(redis-cli KEYS "t:*DOGE*" 2>/dev/null)
if [ -n "$T_KEYS" ]; then
    echo "$T_KEYS" | while read key; do
        echo "     删除: $key"
        redis-cli DEL "$key" > /dev/null 2>&1
    done
fi

# 清理分批止盈相关key
BATCH_KEYS=$(redis-cli KEYS "batch:*doge*" 2>/dev/null)
if [ -n "$BATCH_KEYS" ]; then
    echo "$BATCH_KEYS" | while read key; do
        echo "     删除: $key"
        redis-cli DEL "$key" > /dev/null 2>&1
    done
fi

echo ""
echo "================================"
echo "DOGE仓位清理完成"
echo "================================"
