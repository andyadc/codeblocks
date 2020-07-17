package com.andyadc.codeblocks.test.rpc;

public class ClientTest {

	public static void main(String[] args) {
		RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 8088);
		TestInterface testInterface = proxy.getProxyObject(TestInterface.class);
		System.out.println(testInterface.say("andaicheng"));
	}
}
