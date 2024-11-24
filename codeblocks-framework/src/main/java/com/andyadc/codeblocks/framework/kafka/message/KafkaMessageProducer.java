package com.andyadc.codeblocks.framework.kafka.message;

import com.andyadc.codeblocks.framework.message.Message;
import com.andyadc.codeblocks.framework.message.MessageConverter;
import com.andyadc.codeblocks.framework.message.MessageProducer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Future;

public class KafkaMessageProducer implements MessageProducer, InitializingBean, DisposableBean {

	private static final Logger logger = LoggerFactory.getLogger(KafkaMessageProducer.class);
	private final Properties props;
	private Producer<String, String> producer;
	private String bootstrapServers;

	public KafkaMessageProducer() {
		this.props = getDefaultConfig();
	}

	private static Properties getDefaultConfig() {
		Properties props = new Properties();
		props.put(ProducerConfig.ACKS_CONFIG, "all"); // 确保消息可靠性
		props.put(ProducerConfig.RETRIES_CONFIG, 3); // 重试次数
		props.put(ProducerConfig.LINGER_MS_CONFIG, 10); // 批处理延迟
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return props;
	}

	@Override
	public void send(String topic, Message<?> message) {
		try {
			Future<RecordMetadata> future = sendToKafka(topic, null, null, message);
			RecordMetadata metadata = future.get();
			logger.info("{}", metadata.toString());
		} catch (Exception e) {
			logger.error("sendToKafka error.", e);
			throw new RuntimeException(e);
		}
	}

	private Future<RecordMetadata> sendToKafka(String topic, Integer partition, String key, Message<?> message) {
		if (message.getTimestamp() == null) {
			message.setTimestamp(System.currentTimeMillis() + "");
		}
		if (message.getMessageId() == null) {
			message.setMessageId(UUID.randomUUID().toString().replace("-", ""));
		}
		String value = MessageConverter.toJsonString(message);
		ProducerRecord<String, String> record = new ProducerRecord<>(topic, partition, key, value);
		logger.info("Prepare to send message to broker. topic: {}, key: {}, value: {}, partition: {}", topic, key, value, partition);
		return producer.send(record);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("Initializing kafka producer");
		long start = System.currentTimeMillis();
		Assert.hasText(bootstrapServers, "Kafka bootstrapServers is null");
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		producer = new KafkaProducer<>(props);
		logger.info("Kafka producer initialization completed in {} ms", (System.currentTimeMillis() - start));
	}

	@Override
	public void destroy() throws Exception {
		logger.info("Kafka producer Close initiated...");
		if (this.producer != null) {
			producer.close();
		}
		logger.info("Kafka producer Close completed");
	}

	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}

}
