package com.andyadc.codeblocks.framework.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;

/**
 * 自定义 Jackson2 转换
 */
public class CustomerMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

	protected static final String yyyyMMddHHmmssSSS = "yyyy-MM-dd HH:mm:ss.SSS";

    public CustomerMappingJackson2HttpMessageConverter() {
		super();
		ObjectMapper objectMapper = getObjectMapper();
		// 设置null值不参与序列化(字段不被显示)
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		// 禁用空对象转换 json 校验
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.setDateFormat(new SimpleDateFormat(yyyyMMddHHmmssSSS));
	}
}
