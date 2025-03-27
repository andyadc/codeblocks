package com.andyadc.codeblocks.test.disruptor.sample04;

import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XxxEventHandler implements EventHandler<XxxEventHandler.Message> {

	private static final Logger logger = LoggerFactory.getLogger(XxxEventHandler.class);

	@Override
	public void onEvent(Message event, long sequence, boolean endOfBatch) throws Exception {
		logger.info("接收消息: {}, sequence: {}, endOfBatch:{}", event.getValue(), sequence, endOfBatch);
	}

	public static class Message {

		private String value;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

}
