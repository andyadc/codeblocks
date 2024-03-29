package com.andyadc.codeblocks.test.disruptor;

import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiEventPrintConsumer implements EventConsumer {

	private static final Logger logger = LoggerFactory.getLogger(MultiEventPrintConsumer.class);

	@Override
	public EventHandler<ValueEvent>[] getEventHandler() {
		final EventHandler<ValueEvent> eventHandler
			= (event, sequence, endOfBatch) -> print(event.getValue(), sequence);

		final EventHandler<ValueEvent> otherEventHandler
			= (event, sequence, endOfBatch) -> print(event.getValue(), sequence);
		return new EventHandler[]{eventHandler, otherEventHandler};
	}

	private void print(final int id, final long sequenceId) {
		logger.info("Id is " + id + " sequence id that was used is " + sequenceId);
	}
}
