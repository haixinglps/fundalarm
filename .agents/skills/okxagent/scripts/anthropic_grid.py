#!/usr/bin/env python3
"""
ANTHROPIC-USDT-SWAP 多层网格策略
- 不止损，多层网格摊薄成本
- 每层独立低买高卖
- 跌破 1000 全平止损（唯一止损条件）
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
LEVER = 3

API_KEY = "dcd08b32-9450-4100-bc83-a989feadaed4"
SECRET_KEY = "35DC54ED1F023E51F082CC6AF8985342"
PASSPHRASE = "~89*0374~6512*aI"
HOST = "https://www.okx.com"

# 网格参数
GRID_LAYERS = 20                # 网格层数
GRID_SPACING_PCT = Decimal("0.02")   # 每层间距 2%
LAYER_SZ = Decimal("0.01")           # 每层仓位
TRAIL_PCT = Decimal("0.3")           # 移动止盈：从该层最高价回撤 30%
LIQUIDATION_PRICE = Decimal("1000")  # 跌破 1000 全平
MAX_TOTAL_SZ = LAYER_SZ * GRID_LAYERS  # 最大总仓位 0.2

CHECK_INTERVAL = 60             # 检查间隔 60秒

# 状态文件
STATE_FILE = "/tmp/anthropic_grid_state.json"
LOG_FILE = "/tmp/anthropic_grid.log"


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
            }
    return None


def place_order(side, sz, ord_type="market"):
    body = {
        "instId": INST_ID,
        "tdMode": TD_MODE,
        "side": side,
        "ordType": ord_type,
        "sz": str(sz),
        "posSide": POS_SIDE,
    }
    return okx_request("POST", "/api/v5/trade/order", json.dumps(body))


def close_all_positions():
    """市价全平"""
    pos = get_position()
    if pos and pos['sz'] > 0:
        r = place_order("sell", pos['sz'])
        log(f"🚨 【跌破1000全平】平仓 {pos['sz']} 张，结果: {r.get('msg', r)}")
        return r
    return None


# ============ 网格状态管理 ============

def load_state():
    if os.path.exists(STATE_FILE):
        try:
            with open(STATE_FILE, "r") as f:
                return json.load(f)
        except:
            pass
    return None


def save_state(state):
    with open(STATE_FILE, "w") as f:
        json.dump(state, f, indent=2)


def init_grid_layers(base_price):
    """基于基准价格初始化网格层"""
    layers = []
    for i in range(GRID_LAYERS):
        buy_price = base_price * (Decimal(1) - GRID_SPACING_PCT) ** (i + 1)
        buy_price = buy_price.quantize(Decimal("0.1"), rounding=ROUND_HALF_UP)
        layers.append({
            "layer": i + 1,
            "buy_price": float(buy_price),
            "sz": float(LAYER_SZ),
            "filled": False,      # 是否已买入
            "closed": False,      # 是否已平仓卖出
            "max_price": None,    # 该层经历的最高价（用于移动止盈）
        })
    return layers


def get_grid_summary(layers):
    filled = sum(1 for l in layers if l['filled'] and not l['closed'])
    closed = sum(1 for l in layers if l['closed'])
    total_sz = sum(l['sz'] for l in layers if l['filled'] and not l['closed'])
    return filled, closed, total_sz


def main():
    auto = "--auto" in sys.argv
    log("=" * 60)
    log(f"ANTHROPIC 多层网格启动 | auto={'ON' if auto else 'OFF'}")
    log(f"  网格层数: {GRID_LAYERS} | 间距: {float(GRID_SPACING_PCT)*100}%")
    log(f"  移动止盈: 从每层最高价回撤 {float(TRAIL_PCT)*100}%")
    log(f"  每层仓位: {LAYER_SZ} | 跌破 {LIQUIDATION_PRICE} 全平")
    log(f"  状态文件: {STATE_FILE}")
    log("=" * 60)

    state = load_state()
    if not state:
        log("【初始化】首次运行，等待价格建立网格...")
        # 首次运行时不立即建仓，等获取到价格后再初始化
        state = {"initialized": False, "layers": [], "base_price": None}

    while True:
        try:
            price = get_ticker()
            if not price:
                log("⚠️ 价格获取失败，跳过")
                time.sleep(CHECK_INTERVAL)
                continue

            log(f"现价: {price}")

            # ===== 跌破 1000 全平（唯一止损）=====
            if price < LIQUIDATION_PRICE:
                log(f"🚨🚨🚨 价格 {price} 跌破 {LIQUIDATION_PRICE}，执行全平！")
                if auto:
                    close_all_positions()
                    # 清空状态
                    save_state({"liquidated": True, "price": float(price), "time": time.strftime("%Y-%m-%d %H:%M:%S")})
                    log("【策略终止】已全平并保存状态，请人工检查")
                    break
                else:
                    log("⚠️ auto=OFF，请手动全平！")
                time.sleep(CHECK_INTERVAL)
                continue

            # ===== 初始化网格 =====
            if not state.get("initialized"):
                state["base_price"] = float(price)
                state["layers"] = init_grid_layers(price)
                state["initialized"] = True
                state["created_at"] = time.strftime("%Y-%m-%d %H:%M:%S")
                save_state(state)
                log(f"【网格建立】基准价: {price}")
                for l in state["layers"]:
                    log(f"  第{l['layer']}层: 买入≤{l['buy_price']} | 移动止盈: 回撤{float(TRAIL_PCT)*100}%")
                log("=" * 60)

            layers = state["layers"]
            filled_count, closed_count, total_sz = get_grid_summary(layers)

            log(f"持仓层数: {filled_count} | 已止盈: {closed_count} | 总持仓: {total_sz} 张")

            # ===== 逐层检查 =====
            for layer in layers:
                buy_px = Decimal(str(layer['buy_price']))
                sz = Decimal(str(layer['sz']))

                # 1. 未买入且价格触及买入线 → 买入
                if not layer['filled'] and not layer['closed']:
                    if price <= buy_px:
                        log(f"🛒 【第{layer['layer']}层触发】价格 {price} ≤ 买入价 {buy_px}")
                        if auto:
                            r = place_order("buy", sz)
                            if r.get('code') == '0':
                                layer['filled'] = True
                                layer['fill_price'] = float(price)
                                layer['fill_time'] = time.strftime("%H:%M:%S")
                                layer['max_price'] = float(price)
                                save_state(state)
                                log(f"✅ 买入成功: {sz}张 @ {price}")
                            else:
                                log(f"❌ 买入失败: {r}")
                        else:
                            log("⚠️ auto=OFF，请手动买入")

                # 2. 已买入未平仓 → 更新最高价 + 检查移动止盈
                elif layer['filled'] and not layer['closed']:
                    fill_px = Decimal(str(layer.get('fill_price', layer['buy_price'])))
                    MIN_PROFIT_PCT = Decimal("0.01")  # 浮盈超过 1% 才启用移动止盈

                    # 更新该层最高价
                    if layer.get('max_price') is None or price > Decimal(str(layer['max_price'])):
                        layer['max_price'] = float(price)

                    max_px = Decimal(str(layer['max_price']))
                    pnl_pct = (price - fill_px) / fill_px
                    max_pnl_pct = (max_px - fill_px) / fill_px

                    # 只有最大浮盈 >= 1% 才启用移动止盈，否则不触发任何卖出（除非跌破 1000）
                    if max_pnl_pct >= MIN_PROFIT_PCT:
                        # 计算移动止盈价：从最高价回撤 TRAIL_PCT
                        trail_sl = max_px - (max_px - fill_px) * TRAIL_PCT
                        trail_sl = trail_sl.quantize(Decimal("0.1"), rounding=ROUND_HALF_UP)

                        log(f"持仓 第{layer['layer']}层 {sz}张 @{fill_px} | 现价 {price} | 盈亏 {float(pnl_pct)*100:.2f}% | 最高 {max_px} | 移动止盈 {trail_sl}")

                        if price <= trail_sl:
                            log(f"💰 【第{layer['layer']}层移动止盈】价格 {price} ≤ 移动止盈价 {trail_sl} (从最高 {max_px} 回撤 {float(TRAIL_PCT)*100}%)")
                            if auto:
                                r = place_order("sell", sz)
                                if r.get('code') == '0':
                                    layer['closed'] = True
                                    layer['close_price'] = float(price)
                                    layer['close_time'] = time.strftime("%H:%M:%S")
                                    log(f"✅ 平仓成功: {sz}张 @ {price} | 该层盈亏: {float(pnl_pct)*100:.2f}% | 最高浮盈: {float(max_pnl_pct)*100:.2f}%")
                                    save_state(state)
                                else:
                                    log(f"❌ 平仓失败: {r}")
                            else:
                                log("⚠️ auto=OFF，请手动卖出")
                    else:
                        # 浮盈不足 1%，不启用移动止盈，也不保本平仓
                        log(f"持仓 第{layer['layer']}层 {sz}张 @{fill_px} | 现价 {price} | 盈亏 {float(pnl_pct)*100:.2f}% | 最高 {max_px} | 浮盈不足1%，继续持有")

            # 3. 检查是否需要重新置网格（所有层都止盈后）
            all_closed = all(l['closed'] for l in layers)
            if all_closed:
                log("🔄 【全部止盈】所有网格层已完成，重新建立网格...")
                state["base_price"] = float(price)
                state["layers"] = init_grid_layers(price)
                state["reinit_at"] = time.strftime("%Y-%m-%d %H:%M:%S")
                save_state(state)
                log(f"【新网格建立】基准价: {price}")
                for l in state["layers"]:
                    log(f"  第{l['layer']}层: 买入≤{l['buy_price']} | 移动止盈: 回撤{float(TRAIL_PCT)*100}%")

        except Exception as e:
            log(f"异常: {e}")
            import traceback
            traceback.print_exc()

        time.sleep(CHECK_INTERVAL)


if __name__ == "__main__":
    main()
