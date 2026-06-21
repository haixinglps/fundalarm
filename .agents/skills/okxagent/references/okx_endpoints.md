# OKX 常用 API 端点速查

## 市场数据（Public，无需签名）

| 端点 | 方法 | 说明 | 关键参数 |
|------|------|------|---------|
| `/api/v5/market/ticker` | GET | 最新Ticker | `instId` |
| `/api/v5/market/tickers` | GET | 批量Ticker | `instType` |
| `/api/v5/market/candles` | GET | K线数据 | `instId`, `bar`, `limit` |
| `/api/v5/market/books` | GET | 订单簿 | `instId`, `sz` |
| `/api/v5/market/trades` | GET | 最新成交 | `instId`, `limit` |

**K线周期 bar**: `1m/3m/5m/15m/30m/1H/2H/4H/6H/1D/1W/1M`

## 账户（Account，需签名）

| 端点 | 方法 | 说明 | 关键参数 |
|------|------|------|---------|
| `/api/v5/account/balance` | GET | 账户余额 | `ccy`（可选） |
| `/api/v5/account/positions` | GET | 持仓信息 | `instType`, `instId` |
| `/api/v5/account/config` | GET | 账户配置 | - |

## 交易（Trade，需签名）

| 端点 | 方法 | 说明 | 关键参数 |
|------|------|------|---------|
| `/api/v5/trade/order` | POST | 下单 | `instId`, `tdMode`, `side`, `ordType`, `sz` |
| `/api/v5/trade/cancel-order` | POST | 撤单 | `instId`, `ordId` |
| `/api/v5/trade/close-position` | POST | 一键平仓 | `instId`, `mgnMode`, `posSide` |
| `/api/v5/trade/orders-pending` | GET | 当前挂单 | `instType` |
| `/api/v5/trade/orders-history` | GET | 历史订单 | `instType`, `limit` |

## 订单类型 ordType

| 类型 | 说明 |
|------|------|
| `market` | 市价单 |
| `limit` | 限价单（需传 px） |
| `post_only` | 只做Maker |
| `fok` | 全部成交或撤销 |
| `ioc` | 立即成交并取消剩余 |

## 交易模式 tdMode

| 模式 | 说明 |
|------|------|
| `cash` | 现货 |
| `isolated` | 逐仓合约 |
| `cross` | 全仓合约 |

## 持仓方向 posSide

| 值 | 说明 |
|----|------|
| `long` | 做多 |
| `short` | 做空 |
| `net` | 单向持仓（如现货） |

## 常用合约代码

| 代码 | 说明 |
|------|------|
| `BTC-USDT-SWAP` | BTC 永续合约 |
| `ETH-USDT-SWAP` | ETH 永续合约 |
| `DOGE-USDT-SWAP` | DOGE 永续合约 |
| `XAUT-USDT-SWAP` | 黄金币永续合约 |
