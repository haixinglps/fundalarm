package cn.exrick.manager.service.company;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class Base64ToPdfConverter {

	// 核心转换方法
	public static void convertToFile(String base64Str, String outputPath) throws IOException {
		// 移除可能的Data URI前缀
		String cleanBase64 = base64Str.replaceFirst("^data:application/pdf;base64,", "");

		// Base64解码
		byte[] pdfBytes = Base64.getDecoder().decode(cleanBase64);

		// 写入文件
		try (FileOutputStream fos = new FileOutputStream(outputPath)) {
			fos.write(pdfBytes);
		}
	}

	// 带校验的增强版
	public static void convertWithValidation(String base64Str, String outputPath)
			throws IOException, IllegalArgumentException {

		if (!base64Str.startsWith("JVBERi0x")) {
			throw new IllegalArgumentException("非法的PDF Base64头部");
		}

		convertToFile(base64Str, outputPath);

		// 验证文件是否生成成功
		if (!Files.exists(Paths.get(outputPath))) {
			throw new IOException("文件创建失败");
		}
	}

	public static void main(String[] args) {
		String sampleBase64 = ""; // 替换实际Base64
		String outputFile = "d:\\output.pdf";

		try {
			convertWithValidation(sampleBase64, outputFile);
			System.out.println("PDF文件已保存至: " + Paths.get(outputFile).toAbsolutePath());
		} catch (Exception e) {
			System.err.println("转换失败: " + e.getMessage());
		}
	}
}
