package com.andyadc.codeblocks.framework.kafka.message;

import com.andyadc.codeblocks.framework.message.Message;
import com.andyadc.codeblocks.framework.message.MessageConverter;
import com.andyadc.codeblocks.framework.message.MessageException;
import com.andyadc.codeblocks.framework.message.MessageProducer;
import org.apache.kafka.clients.producer.Callback;
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
	private String clientId;

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
		sendToKafka(topic, null, null, message, true);
	}

	@Override
	public void syncSend(String topic, Message<?> message) {
		sendToKafka(topic, null, null, message, false);
	}

	private void sendToKafka(String topic, Integer partition, String key, Message<?> message, Boolean isAsync) {
		if (message.getTimestamp() == null) {
			message.setTimestamp(System.currentTimeMillis());
		}
		if (message.getMessageId() == null) {
			message.setMessageId(UUID.randomUUID().toString().replace("-", ""));
		}
		String value = MessageConverter.toJsonString(message);
		ProducerRecord<String, String> record = new ProducerRecord<>(topic, partition, key, value);
		logger.info("Prepare to send message to broker. topic: {}, key: {}, value: {}, partition: {}", topic, key, value, partition);

		long sendTime = System.currentTimeMillis();
		if (isAsync) {
			producer.send(record, new sentCallback(sendTime, message.getMessageId(), value));
		} else {
			RecordMetadata metadata = null;
			try {
				Future<RecordMetadata> future = producer.send(record);
				metadata = future.get();
			} catch (Exception e) {
				logger.error("sendToKafka error", e);
				throw new MessageException("Kafka send message error", e);
			} finally {
				long elapsedTime = System.currentTimeMillis() - sendTime;
				if (metadata == null) {
					logger.info("Sync send message error in {}", elapsedTime);
				} else {
					logger.info("Sync send message messageId: {} send to topic: {}, partition {}, offset: {}, in {}\n",
						message.getMessageId(), metadata.topic(), metadata.partition(), metadata.offset(), elapsedTime);
				}
			}

		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("Initializing kafka producer");
		long start = System.currentTimeMillis();
		Assert.hasText(bootstrapServers, "Kafka bootstrapServers is null");
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		if (this.clientId != null) {
			props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
		}
		producer = new KafkaProducer<>(props);
		logger.info("Kafka producer initialization completed in {} ms", (System.currentTimeMillis() - start));
	}

	@Override
	public void destroy() throws Exception {
		logger.info("Kafka producer Close initiated...");
		if (this.producer != null) {
			producer.flush();
			producer.close();
		}
		logger.info("Kafka producer Close completed");
	}

	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	static class sentCallback implements Callback {

		/* 开始发送消息的时间戳 */
		private final long sendTime;
		private final String messageId;
		private final String message;

		public sentCallback(long sendTime, String messageId, String message) {
			this.sendTime = sendTime;
			this.messageId = messageId;
			this.message = message;
		}

		@Override
		public void onCompletion(RecordMetadata metadata, Exception e) {
			long elapsedTime = System.currentTimeMillis() - sendTime;
			if (metadata != null) {
				logger.info("Async send message messageId: {} send to topic: {}, partition {}, offset: {}, in {}\n",
					messageId, metadata.topic(), metadata.partition(), metadata.offset(), elapsedTime);
			} else {
				logger.error("Async send message error. messageId: {}", messageId, e);
			}
		}
	}

}
