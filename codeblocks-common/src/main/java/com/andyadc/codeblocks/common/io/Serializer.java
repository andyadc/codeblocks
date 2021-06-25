package com.andyadc.codeblocks.common.io;

import java.io.IOException;

/**
 * Serializer
 *
 * @param <S> the type to be serialized
 */
public interface Serializer<S> {
	byte[] serialize(S source) throws IOException;
}
