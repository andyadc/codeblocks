package com.andyadc.codeblocks.test.socket.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * andaicheng
 * 2020-02-04
 */
public class UDPProvider {

	public static void main(String[] args) throws Exception {
		System.out.println("UDPProvider Starting...");

		DatagramSocket datagramSocket = new DatagramSocket(20000);

		byte[] buf = new byte[512];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);

		datagramSocket.receive(packet);

		String ip = packet.getAddress().getHostAddress();
		int port = packet.getPort();
		int length = packet.getLength();
		String data = new String(packet.getData(), 0, length);

		System.out.println("UDPProvider received from ip: " + ip + ", port: " + port + ", data: " + data);

		String responseData = "Receive data with len: " + length;
		byte[] responseDataBytes = responseData.getBytes();
		DatagramPacket responsePacket = new DatagramPacket(responseDataBytes,
			responseDataBytes.length,
			packet.getAddress(),
			packet.getPort());

		datagramSocket.send(responsePacket);

		datagramSocket.close();
		System.out.println("UDPProvider Finished");
	}
}
