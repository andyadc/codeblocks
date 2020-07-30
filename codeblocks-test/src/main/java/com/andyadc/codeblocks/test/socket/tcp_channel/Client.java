package com.andyadc.codeblocks.test.socket.tcp_channel;

import com.andyadc.codeblocks.test.socket.upd_ip.ServerInfo;
import com.andyadc.codeblocks.test.socket.upd_ip.UDPSearcher;

public class Client {

	public static void main(String[] args) throws Exception {
		ServerInfo serverInfo = UDPSearcher.searchServer(10000);
		System.out.println("serverInfo: " + serverInfo);

		if (serverInfo != null) {
			TCPClient.linkWith(serverInfo);
		}
	}
}
