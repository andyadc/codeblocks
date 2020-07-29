package com.andyadc.codeblocks.test.socket.tcp_ip;

import com.andyadc.codeblocks.test.socket.TCPConstants;
import com.andyadc.codeblocks.test.socket.upd_ip.UDPProvider;

public class Server {

	public static void main(String[] args) throws Exception {
		TCPServer tcpServer = new TCPServer(TCPConstants.PORT_SERVER);
		boolean isSucceed = tcpServer.start();
		if (!isSucceed) {
			System.out.println("Start TCP server failed!");
			return;
		}
		UDPProvider.start(TCPConstants.PORT_SERVER);

		System.in.read();

		UDPProvider.stop();
		tcpServer.stop();
	}
}
