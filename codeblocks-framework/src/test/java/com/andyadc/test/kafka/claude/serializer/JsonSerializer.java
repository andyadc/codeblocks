package com.andyadc.test.kafka.claude.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Slf4j
public class JsonSerializer<T> implements Serializer<T> {

	private final ObjectMapper objectMapper;

	public JsonSerializer() {
		this.objectMapper = new ObjectMapper()
			.registerModule(new JavaTimeModule())
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			.enable(SerializationFeature.INDENT_OUTPUT);
	}

	public JsonSerializer(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// Configuration can be loaded from configs map if needed
	}

	@Override
	public byte[] serialize(String topic, T data) {
		if (data == null) {
			log.debug("Null data received for serialization on topic: {}", topic);
			return null;
		}

		try {
			return objectMapper.writeValueAsBytes(data);
		} catch (JsonProcessingException e) {
			String errorMsg = String.format(
				"Error serializing message for topic %s: %s",
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
