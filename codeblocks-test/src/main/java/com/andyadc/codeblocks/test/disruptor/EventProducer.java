package com.andyadc.codeblocks.test.disruptor;

import com.lmax.disruptor.RingBuffer;

/**
 * Producer that produces event for ring buffer.
 */
public interface EventProducer {

	/**
	 * Start the producer that would start producing the values.
	 *
	 * @param ringBuffer ringBuffer
	 * @param count      count
	 */
	void startProducing(final RingBuffer<ValueEvent> ringBuffer, final int count);
}
