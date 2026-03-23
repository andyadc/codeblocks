package com.andyadc.test.kafka.claude.health;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterOptions;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Component
public class KafkaHealthIndicator implements HealthIndicator {

	private final String bootstrapServers;

	public KafkaHealthIndicator(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}

	@Override
	public Health health() {
		Properties props = new Properties();
		props.put("bootstrap.servers", bootstrapServers);
		props.put("request.timeout.ms", 5000);
		props.put("connections.max.idle.ms", 10000);

		try (AdminClient adminClient = AdminClient.create(props)) {
			DescribeClusterOptions options = new DescribeClusterOptions()
				.timeoutMs(5000);
			DescribeClusterResult result = adminClient.describeCluster(options);

			String clusterId = result.clusterId().get(5, TimeUnit.SECONDS);
			int nodeCount = result.nodes().get(5, TimeUnit.SECONDS).size();

			return Health.up()
				.withDetail("clusterId", clusterId)
				.withDetail("nodeCount", nodeCount)
				.withDetail("bootstrapServers", bootstrapServers)
				.build();

		} catch (Exception e) {
			return Health.down()
				.withDetail("error", e.getMessage())
				.withDetail("bootstrapServers", bootstrapServers)
				.build();
		}
	}
}
