package com.andyadc.test.kafka.claude.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {

	private UUID eventId;
	private UUID orderId;
	private String customerId;
	private EventType eventType;
	private OrderStatus status;
	private BigDecimal totalAmount;
	private String currency;
	private Map<String, Object> metadata;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Instant timestamp;

	private Integer version;

	public enum EventType {
		ORDER_CREATED,
		ORDER_UPDATED,
		ORDER_CONFIRMED,
		ORDER_SHIPPED,
		ORDER_DELIVERED,
		ORDER_CANCELLED
	}

	public enum OrderStatus {
		PENDING,
		CONFIRMED,
		PROCESSING,
		SHIPPED,
		DELIVERED,
		CANCELLED
	}

}
