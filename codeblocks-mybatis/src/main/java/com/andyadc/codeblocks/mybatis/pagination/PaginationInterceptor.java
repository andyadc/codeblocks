package com.andyadc.codeblocks.mybatis.pagination;

import com.andyadc.codeblocks.mybatis.pagination.dialect.Dialect;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Properties;

/**
 * 拦截 Executor.query 方法实现分页方言的查询
 *
 * @author andaicheng
 * @since 2018/4/10
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {
                        MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class
                })
        }
)
public class PaginationInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaginationInterceptor.class);

    private static final int MAPPED_STATEMENT_INDEX = 0;
    private static final int PARAMETER_INDEX = 1;
    private static final int ROWBOUNDS_INDEX = 2;

    private String dialectClass;
    private String dbType;

    @Override
    public Object intercept(Invocation invocation) throws Exception {
        final Object[] queryArgs = invocation.getArgs();
        RowBounds rowBounds = (RowBounds) queryArgs[ROWBOUNDS_INDEX];
        if (!(rowBounds instanceof PageBounds)) {
            //return invocation.proceed();
        }

        PageBounds pageBounds = new PageBounds(rowBounds);
        if (pageBounds.getOffset() == RowBounds.NO_ROW_OFFSET
                && pageBounds.getLimit() == RowBounds.NO_ROW_LIMIT) {
            //return invocation.proceed();
        }

        final MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
        final Object parameter = queryArgs[PARAMETER_INDEX];

        final Dialect dialect;
        try {
            Class<?> clazz = Class.forName(dialectClass);
            Constructor constructor = clazz.getConstructor(MappedStatement.class, Object.class, PageBounds.class);
            dialect = (Dialect) constructor.newInstance(new Object[]{ms, parameter, pageBounds});
            if (!dialect.supportsPage()) {
                return invocation.proceed();
            }
        } catch (ClassNotFoundException ex) {
            throw new ClassNotFoundException("Cannot create dialect instance: " + dialectClass, ex);
        } catch (Exception e) {
            throw new RuntimeException("instance " + dialectClass + " handled error!", e);
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        String dialectClass = properties.getProperty("dialectClass");
        LOGGER.info("dialectClass:{}", dialectClass);
        setDialectClass(dialectClass);

        String dbType = properties.getProperty("dbType");
        LOGGER.info("dbType:{}", dbType);
        setDbType(dialectClass);
    }

    public void setDialectClass(String dialectClass) {
        this.dialectClass = dialectClass;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
}
