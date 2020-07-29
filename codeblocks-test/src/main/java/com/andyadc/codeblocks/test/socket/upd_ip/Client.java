package com.andyadc.codeblocks.test.socket.upd_ip;

public class Client {
	public static void main(String[] args) {
		ServerInfo serverInfo = UDPSearcher.searchServer(10000);
		System.out.println("serverInfo: " + serverInfo);
	}
}
