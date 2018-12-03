package com.andyadc.codeblocks.test.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

/**
 * @author andy.an
 * @since 2018/8/23
 */
public class KafkaConsumerThread implements Runnable {

    // 非线程安全
    // 每个线程拥有私有的 KafkaConsumer 实例
    private KafkaConsumer<String, String> consumer;

    public KafkaConsumerThread(Map<String, Object> consumerConfig, String... topics) {
        // 多线程环境下不需要配置 client.id
        Properties props = new Properties();
        props.putAll(consumerConfig);
        this.consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topics));
    }

    @Override
    public void run() {
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> record : records) {
                    // 简单打印出消息内容
                    System.out.printf("threadId = %s, partition = %d, offset = %d, key= %s, value = %s %n",
                            Thread.currentThread().getId(),
                            record.partition(),
                            record.offset(),
                            record.key(),
                            record.value());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
