package com.andyadc.codeblocks.serialization.json;

import com.alibaba.fastjson2.JSON;

public class FastjsonSerializer {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    private FastjsonSerializer() {
    }

    public static <T> String toJSON(T o) {
		return JSON.toJSONString(o);
    }

    public static <T> T fromJSON(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }
}
