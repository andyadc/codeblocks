package com.andyadc.codeblocks.test.rpc;

@Rpc
public class TestInterfaceImpl implements TestInterface {
	@Override
	public String say(String message) {
		return "Hello " + message;
	}
}
