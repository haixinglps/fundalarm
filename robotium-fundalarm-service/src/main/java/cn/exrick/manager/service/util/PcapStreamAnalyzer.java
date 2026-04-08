package cn.exrick.manager.service.util;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.IpV4Packet.IpV4Header;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.TcpPacket.TcpHeader;
import org.pcap4j.packet.factory.PacketFactories;
import org.pcap4j.packet.namednumber.DataLinkType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.exrick.manager.pojo.Waiwang2Video;
import cn.exrick.manager.service.RobotService;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;

public class PcapStreamAnalyzer {
	private static final int PCAP_MAGIC = 0xA1B2C3D4;
	private static final int SOCKET_TIMEOUT = 300000;

	static ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext-*.xml");

	public static void main(String[] args) throws FileNotFoundException, IOException {
//		Process adb = Runtime.getRuntime().exec("adb exec-out cat /data/local/tmp/packet_pipe");

		RobotService roomService = context.getBean(RobotService.class);
		//
		try (Socket socket = new Socket("localhost", 12346)) {
//			socket.setSoTimeout(SOCKET_TIMEOUT);
			// InputStream is = adb.getInputStream()
			System.out.println("开始分析");
			analyzeStream(socket.getInputStream(), roomService);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		

//		try (InputStream is = new FileInputStream("C:\\Users\\Administrator\\22.pcap")) {
//			analyzeStream(is);
//		}
	}

	private static void analyzeStream(InputStream input, RobotService robotService) throws IOException {
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

		while (true) {
			if (input.available() < 24) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("WARN: 需要24字节但仅剩" + input.available() + "字节");
				continue;
			} else {
				break;
			}
		}
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
//			if (input.available() < 16) {
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				System.out.println("WARN: 需要16字节但仅剩" + input.available() + "字节");
//				continue;
//			}

			// 读取16字节包头
			byte[] header = readPacket(input, 16);

			// new byte[16];
//			if (input.read(header) != 16) {
//				System.out.println("16包头未发现,暂停十秒");
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
// 					e.printStackTrace();
//				}
// 				continue;
//			}

			// 解析长度字段（小端序）
			// System.out.println(header);
			StringBuilder sb = new StringBuilder();
//			for (byte b : header) {
//				sb.append(String.format("%02X ", b));
//			}
//			// System.out.println("数据头：");
//			System.out.println(sb.toString().trim());

			int caplen = ByteBuffer.wrap(header, 8, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
			// System.out.println("数据长度：" + caplen);
//			if (caplen < 0) {
//				caplen = ByteBuffer.wrap(header, 8, 4).order(ByteOrder.BIG_ENDIAN).getInt();
//				System.out.println("大端序数据长度：" + caplen);
//
//			}

			// 读取包数据
			byte[] data = new byte[caplen];
			int read = 0;
			while (read < caplen) {
				int chunk = input.read(data, read, caplen - read);
				if (chunk == -1) {
					/// throw new EOFException();
					break;
				}
				read += chunk;
			}

			// 解析数据包

			if (data.length > 0) {
				Packet packet = PacketFactories.getFactory(Packet.class, DataLinkType.class).newInstance(data, 0,
						data.length, linkType);

				processPacket(packet, tcpStreams, robotService);
			}
		}
	}

	private static void processPacket(Packet packet, Map<FlowKey, ByteArrayOutputStream> streams,
			RobotService robotService) {
		if (packet.contains(IpV4Packet.class)) {
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

				if (tcp.getHeader().getSrcPort().toString().indexOf("54545") != -1
						|| tcp.getHeader().getDstPort().toString().indexOf("54545") != -1)
					return;

				if (tcp.getHeader().getSrcPort().toString().indexOf("12345") != -1
						|| tcp.getHeader().getDstPort().toString().indexOf("12345") != -1)
					return;
				if (tcp.getHeader().getSrcPort().toString().indexOf("12346") != -1
						|| tcp.getHeader().getDstPort().toString().indexOf("12346") != -1)
					return;
				if (tcp.getHeader().getSrcPort().toString().indexOf("8888") != -1
						|| tcp.getHeader().getDstPort().toString().indexOf("8888") != -1)
					return;
				if (TcpPacketBuilder.serverPort != 8080) {
					if (tcp.getHeader().getDstPort().valueAsInt() != TcpPacketBuilder.serverPort
							&& tcp.getHeader().getSrcPort().valueAsInt() != TcpPacketBuilder.serverPort) {
						return;
					}

				}

//				FlowKey key = new FlowKey(ipv4.getHeader(), tcp.getHeader());

				// WebSocket握手检测
				int tag = 0;
				if (tcp.getPayload() != null && isWebSocketHandshake(tcp.getPayload().getRawData())) {
					// System.out.println("[WebSocket] Handshake detected: ");
					System.out.println(new String(tcp.getPayload().getRawData()));
					int dst = tcp.getHeader().getDstPort().valueAsInt();
					TcpPacketBuilder.serverPort = dst;
					System.out.println(dst);
//					tag = 1;
					return;

				}

				if (tcp.getPayload() != null) {
//					

					// 检查HTTP 101握手响应
//					TcpPacketBuilder.newFlow = 0;
					String streamKey = TcpPacketBuilder.buildStreamKey(ipv4, tcp);
//					if (tag == 1) {
//						stream.setWebSocket(true);
//					}
					// 获取五元组标识

					// 处理TCP分段
					if (tcp.getHeader().getDstPort().valueAsInt() == TcpPacketBuilder.serverPort
							&& ((tcp.getPayload().length() <= 8 && tcp.getPayload().length() > 0)
									|| TcpPacketBuilder.streamCache.containsKey(streamKey))) {
						TcpStream stream = TcpPacketBuilder.streamCache.computeIfAbsent(streamKey, k -> thisstream);

						IpV4Packet combinPacket = TcpPacketBuilder.handleTcpSegment(streamKey, tcp);
						if (combinPacket == null) {
							// System.out.println("tcp segment分段包，继续等待分段包,长度：" + tcp.getPayload().length());
							return;
						} else {

							// 处理数据即可：
							tcp = (TcpPacket) combinPacket.getPayload();
							// System.out.println("发现完整tcp包,长度：" + tcp.getPayload().length());
							// 对strem进行重置，放入
//						payload = tcp.getPayload().getRawData();
						}

					}

					byte[] payload = tcp.getPayload().getRawData();
//					if (payload.length > 6) {
//						
//
//					}
					if (new String(payload).toLowerCase().contains("101 switching protocols")) {
						// System.out.println("WebSocket握手响应 detected");
					}

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
						if (isValidWebSocketOpcode) {
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
								System.out.println(
										"src ip:" + ipv4.getHeader().getSrcAddr() + ":" + tcp.getHeader().getSrcPort());
								System.out.println("dest ip:" + ipv4.getHeader().getDstAddr() + ":"
										+ tcp.getHeader().getDstPort());

								System.out.println(dtString);
								JSONObject jObject = null;
								try {
									jObject = new JSONObject(dtString);
								} catch (Exception e) {
									return;
									// TODO: handle exception
								}

								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

								if (dtString.indexOf("join") != -1) {
									String author = jObject.getStr("liveUserId");
									String roomId = jObject.getStr("targetId");
									String roomName = jObject.getJSONObject("msgBody").getStr("id");
									Long tm = jObject.getJSONObject("msgBody").getLong("createTime");
									Date dt = new Date(tm);
									String info = author + "\t" + roomId + "\t" + sdf.format(dt) + "\t" + roomName
											+ "\r\n";
									System.out.println(info);
									FileUtil.appendUtf8String(info, "d:/download/live.txt");

									Waiwang2Video video = new Waiwang2Video();
									video.setAuthor(author);
//									video.setUid(author);
									Waiwang2Video firstWork = robotService.getAuthorFirst(author);
									if (firstWork != null) {
										System.out.println("昵称：" + firstWork.getNickname());
										System.out.println("uid:" + firstWork.getUid());
										video.setNickname(firstWork.getNickname());
										video.setUid(firstWork.getUid());

									}

									video.setVid(Integer.parseInt(roomId));
									String nc = video.getNickname();
									String strdate = sdf2.format(dt);
									String title = strdate;

//									if (nc != null) {
//										Path filePathHistory = Paths.get("\\\\192.168.1.254\\Users\\tt\ww.txt");
									Path filePathHistory = null;

									filePathHistory = Paths.get("y:/ww.txt");

									try {
										List<String> linesHistory = Files.readAllLines(filePathHistory);
										for (int j = linesHistory.size() - 1; j >= 0;) {
											String item = linesHistory.get(j);
											String itemdt = item.split("\t")[1];
//												if (item.indexOf(nc) != -1 && itemdt.contentEquals(strdate)) {
//													title = item.split("\t")[0] + "_" + title;
//													break;
//												}
											title = item.split("\t")[0] + "_" + title;
											break;

										}

									} catch (IOException e) {
										e.printStackTrace();
									}

//									}

									video.setTitle(
											title + "_" + author + "_" + video.getUid() + "_" + video.getNickname());
									// 通过author定位历史用户昵称

//									video.setNickname(name);
									video.setCover(
											"https://qqcdn7.cjvhh.cn/app/user/162737_20251123205035_4862c74c514ae0066fe72f797bc38d74.png?imageView2/2/w/56");
									video.setUrl(UUID.randomUUID().toString().replace("-", ""));
									video.setPhoto(
											"https://qqcdn7.cjvhh.cn/app/user/162737_20251123205035_4862c74c514ae0066fe72f797bc38d74.png?imageView2/2/w/56");
									video.setDt(sdf.format(dt));
									try {

										System.out.println("保存数据");
										robotService.addVideo(video);
										System.out.println("保存成功");
									} catch (Exception e) {
										e.printStackTrace();
										// TODO: handle exception
									}

								} else if (dtString.indexOf("C_updateRoomNotify") != -1) {

									String author = jObject.getJSONObject("msgBody").getStr("liveUserId");
									String roomId = jObject.getJSONObject("msgBody").getStr("roomId");
									String roomName = jObject.getJSONObject("msgBody").getStr("roomTitle");
									Long tm = jObject.getLong("sysTime");
									Date dt = new Date(tm);
									int status = jObject.getJSONObject("msgBody").getInt("roomStatus");
									String name = jObject.getJSONObject("msgBody").getStr("liveNickName");
									String photo = jObject.getJSONObject("msgBody").getStr("liveAvatar");
									String roomPhoto = jObject.getJSONObject("msgBody").getStr("roomCoverUrl");
									String info = author + "\t" + roomId + "\t" + "\t" + roomName + "\t" + status + "\t"
											+ name + "\t" + photo + "\t" + roomPhoto + "\t" + sdf.format(dt) + "\r\n";
									System.out.println(info);
									FileUtil.appendUtf8String(info, "d:/download/roomInfo.txt");
									Waiwang2Video video = new Waiwang2Video();
									video.setAuthor(author);
									video.setVid(Integer.parseInt(roomId));

									video.setTitle(roomName + "_" + author + "_" + name);
									video.setNickname(name);
									video.setCover(roomPhoto);
									video.setUrl(UUID.randomUUID().toString().replace("-", ""));
									video.setPhoto(photo);
									video.setDt(sdf.format(dt));
									try {

										System.out.println("保存数据");
										robotService.addVideo(video);
										System.out.println("保存成功");
									} catch (Exception e) {
										e.printStackTrace();
										// TODO: handle exception
									}

								}
//							//System.out.println(message);
							}
						}
					}

//					if (TcpPacketBuilder.newFlow == 1) {
//						// 重新解析，本次解析搞的是已合并包的情况。
//						processPacket(packet, streams);
//					}
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
