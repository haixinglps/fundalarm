package cn.exrick.manager.service.huifang;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PackerDetailPlayExtractor {

	public static void main(String[] args) {
		// 你提供的完整 packer 脚本内容（eval(...) 里面的字符串）
		String packedScript = "function(p,a,c,k,e,d){e=function(c){return c.toString(36)};if(!''.replace(/^/,String)){while(c--){d[c.toString(a)]=k[c]||c.toString(a)}k=[function(e){return d[e]}];e=function(){return'\\\\w+'};c=1};while(c--){if(k[c]){p=p.replace(new RegExp('\\\\b'+e(c)+'\\\\b','g'),k[c])}}return p}('(r(){2.3(\"<0 4=\\\\\"/5/6?7=8&9=%a%b%c%1.d&f=&g=\"+h(\"i+j/k=\")+\"&l=\"+m((n o()).p()/q/e)+\"\\\\\"><\\\\/0>\")})()',28,28,'script|2F2026030221520734836|document|write|src|videos|detail_play|id|26501|img|2Fupload_01|2Fxiao|2F20260302|png|1800|ads|u|encodeURIComponent|rBSgfKKGPjBDj48N|tul3um|p2gxYG1eJY3RpbybqRItbCbCZJ2JgKacb9CpXRScRXMhSLMNFMH4M6H6jCzcMfBcDfRUtdrBuxfHrxQwHQ4|t|parseInt|new|Date|getTime|1000|function'.split('|'),0,{})";

		// keys 数组（从 .split('|') 里复制过来）
		String keysStr = "script|2F2026030221520734836|document|write|src|videos|detail_play|id|26501|img|2Fupload_01|2Fxiao|2F20260302|png|1800|ads|u|encodeURIComponent|rBSgfKKGPjBDj48N|tul3um|p2gxYG1eJY3RpbybqRItbCbCZJ2JgKacb9CpXRScRXMhSLMNFMH4M6H6jCzcMfBcDfRUtdrBuxfHrxQwHQ4|t|parseInt|new|Date|getTime|1000|function";
		// + " </script>";
		String[] keys = keysStr.split("\\|");

		System.out.println("开始解包...\n");

		// 步骤1：替换 \b数字\b 为 keys[数字]
		String unpacked = replaceNumericPlaceholders(packedScript, keys);
		System.out.println("解包后代码：");
		System.out.println(unpacked);
		System.out.println("\n");

		// 步骤2：从解包结果中提取 detail_play 的路径（含参数，除了动态 t）
		String detailPlayPath = extractDetailPlayPath(unpacked);
		if (detailPlayPath != null) {
			String fullUrl = "https://wanwuu.com" + detailPlayPath;
			System.out.println("提取到的 videodetail (detail_play) 地址模板：");
			System.out.println(fullUrl);
			System.out.println("\n说明：最后的 &t= 是动态的，需要运行时用当前时间戳替换");
		} else {
			System.out.println("未提取到 detail_play 路径");
		}
	}

	/**
	 * 把源字符串中所有 \b数字\b 替换成 keys[数字]
	 */
	private static String replaceNumericPlaceholders(String source, String[] keys) {
		Pattern pattern = Pattern.compile("\\b(\\d+)\\b");
		Matcher matcher = pattern.matcher(source);
		StringBuffer sb = new StringBuffer();

		while (matcher.find()) {
			try {
				int index = Integer.parseInt(matcher.group(1));
				String replacement = (index >= 0 && index < keys.length) ? keys[index] : matcher.group(1);
				matcher.appendReplacement(sb, replacement);
			} catch (NumberFormatException e) {
				matcher.appendReplacement(sb, matcher.group(0));
			}
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 从解包后的字符串中正则截取 detail_play 的路径部分
	 * 例如："/videos/detail_play?id=26501&img=...&ads=&u=...&t="
	 */
	private static String extractDetailPlayPath(String unpacked) {
		// 匹配 src="/videos/detail_play?..." 直到 ">"
		String regex = "src\\s*=\\s*\"(/videos/detail_play\\?[^\"]+)\"";
		Matcher matcher = Pattern.compile(regex).matcher(unpacked);
		if (matcher.find()) {
			return matcher.group(1);
		}

		// 更宽松匹配（如果引号或格式变了）
		regex = "(/videos/detail_play\\?id=\\d+[^\"<]*)";
		matcher = Pattern.compile(regex).matcher(unpacked);
		if (matcher.find()) {
			return matcher.group(1);
		}

		return null;
	}
}