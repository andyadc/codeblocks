package com.andyadc.test.kafka;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Map;

/**
 * andy.an
 */
public class MyConsumerInterceptor implements ConsumerInterceptor<String, String> {

	@Override
	public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> records) {
		System.out.println("records count: " + records.count());
		return records;
	}

	@Override
	public void onCommit(Map<TopicPartition, OffsetAndMetadata> map) {

	}

	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> map) {

	}
}
