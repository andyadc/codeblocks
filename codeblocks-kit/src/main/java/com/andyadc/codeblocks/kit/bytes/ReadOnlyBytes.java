package com.andyadc.codeblocks.kit.bytes;

import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;

/**
 * The read-only version of {@link Bytes} created by calling {@link #readOnly()}.
 * <p>
 * Read-only Bytes does not have access to the internal byte array ({@link #array()}
 * will throw an exception). Transformers will always create a copy and keep the
 * read-only status.
 */
public final class ReadOnlyBytes extends Bytes {

	/**
	 * Creates a new read-only instance
	 *
	 * @param byteArray internal byte array
	 * @param byteOrder the internal byte order - this is used to interpret given array, not to change it
	 */
	ReadOnlyBytes(byte[] byteArray, ByteOrder byteOrder) {
		super(byteArray, byteOrder, new Factory());
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}

	@Override
	public byte[] array() {
		throw new ReadOnlyBufferException();
	}

	/**
	 * Factory creating mutable byte types
	 */
	private static class Factory implements BytesFactory {
		@Override
		public Bytes wrap(byte[] array, ByteOrder byteOrder) {
			return new ReadOnlyBytes(array, byteOrder);
		}
	}
}
