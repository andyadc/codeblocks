package com.andyadc.codeblocks.framework.kafka.logback.keying;

/**
 * Evenly distributes all written log messages over all available kafka partitions.
 * This strategy can lead to unexpected read orders on clients.
 */
public class NoKeyKeyingStrategy implements KeyingStrategy<Object> {

	@Override
	public byte[] createKey(Object e) {
		return null;
	}
}
