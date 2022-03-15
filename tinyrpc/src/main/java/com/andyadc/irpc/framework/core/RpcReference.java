package com.andyadc.irpc.framework.core;

import com.andyadc.irpc.framework.core.proxy.ProxyFactory;

public class RpcReference {

	public ProxyFactory proxyFactory;

	public RpcReference(ProxyFactory proxyFactory) {
		this.proxyFactory = proxyFactory;
	}

	/**
	 * 根据接口类型获取代理对象
	 */
	public <T> T get(Class<T> clazz) throws Throwable {
		return proxyFactory.getProxy(clazz);
	}
}
