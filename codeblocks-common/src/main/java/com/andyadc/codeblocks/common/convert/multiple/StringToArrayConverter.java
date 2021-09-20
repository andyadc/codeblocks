package com.andyadc.codeblocks.common.convert.multiple;

import com.andyadc.codeblocks.common.convert.Converter;

import java.lang.reflect.Array;

/**
 * The class to convert {@link String} to array-type object
 */
public class StringToArrayConverter implements StringToMultiValueConverter {

	@Override
	public boolean accept(Class<String> sourceType, Class<?> multiValueType) {
		return multiValueType != null && multiValueType.isArray();
	}

	@Override
	public Object convert(String[] segments, int size, Class<?> targetType, Class<?> elementType) {
		Class<?> componentType = targetType.getComponentType();

		Converter converter = Converter.getConverter(String.class, componentType);

		Object array = Array.newInstance(componentType, size);

		for (int i = 0; i < size; i++) {
			Array.set(array, i, converter.convert(segments[i]));
		}

		return array;
	}

	@Override
	public int getPriority() {
		return MIN_PRIORITY;
	}
}
