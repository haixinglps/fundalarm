package cn.exrick.manager.isearch.util.common;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Convert {
	
	public static int[] str2int(String[] strs) {
		if (strs == null)
			return null;
		int[] array = new int[strs.length];
		for (int i = 0; i < strs.length; i++) {
			array[i] = getInt(strs[i]);
		}
		return array;
	}

	public static long[] str2long(String[] strs) {
		if (strs == null)
			return null;
		long[] array = new long[strs.length];
		for (int i = 0; i < strs.length; i++) {
			array[i] = getLong(strs[i]);
		}
		return array;
	}

	public static byte[] hex2byte(String strhex) {
		if (strhex == null) {
			return null;
		}
		int l = strhex.length();
		if (l % 2 == 1) {
			return null;
		}
		byte[] b = new byte[l / 2];
		for (int i = 0; i != l / 2; i++) {
			b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
		}
		return b;
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	/**
	 * extract integer from a string
	 */
	public static int extInt(String s) {
		int i = 0;
		if (s == null)
			return 0;
		Pattern p = Pattern.compile("[\\d]+");
		Matcher m = p.matcher(s);
		String it = "";
		while (m.find()) {
			it += m.group();
		}
		try {
			i = Integer.parseInt(it);
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}

	public static int getInt(String s) {
		int i = 0;
		if (s == null)
			return 0;
		try {
			i = Integer.parseInt(s);
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}

	public static byte getByte(String s) {
		byte i = 0;
		if (s == null)
			return 0;
		try {
			i = Byte.parseByte(s);
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}

	public static long getLong(String s) {
		long l = 0;
		if (s == null)
			return 0;
		try {
			l = Long.parseLong(s);
		} catch (Exception e) {
			return 0;
		}
		return l;
	}

	public static float getFloat(String s) {
		float f = 0;
		if (s == null)
			return 0;
		try {
			f = Float.parseFloat(s);
		} catch (Exception e) {
			return 0;
		}
		return f;
	}

	public static double getDouble(String s) {
		double d = 0;
		if (s == null)
			return 0;
		try {
			d = Double.parseDouble(s);
		} catch (Exception e) {
			return 0;
		}
		return d;
	}

	public static String replace(String s, int i) {
		if (i == 0)
			return s;
		if (s == null)
			return null;
		int len = s.length();
		return s.substring(0, len - i);
	}

	/**
	 * replace [r] to [t] in [str]
	 */
	public static String replace(String str, String r, String t) {
		if (r == null)
			return str;
		if (str == null)
			return str;

		int p = str.indexOf(r);
		int last = 0;
		if (p == -1)
			return str;
		StringBuffer strb = new StringBuffer(str.length() << 1);

		while (p >= 0) {
			strb.append(str.substring(last, p));
			if (t != null)
				strb.append(t);
			last = p + r.length();
			p = str.indexOf(r, last);
		}
		strb.append(str.substring(last));
		return strb.toString();
	}

	/**
	 * With regular [reg] cut [s]
	 */
	public static String[] split(String s, String reg) {
		if (s == null)
			return null;
		if (reg == null)
			return new String[] { s };
		StringTokenizer tokenizer = new StringTokenizer(s, reg);
		String[] arr = new String[tokenizer.countTokens()];
		int i = 0;
		while (tokenizer.hasMoreTokens()) {
			arr[i] = tokenizer.nextToken();
			i++;
		}
		return arr;
	}

	public static String trim(String s) {
		if (s == null)
			return null;
		int i = 0;
		for (i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c != ' ' && c != '\r' && c != '\n' && c != '\t')
				break;
		}
		int e = 0;
		for (e = s.length() - 1; e >= 0; e--) {
			char c = s.charAt(e);
			if (c != ' ' && c != '\r' && c != '\n' && c != '\t')
				break;
		}
		return s.substring(i, e + 1);
	}

}