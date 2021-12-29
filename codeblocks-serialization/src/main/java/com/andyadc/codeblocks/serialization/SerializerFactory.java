package com.andyadc.codeblocks.serialization;

import com.andyadc.codeblocks.serialization.binary.FSTSerializerFactory;
import com.andyadc.codeblocks.serialization.binary.KryoSerializerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class SerializerFactory {

	private static final Logger logger = LoggerFactory.getLogger(SerializerFactory.class);

	private static SerializerType binarySerializerType = SerializerType.FST_BINARY;
	private static SerializerType jsonSerializerType = SerializerType.JACKSON_JSON;
	private static boolean serializerLogPrint;

	public static void initialize(Properties properties) {
		String binarySerializer = properties.getProperty("binarySerializerType");

		binarySerializerType = SerializerType.fromString(binarySerializer);
		logger.info("Binary serializer is {}", binarySerializerType);
		if (binarySerializerType == SerializerType.FST_BINARY) {
			FSTSerializerFactory.initialize();
		} else if (binarySerializerType == SerializerType.KRYO_BINARY) {
			KryoSerializerFactory.initialize();
		}

		String jsonSerializer = properties.getProperty("jsonSerializerType");
		jsonSerializerType = SerializerType.fromString(jsonSerializer);
		logger.info("Json serializer is {}", jsonSerializerType);

		serializerLogPrint = Boolean.parseBoolean(properties.getProperty("serializerLogPrint"));
	}

	public static SerializerType getBinarySerializerType() {
		return binarySerializerType;
	}

	public static void setBinarySerializerType(SerializerType binarySerializerType) {
		SerializerFactory.binarySerializerType = binarySerializerType;
	}

	public static SerializerType getJsonSerializerType() {
		return jsonSerializerType;
	}

	public static void setJsonSerializerType(SerializerType jsonSerializerType) {
		SerializerFactory.jsonSerializerType = jsonSerializerType;
	}

	public static boolean isSerializerLogPrint() {
		return serializerLogPrint;
	}
}
