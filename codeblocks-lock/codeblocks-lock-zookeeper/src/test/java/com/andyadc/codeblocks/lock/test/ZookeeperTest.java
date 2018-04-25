package com.andyadc.codeblocks.lock.test;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

/**
 * @author andy.an
 * @since 2018/4/25
 */
public class ZookeeperTest {

    private static final String ZK_SERVER = "www.jd-server.com:2181";

    @Test
    public void testConnect() throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper(ZK_SERVER, 3000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {

            }
        });

        System.out.println(zooKeeper.getSessionId());

        zooKeeper.close();
    }
}
