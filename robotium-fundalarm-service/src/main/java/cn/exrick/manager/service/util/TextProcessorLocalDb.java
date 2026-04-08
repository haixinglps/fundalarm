package cn.exrick.manager.service.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;

public class TextProcessorLocalDb {
	public static void main(String[] args) {
		try {
			// 读取文件
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream("d:/download/input.txt"), "GBK"));
			String line;

			// 定义需要删除的关键词列表
//			List<String> keywords = new ArrayList<String>();
			HashSet<String> vidSet = new HashSet<String>();

//			Path filePath = Paths.get("d:/download/vidtl.txt");
//			try {
//				List<String> keywordsOri = Files.readAllLines(filePath);
//				for (String key : keywordsOri) {
//					// 二次运行时候要转换
//					String keys = FileNameSanitizer.sanitize(key);
//					keywords.add(keys);
////                	 
//
//					// 或者直接比较
////					keywords.add(key);
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}

			// 写入新文件
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("d:/download/output.txt"), "GBK"));

			while ((line = reader.readLine()) != null) {
				boolean containsKeyword = false;

//				for (String keyword : keywords) {
//					if (line.contains(keyword)) {
//						System.out.println("检测到云盘中已经有的文件：" + line);
//						containsKeyword = true;
//						break;
//					}
//				}

				//
				if (line.indexOf("作品id") == -1) {
					writer.write(line);
					writer.newLine();
				} else {
					String vid = null;
					String uid = null;
					// 作品id_196511_用户id 第一天开播_作品id--125980_用户id--2
					if (line.indexOf("作品id_") != -1) {
						int index = line.indexOf("作品id_") + 5;
						int endindex = line.indexOf("_用户id");
						vid = line.substring(index, endindex);

						// _用户id--468217_昵称 _用户id_171887_昵称_Q

						index = line.indexOf("用户id_") + 5;
						endindex = line.indexOf("_昵称");
						uid = line.substring(index, endindex);

					}
					if (line.indexOf("作品id--") != -1) {
						int index = line.indexOf("作品id--") + 6;
						int endindex = line.indexOf("_用户id");
						vid = line.substring(index, endindex);

						index = line.indexOf("用户id--") + 6;
						endindex = line.indexOf("_昵称");
						uid = line.substring(index, endindex);
					}
					if (vid == null) {
						System.out.println("异常id:" + line);
						writer.write(line);
						writer.newLine();
						continue;
					}
					if (vidSet.add(vid + "-" + uid)) {
						writer.write(line);
						writer.newLine();
					} else {
						System.out.println("发现重复行：" + line);

					}
				}

			}

			reader.close();
			writer.close();
			System.out.println("处理完成，结果已保存到output.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
