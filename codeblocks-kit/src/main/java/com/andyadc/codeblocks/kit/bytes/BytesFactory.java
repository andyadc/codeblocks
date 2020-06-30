package com.andyadc.codeblocks.kit.bytes;

import java.nio.ByteOrder;

/**
 * Simple factory for creating {@link Bytes} instances
 */
public interface BytesFactory {

	/**
	 * Create an instance with given array and order
	 *
	 * @param array     to directly us
	 * @param byteOrder the array is in
	 * @return a new instance
	 */
	Bytes wrap(byte[] array, ByteOrder byteOrder);
}
