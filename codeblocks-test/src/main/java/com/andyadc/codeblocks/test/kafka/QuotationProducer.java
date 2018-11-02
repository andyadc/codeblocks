package com.andyadc.codeblocks.test.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author andy.an
 * @since 2018/8/23
 */
public class QuotationProducer {

    private static final Logger logger = LoggerFactory.getLogger(QuotationProducer.class);

    /**
     * 设置实例生产消息的总数
     */
    private static final int MSG_SIZE = 100;
    /**
     * 线程总数
     */
    private static final int THREAD_NUM = 10;
    /**
     * 主题名称
     */
    private static final String TOPIC = "stock-quotation";

    /**
     * Kafka 集群
     */
    private static final String BROKER_LIST = Const.BROKER_SERVER;

    // 线程安全
    private static KafkaProducer<String, String> producer;

    static {
        // 1.构造用于实例化 KafkaProducer 的 Properties 信息
        Properties configs = initConfig();
        // 2.初始化一个 KafkaProducer
        producer = new KafkaProducer<>(configs);
    }

    public static void main(String[] args) throws Exception {
//        singleThreadProducer();
        multiThreadProducer();
    }

    /**
     * 多线程发送
     */
    private static void multiThreadProducer() throws Exception {
        CountDownLatch latch = new CountDownLatch(THREAD_NUM);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUM);
        ProducerRecord<String, String> record;
        StockQuotationInfo quotationInfo;

        try {
            for (int i = 0; i < MSG_SIZE; i++) {
                quotationInfo = createQuotationInfo();
                record = new ProducerRecord<>(TOPIC, null,
                        quotationInfo.getTradeTime(),
                        quotationInfo.getStockCode(),
                        quotationInfo.toString());

                executor.execute(new KafkaProducerThread(latch, producer, record));
            }
            executor.shutdown();
        } catch (Exception e) {
            logger.error("Send message occurs exception", e);
            e.printStackTrace();
        }

        latch.await();
        producer.close();
    }

    /**
     * 单线程发送
     */
    private static void singleThreadProducer() {
        ProducerRecord<String, String> record;
        StockQuotationInfo quotationInfo;

        try {
            int num = 0;
            for (int i = 0; i < MSG_SIZE; i++) {
                quotationInfo = createQuotationInfo();
                record = new ProducerRecord<>(TOPIC, null,
                        quotationInfo.getTradeTime(),
                        quotationInfo.getStockCode(),
                        quotationInfo.toString());

                producer.send(record, (recordMetadata, e) -> {
                    if (e != null) {
                        logger.error("Send message occurs exception.", e);
                    }
                    if (recordMetadata != null) {
                        logger.info(String.format("offset:%s, partition:%s",
                                recordMetadata.offset(),
                                recordMetadata.partition()));
                    }
                });

                if (num++ % 10 == 0) {
                    Thread.sleep(2000L);// 休眠2s
                }
            }
        } catch (Exception e) {
            logger.error("Send message occurs exception", e);
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }

    /**
     * 初始化 Kafka 配置
     */
    private static Properties initConfig() {
        Properties props = new Properties();
        // Kafka broker列表
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        // 设置序列化类
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return props;
    }

    /**
     * 生产股票行情信息
     */
    private static StockQuotationInfo createQuotationInfo() {
        StockQuotationInfo quotationInfo = new StockQuotationInfo();
        // 随机产生1到10之间的整数，然后与600100相加组成股票代码
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        Integer stockCode = 600100 + tlr.nextInt(10);
        // 随机产生一个0到1之间的浮点数
        float random = (float) Math.random();
        // 设置涨跌规则
        if (random / 2 < 0.5) {
            random = -random;
        }
        DecimalFormat decimalFormat = new DecimalFormat(".00");// 设置保存两位有效数字
        quotationInfo.setCurrentPrice(Float.valueOf(decimalFormat.format(11 + random)));// 设置最新价在11元浮动
        quotationInfo.setPreClosePrice(11.80f);// 设置昨日收盘价为固定值
        quotationInfo.setOpenPrice(11.5f);// 设置开盘价
        quotationInfo.setLowPrice(10.5f);// 设置最低价，并不考虑10%限制，
        //以及当前价是否已是最低价
        quotationInfo.setHighPrice(12.5f);// 设置最高价，并不考虑10%限制，
        //以及当前价是否已是最高价
        quotationInfo.setStockCode(stockCode.toString());
        quotationInfo.setTradeTime(System.currentTimeMillis());
        quotationInfo.setStockName("股票-" + stockCode);
        return quotationInfo;
    }
}
