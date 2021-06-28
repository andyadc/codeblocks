package com.andyadc.codeblocks.common.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Default Serializer implementation based on Java Standard Serialization.
 */
public class DefaultSerializer implements Serializer<Object> {

	@Override
	public byte[] serialize(Object source) throws IOException {
		byte[] bytes;
		try (
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)
		) {
			// Key -> byte[]
			objectOutputStream.writeObject(source);
			bytes = outputStream.toByteArray();
		}
		return bytes;
	}
}
