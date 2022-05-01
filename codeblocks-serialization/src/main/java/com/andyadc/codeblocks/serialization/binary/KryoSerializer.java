package com.andyadc.codeblocks.serialization.binary;

import com.andyadc.codeblocks.serialization.SerializerException;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;

public class KryoSerializer {

	public static <T> byte[] serialize(T object) {
		if (object == null) {
			throw new SerializerException("Object is null.");
		}

		Pool<Kryo> kryoPool = KryoSerializerFactory.getKryoPool();
		Kryo kryo = kryoPool.obtain();
		Pool<Output> outputPool = KryoSerializerFactory.getOutputPool();
		Output output = outputPool.obtain();
		kryo.writeObject(output, object);
		byte[] bytes = output.toBytes();

		outputPool.free(output);
		kryoPool.free(kryo);
		return bytes;
	}

	public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
		if (bytes == null || bytes.length == 0) {
			throw new SerializerException("Bytes is null or empty.");
		}
		if (clazz == null) {
			throw new SerializerException("Target bean is null.");
		}

		Pool<Kryo> kryoPool = KryoSerializerFactory.getKryoPool();
		Kryo kryo = kryoPool.obtain();

		Pool<Input> inputPool = KryoSerializerFactory.getInputPool();
		Input input = inputPool.obtain();
		input.setBuffer(bytes);
		T t = kryo.readObject(input, clazz);

		inputPool.free(input);
		kryoPool.free(kryo);
		return t;
	}
}
