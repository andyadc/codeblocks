package com.andyadc.codeblocks.mybatis.pagination.dialect;

/**
 * @author andy.an
 * @since 2018/4/16
 */
public class MySQLDialect extends Dialect {

    @Override
    public String getPageString(final String sql, final int offset, final int limit) {
        StringBuilder builder = new StringBuilder(sql.length() + 20).append(getLineSql(sql));
        builder.append(" LIMIT ");
        if (offset > 0) {
            builder.append(Integer.toString(offset)).append(", ").append(Integer.toString(limit));
        } else {
            builder.append(Integer.toString(limit));
        }

        return builder.toString();
    }

    @Override
    public boolean supportsPage() {
        return true;
    }
}
