package com.andyadc.codeblocks.framework.idgen.snowflake;

import com.andyadc.codeblocks.framework.idgen.IDGenPropertyFactory;
import com.andyadc.codeblocks.kit.JsonUtil;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * andy.an
 */
public class SnowflakeZookeeperHolder {

    private static final Logger logger = LoggerFactory.getLogger(SnowflakeZookeeperHolder.class);
    private static final String CURR_NODE_NAME = IDGenPropertyFactory.getProperties().getProperty("idgen.name");
    private static final String PREFIX_ZK_PATH = "/idgen/snowflake/" + CURR_NODE_NAME;
    private static final String PATH_PERSIST = PREFIX_ZK_PATH + "/persist";//保存所有数据持久的永久节点
    private static final String PROP_PATH = System.getProperty("java.io.tmpdir") + File.separator + CURR_NODE_NAME + "/conf/{port}/workerID.properties";
    private String zk_AddressNode = null;//保存自身的key  ip:port-000000001
    private String listenAddress;//保存自身的key ip:port
    private String ip;
    private String port;
    private String connectionString;
    private long lastUpdateTime;
    private int workerID;

    public SnowflakeZookeeperHolder(String ip, String port, String connectionString) {
        this.ip = ip;
        this.port = port;
        this.listenAddress = ip + ":" + port;
        this.connectionString = connectionString;
    }

    public boolean init() {
        try {
            CuratorFramework curator = createWithOptions(connectionString, new RetryUntilElapsed(1000, 4), 10000, 6000);
            curator.start();
            Stat stat = curator.checkExists().forPath(PATH_PERSIST);
            if (stat == null) {
                //不存在根节点,机器第一次启动,创建/snowflake/ip:port-000000000,并上传数据
                zk_AddressNode = createNode(curator);
                //worker id 默认是0
                updateLocalWorkerID(workerID);
                //定时上报本机时间给永久节点
                ScheduledUploadData(curator, zk_AddressNode);
                return true;
            } else {
                Map<String, Integer> nodeMap = Maps.newHashMap();
                Map<String, String> realNode = Maps.newHashMap();
                //存在根节点,先检查是否有属于自己的根节点
                List<String> keys = curator.getChildren().forPath(PATH_PERSIST);
                for (String key : keys) {
                    String[] nodeKey = key.split("-");
                    realNode.put(nodeKey[0], key);
                    nodeMap.put(nodeKey[0], Integer.parseInt(nodeKey[1]));
                }
                Integer workerid = nodeMap.get(listenAddress);
                if (workerid != null) {
                    //有自己的节点,zk_AddressNode=ip:port
                    zk_AddressNode = PATH_PERSIST + "/" + realNode.get(listenAddress);
                    workerID = workerid;//启动worder时使用会使用
                    if (!checkInitTimeStamp(curator, zk_AddressNode))
                        throw new RuntimeException("init timestamp check error,forever node timestamp gt this node time");
                    //准备创建临时节点
                    doService(curator);
                    updateLocalWorkerID(workerID);
                    logger.info("[Old NODE] find forever node have this endpoint ip-{} port-{} workid-{} childnode and start SUCCESS", ip, port, workerID);
                } else {
                    //表示新启动的节点,创建持久节点 ,不用check时间
                    String newNode = createNode(curator);
                    zk_AddressNode = newNode;
                    String[] nodeKey = newNode.split("-");
                    workerID = Integer.parseInt(nodeKey[1]);
                    doService(curator);
                    updateLocalWorkerID(workerID);
                    logger.info("[New NODE] can not find node on forever node that endpoint ip-{} port-{} workid-{},create own node on forever node and start SUCCESS ", ip, port, workerID);
                }

            }
        } catch (Exception e) {
            logger.error("Start node error {}", e);
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(new File(PROP_PATH.replace("{port}", port + ""))));
                workerID = Integer.valueOf(properties.getProperty("workerID"));
                logger.warn("START FAILED ,use local node file properties workerID-{}", workerID);
            } catch (IOException e1) {
                logger.error("Read file error ", e1);
                return false;
            }
        }
        return true;
    }

    private void doService(CuratorFramework curator) {
        ScheduledUploadData(curator, zk_AddressNode);
    }

    private void ScheduledUploadData(final CuratorFramework curator, final String zk_AddressNode) {
        Executors.newSingleThreadScheduledExecutor((r) -> {
            Thread thread = new Thread(r, "schedule-upload-time");
            thread.setDaemon(true);
            return thread;
        }).scheduleWithFixedDelay(() -> updateNewData(curator, zk_AddressNode), 1L, 3L, TimeUnit.SECONDS)
        ;
    }

    /**
     * 在节点文件系统上缓存一个workid值,zk失效,机器重启时保证能够正常启动
     *
     * @param workerID workerID
     */
    private void updateLocalWorkerID(int workerID) {
        File localConfFile = new File(PROP_PATH.replace("{port}", port));
        boolean exists = localConfFile.exists();
        logger.info("File exists status is {}", exists);
        if (exists) {
            try {
                FileUtils.writeStringToFile(localConfFile, "workerID=" + workerID, StandardCharsets.UTF_8, false);
                logger.info("Update file cache workerID is {}", workerID);
            } catch (IOException e) {
                logger.error("Update file cache error ", e);
            }
        } else {
            //不存在文件,父目录页肯定不存在
            boolean mkdirs = localConfFile.getParentFile().mkdirs();
            logger.info("Init local file cache create parent dis status is {}, worker id is {}", mkdirs, workerID);
            if (mkdirs) {
                try {
                    if (localConfFile.createNewFile()) {
                        FileUtils.writeStringToFile(localConfFile, "workerID=" + workerID, StandardCharsets.UTF_8, false);
                        logger.info("Local file cache workerID is {}", workerID);
                    }
                } catch (IOException e) {
                    logger.warn("craete workerID conf file error", e);
                }
            } else {
                logger.warn("create parent dir error===");
            }
        }
    }

    private boolean checkInitTimeStamp(CuratorFramework curator, String zk_AddressNode) throws Exception {
        byte[] bytes = curator.getData().forPath(zk_AddressNode);
        Endpoint endPoint = parseEndpointData(new String(bytes));
        //该节点的时间不能小于最后一次上报的时间
        return !(endPoint.getTimestamp() > System.currentTimeMillis());
    }

    private String createNode(CuratorFramework curator) throws Exception {
        return curator.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .forPath(PATH_PERSIST + "/" + listenAddress + '-', buildEndpointData().getBytes());
    }

    private void updateNewData(CuratorFramework curator, String path) {
        if (System.currentTimeMillis() < lastUpdateTime) {
            return;
        }
        try {
            curator.setData().forPath(path, buildEndpointData().getBytes());
        } catch (Exception e) {
            logger.info("Update init data error path is {} error is {}", path, e);
        }
        lastUpdateTime = System.currentTimeMillis();
    }

    private CuratorFramework createWithOptions(String connectionString,
                                               RetryPolicy retryPolicy,
                                               int connectionTimeoutMs,
                                               int sessionTimeoutMs) {
        return CuratorFrameworkFactory.builder().connectString(connectionString)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                .build();
    }

    private String buildEndpointData() {
        Endpoint endpoint = new Endpoint(ip, port, System.currentTimeMillis());
        return JsonUtil.toJSONString(endpoint);
    }

    private Endpoint parseEndpointData(String data) {
        return JsonUtil.parseObject(data, Endpoint.class);
    }

    public int getWorkerID() {
        return workerID;
    }

    public void setWorkerID(int workerID) {
        this.workerID = workerID;
    }

    static class Endpoint {
        private String ip;
        private String port;
        private long timestamp;

        public Endpoint() {
        }

        public Endpoint(String ip, String port, long timestamp) {
            this.ip = ip;
            this.port = port;
            this.timestamp = timestamp;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
