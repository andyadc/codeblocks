package com.andyadc.codeblocks.test.socket.tcp_channel;

import com.andyadc.codeblocks.test.socket.TCPConstants;
import com.andyadc.codeblocks.test.socket.upd_ip.UDPProvider;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Server {

	public static void main(String[] args) throws Exception {
		TCPServer tcpServer = new TCPServer(TCPConstants.PORT_SERVER);
		boolean start = tcpServer.start();
		if (!start) {
			System.out.println("Start TCP server failed!");
			return;
		}

		UDPProvider.start(TCPConstants.PORT_SERVER);

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String s;
		do {
			s = bufferedReader.readLine();
			tcpServer.broadcast(s);
		} while (!"exit".equalsIgnoreCase(s));

		tcpServer.stop();
	}
}
