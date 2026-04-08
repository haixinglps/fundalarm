package cn.exrick.manager.service.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class GzipUtils {
	public static String decompress(byte[] compressed) throws IOException {

		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(compressed))) {
			byte[] buffer = new byte[1024];
			int len;
			while ((len = gzip.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			return out.toString("UTF-8");
		}
	}
}
