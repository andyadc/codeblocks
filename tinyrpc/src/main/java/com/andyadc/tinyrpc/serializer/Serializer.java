package com.andyadc.tinyrpc.serializer;

import java.io.IOException;

import static com.andyadc.tinyrpc.util.ServiceLoaders.loadDefault;

public interface Serializer {

	Serializer DEFAULT = loadDefault(Serializer.class);

	byte[] serialize(Object source) throws IOException;

	Object deserialize(byte[] bytes, Class<?> targetClass) throws IOException;
}
