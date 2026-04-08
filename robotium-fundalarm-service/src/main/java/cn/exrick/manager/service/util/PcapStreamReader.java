package cn.exrick.manager.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.factory.PacketFactories;
import org.pcap4j.packet.namednumber.DataLinkType;

public class PcapStreamReader {
	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("127.0.0.1", 12345); // 端口与nc监听一致
		InputStream input = socket.getInputStream();

		// 1. 首次连接时读取全局头（24B）
		byte[] globalHeader = new byte[24];
		if (input.read(globalHeader) != 24) {
			System.out.println("缺乏日志标记");
			throw new IOException("Invalid PCAP global header");
		}

		// 2. 循环读取数据包记录
		while (true) {
			// 2.1 读取16字节包头
			byte[] packetHeader = new byte[16];
			int headerRead = input.read(packetHeader);
			if (headerRead != 16) {
				System.out.println("缺乏16字节头,继续等待。。。");
				// break; // 连接中断或结束
				continue;
			}

			// 2.2 解析数据长度（caplen字段，偏移量8-11字节）
			int caplen = (packetHeader[8] & 0xFF) | ((packetHeader[9] & 0xFF) << 8);
			byte[] packetData = new byte[caplen];

			// 2.3 确保完整读取数据（应对TCP分段）
			int dataRead = 0;
			while (dataRead < caplen) {
				int chunk = input.read(packetData, dataRead, caplen - dataRead);
				if (chunk == -1) {
					System.out.println("数据完毕");
					break;
				}
				dataRead += chunk;
			}
			if (dataRead != caplen) {
				System.out.println("数据不完整");
				break; // 数据不完整则丢弃
			}

			// 3. 解析并处理数据包
//			Packet packet = Packets.newPacket(packetData, 0, caplen);
//            PcapPacket packet = new PcapPacket(packetHeader, packetData);
			if (caplen > 0) {
				Packet packet = parseRawData(packetData, caplen);

//				if (packet.getPayload() != null)
//					System.out.println(packet.getPayload());

				// 探测websocket请求
				if (isLocalWebSocket(packet)) {
					System.out.println("探测到websocket请求");

					System.out.println(packet);
					break;
				}

				// 探测websocket响应
//				if(isWebSocketResponse(packet))
//					{
//					System.out.println("探测到直播间加入事件");
//					System.out.println(packet);
//					}

			}
		}
	}

	public static Packet parseRawData(byte[] data, int length) {
		return PacketFactories.getFactory(Packet.class, DataLinkType.class).newInstance(data, 0, length,
				DataLinkType.EN10MB);
	}

	public static boolean isLocalWebSocket(Packet packet) {
		try {
			// 检查IPv4层

			IpV4Packet ipV4 = packet.get(IpV4Packet.class);
			if (ipV4 == null) {

				return false;
			}
			// System.out.println("ipv4:" + ipV4.getHeader().getDstAddr().getHostAddress());
			// 检查目的IP是否为127.0.0.1
//			System.out.println(packet);
			if (!ipV4.getHeader().getDstAddr().getHostAddress().equals("127.0.0.1")) {
				return false;
			}

			// 检查TCP层
			TcpPacket tcp = packet.get(TcpPacket.class);
			if (tcp == null)
				return false;

			int dstPort = tcp.getHeader().getDstPort().valueAsInt();
			int srcPort = tcp.getHeader().getSrcPort().valueAsInt();

			if (dstPort == 12345 || srcPort == 12345 || dstPort == 54545 || srcPort == 54545) {
				return false;
			}
			System.out.println("port:" + srcPort + "-->" + dstPort);
//			System.out.println("tcp");
			System.out.println("127.0.0.1");
			// 检查WebSocket端口(通常80/443或自定义端口)

			// 检查HTTP头中的Upgrade字段
			if (tcp.getPayload() != null) {
				byte[] payload = tcp.getPayload().getRawData();
				String payloadStr = new String(payload);
				return payloadStr.contains("Upgrade: websocket") || payloadStr.contains("upgrade: websocket");
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isWebSocketResponse(Packet packet) {
		try {
			TcpPacket tcp = packet.get(TcpPacket.class);
			if (tcp == null || tcp.getPayload() == null)
				return false;

			byte[] payload = tcp.getPayload().getRawData();
			String payloadStr = new String(payload);

//	            // 检查WebSocket握手响应(101状态码)
//	            if (payloadStr.contains("HTTP/1.1 101")) {
//	                return true;
//	            }

			int dstPort = tcp.getHeader().getDstPort().valueAsInt();
			if (dstPort != 42597) {
				return false;
			}

			// 检查WebSocket数据帧(服务端响应帧)
			if (payload.length > 0) {
				byte firstByte = payload[0];
				// FIN=1且Opcode!=8(非关闭帧)
				return (firstByte & 0x80) != 0 && (firstByte & 0x0F) != 8;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
}
