package com.andyadc.codeblocks.mybatis.pagination.dialect;

/**
 * @author andy.an
 * @since 2018/4/17
 */
public class DialectFactory {

    public static Dialect buildDialect(Dialect.Type dialectType) {
        switch (dialectType) {
            case MYSQL:
                return new MySQLDialect();
            default:
                throw new UnsupportedOperationException();
        }
    }
}
