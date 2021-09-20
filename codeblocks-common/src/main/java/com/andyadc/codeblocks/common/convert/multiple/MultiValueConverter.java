package com.andyadc.codeblocks.common.convert.multiple;

import com.andyadc.codeblocks.common.lang.Prioritized;
import com.andyadc.codeblocks.common.reflect.TypeUtils;

import java.util.Collection;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

/**
 * An interface to convert the source-typed value to multiple value, e.g , Java array, {@link Collection} or
 * sub-interfaces
 *
 * @param <S> The source type
 */
public interface MultiValueConverter<S> extends Prioritized {

	/**
	 * Find the {@link MultiValueConverter} instance with the specified source and target type
	 *
	 * @param sourceType the source type
	 * @param targetType the target type
	 * @return <code>null</code> if not found
	 */
	static MultiValueConverter<?> find(Class<?> sourceType, Class<?> targetType) {
		return StreamSupport.stream(ServiceLoader.load(MultiValueConverter.class).spliterator(), false)
			.filter(converter -> converter.accept(sourceType, targetType))
			.findFirst()
			.orElse(null);
	}

	static <T> T convertIfPossible(Object source, Class<?> multiValueType, Class<?> elementType) {
		if (source == null) {
			return null;
		}
		Class<?> sourceType = source.getClass();
		MultiValueConverter converter = find(sourceType, multiValueType);
		if (converter != null) {
			return (T) converter.convert(source, multiValueType, elementType);
		}
		return null;
	}

	/**
	 * Accept the source type and target type or not
	 *
	 * @param sourceType     the source type
	 * @param multiValueType the multi-value type
	 * @return if accepted, return <code>true</code>, or <code>false</code>
	 */
	boolean accept(Class<S> sourceType, Class<?> multiValueType);

	/**
	 * Convert the source to be the multiple value
	 *
	 * @param source         the source-typed value
	 * @param multiValueType the multi-value type
	 * @param elementType    the element type
	 * @return
	 */
	Object convert(S source, Class<?> multiValueType, Class<?> elementType);

	/**
	 * Get the source type
	 *
	 * @return non-null
	 */
	default Class<S> getSourceType() {
		return TypeUtils.findActualTypeArgumentClass(getClass(), MultiValueConverter.class, 0);
	}
}
