package com.andyadc.codeblocks.common.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * The utilities class for Java Reflection {@link Member}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public abstract class MemberUtils {

	/**
	 * check the specified {@link Member member} is static or not ?
	 *
	 * @param member {@link Member} instance, e.g, {@link Constructor}, {@link Method} or {@link Field}
	 * @return Iff <code>member</code> is static one, return <code>true</code>, or <code>false</code>
	 */
	public static boolean isStatic(Member member) {
		return member != null && Modifier.isStatic(member.getModifiers());
	}

	/**
	 * check the specified {@link Member member} is private or not ?
	 *
	 * @param member {@link Member} instance, e.g, {@link Constructor}, {@link Method} or {@link Field}
	 * @return Iff <code>member</code> is private one, return <code>true</code>, or <code>false</code>
	 */
	public static boolean isPrivate(Member member) {
		return member != null && Modifier.isPrivate(member.getModifiers());
	}

	/**
	 * check the specified {@link Member member} is public or not ?
	 *
	 * @param member {@link Member} instance, e.g, {@link Constructor}, {@link Method} or {@link Field}
	 * @return Iff <code>member</code> is public one, return <code>true</code>, or <code>false</code>
	 */
	public static boolean isPublic(Member member) {
		return member != null && Modifier.isPublic(member.getModifiers());
	}
}
