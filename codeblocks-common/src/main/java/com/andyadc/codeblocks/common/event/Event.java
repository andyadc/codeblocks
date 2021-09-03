package com.andyadc.codeblocks.common.event;

import java.util.EventObject;

/**
 * An event object is based on the Java standard {@link EventObject event}
 */
public abstract class Event extends EventObject {

	/**
	 * The timestamp of event occurs
	 */
	private final long timestamp;

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public Event(Object source) {
		super(source);
		this.timestamp = System.currentTimeMillis();
	}

	public long getTimestamp() {
		return timestamp;
	}
}
