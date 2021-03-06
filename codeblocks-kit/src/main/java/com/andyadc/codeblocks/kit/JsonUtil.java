package com.andyadc.codeblocks.kit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

/**
 * json utility (via fastjson)
 *
 * @author andaicheng
 */
public final class JsonUtil {

    private static final SerializeConfig SERIALIZE_CONFIG;
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final ObjectSerializer SIMPLE_DATE_FORMAT_SERIALIZER =
            new SimpleDateFormatSerializer(DATE_FORMAT);

    private static final SerializerFeature[] SERIALIZER_FEATURE = {
            SerializerFeature.DisableCircularReferenceDetect,//关闭循环引用检查
            SerializerFeature.WriteMapNullValue,    // 输出空置字段
//            SerializerFeature.WriteNullStringAsEmpty,   //字符类型字段如果为null，输出为""，而不是null
//            SerializerFeature.WriteNullNumberAsZero,    //数值字段如果为null，输出为0，而不是null
//            SerializerFeature.WriteNullBooleanAsFalse,  //Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullListAsEmpty,  //list字段如果为null，输出为[]，而不是null
    };

    static {
        SERIALIZE_CONFIG = new SerializeConfig();
        SERIALIZE_CONFIG.put(java.util.Date.class, SIMPLE_DATE_FORMAT_SERIALIZER);
        SERIALIZE_CONFIG.put(java.sql.Date.class, SIMPLE_DATE_FORMAT_SERIALIZER);
    }

    private JsonUtil() {
    }

    public static String toJSONString(final Object o) {
        return JSON.toJSONString(o, SERIALIZE_CONFIG, SERIALIZER_FEATURE);
    }

    public static <T> T parseObject(final String json, final Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }
}
