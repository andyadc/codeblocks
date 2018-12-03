package com.andyadc.codeblocks.test.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.KafkaFuture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author andy.an
 * @since 2018/8/23
 */
public class TopicMain {

    public static void main(String[] args) throws Exception {

//        createTopic("test", 3, 1);
//        deleteTopic("test");

        listTopics();
    }

    private static Properties getDefaultProps() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, Const.BROKER_SERVER);

        return props;
    }

    /**
     * 所有的 Topic
     * [__consumer_offsets]为内部 Topic
     */
    private static void listTopics() throws Exception {
        Properties props = getDefaultProps();
        AdminClient client = KafkaAdminClient.create(props);

        ListTopicsOptions options = new ListTopicsOptions();
        options.listInternal(true);
        ListTopicsResult result = client.listTopics(options);
        KafkaFuture<Set<String>> future = result.names();
        Set<String> names = future.get();

        System.out.println(names);
        client.close();
    }

    /**
     * 创建 Topic
     */
    private static void createTopic(int partition, int replia, String... topics) {
        Properties props = getDefaultProps();
        AdminClient client = KafkaAdminClient.create(props);

        List<NewTopic> topicList = new ArrayList<>();
        for (String topic : topics) {
            NewTopic newTopic = new NewTopic(topic, partition, (short) replia);
            topicList.add(newTopic);
        }

        CreateTopicsResult createTopicsResult = client.createTopics(topicList);
        Map<String, KafkaFuture<Void>> values = createTopicsResult.values();
        for (Map.Entry<String, KafkaFuture<Void>> futureEntry : values.entrySet()) {
            System.out.println(futureEntry.getKey() + " - " + futureEntry.getValue().isDone());
        }

        client.close();
    }

    /**
     * 删除 Topic
     */
    private static void deleteTopic(String... topics) {
        Properties props = getDefaultProps();
        AdminClient client = KafkaAdminClient.create(props);
        DeleteTopicsResult deleteTopicsResult = client.deleteTopics(Arrays.asList(topics));
        System.out.println(deleteTopicsResult);

        client.close();
    }
}
