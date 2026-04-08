package cn.exrick.manager.service.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TextProcessor {

	public static void main(String[] args) {
		try {
			// 读取文件
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream("d:/download/allm3u8.bat"), "GBK"));
			String line;

			// 定义需要删除的关键词列表
			List<String> keywords = new ArrayList<String>();

			Path filePath = Paths.get("d:/download/wanwuok.txt");
			try {
				List<String> keywordsOri = Files.readAllLines(filePath);
				for (String key : keywordsOri) {
					// 二次运行时候要转换
					key = key.replace("\"", "");
					String keys = FileNameSanitizer.sanitize(key);
					keywords.add(keys);
//					System.out.println(key);
//					System.out.println(keys);

					keywords.add(key);

//                	 

					// 或者直接比较
//					keywords.add(key);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			filePath = Paths.get("d:/download/taoluok.txt");
			try {
				List<String> keywordsOri = Files.readAllLines(filePath);
				for (String key : keywordsOri) {
					// 二次运行时候要转换
					key = key.replace("\"", "");
//					System.out.println(key);
					String keys = FileNameSanitizer.sanitize(key);
					keywords.add(keys);
//					System.out.println(keys);

					keywords.add(key);

//                	 

					// 或者直接比较
//					keywords.add(key);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			// 写入新文件
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("d:/download/allm3u82.bat"), "GBK"));

			while ((line = reader.readLine()) != null) {
				boolean containsKeyword = false;
//				System.out.println(line);
				for (String keyword : keywords) {
					if (line.contains(keyword)) {
						System.out.println("检测到云盘中已经有的文件：" + line);
						containsKeyword = true;
						break;
					}
				}
				if (!containsKeyword) {
					writer.write(line);
					writer.newLine();
				}
			}

			reader.close();
			writer.close();
			System.out.println("处理完成，结果已保存到output.txt");
			new File("d:/download/allm3u8.bat").delete();
			new File("d:/download/allm3u82.bat").renameTo(new File("d:/download/allm3u8.bat"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
