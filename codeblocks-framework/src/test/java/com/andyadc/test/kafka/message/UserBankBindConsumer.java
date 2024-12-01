package com.andyadc.test.kafka.message;

import com.andyadc.codeblocks.framework.message.Message;
import com.andyadc.codeblocks.framework.message.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserBankBindConsumer implements MessageConsumer {

	private static final Logger logger = LoggerFactory.getLogger(UserBankBindConsumer.class);

	@Override
	public void consume(Message<String> message) {
		logger.info("UserBankBindCosumer >>> {}", message);
	}
}
