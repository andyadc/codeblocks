package com.andyadc.codeblocks.test.disruptor.sample02;

import com.lmax.disruptor.EventHandler;

public class LongEventHandler implements EventHandler<LongEvent> {

	@Override
	public void onEvent(LongEvent longEvent, long sequence, boolean endOfBatch) throws Exception {
		System.out.println("currentThread: " + Thread.currentThread().getName()
			+ " event: " + longEvent
			+ ", sequence: " + sequence
			+ ", endOfBatch: " + endOfBatch
		);
	}
}
