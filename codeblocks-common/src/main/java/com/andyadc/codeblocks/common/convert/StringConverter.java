package com.andyadc.codeblocks.common.convert;

/**
 * A class to covert {@link String} to the target-typed value
 *
 * @param <T>
 * @see Converter
 */
@FunctionalInterface
public interface StringConverter<T> extends Converter<String, T> {
}
