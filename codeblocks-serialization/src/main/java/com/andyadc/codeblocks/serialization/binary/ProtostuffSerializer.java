package com.andyadc.codeblocks.serialization.binary;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class ProtostuffSerializer {

    private static final Objenesis OBJENESIS = new ObjenesisStd(true);
    private static final ConcurrentHashMap<Class<?>, Schema<?>> SCHEMA_MAP = new ConcurrentHashMap<>();
    private static final ThreadLocal<LinkedBuffer> BUFFERS = ThreadLocal.withInitial(LinkedBuffer::allocate);

    private ProtostuffSerializer() {
    }

    public static <T> byte[] serialize(T object) {
        Schema<T> schema = getSchema((Class<T>) object.getClass());

        LinkedBuffer buffer = BUFFERS.get();
        try {
            return ProtostuffIOUtil.toByteArray(object, schema, buffer);
        } finally {
            buffer.clear();
        }
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        T object = OBJENESIS.newInstance(clazz);
		Schema<T> schema = getSchema(clazz);
        ProtostuffIOUtil.mergeFrom(bytes, object, schema);
        return object;
    }

    private static <T> Schema<T> getSchema(Class<T> clazz) {
        Schema<T> schema = (Schema<T>) SCHEMA_MAP.get(clazz);
        if (schema == null) {
            Schema<T> newSchema = RuntimeSchema.createFrom(clazz);
            schema = (Schema<T>) SCHEMA_MAP.putIfAbsent(clazz, newSchema);
            if (schema == null) {
                schema = newSchema;
            }
        }
        return schema;
    }
}
