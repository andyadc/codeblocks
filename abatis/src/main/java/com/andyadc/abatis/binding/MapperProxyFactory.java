package com.andyadc.abatis.binding;

import java.lang.reflect.Proxy;
import java.util.Map;

public class MapperProxyFactory<T> {

	private final Class<T> mapperInterface;

	public MapperProxyFactory(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	@SuppressWarnings({"unchecked"})
	public T newInstance(Map<String, String> sqlSession) {
		final MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, mapperInterface);
		return (T) Proxy.newProxyInstance(
			mapperProxy.getClass().getClassLoader(),
			new Class[]{mapperInterface},
			mapperProxy
		);
	}
}
