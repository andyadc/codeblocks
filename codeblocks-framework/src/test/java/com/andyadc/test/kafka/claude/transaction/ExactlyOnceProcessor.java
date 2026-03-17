package com.andyadc.test.kafka.claude.transaction;

import com.andyadc.test.kafka.claude.model.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;

@Slf4j
public class ExactlyOnceProcessor implements AutoCloseable {

	private final KafkaConsumer<String, OrderEvent> consumer;
	private final KafkaProducer<String, OrderEvent> producer;
	private final String inputTopic;
	private final String outputTopic;
	private final String consumerGroupId;

	public ExactlyOnceProcessor(Properties consumerProps, Properties producerProps,
								String inputTopic, String outputTopic) {
		this.inputTopic = inputTopic;
		this.outputTopic = outputTopic;
		this.consumerGroupId = consumerProps.getProperty(ConsumerConfig.GROUP_ID_CONFIG);

		// Configure consumer for exactly-once
		consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		consumerProps.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

		// Configure producer for transactions
		producerProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
		producerProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG,
			"txn-" + consumerGroupId + "-" + UUID.randomUUID());
		producerProps.put(ProducerConfig.ACKS_CONFIG, "all");

		this.consumer = new KafkaConsumer<>(consumerProps);
		this.producer = new KafkaProducer<>(producerProps);

		// Initialize transactions
		producer.initTransactions();

		log.info("ExactlyOnceProcessor initialized");
	}

	public void processMessages() {
		consumer.subscribe(Collections.singletonList(inputTopic));

		while (true) {
			ConsumerRecords<String, OrderEvent> records = consumer.poll(Duration.ofMillis(100));

			if (records.isEmpty()) {
				continue;
			}

			producer.beginTransaction();

			try {
				Map<TopicPartition, OffsetAndMetadata> offsetsToCommit = new HashMap<>();

				for (ConsumerRecord<String, OrderEvent> record : records) {
					// Process and transform the event
					OrderEvent processedEvent = processEvent(record.value());

					// Send to output topic within transaction
					producer.send(new ProducerRecord<>(outputTopic, record.key(), processedEvent));

					// Track offset
					offsetsToCommit.put(
						new TopicPartition(record.topic(), record.partition()),
						new OffsetAndMetadata(record.offset() + 1)
					);
				}

				// Commit consumer offsets as part of the transaction
				producer.sendOffsetsToTransaction(offsetsToCommit,
					new ConsumerGroupMetadata(consumerGroupId));

				producer.commitTransaction();

				log.debug("Transaction committed for {} records", records.count());

			} catch (Exception e) {
				log.error("Transaction failed, aborting", e);
				producer.abortTransaction();

				// Reset consumer to last committed offsets
				resetToLastCommittedOffsets();
			}
		}
	}

	private OrderEvent processEvent(OrderEvent input) {
		// Transform the event
		return OrderEvent.builder()
			.eventId(input.getEventId())
			.orderId(input.getOrderId())
			.customerId(input.getCustomerId())
			.eventType(input.getEventType())
			.status(input.getStatus())
			.totalAmount(input.getTotalAmount())
			.currency(input.getCurrency())
			.timestamp(input.getTimestamp())
			.version(input.getVersion() + 1)
			.metadata(Map.of(
				"processed", "true",
				"processingTime", String.valueOf(System.currentTimeMillis())
			))
			.build();
	}

	private void resetToLastCommittedOffsets() {
		Set<TopicPartition> assignments = consumer.assignment();
		for (TopicPartition partition : assignments) {
			OffsetAndMetadata committed = consumer.committed(partition);
			if (committed != null) {
				consumer.seek(partition, committed.offset());
			} else {
				consumer.seekToBeginning(Collections.singleton(partition));
			}
		}
	}

	@Override
	public void close() {
		consumer.close();
		producer.close();
	}
}
