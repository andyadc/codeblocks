package com.andyadc.codeblocks.common.convert.multiple;

import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * The class to convert {@link String} to {@link SortedSet}-based value
 */
public class StringToNavigableSetConverter extends StringToIterableConverter<NavigableSet> {

	@Override
	protected NavigableSet createMultiValue(int size, Class<?> multiValueType) {
		return new TreeSet();
	}
}
