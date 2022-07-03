package com.andyadc.abatis.session.defaults;

import com.andyadc.abatis.binding.MapperRegistry;
import com.andyadc.abatis.session.SqlSession;
import com.andyadc.abatis.session.SqlSessionFactory;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

	private final MapperRegistry mapperRegistry;

	public DefaultSqlSessionFactory(MapperRegistry mapperRegistry) {
		this.mapperRegistry = mapperRegistry;
	}

	@Override
	public SqlSession openSession() {
		return new DefaultSqlSession(mapperRegistry);
	}
}
