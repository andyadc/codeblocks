package com.andyadc.codeblocks.framework.idgen.segment.dao.impl;

import com.andyadc.codeblocks.framework.idgen.segment.dao.IDAllocDao;
import com.andyadc.codeblocks.framework.idgen.segment.dao.IDAllocMapper;
import com.andyadc.codeblocks.framework.idgen.segment.model.IdAlloc;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.util.List;

public class IDAllocDaoImpl implements IDAllocDao {

	SqlSessionFactory sqlSessionFactory;

	public IDAllocDaoImpl(DataSource dataSource) {
		TransactionFactory transactionFactory = new JdbcTransactionFactory();
		Environment environment = new Environment("development", transactionFactory, dataSource);
		Configuration configuration = new Configuration(environment);
		configuration.addMapper(IDAllocMapper.class);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
	}

	@Override
	public List<IdAlloc> getAllIdAllocs() {
		try (SqlSession sqlSession = sqlSessionFactory.openSession(false)) {
			return sqlSession.selectList("com.andyadc.codeblocks.framework.idgen.segment.dao.IDAllocMapper.getAllIdAllocs");
		}
	}

	@Override
	public IdAlloc updateMaxIdAndGetIdAlloc(String tag) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			sqlSession.update("com.andyadc.codeblocks.framework.idgen.segment.dao.IDAllocMapper.updateMaxId", tag);
			IdAlloc result = sqlSession.selectOne("com.andyadc.codeblocks.framework.idgen.segment.dao.IDAllocMapper.getIdAlloc", tag);
			sqlSession.commit();
			return result;
		}
	}

	@Override
	public IdAlloc updateMaxIdByCustomStepAndGetIdAlloc(IdAlloc idAlloc) {
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			sqlSession.update("com.andyadc.codeblocks.framework.idgen.segment.dao.IDAllocMapper.updateMaxIdByCustomStep", idAlloc);
			IdAlloc result = sqlSession.selectOne("com.andyadc.codeblocks.framework.idgen.segment.dao.IDAllocMapper.getIdAlloc", idAlloc.getKey());
			sqlSession.commit();
			return result;
		}
	}

	@Override
	public List<String> getAllTags() {
		try (SqlSession sqlSession = sqlSessionFactory.openSession(false)) {
			return sqlSession.selectList("com.andyadc.codeblocks.framework.idgen.segment.dao.IDAllocMapper.getAllTags");
		}
	}
}
