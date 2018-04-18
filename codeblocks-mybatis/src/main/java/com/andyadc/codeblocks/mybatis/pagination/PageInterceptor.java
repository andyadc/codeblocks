package com.andyadc.codeblocks.mybatis.pagination;

import com.andyadc.codeblocks.mybatis.pagination.dialect.Dialect;
import com.andyadc.codeblocks.mybatis.pagination.helper.DialectHelper;
import com.andyadc.codeblocks.mybatis.pagination.helper.SqlHelper;
import com.andyadc.codeblocks.mybatis.util.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
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

    private static final ThreadLocal<Integer> PAGINATION_TOTAL = ThreadLocal.withInitial(() -> 0);

    private Dialect dialect;

    public static int getPaginationTotal() {
        int count = PAGINATION_TOTAL.get();
        clean();
        return count;
    }

    private static void clean() {
        PAGINATION_TOTAL.remove();
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (dialect == null || !dialect.supportsPage()) {
            return invocation.proceed();
        }
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);

        RowBounds rowBounds = (RowBounds) metaObject.getValue("delegate.rowBounds");
        PageBounds pageBounds = new PageBounds(rowBounds);

        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

//        int offset = rowBounds.getOffset();
//        int limit = rowBounds.getLimit();

        BoundSql boundSql = statementHandler.getBoundSql();
        Connection connection = (Connection) invocation.getArgs()[0];
        int count = SqlHelper.getCount(mappedStatement, connection, boundSql.getParameterObject(), dialect);
        PAGINATION_TOTAL.set(count);

        String originalSql = (String) metaObject.getValue("delegate.boundSql.sql");
        LOGGER.info("originalSql: {}", originalSql);

        String newSql = dialect.getPageString(originalSql, pageBounds);
        LOGGER.info("newSql: {}", newSql);
        metaObject.setValue("delegate.boundSql.sql", newSql);
        metaObject.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
        metaObject.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);

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
        if (StringUtils.isBlank(dialectClass)) {
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
