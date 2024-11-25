package com.andyadc.codeblocks.framework.kafka.message;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class TransactionalBatchMessageDispatcher extends DefaultBatchMessageDispatcher {

	private static final Logger logger = LoggerFactory.getLogger(TransactionalBatchMessageDispatcher.class);

	@Override
	public void doDispatch(KafkaConsumer<String, String> kafkaConsumer, ConsumerRecords<String, String> records) {
		Map<TopicPartition, OffsetAndMetadata> offsetMap = this.doBatchDispatch(records);
		if (!offsetMap.isEmpty()) {
			logger.info("Starting commit offsets ...");
			kafkaConsumer.commitSync(offsetMap);
			logger.info("Commit offsets completed.");
		}
	}

}
