package com.andyadc.abatis.binding;

import com.andyadc.abatis.session.SqlSession;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MapperProxy<T> implements InvocationHandler, Serializable {

	private static final long serialVersionUID = -1536518541998092229L;

	private final SqlSession sqlSession;
	private final Class<T> mapperInterface;

	public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface) {
		this.sqlSession = sqlSession;
		this.mapperInterface = mapperInterface;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (Object.class.equals(method.getDeclaringClass())) {
			return method.invoke(this, args);
		} else {

			return method.invoke(sqlSession, args);
		}
	}
}
