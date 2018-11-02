package com.andyadc.codeblocks.test.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author andy.an
 * @since 2018/8/23
 */
public class QuotationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(QuotationConsumer.class);

    private static final String BROKER_LIST = Const.BROKER_SERVER;

    private static Properties configs;

    static {
        configs = initConfig();
    }

    public static void main(String[] args) {
//        autoCommit();
//        manualCommit();
        multiThreadConsumer();
    }

    private static void multiThreadConsumer() {
        Map<String, Object> consumerConfig = new HashMap<>();
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        consumerConfig.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        consumerConfig.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);

        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        for (int i = 0; i < 10; i++) {
            new Thread(new KafkaConsumerThread(consumerConfig, "stock-quotation")).start();
        }
    }

    /**
     * 自动提交
     */
    private static void autoCommit() {
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true); // 设置偏移量自动提交
        configs.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000); // 设置偏移量提交时间间隔

        // 线程不安全
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(configs);
        consumer.subscribe(Arrays.asList("stock-quotation"));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : records) {
                    logger.info(String.format("partition = %d, offset = %d, key= %s, value = %s %n",
                            record.partition(),
                            record.offset(),
                            record.key(),
                            record.value()));
                }
            }
        } catch (Exception e) {
            logger.error("Receive message occurs exception", e);
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }

    /**
     * 手动提交
     */
    private static void manualCommit() {
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // 设置手动提交偏移量
        configs.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 1024); // 为了便于测试，这里设置一次fetch请求取得的数据最大值为1KB,默认是5MB

        // 线程不安全
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(configs);
        consumer.subscribe(Arrays.asList("stock-quotation"));

        int minCommitSize = 10;// 最少处理10条消息后才进行提交
        int icount = 0;// 消息计算器

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : records) {
                    logger.info(String.format("partition = %d, offset = %d, key= %s, value = %s %n",
                            record.partition(),
                            record.offset(),
                            record.key(),
                            record.value()));
                    icount++;
                }

                // 在业务逻辑处理成功后提交偏移量
                if (icount >= minCommitSize) {
                    consumer.commitAsync((map, e) -> {

                        if (null == e) {
                            // 表示偏移量成功提交
                            System.out.println("提交成功");
                        } else {
                            // 表示提交偏移量发生了异常，根据业务进行相关处理
                            System.out.println("发生了异常");
                        }

                    });
                }

                icount = 0;
            }
        } catch (Exception e) {
            logger.error("Receive message occurs exception", e);
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }

    private static Properties initConfig() {
        Properties props = new Properties();
        // Kafka broker列表
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "test");

        // 设置反序列化类
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        return props;
    }
}
