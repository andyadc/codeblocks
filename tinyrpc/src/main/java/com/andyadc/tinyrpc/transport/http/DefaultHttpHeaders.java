package com.andyadc.tinyrpc.transport.http;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认Http头
 */
public class DefaultHttpHeaders implements HttpHeaders {
	/**
	 * 参数
	 */
	protected Map<CharSequence, Object> params = new HashMap<>();

	@Override
	public Object get(CharSequence name) {
		return params.get(name);
	}

	@Override
	public Map<CharSequence, Object> getAll() {
		return params;
	}

	@Override
	public Object set(CharSequence name, Object value) {
		return params.put(name, value);
	}

	@Override
	public boolean add(CharSequence name, Object value) {
		return params.putIfAbsent(name, value) == null;
	}

	@Override
	public Object remove(String name) {
		return params.remove(name);
	}
}
