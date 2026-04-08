package cn.exrick.manager.service.huifang;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JavaScript Eval 混淆解码器 (Java版) 支持解析 eval(function(p,a,c,k,e,d){...}) 格式的混淆代码
 */
public class jiema {

	/**
	 * 解码混淆的 JavaScript 代码
	 */
	public static DecodeResult decode(String obfuscatedCode) {
		// 使用正则提取参数: ('p',a,c,'k1|k2|...'.split...)
//        Pattern pattern = Pattern.compile("\('([^']+)',(\d+),(\d+),'([^']+)'\.split");

		Pattern pattern = Pattern.compile(
				"'([^']*?)',\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*'([^']*?)'\\s*\\.split\\s*\\('\\|'(?:,\\s*\\d+)?\\s*\\)",
				Pattern.DOTALL);
		Matcher matcher = pattern.matcher(obfuscatedCode);

		if (!matcher.find()) {
			throw new IllegalArgumentException("无法识别的混淆格式");
		}

		String p = matcher.group(1);
		int a = Integer.parseInt(matcher.group(2));
		int c = Integer.parseInt(matcher.group(3));
		String kStr = matcher.group(4);
		String[] k = kStr.split("\\|", -1); // -1 保留空字符串

		// //System.out.println("提取参数:");
		// //System.out.println(" a = " + a);
		// //System.out.println(" c = " + c);
		// //System.out.println(" k数组长度 = " + k.length);

		// 建立映射表 (36进制: 0-9, a-z)
		Map<String, String> mapping = new LinkedHashMap<>();
		for (int i = 0; i < c; i++) {
			String key = i < 10 ? String.valueOf(i) : String.valueOf((char) ('a' + i - 10));
			String value = i < k.length ? k[i] : key;
			mapping.put(key, value);
		}

		// //System.out.println("\n映射表 (前15项):");
		int count = 0;
		for (Map.Entry<String, String> entry : mapping.entrySet()) {
			if (count++ >= 15) {
				// //System.out.println(" ...");
				break;
			}
			String val = entry.getValue();
			if (val.length() > 40)
				val = val.substring(0, 40) + "...";
			// //System.out.println(" " + entry.getKey() + " -> " + val);
		}

		// 替换（从大到小避免冲突，如先替换10再替换1）
		String result = p;
		// //System.out.println("\n开始替换...");

		for (int i = c - 1; i >= 0; i--) {
			String key = i < 10 ? String.valueOf(i) : String.valueOf((char) ('a' + i - 10));
			String value = i < k.length ? k[i] : null;

			if (value != null && !value.isEmpty()) {
				// 使用单词边界：前面不是单词字符，后面也不是单词字符
				String regex = "(?<!\\w)" + Pattern.quote(key) + "(?!\\w)";
				String newResult = result.replaceAll(regex, Matcher.quoteReplacement(value));

				if (!newResult.equals(result)) {
					result = newResult;
					String displayVal = value.length() > 50 ? value.substring(0, 50) + "..." : value;
					// //System.out.println(" 替换 '" + key + "' -> '" + displayVal + "'");
				}
			}
		}

		// 处理转义字符
		// result = result.replace("\\"", "\"").replace("\\'", "'").replace("\\/", "/");

		return new DecodeResult(result, mapping, p, a, c, k);
	}

	/**
	 * 解析视频详情请求配置
	 */
	public static VideoDetailConfig parseVideoDetail(String decodedCode) {
		VideoDetailConfig config = new VideoDetailConfig();

		// 提取 document.write 内容
		Pattern writePattern = Pattern.compile("document.write\\(\"(.+)\"\\)");
		Matcher writeMatcher = writePattern.matcher(decodedCode);

		if (writeMatcher.find()) {
			// System.out.println("find1");
			String html = writeMatcher.group(1);
			// System.out.println(html);

			// 提取 src 属性
			Pattern srcPattern = Pattern.compile("src.*?\"([^\"]+)\"");
			Matcher srcMatcher = srcPattern.matcher(html);
			if (srcMatcher.find()) {
				// System.out.println("find2");
				config.srcTemplate = srcMatcher.group(1);
				int ubegin = html.indexOf("encodeURIComponent(\"");
				int uend = html.indexOf("\")+\"&t=\"");
				String ustr = html.substring(ubegin + 20, uend);
				String ustrcode = "";
				try {
					ustrcode = encodeURIComponent(ustr);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				long now = System.currentTimeMillis();
				long tValue = now / 1000 / 1800;
				// //System.out.println("当前时间戳 t = " + tValue);
//				////System.out.println("(对应时间: " + new Date(tValue * 1800 * 1000) + ")");

				// 解析各个参数
				config.id = extractQueryParam(config.srcTemplate, "id");
				config.img = extractQueryParam(config.srcTemplate, "img");
				config.ads = extractQueryParam(config.srcTemplate, "ads");
				config.u = ustrcode;
				config.t = tValue + "";
			}
		}

		return config;
	}

	public static String encodeURIComponent(String value) throws UnsupportedEncodingException {
		return URLEncoder.encode(value, StandardCharsets.UTF_8.toString()).replace("+", "%20") // 空格: + → %20
				.replace("%21", "!") // 不编码 !
				.replace("%27", "'") // 不编码 '
				.replace("%28", "(") // 不编码 (
				.replace("%29", ")") // 不编码 )
				.replace("%7E", "~"); // 不编码 ~
	}

	private static String extractQueryParam(String url, String paramName) {
		Pattern pattern = Pattern.compile("[?&]" + paramName + "=([^&]+)");
		Matcher matcher = pattern.matcher(url);
		return matcher.find() ? matcher.group(1) : null;
	}

	// ============ 数据类 ============

	public static class DecodeResult {
		public final String code;
		public final Map<String, String> mapping;
		public final String rawP;
		public final int a;
		public final int c;
		public final String[] k;

		public DecodeResult(String code, Map<String, String> mapping, String rawP, int a, int c, String[] k) {
			this.code = code;
			this.mapping = mapping;
			this.rawP = rawP;
			this.a = a;
			this.c = c;
			this.k = k;
		}
	}

	public static class VideoDetailConfig {
		public String srcTemplate;
		public String id;
		public String img;
		public String ads;
		public String u;
		public String t;

		@Override
		public String toString() {
			return "VideoDetailConfig{" + "\n  srcTemplate='" + srcTemplate + "'" + ",\n  id='" + id + "'"
					+ ",\n  img='" + img + "'" + ",\n  ads='" + ads + "'" + ",\n  u='"
					+ (u != null && u.length() > 30 ? u.substring(0, 30) + "..." : u) + "'" + ",\n  t='" + t + "'"
					+ "\n}";
		}
	}

	// ============ 主程序 ============

	public static String getvideourl(String obfuscated) {
		// 新的混淆代码

		// //System.out.println("========================================");
		// //System.out.println("JavaScript Eval 混淆解码器");
		// //System.out.println("========================================\n");

		try {
			// 解码
			DecodeResult result = decode(obfuscated);

			// //System.out.println("\n========================================");
			// System.out.println("解码结果:");
			// //System.out.println("========================================");
			// System.out.println(result.code);

			// 解析视频详情
			// //System.out.println("\n========================================");
			// //System.out.println("视频详情配置解析:");
			// //System.out.println("========================================");
			VideoDetailConfig config = parseVideoDetail(result.code);
			// System.out.println(config);

			// 详细说明
			// //System.out.println("\n========================================");
			// //System.out.println("解析说明:");
			// //System.out.println("========================================");
			// //System.out.println("1. 代码类型: 立即执行函数 (IIFE)");
			// //System.out.println("2. 功能: 动态插入script标签请求视频详情");
			// //System.out.println("3. 接口: /videos/detail_play");
			// //System.out.println("4. 安全特性:");
			// //System.out.println(" • u参数: 加密签名 (防伪造)");
			// //System.out.println(" • t参数: 时间戳 (30分钟粒度，防重放)");
			// //System.out.println(" • img路径: URL编码");

			String url = "https://am.uvuccnx.xyz" + config.srcTemplate.replace("u=", "u=" + config.u) + "&t="
					+ config.t;
			// System.out.println(url);
			return url;

		} catch (Exception e) {
			// System.err.println("解码失败: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {

		String obfuscated = "eval(function(p,a,c,k,e,d){e=function(c){return c.toString(36)};if(!''.replace(/^/,String)){while(c--){d[c.toString(a)]=k[c]||c.toString(a)}k=[function(e){return d[e]}];e=function(){return'\\\\w+'};c=1};while(c--){if(k[c]){p=p.replace(new RegExp('\\\\b'+e(c)+'\\\\b','g'),k[c])}}return p}('(q(){2.3(\"<0 4=\\\\\"/5/6?7=8&9=%a%b%c%1.d&f=&g=\"+h(\"i+j=\")+\"&k=\"+l((m n()).o()/p/e)+\"\\\\\"><\\\\/0>\")})()',27,27,'script|2F2025100911353568104|document|write|src|videos|detail_play|id|2893|img|2Fupload_01|2Fxiao|2F20251009|jpg|1800|ads|u|encodeURIComponent|NRu8XZe|nwHPi3CdPC56XkjSyfSCYU9QBtv6cCV5zsZl12ZL8c43jpzh3Y8WoWLrrkyI6ODiu1NGpG6K7WDmmAQYl176y9HNtI6NOgc9Kp4|t|parseInt|new|Date|getTime|1000|function'.split('|'),0,{}))";
		obfuscated = "eval(function(p,a,c,k,e,d){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--){d[e(c)]=k[c]||e(c)}k=[function(e){return d[e]}];e=function(){return'\\\\w+'};c=1};while(c--){if(k[c]){p=p.replace(new RegExp('\\\\b'+e(c)+'\\\\b','g'),k[c])}}return p}('(G(){i.h(\"<0 g=\\\\\"/f/e?d=c&b=%9-1%8-7%6%5-4.3&2=&u=\"+j(\"a+k/m+F+E/D/C/B+A+/z+y++x/w/v/s=\")+\"&t=\"+r((q p()).o()/n/l)+\"\\\\\"><\\\\/0>\")})()',43,43,'script|204|ads|png|small|2F896a1b078cf3ce40506972ffc0d4b49e|2F89|file|2Fdcc|2Fmd|XFbhC9sUBBR52|img|20609|id|detail_play|videos|src|write|document|encodeURIComponent|uC5|1800|4dtPme|1000|getTime|Date|new|parseInt|ee8U98ir04RFYtg9zuLP3KbrdS8|||V0it4G8HbhrvaZmMXSLl|ODpRiDd902mSq98Z5vCH|M2y|Uoa5V03XVcQOlsLn3FuvQ|3gRwnjLn2|2x8|yXyZUTvpUm1fLjkBqUOa|SdOixK1eUrzQy|2v0mWeDXy5Mm0NehZ|OUFTohnHoW7PdMEp|e4vfuiT1xN4ETVefywivzlIljV50|function'.split('|'),0,{}))"
				+ "";
		System.out.println(getvideourl(obfuscated));

	}
}