package com.andyadc.tinyrpc.demo;

import com.andyadc.tinyrpc.server.RpcServer;

public class ServiceProvider {

	public static void main(String[] args) throws Exception {
		try (RpcServer serviceServer = new RpcServer("echoService", 12345)) {
			serviceServer.registerService(EchoService.class.getName(), new DefaultEchoService());
			serviceServer.start();
		}
	}
}
