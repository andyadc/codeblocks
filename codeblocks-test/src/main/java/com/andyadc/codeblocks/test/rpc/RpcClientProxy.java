package com.andyadc.codeblocks.test.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcClientProxy implements InvocationHandler {

	private final String host;
	private final Integer port;

	public RpcClientProxy(String host, Integer port) {
		this.host = host;
		this.port = port;
	}

	/**
	 * 创建接口的代理对象
	 */
	public <T> T getProxyObject(Class<T> clazz) {
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		RpcRequest request = new RpcRequest();
		request.setClassName(method.getDeclaringClass().getName());
		request.setMethodName(method.getName());
		request.setParamsType(method.getParameterTypes());
		request.setParams(args);
		RpcClient client = new RpcClient();
		return client.start(request, host, port);
	}
}
