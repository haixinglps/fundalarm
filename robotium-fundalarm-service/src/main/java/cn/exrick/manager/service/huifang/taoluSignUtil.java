package cn.exrick.manager.service.huifang;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class taoluSignUtil {
	private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	private static String bytesToHex(byte[] bArr) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < 16; i++) {
			int i2 = bArr[i];
			if (i2 < 0) {
				i2 += 256;
			}
			char[] cArr = hexDigits;
			stringBuffer.append(cArr[i2 >>> 4]);
			stringBuffer.append(cArr[i2 % 16]);
		}
		return stringBuffer.toString();
	}

	public static String encrypt(String str, int i) throws NoSuchAlgorithmException {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(System.getProperty("MD5.algorithm", "MD5"));
			if (i == 16) {
				return bytesToHex(messageDigest.digest(str.getBytes("utf-8"))).substring(8, 24);
			}
			return bytesToHex(messageDigest.digest(str.getBytes("utf-8")));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String md5(String str) throws NoSuchAlgorithmException, MalformedURLException {
		String signKey = "jGZH2Yf77YHpcyeZ";
		String pathString = new URL(str).getPath().toLowerCase();
		System.out.println(pathString);
		long tm = System.currentTimeMillis() / 1000;
//		tm = 1759419053;
//		String pathString = "/live/live/video/list/1/20";
		String strEncrypt = encrypt(pathString + "-" + tm + "-eb4daf8ec6704abcbb7620c11175683a-0-" + signKey, 32);
		System.out.println(strEncrypt);
		String result = "?sign=" + tm + "-eb4daf8ec6704abcbb7620c11175683a-0-" + strEncrypt;
		return result;
	}

	public static void main(String[] args) {
		try {
			System.out.println(md5("http://testlive.yueji.pro/user/userInfo/getUserInfo"));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
