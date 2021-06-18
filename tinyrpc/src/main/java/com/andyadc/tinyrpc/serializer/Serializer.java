package com.andyadc.tinyrpc.serializer;

import java.io.IOException;

public interface Serializer {

	byte[] serialize(Object source) throws IOException;

	Object deserialize(byte[] bytes, Class<?> targetClass) throws IOException;
}
