package com.andyadc.test.kafka.message;

import com.andyadc.codeblocks.framework.message.Message;
import com.andyadc.codeblocks.framework.message.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRegisterConsumer implements MessageConsumer {

	private static final Logger logger = LoggerFactory.getLogger(UserRegisterConsumer.class);

	@Override
	public void consume(Message<String> message) {
		logger.info("UserRegisterCosumer >>> {}", message);
	}

}
