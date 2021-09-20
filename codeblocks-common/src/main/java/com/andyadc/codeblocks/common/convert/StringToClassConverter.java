package com.andyadc.codeblocks.common.convert;

import com.andyadc.codeblocks.common.lang.StringUtils;
import com.andyadc.codeblocks.common.reflect.ClassLoaderUtils;
import com.andyadc.codeblocks.common.reflect.ClassUtils;

/**
 * The class to convert {@link String} to {@link Class}
 */
public class StringToClassConverter implements StringConverter<Class<?>> {

	@Override
	public Class<?> convert(String source) {
		if (StringUtils.isBlank(source)) {
			return null;
		}
		ClassLoader classLoader = ClassLoaderUtils.getClassLoader(getClass());
		return ClassUtils.resolveClass(source, classLoader);
	}
}
