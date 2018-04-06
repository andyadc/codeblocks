package com.andyadc.codeblocks.serialization.json;

import com.jsoniter.output.JsonStream;

/**
 * @author andaicheng
 * @since 2018/4/6
 */
public class JsoniterSerializer {

    public static <T> String toJson(T object) {
        return JsonStream.serialize(object);
    }
}
