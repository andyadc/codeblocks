package com.andyadc.codeblocks.framework.kafka.logback.keying;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;

import java.nio.ByteBuffer;

/**
 * This strategy uses the HOSTNAME as kafka message key.
 * This is useful because it ensures that all log messages issued by this host will remain in the correct order for any consumer.
 * But this strategy can lead to uneven log distribution for a small number of hosts (compared to the number of partitions).
 *
 * @author andy.an
 * @since 2018/12/7
 */
public class HostNameKeyingStrategy extends ContextAwareBase implements KeyingStrategy<ILoggingEvent>, LifeCycle {

    private byte[] hostnameHash = null;
    private boolean errorWasShown = false;

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        final String hostname = context.getProperty(CoreConstants.HOSTNAME_KEY);
        if (hostname == null) {
            if (!errorWasShown) {
                addError("Hostname could not be found in context. HostNamePartitioningStrategy will not work.");
                errorWasShown = true;
            }
        } else {
            hostnameHash = ByteBuffer.allocate(4).putInt(hostname.hashCode()).array();
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        errorWasShown = false;
    }

    @Override
    public boolean isStarted() {
        return true;
    }

    @Override
    public byte[] createKey(ILoggingEvent iLoggingEvent) {
        return hostnameHash;
    }
}
