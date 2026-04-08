package cn.exrick.manager.service.util;

import java.io.ByteArrayOutputStream;
import java.util.TreeMap;

import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.TcpPacket;

public class TcpStream {
	int nextExpectedSeq;
	boolean isWebSocket;
	ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	TreeMap<Integer, byte[]> outOfOrder = new TreeMap<>();
	IpV4Packet originalIpPacket;
	TcpPacket originalTcpPacket;
	int count = 0;

	public boolean isWebSocket() {
		return isWebSocket;
	}

	public void setWebSocket(boolean isWebSocket) {
		this.isWebSocket = isWebSocket;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getNextExpectedSeq() {
		return nextExpectedSeq;
	}

	public void setNextExpectedSeq(int nextExpectedSeq) {
		this.nextExpectedSeq = nextExpectedSeq;
	}

	public ByteArrayOutputStream getBuffer() {
		return buffer;
	}

	public void setBuffer(ByteArrayOutputStream buffer) {
		this.buffer = buffer;
	}

	public TreeMap<Integer, byte[]> getOutOfOrder() {
		return outOfOrder;
	}

	public void setOutOfOrder(TreeMap<Integer, byte[]> outOfOrder) {
		this.outOfOrder = outOfOrder;
	}

	public IpV4Packet getOriginalIpPacket() {
		return originalIpPacket;
	}

	public void setOriginalIpPacket(IpV4Packet originalIpPacket) {
		this.originalIpPacket = originalIpPacket;
	}

	public TcpPacket getOriginalTcpPacket() {
		return originalTcpPacket;
	}

	public void setOriginalTcpPacket(TcpPacket originalTcpPacket) {
		this.originalTcpPacket = originalTcpPacket;
	}

}