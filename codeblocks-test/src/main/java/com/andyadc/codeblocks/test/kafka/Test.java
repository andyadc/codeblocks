package com.andyadc.codeblocks.test.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @author andy.an
 * @since 2018/8/17
 */
public class Test {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", Const.BROKER_SERVER);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);

        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        String topic = "test";
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, "This is a message!");

        producer.send(record, (metadata, e) ->
                System.out.printf("topic: %s, partition: %s, offset: %s, %n",
					metadata.topic(),
					metadata.offset(),
					metadata.partition())
        );

        producer.close();
    }
}
