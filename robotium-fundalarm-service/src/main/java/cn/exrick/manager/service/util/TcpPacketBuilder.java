package cn.exrick.manager.service.util;

import java.util.HashMap;
import java.util.Map;

import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UnknownPacket;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;

public class TcpPacketBuilder {

	public static final Map<String, TcpStream> streamCache = new HashMap<>();

	public static int newFlow = 0;
	public static int serverPort = 8080;

	// 重组数据并构建TCP报文
	private static IpV4Packet deliverCompleteData(String streamKey) {
		TcpStream stream = streamCache.get(streamKey);
		if (stream == null || stream.buffer.size() == 0)
			return null;

		try {
			// 1. 获取原始报文头信息
			IpV4Packet originalIp = stream.originalIpPacket;
			TcpPacket originalTcp = stream.originalTcpPacket;

			// 2. 构建新TCP头部
			TcpPacket.Builder tcpBuilder = new TcpPacket.Builder().srcPort(originalTcp.getHeader().getSrcPort())
					.dstPort(originalTcp.getHeader().getDstPort())
					.sequenceNumber(originalTcp.getHeader().getSequenceNumber())
					.acknowledgmentNumber(originalTcp.getHeader().getAcknowledgmentNumber()).dataOffset((byte) 5)
					.window(originalTcp.getHeader().getWindow())
					.urgentPointer(originalTcp.getHeader().getUrgentPointer())
					.payloadBuilder(new UnknownPacket.Builder().rawData(stream.buffer.toByteArray()));

			// 3. 构建新IP头部
			IpV4Packet.Builder ipBuilder = new IpV4Packet.Builder().version(IpVersion.IPV4)
					.tos(originalIp.getHeader().getTos()).identification(originalIp.getHeader().getIdentification())
					.ttl(originalIp.getHeader().getTtl()).protocol(IpNumber.TCP)
					.srcAddr(originalIp.getHeader().getSrcAddr()).dstAddr(originalIp.getHeader().getDstAddr())
					.payloadBuilder(tcpBuilder);

			// 4. 计算校验和（Pcap4J会自动处理）
			IpV4Packet fullPacket = ipBuilder.build();

			// 5. 输出封装结果
//			//System.out.println("Reconstructed TCP Packet:\n" + fullPacket);
			return fullPacket;

		} finally {
			streamCache.remove(streamKey);
//			newFlow = 1;
		}
	}

	// 处理原始报文时保存头部信息
//    private void processPacket(Packet packet) {
//        if (packet.contains(IpV4Packet.class) && packet.contains(TcpPacket.class)) {
//            IpV4Packet ip = packet.get(IpV4Packet.class);
//            TcpPacket tcp = packet.get(TcpPacket.class);
//            String key = buildStreamKey(ip, tcp);
//
//            TcpStream stream = streamCache.computeIfAbsent(key, k -> new TcpStream());
//            stream.originalIpPacket = ip;
//            stream.originalTcpPacket = tcp;
//            
//            handleTcpSegment(stream, tcp);
//        }
//    }

	// 其他方法保持不变...

	public static String buildStreamKey(IpV4Packet ip, TcpPacket tcp) {
		return ip.getHeader().getSrcAddr().getHostAddress() + ":" + tcp.getHeader().getSrcPort() + "-"
				+ ip.getHeader().getDstAddr().getHostAddress() + ":" + tcp.getHeader().getDstPort();
	}

	public static IpV4Packet handleTcpSegment(String streamKey, TcpPacket tcpPacket) {
		TcpStream stream = streamCache.get(streamKey);
		if (stream == null) {
			System.err.println("stream is null");
			return null;
		}
		int currentSeq = tcpPacket.getHeader().getSequenceNumber();
		byte[] payload = tcpPacket.getPayload() != null ? tcpPacket.getPayload().getRawData() : new byte[0];

		// System.out.println(streamKey + "序列号-当前：" + currentSeq);
		// System.out.println(streamKey + "序列号-等待：" + stream.nextExpectedSeq);
		// 处理乱序包
//		if (currentSeq > stream.nextExpectedSeq) {
//			stream.outOfOrder.put(currentSeq, payload);
//			return null;
//		}

		// 处理按序到达的包
		if (currentSeq == stream.nextExpectedSeq) {
			stream.buffer.write(payload, 0, payload.length);
			// System.out.println("等待序列号0：" + stream.getNextExpectedSeq());
			// System.out.println("负载长度：" + payload.length);
			stream.nextExpectedSeq += payload.length;
			// System.out.println("等待序列号1：" + stream.getNextExpectedSeq());

			// 检查是否有后续乱序包可以合并
//			checkOutOfOrderPackets(stream);
			// System.out.println("拼接完成:" + streamKey + "-->" +
			// stream.getNextExpectedSeq());
			stream.count += 1;
			// System.out.println("asessment segments:" + stream.count);
			// System.out.println("psh/fin:" + tcpPacket.getHeader().getPsh() + "/" +
			// tcpPacket.getHeader().getFin());

			// 检查结束标志 //tcpPacket.getHeader().getPsh() |
			if (stream.count == 2)
				return deliverCompleteData(streamKey);

		}
//		else {
//			if (tcpPacket.getHeader().getFin()) {
//				//System.out.println("流终止");
//			return deliverCompleteData(streamKey);
//			}
//		}
		return null;
	}

	public static void checkOutOfOrderPackets(TcpStream stream) {
		while (!stream.outOfOrder.isEmpty()) {
			Map.Entry<Integer, byte[]> entry = stream.outOfOrder.firstEntry();
			if (entry.getKey() == stream.nextExpectedSeq) {
				byte[] data = entry.getValue();
				stream.buffer.write(data, 0, data.length);
				stream.nextExpectedSeq += data.length;
				stream.outOfOrder.remove(entry.getKey());
			} else {
				break;
			}
		}
	}
}
