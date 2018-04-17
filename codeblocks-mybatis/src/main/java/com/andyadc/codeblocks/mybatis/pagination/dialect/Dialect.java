package com.andyadc.codeblocks.mybatis.pagination.dialect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author andaicheng
 * @since 2018/4/10
 */
public abstract class Dialect {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dialect.class);

    public static void main(String[] args) {
        System.out.println(getDialectTypeValidValues());
    }

    protected static String getLineSQL(String sql) {
        return sql.replaceAll("[\r\n]", " ").replaceAll("\\s{2,}", " ");
    }

    public static String getDialectTypeValidValues() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, j = Dialect.Type.values().length; i < j; i++) {
            sb.append(Dialect.Type.values()[i].name());
            if (i != j - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    protected String getCountString(String sql) {
        return "SELECT COUNT(1) FROM ( " + sql + " ) tmp";
    }

    public abstract String getPageString(final String sql, final int offset, final int limit);

    public abstract boolean supportsPage();

    public enum Type {
        MYSQL,
        ORACLE,
        MSSQL
    }
}
