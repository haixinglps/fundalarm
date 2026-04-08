package cn.exrick.manager.service.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SignatureGenerator {
	public static String generateSignature(String timestamp, String method, String requestPath, String body,
			String secretKey) throws Exception {
		// 字符串连接
		String dataString = timestamp + method + requestPath + body;

		// 创建密钥规范
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

		// 创建HmacSHA256的Mac实例
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(secretKeySpec);

		// 对数据进行加密
		byte[] bytes = mac.doFinal(dataString.getBytes(StandardCharsets.UTF_8));

		// 对加密后的字节进行Base64编码
		String sign = Base64.getEncoder().encodeToString(bytes);

		return sign;
	}

	public static void main(String[] args) throws Exception {
//    	OK-ACCESS-KEY字符串类型的APIKey。
//
//    	OK-ACCESS-SIGN使用HMAC SHA256哈希函数获得哈希值，再使用Base-64编码（请参阅签名）。
//
//    	OK-ACCESS-TIMESTAMP发起请求的时间（UTC），如：2020-12-08T09:08:57.715Z
//
//    	OK-ACCESS-PASSPHRASE您在创建API密钥时指定的Passphrase。
		String timestamp = "1625123456"; // 假设的时间戳 2020-12-08T09:08:57.715Z
//							1716111714
		String method = "GET"; // 请求方法
		String requestPath = "/api/v5/account/balance?ccy=BTC"; // 请求路径
		String body = ""; // 请求体（对于GET请求，这通常是空的）
		String secretKey = "your-secret-key"; // 您的SecretKey

		String sign = generateSignature(timestamp, method, requestPath, body, secretKey);
		System.out.println("Sign: " + sign); // 输出签名

		String apikey = "f2caee79-82e8-40e2-838c-d5531a223584";

	}

}