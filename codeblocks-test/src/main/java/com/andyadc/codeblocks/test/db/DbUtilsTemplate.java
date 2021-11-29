package com.andyadc.codeblocks.test.db;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * https://programmerall.com/article/3639526658/
 */
public class DbUtilsTemplate {

	private static final Logger logger = LoggerFactory.getLogger(DbUtilsTemplate.class);

	private DataSource dataSource;
	private QueryRunner queryRunner;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void query(String sql) {
		queryRunner = new QueryRunner();
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			queryRunner.query(connection, sql, new ResultSetHandler<Object>() {
				@Override
				public Object handle(ResultSet resultSet) throws SQLException {
					ResultSetMetaData metaData = resultSet.getMetaData();
					int count = metaData.getColumnCount();
					for (int i = 1; i <= count; i++) {
						System.out.println("ColumnName: " + metaData.getColumnName(i));
						System.out.println("CatalogName: " + metaData.getCatalogName(i));
						System.out.println("ColumnClassName: " + metaData.getColumnClassName(i));
						System.out.println("ColumnTypeName: " + metaData.getColumnTypeName(i));
						System.out.println("SchemaName: " + metaData.getSchemaName(i));
						System.out.println("TableName: " + metaData.getTableName(i));
						System.out.println("\r\n");
					}
					return null;
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Execute sql statement
	 *
	 * @param sql    sql statement
	 * @param params parameter array
	 * @return Number of rows affected
	 */
	public int update(String sql, Object... params) {
		queryRunner = new QueryRunner();
//		queryRunner = new QueryRunner(dataSource);
		Connection connection = null;
		int affectedRows;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			if (params == null) {
				affectedRows = queryRunner.update(connection, sql);
			} else {
				affectedRows = queryRunner.update(connection, sql, params);
			}
			DbUtils.commitAndCloseQuietly(connection);
		} catch (Exception e) {
			logger.error("Error occured while attempting to update data", e);
			DbUtils.rollbackAndCloseQuietly(connection);
			throw new RuntimeException(e);
		}
		return affectedRows;
	}
}
