package com.andyadc.test.kafka.claude.metrics;

import io.micrometer.core.instrument.*;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicLong;

@Getter
public class KafkaMetrics {

	private final MeterRegistry registry;
	private final String prefix;

	// Counters
	private final Counter producerSuccessCounter;
	private final Counter producerErrorCounter;
	private final Counter consumerSuccessCounter;
	private final Counter consumerErrorCounter;
	private final Counter dlqMessageCounter;
	private final Counter commitFailureCounter;

	// Timers
	private final Timer producerLatencyTimer;
	private final Timer consumerLatencyTimer;

	// Gauges
	private final AtomicLong consumerLag = new AtomicLong(0);
	private final AtomicLong lastProcessedTimestamp = new AtomicLong(0);

	// Distribution Summary
	private final DistributionSummary batchSizeSummary;

	public KafkaMetrics(MeterRegistry registry, String componentName) {
		this.registry = registry;
		this.prefix = "kafka." + componentName;

		// Producer metrics
		this.producerSuccessCounter = Counter.builder(prefix + ".produced")
			.description("Number of successfully produced messages")
			.tag("status", "success")
			.register(registry);

		this.producerErrorCounter = Counter.builder(prefix + ".produced")
			.description("Number of failed message productions")
			.tag("status", "error")
			.register(registry);

		this.producerLatencyTimer = Timer.builder(prefix + ".producer.latency")
			.description("Producer latency")
			.publishPercentiles(0.5, 0.95, 0.99)
			.register(registry);

		// Consumer metrics
		this.consumerSuccessCounter = Counter.builder(prefix + ".consumed")
			.description("Number of successfully consumed messages")
			.tag("status", "success")
			.register(registry);

		this.consumerErrorCounter = Counter.builder(prefix + ".consumed")
			.description("Number of failed message consumptions")
			.tag("status", "error")
			.register(registry);

		this.consumerLatencyTimer = Timer.builder(prefix + ".consumer.latency")
			.description("Consumer processing latency")
			.publishPercentiles(0.5, 0.95, 0.99)
			.register(registry);

		// DLQ metrics
		this.dlqMessageCounter = Counter.builder(prefix + ".dlq.messages")
			.description("Number of messages sent to DLQ")
			.register(registry);

		this.commitFailureCounter = Counter.builder(prefix + ".commit.failures")
			.description("Number of offset commit failures")
			.register(registry);

		// Batch size distribution
		this.batchSizeSummary = DistributionSummary.builder(prefix + ".batch.size")
			.description("Distribution of batch sizes")
			.publishPercentiles(0.5, 0.95, 0.99)
			.register(registry);

		// Gauges
		Gauge.builder(prefix + ".consumer.lag", consumerLag, AtomicLong::get)
			.description("Consumer lag in milliseconds")
			.register(registry);

		Gauge.builder(prefix + ".last.processed", lastProcessedTimestamp, AtomicLong::get)
			.description("Timestamp of last processed message")
			.register(registry);
	}

	public void incrementProducerSuccess() {
		producerSuccessCounter.increment();
	}

	public void incrementProducerErrors() {
		producerErrorCounter.increment();
	}

	public void incrementConsumerSuccess() {
		consumerSuccessCounter.increment();
	}

	public void incrementConsumerErrors() {
		consumerErrorCounter.increment();
	}

	public void incrementDlqMessages() {
		dlqMessageCounter.increment();
	}

	public void incrementCommitFailures() {
		commitFailureCounter.increment();
	}

	public void recordBatchSize(int size) {
		batchSizeSummary.record(size);
	}

	public void updateConsumerLag(long lagMs) {
		consumerLag.set(lagMs);
	}

	public void updateLastProcessed(long timestamp) {
		lastProcessedTimestamp.set(timestamp);
	}
}
