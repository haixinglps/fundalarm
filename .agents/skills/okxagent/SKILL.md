---
name: okxagent
description: OKX加密货币交易所REST API调用技能。支持行情查询（Ticker/K线/订单簿）、账户查询（余额/持仓）、交易执行（下单/撤单/一键平仓）。使用场景：(1) 需要查询OKX市场数据或账户状态，(2) 需要执行交易操作（下单/平仓/撤单），(3) 需要调试或验证OKX API响应，(4) FundAlarm交易系统中涉及OKX接口的任何操作。脚本使用Python标准库，无需额外依赖。
---

# OKX Agent

## 快速开始

### 1. 环境变量配置

设置 API 认证信息（建议写入 `~/.bashrc`）：

```bash
export OKX_API_KEY="your-api-key"
export OKX_API_SECRET="your-api-secret"
export OKX_PASSPHRASE="your-passphrase"
```

### 2. 命令行调用

```bash
# 查询行情
python3 scripts/okx_api.py ticker --inst-id DOGE-USDT-SWAP

# 获取K线
python3 scripts/okx_api.py candles --inst-id XAUT-USDT-SWAP --bar 5m --limit 30

# 查询持仓
python3 scripts/okx_api.py positions --inst-id DOGE-USDT-SWAP

# 市价开多（模拟盘）
python3 scripts/okx_api.py order --inst-id DOGE-USDT-SWAP --side buy --sz 0.01 --td-mode isolated --pos-side long

# 一键全平
python3 scripts/okx_api.py close --inst-id DOGE-USDT-SWAP --td-mode isolated --pos-side long

# 实盘模式加 --live
python3 scripts/okx_api.py order --live --inst-id ...
```

### 3. Python 代码调用

```python
from scripts.okx_api import OKXClient

client = OKXClient(simulated=True)  # 模拟盘
# 或 client = OKXClient(simulated=False)  # 实盘

# 查询Ticker
print(client.get_ticker('DOGE-USDT-SWAP'))

# 查询持仓
print(client.get_positions(inst_id='DOGE-USDT-SWAP'))

# 市价下单
print(client.place_order(
    inst_id='DOGE-USDT-SWAP',
    td_mode='isolated',
    side='buy',
    ord_type='market',
    sz='0.01',
    posSide='long'
))

# 一键平仓
print(client.close_position(
    inst_id='DOGE-USDT-SWAP',
    mgn_mode='isolated',
    pos_side='long'
))
```

## 核心方法

| 方法 | 用途 | 示例 |
|------|------|------|
| `get_ticker(inst_id)` | 最新行情 | `get_ticker('DOGE-USDT-SWAP')` |
| `get_candles(inst_id, bar, limit)` | K线数据 | `get_candles('DOGE-USDT-SWAP', '5m', 30)` |
| `get_positions(inst_id='')` | 持仓查询 | `get_positions(inst_id='DOGE-USDT-SWAP')` |
| `place_order(...)` | 下单 | 见上方示例 |
| `cancel_order(inst_id, ord_id)` | 撤单 | `cancel_order('DOGE-USDT-SWAP', '12345')` |
| `close_position(inst_id, mgn_mode, pos_side)` | 一键平仓 | `close_position('DOGE-USDT-SWAP', 'isolated', 'long')` |
| `get_account_balance()` | 账户余额 | `get_account_balance()` |

## 签名机制

OKX API 使用 HMAC-SHA256 Base64 签名：

```
Sign = Base64(HMAC-SHA256(timestamp + method + request_path + body, secret))
```

脚本已自动处理签名生成和请求头组装，无需手动计算。

## 模拟盘 vs 实盘

| 模式 | 说明 | 标识 |
|------|------|------|
| 模拟盘 | 默认，测试用，不消耗真实资金 | `x-simulated-trading: 1` |
| 实盘 | 需显式开启 `--live`，真实资金 | 无模拟盘标识 |

## 参考文档

- **完整端点列表**: 见 `references/okx_endpoints.md`
- **官方文档**: https://www.okx.com/docs-v5/en/#rest-api
