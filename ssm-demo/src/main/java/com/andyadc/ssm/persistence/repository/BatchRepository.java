package com.andyadc.ssm.persistence.repository;

import com.andyadc.ssm.persistence.entity.SimpleTableRecord;
import com.andyadc.ssm.persistence.mapper.SimpleTableRecordMapper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;
import org.mybatis.dynamic.sql.insert.render.BatchInsert;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.stereotype.Component;

import java.sql.JDBCType;
import java.util.List;

/**
 * https://mybatis.org/mybatis-dynamic-sql/docs/insert.html
 */
@Component
public class BatchRepository {

	private static final SqlTable table = SqlTable.of("user");
	private static final SqlColumn<Integer> id = table.column("id", JDBCType.INTEGER);
	private static final SqlColumn<String> username = table.column("username", JDBCType.VARCHAR);

	private final SqlSessionFactory sqlSessionFactory;
	private final SimpleTableRecordMapper simpleTableRecordMapper;

	public BatchRepository(SqlSessionFactory sqlSessionFactory, SimpleTableRecordMapper simpleTableRecordMapper) {
		this.sqlSessionFactory = sqlSessionFactory;
		this.simpleTableRecordMapper = simpleTableRecordMapper;
	}

	public void batchInsert(List<SimpleTableRecord> recordList) {
		try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH)) {

			BatchInsert<SimpleTableRecord> batchInsert = SqlBuilder.insertBatch(recordList)
				.into(table)
				.map(username).toProperty("username")
				.build()
				.render(RenderingStrategies.MYBATIS3);

			batchInsert.insertStatements().forEach(insertStatementProvider -> {
				simpleTableRecordMapper.insert(insertStatementProvider.getRow());
			});

			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
