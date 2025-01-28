package com.andyadc.summer.jdbc;

import com.andyadc.summer.exception.DataAccessException;
import com.andyadc.summer.jdbc.tx.TransactionalUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcTemplate {

	final DataSource dataSource;

	public JdbcTemplate(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public int update(String sql, Object... args) throws DataAccessException {
		return execute(preparedStatementCreator(sql, args),
			// PreparedStatementCallback
			(PreparedStatement ps) -> {
				return ps.executeUpdate();
			}
		);
	}

	public <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action) {
		return execute((Connection con) -> {
			try (PreparedStatement ps = psc.createPreparedStatement(con)) {
				return action.doInPreparedStatement(ps);
			}
		});
	}

	public <T> T execute(ConnectionCallback<T> action) throws DataAccessException {
		// 尝试获取当前事务连接:
		Connection current = TransactionalUtils.getCurrentConnection();
		if (current != null) {
			try {
				return action.doInConnection(current);
			} catch (SQLException e) {
				throw new DataAccessException(e);
			}
		}

		// 获取新连接:
		try (Connection newConn = dataSource.getConnection()) {
			final boolean autoCommit = newConn.getAutoCommit();
			if (!autoCommit) {
				newConn.setAutoCommit(true);
			}
			T result = action.doInConnection(newConn);
			if (!autoCommit) {
				newConn.setAutoCommit(false);
			}
			return result;
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	private PreparedStatementCreator preparedStatementCreator(String sql, Object... args) {
		return (Connection con) -> {
			PreparedStatement ps = con.prepareStatement(sql);
			bindArgs(ps, args);
			return ps;
		};
	}

	private void bindArgs(PreparedStatement ps, Object... args) throws SQLException {
		for (int i = 0; i < args.length; i++) {
			ps.setObject(i + 1, args[i]);
		}
	}

}
