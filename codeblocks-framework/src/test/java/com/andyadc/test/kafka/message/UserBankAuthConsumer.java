package com.andyadc.test.kafka.message;

import com.andyadc.codeblocks.framework.message.Message;
import com.andyadc.codeblocks.framework.message.MessageConsumer;
import com.andyadc.codeblocks.framework.message.MessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserBankAuthConsumer implements MessageConsumer {

	private static final Logger logger = LoggerFactory.getLogger(UserBankAuthConsumer.class);

	@Override
	public void consume(Message<String> message) {
		logger.info("UserBankAuthCosumer >>> {}", message);
		String body = message.getBody();
		UserBankAuthBody bankAuthBody = MessageConverter.toObject(body, UserBankAuthBody.class);
		System.out.println(bankAuthBody);
		System.out.println(message.getSendTime());
	}
}
