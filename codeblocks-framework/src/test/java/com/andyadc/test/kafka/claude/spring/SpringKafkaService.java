package com.andyadc.test.kafka.claude.spring;

import com.andyadc.test.kafka.claude.model.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpringKafkaService {

	private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

	public CompletableFuture<SendResult<String, OrderEvent>> sendEvent(
		String topic, OrderEvent event) {

		String key = event.getOrderId().toString();

		return kafkaTemplate.send(topic, key, event)
			.whenComplete((result, ex) -> {
				if (ex != null) {
					log.error("Failed to send event {}: {}",
						event.getEventId(), ex.getMessage(), ex);
				} else {
					log.info("Event sent - eventId: {}, partition: {}, offset: {}",
						event.getEventId(),
						result.getRecordMetadata().partition(),
						result.getRecordMetadata().offset());
				}
			});
	}

	@KafkaListener(
		topics = "${app.kafka.topics.orders}",
		groupId = "${spring.kafka.consumer.group-id}",
		containerFactory = "kafkaListenerContainerFactory"
	)
	@Retryable(
		retryFor = RuntimeException.class,
		maxAttempts = 3,
		backoff = @Backoff(delay = 1000, multiplier = 2)
	)
	public void handleOrderEvent(
		ConsumerRecord<String, OrderEvent> record,
		Acknowledgment acknowledgment) {

		OrderEvent event = record.value();

		log.info("Received event - eventId: {}, partition: {}, offset: {}",
			event.getEventId(),
			record.partition(),
			record.offset());

		try {
			processEvent(event);
			acknowledgment.acknowledge();
			log.debug("Event {} processed and acknowledged", event.getEventId());

		} catch (Exception e) {
			log.error("Error processing event {}", event.getEventId(), e);
			throw e; // Will trigger retry
		}
	}

	private void processEvent(OrderEvent event) {
		// Business logic here
		log.info("Processing order {} with status {}",
			event.getOrderId(), event.getStatus());
	}
}
