package com.andyadc.codeblocks.framework.kafka.logback;

import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.spi.AppenderAttachable;
import com.andyadc.codeblocks.framework.kafka.logback.delivery.AsynchronousDeliveryStrategy;
import com.andyadc.codeblocks.framework.kafka.logback.delivery.DeliveryStrategy;
import com.andyadc.codeblocks.framework.kafka.logback.keying.KeyingStrategy;
import com.andyadc.codeblocks.framework.kafka.logback.keying.NoKeyKeyingStrategy;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author andy.an
 * @since 2018/12/6
 */
public abstract class KafkaAppenderConfig<E> extends UnsynchronizedAppenderBase<E> implements AppenderAttachable<E> {

    protected String topic = null;
    protected Encoder<E> encoder = null;

    protected KeyingStrategy<? super E> keyingStrategy = null;
    protected DeliveryStrategy deliveryStrategy;

    protected Integer partition = null;

    protected Map<String, Object> producerConfig = new HashMap<>();

    protected boolean checkPrerequisites() {
        boolean errorFree = true;

        if (producerConfig.get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG) == null) {
            addError("No \"" + ProducerConfig.BOOTSTRAP_SERVERS_CONFIG + "\" set for the appender named [\""
                    + name + "\"].");
            errorFree = false;
        }

        if (topic == null) {
            addError("No topic set for the appender named [\"" + name + "\"].");
            errorFree = false;
        }

        if (encoder == null) {
            addError("No encoder set for the appender named [\"" + name + "\"].");
            errorFree = false;
        }

        if (keyingStrategy == null) {
            addInfo("No explicit keyingStrategy set for the appender named [\"" + name + "\"]. Using default NoKeyKeyingStrategy.");
            keyingStrategy = new NoKeyKeyingStrategy();
        }

        if (deliveryStrategy == null) {
            addInfo("No explicit deliveryStrategy set for the appender named [\"" + name + "\"]. Using default asynchronous strategy.");
            deliveryStrategy = new AsynchronousDeliveryStrategy();
        }

        return errorFree;
    }

    public void addProducerConfig(String keyValue) {
        String[] kv = keyValue.split("=", 2);
        if (kv.length == 2) {
            addProducerConfigValue(kv[0], kv[1]);
        }
    }

    public void addProducerConfigValue(String key, Object value) {
        this.producerConfig.put(key, value);
    }

    public void setPartition(Integer partition) {
        this.partition = partition;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setEncoder(Encoder<E> encoder) {
        this.encoder = encoder;
    }

    public void setKeyingStrategy(KeyingStrategy<? super E> keyingStrategy) {
        this.keyingStrategy = keyingStrategy;
    }

    public void setDeliveryStrategy(DeliveryStrategy deliveryStrategy) {
        this.deliveryStrategy = deliveryStrategy;
    }
}
