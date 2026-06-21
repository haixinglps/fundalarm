#!/usr/bin/env python3
"""
OKX REST API Client
通用封装：行情查询 + 交易执行 + 账户查询
支持模拟盘与实盘切换
"""

import os
import sys
import json
import hmac
import hashlib
import base64
import time
from datetime import datetime, timezone
from urllib.parse import urlencode
import urllib.request
import urllib.error


class OKXClient:
    """OKX API 客户端"""

    def __init__(self, api_key=None, api_secret=None, passphrase=None, simulated=True):
        """
        初始化客户端
        :param api_key: API Key
        :param api_secret: API Secret
        :param passphrase: API Passphrase
        :param simulated: True=模拟盘, False=实盘
        """
        self.api_key = api_key or os.environ.get('OKX_API_KEY', '')
        self.api_secret = api_secret or os.environ.get('OKX_API_SECRET', '')
        self.passphrase = passphrase or os.environ.get('OKX_PASSPHRASE', '')
        self.simulated = simulated

        self.base_url = 'https://www.okx.com'
        if simulated:
            # 模拟盘需要在请求头加 x-simulated-trading: 1
            pass

    def _signature(self, timestamp, method, request_path, body=''):
        """生成 HMAC-SHA256 Base64 签名"""
        if body and isinstance(body, dict):
            body = json.dumps(body, separators=(',', ':'))
        message = timestamp + method.upper() + request_path + (body or '')
        mac = hmac.new(
            self.api_secret.encode('utf-8'),
            message.encode('utf-8'),
            hashlib.sha256
        )
        return base64.b64encode(mac.digest()).decode('utf-8')

    def _request(self, method, path, params=None, body=None):
        """发送 HTTP 请求"""
        timestamp = datetime.now(timezone.utc).strftime('%Y-%m-%dT%H:%M:%S.%f')[:-3] + 'Z'
        request_path = path
        if params:
            request_path += '?' + urlencode(params)

        url = self.base_url + request_path
        headers = {
            'OK-ACCESS-KEY': self.api_key,
            'OK-ACCESS-SIGN': self._signature(timestamp, method, request_path, body or ''),
            'OK-ACCESS-TIMESTAMP': timestamp,
            'OK-ACCESS-PASSPHRASE': self.passphrase,
            'Content-Type': 'application/json',
        }
        if self.simulated:
            headers['x-simulated-trading'] = '1'

        data = None
        if body:
            data = json.dumps(body, separators=(',', ':')).encode('utf-8')

        headers['User-Agent'] = 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'
        req = urllib.request.Request(url, data=data, headers=headers, method=method.upper())

        try:
            with urllib.request.urlopen(req, timeout=30) as resp:
                return json.loads(resp.read().decode('utf-8'))
        except urllib.error.HTTPError as e:
            err_body = e.read().decode('utf-8')
            return {'code': str(e.code), 'msg': err_body}
        except Exception as e:
            return {'code': '-1', 'msg': str(e)}

    # ==================== 市场数据（无需签名） ====================

    def get_ticker(self, inst_id):
        """获取最新Ticker"""
        return self._request('GET', '/api/v5/market/ticker', {'instId': inst_id})

    def get_candles(self, inst_id, bar='5m', limit=30):
        """获取K线数据
        bar: 1m/3m/5m/15m/30m/1H/2H/4H/6H/1D
        """
        return self._request('GET', '/api/v5/market/candles', {
            'instId': inst_id, 'bar': bar, 'limit': str(limit)
        })

    def get_books(self, inst_id, sz=5):
        """获取订单簿"""
        return self._request('GET', '/api/v5/market/books', {'instId': inst_id, 'sz': str(sz)})

    # ==================== 账户接口 ====================

    def get_account_balance(self, ccy=''):
        """获取账户余额"""
        params = {'ccy': ccy} if ccy else {}
        return self._request('GET', '/api/v5/account/balance', params)

    def get_positions(self, inst_type='SWAP', inst_id=''):
        """获取持仓信息"""
        params = {'instType': inst_type}
        if inst_id:
            params['instId'] = inst_id
        return self._request('GET', '/api/v5/account/positions', params)

    # ==================== 交易接口 ====================

    def place_order(self, inst_id, td_mode, side, ord_type, sz, **kwargs):
        """下单
        :param inst_id: 交易对，如 BTC-USDT-SWAP
        :param td_mode: 交易模式 cash/isolated/cross
        :param side: buy/sell
        :param ord_type: market/limit/post_only/fok/ioc
        :param sz: 数量
        :param kwargs: 其他参数（如 px=价格, posSide=long/short）
        """
        body = {
            'instId': inst_id,
            'tdMode': td_mode,
            'side': side,
            'ordType': ord_type,
            'sz': str(sz),
        }
        body.update(kwargs)
        return self._request('POST', '/api/v5/trade/order', body=body)

    def cancel_order(self, inst_id, ord_id=''):
        """撤单"""
        body = {'instId': inst_id}
        if ord_id:
            body['ordId'] = ord_id
        return self._request('POST', '/api/v5/trade/cancel-order', body=body)

    def close_position(self, inst_id, mgn_mode, pos_side=''):
        """一键平仓"""
        body = {'instId': inst_id, 'mgnMode': mgn_mode}
        if pos_side:
            body['posSide'] = pos_side
        return self._request('POST', '/api/v5/trade/close-position', body=body)

    def get_order_history(self, inst_type='SWAP', limit=100):
        """查询历史订单"""
        params = {'instType': inst_type, 'limit': str(limit)}
        return self._request('GET', '/api/v5/trade/orders-history', params)


def main():
    """命令行入口"""
    import argparse
    parser = argparse.ArgumentParser(description='OKX API CLI')
    parser.add_argument('--key', default=os.environ.get('OKX_API_KEY', ''), help='API Key')
    parser.add_argument('--secret', default=os.environ.get('OKX_API_SECRET', ''), help='API Secret')
    parser.add_argument('--passphrase', default=os.environ.get('OKX_PASSPHRASE', ''), help='Passphrase')
    parser.add_argument('--live', action='store_true', help='实盘模式（默认模拟盘）')
    parser.add_argument('action', choices=[
        'ticker', 'candles', 'books', 'balance', 'positions',
        'order', 'cancel', 'close', 'history'
    ])
    parser.add_argument('--inst-id', default='', help='交易对')
    parser.add_argument('--bar', default='5m', help='K线周期')
    parser.add_argument('--limit', type=int, default=30, help='数量限制')
    parser.add_argument('--side', default='buy', help='buy/sell')
    parser.add_argument('--sz', default='', help='下单数量')
    parser.add_argument('--td-mode', default='isolated', help='cash/isolated/cross')
    parser.add_argument('--ord-type', default='market', help='market/limit/post_only')
    parser.add_argument('--px', default='', help='限价单价格')
    parser.add_argument('--pos-side', default='', help='long/short')

    args = parser.parse_args()

    if not args.key or not args.secret or not args.passphrase:
        print("错误: 缺少 API Key/Secret/Passphrase")
        print("请设置环境变量 OKX_API_KEY, OKX_API_SECRET, OKX_PASSPHRASE")
        sys.exit(1)

    client = OKXClient(args.key, args.secret, args.passphrase, simulated=not args.live)

    if args.action == 'ticker':
        r = client.get_ticker(args.inst_id)
    elif args.action == 'candles':
        r = client.get_candles(args.inst_id, args.bar, args.limit)
    elif args.action == 'books':
        r = client.get_books(args.inst_id, args.limit)
    elif args.action == 'balance':
        r = client.get_account_balance()
    elif args.action == 'positions':
        r = client.get_positions(inst_id=args.inst_id)
    elif args.action == 'order':
        kwargs = {}
        if args.px:
            kwargs['px'] = args.px
        if args.pos_side:
            kwargs['posSide'] = args.pos_side
        r = client.place_order(args.inst_id, args.td_mode, args.side, args.ord_type, args.sz, **kwargs)
    elif args.action == 'cancel':
        r = client.cancel_order(args.inst_id)
    elif args.action == 'close':
        r = client.close_position(args.inst_id, args.td_mode, args.pos_side)
    elif args.action == 'history':
        r = client.get_order_history(limit=args.limit)
    else:
        r = {'msg': '未知操作'}

    print(json.dumps(r, indent=2, ensure_ascii=False))


if __name__ == '__main__':
    main()
