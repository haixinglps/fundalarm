package cn.exrick.manager.service.huifang;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

public class AESDecryption {
	public static void main(String[] args) {
		try {
			String encB64 = "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";
			String keyB64 = "9GgjZ4Z+fT99OhyP2cOTvybkGvakK7a8oHlWSmMebLl9hYFo7Hpnpp9ldM9CY0j4";
			String ivB64 = "u+HXFlLjaFNbIHskyM2Hxg==";

			byte[] key = Base64.getDecoder().decode(keyB64);
			byte[] iv = Base64.getDecoder().decode(ivB64);

			// SecretKeySpec secretKey =aes.get
			// new SecretKeySpec(key, "AES");
			IvParameterSpec ivSpec = new IvParameterSpec(iv);

			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE,
					aes.getRawKey("9GgjZ4Z+fT99OhyP2cOTvybkGvakK7a8oHlWSmMebLl9hYFo7Hpnpp9ldM9CY0j4"), ivSpec);

			byte[] encrypted = Base64.getDecoder().decode(encB64);
			byte[] decrypted = cipher.doFinal(encrypted);

			System.out.println("解密结果: " + new String(decrypted, StandardCharsets.UTF_8));
		} catch (Exception e) {
			System.err.println("解密失败: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
