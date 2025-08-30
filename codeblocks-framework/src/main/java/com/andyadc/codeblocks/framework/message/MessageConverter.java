package com.andyadc.codeblocks.framework.message;

import com.andyadc.codeblocks.common.JsonUtils;

public final class MessageConverter {

	public static String toJsonString(Message<?> message) {
		return JsonUtils.toJSONString(message);
	}

	public static String toJsonString(Object obj) {
		return JsonUtils.toJSONString(obj);
	}

	public static <T> T toObject(String body, Class<T> clazz) {
		return JsonUtils.parse(body, clazz);
	}

}
