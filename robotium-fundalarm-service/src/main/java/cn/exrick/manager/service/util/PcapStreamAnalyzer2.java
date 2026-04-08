package cn.exrick.manager.service.util;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.IpV4Packet.IpV4Header;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.TcpPacket.TcpHeader;
import org.pcap4j.packet.factory.PacketFactories;
import org.pcap4j.packet.namednumber.DataLinkType;

import cn.exrick.manager.pojo.Wanwu;
import cn.hutool.core.io.FileUtil;

public class PcapStreamAnalyzer2 {
	private static final int PCAP_MAGIC = 0xA1B2C3D4;
	private static final int SOCKET_TIMEOUT = 3600000;
//	static ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-*.xml");
//	static RobotService roomService = context.getBean(RobotService.class);

	public static void main(String[] args) throws FileNotFoundException, IOException {
		try (Socket socket = new Socket("localhost", 12345)) {
//			socket.setSoTimeout(SOCKET_TIMEOUT);
			analyzeStream(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}

//		  Process process = Runtime.getRuntime().exec("adb exec-out tcpdump -i wlan0 -w -");
//	        InputStream stream = process.getInputStream();

//	        PcapHandle handle = Pcaps.openOffline
//	        handle.loop(-1, new PacketListener() {
//				
//				@Override
//				public void gotPacket(Packet packet) {
//	                System.out.println(packet);
//
//					
//				}
//			});

//		

//		try (InputStream is = new FileInputStream("C:\\Users\\Administrator\\44.pcap")) {
//			analyzeStream(is);
//		}
	}

	private static void analyzeStream(InputStream input) throws IOException {
//		// 验证PCAP魔数
//		byte[] magic = new byte[4];
//		if (input.read(magic) != 4 || ByteBuffer.wrap(magic).getInt() != PCAP_MAGIC) {
//			throw new IOException("Invalid PCAP magic number");
//		}
//		
//		
//		ByteBuffer buffer = ByteBuffer.wrap(globalHeader).order(ByteOrder.LITTLE_ENDIAN);
//		int linkTypeValue = buffer.getShort(20) & 0xFFFF; // 偏移量20-21字节
//		DataLinkType linkType = DataLinkType.getInstance(linkTypeValue);
//
//		// 跳过剩余全局头
//		input.skip(20);

		// 1. 读取并验证PCAP全局头
		byte[] globalHeader = new byte[24];
		if (input.read(globalHeader) != 24) {
			throw new IOException("Incomplete PCAP header");
		}

		// 2. 动态检测链路类型（小端序）
		ByteBuffer buffer = ByteBuffer.wrap(globalHeader).order(ByteOrder.LITTLE_ENDIAN);
		int magicNumber = buffer.getInt();
		if (magicNumber != PCAP_MAGIC && magicNumber != 0xD4C3B2A1) {
			throw new IOException("Invalid PCAP magic: 0x" + Integer.toHexString(magicNumber));
		}

		int linkTypeValue = buffer.getShort(20) & 0xFFFF;
		DataLinkType linkType = DataLinkType.getInstance(linkTypeValue);
		System.out.println("Detected link type: " + linkType);

		// TCP流重组缓存
		Map<FlowKey, ByteArrayOutputStream> tcpStreams = new HashMap<>();

		while (true) {
			// 读取16字节包头
			// 增加可用字节数检查
//			if (input.available() < 16) {
//				System.out.println("WARN: 需要16字节但仅剩" + input.available() + "字节");
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				continue;
//			}
//
//			byte[] header = new byte[16];
//			if (input.read(header) != 16) {
//				System.out.println("16包头未发现,暂停十秒");
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
////				break;
//				continue;
//			}
			byte[] header = readPacket(input, 16);

			// 解析长度字段（小端序）
			int caplen = ByteBuffer.wrap(header, 8, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
//			//System.out.println("长度：" + caplen);
			// 读取包数据
			byte[] data = new byte[caplen];
			int read = 0;
			while (read < caplen) {
				int chunk = input.read(data, read, caplen - read);
				if (chunk == -1)
					throw new EOFException();
				read += chunk;
			}

			// 解析数据包

//			System.out.println("begin...");
			if (data.length > 0) {
				Packet packet = PacketFactories.getFactory(Packet.class, DataLinkType.class).newInstance(data, 0,
						data.length, linkType);

				processPacket(packet, tcpStreams);
			}
		}
	}

	private static void processPacket(Packet packet, Map<FlowKey, ByteArrayOutputStream> streams) {
		if (packet.contains(IpV4Packet.class)) {

			// 改造后的RTMP包构建方式
//			RtmpPacket rtmpPacket = (RtmpPacket) packet;
//
//			if (rtmpPacket.getMsgType() == 0x14 || rtmpPacket.getMsgType() == 0x11) {
//				System.out.println("Packet #" + packetCount + ": RTMP Command Packet");
//				System.out.println("Command Name: " + rtmpPacket.getCommandName());
//				System.out.println("Transaction ID: " + rtmpPacket.getTransactionId());
//				System.out.println("Parameters: " + rtmpPacket.getParameters());
//			}

			IpV4Packet ipv4 = packet.get(IpV4Packet.class);

			IpV4Header ipHeader = ipv4.getHeader();

			// 检测IP分片
			if (ipHeader.getMoreFragmentFlag() || ipHeader.getFragmentOffset() > 0) {
				String fragmentKey = FragmentationDetector.buildFragmentKey(ipHeader);
				FragmentationDetector.fragmentCache.computeIfAbsent(fragmentKey, k -> new ArrayList<>()).add(ipv4);

				if (ipHeader.getMoreFragmentFlag() == false) {
					// System.out.println("开始合并分片，重置一个ipv4完整包给后续程序");
					ipv4 = FragmentationDetector.reassemblePackets(fragmentKey); // 触发重组
					// System.out.println("合并完毕");

				}

				else {
					// System.out.println("属于分片，缓存起来，暂不处理");
					return;
				}

			} else {
//				//System.out.println("发现一个完整包");
			}

			if (ipv4.getPayload() instanceof TcpPacket) {
				TcpPacket tcp = (TcpPacket) ipv4.getPayload();
				TcpStream thisstream = new TcpStream();
				thisstream.setOriginalIpPacket(ipv4);
				thisstream.setOriginalTcpPacket(tcp);
				thisstream.setNextExpectedSeq(tcp.getHeader().getSequenceNumber());

				if (tcp.getHeader().getDstPort().toString().indexOf("1935") == -1)
					return;
//				&& tcp.getHeader().getDstPort().toString().indexOf("80") == -1
				// System.out.println("发现rmpt");
//				FlowKey key = new FlowKey(ipv4.getHeader(), tcp.getHeader());

				// WebSocket握手检测
//				int tag = 0;
//				if (tcp.getPayload() != null && isWebSocketHandshake(tcp.getPayload().getRawData())) {
//					// System.out.println("[WebSocket] Handshake detected: ");
//					System.out.println(new String(tcp.getPayload().getRawData()));
//					int dst = tcp.getHeader().getDstPort().valueAsInt();
//					TcpPacketBuilder.serverPort = dst;
//					System.out.println(dst);
////					tag = 1;
//					return;
//
//				}

				if (tcp.getPayload() != null) {

					byte[] payload = tcp.getPayload().getRawData();

					if (tcp.getHeader().getDstPort().toString().indexOf("1935") == -1) {
						if (payload.length >= 2) {

							byte secondByte = payload[1];
//						boolean isMasked = (secondByte & 0x80) != 0; // Mask标志位
							int opcode = payload[0] & 0x0F; // 操作码
							// !isMasked &&

							// 所有有效的WebSocket操作码
							boolean isValidWebSocketOpcode = opcode == 0x0 || // 继续帧
									opcode == 0x1 || // 文本帧
									opcode == 0x2 || // 二进制帧
									opcode == 0x8 || // 关闭连接
									opcode == 0x9 || // Ping
									opcode == 0xA; // Pong

//						if (opcode == 0x1 || opcode == 0x2) {
							if (isValidWebSocketOpcode || 1 == 1) {
//							//System.out.println("===============");
//							//System.out.println(packet);
//							//System.out.println("----");

//							//System.out.println("----");

								// //System.out.println("WebSocket响应帧 detected");
//							int payloadStart = 2 + (isMasked ? 4 : 0); // 跳过帧头
//							String message = new String(payload);
//							for (byte b : payload) {
//								System.out.printf("%02X ", b); // 输出: 48 65 6C 6C 6F
//							}
								// System.out.println("\n");
								String dtString = parseWebSocketFrame(payload);

								if (dtString != null && !dtString.contentEquals("")) {

									System.out.println("");
									System.out.println("src ip:" + ipv4.getHeader().getSrcAddr() + ":"
											+ tcp.getHeader().getSrcPort());
									System.out.println("dest ip:" + ipv4.getHeader().getDstAddr() + ":"
											+ tcp.getHeader().getDstPort());

									System.out.println(dtString);
//								JSONObject jObject = null;
//								try {
//									jObject = new JSONObject(dtString);
//								} catch (Exception e) {
//									return;
//									// TODO: handle exception
//								}

									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
									int indx = dtString.indexOf("wiresharkData:");
									if (indx != -1) {

										try {

											System.out.println("保存数据:" + dtString);
											Wanwu wanwu = new Wanwu();
											wanwu.setName(dtString.substring(indx + 14));
											wanwu.setUrl(sdf2.format(new Date()));
//											roomService.addTiteDate(wanwu);
											System.out.println("保存成功");
										} catch (Exception e) {
											e.printStackTrace();
											// TODO: handle exception
										}
										return;

									}
//							//System.out.println(message);
								}
								// return;

							}

						}

					} else {

						// System.out.println("------------------");
//					for (byte b : payload) {
//						System.out.printf("%02X ", b); // 输出: 48 65 6C 6C 6F
//					}
						Map<String, Object> data = RtmpAmf0Parser.parseAmf0Command(payload);
						if (data == null) {
							// System.out.println("解析失败");

							return;
						}
//					JSONObject jObject = new JSONObject(result);
//					System.out.println(jObject.toString());
						org.json.JSONObject json = new org.json.JSONObject();
						for (Map.Entry<String, Object> entry : data.entrySet()) {
							json.put(entry.getKey(), entry.getValue());
						}
						System.out.println(json.toString(4));
						if (json.getString("command").contentEquals("play")) {
							Date dt = new Date();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String roomIdStr = json.getString("streamName").split("\\?")[0];
							String roomId = roomIdStr.substring(5);

							String url = "rtmp://play2.fjefu.cn/ww/" + json.getString("streamName");
							String info = roomId + "\t" + sdf.format(dt) + "\t" + url + "\r\n";
							System.out.println(info);
							FileUtil.appendUtf8String(info, "d:/download/mp4.txt");
						}

					}

				}

				// TCP流重组逻辑
//				if (tcp.getPayload() != null && tcp.getPayload().length() > 0) {
//					streams.computeIfAbsent(key, k -> new ByteArrayOutputStream()).write(tcp.getPayload().getRawData(),
//							0, tcp.getPayload().length());
//				}
			}
		}
	}

	private static boolean isWebSocketHandshake(byte[] data) {
		String payload = new String(data).toLowerCase();
		return payload.contains("upgrade: websocket") && payload.contains("sec-websocket-key");
	}

	// WebSocket帧基础头部长度
	private static final int BASE_HEADER_LENGTH = 2;

	/**
	 * 解析TCP报文中的WebSocket帧
	 * 
	 * @param tcpPayload TCP报文负载数据
	 * @return 解析后的消息内容（已处理分片和掩码）
	 */

	public static String parseWebSocketFrame(byte[] tcpPayload) {
		if (tcpPayload.length < BASE_HEADER_LENGTH) {
			throw new IllegalArgumentException("Invalid WebSocket frame length");
		}

		// 混合协议摘取websocket isWebSocket
		if (1 == 0) {

			int index = 0;
			int find = 0;
			// System.out.println("修正websocket:");

			while (index < tcpPayload.length) {
				// 搜索 HTTP 结束标志 \r\n\r\n

				int endHeader = findHeaderEnd(tcpPayload, index);
				if (endHeader != -1) {
					find = 1;
					index = endHeader + 4; // 跳过 \r\n\r\n
					tcpPayload = Arrays.copyOfRange(tcpPayload, index, tcpPayload.length);
				} else {
//					return; // 等待更多 HTTP 数据
					index += 1;
					continue;
				}

//				break;

			}
			if (find == 0)
				return null;

		}

		// 解析帧头
		byte firstByte = tcpPayload[0];
		byte secondByte = tcpPayload[1];

		boolean fin = (firstByte & 0x80) != 0; // FIN标志位
		int opcode = firstByte & 0x0F; // 操作码
		boolean isMasked = (secondByte & 0x80) != 0; // 掩码标志
		int payloadLen = secondByte & 0x7F; // 基础负载长度
		// System.out.println("掩码：" + isMasked);

		// 处理扩展长度
		int headerLen = BASE_HEADER_LENGTH;
		if (payloadLen == 126) {
			if (tcpPayload.length < 4)
				return null;

			payloadLen = ByteBuffer.wrap(tcpPayload, 2, 2).getShort() & 0xFFFF;
			headerLen += 2;
		} else if (payloadLen == 127) {
			if (tcpPayload.length < 10)
				return null;

			payloadLen = (int) ByteBuffer.wrap(tcpPayload, 2, 8).getLong();
			headerLen += 8;
		}

		// 处理掩码键
		byte[] maskingKey = null;
		if (isMasked) {
			maskingKey = Arrays.copyOfRange(tcpPayload, headerLen, headerLen + 4);
			headerLen += 4;
			// System.out.println("掩码value：" + new String(maskingKey));
//			for (byte b : maskingKey) {
//				// System.out.printf("%02X ", b); // 输出: 48 65 6C 6C 6F
//			}
			// System.out.println("\n");
		}

		// System.out.println("\r\n头长度a：" + headerLen);
		// System.out.println("数据长度b：" + payloadLen);
		// System.out.println("包长度c：" + tcpPayload.length);
		if (headerLen + payloadLen != tcpPayload.length) {
			return null;
		}

		// 提取负载数据
		byte[] payloadData = Arrays.copyOfRange(tcpPayload, headerLen, headerLen + payloadLen);

		// 掩码解码（仅客户端帧需要）
		if (isMasked && maskingKey != null) {
			// System.out.println("去掩码...");
			for (int i = 0; i < payloadData.length; i++) {
				payloadData[i] ^= maskingKey[i % 4];
			}
		}

		// 解压：zlib解压

		// 返回UTF-8解码的文本内容（仅处理文本帧）
		if (opcode == 0x1) {

//			for (byte b : payloadData) {
//				System.out.printf("%02X ", b); // 输出: 48 65 6C 6C 6F
//			}
			// System.out.println("");
			return ZlibDecompressFixed.unzip(payloadData);
//			return new String(payloadData, StandardCharsets.UTF_8);
		} else {
			return "[Binary Frame]";
		}
	}

	/**
	 * 在字节数组中查找HTTP头部结束标记\r\n\r\n
	 * 
	 * @param data       待搜索的字节数组
	 * @param startIndex 开始搜索的位置
	 * @return 结束标记起始位置索引，未找到返回-1
	 */
	public static int findHeaderEnd(byte[] data, int startIndex) {
		// 边界检查
		if (data == null || startIndex < 0 || startIndex >= data.length - 3) {
			return -1;
		}

		// 遍历查找连续\r\n\r\n
		for (int i = startIndex; i < data.length - 3; i++) {
			if (data[i] == '\r' && data[i + 1] == '\n' && data[i + 2] == '\r' && data[i + 3] == '\n') {
				return i;
			}
		}
		return -1;
	}

	static class FlowKey {
		final String srcIp, dstIp;
		final int srcPort, dstPort;

		FlowKey(IpV4Header ipHeader, TcpHeader tcpHeader) {
			this.srcIp = ipHeader.getSrcAddr().getHostAddress();
			this.dstIp = ipHeader.getDstAddr().getHostAddress();
			this.srcPort = tcpHeader.getSrcPort().value();
			this.dstPort = tcpHeader.getDstPort().value();
		}

		@Override
		public boolean equals(Object o) {
			return false;
			// 实现equals和hashCode
		}
	}

	/**
	 * 从输入流中读取指定长度的数据包
	 * 
	 * @param in   输入流对象
	 * @param size 需要读取的字节数
	 * @return 包含读取数据的字节数组
	 * @throws IOException 当发生I/O错误或流提前结束时抛出
	 */
	public static byte[] readPacket(InputStream in, int size) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int remaining = size;
		byte[] chunk = new byte[Math.min(4096, size)];

		while (remaining > 0) {
			int read = in.read(chunk, 0, Math.min(chunk.length, remaining));
			if (read == -1) {
				throw new EOFException("Unexpected end of stream");
			}
			buffer.write(chunk, 0, read);
			remaining -= read;
		}

		return buffer.toByteArray();
	}
}
