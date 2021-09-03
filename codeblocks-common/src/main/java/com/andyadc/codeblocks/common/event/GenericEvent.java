package com.andyadc.codeblocks.common.event;

/**
 * Generic {@link Event event}
 *
 * @param <S> the type of event source
 */
public class GenericEvent<S> extends Event {

	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @throws IllegalArgumentException if source is null.
	 */
	public GenericEvent(Object source) {
		super(source);
	}

	public S getSource() {
		return (S) super.getSource();
	}
}
