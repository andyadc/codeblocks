package com.andyadc.codeblocks.common;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;

public final class JsonUtils {

	private static final ObjectMapper mapper = new ObjectMapper();
	private static final JsonFactory jasonFactory = mapper.getFactory();

	static {
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 日期不序列化为时间戳
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

		// 	当反序列化出现未定义字段时候，不出现错误
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
			.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
			.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
			.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

	}

	public static String toJSONString(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException("object format to json error " + obj, e);
		}
	}

	public static <T> T parse(String str, Class<T> clz) {
		try {
			return mapper.readValue(str == null ? "{}" : str, clz);
		} catch (Exception e) {
			throw new RuntimeException("json parse to object [" + clz + "] error " + str, e);
		}
	}

	public static <T> T parse(String str, TypeReference<T> tr) {
		try {
			return mapper.readValue(str, tr);
		} catch (Exception e) {
			throw new RuntimeException("json parse to object [" + tr + "] error " + str, e);
		}
	}

	public static String parseOneField(String str, String fieldName) {
		try (JsonParser jsonParser = jasonFactory.createParser(str)) {
			while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
				// get the current token
				String fieldname = jsonParser.currentName();
				if (fieldName.equals(fieldname)) {
					// move to next token
					jsonParser.nextToken();
					return jsonParser.getText();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("object format to json error.", e);
		}
		return null;
	}

}
