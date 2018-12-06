package com.andyadc.codeblocks.framework.kafka.logback.keying;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * A strategy that can create byte array key for a given {@link ILoggingEvent}.
 *
 * @author andy.an
 * @since 2018/12/6
 */
@FunctionalInterface
public interface KeyingStrategy<E> {
    /**
     * creates a byte array key for the given {@link ch.qos.logback.classic.spi.ILoggingEvent}
     *
     * @param e the logging event
     * @return a key
     */
    byte[] createKey(E e);
}
