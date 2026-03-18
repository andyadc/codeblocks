package com.andyadc.test.kafka.claude.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class JsonDeserializer<T> implements Deserializer<T> {

	private final ObjectMapper objectMapper;
	private Class<T> targetType;

	public JsonDeserializer() {
		this.objectMapper = new ObjectMapper()
			.registerModule(new JavaTimeModule());
	}

	public JsonDeserializer(Class<T> targetType) {
		this();
		this.targetType = targetType;
	}

	public JsonDeserializer(Class<T> targetType, ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		this.targetType = targetType;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void configure(Map<String, ?> configs, boolean isKey) {
		if (targetType == null) {
			String typeClassName = (String) configs.get("value.deserializer.type");
			if (typeClassName != null) {
				try {
					targetType = (Class<T>) Class.forName(typeClassName);
				} catch (ClassNotFoundException e) {
					throw new SerializationException("Failed to configure deserializer", e);
				}
			}
		}
	}

	@Override
	public T deserialize(String topic, byte[] data) {
		if (data == null || data.length == 0) {
			log.debug("Null or empty data received for deserialization on topic: {}", topic);
			return null;
		}

		try {
			return objectMapper.readValue(data, targetType);
		} catch (IOException e) {
			String errorMsg = String.format(
				"Error deserializing message from topic %s: %s",
				topic,
				e.getMessage()
			);
			log.error(errorMsg, e);
			throw new SerializationException(errorMsg, e);
		}
	}

	@Override
	public void close() {
		// Nothing to close
	}

}
