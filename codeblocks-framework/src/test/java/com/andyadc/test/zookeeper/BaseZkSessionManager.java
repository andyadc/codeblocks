package com.andyadc.test.zookeeper;

import com.andyadc.codeblocks.framework.zookeeper.ConnectionListener;
import com.andyadc.codeblocks.framework.zookeeper.ZkSessionManager;
import org.apache.zookeeper.ZooKeeper;

/**
 * andy.an
 * 2019/12/25
 */
public class BaseZkSessionManager implements ZkSessionManager {

	private final ZooKeeper zk;

	public BaseZkSessionManager(ZooKeeper zk) {
		this.zk = zk;
	}

	@Override
	public ZooKeeper getZooKeeper() {
		return zk;
	}

	@Override
	public void closeSession() {
		try {
			zk.close();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void addConnectionListener(ConnectionListener listener) {
		//default no-op
	}

	@Override
	public void removeConnectionListener(ConnectionListener listener) {
		//default no-op
	}
}
