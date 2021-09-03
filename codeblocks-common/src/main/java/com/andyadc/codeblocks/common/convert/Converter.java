package com.andyadc.codeblocks.common.convert;

import com.andyadc.codeblocks.common.function.Streams;
import com.andyadc.codeblocks.common.lang.Prioritized;
import com.andyadc.codeblocks.common.reflect.ClassUtils;
import com.andyadc.codeblocks.common.reflect.TypeUtils;

import java.util.ServiceLoader;
import java.util.function.Function;

@FunctionalInterface
public interface Converter<S, T> extends Function<S, T>, Prioritized {

	/**
	 * Get the Converter instance from {@link ServiceLoader} with the specified source and target type
	 *
	 * @param sourceType the source type
	 * @param targetType the target type
	 * @see ServiceLoader#load(Class)
	 */
	static Converter<?, ?> getConverter(Class<?> sourceType, Class<?> targetType) {
		return Streams.stream(ServiceLoader.load(Converter.class))
			.filter(converter -> converter.accept(sourceType, targetType))
			.findFirst()
			.orElse(null);
	}

	/**
	 * Convert the value of source to target-type value if possible
	 *
	 * @param source     the value of source
	 * @param targetType the target type
	 * @param <T>        the target type
	 * @return <code>null</code> if can't be converted
	 */
	static <T> T convertIfPossible(Object source, Class<T> targetType) {
		if (source == null) {
			return null;
		}
		Converter converter = getConverter(source.getClass(), targetType);
		if (converter != null) {
			return (T) converter.convert(source);
		}
		return null;
	}

	/**
	 * Accept the source type and target type or not
	 *
	 * @param sourceType the source type
	 * @param targetType the target type
	 * @return if accepted, return <code>true</code>, or <code>false</code>
	 */
	default boolean accept(Class<?> sourceType, Class<?> targetType) {
		return ClassUtils.isAssignableFrom(sourceType, getSourceType())
			&& ClassUtils.isAssignableFrom(targetType, getTargetType());
	}

	/**
	 * Convert the source-typed value to the target-typed value
	 *
	 * @param source the source-typed value
	 * @return the target-typed value
	 */
	T convert(S source);

	@Override
	default T apply(S source) {
		return convert(source);
	}

	/**
	 * Get the source type
	 *
	 * @return non-null
	 */
	default Class<S> getSourceType() {
		return TypeUtils.findActualTypeArgument(getClass(), Converter.class, 0);
	}

	/**
	 * Get the target type
	 *
	 * @return non-null
	 */
	default Class<T> getTargetType() {
		return TypeUtils.findActualTypeArgument(getClass(), Converter.class, 1);
	}
}
