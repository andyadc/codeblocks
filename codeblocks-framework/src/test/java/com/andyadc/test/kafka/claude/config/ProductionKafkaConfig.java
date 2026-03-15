package com.andyadc.test.kafka.claude.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

@Slf4j
public class ProductionKafkaConfig {

	/**
	 * Production-ready producer configuration
	 */
	public static Properties getProducerConfig(String bootstrapServers, String clientId) {
		Properties props = new Properties();

		// Connection
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);

		// Reliability - MUST HAVE for production
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
		props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
		props.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
		props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000);

		// Performance tuning
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
		props.put(ProducerConfig.LINGER_MS_CONFIG, 5);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");

		// Timeouts
		props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
		props.put(ProducerConfig.METADATA_MAX_AGE_CONFIG, 300000);

		// Monitoring
		props.put(ProducerConfig.METRICS_RECORDING_LEVEL_CONFIG, "INFO");

		return props;
	}

	/**
	 * Production-ready consumer configuration
	 */
	public static Properties getConsumerConfig(String bootstrapServers, String groupId,
											   String clientId) {
		Properties props = new Properties();

		// Connection
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);

		// Offset management - CRITICAL for production
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		// Session management
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000);
		props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 10000);
		props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);

		// Fetch configuration
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
		props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1);
		props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 500);
		props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 1048576);

		// Isolation level for exactly-once
		props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

		// Partition assignment
		props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG,
			"org.apache.kafka.clients.consumer.CooperativeStickyAssignor");

		return props;
	}

	/**
	 * Security configuration for SSL/SASL
	 */
	public static void addSecurityConfig(Properties props, SecurityConfig security) {
		props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, security.getProtocol());

		if (security.isSslEnabled()) {
			props.put("ssl.truststore.location", security.getTruststoreLocation());
			props.put("ssl.truststore.password", security.getTruststorePassword());
			props.put("ssl.keystore.location", security.getKeystoreLocation());
			props.put("ssl.keystore.password", security.getKeystorePassword());
			props.put("ssl.key.password", security.getKeyPassword());
		}

		if (security.isSaslEnabled()) {
			props.put("sasl.mechanism", security.getSaslMechanism());
			props.put("sasl.jaas.config", security.getSaslJaasConfig());
		}
	}

	@lombok.Data
	@lombok.Builder
	public static class SecurityConfig {
		private String protocol;
		private boolean sslEnabled;
		private String truststoreLocation;
		private String truststorePassword;
		private String keystoreLocation;
		private String keystorePassword;
		private String keyPassword;
		private boolean saslEnabled;
		private String saslMechanism;
		private String saslJaasConfig;
	}

}
