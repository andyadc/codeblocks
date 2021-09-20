package com.andyadc.codeblocks.common.convert;

import java.util.Optional;

/**
 * The class to convert {@link String} to {@link Optional}
 */
public class StringToOptionalConverter implements StringConverter<Optional<?>> {

	@Override
	public Optional<?> convert(String source) {
		return Optional.ofNullable(source);
	}

	@Override
	public int getPriority() {
		return MIN_PRIORITY;
	}
}
