package com.andyadc.codeblocks.framework.message;

import com.alibaba.fastjson2.JSON;

public final class MessageConverter {

	public static String toJsonString(Message<?> message) {
		return JSON.toJSONString(message);
	}

	public static String toJsonString(Object obj) {
		return JSON.toJSONString(obj);
	}

	public static <T> T toObject(String body, Class<T> clazz) {
		return JSON.parseObject(body, clazz);
	}

}
