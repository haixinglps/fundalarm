#!/usr/bin/env python3
"""
ANTHROPIC-USDT-SWAP 顺势 T 监控脚本
只做多（右侧交易），不持底仓过夜
"""

import urllib.request
import json
import hmac
import hashlib
import base64
import time
import sys
from decimal import Decimal, ROUND_HALF_UP

# ============ 配置 ============
INST_ID = "ANTHROPIC-USDT-SWAP"
TD_MODE = "isolated"
POS_SIDE = "long"
LEVER = 5

# API 凭证（从 application.properties 读取）
API_KEY = "dcd08b32-9450-4100-bc83-a989feadaed4"
SECRET_KEY = "35DC54ED1F023E51F082CC6AF8985342"
PASSPHRASE = "~89*0374~6512*aI"
HOST = "https://www.okx.com"

# 顺势 T 参数（适配 ANTHROPIC 高波动）
TP_PCT = Decimal("0.03")      # 止盈 3%
SL_PCT = Decimal("0.015")     # 止损 1.5%
RSI_MIN = Decimal("40")
RSI_MAX = Decimal("65")
VOL_RATIO_MIN = Decimal("0.5")
MAX_POSITIONS = 1
CHECK_INTERVAL = 15           # 秒

# 状态
position = None               # {entry_price, sz, open_time}
last_signal = None


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
    # 转为正序（旧 → 新）
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


def sma(data, period):
    if len(data) < period:
        return None
    return sum(data[-period:]) / Decimal(period)


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
    prev_hist = macd_hist[-2] - ema(macd_hist[:-1], 9) if len(macd_hist) > 9 else None
    return cur_hist, prev_hist


def check_eod():
    now = time.localtime()
    return now.tm_hour == 23 and now.tm_min >= 55


def place_order(side, sz, ord_type="market", px=None, sl_px=None, tp_px=None):
    body = {
        "instId": INST_ID,
        "tdMode": TD_MODE,
        "side": side,
        "ordType": ord_type,
        "sz": str(sz),
        "posSide": POS_SIDE,
    }
    if px:
        body["px"] = str(px)
    if sl_px:
        body["slTriggerPx"] = str(sl_px)
        body["slOrdPx"] = "-1"
    if tp_px:
        body["tpTriggerPx"] = str(tp_px)
        body["tpOrdPx"] = "-1"

    result = okx_request("POST", "/api/v5/trade/order", json.dumps(body))
    return result


def log(msg):
    t = time.strftime("%H:%M:%S")
    print(f"[{t}] {msg}")


def main():
    auto = "--auto" in sys.argv
    log(f"ANTHROPIC 顺势 T 启动 | auto={'ON' if auto else 'OFF'} | 止盈={float(TP_PCT)*100}% | 止损={float(SL_PCT)*100}%")
    log("=" * 60)

    while True:
        try:
            # 日终强平
            if check_eod():
                log("【日终强平】23:55 到了")
                pos = get_position()
                if pos:
                    log(f"强制平仓 {pos['sz']} 张")
                    r = place_order("sell", pos['sz'])
                    log(f"平仓结果: {r.get('msg', r)}")
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
            sma10 = sma(closes, 10)
            rsi14 = rsi(closes, 14)
            atr14 = atr(highs, lows, closes, 14)
            hist, prev_hist = macd_state(closes)

            vol_ratio = None
            if len(vols) >= 25:
                vol_ratio = (sum(vols[-5:]) / 5) / (sum(vols[-25:-5]) / 20) if sum(vols[-25:-5]) > 0 else Decimal(1)

            deviation = None
            if sma10 and sma10 > 0:
                deviation = (price - sma10) / sma10

            atr_pct = (atr14 / price * 100) if atr14 else None

            # 检查持仓
            pos = get_position()

            if pos:
                # ===== 持仓中：检查止盈止损 =====
                entry = pos['avgPx']
                sz = pos['sz']
                pnl_pct = (price - entry) / entry
                tp_price = entry * (Decimal(1) + TP_PCT)
                sl_price = entry * (Decimal(1) - SL_PCT)

                log(f"持仓 {sz}张 @{entry} | 现价 {price} | 盈亏 {float(pnl_pct)*100:.2f}% | TP={tp_price:.1f} SL={sl_price:.1f}")

                exit_reason = None
                if price >= tp_price:
                    exit_reason = "止盈"
                elif price <= sl_price:
                    exit_reason = "止损"
                elif hist is not None and hist < 0 and prev_hist is not None and prev_hist > 0:
                    # MACD 死叉
                    exit_reason = "MACD死叉"

                if exit_reason:
                    log(f"🚨 【{exit_reason}触发】平仓！")
                    if auto:
                        r = place_order("sell", sz)
                        log(f"平仓结果: {r.get('msg', r)}")
                    else:
                        log("⚠️ 非自动模式，请手动下单或加 --auto")

            else:
                # ===== 空仓：检查开仓信号 =====
                signals = []
                blocked = False

                # 1. SMA10 过滤
                if sma10 and price < sma10 * Decimal("0.998"):
                    blocked = True
                    signals.append("低于SMA10")
                elif sma10 and deviation is not None and atr14:
                    max_dev = max(atr14 * Decimal("1.5"), price * Decimal("0.003"))
                    if deviation * price > max_dev:
                        blocked = True
                        signals.append(f"追高(偏离{(deviation*100):.2f}%)")
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
                    tp_val = price * (Decimal(1) + TP_PCT)
                    sl_val = price * (Decimal(1) - SL_PCT)
                    log(f"   止盈: {tp_val:.1f} (+{float(TP_PCT)*100}%)")
                    log(f"   止损: {sl_val:.1f} (-{float(SL_PCT)*100}%)")
                    log("=" * 60)

                    if auto:
                        # 开仓 0.01 张（最小单位，控制仓位）
                        sz = Decimal("0.01")
                        r = place_order("buy", sz)
                        if r.get('code') == '0':
                            log(f"✅ 开仓成功: {r['data'][0]['ordId']}")
                        else:
                            log(f"❌ 开仓失败: {r}")
                    else:
                        log("⚠️ 非自动模式，请手动下单或加 --auto")

        except Exception as e:
            log(f"异常: {e}")

        time.sleep(CHECK_INTERVAL)


if __name__ == "__main__":
    main()
