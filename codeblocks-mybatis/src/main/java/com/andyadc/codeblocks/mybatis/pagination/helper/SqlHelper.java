package com.andyadc.codeblocks.mybatis.pagination.helper;

import com.andyadc.codeblocks.mybatis.pagination.dialect.Dialect;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author andy.an
 * @since 2018/4/18
 */
public class SqlHelper {

    private static final Logger logger = LoggerFactory.getLogger(SqlHelper.class);

    public static int getCount(final MappedStatement statement, final Connection connection, final Object parameterObject, Dialect dialect) throws SQLException {
        BoundSql boundSql = statement.getBoundSql(parameterObject);
        String countSql = dialect.getCountString(boundSql.getSql());

        logger.info("Total count SQL [{}]", countSql);
        logger.info("Total count Parameters: {} ", parameterObject);

        PreparedStatement ps = null;
        ResultSet rs;
        try {
            ps = connection.prepareStatement(countSql);
            DefaultParameterHandler handler = new DefaultParameterHandler(statement, parameterObject, boundSql);
            handler.setParameters(ps);
            rs = ps.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            logger.debug("Total count: {}", count);
            return count;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }
}
