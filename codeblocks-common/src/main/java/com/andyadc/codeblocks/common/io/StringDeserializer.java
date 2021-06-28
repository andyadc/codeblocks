package com.andyadc.codeblocks.common.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * String {@link Deserializer}
 */
public class StringDeserializer implements Deserializer<String> {

	@Override
	public String deserialize(byte[] bytes) throws IOException {
		return new String(bytes, StandardCharsets.UTF_8);
	}
}
