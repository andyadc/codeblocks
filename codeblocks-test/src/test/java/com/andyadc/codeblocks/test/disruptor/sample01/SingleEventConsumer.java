package com.andyadc.codeblocks.test.disruptor.sample01;

import com.andyadc.codeblocks.test.disruptor.EventConsumer;
import com.andyadc.codeblocks.test.disruptor.ValueEvent;
import com.lmax.disruptor.EventHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SingleEventConsumer implements EventConsumer {

	private int expectedValue = -1;

	@Override
	public EventHandler<ValueEvent>[] getEventHandler() {
		final EventHandler<ValueEvent> eventHandler
			= (event, sequence, endOfBatch) -> assertExpectedValue(event.getValue());
		return new EventHandler[]{eventHandler};
	}

	private void assertExpectedValue(final int id) {
		assertEquals(++expectedValue, id);
	}
}
