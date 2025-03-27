package com.andyadc.codeblocks.test.disruptor.sample04;

import com.lmax.disruptor.dsl.Disruptor;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

public class XxxEventDisruptorTest {

	private static final Disruptor<XxxEventHandler.Message> xxxEventDisruptor;

	static {
		DisruptorConfig config = new DisruptorConfig();
		xxxEventDisruptor = config.disruptor();
	}

	@Test
	public void test_publishEvent() throws Exception {
		for (int i = 0; i < 10; i++) {
			xxxEventDisruptor.publishEvent(
				((event, sequence) -> event.setValue("你好，我是 Disruptor Message"))
			);
		}

		// 暂停 - 测试完手动关闭程序
		new CountDownLatch(1).await();
	}
}
