package cn.exrick.manager.service.util;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AlarmPlayer {
	// 在这里添加代码
	private static final int SAMPLE_RATE = 44100;
//	private static final int DURATION = 2; // 播放时长为2秒
	private static final double FREQUENCY = 440; // 频率为440Hz

	@Async
	public void playAlarm(int seconds) {
		try {
			AudioFormat audioFormat = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);
			SourceDataLine line = AudioSystem.getSourceDataLine(audioFormat);
			line.open(audioFormat, SAMPLE_RATE);
			line.start();

			byte[] buffer = new byte[SAMPLE_RATE * seconds];
			for (int i = 0; i < buffer.length; i++) {
				double angle = (2.0 * Math.PI * FREQUENCY * i) / SAMPLE_RATE;
				buffer[i] = (byte) (Math.sin(angle) * 127.0);
			}

			line.write(buffer, 0, buffer.length);
			line.drain();
			line.stop();
			line.close();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	@Async
	public void playAlarm2(int seconds) {
		double fre = 2000;
		try {
			AudioFormat audioFormat = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);
			SourceDataLine line = AudioSystem.getSourceDataLine(audioFormat);
			line.open(audioFormat, SAMPLE_RATE);
			line.start();

			byte[] buffer = new byte[SAMPLE_RATE * seconds];
			for (int i = 0; i < buffer.length; i++) {
				double angle = (2.0 * Math.PI * fre * i) / SAMPLE_RATE;
				buffer[i] = (byte) (Math.sin(angle) * 127.0);
			}

			line.write(buffer, 0, buffer.length);
			line.drain();
			line.stop();
			line.close();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
//			playal(5);
//		System.out.println("#EXTM3U\r\n" + "#EXT-X-VERSION:3\r\n" + "#EXT-X-TARGETDURATION:7\r\n"
//				+ "#EXT-X-MEDIA-SEQUENCE:0\r\n"
//				+ "#EXT-X-KEY:METHOD=AES-128,URI=\"https://www.tlhf01.com/videos/202208/13/62f6a0015a0b19072ffb5dbb/ts.key\",IV=0x00000000000000000000000000000000\r\n"
//				+ "");
//		for (int i = 0; i < 476; i++) {
//			System.out.println("#EXTINF:4.000000,\r\n"
//					+ "https://tlvod.chinaysfc.com/videos/202208/13/62f6a0015a0b19072ffb5dbb/863a3a/index" + i + ".ts");
//		}
//		System.out.println("#EXT-X-ENDLIST");
	}
}