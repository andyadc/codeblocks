package com.andyadc.codeblocks.common.reflect;

import com.andyadc.codeblocks.common.function.ThrowableSupplier;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * The utilities class for Java Reflection {@link Field}
 *
 * @since 1.0.0
 */
public abstract class FieldUtils {

	/**
	 * Like the {@link Class#getDeclaredField(String)} method without throwing any {@link Exception}
	 *
	 * @param declaredClass the declared class
	 * @param fieldName     the name of {@link Field}
	 * @return if can't be found, return <code>null</code>
	 */
	public static Field getDeclaredField(Class<?> declaredClass, String fieldName) {
		Field targetField = null;
		Field[] fields = declaredClass.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (Objects.equals(fieldName, field.getName())) {
				targetField = field;
			}
		}
		return targetField;
	}

	/**
	 * Find the {@link Field} by the name in the specified class and its inherited types
	 *
	 * @param declaredClass the declared class
	 * @param fieldName     the name of {@link Field}
	 * @return if can't be found, return <code>null</code>
	 */
	public static Field findField(Class<?> declaredClass, String fieldName) {
		Field field = getDeclaredField(declaredClass, fieldName);
		if (field != null) {
			return field;
		}
		for (Class superType : ClassUtils.getAllInheritedTypes(declaredClass)) {
			field = getDeclaredField(superType, fieldName);
			if (field != null) {
				break;
			}
		}

		if (field == null) {
			throw new IllegalStateException(String.format("cannot find field %s,field is null", fieldName));
		}

		return field;
	}

	/**
	 * Find the {@link Field} by the name in the specified class and its inherited types
	 *
	 * @param object    the object whose field should be modified
	 * @param fieldName the name of {@link Field}
	 * @return if can't be found, return <code>null</code>
	 */
	public static Field findField(Object object, String fieldName) {
		return findField(object.getClass(), fieldName);
	}

	/**
	 * Get the value of the specified {@link Field}
	 *
	 * @param object    the object whose field should be modified
	 * @param fieldName the name of {@link Field}
	 * @return the value of  the specified {@link Field}
	 */
	public static <T> T getFieldValue(Object object, String fieldName) {
		return getFieldValue(object, findField(object, fieldName));
	}

	/**
	 * Get the value of the specified {@link Field}
	 *
	 * @param object the object whose field should be modified
	 * @param field  {@link Field}
	 * @return the value of  the specified {@link Field}
	 */
	public static <T> T getFieldValue(Object object, Field field) {
		return (T) ThrowableSupplier.execute(() -> {
			enableAccessible(field);
			return field.get(object);
		});
	}

	/**
	 * Set the value for the specified {@link Field}
	 *
	 * @param object    the object whose field should be modified
	 * @param fieldName the name of {@link Field}
	 * @param value     the value of field to be set
	 * @return the previous value of the specified {@link Field}
	 */
	public static <T> T setFieldValue(Object object, String fieldName, T value) {
		return setFieldValue(object, findField(object, fieldName), value);
	}

	/**
	 * Set the value for the specified {@link Field}
	 *
	 * @param object the object whose field should be modified
	 * @param field  {@link Field}
	 * @param value  the value of field to be set
	 * @return the previous value of the specified {@link Field}
	 */
	public static <T> T setFieldValue(Object object, Field field, T value) {
		Object previousValue = null;
		try {
			enableAccessible(field);
			previousValue = field.get(object);
			if (!Objects.equals(previousValue, value)) {
				field.set(object, value);
			}
		} catch (IllegalAccessException ignored) {
		}
		return (T) previousValue;
	}

	/**
	 * Assert Field type match
	 *
	 * @param object       Object
	 * @param fieldName    field name
	 * @param expectedType expected type
	 * @throws IllegalArgumentException if type is not matched
	 */
	public static void assertFieldMatchType(Object object, String fieldName, Class<?> expectedType) throws IllegalArgumentException {
		Class<?> type = object.getClass();
		Field field = findField(type, fieldName);
		Class<?> fieldType = field.getType();
		if (!expectedType.isAssignableFrom(fieldType)) {
			String message = String.format("The type[%s] of field[%s] in Class[%s] can't match expected type[%s]", fieldType.getName(), fieldName, type.getName(), expectedType.getName());
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Enable field to be accessible
	 *
	 * @param field {@link Field}
	 */
	public static void enableAccessible(Field field) {
		if (!field.isAccessible()) {
			field.setAccessible(true);
		}
	}
}
