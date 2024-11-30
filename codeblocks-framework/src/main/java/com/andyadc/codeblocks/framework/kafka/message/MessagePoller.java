package com.andyadc.codeblocks.framework.kafka.message;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.LongAdder;

public class MessagePoller implements Runnable, InitializingBean, DisposableBean {

	private static final Logger logger = LoggerFactory.getLogger(MessagePoller.class);

	private final LongAdder consumeAdder = new LongAdder();
	private final AtomicBoolean running = new AtomicBoolean(true);
	private final Properties props;
	private MessageDispatcher dispatcher;
	private String[] topics;
	private String groupId;
	private String clientId;
	private long timeout = 3000L;
	private String bootstrapServers;

	public MessagePoller() {
		this.props = getDefaultConfig();
	}

	private static Properties getDefaultConfig() {
		Properties props = new Properties();

		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // 从头开始消费

		return props;
	}

	@Override
	public void run() {
		while (running.get()) {
			KafkaConsumer<String, String> kafkaConsumer = null;
			try {
				kafkaConsumer = new KafkaConsumer<>(props);
				kafkaConsumer.subscribe(Arrays.asList(topics));
				int i = 0;
				while (running.get()) {
					ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(timeout));
					if (records.isEmpty()) {
						continue;
					}
					int curCount = records.count();
					logger.info(curCount + " records polled!");
					dispatcher.doDispatch(kafkaConsumer, records);
					consumeAdder.add(curCount);
				}
			} catch (Exception e) {
				logger.error("KafkaConsumer poll records error.", e);
			} finally {
				if (kafkaConsumer != null) {
					logger.info("Closing message poller ...");
					try {
						kafkaConsumer.close();
					} catch (Throwable e) {
						logger.warn("Closing message poller occurs exception", e);
					}
				}
				running.set(false);
			}
		}
		logger.info("Message poller thread closed.");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("Initializing kafka message poller.");
		long start = System.currentTimeMillis();
		Assert.hasText(bootstrapServers, "Kafka bootstrapServers is null");
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

		if (this.groupId != null) {
			props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		}
		if (this.clientId != null) {
			props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
		}

		if (dispatcher instanceof TransactionalBatchMessageDispatcher) {
			props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // false-禁用自动提交
		} else {
			props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true); // true-自动提交
		}

		Thread thread = new Thread(this);
		thread.setName("MessagePoller");
		thread.setDaemon(true);
		thread.start();

		logger.info("Kafka poller initialization completed in {} ms", (System.currentTimeMillis() - start));
	}

	@Override
	public void destroy() throws Exception {
		logger.info("MessagePoller shutdown initiated...");
		this.running.set(false);
		logger.info("Message consumed {}.", consumeAdder.sum());
		logger.info("MessagePoller completed.");
	}

	public void setTopics(String[] topics) {
		this.topics = topics;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setDispatcher(MessageDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}
}
