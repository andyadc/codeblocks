package com.andyadc.codeblocks.framework.kafka.logback.delivery;

import ch.qos.logback.core.spi.ContextAwareBase;
import org.apache.kafka.clients.producer.BufferExhaustedException;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.TimeoutException;

/**
 * @author andy.an
 * @since 2018/12/6
 */
public class AsynchronousDeliveryStrategy extends ContextAwareBase implements DeliveryStrategy {

    @Override
    public <K, V, E> boolean send(Producer<K, V> producer, ProducerRecord<K, V> record, E event,
                                  FailedDeliveryCallback<E> failedDeliveryCallback) {

        try {
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    failedDeliveryCallback.onFailedDelivery(event, exception);
                }
            });

            return true;
        } catch (BufferExhaustedException | TimeoutException e) {
            failedDeliveryCallback.onFailedDelivery(event, e);
            return false;
        }
    }
}
