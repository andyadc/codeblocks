package com.andyadc.codeblocks.framework.mybatis.pagination.dialect;

public class MySQLDialect extends Dialect {

    @Override
    public String getPageString(final String sql, final int offset, final int limit) {
        StringBuilder builder = new StringBuilder(sql.length() + 20).append(getLineSql(sql));
        builder.append(" LIMIT ");
        if (offset > 0) {
			builder.append(offset).append(", ").append(limit);
        } else {
			builder.append(limit);
        }

        return builder.toString();
    }

    @Override
    public boolean supportsPage() {
        return true;
    }
}
