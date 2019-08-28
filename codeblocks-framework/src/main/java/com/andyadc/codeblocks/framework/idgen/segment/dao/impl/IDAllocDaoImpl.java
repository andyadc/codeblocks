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
		SqlSession sqlSession = sqlSessionFactory.openSession(false);
		try {
			return sqlSession.selectList("com.andyadc.codeblocks.framework.idgen.segment.dao.IDAllocMapper.IDAllocMapper.getAllLeafAllocs");
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public IdAlloc updateMaxIdAndGetLeafAlloc(String tag) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			sqlSession.update("com.andyadc.codeblocks.framework.idgen.segment.dao.IDAllocMapper.IDAllocMapper.updateMaxId", tag);
			IdAlloc result = sqlSession.selectOne("com.andyadc.codeblocks.framework.idgen.segment.dao.IDAllocMapper.IDAllocMapper.getLeafAlloc", tag);
			sqlSession.commit();
			return result;
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public IdAlloc updateMaxIdByCustomStepAndGetIdAlloc(IdAlloc leafAlloc) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			sqlSession.update("com.andyadc.codeblocks.framework.idgen.segment.dao.IDAllocMapper.IDAllocMapper.updateMaxIdByCustomStep", leafAlloc);
			IdAlloc result = sqlSession.selectOne("com.andyadc.codeblocks.framework.idgen.segment.dao.IDAllocMapper.IDAllocMapper.getLeafAlloc", leafAlloc.getKey());
			sqlSession.commit();
			return result;
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public List<String> getAllTags() {
		SqlSession sqlSession = sqlSessionFactory.openSession(false);
		try {
			return sqlSession.selectList("com.andyadc.codeblocks.framework.idgen.segment.dao.IDAllocMapper.IDAllocMapper.getAllTags");
		} finally {
			sqlSession.close();
		}
	}
}
