package com.andyadc.codeblocks.test.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author andy.an
 * @since 2018/8/23
 */
public class KafkaProducerThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerThread.class);

    private CountDownLatch latch;
    private KafkaProducer<String, String> producer;
    private ProducerRecord<String, String> record;

    public KafkaProducerThread(CountDownLatch latch, KafkaProducer<String, String> producer, ProducerRecord<String, String> record) {
        this.latch = latch;
        this.producer = producer;
        this.record = record;
    }

    @Override
    public void run() {
        producer.send(record, (recordMetadata, e) -> {
            if (e != null) {
                logger.error("Send message occurs exception.", e);
            }
            if (recordMetadata != null) {
                logger.info(String.format("offset:%s, partition:%s",
                        recordMetadata.offset(),
                        recordMetadata.partition()));
            }
            latch.countDown();
        });
    }
}
