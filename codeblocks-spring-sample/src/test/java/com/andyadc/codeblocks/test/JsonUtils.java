package com.andyadc.codeblocks.test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public final class JsonUtils {

	private static final String yyyyMMddHHmmssSSS = "yyyy-MM-dd HH:mm:ss.SSS";

	private static final ObjectMapper objectMapper;

	static {
		objectMapper = new ObjectMapper();
//		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);

		objectMapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);

		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

		objectMapper.setDateFormat(new SimpleDateFormat(yyyyMMddHHmmssSSS));

	}

	public static void main(String[] args) {
		User user = new User();
		user.setId(1L);
//		user.setName("andy");
		user.setNum(666);
		user.setAmount(new BigDecimal("10.000000777766600009"));
		user.setBirthday(new Date());
		user.setHobbies(Arrays.asList("tech", "digital", "book"));
		HashMap<String, Object> goods = new HashMap<>();
		goods.put("apple", 10);
		user.setGoods(goods);
		System.out.println(toJson(user));
	}

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public static String toJson(Object obj, boolean pretty) {
		if (obj == null)
			return "";

		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String toJson(Object obj) {
		if (obj == null)
			return "";

//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}
}
