package com.andyadc.codeblocks.framework.mybatis.pagination.helper;

import com.andyadc.codeblocks.framework.mybatis.pagination.dialect.Dialect;
import com.andyadc.codeblocks.framework.mybatis.pagination.dialect.DialectFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DialectHelper {

    private static final Map<Dialect.Type, Dialect> DIALECTS = new ConcurrentHashMap<>(8);

    public static Dialect getDialect(Dialect.Type dialectType) {
        if (DIALECTS.containsKey(dialectType)) {
            return DIALECTS.get(dialectType);
        } else {
            Dialect dialect = DialectFactory.buildDialect(dialectType);
            DIALECTS.put(dialectType, dialect);
            return dialect;
        }
    }
}
