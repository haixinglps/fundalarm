package cn.exrick.manager.service.util;

import java.io.IOException;

//import com.alibaba.fastjson.JSONObject;
//import com.wlf.entity.WSResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

@Slf4j
public class OkxListen extends WebSocketListener {

	/**
	 * websocket连接建立后来到这个方法
	 *
	 * @param webSocket
	 * @param response
	 */
	int status = 0;

	@Override
	public void onOpen(WebSocket webSocket, Response response) {
		super.onOpen(webSocket, response);

		// 打印连接建立后的响应
		try {
			log.info(response.body().string());
		} catch (IOException e) {
			log.error("call onOpen failed, error :{}", e.getMessage());

		}
	}

	@Override
	public void onMessage(WebSocket webSocket, String text) {
		super.onMessage(webSocket, text);

		log.info("receive=>" + text);

		// 处理返回数据
//        WSResponse response = (WSResponse) JSONObject.parse(text);

//        if (response != null) {
//            if (!"200".equals(response.getCode())) {
//                log.error("code=> :{}, error=> :{}", response.getCode(), response.getMessage());
//                return;
//            }
//        }
	}

	@Override
	public void onMessage(WebSocket webSocket, ByteString bytes) {
		log.info("receive OK =>" + bytes);
		super.onMessage(webSocket, bytes);
	}

	@Override
	public void onClosing(WebSocket webSocket, int code, String reason) {
		super.onClosing(webSocket, code, reason);
		log.info("socket closing.");
	}

	@Override
	public void onClosed(WebSocket webSocket, int code, String reason) {
		super.onClosed(webSocket, code, reason);
		log.info("socket closed.");
	}

	@Override
	public void onFailure(WebSocket webSocket, Throwable t, Response response) {
		super.onFailure(webSocket, t, response);
		if (response == null) {
			log.error("onFailure, response is null.");
			return;
		}
		try {
			log.error("onFailure, code: {}, errmsg: {}", response.code(), response.body().string());
		} catch (IOException e) {
			log.warn("onFailure failed, error: {}", e.getMessage());
		}
	}
}