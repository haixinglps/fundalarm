#!/usr/bin/env python3
"""
ANTHROPIC-USDT-SWAP ATR自适应顺势T交易脚本
基于 DailyProfitTManager 逻辑，适配 Pre-IPO 高波动特性
"""

import urllib.request
import json
import hmac
import hashlib
import base64
import time
import sys
import os
from decimal import Decimal, ROUND_HALF_UP

# ============ 配置 ============
INST_ID = "ANTHROPIC-USDT-SWAP"
TD_MODE = "isolated"
POS_SIDE = "long"
LEVER = 3                       # 5x → 3x，降低爆仓风险

API_KEY = "dcd08b32-9450-4100-bc83-a989feadaed4"
SECRET_KEY = "35DC54ED1F023E51F082CC6AF8985342"
PASSPHRASE = "~89*0374~6512*aI"
HOST = "https://www.okx.com"

# ATR 自适应参数
TP_ATR_MULT = Decimal("3.0")    # 止盈 = ATR × 3（让利润奔跑）
SL_ATR_MULT = Decimal("1.5")    # 止损 = ATR × 1.5（给足够空间）
TRAIL_PCT = Decimal("0.5")      # 移动止盈：回撤 50% 最大浮盈
MIN_ATR_PCT = Decimal("0.3")    # ATR% < 0.3% 暂停交易（低波动无效）

# 过滤参数
RSI_MIN = Decimal("35")         # 放宽到 35（高波动币超卖反弹多）
RSI_MAX = Decimal("70")         # 放宽到 70（强势币可更超买）
VOL_RATIO_MIN = Decimal("0.8")  # 量比门槛提高，避免缩量假突破
MAX_POSITIONS = 1
CHECK_INTERVAL = 60             # 60秒（减少噪音）

# 风控
MAX_DAILY_LOSS = Decimal("5")   # 日亏损上限 5U
POSITION_SZ = Decimal("0.01")   # 固定开仓量

# 信号确认
EXIT_CONFIRM = 2                # MACD死叉需连续确认 2 次才平仓

# 状态
last_exit_count = 0
max_profit_pct = Decimal("0")
daily_loss = Decimal("0")
last_trade_day = ""

LOG_FILE = "/tmp/anthropic_trend_atr.log"


def log(msg):
    t = time.strftime("%H:%M:%S")
    line = f"[{t}] {msg}"
    print(line)
    with open(LOG_FILE, "a") as f:
        f.write(line + "\n")


def okx_request(method, path, body_str="", signed=True):
    timestamp = time.strftime("%Y-%m-%dT%H:%M:%S.000Z", time.gmtime())
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64)",
        "Content-Type": "application/json"
    }
    if signed:
        message = timestamp + method + path + body_str
        mac = hmac.new(SECRET_KEY.encode('utf-8'), message.encode('utf-8'), hashlib.sha256)
        sign = base64.b64encode(mac.digest()).decode('utf-8')
        headers.update({
            "OK-ACCESS-KEY": API_KEY,
            "OK-ACCESS-SIGN": sign,
            "OK-ACCESS-TIMESTAMP": timestamp,
            "OK-ACCESS-PASSPHRASE": PASSPHRASE,
        })
    try:
        req = urllib.request.Request(
            HOST + path,
            data=body_str.encode('utf-8') if body_str else None,
            headers=headers,
            method=method
        )
        with urllib.request.urlopen(req, timeout=30) as resp:
            return json.loads(resp.read().decode('utf-8'))
    except urllib.error.HTTPError as e:
        return {"error": f"HTTP {e.code}", "detail": e.read().decode('utf-8')}


def get_klines(bar="5m", limit=100):
    path = f"/api/v5/market/candles?instId={INST_ID}&bar={bar}&limit={limit}"
    data = okx_request("GET", path, signed=False)
    klines = data.get('data', [])
    if not klines:
        return None
    klines = list(reversed(klines))
    return {
        'ts': [int(k[0]) for k in klines],
        'open': [Decimal(k[1]) for k in klines],
        'high': [Decimal(k[2]) for k in klines],
        'low': [Decimal(k[3]) for k in klines],
        'close': [Decimal(k[4]) for k in klines],
        'vol': [Decimal(k[5]) for k in klines],
    }


def get_ticker():
    data = okx_request("GET", f"/api/v5/market/ticker?instId={INST_ID}", signed=False)
    d = data.get('data', [{}])[0]
    return Decimal(d.get('last', 0)) if d else None


def get_position():
    data = okx_request("GET", f"/api/v5/account/positions?instId={INST_ID}")
    for p in data.get('data', []):
        if p.get('posSide') == POS_SIDE and Decimal(p.get('pos', 0)) > 0:
            return {
                'sz': Decimal(p['pos']),
                'avgPx': Decimal(p.get('avgPx', 0)),
                'upl': Decimal(p.get('upl', 0)),
                'bePx': Decimal(p.get('bePx', 0)),
            }
    return None


def get_balance():
    """获取 USDT 余额"""
    data = okx_request("GET", "/api/v5/account/balance?ccy=USDT")
    for d in data.get('data', []):
        for detail in d.get('details', []):
            if detail.get('ccy') == 'USDT':
                return Decimal(detail.get('availEq', 0))
    return Decimal("0")


def ema(data, period):
    if len(data) < period:
        return None
    mult = Decimal(2) / Decimal(period + 1)
    val = sum(data[:period]) / Decimal(period)
    for price in data[period:]:
        val = (price - val) * mult + val
    return val


def rsi(closes, period=14):
    if len(closes) < period + 1:
        return None
    gains, losses = [], []
    for i in range(1, period + 1):
        ch = closes[-period - 1 + i] - closes[-period - 2 + i]
        gains.append(max(ch, Decimal(0)))
        losses.append(max(-ch, Decimal(0)))
    avg_g = sum(gains) / Decimal(period)
    avg_l = sum(losses) / Decimal(period)
    for i in range(len(closes) - period - 1, len(closes) - 1):
        ch = closes[i + 1] - closes[i]
        avg_g = (avg_g * Decimal(period - 1) + max(ch, Decimal(0))) / Decimal(period)
        avg_l = (avg_l * Decimal(period - 1) + max(-ch, Decimal(0))) / Decimal(period)
    if avg_l == 0:
        return Decimal(100)
    return Decimal(100) - Decimal(100) / (Decimal(1) + avg_g / avg_l)


def atr(highs, lows, closes, period=14):
    if len(closes) < period + 1:
        return None
    trs = []
    for i in range(1, len(closes)):
        trs.append(max(highs[i] - lows[i], abs(highs[i] - closes[i-1]), abs(lows[i] - closes[i-1])))
    return sum(trs[-period:]) / Decimal(period)


def macd_state(closes):
    if len(closes) < 26:
        return None, None
    macd_hist = []
    for i in range(26, len(closes) + 1):
        e12 = ema(closes[:i], 12)
        e26 = ema(closes[:i], 26)
        if e12 and e26:
            macd_hist.append(e12 - e26)
    if len(macd_hist) < 9:
        return None, None
    sig = ema(macd_hist, 9)
    cur_hist = macd_hist[-1] - sig if sig else None
    prev_hist = None
    if len(macd_hist) > 9:
        prev_sig = ema(macd_hist[:-1], 9)
        prev_hist = macd_hist[-2] - prev_sig if prev_sig else None
    return cur_hist, prev_hist


def check_eod():
    now = time.localtime()
    return now.tm_hour == 23 and now.tm_min >= 55


def get_today():
    return time.strftime("%Y%m%d")


def place_order(side, sz, ord_type="market", sl_px=None, tp_px=None):
    body = {
        "instId": INST_ID,
        "tdMode": TD_MODE,
        "side": side,
        "ordType": ord_type,
        "sz": str(sz),
        "posSide": POS_SIDE,
    }
    if sl_px:
        body["slTriggerPx"] = str(sl_px)
        body["slOrdPx"] = "-1"
    if tp_px:
        body["tpTriggerPx"] = str(tp_px)
        body["tpOrdPx"] = "-1"

    result = okx_request("POST", "/api/v5/trade/order", json.dumps(body))
    return result


def main():
    global last_exit_count, max_profit_pct, daily_loss, last_trade_day

    auto = "--auto" in sys.argv
    log(f"ANTHROPIC ATR自适应顺势T启动 | auto={'ON' if auto else 'OFF'}")
    log(f"  止盈=ATR×{TP_ATR_MULT} 止损=ATR×{SL_ATR_MULT} 移动止盈回撤{float(TRAIL_PCT)*100}%")
    log(f"  最低ATR={float(MIN_ATR_PCT)}% 检查间隔={CHECK_INTERVAL}s 杠杆={LEVER}x")
    log("=" * 60)

    while True:
        try:
            today = get_today()
            if today != last_trade_day:
                daily_loss = Decimal("0")
                last_trade_day = today
                log(f"【新的一天】日亏损重置: 0")

            # 日终强平（修复：加 auto 判断）
            if auto and check_eod():
                log("【日终强平】23:55 到了")
                pos = get_position()
                if pos:
                    log(f"强制平仓 {pos['sz']} 张")
                    r = place_order("sell", pos['sz'])
                    log(f"平仓结果: {r.get('msg', r)}")
                time.sleep(300)
                continue
            elif check_eod():
                log("【日终】23:55 到了，但 auto=OFF，不强制平仓")
                time.sleep(300)
                continue

            # 日亏损硬停
            if daily_loss <= -MAX_DAILY_LOSS:
                log(f"【日亏损硬停】已亏损 {daily_loss}U，当日停止交易")
                time.sleep(300)
                continue

            # 获取数据
            ticker_price = get_ticker()
            k = get_klines()
            if not k or not ticker_price:
                log("⚠️ 数据获取失败，跳过")
                time.sleep(CHECK_INTERVAL)
                continue

            closes = k['close']
            highs = k['high']
            lows = k['low']
            vols = k['vol']
            price = ticker_price

            # 计算指标
            sma10 = sum(closes[-10:]) / Decimal(10) if len(closes) >= 10 else None
            rsi14 = rsi(closes, 14)
            atr14 = atr(highs, lows, closes, 14)
            hist, prev_hist = macd_state(closes)

            vol_ratio = None
            if len(vols) >= 25:
                vol_ratio = (sum(vols[-5:]) / 5) / (sum(vols[-25:-5]) / 20) if sum(vols[-25:-5]) > 0 else Decimal(1)

            atr_pct = (atr14 / price * 100) if atr14 else None

            # ATR 低波动暂停
            if atr_pct and atr_pct < MIN_ATR_PCT:
                log(f"⏸️ 低波动暂停 | ATR={atr_pct:.2f}% < {float(MIN_ATR_PCT)}%")
                time.sleep(CHECK_INTERVAL)
                continue

            # 检查持仓
            pos = get_position()

            if pos:
                # ===== 持仓中：ATR自适应止盈止损 + 移动止盈 =====
                entry = pos['avgPx']
                sz = pos['sz']
                pnl_pct = (price - entry) / entry

                # 更新最大浮盈
                if pnl_pct > max_profit_pct:
                    max_profit_pct = pnl_pct

                # ATR 自适应止盈止损
                tp_price = entry + atr14 * TP_ATR_MULT if atr14 else entry * (Decimal(1) + Decimal("0.03"))
                sl_price = entry - atr14 * SL_ATR_MULT if atr14 else entry * (Decimal(1) - Decimal("0.015"))

                # 保本机制：浮盈达到止损金额后，止损移到成本价
                actual_sl = sl_price
                be_sl_triggered = False
                sl_pct_at_entry = (entry - sl_price) / entry  # 原始止损百分比
                if pnl_pct >= sl_pct_at_entry:
                    actual_sl = entry
                    be_sl_triggered = True

                # 移动止盈：从最大浮盈回撤 TRAIL_PCT
                trail_sl = entry * (Decimal(1) + max_profit_pct * (Decimal(1) - TRAIL_PCT))
                if trail_sl > actual_sl:
                    actual_sl = trail_sl

                log(f"持仓 {sz}张 @{entry} | 现价 {price} | 盈亏 {float(pnl_pct)*100:.2f}% | 最大浮盈 {float(max_profit_pct)*100:.2f}% | TP={tp_price:.1f} SL={actual_sl:.1f}{' 保本' if be_sl_triggered else ''}")

                exit_reason = None
                if price >= tp_price:
                    exit_reason = "ATR止盈"
                elif price <= actual_sl:
                    if be_sl_triggered and actual_sl == entry:
                        exit_reason = "保本止损"
                    else:
                        exit_reason = "ATR止损"
                elif hist is not None and hist < 0:
                    # MACD 死叉需连续确认
                    last_exit_count += 1
                    if last_exit_count >= EXIT_CONFIRM:
                        exit_reason = f"MACD死叉({EXIT_CONFIRM}次确认)"
                        last_exit_count = 0
                    else:
                        log(f"   MACD走弱({last_exit_count}/{EXIT_CONFIRM}次)，继续观察")
                else:
                    last_exit_count = 0

                if exit_reason:
                    log(f"🚨 【{exit_reason}触发】平仓！")
                    if auto:
                        r = place_order("sell", sz)
                        if r.get('code') == '0':
                            realized = float(pnl_pct) * float(entry) * float(sz)
                            daily_loss += Decimal(str(realized))
                            log(f"✅ 平仓成功 | 实现盈亏: {realized:.3f}U | 日累计: {daily_loss:.3f}U")
                        else:
                            log(f"❌ 平仓失败: {r}")
                    else:
                        log("⚠️ 非自动模式，请手动下单或加 --auto")
                    max_profit_pct = Decimal("0")
                    last_exit_count = 0

            else:
                # 空仓状态重置
                max_profit_pct = Decimal("0")
                last_exit_count = 0

                # ===== 空仓：检查开仓信号 =====
                signals = []
                blocked = False

                # 1. SMA10 过滤
                if sma10 and price < sma10 * Decimal("0.995"):
                    blocked = True
                    signals.append("低于SMA10")
                elif sma10:
                    deviation = (price - sma10) / sma10
                    if deviation > Decimal("0.02"):  # 追高超过 2% 阻止
                        blocked = True
                        signals.append(f"追高({float(deviation)*100:.1f}%)")
                    else:
                        signals.append("SMA10正常")

                # 2. RSI 过滤
                if rsi14 is not None:
                    if rsi14 > RSI_MAX:
                        blocked = True
                        signals.append(f"RSI高({rsi14:.1f})")
                    elif rsi14 < RSI_MIN:
                        blocked = True
                        signals.append(f"RSI低({rsi14:.1f})")
                    else:
                        signals.append(f"RSI正常({rsi14:.1f})")

                # 3. MACD
                macd_ok = False
                if hist is not None and prev_hist is not None:
                    if hist > 0 and prev_hist <= 0:
                        macd_ok = True
                        signals.append("MACD金叉")
                    elif hist > 0 and hist > prev_hist:
                        macd_ok = True
                        signals.append("MACD扩大")
                    elif hist > 0:
                        signals.append("MACD红柱")
                    else:
                        blocked = True
                        signals.append("MACD绿柱")
                elif hist is not None and hist > 0:
                    macd_ok = True
                    signals.append("MACD红柱(新)")
                else:
                    blocked = True
                    signals.append("MACD无信号")

                # 4. 量比
                if vol_ratio is not None and vol_ratio < VOL_RATIO_MIN:
                    blocked = True
                    signals.append(f"缩量({vol_ratio:.2f})")
                elif vol_ratio is not None:
                    signals.append(f"量比{vol_ratio:.2f}")

                # 综合判断
                can_open = not blocked and macd_ok and sma10 and rsi14 and rsi14 >= RSI_MIN and rsi14 <= RSI_MAX

                status = "❌ BLOCKED" if blocked else ("🟢 可开仓" if can_open else "🟡 等待")
                sma_str = f"{sma10:.1f}" if sma10 else "?"
                rsi_str = f"{rsi14:.1f}" if rsi14 else "?"
                atr_str = f"{atr_pct:.2f}" if atr_pct else "?"
                log(f"{status} | 价{price} SMA10{sma_str} RSI{rsi_str} ATR{atr_str}% | {' '.join(signals)}")

                if can_open:
                    log("=" * 60)
                    log("🚀 【开仓信号】顺势T触发！")
                    log(f"   买入价: {price}")
                    tp_val = price + atr14 * TP_ATR_MULT if atr14 else price * (Decimal(1) + Decimal("0.03"))
                    sl_val = price - atr14 * SL_ATR_MULT if atr14 else price * (Decimal(1) - Decimal("0.015"))
                    log(f"   止盈: {tp_val:.1f} (ATR×{TP_ATR_MULT})")
                    log(f"   止损: {sl_val:.1f} (ATR×{SL_ATR_MULT})")
                    log(f"   移动止盈: 回撤 {float(TRAIL_PCT)*100}% 最大浮盈")
                    log("=" * 60)

                    if auto:
                        r = place_order("buy", POSITION_SZ)
                        if r.get('code') == '0':
                            log(f"✅ 开仓成功: {r['data'][0]['ordId']}")
                        else:
                            log(f"❌ 开仓失败: {r}")
                    else:
                        log("⚠️ 非自动模式，请手动下单或加 --auto")

        except Exception as e:
            log(f"异常: {e}")
            import traceback
            traceback.print_exc()

        time.sleep(CHECK_INTERVAL)


if __name__ == "__main__":
    main()
