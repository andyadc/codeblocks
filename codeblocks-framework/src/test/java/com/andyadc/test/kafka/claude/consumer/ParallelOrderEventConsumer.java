package com.andyadc.test.kafka.claude.consumer;

import com.andyadc.test.kafka.claude.model.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

@Slf4j
public class ParallelOrderEventConsumer implements AutoCloseable {

	private final KafkaConsumer<String, OrderEvent> consumer;
	private final ExecutorService processingExecutor;
	private final Consumer<OrderEvent> eventHandler;
	private final int parallelism;
	private final Map<TopicPartition, Long> partitionOffsets = new ConcurrentHashMap<>();

	public ParallelOrderEventConsumer(Properties props, Consumer<OrderEvent> eventHandler,
									  int parallelism) {
		this.consumer = new KafkaConsumer<>(props);
		this.eventHandler = eventHandler;
		this.parallelism = parallelism;
		this.processingExecutor = new ThreadPoolExecutor(
			parallelism,
			parallelism,
			60L, TimeUnit.SECONDS,
			new LinkedBlockingQueue<>(1000),
			new ThreadPoolExecutor.CallerRunsPolicy()
		);
	}

	public void process(String topic) {
		consumer.subscribe(Collections.singletonList(topic));

		while (true) {
			ConsumerRecords<String, OrderEvent> records = consumer.poll(Duration.ofMillis(100));

			if (records.isEmpty()) {
				continue;
			}

			// Group records by partition for ordering guarantees
			Map<TopicPartition, List<ConsumerRecord<String, OrderEvent>>> byPartition =
				new HashMap<>();

			for (ConsumerRecord<String, OrderEvent> record : records) {
				TopicPartition tp = new TopicPartition(record.topic(), record.partition());
				byPartition.computeIfAbsent(tp, k -> new ArrayList<>()).add(record);
			}

			// Process each partition's records in parallel, but maintain order within partition
			List<CompletableFuture<Void>> futures = new ArrayList<>();

			for (Map.Entry<TopicPartition, List<ConsumerRecord<String, OrderEvent>>> entry :
				byPartition.entrySet()) {

				TopicPartition partition = entry.getKey();
				List<ConsumerRecord<String, OrderEvent>> partitionRecords = entry.getValue();

				CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
					for (ConsumerRecord<String, OrderEvent> record : partitionRecords) {
						try {
							eventHandler.accept(record.value());
							partitionOffsets.put(partition, record.offset() + 1);
						} catch (Exception e) {
							log.error("Error processing record at offset {}",
								record.offset(), e);
							throw e;
						}
					}
				}, processingExecutor);

				futures.add(future);
			}

			// Wait for all partitions to complete
			try {
				CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
					.get(5, TimeUnit.MINUTES);

				// Commit offsets
				commitOffsets();

			} catch (Exception e) {
				log.error("Error during parallel processing", e);
			}
		}
	}

	private void commitOffsets() {
		Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();

		partitionOffsets.forEach((partition, offset) ->
			offsets.put(partition, new OffsetAndMetadata(offset))
		);

		if (!offsets.isEmpty()) {
			consumer.commitSync(offsets);
			partitionOffsets.clear();
		}
	}

	@Override
	public void close() {
		processingExecutor.shutdown();
		try {
			if (!processingExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
				processingExecutor.shutdownNow();
			}
		} catch (InterruptedException e) {
			processingExecutor.shutdownNow();
			Thread.currentThread().interrupt();
		}
		consumer.close();
	}

}
