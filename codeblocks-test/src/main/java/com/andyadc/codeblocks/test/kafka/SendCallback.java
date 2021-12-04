package com.andyadc.codeblocks.test.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class SendCallback implements Callback {

	private static final Logger logger = LoggerFactory.getLogger(SendCallback.class);

	@Override
	public void onCompletion(RecordMetadata recordMetadata, Exception e) {
		if (null != recordMetadata) {
			logger.info("Send success! partition={}, offset={}, topic={}, timestamp={}",
				recordMetadata.offset(),
				recordMetadata.partition(),
				recordMetadata.topic(),
				recordMetadata.timestamp()
			);
		} else {
			logger.error("Send error!", e);
		}
	}
}
