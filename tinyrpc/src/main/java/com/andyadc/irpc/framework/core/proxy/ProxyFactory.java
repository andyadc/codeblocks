package com.andyadc.irpc.framework.core.proxy;

public interface ProxyFactory {
	<T> T getProxy(Class<T> clazz) throws Throwable;
}
