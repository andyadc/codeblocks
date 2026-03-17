package com.andyadc.test.kafka.claude.spring;

import com.andyadc.test.kafka.claude.model.OrderEvent;
import com.andyadc.test.kafka.claude.serializer.JsonDeserializer;
import com.andyadc.test.kafka.claude.serializer.JsonSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaSpringConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;

	@Bean
	public ProducerFactory<String, OrderEvent> producerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		config.put(ProducerConfig.ACKS_CONFIG, "all");
		config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
		config.put(ProducerConfig.RETRIES_CONFIG, 3);
		config.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
		config.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		config.put(ProducerConfig.LINGER_MS_CONFIG, 5);
		config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		return new DefaultKafkaProducerFactory<>(config);
	}

	@Bean
	public KafkaTemplate<String, OrderEvent> kafkaTemplate() {
		KafkaTemplate<String, OrderEvent> template = new KafkaTemplate<>(producerFactory());
		template.setObservationEnabled(true); // Enable micrometer observations
		return template;
	}

	@Bean
	public ConsumerFactory<String, OrderEvent> consumerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
			ErrorHandlingDeserializer.class);
		config.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS,
			JsonDeserializer.class.getName());
		config.put("value.deserializer.type", OrderEvent.class.getName());
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
		config.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);
		config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000);
		config.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 10000);
		config.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
		return new DefaultKafkaConsumerFactory<>(config);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, OrderEvent>
	kafkaListenerContainerFactory(
		ConsumerFactory<String, OrderEvent> consumerFactory,
		KafkaTemplate<String, OrderEvent> kafkaTemplate) {

		ConcurrentKafkaListenerContainerFactory<String, OrderEvent> factory =
			new ConcurrentKafkaListenerContainerFactory<>();

		factory.setConsumerFactory(consumerFactory);
		factory.setConcurrency(3); // Number of consumer threads
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
		factory.getContainerProperties().setObservationEnabled(true);

		// Configure error handling with DLQ
		DefaultErrorHandler errorHandler = createErrorHandler(kafkaTemplate);
		factory.setCommonErrorHandler(errorHandler);

		return factory;
	}

	private DefaultErrorHandler createErrorHandler(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
		// Dead Letter Publishing Recoverer
		DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
			kafkaTemplate,
			(record, exception) -> new org.apache.kafka.common.TopicPartition(
				record.topic() + ".dlq",
				record.partition()
			)
		);

		// Exponential backoff: starts at 1s, max 10s, max 5 retries
		ExponentialBackOffWithMaxRetries backOff = new ExponentialBackOffWithMaxRetries(5);
		backOff.setInitialInterval(1000L);
		backOff.setMultiplier(2.0);
		backOff.setMaxInterval(10000L);

		DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, backOff);

		// Add non-retryable exceptions
		errorHandler.addNotRetryableExceptions(
			IllegalArgumentException.class,
			NullPointerException.class
		);

		return errorHandler;
	}

	// Batch listener factory for high-throughput scenarios
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, OrderEvent>
	batchKafkaListenerContainerFactory(
		ConsumerFactory<String, OrderEvent> consumerFactory) {

		ConcurrentKafkaListenerContainerFactory<String, OrderEvent> factory =
			new ConcurrentKafkaListenerContainerFactory<>();

		factory.setConsumerFactory(consumerFactory);
		factory.setConcurrency(3);
		factory.setBatchListener(true);
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

		return factory;
	}

}
