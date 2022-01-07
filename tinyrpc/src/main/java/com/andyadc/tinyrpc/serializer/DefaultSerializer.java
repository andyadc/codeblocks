package com.andyadc.tinyrpc.serializer;

import com.andyadc.codeblocks.common.io.Deserializer;
import com.andyadc.codeblocks.common.io.Deserializers;
import com.andyadc.codeblocks.common.io.Serializers;

import java.io.IOException;

public class DefaultSerializer implements Serializer {

	private final Serializers serializers = new Serializers();
	private final Deserializers deserializers = new Deserializers();

	public DefaultSerializer() {
		serializers.loadSPI();
		deserializers.loadSPI();
	}

	@Override
	public byte[] serialize(Object source) throws IOException {
		Class<?> targetClass = source.getClass();
		com.andyadc.codeblocks.common.io.Serializer serializer
			= serializers.getMostCompatible(targetClass);
		return serializer.serialize(source);
	}

	@Override
	public Object deserialize(byte[] bytes, Class<?> targetClass) throws IOException {
		Deserializer<?> deserializer = deserializers.getMostCompatible(targetClass);
		return deserializer.deserialize(bytes);
	}
}
