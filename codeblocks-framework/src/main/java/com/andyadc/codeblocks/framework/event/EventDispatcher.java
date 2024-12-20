package com.andyadc.codeblocks.framework.event;

import com.andyadc.codeblocks.common.util.ServiceLoaders;

import java.util.ServiceLoader;
import java.util.concurrent.Executor;

/**
 * {@link Event event} Dispatcher
 *
 * @see Event
 * @see EventListener
 * @see DirectEventDispatcher
 */
public interface EventDispatcher extends Listenable<EventListener<?>> {

	/**
	 * Direct {@link Executor} uses sequential execution model
	 */
	Executor DIRECT_EXECUTOR = Runnable::run;

	/**
	 * The default extension of {@link EventDispatcher} is loaded by {@link ServiceLoader}
	 *
	 * @return the default extension of {@link EventDispatcher}
	 */
	static EventDispatcher getDefaultExtension() {
		return ServiceLoaders.loadSpi(EventDispatcher.class);
	}

	/**
	 * Dispatch an event to the registered {@link EventListener event listeners}
	 *
	 * @param event a {@link Event event}
	 */
	void dispatch(Event event);

	/**
	 * The {@link Executor} to dispatch a {@link Event event}
	 *
	 * @return default implementation directly invoke {@link Runnable#run()} method, rather than multiple-threaded
	 * {@link Executor}. If the return value is <code>null</code>, the behavior is same as default.
	 * @see #DIRECT_EXECUTOR
	 */
	default Executor getExecutor() {
		return DIRECT_EXECUTOR;
	}
}
