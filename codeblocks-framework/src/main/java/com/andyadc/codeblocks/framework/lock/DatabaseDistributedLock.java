package com.andyadc.codeblocks.framework.lock;

import com.andyadc.codeblocks.common.function.ThrowableAction;
import com.andyadc.codeblocks.common.function.ThrowableSupplier;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class DatabaseDistributedLock implements Lock {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseDistributedLock.class);

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
			Thread currentThread = Thread.currentThread();
			StackTraceElement[] stackTraceElements = currentThread.getStackTrace();
			StackTraceElement sourceElement = stackTraceElements[stackTraceElements.length - 1];
			return sourceElement.getClassName() + "." + sourceElement.getMethodName();
		}
	};

	private final ThreadLocal<Long> lockIdHolder = new ThreadLocal<>();

	private final Object lock = new Object();

	private DataSource dataSource;

	public DatabaseDistributedLock() {
		initDataSource();
		initTables();
	}

	private void initDataSource() {

	}

	private void initTables() {
		if (isLocksTableAbsent()) {
			executeSQL(CREATE_LOCK_TABLE_DDL_SQL);
			executeSQL(ADD_UNIQUE_INDEX_DDL_SQL);
		}
	}

	private Connection getConnection() {
		return ThrowableSupplier.execute(() -> dataSource.getConnection());
	}

	private boolean isLocksTableAbsent() {
		return ThrowableSupplier.execute(() -> {
			try (Connection connection = getConnection();
				 ResultSet resultSet = getTables(connection)) {
				boolean present = false;
				while (resultSet.next()) {
					String tableName = resultSet.getString("TABLE_NAME").toLowerCase();
					if ("locks".equals(tableName)) {
						present = true;
						break;
					}
				}
				return !present;
			}
		});
	}

	private ResultSet getTables(Connection connection) throws SQLException {
		DatabaseMetaData databaseMetaData = connection.getMetaData();
		return databaseMetaData.getTables(null, null, null, new String[]{"TABLE"});
	}

	private void executeSQL(String sql) {
		ThrowableAction.execute(() -> {
			try (Connection connection = getConnection();
				 Statement statement = connection.createStatement()) {
				statement.execute(sql);
			}
		});
	}

	private String getResourceName() {
		return resourceNameHolder.get();
	}

	@Override
	public void lock() {
		String resourceName = getResourceName();
		if (!acquireLock(resourceName)) {
			// block
			block(resourceName);
		}
	}

	private void block(String resourceName) {
		// park 时间等待需要评估
		// 过长的话，非获取锁线程等待超出期望时间
		// 1s, T1(lock) 执行 20ms
		while (isLockHeld(resourceName)) {
			synchronized (this) {
				try {
					this.lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean isLockHeld(String resourceName) {
		boolean held = false;
		try (Connection connection = getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(HOLD_LOCK_DML_SQL)) {
			preparedStatement.setString(1, resourceName);
			ResultSet resultSet = preparedStatement.executeQuery();
			held = resultSet.next();
		} catch (Exception e) {
			logger.error("", e);
		}
		return held;
	}

	private boolean acquireLock(String resourceName) {
		// Reentrant
		Long id = lockIdHolder.get();
		if (id != null) {
			return true;
		}

		boolean acquired = false;
		try (Connection connection = getConnection();
			 Statement statement = connection.createStatement()) {
			String sql = StringUtils.replace(ADD_LOCK_DML_SQL, "?", "'" + resourceName + "'");
			statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet resultSet = statement.getGeneratedKeys();
			while (resultSet.next()) {
				id = resultSet.getLong(1);
				lockIdHolder.set(id);
				acquired = true;
				break;
			}
		} catch (Exception e) {
			String message = e.getMessage();
			if (message.contains("duplicate key")) {
				acquired = false;
				lockIdHolder.remove();
			}
			logger.error("", e);
		}
		return acquired;
	}

	private boolean releaseLock(String resourceName) {
		return ThrowableSupplier.execute(() -> {
			try (Connection connection = getConnection();
				 PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_LOCK_DML_SQL)) {
				preparedStatement.setString(1, resourceName);
				return preparedStatement.executeUpdate() > 0;
			}
		});
	}

	private void clearThreadLocals() {
		resourceNameHolder.remove();
		lockIdHolder.remove();
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {

	}

	@Override
	public boolean tryLock() {
		return false;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return false;
	}

	@Override
	public void unlock() {
		String resourceName = getResourceName();
		releaseLock(resourceName);
		clearThreadLocals();
	}

	@Override
	public Condition newCondition() {
		return null;
	}
}
