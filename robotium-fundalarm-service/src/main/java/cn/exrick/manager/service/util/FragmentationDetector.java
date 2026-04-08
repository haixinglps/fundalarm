package cn.exrick.manager.service.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.IpV4Packet.IpV4Header;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UnknownPacket;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;
import org.pcap4j.packet.namednumber.TcpPort;

public class FragmentationDetector {
	// 存储分片包的缓存结构
	public static Map<String, List<IpV4Packet>> fragmentCache = new HashMap<>();

	public static void processPacket(Packet packet) {
		if (packet.contains(IpV4Packet.class)) {
			IpV4Packet ipPacket = packet.get(IpV4Packet.class);
			IpV4Header ipHeader = ipPacket.getHeader();

			// 检测IP分片
			if (ipHeader.getMoreFragmentFlag() || ipHeader.getFragmentOffset() > 0) {
				String fragmentKey = buildFragmentKey(ipHeader);
				fragmentCache.computeIfAbsent(fragmentKey, k -> new ArrayList<>()).add(ipPacket);

				if (ipHeader.getMoreFragmentFlag() == false) {
					IpV4Packet finaPacket = reassemblePackets(fragmentKey); // 触发重组

				}

			}

		}
	}

	public static String buildFragmentKey(IpV4Header header) {
		return header.getSrcAddr() + "-" + header.getDstAddr() + "-" + header.getIdentification();
	}

	public static IpV4Packet reassemblePackets(String key) {
		List<IpV4Packet> fragments = fragmentCache.get(key);
		fragments.sort(Comparator.comparingInt(p -> p.getHeader().getFragmentOffset()));

		// 重组逻辑
		byte[] reassembled = new byte[calculateTotalLength(fragments)];
		int position = 0;
		for (IpV4Packet frag : fragments) {
			byte[] payload = frag.getPayload().getRawData();
			System.arraycopy(payload, 0, reassembled, position, payload.length);
			position += payload.length;
		}
		System.out.println("重组完成，总长度: " + reassembled.length);
		fragmentCache.remove(key);
		IpV4Packet re = null;
		try {
			InetAddress srcAddr = Inet4Address.getByName("127.0.0.1");
			re = buildCompletePacket(reassembled, srcAddr, srcAddr);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;
	}

	private static int calculateTotalLength(List<IpV4Packet> fragments) {
		return fragments.stream().mapToInt(p -> p.getPayload().length()).sum();
	}

	public static IpV4Packet buildCompletePacket(byte[] reassembled, InetAddress srcAddr, InetAddress dstAddr)
			throws UnknownHostException {

		// 3. 构造原始TCP负载（需确保reassembled包含完整TCP报文）
//		UnknownPacket.Builder payloadBuilder = new UnknownPacket.Builder().rawData(reassembled);
		TcpPacket.Builder tcpBuilder = new TcpPacket.Builder().srcPort(new TcpPort((short) 80, ""))
				.dstPort(new TcpPort((short) 80, "")).payloadBuilder(new UnknownPacket.Builder().rawData(reassembled));

		IpV4Packet.Builder ipBuilder = new IpV4Packet.Builder().version(IpVersion.IPV4).protocol(IpNumber.TCP)
				.srcAddr((Inet4Address) Inet4Address.getByName("127.0.0.1"))
				.dstAddr((Inet4Address) Inet4Address.getByName("127.0.0.1"))

				.correctChecksumAtBuild(true);

		ipBuilder.payloadBuilder(tcpBuilder);

		return ipBuilder.build();

	}
}
