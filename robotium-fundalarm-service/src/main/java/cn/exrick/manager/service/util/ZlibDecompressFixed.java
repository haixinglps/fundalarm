package cn.exrick.manager.service.util;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import javax.xml.bind.DatatypeConverter;

public class ZlibDecompressFixed {
	public static void main(String[] args) {
		String unmaskedHex = ("AA 26 E0 B3 E4 8C C4 92 A0 D4 E4 FC A2 14 9F CC E2 12 25 4C BF E4 16 F8 18 47 79 29 E9 28 95 80 B5 18 E8 28 25 E7 E7 95 A4 E6 95 28 59 E5 95 E6 E4 90 E0 47 B8 49 D8 82 90 DA FE 06 00"
				+ "").replace(" ", "").toLowerCase(); // 你的HEX字符串
		System.out.println(unmaskedHex);
		System.out.println("str len:" + unmaskedHex.length());
		byte[] data = hexToBytes(unmaskedHex);
		byte[] withTail = new byte[data.length + 4];
		try {

			System.arraycopy(data, 0, withTail, 0, data.length);
			withTail[data.length] = 0x00;
			withTail[data.length + 1] = 0x00;
			withTail[data.length + 2] = (byte) 0xff;
			withTail[data.length + 3] = (byte) 0xff;

			System.out.println("开始解压...");
			String result = decompress(withTail);
			System.out.println("解压结果:\n" + result);
		} catch (DataFormatException e2) {
			// TODO: handle exception

			e2.printStackTrace();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String decompress(byte[] data) throws Exception {
//		byte[] testData = Arrays.copyOfRange(data, 0, 60);
//		data = testData;
		Inflater inflater = new Inflater(true);
		inflater.setInput(data);
//		for (byte b : data) {
//			System.out.printf("%02X ", b); // 输出: 48 65 6C 6C 6F
//		}

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		long startTime = System.currentTimeMillis();

		try {
			while (!inflater.finished()) {
//				if (System.currentTimeMillis() - startTime > 30000) {
//					throw new RuntimeException("解压超时");
//				}

				try {
					int count = inflater.inflate(buffer);
					// System.out.println("读取字节：" + count);
					if (count > 0) {
						// System.out.println("count:" + count);
						output.write(buffer, 0, count);
					} else {

						// System.out.println("无数据");
						// 返回0，可能是需要输入或输出缓冲区不足
						if (inflater.needsInput()) {
							// 但是我们已经提供了所有数据，所以这里应该退出
							break;
						}

					}
				}

				catch (DataFormatException e) {
//					e.printStackTrace();
					int position = Integer.parseInt(inflater.getBytesRead() + "");
					// System.out.println("pos:" + position);
					// System.out.println("剩余数据: " + Arrays.toString(
					// Arrays.copyOfRange(data, Integer.parseInt(inflater.getBytesRead() + ""),
					// data.length)));
					break;
				}

				catch (Exception e) {

//					e.printStackTrace();
					break;
					// TODO: handle exception
				}

				// System.out.println("继续读取");
			}
			return output.toString("UTF-8");
		} finally {
			inflater.end();
		}
	}

	private static byte[] hexToBytes(String hex) {
		// 实现同前文方法一
		return DatatypeConverter.parseHexBinary(hex);

	}

	public static String unzip(byte[] data) {
//		String unmaskedHex = "54cdbd0e82301885e17b3973632c2db674f33721c6c5e06c2a7c9a2a504301a3867b376a1cdc9ff79c275adb9ca84d0b18aeb5509ca10aa7ec7e2518ccf767efeaadf715184ad7d32e50f3a58954e24367beb8c33ce10a18d8cd637d8bc09037645bca5c45305cc55ceb487121623930e4b62c0f36bfa47f4568bb435a1ffd7babfbdd44e34469864021385f67fe42350c262b3de60b99cc97b19a4ea500434fcd5bc0808fe428c630bc00"; // 你的HEX字符串

		try {
//			byte[] data = hexToBytes(unmaskedHex);
			byte[] withTail = new byte[data.length + 4];
			System.arraycopy(data, 0, withTail, 0, data.length);
			withTail[data.length] = 0x00;
			withTail[data.length + 1] = 0x00;
			withTail[data.length + 2] = (byte) 0xff;
			withTail[data.length + 3] = (byte) 0xff;

			// System.out.println("开始解压...");
			String result = decompress(withTail);
			// System.out.println("\r\n解压结果:" + result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
