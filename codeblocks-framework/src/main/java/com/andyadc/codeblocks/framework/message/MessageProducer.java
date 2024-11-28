package com.andyadc.codeblocks.framework.message;

public interface MessageProducer {

	void send(String topic, Message<?> message);

	/**
	 * 向 topic 发送消息，同时提供消息的 key 和指定分区  -→  发送给指定分区
	 *
	 * @param topic     topic
	 * @param partition partition
	 * @param key       key
	 * @param message   message
	 */
	void send(String topic, Integer partition, String key, Message<?> message);

	void syncSend(String topic, Message<?> message);

	void syncSend(String topic, Integer partition, String key, Message<?> message);
}
