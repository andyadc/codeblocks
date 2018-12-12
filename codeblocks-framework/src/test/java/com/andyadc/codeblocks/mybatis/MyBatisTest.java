package com.andyadc.codeblocks.mybatis;

import com.andyadc.codeblocks.framework.mybatis.pagination.Order;
import com.andyadc.codeblocks.framework.mybatis.pagination.PageBounds;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author andy.an
 * @since 2018/4/16
 */
public class MyBatisTest {

    private static final Logger logger = LoggerFactory.getLogger(MyBatisTest.class);

    private static final String CONFIG_RESOURCE = "mybatis-config.xml";
    private static final String NAMESPACE = "com.andyadc.codeblocks.mybatis.test.BankMapper";

    private SqlSession session;
    private SqlSessionFactory factory;

    @Before
    public void build() throws Exception {
        InputStream inputStream = Resources.getResourceAsStream(CONFIG_RESOURCE);
        factory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void update() {
        try {
            session = factory.openSession();
            System.out.println(session);

            BankMapping bankMapping = new BankMapping();
            bankMapping.setBankCode("test");
            bankMapping.setBankName("测试123");
            bankMapping.setCardType(2);
            bankMapping.setId(231L);

            int result = session.update(NAMESPACE + ".updateByPrimaryKeySelective", bankMapping);
            session.commit(true);
            System.out.println(result);
        } finally {
            session.close();
        }
    }

    @Test
    public void insert() {
        try {
            session = factory.openSession();
            System.out.println(session);

            BankMapping bankMapping = new BankMapping();
            bankMapping.setBankCode("test");
            bankMapping.setBankName("测试");
            bankMapping.setCardType(2);

            int result = session.insert(NAMESPACE + ".insertSelective", bankMapping);
            session.commit();
            System.out.println(result);
            System.out.println(bankMapping.getId());
        } finally {
            session.close();
        }
    }

    @Test
    public void query() {
        try {
            session = factory.openSession();
            System.out.println(session);

            List list = session.selectList(NAMESPACE + ".selectBankMapping", null, new PageBounds(4, 10));
            logger.info("query total size: {}", list.size());
            for (Object o : list) {
                System.out.println(o);
            }
        } finally {
            session.close();
        }
    }

    @Test
    public void queryByMapper() {
        try {
            session = factory.openSession();
            System.out.println(session);

            BankMapper mapper = session.getMapper(BankMapper.class);
            System.out.println(mapper.selectBankMapping());
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
//            mapping.setId(1L);
            mapping.setCardType(1);

            PageBounds pageBounds = new PageBounds();
            pageBounds.setPage(4);
            pageBounds.setLimit(10);
            Order order = Order.create("id", "desc");
            pageBounds.setOrders(Arrays.asList(order));

            List<BankMapping> list = session.selectList(NAMESPACE + ".selectBankMappingSelective", mapping, pageBounds);
            for (BankMapping bankMapping : list) {
                System.out.println("--> " + bankMapping);
            }
        } finally {
            session.close();
        }
    }
}
