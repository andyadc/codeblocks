package com.andyadc.codeblocks.serialization;

/**
 * andy.an
 */
public interface Serializer {
	<T> byte[] serialize(T object);
	<T> T deserialize(byte[] bytes, Class<T> clazz);
}
