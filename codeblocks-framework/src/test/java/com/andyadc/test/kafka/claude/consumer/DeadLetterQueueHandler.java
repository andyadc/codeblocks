package com.andyadc.test.kafka.claude.consumer;

import com.andyadc.test.kafka.claude.config.KafkaConfig;
import com.andyadc.test.kafka.claude.model.OrderEvent;
import com.andyadc.test.kafka.claude.serializer.JsonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Properties;

@Slf4j
public class DeadLetterQueueHandler implements AutoCloseable {

	private final KafkaProducer<String, OrderEvent> producer;
	private final String dlqTopic;

	public DeadLetterQueueHandler(KafkaConfig config) {
		this.dlqTopic = config.getOrdersTopic() + ".dlq";
		this.producer = createProducer(config);
		log.info("DeadLetterQueueHandler initialized for topic: {}", dlqTopic);
	}

	private KafkaProducer<String, OrderEvent> createProducer(KafkaConfig config) {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.RETRIES_CONFIG, 3);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "dlq-producer");
		return new KafkaProducer<>(props);
	}

	public void send(ConsumerRecord<String, OrderEvent> originalRecord, Exception error) {
		RecordHeaders headers = new RecordHeaders();

		// Copy original headers
		originalRecord.headers().forEach(h -> headers.add(h.key(), h.value()));

		// Add DLQ metadata
		headers.add("dlq-reason", error.getMessage().getBytes(StandardCharsets.UTF_8));
		headers.add("dlq-exception-type", error.getClass().getName().getBytes(StandardCharsets.UTF_8));
		headers.add("dlq-stack-trace", getStackTrace(error).getBytes(StandardCharsets.UTF_8));
		headers.add("dlq-timestamp", Instant.now().toString().getBytes(StandardCharsets.UTF_8));
		headers.add("dlq-original-topic", originalRecord.topic().getBytes(StandardCharsets.UTF_8));
		headers.add("dlq-original-partition",
			String.valueOf(originalRecord.partition()).getBytes(StandardCharsets.UTF_8));
		headers.add("dlq-original-offset",
			String.valueOf(originalRecord.offset()).getBytes(StandardCharsets.UTF_8));

		ProducerRecord<String, OrderEvent> dlqRecord = new ProducerRecord<>(
			dlqTopic,
			null,
			originalRecord.timestamp(),
			originalRecord.key(),
			originalRecord.value(),
			headers
		);

		producer.send(dlqRecord, (metadata, ex) -> {
			if (ex != null) {
				log.error("Failed to send message to DLQ: {}", ex.getMessage(), ex);
			} else {
				log.info("Message sent to DLQ - topic: {}, partition: {}, offset: {}",
					metadata.topic(), metadata.partition(), metadata.offset());
			}
		});
	}

	private String getStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

	@Override
	public void close() {
		producer.close();
	}
}
