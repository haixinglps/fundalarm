package cn.exrick.manager.service.huifang;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class jiema2 {

	public static String getvideourl(String packedJs) throws Exception {
		// 输入你的 eval 字符串
		// String packedJs = "eval(function(p,a,c,k,e,d){e=function(c){return
		// c.toString(36)};if(!''.replace(/^/,String)){while(c--){d[c.toString(a)]=k[c]||c.toString(a)}k=[function(e){return
		// d[e]}];e=function(){return'\\\\w+'};c=1};while(c--){if(k[c]){p=p.replace(new
		// RegExp('\\\\b'+e(c)+'\\\\b','g'),k[c])}}return p}('(r(){2.3(\"<0
		// 4=\\\\\"/5/6?7=8&9=%a%b%c%1.d&f=&g=\"+h(\"i+j/k=\")+\"&l=\"+m((n
		// o()).p()/q/e)+\"\\\\\"><\\\\/0>\")})()',28,28,'script|2F2022081109235248059|document|write|src|videos|detail_play|id|20643|img|2Fnew|2Fxiao|2F20220811|jpeg|1800|ads|u|encodeURIComponent|fjLJ0|qM1uCesB6i3KuewSRMo|s65QWvWvM0p2iyD9bQG6e5xs58TNZ1f0FjV2H3HyUrzqTtPh1zQ1L3ibHTnhuzuc3Jylt39zFNejV6xng|t|parseInt|new|Date|getTime|1000|function'.split('|'),0,{}))";

		// 解析参数
//		String[] params = extractParams(packedJs);
//		String p = params[0]; // 压缩代码
//		int a = Integer.parseInt(params[1]);
//		int c = Integer.parseInt(params[2]);
//		String[] k = params[3].split("\\|");
//
//		//System.out.println("p (packed code): " + p.substring(0, 50) + "...");
//		//System.out.println("a: " + a);
//		//System.out.println("c: " + c);
//		//System.out.println("k length: " + k.length);
//
//		// 构建解码表
//		Map<String, String> dict = new HashMap<>();
//		for (int i = 0; i < k.length; i++) {
//			String key = encodeKey(i, a);
//			dict.put(key, k[i]);
//		}
//
//		// 解码 p
//		String decoded = decodePackedString(p, dict);

		// 提取参数
		String[] params = extractParams(packedJs);
		String p = params[0];
		int a = Integer.parseInt(params[1]);
		int c = Integer.parseInt(params[2]);
		String[] k = params[3].split("\\|", -1); // -1 保留空字符串

		// System.out.println("Parameters extracted:");
		// System.out.println("a = " + a + ", c = " + c + ", k.length = " + k.length);

		// 严格按照 JS 逻辑解码
		String decoded = decodePacker(p, a, c, k);

		// System.out.println("\n=== Decoded JavaScript ===\n" + decoded);
		int beginIndex = decoded.indexOf("/videos/detail");
		int endIndex = decoded.indexOf("&u=");
		String pre = decoded.substring(beginIndex, endIndex);

		// 提取并解码 u 参数
//		String uEncoded = extractUParam(packedJs);
//		//System.out.println("\n=== u parameter (encoded) ===\n" + uEncoded);
		beginIndex = decoded.indexOf("encodeURIComponent(\"");
		endIndex = decoded.indexOf("\")+\"&t=\"");
		String u = decoded.substring(beginIndex + 20, endIndex);
		/// u=encodeURIComponent(u);
//		String uDecoded = decodeUParam(uEncoded, dict);
//		//System.out.println("\n=== u parameter (decoded) ===\n" + uDecoded);

		// 构建 URL
		String url = buildUrl(pre, u);
		// System.out.println("\n=== Final URL ===\n" + url);
		return url;
	}

	// 严格按照 Packer 逻辑解码
	private static String decodePacker(String p, int a, int c, String[] k) {
		// 第一阶段：构建 d 映射表
		Map<String, String> d = new HashMap<>();

		// e(c) 函数
		java.util.function.IntFunction<String> e = (int c1) -> {
			StringBuilder sb = new StringBuilder();
			int c2 = c1;
			while (c2 >= a) {
				int remainder = c2 % a;
				if (remainder > 35) {
					sb.insert(0, (char) (remainder + 29));
				} else {
					sb.insert(0, Integer.toString(remainder, 36));
				}
				c2 = c2 / a;
			}
			if (c2 > 35) {
				sb.insert(0, (char) (c2 + 29));
			} else {
				sb.insert(0, Integer.toString(c2, 36));
			}
			return sb.toString();
		};

		// while(c--) { d[e(c)] = k[c] || e(c) }
		for (int i = c - 1; i >= 0; i--) {
			String key = e.apply(i);
			String val = (k[i] != null && !k[i].isEmpty()) ? k[i] : key;
			d.put(key, val);
			// System.out.println("d[" + key + "] = " + val.substring(0,
			// Math.min(val.length(), 30)));
		}

		// 第二阶段：替换
		// k = [function(e) { return d[e] }]
		// e = function() { return '\\w+' }
		// c = 1
		// while(c--) { if(k[c]) p = p.replace(...) }

		// 实际上第二阶段 c=1，只循环一次，但 e(c) 返回 \w+ 不匹配任何内容
		// 真正的替换在第一阶段已经完成？不对...

		// 重新理解：第二阶段后，e 返回 \w+，但 k[c] 是 function，所以：
		// 实际上应该直接用 d 映射表替换 p 中的单词

		// 按 key 长度降序，避免短 key 干扰长 key
		List<String> keys = new ArrayList<>(d.keySet());
		keys.sort((s1, s2) -> s2.length() - s1.length());

		String result = p;
		for (String key : keys) {
			String val = d.get(key);
			// 使用正则 \bkey\b 替换
			String regex = "\\b" + Pattern.quote(key) + "\\b";
			result = result.replaceAll(regex, Matcher.quoteReplacement(val));
		}

		return result;
	}

	/**
	 * 等效 JavaScript 的 encodeURIComponent
	 */
	public static String encodeURIComponent(String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString()).replace("+", "%20") // 空格必须是 %20 不是 +
					.replace("%21", "!").replace("%27", "'").replace("%28", "(").replace("%29", ")").replace("%7E", "~")
					.replace("%2A", "*");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 从 eval 字符串中提取参数
	private static String[] extractParams(String packedJs) {
		// 匹配 eval(function(...){...}('p',a,c,'k'.split('|'),0,{}))
		Pattern pattern = Pattern.compile(
				"eval\\(function\\(p,a,c,k,e,d\\)\\{.*?\\}\\('([^']+)'\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*'([^']+)'\\.split\\('\\|'\\)\\s*,\\s*0\\s*,\\s*\\{\\}\\)\\)");
		Matcher matcher = pattern.matcher(packedJs);

		if (!matcher.find()) {
			throw new IllegalArgumentException("无法解析 packed JS，请检查格式");
		}

		return new String[] { matcher.group(1), // p
				matcher.group(2), // a
				matcher.group(3), // c
				matcher.group(4) // k (pipe separated)
		};
	}

	// 从原始字符串中提取 u 参数的编码形式
	private static String extractUParam(String packedJs) {
		// 找到 j("...") 中的内容
		Pattern pattern = Pattern.compile("j\\(\"([^\"]+)\"\\)");
		Matcher matcher = pattern.matcher(packedJs);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return "a+k/m+F+E/D/C/B+A+/z+y++x/w/v/s="; // 默认值
	}

	// e(c) 函数实现
	private static String encodeKey(int c, int a) {
		if (c < a) {
			if (c > 35) {
				return String.valueOf((char) (c + 29)); // A-G
			}
			return Integer.toString(c, 36); // 0-9, a-z
		}
		return encodeKey(c / a, a) + encodeKey(c % a, a);
	}

	// 解码压缩字符串
	private static String decodePackedString(String p, Map<String, String> dict) {
		// 按 key 长度降序排序，避免短 key 干扰
		String[] keys = dict.keySet().stream().sorted((a, b) -> b.length() - a.length()).toArray(String[]::new);

		String result = p;
		for (String key : keys) {
			String val = dict.get(key);
			// 转义正则特殊字符
			String regex = "\\b" + key.replaceAll("([\\\\.\\[\\{\\}\\(\\)\\*\\+\\?\\^\\$\\|])", "\\\\$1") + "\\b";
			result = result.replaceAll(regex, val);
		}

		return result;
	}

	// 解码 u 参数
	private static String decodeUParam(String uEncoded, Map<String, String> dict) {
		StringBuilder result = new StringBuilder();
		int i = 0;

		while (i < uEncoded.length()) {
			boolean matched = false;

			// 优先尝试匹配2字符的 key（如 10, 11, 1a, 1b 等）
			if (i + 2 <= uEncoded.length()) {
				String key2 = uEncoded.substring(i, i + 2);
				if (dict.containsKey(key2)) {
					result.append(dict.get(key2));
					i += 2;
					matched = true;
				}
			}

			// 尝试1字符的 key
			if (!matched && i < uEncoded.length()) {
				String key1 = uEncoded.substring(i, i + 1);
				if (dict.containsKey(key1)) {
					result.append(dict.get(key1));
					i += 1;
					matched = true;
				}
			}

			// 未匹配，保留原字符（+ / = 等）
			if (!matched) {
				char ch = uEncoded.charAt(i);
				result.append(ch);
				i++;
			}
		}

		return result.toString();
	}

	// 构建最终 URL
	private static String buildUrl(String pre, String uDecoded) throws Exception {
		String uUrlEncoded = URLEncoder.encode(uDecoded, StandardCharsets.UTF_8.toString()).replace("+", "%2B")
				.replace("/", "%2F").replace("=", "%3D");

		long t = System.currentTimeMillis() / 1000 / 1800;

		return "https://am.uvuccnx.xyz" + pre + "&u=" + uUrlEncoded + "&t=" + t;
	}
}