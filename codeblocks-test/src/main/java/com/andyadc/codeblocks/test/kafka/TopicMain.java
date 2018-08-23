package com.andyadc.codeblocks.test.kafka;

import kafka.admin.AdminUtils;
import kafka.server.ConfigType;
import kafka.utils.ZkUtils;
import org.apache.kafka.common.security.JaasUtils;

import java.util.Properties;

/**
 * @author andy.an
 * @since 2018/8/23
 */
public class TopicMain {

    /**
     * 连接 Zk
     */
    private static final String ZK_CONNECT = "www.qq-server.com:2181";
    /**
     * session 过期时间
     */
    private static final int SESSION_TIMEOUT = 6000;
    /**
     * 连接超时时间
     */
    private static final int CONNECT_TIMEOUT = 6000;

    public static void main(String[] args) {

        createTopic("stock-quotation", 6, 1, new Properties());
//        deleteTopic("stock-quotation");
    }

    /**
     * 创建主题
     */
    private static void createTopic(String topic, int partition, int replia, Properties props) {
        ZkUtils zkUtils = null;
        try {
            zkUtils = ZkUtils.apply(ZK_CONNECT, SESSION_TIMEOUT, CONNECT_TIMEOUT, JaasUtils.isZkSecurityEnabled());
            if (!AdminUtils.topicExists(zkUtils, topic)) {
                AdminUtils.createTopic(zkUtils, topic, partition, replia, props, AdminUtils.createTopic$default$6());
            } else {
                System.out.println(String.format("Topic [%s] already existed", topic));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (zkUtils != null)
                zkUtils.close();
        }
    }

    /**
     * 修改主题级别配置
     */
    public static void modifyTopicConfig(String topic, Properties props) {
        ZkUtils zkUtils = null;
        try {
            zkUtils = ZkUtils.apply(ZK_CONNECT, SESSION_TIMEOUT, CONNECT_TIMEOUT, JaasUtils.isZkSecurityEnabled());
            Properties curProp = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), topic);
            curProp.putAll(props);
            AdminUtils.changeTopicConfig(zkUtils, topic, curProp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (zkUtils != null)
                zkUtils.close();
        }
    }

    /**
     * 删除主题
     */
    public static void deleteTopic(String topic) {
        ZkUtils zkUtils = null;
        try {
            zkUtils = ZkUtils.apply(ZK_CONNECT, SESSION_TIMEOUT, CONNECT_TIMEOUT, JaasUtils.isZkSecurityEnabled());
            AdminUtils.deleteTopic(zkUtils, topic);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (zkUtils != null)
                zkUtils.close();
        }
    }
}
