package cn.exrick.manager.service.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESDecryptor {
	public static void main(String[] args) {
		try {
			// 配置密钥和IV（从日志中提取）
			String keyHex = "38 37 36 35 34 33 32 31 31 32 33 34 35 36 37 38";
			String ivHex = "0a 01 0b 05 04 0f 07 09 17 03 01 06 08 0c 0d 5b";

			// 5B开头的Base64密文
			String ciphertext = "5BgD4sG0EKai78ebcTnlvL90x3FbyTB9MeloGfdAZu0y5fx7Y+OXgUID/YrHaHulFzVT1QQsTB7bWfpYeT1tNmfMfEedCBagj2JzyHhXVi0=";

			// 转换密钥和IV
			byte[] keyBytes = hexStringToByteArray(keyHex.replace(" ", ""));
			byte[] ivBytes = hexStringToByteArray(ivHex.replace(" ", ""));

			// 解密处理
			byte[] decrypted = decrypt(cn.hutool.core.codec.Base64.decode(ciphertext), keyBytes, ivBytes);

			System.out.println("解密结果: " + new String(decrypted));
		} catch (Exception e) {
			System.err.println("解密失败: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static byte[] decrypt(byte[] ciphertext, byte[] key, byte[] iv) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // Java中使用PKCS5Padding对应PKCS7
		SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
		return cipher.doFinal(ciphertext);
	}

	public static byte[] hexStringToByteArray(String hex) {
		int len = hex.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
		}
		return data;
	}
}
