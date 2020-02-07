package com.andyadc.codeblocks.test.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * andaicheng
 * 2020-02-04
 */
public class UDPSearcher {

	public static void main(String[] args) throws Exception {
		System.out.println("UDPSearcher Starting...");

		DatagramSocket datagramSocket = new DatagramSocket();

		String requestData = "Hello World";
		byte[] requestDataBytes = requestData.getBytes();

		DatagramPacket requestPacket = new DatagramPacket(requestDataBytes, requestDataBytes.length);
		requestPacket.setAddress(InetAddress.getLocalHost());
		requestPacket.setPort(20000);

		datagramSocket.send(requestPacket);

		byte[] buf = new byte[512];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);

		datagramSocket.receive(packet);

		String ip = packet.getAddress().getHostAddress();
		int port = packet.getPort();
		int length = packet.getLength();
		String data = new String(packet.getData(), 0, length);

		System.out.println("UDPSearcher received from ip: " + ip + ", port: " + port + ", data: " + data);


		datagramSocket.close();
		System.out.println("UDPSearcher Finished");
	}
}
