package com.andyadc.codeblocks.test.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * Consumer that consumes event from ring buffer.
 */
public interface EventConsumer {

	/**
	 * One or more event handler to handle event from ring buffer.
	 */
	EventHandler<ValueEvent>[] getEventHandler();
}
