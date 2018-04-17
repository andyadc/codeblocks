package com.andyadc.codeblocks.mybatis.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;

/**
 * @author andy.an
 * @since 2018/4/16
 */
public class MyBatisTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyBatisTest.class);

    private static final String CONFIG_RESOURCE = "mybatis-config.xml";
    private static final String NAMESPACE = "com.andyadc.codeblocks.mybatis.mapper";

    private SqlSession session;
    private SqlSessionFactory factory;

    @Before
    public void build() throws Exception {
        InputStream inputStream = Resources.getResourceAsStream(CONFIG_RESOURCE);
        factory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void query() {
        try {
            session = factory.openSession();
            System.out.println(session);

            List list = session.selectList(NAMESPACE + ".selectBankMapping", null, new RowBounds(7, 13));
            LOGGER.info("query total size: {}", list.size());
            for (Object o : list) {
                System.out.println(o);
            }
        } finally {
            session.close();
        }
    }

    @Test
    public void querySelective() {
        try {
            session = factory.openSession();
            System.out.println(session);

            BankMapping mapping = new BankMapping();
            mapping.setId(1L);
            List<BankMapping> list = session.selectList(NAMESPACE + ".selectBankMappingSelective", mapping, new RowBounds(2, 3));

            LOGGER.info("{}", (list == null || list.size() < 1) ? null : list.get(0));
        } finally {
            session.close();
        }
    }
}
