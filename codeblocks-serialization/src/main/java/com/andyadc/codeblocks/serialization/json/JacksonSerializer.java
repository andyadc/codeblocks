package com.andyadc.codeblocks.serialization.json;

import com.andyadc.codeblocks.serialization.SerializerException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;

public class JacksonSerializer {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT));
    }

    private JacksonSerializer() {
    }

    public static <T> String toJSON(T object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new SerializerException(e.getMessage(), e);
        }
    }

    public static <T> T fromJSON(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            throw new SerializerException("Json is null or empty");
        }
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new SerializerException(e.getMessage(), e);
        }
    }
}
