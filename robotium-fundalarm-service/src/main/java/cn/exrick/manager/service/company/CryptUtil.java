package cn.exrick.manager.service.company;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.core.codec.Base64Decoder;

//import sun.misc.BASE64Decoder;

public class CryptUtil {
	private static final Logger logger = LoggerFactory.getLogger(CryptUtil.class);
	private static final String KEY_ALGORITHM = "AES";
	private static final String CHAR_SET = "UTF-8";
	private static final String IV = "0000000000000000";

	/**
	 * 加解密算法/工作模式/填充方式
	 */
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";// 默认的加密算法

	/**
	 * AES 加密操作
	 *
	 * @param content 待加密内容
	 * @param key     加密密钥
	 * @return 返回Base64转码后的加密数据
	 */
	public static String encrypt(String content, String key) {
		if (StringUtils.isBlank(content) || StringUtils.isBlank(key)) {
			logger.error("AES encryption params is empty");
			return null;
		}
		try {
			// 创建密码器
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

			byte[] byteContent = content.getBytes(CHAR_SET);

			// 初始化为加密模式的密码器
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), KEY_ALGORITHM),
					new IvParameterSpec(IV.getBytes()));

			// 加密
			byte[] result = cipher.doFinal(byteContent);

			// 通过Base64转码返回
			return Base64.encodeBase64String(result);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("AES encryption operation has exception,content:{},key:{}", content, e);
		}

		return null;
	}

	/**
	 * AES 解密
	 *
	 * @param content 待解密内容
	 * @param key     加密密钥
	 * @return 解密后的数据
	 */
	public static String decrypt(String content, String key) {
		if (StringUtils.isBlank(content) || StringUtils.isBlank(key)) {
			logger.error("AES encryption params is empty");
			return null;
		}

		try {
			// 实例化
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

			// 使用密钥初始化，设置为解密模式
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), KEY_ALGORITHM),
					new IvParameterSpec(IV.getBytes()));

			// 处理Base64解码后的长度校验
			byte[] encrypted = new Base64Decoder().decode(content);
			// .decodeBuffer(content);
			System.out.println("解码后长度：" + encrypted.length); // 输出应为256字节

			if (encrypted.length % 16 != 0) {
				throw new IllegalArgumentException("Invalid cipher text length");
			}

			// 执行操作
			byte[] result = cipher.doFinal(encrypted);

			return new String(result, CHAR_SET);
		} catch (Exception e) {
			logger.error("AES encryption operation has exception,content:{},key:{}", content, e);
		}

		return null;
	}

	/**
	 * 签名
	 *
	 * @param plainString 签名字符串
	 * @return 签名值
	 */
	public static String sha256(String plainString) {
		String cipherString = null;
		try {
			// 获取实例（SHA-512同理）
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			// 计算摘要
			byte[] cipherBytes = messageDigest.digest(plainString.getBytes(StandardCharsets.UTF_8));
			// 输出为16进制字符串
			StringBuilder sb = new StringBuilder();
			for (byte b : cipherBytes) {
				sb.append(String.format("%02x", b));
			}
			cipherString = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cipherString;
	}

	public static void main(String[] args) {
		String key = "d2df4fe22f5053846cf4049b9198b1cd";
		// key = "0123456789abcdef";
		String content = "123456";
		// AES加密、解密
		System.out.println("原始内容：" + content);

		String encrypted = encrypt(content, key);
		System.out.println("加密内容：" + encrypted);

//		String decrypted = decrypt(encrypted, key);
//		System.out.println("解密内容：" + decrypted);
//		// SHA256签名
//		String sign = sha256(content + key);
//		System.out.println("签名：" + sign);
	}
}