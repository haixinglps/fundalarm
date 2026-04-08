package cn.exrick.manager.service;

public interface OkxService {

	/**
	 * 下单接口
	 * <a href="https://www.okx.com/docs-v5/zh/#rest-api-trade-place-order">api</a>
	 * 
	 * @param request {@link Order.Request}
	 * @return {@link Order.Response}
	 */
	String trade(String path, String method, String body);

	void connectWanwuConnect();

}
