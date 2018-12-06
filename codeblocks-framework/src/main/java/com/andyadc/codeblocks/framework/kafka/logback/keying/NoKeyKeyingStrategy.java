package com.andyadc.codeblocks.framework.kafka.logback.keying;

/**
 * Evenly distributes all written log messages over all available kafka partitions.
 * This strategy can lead to unexpected read orders on clients.
 *
 * @author andy.an
 * @since 2018/12/6
 */
public class NoKeyKeyingStrategy implements KeyingStrategy<Object> {

    @Override
    public byte[] createKey(Object e) {
        return new byte[0];
    }
}
