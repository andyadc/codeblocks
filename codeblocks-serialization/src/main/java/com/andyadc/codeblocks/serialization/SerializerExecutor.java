package com.andyadc.codeblocks.serialization;

import com.andyadc.codeblocks.serialization.binary.FSTSerializer;
import com.andyadc.codeblocks.serialization.binary.JDKSerializer;
import com.andyadc.codeblocks.serialization.binary.KryoSerializer;
import com.andyadc.codeblocks.serialization.binary.ProtostuffSerializer;
import com.andyadc.codeblocks.serialization.compression.CompressorExecutor;
import com.andyadc.codeblocks.serialization.compression.CompressorFactory;
import com.andyadc.codeblocks.serialization.compression.CompressorType;
import com.andyadc.codeblocks.serialization.json.FastjsonSerializer;
import com.andyadc.codeblocks.serialization.json.JacksonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerializerExecutor {

	private static final Logger logger = LoggerFactory.getLogger(SerializerExecutor.class);

	public static <T> byte[] serialize(T object) {
		return serialize(object, CompressorFactory.isCompress());
	}

	public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
		return deserialize(bytes, clazz, CompressorFactory.isCompress());
	}

	public static <T> byte[] serialize(T object, boolean compress) {
		return serialize(object, SerializerFactory.getBinarySerializerType(), CompressorFactory.getCompressorType(), compress, SerializerFactory.isSerializerLogPrint());
	}

	public static <T> T deserialize(byte[] bytes, Class<T> clazz, boolean compress) {
		return deserialize(bytes, clazz, SerializerFactory.getBinarySerializerType(), CompressorFactory.getCompressorType(), compress, SerializerFactory.isSerializerLogPrint());
	}

	public static <T> String toJSON(T object) {
		return toJSON(object, SerializerFactory.getJsonSerializerType());
	}

	public static <T> T fromJSON(String json, Class<T> clazz) {
		return fromJSON(json, clazz, SerializerFactory.getJsonSerializerType());
	}

	public static <T> byte[] serialize(T object, SerializerType serializerType, CompressorType compressorType, boolean compress, boolean serializerLogPrint) {
		byte[] bytes;
		if (serializerType == SerializerType.FST_BINARY) {
			bytes = FSTSerializer.serialize(object);
		} else if (serializerType == SerializerType.KRYO_BINARY) {
			bytes = KryoSerializer.serialize(object);
		} else if (serializerType == SerializerType.PROTO_BINARY) {
			bytes = ProtostuffSerializer.serialize(object);
		} else if (serializerType == SerializerType.JDK_BINARY) {
			bytes = JDKSerializer.serialize(object);
		} else {
			throw new SerializerException("Invalid serializer type of binary: " + serializerType);
		}

		if (compress) {
			print(bytes, null, true, false, serializerLogPrint);
			bytes = compress(bytes, compressorType);
			print(bytes, null, true, true, serializerLogPrint);
		} else {
			print(bytes, object.getClass(), true, false, serializerLogPrint);
		}
		return bytes;
	}

	public static <T> T deserialize(byte[] bytes, Class<T> clazz, SerializerType serializerType, CompressorType compressorType, boolean compress, boolean serializerLogPrint) {
		if (compress) {
			print(bytes, null, false, true, serializerLogPrint);
			bytes = decompress(bytes, compressorType);
			print(bytes, null, false, false, serializerLogPrint);
		}

		T object;
		if (serializerType == SerializerType.FST_BINARY) {
			object = FSTSerializer.deserialize(bytes);
		} else if (serializerType == SerializerType.KRYO_BINARY) {
			object = KryoSerializer.deserialize(bytes, clazz);
		} else if (serializerType == SerializerType.PROTO_BINARY) {
			object = ProtostuffSerializer.deserialize(bytes, clazz);
		} else if (serializerType == SerializerType.JDK_BINARY) {
			object = JDKSerializer.deserialize(bytes);
		} else {
			throw new SerializerException("Invalid serializer type of binary: " + serializerType);
		}

		if (!compress) {
			print(bytes, object.getClass(), false, false, serializerLogPrint);
		}
		return object;
	}

	public static <T> String toJSON(T object, SerializerType serializerType) {
		if (serializerType == SerializerType.JACKSON_JSON) {
			return JacksonSerializer.toJSON(object);
		} else if (serializerType == SerializerType.FAST_JSON) {
			return FastjsonSerializer.toJSON(object);
		} else {
			throw new SerializerException("Invalid serializer type of json: " + serializerType);
		}
	}

	public static <T> T fromJSON(String json, Class<T> clazz, SerializerType serializerType) {
		if (serializerType == SerializerType.JACKSON_JSON) {
			return JacksonSerializer.fromJSON(json, clazz);
		} else if (serializerType == SerializerType.FAST_JSON) {
			return FastjsonSerializer.fromJSON(json, clazz);
		} else {
			throw new SerializerException("Invalid serializer type of json: " + serializerType);
		}
	}

	public static byte[] compress(byte[] bytes, CompressorType compressorType) {
		return CompressorExecutor.compress(bytes, compressorType);
	}

	public static byte[] decompress(byte[] bytes, CompressorType compressorType) {
		return CompressorExecutor.decompress(bytes, compressorType);
	}

	public static void print(byte[] bytes, Class<?> clazz, boolean serialize, boolean compress, boolean serializerLogPrint) {
		if (serializerLogPrint) {
			if (clazz != null) {
				logger.info("{}, size={} KB, {} Byte, compress={}, class={}",
					serialize ? "Serialize" : "Deserialize",
					(float) bytes.length / 1024,
					bytes.length,
					compress,
					clazz.getName());
			} else {
				logger.info("{}, size={} KB, {} Byte, compress={}",
					serialize ? "Serialize" : "Deserialize",
					(float) bytes.length / 1024,
					bytes.length,
					compress);
			}
		}
	}
}
