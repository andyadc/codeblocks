package com.andyadc.test;

import com.andyadc.codeblocks.common.reflect.ClassUtils;
import com.andyadc.codeblocks.common.util.ClassPathUtils;
import com.andyadc.codeblocks.common.util.MapUtils;
import org.junit.jupiter.api.Assertions;
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
		assertTrue(concreteClassCache.containsKey(Object.class));
		assertTrue(concreteClassCache.containsKey(String.class));
		Assertions.assertEquals(2, concreteClassCache.size());

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
}
