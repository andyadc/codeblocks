package com.andyadc.codeblocks.common.convert.multiple;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * The class to convert {@link String} to {@link SortedSet}-based value
 */
public class StringToSortedSetConverter extends StringToIterableConverter<SortedSet> {

	@Override
	protected SortedSet createMultiValue(int size, Class<?> multiValueType) {
		return new TreeSet();
	}
}
