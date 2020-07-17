package com.andyadc.codeblocks.test.rpc;

public class ServerClient {

	public static void main(String[] args) {
		System.out.println("server start");
		RpcServer server = new RpcServer();
		server.start(8088, "com.andyadc.codeblocks.test.rpc");
	}
}
