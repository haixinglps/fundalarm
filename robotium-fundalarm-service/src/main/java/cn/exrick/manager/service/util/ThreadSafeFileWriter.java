package cn.exrick.manager.service.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeFileWriter {
	private final File file;
	private final ReentrantLock lock = new ReentrantLock();
	private final BufferedWriter writer;

	public ThreadSafeFileWriter(String filePath) throws IOException {
		file = new File(filePath);
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8));
	}

	public void write(String content) {
		lock.lock();
		try {
			writer.write(content);
			writer.newLine(); // 自动换行
			writer.flush();
		} catch (IOException e) {
			throw new RuntimeException("写入文件失败", e);
		} finally {
			lock.unlock();
		}
	}

	public void close() throws IOException {
		writer.close();
	}
}
