package com.andyadc.codeblocks.framework.event;

/**
 * Generic {@link Event event}
 *
 * @param <S> the type of event source
 */
public class GenericEvent<S> extends Event {

	private static final long serialVersionUID = 5392110492655173361L;

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
