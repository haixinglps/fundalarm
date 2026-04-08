package cn.exrick.manager.service.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class FileUtil {
	private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";

	// Base64解码+AES解密
	public static String decrypt(String encryptedData, String secretKey) throws Exception {
		// 1. Base64解码
//		byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
//		byte[] decodedBytes = GzipUtils.decompress(decodedBytesGzip).getBytes();
		byte[] decodedBytes = org.apache.commons.net.util.Base64.decodeBase64(encryptedData);
		if (isGzipped(decodedBytes)) {
			System.out.println("is gzip");
		}
		// System.out.println(new String(decodedBytes));

		// 2. AES解密
		SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
//        cipher.init(Cipher.DECRYPT_MODE, keySpec);
		cipher.init(2, keySpec, new IvParameterSpec("1264a583d5d3540f".getBytes()));

		byte[] decryptedBytes = cipher.doFinal(decodedBytes);

		return new String(decryptedBytes);
	}

	public static void main(String[] args) throws Exception {
//		String secretKey = "1264a583d5d3540f" + ""; // 16/24/32字节密钥
//		String encryptedBase64 =
//
//				"5BgD4sG0EKai78ebcTnlvL90x3FbyTB9MeloGfdAZu0y5fx7Y+OXgUID/YrHaHulFzVT1QQsTB7bWfpYeT1tNmfMfEedCBagj2JzyHhXVi0=";
//
//		// // 替换为实际密文
//
//		// System.out.println(decrypt(encryptedBase64, secretKey));
//		String res = decrypt(encryptedBase64, secretKey);
//		System.out.println(res);
		// 资源探测：
		// 20250707 房间号：188221（前后100个）
		// 20250708 房间号：

		int roomid = 189639;// 189639
		String dayString = "2025-07-09";
		int hourEnd = 18;
		int minuteEnd = 1;
		for (int i = roomid; i < roomid + 1; i++) {

			for (int hour = hourEnd; hour < hourEnd + 1; hour++) {

				for (int minute = minuteEnd; minute < 60; minute++) {
					for (int second = 0; second < 60; second++) {
						String tm = String.format("%02d-%02d-%02d", hour, minute, second);
						String pU = "http://wwlive-1335910371.cos.ap-guangzhou.myqcloud.com/room_" + i + "/" + dayString
								+ "-" + tm + "_eof.m3u8";
						System.out.printf(i + " " + dayString + " %02d:%02d:%02d" + "" + "\n", hour, minute, second);

						try {
							System.out.println(pU);
							Response res = Jsoup.connect(pU).timeout(30000).ignoreContentType(true).execute();
							if (res.statusCode() == 404) {
								System.out.println("---404----");
								continue;
							} else if (res.statusCode() == 200) {
								String body = res.body();
								System.out.println(pU);
								cn.hutool.core.io.FileUtil.appendUtf8String(roomid + "\t" + pU.toString() + "\r\n",
										"d:/ts/video.txt");
								return;
							}
						} catch (Exception e) {
							System.out.println("---404----");

							// TODO: handle exception
							// e.printStackTrace();
							continue;
						}

					}
				}
			}

		}

	}

	private static boolean isGzipped(byte[] data) {
		return data.length > 2 && (data[0] == (byte) 0x1f && data[1] == (byte) 0x8b);
	}

}
