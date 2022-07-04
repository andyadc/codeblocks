package com.andyadc.codeblocks.test.disruptor;

import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleEventPrintConsumer implements EventConsumer {

	private static final Logger logger = LoggerFactory.getLogger(SingleEventPrintConsumer.class);

	@Override
	public EventHandler<ValueEvent>[] getEventHandler() {
		EventHandler<ValueEvent> eventHandler
			= (event, sequence, endOfBatch)
			-> print(event.getValue(), sequence);
		return new EventHandler[]{eventHandler};
	}

	private void print(int id, long sequenceId) {
		logger.info("Id is " + id
			+ " sequence id that was used is " + sequenceId);
	}
}
