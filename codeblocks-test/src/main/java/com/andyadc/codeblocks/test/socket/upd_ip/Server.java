package com.andyadc.codeblocks.test.socket.upd_ip;

import com.andyadc.codeblocks.test.socket.TCPConstants;

import java.io.IOException;

public class Server {

	public static void main(String[] args) {
		UDPProvider.start(TCPConstants.PORT_SERVER);
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		UDPProvider.stop();
	}
}
