package cn.exrick.manager.service.huifang;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PackerM3u8Extractor {

	/**
	 * 从 packer 字符串中提取 m3u8 地址
	 * 
	 * @param packed 整个 eval(...) 里的 packer 内容，例如你贴的那一大段
	 * @return 提取到的 m3u8 地址，或 null（失败时）
	 */
	public static String extractM3u8FromPacker(String packed) {
		// 步骤1：提取 keys 数组（split('|') 后的内容）
		// 你贴的 packer 最后是 .split('|'),0,{} 所以 keys 在这里
		String keysPart = packed.substring(packed.lastIndexOf("'|") + 2, packed.lastIndexOf("'.split('|')"));
		// 更稳妥的方式：直接从你贴的代码复制 keys（实际使用时可手动粘贴或正则切）
		String[] keys = new String[] { "", "data", "div", "cn", "https", "b70daf7a3a206612b486052dc0288000", "auth_key",
				"write", "id", "mse", "video", "25800", "url", "long", "isidum", "videos5", "m3u8",
				"4bf4c12ca08e40429e4b139ee11f41c7", "1772640617", "ads_url", "via_m", "wanwu", "poster", "pic",
				"fzmbxi", "upload_01", "xiao", "20260302", "2026030221520734836", "png", "document" };

		// 步骤2：提取源字符串 p（'u.7(\'<2 8=... 这一段）
		// 假设 p 是第一个单引号到倒数第二个单引号之间的内容
		int start = packed.indexOf("('") + 2;
		int end = packed.lastIndexOf("',", packed.lastIndexOf("split('|')"));
		if (start < 2 || end <= start)
			return null;
		String source = packed.substring(start, end);

		// 步骤3：替换 \b数字\b 为 keys[数字]
		Pattern numPattern = Pattern.compile("\\b(\\d+)\\b");
		Matcher matcher = numPattern.matcher(source);
		StringBuffer unpackedSb = new StringBuffer();
		while (matcher.find()) {
			try {
				int idx = Integer.parseInt(matcher.group(1));
				String replacement = (idx >= 0 && idx < keys.length) ? keys[idx] : matcher.group(1);
				matcher.appendReplacement(unpackedSb, Matcher.quoteReplacement(replacement));
			} catch (NumberFormatException e) {
				matcher.appendReplacement(unpackedSb, matcher.group(0));
			}
		}
		matcher.appendTail(unpackedSb);
		String unpacked = unpackedSb.toString();

		// 步骤4：从解包结果中匹配 m3u8（最稳的特征）
		// 匹配 https://... .m3u8?auth_key=...
		String m3u8Regex = "(https?://[^'\"\\\\\\s<]+?\\.m3u8\\?[^'\"\\\\\\s<&]+)";
		Pattern m3u8Pattern = Pattern.compile(m3u8Regex);
		Matcher m3u8Matcher = m3u8Pattern.matcher(unpacked);
		if (m3u8Matcher.find()) {
			return m3u8Matcher.group(1);
		}

		// 备选：更宽松匹配任何 .m3u8
		String fallbackRegex = "(https?://[^'\"\\s<]+?\\.m3u8[^'\"\\s<]*)";
		Matcher fallback = Pattern.compile(fallbackRegex).matcher(unpacked);
		if (fallback.find()) {
			return fallback.group(1);
		}

		return null; // 没找到
	}

	// 测试 main
	public static void main(String[] args) {
		String packed = "eval(function(p,a,c,k,e,d){e=function(c){return c.toString(36)};if(!''.replace(/^/,String)){while(c--){d[c.toString(a)]=k[c]||c.toString(a)}k=[function(e){return d[e]}];e=function(){return'\\\\w+'};c=1};while(c--){if(k[c]){p=p.replace(new RegExp('\\\\b'+e(c)+'\\\\b','g'),k[c])}}return p}('u.7(\\'<2 8=\"9\" 1-a=\"b\" 1-c=\"4://d.e.3/f/5/5.g?6=i-0-0-h&k=l\" 1-m=\"4://n.o.3/p/q/r/s.t\" 1-j=\"\"></2>\\');',31,31,'|data|div|cn|https|b70daf7a3a206612b486052dc0288000|auth_key|write|id|mse|video|25800|url|long|isidum|videos5|m3u8|4bf4c12ca08e40429e4b139ee11f41c7|1772640617|ads_url|via_m|wanwu|poster|pic|fzmbxi|upload_01|xiao|20260302|2026030221520734836|png|document'.split('|'),0,{}))";

		String m3u8 = extractM3u8FromPacker(packed);
		if (m3u8 != null) {
			System.out.println("提取到的 m3u8:");
			System.out.println(m3u8);
		} else {
			System.out.println("未提取到 m3u8");
		}
	}
}
