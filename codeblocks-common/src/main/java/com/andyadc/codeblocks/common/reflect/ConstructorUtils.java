package com.andyadc.codeblocks.common.reflect;

import com.andyadc.codeblocks.common.function.Streams;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * The utilities class of {@link Constructor}
 */
public abstract class ConstructorUtils {

	/**
	 * Is a public no-arg constructor or not ?
	 *
	 * @param constructor {@link Constructor}
	 * @return <code>true</code> if the given {@link Constructor} is a public no-arg one,
	 * otherwise <code>false</code>
	 */
	public static boolean isPublicNoArgConstructor(Constructor<?> constructor) {
		return MemberUtils.isPublic(constructor)
			&& constructor.getParameterCount() == 0;
	}

	public static boolean hasPublicNoArgConstructor(Class<?> type) {
		boolean has = false;
		for (Constructor<?> constructor : type.getConstructors()) {
			if (isPublicNoArgConstructor(constructor)) {
				has = true;
				break;
			}
		}
		return has;
	}

	public static List<Constructor<?>> getConstructors(Class<?> type,
													   Predicate<? super Constructor<?>>... constructorFilters) {
		List<Constructor<?>> constructors = Arrays.asList(type.getConstructors());
		return Streams.filter(constructors, constructorFilters);
	}
}
