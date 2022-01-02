package com.andyadc.codeblocks.framework.kafka.logback.keying;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class NamingKeyingStrategy implements KeyingStrategy<ILoggingEvent> {

    private String keyName;

    @Override
    public byte[] createKey(ILoggingEvent e) {
        return keyName.getBytes();
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }
}
