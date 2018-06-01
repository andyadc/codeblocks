package com.andyadc.codeblocks.mybatis.pagination.helper;

import com.andyadc.codeblocks.mybatis.pagination.dialect.Dialect;
import com.andyadc.codeblocks.mybatis.pagination.dialect.DialectFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author andy.an
 * @since 2018/4/17
 */
public class DialectHelper {

    private static Map<Dialect.Type, Dialect> DIALECTS = new ConcurrentHashMap<>(8);

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
