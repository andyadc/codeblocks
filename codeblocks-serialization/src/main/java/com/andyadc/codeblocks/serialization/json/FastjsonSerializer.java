package com.andyadc.codeblocks.serialization.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

public class FastjsonSerializer {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final SerializeConfig SERIALIZE_CONFIG;

    private static final SerializerFeature[] SERIALIZER_FEATURE = {
            SerializerFeature.WriteMapNullValue,    // 输出空置字段
//            SerializerFeature.WriteNullStringAsEmpty,   //字符类型字段如果为null，输出为""，而不是null
//            SerializerFeature.WriteNullNumberAsZero,    //数值字段如果为null，输出为0，而不是null
//            SerializerFeature.WriteNullBooleanAsFalse,  //Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullListAsEmpty,  //list字段如果为null，输出为[]，而不是null
            SerializerFeature.DisableCircularReferenceDetect //关闭循环引用检查
    };

    static {
        SERIALIZE_CONFIG = new SerializeConfig();
        SERIALIZE_CONFIG.put(java.util.Date.class, new SimpleDateFormatSerializer(DATE_FORMAT));
        SERIALIZE_CONFIG.put(java.sql.Date.class, new SimpleDateFormatSerializer(DATE_FORMAT));
    }

    private FastjsonSerializer() {
    }

    public static <T> String toJSON(T o) {
        return JSON.toJSONString(o, SERIALIZE_CONFIG, SERIALIZER_FEATURE);
    }

    public static <T> T fromJSON(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }
}
