package com.andyadc.codeblocks.framework.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * 自定义 Jackson2 转换
 *
 * @author andy.an
 * @since 2018/6/4
 */
public class CustomerMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    public CustomerMappingJackson2HttpMessageConverter() {
        super();

        // 设置null值不参与序列化(字段不被显示)
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 禁用空对象转换 json 校验
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
