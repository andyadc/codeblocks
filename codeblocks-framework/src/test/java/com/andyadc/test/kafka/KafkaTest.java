package com.andyadc.test.kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * Kafka version is 2.1.0
 *
 * @author andy.an
 * @since 2018/12/4
 */
public class KafkaTest {

	private static final String BROKER_SERVER = "localhost:9092";

	private static final String[] TEST_TOPICS = {"test"};

	private static AdminClient adminClient;
	private static KafkaProducer<String, String> producer;

	@BeforeAll
	public static void before() {
		adminClient = AdminClient.create(getTopicProps());
		producer = new KafkaProducer<>(getProducerProps());
	}

	/**
	 * Producer properties
	 */
	private static Properties getProducerProps() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_SERVER);
		props.put(ProducerConfig.ACKS_CONFIG, "-1"); // 高可靠性要求则应该设置成"all" 或 "-1"
		props.put(ProducerConfig.RETRIES_CONFIG, 3); // 启用重试机制
		props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1); // 避免消息重排序
		props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "zstd"); //开启压缩 ('gzip', 'snappy', 'lz4', 'zstd')

		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
			"org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
			"org.apache.kafka.common.serialization.StringSerializer");

		List<String> interceptors = new ArrayList<>(2);
		interceptors.add(MyProducerInterceptor.class.getName());
		props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, interceptors);

		return props;
	}

	/**
	 * Consumer properties
	 */
	private static Properties getConsumerProps() {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_SERVER);

		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

		List<String> interceptors = new ArrayList<>(2);
		interceptors.add(MyConsumerInterceptor.class.getName());
		props.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, interceptors);

		return props;
	}

	/**
	 * Topic properties
	 */
	private static Properties getTopicProps() {
		Properties props = new Properties();
		props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_SERVER);

		return props;
	}

	@AfterAll
	public static void after() {
		adminClient.close();
		producer.close();
	}

	/**
	 * Send message
	 */
	@Test
	public void testProducer() {
		String topic = TEST_TOPICS[0];
		String message = "This is a message!";
		ProducerRecord<String, String> record = new ProducerRecord<>(topic, null, message);

		producer.send(record, (metadata, e) ->
			System.out.printf("topic: %s, partition: %s, offset: %s, %n",
				metadata.topic(),
				metadata.partition(),
				metadata.offset())
		);
	}

	/**
	 * Consumer Auto commit
	 */
	@Test
	public void testAutoConsumer() {
		Properties props = getConsumerProps();
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
		props.put(ConsumerConfig.CLIENT_ID_CONFIG, "test");

		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true); // 设置偏移量自动提交
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000); // 设置偏移量提交时间间隔

		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList(TEST_TOPICS));

		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
			for (ConsumerRecord<String, String> record : records) {
				print(record);
			}
		}
	}

	/**
	 * Consumer Not auto commit
	 */
	@Test
	public void testManualConsumer() {
		Properties props = getConsumerProps();
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
		props.put(ConsumerConfig.CLIENT_ID_CONFIG, "test");

		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // 设置手动提交偏移量
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1024); // 为了便于测试，这里设置一次fetch请求取得的数据最大值为1KB,默认是5MB

		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList(TEST_TOPICS));

		int minCommitSize = 10;// 最少处理10条消息后才进行提交
		int icount = 0;// 消息计算器

		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
			for (ConsumerRecord<String, String> record : records) {
				print(record);

				icount++;
			}

			// 在业务逻辑处理成功后提交偏移量
			if (icount >= minCommitSize) {
				consumer.commitAsync((map, e) -> {
					if (null == e) {
						System.out.println("提交成功"); // 表示偏移量成功提交
					} else {
						System.out.println("发生了异常"); // 表示提交偏移量发生了异常，根据业务进行相关处理
					}
				});
			}

			icount = 0;
		}
	}

	private void print(ConsumerRecord record) {
		System.out.printf("topic= %s, partition= %d, offset= %d, key= %s, value= %s %n",
			record.topic(),
			record.partition(),
			record.offset(),
			record.key(),
			record.value());
	}

	/**
	 * List all topics
	 * [__consumer_offsets] is internal topic
	 */
	@Test
	public void testListTopic() throws Exception {
		ListTopicsOptions options = new ListTopicsOptions();
		options.listInternal(true);
		ListTopicsResult result = adminClient.listTopics(options);
		KafkaFuture<Set<String>> future = result.names();
		Set<String> names = future.get();

		System.out.println(names);
	}

	/**
	 * Create topic
	 */
	@Test
	public void testCreateTopic() {
		int partition = 3;
		short replia = 1;

		List<NewTopic> topicList = new ArrayList<>();
		for (String topic : TEST_TOPICS) {
			NewTopic newTopic = new NewTopic(topic, partition, replia);
			topicList.add(newTopic);
		}

		CreateTopicsResult createTopicsResult = adminClient.createTopics(topicList);
		Map<String, KafkaFuture<Void>> values = createTopicsResult.values();
		for (Map.Entry<String, KafkaFuture<Void>> futureEntry : values.entrySet()) {
			System.out.println(futureEntry.getKey() + " - " + futureEntry.getValue().isDone());
		}
	}

	/**
	 * Delete topic
	 */
	@Test
	public void testDeleteTopic() {
		DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Arrays.asList(TEST_TOPICS));
		System.out.println(deleteTopicsResult);
	}

	/**
	 * 监控给定消费者组的 Lag 值
	 */
	public static Map<TopicPartition, Long> lagOf(String groupID, String bootstrapServers) throws TimeoutException {
		Properties props = new Properties();
		props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		try (AdminClient client = AdminClient.create(props)) {
			ListConsumerGroupOffsetsResult result = client.listConsumerGroupOffsets(groupID);
			try {
				Map<TopicPartition, OffsetAndMetadata> consumedOffsets = result.partitionsToOffsetAndMetadata().get(10, TimeUnit.SECONDS);
				props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // 禁止自动提交位移
				props.put(ConsumerConfig.GROUP_ID_CONFIG, groupID);
				props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
				props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
				try (final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
					Map<TopicPartition, Long> endOffsets = consumer.endOffsets(consumedOffsets.keySet());
					return endOffsets.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(),
						entry -> entry.getValue() - consumedOffsets.get(entry.getKey()).offset()));
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				// 处理中断异常
				// ...
				return Collections.emptyMap();
			} catch (ExecutionException e) {
				// 处理 ExecutionException
				// ...
				return Collections.emptyMap();
			} catch (TimeoutException e) {
				throw new TimeoutException("Timed out when getting lag for consumer group " + groupID);
			}
		}
	}

}
