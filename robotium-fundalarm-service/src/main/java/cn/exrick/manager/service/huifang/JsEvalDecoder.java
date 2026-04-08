package cn.exrick.manager.service.huifang;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import okhttp3.OkHttpClient;
import okhttp3.Request;;

public class JsEvalDecoder {
	static {
		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "7890");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyPort", "7890");
	}
	static OkHttpClient client = createUnsafeClient();// new OkHttpClient();//

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

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("=== 解码结果 ===\n");
			sb.append("代码: ").append(code).append("\n\n");
			sb.append("映射表 (").append(mapping.size()).append(" 项):\n");
			mapping.forEach((key, val) -> sb.append(" ").append(key).append(" -> ").append(val).append("\n"));
			return sb.toString();
		}
	}

	public static class VideoConfig {
		public String id;
		public String videoId;
		public String videoUrl; // 通常就是 m3u8
		public String posterUrl;
		public String adsUrl;

		@Override
		public String toString() {
			return "VideoConfig{" + "id='" + id + '\'' + ", videoId='" + videoId + '\'' + ", videoUrl='"
					+ (videoUrl != null ? videoUrl.substring(0, Math.min(60, videoUrl.length())) + "..." : null) + '\''
					+ ", posterUrl='" + posterUrl + '\'' + ", adsUrl='" + adsUrl + '\'' + '}';
		}
	}

	private static String extractP(String obfuscated) {
		// 找到 .split('|') 的位置
		int splitIndex = obfuscated.indexOf(".split('|')");
		if (splitIndex == -1) {
			splitIndex = obfuscated.indexOf(".split('|',");
			if (splitIndex == -1)
				return null;
		}

		// keys 开始位置：split 前最后一个 '
		int keysEnd = splitIndex;
		int keysStart = obfuscated.lastIndexOf("'", keysEnd - 1);

		// c 的位置：keysStart 前找 ,数字,
		String beforeKeys = obfuscated.substring(0, keysStart);
		int cStart = beforeKeys.lastIndexOf(",");
		if (cStart == -1)
			return null;

		// p 结束位置：c 前面的那个 '
		int pEnd = beforeKeys.lastIndexOf("'", cStart);

		// p 开始位置：eval( 后面的第一个 '
		int pStart = obfuscated.indexOf("('");
		if (pStart == -1)
			pStart = obfuscated.indexOf("'");
		pStart += 1;

		if (pStart >= pEnd)
			return null;

		return obfuscated.substring(pStart, pEnd);
	}

	/**
	 * 解码 eval(function(p,a,c,k,e,d){...}) 格式的混淆代码
	 */
	public static DecodeResult decode(String obfuscatedCode) {
		// 简化正则：只抓 'p内容',a,c,'k1|k2|...' 这四个部分
		// 允许中间有任意内容（用 .*? 非贪婪）
		// System.out.println(obfuscatedCode);
		Pattern pattern = Pattern.compile(
				"'([^']*?)',\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*'([^']*?)'\\s*\\.split\\s*\\('\\|'(?:,\\s*\\d+)?\\s*\\)",
				Pattern.DOTALL);

//		pattern = Pattern.compile(
//				"'([^']*?)'\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*'([^']*?)'\\s*\\.split\\s*\\(['\"]\\|['\"]\\)(?:\\s*,\\s*[^)]+)*\\s*\\)",
//				Pattern.DOTALL);
//
//		pattern = Pattern.compile(
//				"'([^']*?)'\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*'([^']*?)'\\s*\\.split\\s*\\('\\|'\\)\\s*,\\s*(\\d+)\\s*,\\s*\\{\\s*\\}",
//				Pattern.DOTALL);

		Matcher matcher = pattern.matcher(obfuscatedCode);
		if (!matcher.find()) {
			// 调试：打印附近内容帮助定位
			int pos = obfuscatedCode.indexOf(".split('|')");
			if (pos > 0) {
				String nearby = obfuscatedCode.substring(Math.max(0, pos - 300),
						Math.min(obfuscatedCode.length(), pos + 300));
				// System.err.println("调试：.split('|') 附近内容：\n" + nearby);
			}
			throw new IllegalArgumentException("无法识别的混淆格式，请检查是否包含正确的 packer 结构");
		}

		String p = extractP(obfuscatedCode);
		// matcher.group(1);
		// System.out.println("result:" + p);
		int a = Integer.parseInt(matcher.group(2));
		int c = Integer.parseInt(matcher.group(3));
		String[] k = matcher.group(4).split("\\|");

		// 构建映射
		Map<String, String> mapping = new HashMap<>();
		for (int i = 0; i < c; i++) {
			String key = (i < 10) ? String.valueOf(i) : String.valueOf((char) ('a' + i - 10));
			String value = (i < k.length && !k[i].isEmpty()) ? k[i] : key;
			mapping.put(key, value);
		}

		// 从大到小替换（防止 10 被 1 替换）
		String result = p;
		for (int i = c - 1; i >= 0; i--) {
			String key = (i < 10) ? String.valueOf(i) : String.valueOf((char) ('a' + i - 10));
			String value = mapping.get(key);
			if (value != null) {
				result = result.replaceAll("\\b" + Pattern.quote(key) + "\\b", Matcher.quoteReplacement(value));
			}
		}

		// 处理常见转义
		// result = result.replace("\\'", "'").replace("\\\"", "\"").replace("\\\\",
		// "\\");

		return new DecodeResult(result, mapping, p, a, c, k);
	}

	/**
	 * 从解码后的代码中解析视频配置（针对 wanwuu 网站的 document.write('<div ...>') 格式）
	 */
	public static VideoConfig parseVideoConfig(String decodedCode) {
		VideoConfig config = new VideoConfig();

		// 匹配 document.write('...');
		Pattern writePattern = Pattern.compile("document\\.write\\s*\\(\\s*['\"](.+?)['\"]\\s*\\)\\s*;",
				Pattern.DOTALL);
		Matcher writeMatcher = writePattern.matcher(decodedCode);
		if (writeMatcher.find()) {
			String html = writeMatcher.group(1);
			config.id = extractAttr(html, "id");
			config.videoId = extractAttr(html, "data-video");
			config.videoUrl = extractAttr(html, "data-url");
			config.posterUrl = extractAttr(html, "data-poster");
			config.adsUrl = extractAttr(html, "data-ads_url");
		}

		return config;
	}

	/**
	 * 保底提取 m3u8 地址（即使 parseVideoConfig 没抓到，也能从整个解码文本中找）
	 */
	public static String extractM3u8(String decodedCode) {
		// 优先匹配完整的 m3u8 + auth_key
		Pattern m3u8Pattern = Pattern
				.compile("(https?://long\\.isidum\\.cn/videos5/[0-9a-f]{32}/[0-9a-f]{32}\\.m3u8\\?[^'\"\\s<]+)");
		Matcher matcher = m3u8Pattern.matcher(decodedCode);
		if (matcher.find()) {
			return matcher.group(1);
		}

		// 宽松匹配任意 .m3u8
		Pattern fallback = Pattern.compile("(https?://[^'\"\\s<]+?\\.m3u8[^'\"\\s<]*)");
		matcher = fallback.matcher(decodedCode);
		if (matcher.find()) {
			return matcher.group(1);
		}

		return null;
	}

	private static String extractAttr(String html, String attrName) {
		Pattern pattern = Pattern.compile(attrName + "\\s*=\\s*['\"]([^'\"]*)['\"]");
		Matcher matcher = pattern.matcher(html);
		return matcher.find() ? matcher.group(1) : null;
	}

	public static String getm3u8(String obj) {
//
//		try {
//			HttpsUrlValidator.trustAllHttpsCertificates();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		// 使用你最新提供的 packer（videoId=25800）
		String htmlstr2 = "";
		String urlString = obj;
		Document doc = null;// Jsoup.connect(urlString).get();// .proxy("127.0.0.1", 7890)

		Request request = new Request.Builder().url(urlString)
				.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36").build();

		try (okhttp3.Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);
			String html = response.body().string();
			doc = Jsoup.parse(html);
			// 然后继续用 Jsoup.parse(html, url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (doc == null)
			return null;

		String htmlstr = doc.outerHtml();
		int begin = htmlstr.indexOf("eval(function");
		int end = htmlstr.indexOf("{}))") + 4;

		String id = htmlstr.substring(begin, end);
		// System.out.println("[" + id + "]");
//			String urlString2 = "https://wanwuu.com/videos/detail_play?id=" + 2893
//					+ "&img=%2Fupload_01%2Fxiao%2F20260302%2F2026030221520734836.png&ads=&u=rBSgfKKGPjBDj48N%2Btul3um%2Fp2gxYG1eJY3RpbybqRItbCbCZJ2JgKacb9CpXRScRXMhSLMNFMH4M6H6jCzcMfBcDfRUtdrBuxfHrxQwHQ4%3D&t=984794";
//			 String urlString2 = extractDetailPlayUrlFromPacker(id);
//			DecodeResult url2result = decode(id);
//			//System.out.println("[" + url2result.code + "]");.proxy("127.0.0.1", 7890)
		// //System.out.println(urlString2);
		String urlString2 = null;
		try {
			urlString2 = jiema2.getvideourl(id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			urlString2 = jiema.getvideourl(id);
		}
		if (urlString2 == null) {
			// System.out.println("url解析失败");
			return null;
		}
//		System.out.println(urlString2);
//			Response doc2 = Jsoup.connect(urlString2).ignoreContentType(true).execute();
		Request request2 = new Request.Builder().url(urlString2)
				.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36").build();

		try (okhttp3.Response response = client.newCall(request2).execute()) {
			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);
			String html = response.body().string();
//			System.out.println("html:");
//			System.out.println(html);
			htmlstr2 = html;// doc2.body();
			// doc=Jsoup.parse(html);
			// 然后继续用 Jsoup.parse(html, url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (htmlstr2 == null) {
			// System.out.println("");
			return null;
		}

		String obfuscated = "eval(function(p,a,c,k,e,d){e=function(c){return c.toString(36)};if(!''.replace(/^/,String)){while(c--){d[c.toString(a)]=k[c]||c.toString(a)}k=[function(e){return d[e]}];e=function(){return'\\\\w+'};c=1};while(c--){if(k[c]){p=p.replace(new RegExp('\\\\b'+e(c)+'\\\\b','g'),k[c])}}return p}('u.7(\\'<2 8=\"9\" 1-a=\"b\" 1-c=\"4://d.e.3/f/5/5.g?6=i-0-0-h&k=l\" 1-m=\"4://n.o.3/p/q/r/s.t\" 1-j=\"\"></2>\\');',31,31,'|data|div|cn|https|b70daf7a3a206612b486052dc0288000|auth_key|write|id|mse|video|25983|url|long|isidum|videos5|m3u8|31db158da98bfa658a6f34e2d8045b1c|1772650090|ads_url|via_m|wanwu|poster|pic|fzmbxi|upload_01|xiao|20260302|2026030221520734836|png|document'.split('|'),0,{}))"
				+ "";
		obfuscated = htmlstr2;
		// //System.out.println(obfuscated);
		String directM3u8 = null;
//		//System.out.println("开始解码...\n");
		try {
			DecodeResult result = decode(obfuscated);
			// System.out.println("[" + result.code + "]");
//
//			//System.out.println("\n=== 视频配置解析 ===");
			VideoConfig config = parseVideoConfig(result.code);
//			//System.out.println(config);

			// 额外保底提取 m3u8
			directM3u8 = extractM3u8(result.code);
			if (directM3u8 != null && (config.videoUrl == null || config.videoUrl.isEmpty())) {
//				//System.out.println("\n保底提取到 m3u8: " + directM3u8);
			}

//			//System.out.println("\n=== 关键信息总结 ===");
//			//System.out.println("视频ID     : " + config.videoId);
//			//System.out.println("m3u8 地址  : " + (config.videoUrl != null ? config.videoUrl : directM3u8));
//			//System.out.println("封面地址   : " + config.posterUrl);
//			//System.out.println("广告地址   : " + (config.adsUrl != null && !config.adsUrl.isEmpty() ? config.adsUrl : "无"));

		} catch (Exception e) {
//			//System.err.println("解码失败: " + e.getMessage());
			e.printStackTrace();
		}
		// System.out.println(directM3u8);
		return directM3u8;
	}

	// import java.util.regex.*;

	public static String extractDetailPlayUrlFromPacker(String obfuscatedCode) {
		// System.out.println("length: " + obfuscatedCode.length());

		// 1. splitPos
		int splitPos = obfuscatedCode.lastIndexOf(".split('|')");
		if (splitPos == -1) {
			splitPos = obfuscatedCode.lastIndexOf(".split('|',");
			if (splitPos == -1)
				return null;
		}

		// 2. keys 结束引号
		int keysEndQuote = obfuscatedCode.lastIndexOf("'", splitPos);
		if (keysEndQuote == -1)
			return null;

		int keysStartQuote = obfuscatedCode.lastIndexOf("'", keysEndQuote - 1);
		if (keysStartQuote == -1)
			return null;

		String keysStr = obfuscatedCode.substring(keysStartQuote + 1, keysEndQuote);
		// System.out.println("keysStr: " + keysStr);

		String[] keys = keysStr.split("\\|");
		// System.out.println("keys length: " + keys.length);
		if (keys.length < 20)
			return null;

		// 3. p 结束位置
		String beforeKeys = obfuscatedCode.substring(0, keysStartQuote);
		int pEnd = beforeKeys.lastIndexOf("'");
		if (pEnd == -1)
			return null;

		// 4. p 开始位置：从 pEnd 往前找最近的 '(
		int pStart = -1;
		for (int i = pEnd - 1; i >= 0; i--) {
			if (obfuscatedCode.charAt(i) == '\'' && i > 0 && obfuscatedCode.charAt(i - 1) == '(') {
				pStart = i + 1; // 跳过 '(' 后的位置
				break;
			}
		}

		if (pStart == -1 || pStart >= pEnd) {
			// System.out.println("pStart 失败");
			return null;
		}

		String p = obfuscatedCode.substring(pStart, pEnd);
		// System.out.println("p (从后往前定位): " + p);

		// 5. 替换 p 中的占位符 → keys 值（全局替换，packer 特征允许）
		String decoded = p;

		// 处理 0~9 的数字占位符
		for (int i = 0; i <= 9; i++) {
			String key = String.valueOf(i);
			if (i < keys.length && !keys[i].isEmpty()) {
				decoded = decoded.replace(key, keys[i]);
			}
		}

		// 处理 a~z 的字母占位符（10=a, 11=b, ...）
		for (int i = 10; i < 36 && i < keys.length; i++) {
			String key = String.valueOf((char) ('a' + i - 10));
			String value = keys[i];
			if (!value.isEmpty()) {
				decoded = decoded.replace(key, value);
			}
		}

		// System.out.println("decoded (正确全局替换后): " + decoded);

		// 6. 清理转义 + 残留 packer 语法
		String cleaned = decoded;
		for (int i = 0; i < 3; i++) {
			cleaned = cleaned.replace("\\\"", "\"").replace("\\\\", "\\").replace("\\'", "'").replace("\\/", "/")
					.replace("\\<", "<").replace("\\>", ">");
		}

		// 去掉外层 packer 残留（如 (q(){...})() ）
		cleaned = cleaned.replaceAll("\\(q\\(\\)\\{(.+?)\\}\\)\\(\\)", "$1"); // 去掉匿名函数包装
		cleaned = cleaned.replaceAll("\\(function\\(.+?\\)\\{\\}", ""); // 去掉多余的 function
		// System.out.println("cleaned (最终): " + cleaned);

		// 7. 提取 src（现在应该能匹配了）
		Pattern srcPattern = Pattern.compile("src\\s*=\\s*[\"'](/videos/detail_play\\?[^\"']*?)[\"']",
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = srcPattern.matcher(cleaned);

		if (matcher.find()) {
			String path = matcher.group(1);
			String fullUrl = "https://am.uvuccnx.xyz" + path;
			int tIndex = fullUrl.indexOf("&t=");
			if (tIndex != -1) {
				fullUrl = fullUrl.substring(0, tIndex + 3);
			}
			return fullUrl;
		} else {
			// System.out.println("未匹配 src，cleaned 前400:");
			// System.out.println(cleaned.substring(0, Math.min(400, cleaned.length())));
			return null;
		}
	}

	// 创建信任所有证书的 OkHttpClient
	public static OkHttpClient createUnsafeClient() {

//		ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_2) // 只用
//																													// TLS
//																													// 1.2，Java
//																													// 8
//																													// 支持
//				.cipherSuites(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
//						CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
//						CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384,
//						CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,
//						CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256, // 如果支持
//						// 加一些 TLS 1.2 常见强套件（防万一服务器降级）
//						CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,
//						CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384)
//				.build();

		try {
			// 创建信任所有证书的 TrustManager
			final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[] {};
				}
			} };

			// 安装全信任 TrustManager
			// final SSLContext sslContext = SSLContext.getInstance("SSL");
			final SSLContext sslContext = SSLContext.getInstance("TLS"); // 修改这里

			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

			// 创建 SSLSocketFactory
			final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

			return new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
					.hostnameVerifier((hostname, session) -> true) // 忽略主机名验证
//					.connectionSpecs(Collections.singletonList(spec))
					.addInterceptor(chain -> {
						Request original = chain.request();
						Request req = original.newBuilder().header("User-Agent",
								"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")
								.build();
						return chain.proceed(req);
					})

					.build();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {

		// System.out.println("4444");44989479日期
//		if (args.length == 0) {
//			args[0] = "https://wanwuu.com/videos/zhibo-huifang/vd-hBU-ae2d/";
//		}
		String re = getm3u8("https://am.uvuccnx.xyz/posts/doum-tiantang/md-gSn-ce0c/");
		System.out.println(re);
		System.exit(0); // 强制退出

	}
}