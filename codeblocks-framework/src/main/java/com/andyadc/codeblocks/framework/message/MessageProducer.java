package com.andyadc.codeblocks.framework.message;

public interface MessageProducer {

	void send(String topic, Message<?> message);

	void syncSend(String topic, Message<?> message);
}
