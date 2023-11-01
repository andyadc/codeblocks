package com.andyadc.workflow.base;

import java.io.Serializable;
import java.lang.reflect.Field;

public final class Utils {

	public static Object getFieldValue(Serializable request, String filed) {
		Class<? extends Serializable> clazz = request.getClass();
		try {
			Field field = clazz.getDeclaredField(filed);
			field.setAccessible(true);
			Object o = field.get(request);
			return String.valueOf(o);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
