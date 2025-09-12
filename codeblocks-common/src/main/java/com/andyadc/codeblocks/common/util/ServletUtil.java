package com.andyadc.codeblocks.common.util;

import com.andyadc.codeblocks.common.lang.StringUtils;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.SortedMap;
import java.util.TreeMap;

public final class ServletUtil {

	/**
	 * get请求处理：将URL请求参数转换成SortedMap
	 */
	public static SortedMap<String, String> getUrlParams(HttpServletRequest request) {
		String param = "";
		SortedMap<String, String> result = new TreeMap<>();

		if (StringUtils.isEmpty(request.getQueryString())) {
			return result;
		}

		param = URLDecoder.decode(request.getQueryString(), StandardCharsets.UTF_8);

		String[] params = param.split("&");
		for (String s : params) {
			String[] array = s.split("=");
			result.put(array[0], array[1]);
		}
		return result;
	}

	/**
	 * post请求处理：获取 Body 参数，转换为SortedMap
	 * TODO
	 */
	public SortedMap<String, String> getBodyParams(final HttpServletRequest request) throws IOException {
//		byte[] requestBody = StreamUtils.copyToByteArray(request.getInputStream());
//		String body = new String(requestBody);
//		return JsonUtil.json2Object(body, SortedMap.class);
		return null;
	}
}
