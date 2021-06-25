package com.andyadc.codeblocks.common.io;

import java.io.IOException;

/**
 * Deserializer
 *
 * @param <T> the type to be deserialized
 */
public interface Deserializer<T> {
	T deserialize(byte[] bytes) throws IOException;
}
