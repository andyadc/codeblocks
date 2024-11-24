package com.andyadc.codeblocks.framework.kafka.message;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public interface MessageDispatcher {

	void doDispatch(KafkaConsumer<String, String> kafkaConsumer, ConsumerRecords<String, String> records);
}
