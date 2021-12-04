package com.andyadc.codeblocks.test.kafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 自定义分区器
 * <code>
 * properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, StockPartitionor.class.getName());
 * </code>
 */
public class StockPartitionor implements Partitioner {

	private static final Logger logger = LoggerFactory.getLogger(StockPartitionor.class);

	/**
	 * 分区数
	 */
	private static final Integer PARTITIONS = 6;

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		if (key == null) {
			return 0;
		}
		String stockCode = String.valueOf(key);
		try {
			return Integer.parseInt(stockCode.substring(stockCode.length() - 2)) % PARTITIONS;
		} catch (NumberFormatException e) {
			logger.error("Parse message key occurs exception, key: " + stockCode, e);
			return 0;
		}
	}

	@Override
	public void close() {
	}

	@Override
	public void configure(Map<String, ?> configs) {
	}
}
