package com.andyadc.tinyrpc.serializer;

import java.io.IOException;

public class DefaultSerializer implements Serializer {

	@Override
	public byte[] serialize(Object source) throws IOException {
		return new byte[0];
	}

	@Override
	public Object deserialize(byte[] bytes, Class<?> targetClass) throws IOException {
		return null;
	}
}
