package cn.exrick.manager.service.util;

import java.util.UUID;
import java.util.regex.Pattern;

public class FileNameSanitizer {
	// 匹配所有非字母数字、非汉字、非下划线和连字符的字符
	private static final Pattern INVALID_CHARS = Pattern.compile("[^\\w\\u4e00-\\u9fa5-]");

	public static String sanitize(String input) {
		if (input == null || input.trim().isEmpty()) {
			return UUID.randomUUID().toString().replace("-", "");
		}

		// 替换所有非法字符为空字符串
		String sanitized = INVALID_CHARS.matcher(input).replaceAll("");

		// 处理连续的下划线/连字符
		sanitized = sanitized.replaceAll("[-_]{2,}", "_");

		// 去除首尾非字母数字字符
//		sanitized = sanitized.replaceAll("^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$", "");
//		sanitized = sanitized.replaceAll("^[^a-zA-Z0-9\\u4e00-\\u9fa5]+|[^a-zA-Z0-9\\u4e00-\\u9fa5]+$", "");

		return sanitized.isEmpty() ? "unnamed" : sanitized;
	}

	public static void main(String[] args) {
		String testStr = "香水控制 搓🐔8️\\u20e3_110721_11s";
		System.out.println(sanitize(testStr)); // 输出: A文件名测试-2024
	}
}
