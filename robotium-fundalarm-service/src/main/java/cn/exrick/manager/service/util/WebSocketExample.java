package cn.exrick.manager.service.util;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketExample {
	static WebSocketClient webSocketClient = null;

	public static void main(String[] args) {

//		System.setProperty("http.proxyHost", "localhost");
//		System.setProperty("http.proxyPort", "7890");
//		System.setProperty("https.proxyHost", "localhost");
//		System.setProperty("https.proxyPort", "7890");
////
//		System.setProperty("proxyType", "4");
//		System.setProperty("proxyPort", "7890");
//		System.setProperty("proxyHost", "127.0.0.1");
//		System.setProperty("proxySet", "true");

		try {
			HttpsUrlValidator.trustAllHttpsCertificates();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			URI uri = new URI("wss://wseea.okx.com:8443/ws/v5/public"); // 替换为实际的WebSocket服务器URI Proxy proxy = new
																		// Proxy(Proxy.Type.SOCKS, new
																		// InetSocketAddress("your.proxy.host",
																		// 8080));
			Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("localhost", 7890));

			webSocketClient = new WebSocketClient(uri, new Draft_6455(), null, 60000) {// , new Draft_6455(), null,
																						// 60000
				@Override
				public void onOpen(ServerHandshake handshakedata) {
					System.out.println("新连接已打开"
							+ "{\"op\": \"subscribe\", \"args\": [{\"channel\": \"tickers\",\"instId\": \"SOL-USDT-SWAP\"}]}");
//					send("ping"); // 发送消息
					send("{\"op\": \"subscribe\", \"args\": [{\"channel\": \"tickers\",\"instId\": \"SOL-USDT-SWAP\"}]}");
				}

				@Override
				public void onMessage(String message) {
					System.out.println("接收到消息: " + message);
				}

				@Override
				public void onClose(int code, String reason, boolean remote) {
					System.out.println("连接已关闭:" + reason);
				}

				@Override
				public void onError(Exception ex) {
					ex.printStackTrace();
				}
			};

//			webSocketClient.setProxy(proxy);

			webSocketClient.connectBlocking(); // 连接到WebSocket服务器

			webSocketClient.setConnectionLostTimeout(16); // 设置5秒超时

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void send(String message) {
		// 如果连接已打开，则发送消息
		if (webSocketClient.isOpen()) {
			webSocketClient.send(message);
		} else {
			System.out.println("WebSocket连接尚未建立，无法发送消息。");
		}
	}
}
