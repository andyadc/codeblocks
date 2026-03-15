package com.andyadc.test.kafka.claude.config;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KafkaConfig {

	private final String bootstrapServers;
	private final String ordersTopic;
	private final String consumerGroupId;

	@Builder.Default
	private final int maxRetries = 3;

	@Builder.Default
	private final long pollTimeoutMs = 1000;

	@Builder.Default
	private final int batchSize = 500;

	@Builder.Default
	private final boolean securityEnabled = false;

	private final String saslJaasConfig;

	public static KafkaConfig development() {
		return KafkaConfig.builder()
			.bootstrapServers("localhost:9092")
			.ordersTopic("orders")
			.consumerGroupId("order-service-dev")
			.build();
	}

	public static KafkaConfig production() {
		return KafkaConfig.builder()
			.bootstrapServers(System.getenv("KAFKA_BOOTSTRAP_SERVERS"))
			.ordersTopic(System.getenv("KAFKA_ORDERS_TOPIC"))
			.consumerGroupId(System.getenv("KAFKA_CONSUMER_GROUP"))
			.maxRetries(5)
			.batchSize(1000)
			.securityEnabled(true)
			.saslJaasConfig(System.getenv("KAFKA_SASL_JAAS_CONFIG"))
			.build();
	}

}
