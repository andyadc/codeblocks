package com.andyadc.codeblocks.framework.lock;

import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class DatabaseDistributedLock implements Lock {

	private static final String CREATE_LOCK_TABLE_DDL_SQL = "CREATE TABLE locks(\n" +
		"id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n" +
		"resource_name VARCHAR(512) NOT NULL)";

	private static final String ADD_UNIQUE_INDEX_DDL_SQL = "CREATE UNIQUE INDEX unique_resource_name on locks(resource_name)";

	private static final String ADD_LOCK_DML_SQL = "INSERT INTO locks(resource_name) VALUES (?)";

	private static final String HOLD_LOCK_DML_SQL = "SELECT id FROM locks WHERE resource_name = ?";

	private static final String REMOVE_LOCK_DML_SQL = "DELETE FROM locks WHERE resource_name = ?";

	private static final Long DUPLICATED_LOCK_ID = Long.MIN_VALUE;

	private final ThreadLocal<String> resourceNameHolder = new ThreadLocal<String>() {
		@Override
		protected String initialValue() {
			return super.initialValue();
		}
	};

	private final Object lock = new Object();

	private DataSource dataSource;

	public DatabaseDistributedLock() {
		initDataSource();
		initTables();
	}

	private void initDataSource() {

	}

	private void initTables() {

	}

	private ResultSet getTables(Connection connection) throws SQLException {
		DatabaseMetaData databaseMetaData = connection.getMetaData();
		return databaseMetaData.getTables(null, null, null, new String[]{"TABLE"});
	}

	private String getResourceName() {
		return resourceNameHolder.get();
	}

	@Override
	public void lock() {

	}

	@Override
	public void lockInterruptibly() throws InterruptedException {

	}

	@Override
	public boolean tryLock() {
		return false;
	}

	@Override
	public boolean tryLock(long time, @NotNull TimeUnit unit) throws InterruptedException {
		return false;
	}

	@Override
	public void unlock() {

	}

	@NotNull
	@Override
	public Condition newCondition() {
		return null;
	}
}
