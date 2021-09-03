package com.andyadc.codeblocks.common.event;

import com.andyadc.codeblocks.common.function.ThrowableConsumer;
import com.andyadc.codeblocks.common.function.ThrowableFunction;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * An abstract class of {@link EventListener} for Generic events, the sub class could add more {@link Event event}
 * handle methods, rather than only binds the {@link EventListener#onEvent(Event)} method that is declared to be
 * <code>final</code> the implementation can't override. It's notable that all {@link Event event} handle methods must
 * meet following conditions:
 * <ul>
 * <li>not {@link #onEvent(Event)} method</li>
 * <li><code>public</code> accessibility</li>
 * <li><code>void</code> return type</li>
 * <li>no {@link Exception exception} declaration</li>
 * <li>only one {@link Event} type argument</li>
 * </ul>
 *
 * @see Event
 * @see EventListener
 */
public abstract class GenericEventListener implements EventListener<Event> {

	private final Method onEventMethod;

	private final Map<Class<?>, Set<Method>> handleEventMethods;

	protected GenericEventListener() {
		this.onEventMethod = findOnEventMethod();
		this.handleEventMethods = findHandleEventMethods();
	}

	private Method findOnEventMethod() {
		return ThrowableFunction.execute(getClass(), listenerClass -> listenerClass.getMethod("onEvent", Event.class));
	}

	private Map<Class<?>, Set<Method>> findHandleEventMethods() {
		// Event class for key, the eventMethods' Set as value
		Map<Class<?>, Set<Method>> eventMethods = new HashMap<>();
		Stream.of(getClass().getMethods())
			.filter(this::isHandleEventMethod)
			.forEach(method -> {
				Class<?> paramType = method.getParameterTypes()[0];
				Set<Method> methods = eventMethods.computeIfAbsent(paramType, key -> new LinkedHashSet<>());
				methods.add(method);
			});
		return eventMethods;
	}

	public final void onEvent(Event event) {
		Class<?> eventClass = event.getClass();
		handleEventMethods.getOrDefault(eventClass, Collections.emptySet()).forEach(method -> {
			ThrowableConsumer.execute(method, m -> {
				method.invoke(this, event);
			});
		});
	}

	/**
	 * The {@link Event event} handle methods must meet following conditions:
	 * <ul>
	 * <li>not {@link #onEvent(Event)} method</li>
	 * <li><code>public</code> accessibility</li>
	 * <li><code>void</code> return type</li>
	 * <li>no {@link Exception exception} declaration</li>
	 * <li>only one {@link Event} type argument</li>
	 * </ul>
	 */
	private boolean isHandleEventMethod(Method method) {
		if (onEventMethod.equals(method)) { // not {@link #onEvent(Event)} method
			return false;
		}

		if (!Modifier.isPublic(method.getModifiers())) { // not public
			return false;
		}

		if (!void.class.equals(method.getReturnType())) { // void return type
			return false;
		}

		Class<?>[] exceptionTypes = method.getExceptionTypes();
		if (exceptionTypes.length > 0) { // no exception declaration
			return false;
		}

		Class<?>[] paramTypes = method.getParameterTypes();
		if (paramTypes.length != 1) { // not only one argument
			return false;
		}

		// not Event type argument
		return Event.class.isAssignableFrom(paramTypes[0]);
	}
}
