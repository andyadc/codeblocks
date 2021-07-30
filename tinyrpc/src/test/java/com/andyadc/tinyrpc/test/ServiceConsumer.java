package com.andyadc.tinyrpc.test;

import com.andyadc.tinyrpc.client.RpcClient;

/**
 * Service Consumer
 */
public class ServiceConsumer {

	public static void main(String[] args) throws Exception {
		try (RpcClient rpcClient = new RpcClient()) {
			EchoService echoService = rpcClient.getService("echoService", EchoService.class);
			System.out.println(echoService.echo("Hello,World"));
		}
	}
}
