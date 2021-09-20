package com.andyadc.codeblocks.common.convert.multiple;

import com.andyadc.codeblocks.common.lang.StringUtils;
import com.andyadc.codeblocks.common.util.ArrayUtils;

/**
 * The class to convert {@link String} to multiple value object
 *
 * @see MultiValueConverter
 */
public interface StringToMultiValueConverter extends MultiValueConverter<String> {

	@Override
	default Object convert(String source, Class<?> multiValueType, Class<?> elementType) {
		if (StringUtils.isEmpty(source)) {
			return null;
		}

		// split by the comma
		String[] segments = StringUtils.split(source, ',');

		if (ArrayUtils.isEmpty(segments)) { // If empty array, create an array with only one element
			segments = new String[]{source};
		}

		int size = segments.length;

		return convert(segments, size, multiValueType, elementType);
	}

	/**
	 * Convert the segments to multiple value object
	 *
	 * @param segments    the String array of content
	 * @param size        the size of multiple value object
	 * @param targetType  the target type
	 * @param elementType the element type
	 * @return multiple value object
	 */
	Object convert(String[] segments, int size, Class<?> targetType, Class<?> elementType);
}
