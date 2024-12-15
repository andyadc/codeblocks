package com.andyadc.test.concurrent;

import com.andyadc.codeblocks.framework.concurrent.LockFreeRingBuffer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * https://github.com/jianhong-li/LockFreeRingBuffer
 */
public class LockFreeRingBufferTest {

	public static final Logger logger = LoggerFactory.getLogger(LockFreeRingBufferTest.class);

	/**
	 * test integer  overflow
	 */
	@Test
	public void testOverflow() {
		int y = 0;
		int x = Integer.MAX_VALUE;
		x = x + 1;
		Assertions.assertEquals(Integer.MIN_VALUE, x);
		y = x & 0x7;
		Assertions.assertEquals(0, y);
		y = (++x) & 0x07;
		Assertions.assertEquals(1, y);
		y = (++x) & 0x07;
		Assertions.assertEquals(2, y);
	}

	/**
	 * Test method for {@link LockFreeRingBuffer#LockFreeRingBuffer(int)}.
	 * Test if the buffer size is power of 2
	 */
	@Test
	public void testBufferSize() {
		Exception exception = null;
		try {
			LockFreeRingBuffer<Integer> buffer = new LockFreeRingBuffer<>(100);
		} catch (Exception e) {
			exception = e;
		}
		Assertions.assertNotNull(exception);
		Assertions.assertTrue(exception instanceof IllegalArgumentException);

		exception = null;
		try {
			LockFreeRingBuffer<Integer> buffer = new LockFreeRingBuffer<>(4);
		} catch (Exception e) {
			exception = e;
		}
		Assertions.assertNull(exception);

		exception = null;
		try {
			LockFreeRingBuffer<Integer> buffer = new LockFreeRingBuffer<>(1024);
		} catch (Exception e) {
			exception = e;
		}
		Assertions.assertNull(exception);
	}

	/**
	 * Test method for {@link LockFreeRingBuffer#push(Object)}.
	 * Test if the buffer is full
	 */
	@Test
	public void testPush() {
		LockFreeRingBuffer<Integer> buffer = new LockFreeRingBuffer<>(4);
		Assertions.assertTrue(buffer.push(1) >= 0);
		Assertions.assertTrue(buffer.push(2) >= 0);
		Assertions.assertTrue(buffer.push(3) >= 0);
		Assertions.assertFalse(buffer.push(4) >= 0);
		Assertions.assertFalse(buffer.push(5) >= 0);
		Assertions.assertFalse(buffer.push(null) >= 0);
	}

	@Test
	public void testSingleThread() {
		int bufferSize = 8;
		int threadCnt = 8;
		logger.info("threadCnt:{},bufferSize:{}", threadCnt, bufferSize);
		LockFreeRingBuffer<ExclusiveData> ringBuffer = new LockFreeRingBuffer<>(bufferSize);
		// init buffer data
		for (int i = 0; i < bufferSize - 1; i++) {
			ringBuffer.push(new ExclusiveData(i));
		}

		for (int i = 0; i < 2000; i++) {
			ExclusiveData data = ringBuffer.pop();
			Assertions.assertNotNull(data);
			Assertions.assertEquals(data.index, (i % 7));
			ringBuffer.push(data);
		}
	}

	/**
	 * 测试一个环形缓冲区中的数据不会被同时分配给两个以上的线程使用.
	 */
	@Test
	public void testCaseRepeatGetOne() throws InterruptedException {
		int bufferSize = 8;
		int threadCnt = 8;
		int loopCnt = 100;
		logger.info("threadCnt:{},bufferSize:{}", threadCnt, bufferSize);

		// init buffer data
		LockFreeRingBuffer<ExclusiveData> ringBuffer = new LockFreeRingBuffer<>(bufferSize);
		for (int i = 0; i < bufferSize - 1; i++) {
			ringBuffer.push(new ExclusiveData(i));
		}
		// reset count after init
		// ringBuffer.resetCnt();

		boolean[] rs = new boolean[threadCnt];
		Arrays.fill(rs, true);

		CountDownLatch countDownLatch = new CountDownLatch(threadCnt);

		for (int i = 0; i < threadCnt; i++) {
			int _index = i;
			new Thread(() -> {

				try {
					for (int j = 0; j < loopCnt; j++) {

						ExclusiveData exclusiveData = ringBuffer.pop();
						if (exclusiveData != null) {
//                            logger.info("thread: {} get data:{}", _index, exclusiveData.index);
							boolean success = exclusiveData.fooWithLock();
							int push = ringBuffer.push(exclusiveData);
//                            logger.info("thread: {} push [{}] to index :{}", _index, exclusiveData.index, push);
							if (!success) {
								rs[_index] = false;
								break;
							}
						} else {
//                            logger.warn("thread: {} no buffer data", _index);
						}
					}

				} catch (Exception e) {
					logger.error("run with exclusiveData Error:", e);
				}
				countDownLatch.countDown();
			}).start();
		}

		countDownLatch.await();

		for (boolean r : rs) {
			Assertions.assertTrue(r);
		}

		System.out.println(ringBuffer);

		Assertions.assertEquals(ringBuffer.getReadCnt() + bufferSize - 1, ringBuffer.getWriteCnt());
		Assertions.assertEquals(loopCnt * (threadCnt - 1), ringBuffer.getReadCnt());
	}

	public class ExclusiveData {

		private final int index;

		private final int slot = -1;
		private final Lock lock = new ReentrantLock();

		public ExclusiveData(int index) {
			this.index = index;
		}

		@Override
		public String toString() {
			return "ExclusiveData{" +
				"index=" + index +
				'}';
		}

		public boolean fooWithLock() {
			boolean locked = lock.tryLock();
			if (locked) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					//
				}
				lock.unlock();
				return true;
			} else {
				logger.error("tryLock Data error");
				return false;
			}
		}
	}

}
