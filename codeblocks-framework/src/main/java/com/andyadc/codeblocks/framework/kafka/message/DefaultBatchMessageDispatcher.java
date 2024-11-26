package com.andyadc.codeblocks.framework.kafka.message;

import com.andyadc.codeblocks.framework.message.DefaultMessage;
import com.andyadc.codeblocks.framework.message.MessageConsumer;
import com.andyadc.codeblocks.framework.message.MessageConverter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultBatchMessageDispatcher implements MessageDispatcher {

	private static final Logger logger = LoggerFactory.getLogger(DefaultBatchMessageDispatcher.class);

	private Map<String, MessageConsumer> consumers;

	@Override
	public void doDispatch(KafkaConsumer<String, String> kafkaConsumer, ConsumerRecords<String, String> records) {
		doBatchDispatch(records);
	}

	protected Map<TopicPartition, OffsetAndMetadata> doBatchDispatch(ConsumerRecords<String, String> records) {
		Set<TopicPartition> partitions = records.partitions();
		Map<TopicPartition, OffsetAndMetadata> offsetMap = new HashMap<>(partitions.size(), 1);
		for (TopicPartition partition : partitions) {
			AtomicLong offsetHolder = new AtomicLong(-1);
			try {
				List<ConsumerRecord<String, String>> recordList = records.records(partition);
				doPartitionDispatch(recordList, offsetHolder);
				if (offsetHolder.get() > -1) {
					offsetMap.put(partition, new OffsetAndMetadata(offsetHolder.get()));
				}
			} catch (Exception e) {
				logger.warn("Consume record occurs exception", e);
				break;
			}
		}
		dumpPartitionOffsetInfo(offsetMap);
		return offsetMap;
	}

	protected void doPartitionDispatch(List<ConsumerRecord<String, String>> list, AtomicLong offsetHolder) {
		for (ConsumerRecord<String, String> record : list) {
			try {
				doMessageDispatch(record, offsetHolder);
			} catch (Exception e) {
				logger.error("Consume record occurs exception", e);
			}
		}
	}

	protected void doMessageDispatch(ConsumerRecord<String, String> record, AtomicLong offsetHolder) throws Exception {
		String payload = record.value();
		if (payload == null) {
			offsetHolder.set(record.offset());
			return;
		}
		DefaultMessage message = MessageConverter.toObject(payload, DefaultMessage.class);
		Object body = message.getBody();
		String bodyJson = MessageConverter.toJsonString(body);
		message.setBody(bodyJson);

		String eventType = message.getEventType();
		MessageConsumer consumer = consumers.get(eventType);
		if (consumer == null) {
			logger.info("No consumer for event type {}", eventType);
			offsetHolder.set(record.offset());
			return;
		}

		try {
			consumer.consume(message);
			offsetHolder.set(record.offset());
		} catch (Exception e) {
			logger.error("consumer error", e);
		}
	}

	private void dumpPartitionOffsetInfo(Map<TopicPartition, OffsetAndMetadata> offsets) {
		if (logger.isInfoEnabled()) {
			StringBuilder sb = new StringBuilder("Partition Offset Dump:");
			if (offsets.isEmpty()) {
				sb.append("No message is be consumed!");
			} else {
				for (TopicPartition partition : offsets.keySet()) {
					sb.append("\n\ttopic=").append(partition.topic()).append(", partition=")
						.append(partition.partition()).append(", offset=").append(offsets.get(partition).offset());
				}
			}
			logger.info(sb.toString());
		}
	}

	public void setConsumers(Map<String, MessageConsumer> consumers) {
		this.consumers = consumers;
	}
}
