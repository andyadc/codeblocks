package com.andyadc.abatis.session.defaults;

import com.andyadc.abatis.session.SqlSession;

public class DefaultSqlSession implements SqlSession {

	@Override
	public <T> T selectOne(String statement) {
		return null;
	}

	@Override
	public <T> T selectOne(String statement, Object parameter) {
		return null;
	}

	@Override
	public <T> T getMapper(Class<T> type) {
		return null;
	}
}
