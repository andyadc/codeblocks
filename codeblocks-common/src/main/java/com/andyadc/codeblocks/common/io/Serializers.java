package com.andyadc.codeblocks.common.io;

import com.andyadc.codeblocks.common.reflect.TypeUtils;
import com.andyadc.codeblocks.common.util.PriorityComparator;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

public class Serializers {

	private final Map<Class<?>, List<Serializer>> typedSerializers = new HashMap<>();

	private final ClassLoader classLoader;

	public Serializers(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public Serializers() {
		this(Thread.currentThread().getContextClassLoader());
	}

	public void loadSPI() {
		ServiceLoader<Serializer> serializers = ServiceLoader.load(Serializer.class);
		for (Serializer<?> serializer : serializers) {
			List<Class<?>> typeArguments = TypeUtils.resolveTypeArguments(serializer.getClass());
			Class<?> targetClass = typeArguments.isEmpty() ? Object.class : typeArguments.get(0);
			List<Serializer> serializerList = typedSerializers.computeIfAbsent(targetClass, k -> new LinkedList<>());
			serializerList.add(serializer);
			serializerList.sort(PriorityComparator.INSTANCE);
		}
	}

	/**
	 * Get the most compatible instance of {@link Serializer} by the specified deserialized type
	 *
	 * @param serializedType the type to be serialized
	 * @return <code>null</code> if not found
	 */
	public Serializer<?> getMostCompatible(Class<?> serializedType) {
		Serializer<?> serializer = getHighestPriority(serializedType);
		if (serializer == null) {
			serializer = getLowestPriority(Object.class);
		}
		return serializer;
	}

	/**
	 * Get the highest priority instance of {@link Serializer} by the specified serialized type
	 *
	 * @param serializedType the type to be serialized
	 * @param <S>            the type to be serialized
	 * @return <code>null</code> if not found
	 */
	public <S> Serializer<S> getHighestPriority(Class<S> serializedType) {
		List<Serializer<S>> serializers = get(serializedType);
		return serializers.isEmpty() ? null : serializers.get(0);
	}

	/**
	 * Get the lowest priority instance of {@link Serializer} by the specified serialized type
	 *
	 * @param serializedType the type to be serialized
	 * @param <S>            the type to be serialized
	 * @return <code>null</code> if not found
	 */
	public <S> Serializer<S> getLowestPriority(Class<S> serializedType) {
		List<Serializer<S>> serializers = get(serializedType);
		return serializers.isEmpty() ? null : serializers.get(serializers.size() - 1);
	}

	/**
	 * Get all instances of {@link Serializer} by the specified serialized type
	 *
	 * @param serializedType the type to be serialized
	 * @param <S>            the type to be serialized
	 * @return non-null {@link List}
	 */
	public <S> List<Serializer<S>> get(Class<S> serializedType) {
		return (List) typedSerializers.getOrDefault(serializedType, Collections.emptyList());
	}
}
