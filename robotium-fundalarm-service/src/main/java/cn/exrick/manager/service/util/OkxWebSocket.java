package cn.exrick.manager.service.util;

import java.security.NoSuchAlgorithmException;

//import com.wlf.utils.MD5Util;
//import com.wlf.ws.SubTitleWS;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

@Slf4j
//@Service
public class OkxWebSocket {

	// websocket客户端对象
	final OkHttpClient webSocketClient = new OkHttpClient.Builder().build();

	// websocket请求url
//	@Value("${subTitleUrl}")
	private String subTitleUrl = "wss://wseea.okx.com:8443/ws/v5/public";

//	@Value("${secret}")
	private String secret;

	/**
	 * 发起字幕合成
	 */
	public void liveMix(String liveId) throws NoSuchAlgorithmException {
		sendRequest(liveId);
	}

	/**
	 * 发送websocket建立连接请求
	 *
	 * @param liveId
	 */
	private void sendRequest(String liveId) throws NoSuchAlgorithmException {
//        String seed = subTitleUrl + "?liveId=" + liveId + "+" + secret;
//        String md5 = MD5Util.md5Sum(seed);
		String url = subTitleUrl;// + "?liveId=" + liveId + "&md5=" + md5;

		log.info("websocket url: {}", url);

		// 构造请求对象
		Request request = new Request.Builder().url(url).build();

		// 调用websocket服务端
		WebSocket webSocket = webSocketClient.newWebSocket(request, new OkxListen());
		webSocket.send(
				"{\"op\": \"subscribe\", \"args\": [{\"channel\": \"tickers\",\"instId\": \"SOL-USDT-SWAP\"},{\"channel\": \"tickers\",\"instId\": \"NOT-USDT-SWAP\"}]}");

	}

	public static void main(String[] args) {
		OkxWebSocket ser = new OkxWebSocket();
		try {
			ser.sendRequest(null);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}