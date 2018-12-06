package com.andyadc.codeblocks.framework.kafka.logback.delivery;

import org.apache.kafka.clients.producer.BufferExhaustedException;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.TimeoutException;

/**
 * @author andy.an
 * @since 2018/12/6
 */
public class AsynchronousDeliveryStrategy implements DeliveryStrategy {

    @Override
    public <K, V, E> boolean send(Producer<K, V> producer, ProducerRecord<K, V> record, E event,
                                  FailedDeliveryCallback<E> failedDeliveryCallback) {

        try {
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    failedDeliveryCallback.onFailedDelivery(event, exception);
                }
                System.out.println(String.format("topic: %s, partition: %s, offset: %s, ",
                        metadata.topic(),
                        metadata.offset(),
                        metadata.partition()));
            });

            return true;
        } catch (BufferExhaustedException | TimeoutException e) {
            e.printStackTrace();
            failedDeliveryCallback.onFailedDelivery(event, e);
            return false;
        }
    }
}
