package com.andyadc.codeblocks.test.disruptor.sample02;

import com.andyadc.codeblocks.kit.concurrent.ThreadUtil;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

public class LongEventMain {

	public static void main(String[] args) {
		int bufferSize = 2;

		Disruptor<LongEvent> disruptor =
			new Disruptor<>(
				new LongEventFactory(),
				bufferSize,
				DaemonThreadFactory.INSTANCE,
				ProducerType.SINGLE,
				new BlockingWaitStrategy()
			);

		disruptor.handleEventsWith(new LongEventHandler());
		disruptor.start();

		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
		ByteBuffer byteBuffer = ByteBuffer.allocate(8);
		for (long l = 0L; true; l++) {
			byteBuffer.putLong(0, l);
			ringBuffer.publishEvent(
				(event, sequence, buffer) -> event.set(buffer.getLong(0)), byteBuffer
			);

			ThreadUtil.sleep(1000);
		}
	}
}
