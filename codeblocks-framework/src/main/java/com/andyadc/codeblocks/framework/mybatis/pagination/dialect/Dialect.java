package com.andyadc.codeblocks.framework.mybatis.pagination.dialect;

import com.andyadc.codeblocks.framework.mybatis.pagination.Order;
import com.andyadc.codeblocks.framework.mybatis.pagination.PageBounds;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Dialect {

    private static final Pattern ORDER_BY_SPLIT_PATTERN = Pattern.compile("(.*)ORDER\\s+BY(.*)", Pattern.CASE_INSENSITIVE);

    protected static String getLineSql(String sql) {
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

    /**
     * 把SQL语句基于Order By进行切分
     *
     * @param sql 原始SQL语句
     * @return 如果包含order by语句则返回2个元素数组，0元素为order by前部分，1元素为后部分；否则直接返回1个元素的原始SQL的数组
     */
    public static String[] splitOrderBy(String sql) {
		Matcher matcher = ORDER_BY_SPLIT_PATTERN.matcher(sql);
        if (matcher.find()) {
            return new String[]{matcher.group(1), matcher.group(2)};
        } else {
            return new String[]{sql};
        }
    }

    public String getCountString(String sql) {
        return "SELECT COUNT(1) FROM ( " + sql + " ) tmp";
    }

    public String getSortString(String sql, List<Order> orders) {
        if (orders == null || orders.size() < 1) {
            return sql;
        }
        sql = splitOrderBy(sql)[0];
        StringBuilder builder = new StringBuilder(sql).append(" ORDER BY ");
        for (Order order : orders) {
            builder.append(order.toString());
        }
        return builder.toString();
    }

    public String getPageString(String sql, PageBounds pageBounds) {
        sql = getSortString(sql, pageBounds.getOrders());
        return getPageString(sql, pageBounds.startIndex(), pageBounds.getLimit());
    }

    public enum Type {
        MYSQL
    }

    public abstract String getPageString(final String sql, final int offset, final int limit);

    public abstract boolean supportsPage();
}
