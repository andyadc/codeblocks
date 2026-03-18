package com.andyadc.test.kafka.claude.producer;

import com.andyadc.test.kafka.claude.config.KafkaConfig;
import com.andyadc.test.kafka.claude.metrics.KafkaMetrics;
import com.andyadc.test.kafka.claude.model.OrderEvent;
import com.andyadc.test.kafka.claude.serializer.JsonSerializer;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.RetriableException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.serialization.StringSerializer;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class OrderEventProducer implements AutoCloseable {

	private final KafkaProducer<String, OrderEvent> producer;
	private final String topic;
	private final KafkaMetrics metrics;
	private final AtomicBoolean closed = new AtomicBoolean(false);
	private final ExecutorService callbackExecutor;

	public OrderEventProducer(KafkaConfig config, MeterRegistry meterRegistry) {
		this.topic = config.getOrdersTopic();
		this.metrics = new KafkaMetrics(meterRegistry, "order-producer");
		this.callbackExecutor = Executors.newFixedThreadPool(
			Runtime.getRuntime().availableProcessors(),
			r -> {
				Thread t = new Thread(r, "kafka-callback-");
				t.setDaemon(true);
				return t;
			}
		);
		this.producer = createProducer(config);

		log.info("OrderEventProducer initialized for topic: {}", topic);
	}

	private KafkaProducer<String, OrderEvent> createProducer(KafkaConfig config) {
		Properties props = new Properties();

		// Bootstrap servers
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());

		// Serializers
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

		// Reliability settings
		props.put(ProducerConfig.ACKS_CONFIG, "all"); // Wait for all replicas
		props.put(ProducerConfig.RETRIES_CONFIG, 3);
		props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
		props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000); // 2 minutes
		props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);

		// Idempotence (exactly-once semantics)
		props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
		props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);

		// Batching for performance
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); // 16KB
		props.put(ProducerConfig.LINGER_MS_CONFIG, 5); // Wait up to 5ms for batching
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432); // 32MB

		// Compression
		props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");

		// Client identification
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "order-service-producer-" + UUID.randomUUID());

		// Security (if needed)
		if (config.isSecurityEnabled()) {
			props.put("security.protocol", "SASL_SSL");
			props.put("sasl.mechanism", "PLAIN");
			props.put("sasl.jaas.config", config.getSaslJaasConfig());
		}

		return new KafkaProducer<>(props);
	}

	/**
	 * Sends an order event asynchronously with full observability
	 */
	public CompletableFuture<RecordMetadata> sendAsync(OrderEvent event) {
		validateEvent(event);

		String key = event.getOrderId().toString();
		Headers headers = createHeaders(event);

		ProducerRecord<String, OrderEvent> record = new ProducerRecord<>(
			topic,
			null, // partition (let Kafka decide based on key)
			event.getTimestamp().toEpochMilli(),
			key,
			event,
			headers
		);

		CompletableFuture<RecordMetadata> future = new CompletableFuture<>();
		Timer.Sample sample = Timer.start(metrics.getRegistry());

		try {
			producer.send(record, (metadata, exception) -> {
				callbackExecutor.execute(() -> {
					sample.stop(metrics.getProducerLatencyTimer());

					if (exception != null) {
						handleSendFailure(event, exception, future);
					} else {
						handleSendSuccess(event, metadata, future);
					}
				});
			});
		} catch (KafkaException e) {
			metrics.incrementProducerErrors();
			future.completeExceptionally(e);
			log.error("Failed to send event {}: {}", event.getEventId(), e.getMessage(), e);
		}

		return future;
	}

	/**
	 * Sends an order event synchronously with timeout
	 */
	public RecordMetadata sendSync(OrderEvent event, Duration timeout)
		throws ExecutionException, InterruptedException, TimeoutException {
		return sendAsync(event).get(timeout.toMillis(), TimeUnit.MILLISECONDS);
	}

	/**
	 * Sends multiple events in a batch (transactional)
	 */
	public void sendBatch(Iterable<OrderEvent> events) {
		producer.beginTransaction();
		try {
			for (OrderEvent event : events) {
				sendAsync(event).join();
			}
			producer.commitTransaction();
			log.info("Batch transaction committed successfully");
		} catch (Exception e) {
			log.error("Batch transaction failed, aborting", e);
			producer.abortTransaction();
			throw new KafkaException("Batch send failed", e);
		}
	}

	private Headers createHeaders(OrderEvent event) {
		RecordHeaders headers = new RecordHeaders();
		headers.add("event-id", event.getEventId().toString().getBytes(StandardCharsets.UTF_8));
		headers.add("event-type", event.getEventType().name().getBytes(StandardCharsets.UTF_8));
		headers.add("timestamp", event.getTimestamp().toString().getBytes(StandardCharsets.UTF_8));
		headers.add("content-type", "application/json".getBytes(StandardCharsets.UTF_8));
		headers.add("source", "order-service".getBytes(StandardCharsets.UTF_8));
		return headers;
	}

	private void validateEvent(OrderEvent event) {
		if (event == null) {
			throw new IllegalArgumentException("Event cannot be null");
		}
		if (event.getEventId() == null) {
			event.setEventId(UUID.randomUUID());
		}
		if (event.getTimestamp() == null) {
			event.setTimestamp(Instant.now());
		}
		if (event.getVersion() == null) {
			event.setVersion(1);
		}
	}

	private void handleSendSuccess(OrderEvent event, RecordMetadata metadata,
								   CompletableFuture<RecordMetadata> future) {
		metrics.incrementProducerSuccess();
		log.info("Event sent successfully - eventId: {}, topic: {}, partition: {}, offset: {}",
			event.getEventId(),
			metadata.topic(),
			metadata.partition(),
			metadata.offset()
		);
		future.complete(metadata);
	}

	private void handleSendFailure(OrderEvent event, Exception exception,
								   CompletableFuture<RecordMetadata> future) {
		metrics.incrementProducerErrors();

		if (exception instanceof RetriableException) {
			log.warn("Retriable error sending event {}: {}",
				event.getEventId(), exception.getMessage());
		} else {
			log.error("Non-retriable error sending event {}: {}",
				event.getEventId(), exception.getMessage(), exception);
		}

		future.completeExceptionally(exception);
	}

	/**
	 * Flush any pending messages
	 */
	public void flush() {
		producer.flush();
	}

	@Override
	public void close() {
		if (closed.compareAndSet(false, true)) {
			log.info("Closing OrderEventProducer...");

			try {
				producer.flush();
				producer.close(Duration.ofSeconds(30));
			} catch (Exception e) {
				log.error("Error closing producer", e);
			}

			callbackExecutor.shutdown();
			try {
				if (!callbackExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
					callbackExecutor.shutdownNow();
				}
			} catch (InterruptedException e) {
				callbackExecutor.shutdownNow();
				Thread.currentThread().interrupt();
			}

			log.info("OrderEventProducer closed");
		}
	}
}
