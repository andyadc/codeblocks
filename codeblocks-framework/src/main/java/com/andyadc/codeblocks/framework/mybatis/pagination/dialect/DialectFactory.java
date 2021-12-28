package com.andyadc.codeblocks.framework.mybatis.pagination.dialect;

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
