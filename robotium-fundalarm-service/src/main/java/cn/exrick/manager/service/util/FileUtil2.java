package cn.exrick.manager.service.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FileUtil2 {
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
		System.out.println(new String(decodedBytes));

		// 2. AES解密
		SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
//        cipher.init(Cipher.DECRYPT_MODE, keySpec);
		cipher.init(2, keySpec, new IvParameterSpec("bA&$8HPI7y0o2Rk&".getBytes()));

		byte[] decryptedBytes = cipher.doFinal(decodedBytes);

		return new String(decryptedBytes);
	}

	public static void main(String[] args) throws Exception {

	}

	private static boolean isGzipped(byte[] data) {
		return data.length > 2 && (data[0] == (byte) 0x1f && data[1] == (byte) 0x8b);
	}

}
