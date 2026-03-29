package com.andyadc.test.kafka.claude.consumer;

import com.andyadc.test.kafka.claude.config.KafkaConfig;
import com.andyadc.test.kafka.claude.metrics.KafkaMetrics;
import com.andyadc.test.kafka.claude.model.OrderEvent;
import com.andyadc.test.kafka.claude.serializer.JsonDeserializer;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@Slf4j
public class OrderEventConsumer implements AutoCloseable {

	private final KafkaConsumer<String, OrderEvent> consumer;
	private final String topic;
	private final KafkaMetrics metrics;
	private final AtomicBoolean running = new AtomicBoolean(false);
	private final AtomicBoolean closed = new AtomicBoolean(false);
	private final CountDownLatch shutdownLatch = new CountDownLatch(1);
	private final Consumer<OrderEvent> eventHandler;
	private final DeadLetterQueueHandler dlqHandler;
	private final Map<TopicPartition, OffsetAndMetadata> pendingOffsets = new ConcurrentHashMap<>();

	// Configuration
	private final int maxRetries;
	private final Duration pollTimeout;
	private final int batchSize;

	public OrderEventConsumer(
		KafkaConfig config,
		MeterRegistry meterRegistry,
		Consumer<OrderEvent> eventHandler,
		DeadLetterQueueHandler dlqHandler) {

		this.topic = config.getOrdersTopic();
		this.metrics = new KafkaMetrics(meterRegistry, "order-consumer");
		this.eventHandler = eventHandler;
		this.dlqHandler = dlqHandler;
		this.maxRetries = config.getMaxRetries();
		this.pollTimeout = Duration.ofMillis(config.getPollTimeoutMs());
		this.batchSize = config.getBatchSize();
		this.consumer = createConsumer(config);

		log.info("OrderEventConsumer initialized for topic: {}", topic);
	}

	private KafkaConsumer<String, OrderEvent> createConsumer(KafkaConfig config) {
		Properties props = new Properties();

		// Bootstrap servers
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());

		// Deserializers
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
		props.put("value.deserializer.type", OrderEvent.class.getName());

		// Consumer group
		props.put(ConsumerConfig.GROUP_ID_CONFIG, config.getConsumerGroupId());
		props.put(ConsumerConfig.CLIENT_ID_CONFIG,
			"order-consumer-" + UUID.randomUUID().toString().substring(0, 8));

		// Offset management
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // Manual commit
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		// Performance tuning
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, config.getBatchSize());
		props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000); // 5 minutes
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000);
		props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 10000);
		props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1);
		props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 500);
		props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 1048576); // 1MB

		// Isolation level for transactional messages
		props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

		// Security (if needed)
		if (config.isSecurityEnabled()) {
			props.put("security.protocol", "SASL_SSL");
			props.put("sasl.mechanism", "PLAIN");
			props.put("sasl.jaas.config", config.getSaslJaasConfig());
		}

		return new KafkaConsumer<>(props);
	}

	/**
	 * Start consuming messages in a blocking manner
	 */
	public void start() {
		if (!running.compareAndSet(false, true)) {
			log.warn("Consumer is already running");
			return;
		}

		log.info("Starting consumer for topic: {}", topic);

		consumer.subscribe(
			Collections.singletonList(topic),
			new RebalanceListener()
		);

		try {
			while (running.get()) {
				pollAndProcess();
			}
		} catch (WakeupException e) {
			if (running.get()) {
				throw e;
			}
			log.info("Consumer wakeup triggered for shutdown");
		} catch (Exception e) {
			log.error("Unexpected error in consumer loop", e);
			metrics.incrementConsumerErrors();
		} finally {
			cleanup();
		}
	}

	private void pollAndProcess() {
		ConsumerRecords<String, OrderEvent> records = consumer.poll(pollTimeout);

		if (records.isEmpty()) {
			return;
		}

		metrics.recordBatchSize(records.count());
		log.debug("Received {} records", records.count());

		for (ConsumerRecord<String, OrderEvent> record : records) {
			Timer.Sample sample = Timer.start(metrics.getRegistry());

			try {
				processRecord(record);
				trackOffset(record);
				sample.stop(metrics.getConsumerLatencyTimer());
				metrics.incrementConsumerSuccess();

			} catch (Exception e) {
				sample.stop(metrics.getConsumerLatencyTimer());
				handleProcessingError(record, e);
			}
		}

		commitOffsets();
	}

	private void processRecord(ConsumerRecord<String, OrderEvent> record) {
		OrderEvent event = record.value();

		log.info("Processing event - eventId: {}, partition: {}, offset: {}, lag: {}ms",
			event.getEventId(),
			record.partition(),
			record.offset(),
			Instant.now().toEpochMilli() - record.timestamp()
		);

		// Log headers for debugging
		record.headers().forEach(header ->
			log.debug("Header: {} = {}", header.key(),
				new String(header.value()))
		);

		eventHandler.accept(event);
	}

	private void handleProcessingError(ConsumerRecord<String, OrderEvent> record, Exception e) {
		metrics.incrementConsumerErrors();
		OrderEvent event = record.value();

		log.error("Error processing event {} at partition {} offset {}: {}",
			event != null ? event.getEventId() : "null",
			record.partition(),
			record.offset(),
			e.getMessage(),
			e
		);

		// Retry logic with exponential backoff
		int retryCount = getRetryCount(record);
		if (retryCount < maxRetries) {
			retryWithBackoff(record, retryCount);
		} else {
			// Send to DLQ after max retries
			sendToDeadLetterQueue(record, e);
			trackOffset(record); // Move past the failed message
		}
	}

	private int getRetryCount(ConsumerRecord<String, OrderEvent> record) {
		return record.headers().headers("retry-count")
			.iterator().hasNext()
			? Integer.parseInt(new String(
			record.headers().headers("retry-count").iterator().next().value()))
			: 0;
	}

	private void retryWithBackoff(ConsumerRecord<String, OrderEvent> record, int retryCount) {
		long backoffMs = (long) Math.pow(2, retryCount) * 1000;
		log.warn("Retrying message processing in {}ms (attempt {}/{})",
			backoffMs, retryCount + 1, maxRetries);

		try {
			Thread.sleep(backoffMs);
			processRecord(record);
			trackOffset(record);
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		} catch (Exception e) {
			// If retry fails, continue with normal error handling
			if (retryCount + 1 >= maxRetries) {
				sendToDeadLetterQueue(record, e);
				trackOffset(record);
			}
		}
	}

	private void sendToDeadLetterQueue(ConsumerRecord<String, OrderEvent> record, Exception e) {
		log.error("Sending message to DLQ after {} retries", maxRetries);
		try {
			dlqHandler.send(record, e);
			metrics.incrementDlqMessages();
		} catch (Exception dlqError) {
			log.error("Failed to send to DLQ", dlqError);
		}
	}

	private void trackOffset(ConsumerRecord<String, OrderEvent> record) {
		TopicPartition partition = new TopicPartition(record.topic(), record.partition());
		OffsetAndMetadata offset = new OffsetAndMetadata(record.offset() + 1);
		pendingOffsets.put(partition, offset);
	}

	private void commitOffsets() {
		if (pendingOffsets.isEmpty()) {
			return;
		}

		try {
			consumer.commitSync(new HashMap<>(pendingOffsets));
			log.debug("Committed offsets: {}", pendingOffsets);
			pendingOffsets.clear();
		} catch (CommitFailedException e) {
			log.error("Failed to commit offsets", e);
			metrics.incrementCommitFailures();
		}
	}

	/**
	 * Graceful shutdown
	 */
	public void shutdown() {
		log.info("Initiating consumer shutdown...");
		running.set(false);
		consumer.wakeup();

		try {
			shutdownLatch.await();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private void cleanup() {
		try {
			// Commit any remaining offsets
			if (!pendingOffsets.isEmpty()) {
				consumer.commitSync(pendingOffsets);
			}
		} catch (Exception e) {
			log.error("Error during final commit", e);
		} finally {
			consumer.close(Duration.ofSeconds(30));
			shutdownLatch.countDown();
			log.info("Consumer shutdown complete");
		}
	}

	@Override
	public void close() {
		if (closed.compareAndSet(false, true)) {
			shutdown();
		}
	}

	/**
	 * Rebalance listener for handling partition assignments
	 */
	private class RebalanceListener implements ConsumerRebalanceListener {

		@Override
		public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
			log.info("Partitions revoked: {}", partitions);

			// Commit offsets for revoked partitions
			Map<TopicPartition, OffsetAndMetadata> toCommit = new HashMap<>();
			for (TopicPartition partition : partitions) {
				OffsetAndMetadata offset = pendingOffsets.remove(partition);
				if (offset != null) {
					toCommit.put(partition, offset);
				}
			}

			if (!toCommit.isEmpty()) {
				try {
					consumer.commitSync(toCommit);
					log.info("Committed offsets for revoked partitions: {}", toCommit);
				} catch (Exception e) {
					log.error("Error committing offsets during rebalance", e);
				}
			}
		}

		@Override
		public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
			log.info("Partitions assigned: {}", partitions);

			// Log current position for each partition
			for (TopicPartition partition : partitions) {
				long position = consumer.position(partition);
				log.info("Starting position for {}: {}", partition, position);
			}
		}

		@Override
		public void onPartitionsLost(Collection<TopicPartition> partitions) {
			log.warn("Partitions lost: {}", partitions);

			// Clear pending offsets for lost partitions
			for (TopicPartition partition : partitions) {
				pendingOffsets.remove(partition);
			}
		}
	}
}
