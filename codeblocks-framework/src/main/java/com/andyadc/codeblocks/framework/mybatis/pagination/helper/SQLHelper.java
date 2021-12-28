package com.andyadc.codeblocks.framework.mybatis.pagination.helper;

import com.andyadc.codeblocks.framework.mybatis.pagination.dialect.Dialect;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLHelper {

	private static final Logger logger = LoggerFactory.getLogger(SQLHelper.class);

	public static int getCount(final MappedStatement statement, final Connection connection, final Object parameterObject, Dialect dialect) throws SQLException {
		BoundSql boundSql = statement.getBoundSql(parameterObject);
		String countSql = dialect.getCountString(boundSql.getSql());

		if (logger.isDebugEnabled()) {
			logger.debug("Total count SQL [{}]", countSql);
			logger.debug("Total count Parameters: {} ", parameterObject);
		}

		ResultSet rs;
		try (PreparedStatement ps = connection.prepareStatement(countSql)) {
			DefaultParameterHandler handler = new DefaultParameterHandler(statement, parameterObject, boundSql);
			handler.setParameters(ps);
			rs = ps.executeQuery();
			int count = 0;
			if (rs.next()) {
				count = rs.getInt(1);
			}
			logger.debug("Total count: {}", count);
			return count;
		}
	}
}
