package com.andyadc.test.kafka.message;

import com.andyadc.codeblocks.framework.kafka.message.MessagePoller;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class KafkaConsumerTests extends MessageTests {

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context-message-consumer-kafka.xml");
		ctx.getBean("messagePoller", MessagePoller.class);

		printToClose(ctx);
	}


}
