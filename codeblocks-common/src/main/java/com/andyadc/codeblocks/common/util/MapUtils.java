package com.andyadc.codeblocks.common.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MapUtils extends BaseUtils {

	public static Map of(Object... values) {
		int length = values.length;
		Map map = new HashMap<>(length / 2);
		for (int i = 0; i < length; ) {
			map.put(values[i++], values[i++]);
		}
		return Collections.unmodifiableMap(map);
	}

	public static <K, V> Map<K, V> newLinkedHashMap() {
		return new LinkedHashMap<>();
	}

	public static <K, V> Map<K, V> newLinkedHashMap(int initialCapacity) {
		return new LinkedHashMap<>(initialCapacity, 0.75f);
	}

	public static <K, V> Map<K, V> newLinkedHashMap(int initialCapacity,
													float loadFactor) {
		return newLinkedHashMap(initialCapacity, loadFactor, false);
	}

	public static <K, V> Map<K, V> newLinkedHashMap(int initialCapacity,
													float loadFactor,
													boolean accessOrder) {
		return new LinkedHashMap<>(initialCapacity, loadFactor, accessOrder);
	}

	public static <K, V> Map<K, V> newConcurrentHashMap() {
		return new ConcurrentHashMap<>();
	}

	public static <K, V> Map<K, V> newConcurrentHashMap(int initialCapacity) {
		return newConcurrentHashMap(initialCapacity, 0.75f);
	}

	public static <K, V> Map<K, V> newConcurrentHashMap(int initialCapacity,
														float loadFactor) {
		return new ConcurrentHashMap<>(initialCapacity, loadFactor);
	}
}
