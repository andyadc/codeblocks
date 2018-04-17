package com.andyadc.codeblocks.mybatis.pagination;

import com.andyadc.codeblocks.mybatis.pagination.dialect.Dialect;
import com.andyadc.codeblocks.mybatis.pagination.helper.DialectHelper;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Properties;

/**
 * @author andy.an
 * @since 2018/4/17
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {
                Connection.class, Integer.class
        })
})
public class PageInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageInterceptor.class);

    private Dialect dialect;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (dialect == null || !dialect.supportsPage()) {
            return invocation.proceed();
        }
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);

        RowBounds rowBounds = (RowBounds) metaObject.getValue("delegate.rowBounds");
//        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

        int offset = rowBounds.getOffset();
        int limit = rowBounds.getLimit();

        String originalSql = (String) metaObject.getValue("delegate.boundSql.sql");
        LOGGER.info("originalSql: {}", originalSql);

        String newSql = dialect.getPageString(originalSql, offset, limit);
        LOGGER.info("newSql: {}", newSql);
        metaObject.setValue("delegate.boundSql.sql", newSql);

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        String dialectClass = properties.getProperty("dialectClass");
        String dialectStr = properties.getProperty("dialect");
        LOGGER.info("dialectClass: {}, dialect: {}", dialectClass, dialectStr);
        if (dialectClass == null || "".equals(dialectClass.trim())) {
            Dialect.Type databaseType = null;
            try {
                databaseType = Dialect.Type.valueOf(dialectStr.toUpperCase());
            } catch (Exception e) {
                // ignore
            }

            if (databaseType == null) {
                throw new RuntimeException("The dialect of the attribute [" + dialectStr + "] value is invalid! \n" +
                        "Valid values for: " + Dialect.getDialectTypeValidValues());
            }
            dialect = DialectHelper.getDialect(databaseType);
        } else {
            try {
                Class<?> clazz = Class.forName(dialectClass);
                dialect = (Dialect) clazz.newInstance();
            } catch (Exception ex) {
                throw new RuntimeException("Cannot create dialect instance by dialectClass: " + dialectClass);
            }
        }

    }
}
