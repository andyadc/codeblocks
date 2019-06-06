package com.andyadc.codeblocks.framework.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author andy.an
 * @since 2019/2/18
 */
public interface ZkCommand<T> {

    T execute(ZooKeeper zk) throws KeeperException, InterruptedException;
}
