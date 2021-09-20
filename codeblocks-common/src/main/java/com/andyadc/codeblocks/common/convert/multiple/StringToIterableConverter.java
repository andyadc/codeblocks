package com.andyadc.codeblocks.common.convert.multiple;

import com.andyadc.codeblocks.common.convert.Converter;
import com.andyadc.codeblocks.common.convert.StringConverter;
import com.andyadc.codeblocks.common.reflect.ClassUtils;
import com.andyadc.codeblocks.common.reflect.TypeUtils;

import java.util.Collection;
import java.util.Optional;

/**
 * The class to convert {@link String} to {@link Iterable}-based value
 */
public abstract class StringToIterableConverter<T extends Iterable> implements StringToMultiValueConverter {

	public boolean accept(Class<String> type, Class<?> multiValueType) {
		return ClassUtils.isAssignableFrom(getSupportedType(), multiValueType);
	}

	@Override
	public final Object convert(String[] segments, int size, Class<?> multiValueType, Class<?> elementType) {

		Optional<StringConverter> stringConverter = getStringConverter(elementType);

		return stringConverter.map(converter -> {

			T convertedObject = createMultiValue(size, multiValueType);

			if (convertedObject instanceof Collection) {
				Collection collection = (Collection) convertedObject;
				for (int i = 0; i < size; i++) {
					String segment = segments[i];
					Object element = converter.convert(segment);
					collection.add(element);
				}
				return collection;
			}

			return convertedObject;
		}).orElse(null);
	}

	protected abstract T createMultiValue(int size, Class<?> multiValueType);

	protected Optional<StringConverter> getStringConverter(Class<?> elementType) {
		StringConverter converter = (StringConverter) Converter.getConverter(String.class, elementType);
		return Optional.ofNullable(converter);
	}

	protected final Class<T> getSupportedType() {
		return TypeUtils.findActualTypeArgumentClass(getClass(), StringToIterableConverter.class, 0);
	}

	@Override
	public final int getPriority() {
		int level = ClassUtils.getAllInterfaces(getSupportedType(),
			type -> ClassUtils.isAssignableFrom(Iterable.class, type)
		).size();
		return MIN_PRIORITY - level;
	}
}
