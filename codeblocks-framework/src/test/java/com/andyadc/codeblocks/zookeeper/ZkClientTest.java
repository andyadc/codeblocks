package com.andyadc.codeblocks.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.junit.Test;

import java.util.List;

/**
 * @author andy.an
 * @since 2018/12/20
 */
public class ZkClientTest {

    private static final String zkServers = "www.hw-server.com:2181";

    @Test
    public void testConnect() {
        ZkClient client = new ZkClient(zkServers, 5000);
        List<String> paths = client.getChildren("/");
        System.out.println(paths);

        client.close();
    }
}
