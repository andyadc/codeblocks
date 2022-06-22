package com.andyadc.abatis.binding;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class MapperProxy<T> implements InvocationHandler, Serializable {

	private static final long serialVersionUID = -1536518541998092229L;

	private final Class<T> mapperInterface;
	private final Map<String, String> sqlSession;

	public MapperProxy(Map<String, String> sqlSession, Class<T> mapperInterface) {
		this.sqlSession = sqlSession;
		this.mapperInterface = mapperInterface;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (Object.class.equals(method.getDeclaringClass())) {
			return method.invoke(this, args);
		} else {
			return "你的被代理了！" + sqlSession.get(mapperInterface.getName() + "." + method.getName());
		}
	}
}
