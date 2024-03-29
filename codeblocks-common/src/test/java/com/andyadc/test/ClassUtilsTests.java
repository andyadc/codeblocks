package com.andyadc.test;

import com.andyadc.codeblocks.common.reflect.ClassUtils;
import com.andyadc.codeblocks.common.util.ClassPathUtils;
import com.andyadc.codeblocks.common.util.MapUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.AbstractCollection;
import java.util.Map;
import java.util.Set;

import static com.andyadc.codeblocks.common.reflect.ClassUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link com.andyadc.codeblocks.common.reflect.ClassUtils}
 */
public class ClassUtilsTests {

	@Test
	public void testIsConcreteClass() {
		assertTrue(isConcreteClass(Object.class));
		assertTrue(isConcreteClass(String.class));
//		assertTrue(concreteClassCache.containsKey(Object.class));
//		assertTrue(concreteClassCache.containsKey(String.class));
//		assertEquals(2, concreteClassCache.size());

		assertFalse(isConcreteClass(CharSequence.class));
		assertFalse(isConcreteClass(AbstractCollection.class));
		assertFalse(isConcreteClass(int.class));
		assertFalse(isConcreteClass(int[].class));
		assertFalse(isConcreteClass(Object[].class));
	}

	@Test
	public void testIsTopLevelClass() {
		assertTrue(isTopLevelClass(Object.class));
		assertTrue(isTopLevelClass(String.class));
		assertFalse(isTopLevelClass(Map.Entry.class));

		class A {

		}

		assertFalse(isTopLevelClass(A.class));
	}

	@Test
	public void testGetClassNamesInClassPath() {
		Set<String> classPaths = ClassPathUtils.getClassPaths();
		for (String classPath : classPaths) {
			Set<String> classNames = ClassUtils.getClassNamesInClassPath(classPath, true);
			assertNotNull(classNames);
		}
	}

	@Test
	public void testGetClassNamesInPackage() {
		Set<String> packageNames = ClassUtils.getAllPackageNamesInClassPaths();
		for (String packageName : packageNames) {
			Set<String> classNames = ClassUtils.getClassNamesInPackage(packageName);
			assertFalse(classNames.isEmpty());
			assertNotNull(classNames);
		}
	}

	@Test
	public void testGetAllPackageNamesInClassPaths() {
		Set<String> packageNames = ClassUtils.getAllPackageNamesInClassPaths();
		assertNotNull(packageNames);
	}

	@Test
	public void testFindClassPath() {
		String classPath = ClassUtils.findClassPath(MapUtils.class);
		assertNotNull(classPath);

		classPath = ClassUtils.findClassPath(String.class);
		assertNotNull(classPath);
	}

	@Test
	public void testGetAllClassNamesMapInClassPath() {
		Map<String, Set<String>> allClassNamesMapInClassPath = ClassUtils.getClassPathToClassNamesMap();
		assertFalse(allClassNamesMapInClassPath.isEmpty());
	}

	@Test
	public void testGetAllClassNamesInClassPath() {
		Set<String> allClassNames = ClassUtils.getAllClassNamesInClassPaths();
		assertFalse(allClassNames.isEmpty());
	}

	@Test
	public void testGetCodeSourceLocation() {
		URL codeSourceLocation = null;
		assertNull(codeSourceLocation);

		codeSourceLocation = ClassUtils.getCodeSourceLocation(getClass());
		assertNotNull(codeSourceLocation);

		codeSourceLocation = ClassUtils.getCodeSourceLocation(String.class);
		assertNotNull(codeSourceLocation);
	}

	@Test
	public void testIsPrimitive() {
		assertTrue(isPrimitive(void.class));
		assertTrue(isPrimitive(Void.TYPE));

		assertTrue(isPrimitive(boolean.class));
		assertTrue(isPrimitive(Boolean.TYPE));

		assertTrue(isPrimitive(byte.class));
		assertTrue(isPrimitive(Byte.TYPE));

		assertTrue(isPrimitive(char.class));
		assertTrue(isPrimitive(Character.TYPE));

		assertTrue(isPrimitive(short.class));
		assertTrue(isPrimitive(Short.TYPE));

		assertTrue(isPrimitive(int.class));
		assertTrue(isPrimitive(Integer.TYPE));

		assertTrue(isPrimitive(long.class));
		assertTrue(isPrimitive(Long.TYPE));

		assertTrue(isPrimitive(float.class));
		assertTrue(isPrimitive(Float.TYPE));

		assertTrue(isPrimitive(double.class));
		assertTrue(isPrimitive(Double.TYPE));

		assertFalse(isPrimitive(null));
		assertFalse(isPrimitive(Object.class));
	}

	@Test
	public void testIsArray() {
		// Primitive-Type array
		assertTrue(isArray(int[].class));

		// Object-Type array
		assertTrue(isArray(Object[].class));

		// Dynamic-Type array
		assertTrue(isArray(Array.newInstance(int.class, 0).getClass()));
		assertTrue(isArray(Array.newInstance(Object.class, 0).getClass()));

		// Dynamic multiple-dimension array
		assertTrue(isArray(Array.newInstance(int.class, 0, 3).getClass()));
		assertTrue(isArray(Array.newInstance(Object.class, 0, 3).getClass()));

		// non-array
		assertFalse(isArray(Object.class));
		assertFalse(isArray(int.class));
	}

	@Test
	public void testResolvePrimitiveType() {
		assertEquals(Boolean.TYPE, resolvePrimitiveType(Boolean.TYPE));
		assertEquals(Boolean.TYPE, resolvePrimitiveType(Boolean.class));

		assertEquals(Byte.TYPE, resolvePrimitiveType(Byte.TYPE));
		assertEquals(Byte.TYPE, resolvePrimitiveType(Byte.class));

		assertEquals(Character.TYPE, resolvePrimitiveType(Character.TYPE));
		assertEquals(Character.TYPE, resolvePrimitiveType(Character.class));

		assertEquals(Short.TYPE, resolvePrimitiveType(Short.TYPE));
		assertEquals(Short.TYPE, resolvePrimitiveType(Short.class));

		assertEquals(Integer.TYPE, resolvePrimitiveType(Integer.TYPE));
		assertEquals(Integer.TYPE, resolvePrimitiveType(Integer.class));

		assertEquals(Long.TYPE, resolvePrimitiveType(Long.TYPE));
		assertEquals(Long.TYPE, resolvePrimitiveType(Long.class));

		assertEquals(Float.TYPE, resolvePrimitiveType(Float.TYPE));
		assertEquals(Float.TYPE, resolvePrimitiveType(Float.class));

		assertEquals(Double.TYPE, resolvePrimitiveType(Double.TYPE));
		assertEquals(Double.TYPE, resolvePrimitiveType(Double.class));
	}

	@Test
	public void testResolveWrapperType() {
		assertEquals(Boolean.class, resolveWrapperType(Boolean.TYPE));
		assertEquals(Boolean.class, resolveWrapperType(Boolean.class));

		assertEquals(Byte.class, resolveWrapperType(Byte.TYPE));
		assertEquals(Byte.class, resolveWrapperType(Byte.class));

		assertEquals(Character.class, resolveWrapperType(Character.TYPE));
		assertEquals(Character.class, resolveWrapperType(Character.class));

		assertEquals(Short.class, resolveWrapperType(Short.TYPE));
		assertEquals(Short.class, resolveWrapperType(Short.class));

		assertEquals(Integer.class, resolveWrapperType(Integer.TYPE));
		assertEquals(Integer.class, resolveWrapperType(Integer.class));

		assertEquals(Long.class, resolveWrapperType(Long.TYPE));
		assertEquals(Long.class, resolveWrapperType(Long.class));

		assertEquals(Float.class, resolveWrapperType(Float.TYPE));
		assertEquals(Float.class, resolveWrapperType(Float.class));

		assertEquals(Double.class, resolveWrapperType(Double.TYPE));
		assertEquals(Double.class, resolveWrapperType(Double.class));
	}

	@Test
	public void testArrayTypeEquals() {
		Class<?> oneArrayType = int[].class;
		Class<?> anotherArrayType = int[].class;

		assertTrue(arrayTypeEquals(oneArrayType, anotherArrayType));

		oneArrayType = int[][].class;
		anotherArrayType = int[][].class;
		assertTrue(arrayTypeEquals(oneArrayType, anotherArrayType));

		oneArrayType = int[][][].class;
		anotherArrayType = int[][][].class;
		assertTrue(arrayTypeEquals(oneArrayType, anotherArrayType));

		oneArrayType = int[][][].class;
		anotherArrayType = int[].class;
		assertFalse(arrayTypeEquals(oneArrayType, anotherArrayType));
	}
}
