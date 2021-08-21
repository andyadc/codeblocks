package com.andyadc.codeblocks.common.event;

/**
 * An {@link EventListener} extending the the conditional feature that {@link #accept(Event) decides} some
 * {@link Event event} is handled or not by current listener.
 *
 * @see EventListener
 * @since 1.0.0
 */
public interface ConditionalEventListener<E extends Event> extends EventListener<E> {
	/**
	 * Accept the event is handled or not by current listener
	 *
	 * @param event {@link Event event}
	 * @return if handled, return <code>true</code>, or <code>false</code>
	 */
	boolean accept(E event);
}
