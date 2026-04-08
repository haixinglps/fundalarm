package cn.exrick.manager.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

import io.pkts.Pcap;
import io.pkts.packet.Packet;
import io.pkts.packet.PacketParseException;
import io.pkts.protocol.Protocol;

public class AdbPacketClient {
	public static void main(String[] args) {
		try (Socket socket = new Socket("127.0.0.1", 12345); InputStream input = socket.getInputStream()) {

			Pcap pcap = Pcap.openStream(input);
			pcap.loop(packet -> {
				// 协议解析逻辑
				// 解析以太网层
//				if (packet.hasProtocol(Protocol.ETHERNET_II)) {
//					System.out.println("[MAC] " + packet.getPacket(Protocol.ETHERNET_II).getSourceMac() + " -> "
//							+ packet.getPacket(Protocol.ETHERNET_II).getDestinationMac());
//				}

				// 解析IP层
				if (packet.hasProtocol(Protocol.IPv4)) {
					System.out.println("[IP] " + getSourceIP(packet.getPacket(Protocol.IPv4)) + ":" + getPort(packet, 0)
							+ " -> " + getDestIP(packet.getPacket(Protocol.IPv4)) + ":" + getPort(packet, 1));
					System.out.println(packet.toString());
				}
				return true;
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static String getPort(Packet packet, int type) throws PacketParseException, IOException {

		if (packet.hasProtocol(Protocol.TCP)) {
			byte[] tcpHeader = packet.getPacket(Protocol.TCP).getPayload().getArray();
			int srcPort = ((tcpHeader[0] & 0xFF) << 8) | (tcpHeader[1] & 0xFF);
			int dstPort = ((tcpHeader[2] & 0xFF) << 8) | (tcpHeader[3] & 0xFF);
			if (type == 0)
				return srcPort + "";
			else {
				return dstPort + "";
			}

		}
		return null;
	}

	private static String getDestIP(Packet packet) throws PacketParseException, IOException {

		byte[] rawData = packet.getPayload().getArray();
		byte[] ipHeader = Arrays.copyOfRange(rawData, 0, 20);
		String destIP = String.format("%d.%d.%d.%d", ipHeader[16] & 0xFF, ipHeader[17] & 0xFF, ipHeader[18] & 0xFF,
				ipHeader[19] & 0xFF);

		return destIP;
	}

	private static String getSourceIP(Packet packet) throws PacketParseException, IOException {

		byte[] rawData = packet.getPayload().getArray();
		byte[] ipHeader = Arrays.copyOfRange(rawData, 0, 20);
		String sourceIP = String.format("%d.%d.%d.%d", ipHeader[12] & 0xFF, ipHeader[13] & 0xFF, ipHeader[14] & 0xFF,
				ipHeader[15] & 0xFF);
		return sourceIP;

	}
}
