package com.andyadc.codeblocks.framework.event;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * event Listenable
 *
 * @see EventDispatcher
 */
public interface Listenable<E extends EventListener<?>> {

	/**
	 * Assets the listener is valid or not
	 *
	 * @param listener the instance of {@link EventListener}
	 */
	static void assertListener(EventListener<?> listener) throws NullPointerException {
		if (listener == null) {
			throw new NullPointerException("The listener must not be null.");
		}

		Class<?> listenerClass = listener.getClass();
		int modifiers = listenerClass.getModifiers();

		if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)) {
			throw new IllegalArgumentException("The listener must be concrete class");
		}
	}

	/**
	 * Add a {@link EventListener event listener}
	 *
	 * @param listener a {@link EventListener event listener}
	 *                 If current {@link EventListener} is existed, return <code>false</code>
	 * @throws NullPointerException     if <code>listener</code> argument is <code>null</code>
	 * @throws IllegalArgumentException if <code>listener</code> argument is not concrete instance
	 */
	void addEventListener(E listener) throws NullPointerException, IllegalArgumentException;

	/**
	 * Add one or more {@link EventListener event listeners}
	 *
	 * @param listener a {@link EventListener event listener}
	 * @param others   an optional {@link EventListener event listeners}
	 * @throws NullPointerException     if one of arguments is <code>null</code>
	 * @throws IllegalArgumentException if one of arguments argument is not concrete instance
	 */
	default void addEventListeners(E listener, E... others) throws NullPointerException,
		IllegalArgumentException {
		List<E> listeners = new ArrayList<>(1 + others.length);
		listeners.add(listener);
		listeners.addAll(Arrays.asList(others));
		addEventListeners(listeners);
	}

	/**
	 * Add multiple {@link EventListener event listeners}
	 *
	 * @param listeners the {@link EventListener event listeners}
	 * @throws NullPointerException     if <code>listeners</code> argument is <code>null</code>
	 * @throws IllegalArgumentException if any element of <code>listeners</code> is not concrete instance
	 */
	default void addEventListeners(Iterable<E> listeners) throws NullPointerException, IllegalArgumentException {
		StreamSupport.stream(listeners.spliterator(), false).forEach(this::addEventListener);
	}

	/**
	 * Remove a {@link EventListener event listener}
	 *
	 * @param listener a {@link EventListener event listener}
	 * @return If remove successfully, return <code>true</code>.
	 * If current {@link EventListener} is existed, return <code>false</code>
	 * @throws NullPointerException if <code>listener</code> argument is <code>null</code>
	 */
	void removeEventListener(E listener) throws NullPointerException, IllegalArgumentException;

	/**
	 * Remove a {@link EventListener event listener}
	 *
	 * @param listeners the {@link EventListener event listeners}
	 * @return If remove successfully, return <code>true</code>.
	 * If current {@link EventListener} is existed, return <code>false</code>
	 * @throws NullPointerException     if <code>listener</code> argument is <code>null</code>
	 * @throws IllegalArgumentException if any element of <code>listeners</code> is not concrete instance
	 */
	default void removeEventListeners(Iterable<E> listeners) throws NullPointerException, IllegalArgumentException {
		StreamSupport.stream(listeners.spliterator(), false).forEach(this::removeEventListener);
	}

	/**
	 * Remove all {@link EventListener event listeners}
	 *
	 * @return a amount of removed listeners
	 */
	default void removeAllEventListeners() {
		removeEventListeners(getAllEventListeners());
	}

	/**
	 * Get all registered {@link EventListener event listeners}
	 *
	 * @return non-null read-only ordered {@link EventListener event listeners}
	 * @see EventListener#getPriority()
	 */
	List<E> getAllEventListeners();
}
