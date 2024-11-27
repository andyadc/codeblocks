package com.andyadc.codeblocks.framework.message;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public abstract class Message<T> {

	private String messageId;
	private Long timestamp;
	private String eventType;

	public abstract T getBody();

	public abstract void setBody(T body);

	public String getSendTime() {
		if (timestamp != null) {
			LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
			return dateTime.toString();
		}
		return null;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	@Override
	public String toString() {
		return "Message{" +
			"messageId=" + messageId +
			", timestamp=" + timestamp +
			", eventType=" + eventType +
			'}';
	}

}
