package com.andyadc.codeblocks.serialization.binary;

import com.andyadc.codeblocks.serialization.SerializerException;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;

/**
 * andy.an
 * 2019/12/26
 */
public class KryoSerializer {

	private static final int BUFFER_SIZE = 2 * 1024;
	private static final int MAX_BUFFER_SIZE = 512 * 1024;

	public static <T> byte[] serialize(T object) {
		return serialize(object, BUFFER_SIZE, MAX_BUFFER_SIZE);
	}

	private static <T> byte[] serialize(T object, int bufferSize, int maxBufferSize) {
		if (object == null) {
			throw new SerializerException("Object is null");
		}

		KryoPool pool = KryoSerializerFactory.getDefaultPool();
		Kryo kryo = null;
		byte[] bytes;
		try (Output output = new Output(bufferSize, maxBufferSize)) {
			kryo = pool.borrow();
			kryo.writeClassAndObject(output, object);
			bytes = output.toBytes();
		} finally {
			if (kryo != null) {
				pool.release(kryo);
			}
		}
		return bytes;
	}

	public static <T> byte[] serialize(Kryo kryo, T object) {
		return serialize(kryo, object, BUFFER_SIZE, MAX_BUFFER_SIZE);
	}

	private static <T> byte[] serialize(Kryo kryo, T object, int bufferSize, int maxBufferSize) {
		if (object == null) {
			throw new SerializerException("Object is null");
		}

		byte[] bytes;
		try (Output output = new Output(bufferSize, maxBufferSize)) {
			kryo.writeClassAndObject(output, object);
			bytes = output.toBytes();
		}
		return bytes;
	}

	@SuppressWarnings({"unchecked"})
	public static <T> T deserialize(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			throw new SerializerException("Bytes is null or empty");
		}

		KryoPool pool = KryoSerializerFactory.getDefaultPool();
		Kryo kryo = null;
		Object object;
		try (Input input = new Input(bytes)) {
			kryo = pool.borrow();
			object = kryo.readClassAndObject(input);
		} finally {
			if (kryo != null) {
				pool.release(kryo);
			}
		}
		return (T) object;
	}

	@SuppressWarnings({"unchecked"})
	public static <T> T deserialize(Kryo kryo, byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			throw new SerializerException("Bytes is null or empty");
		}

		Object object;
		try (Input input = new Input(bytes)) {
			object = kryo.readClassAndObject(input);
		}
		return (T) object;
	}
}
