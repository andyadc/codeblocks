package framework.test.zookeeper.locks;

import com.andyadc.codeblocks.framework.zookeeper.ZkSessionManager;
import com.andyadc.codeblocks.framework.zookeeper.locks.ReentrantZkLock;
import framework.test.zookeeper.BaseZkSessionManager;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.locks.Lock;

/**
 * andy.an
 * 2019/12/25
 */
public class ReentrantZkLockTestShadow {

	private static final String zkServers = "www.qq-server.com:2181";
	private static final String baseLockPath = "/test-locks";
	private static final int timeout = 2000;

	private static ZooKeeper zk;
	private static ZkSessionManager zkSessionManager;

	@Before
	public void setup() throws Exception {
		zk = newZookeeper();

//		ZkUtils.recursiveSafeDelete(zk, baseLockPath, -1);
//		zk.create(baseLockPath, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

		zkSessionManager = new BaseZkSessionManager(zk);
	}

	@Test
	public void testLock() {
		Lock lock = new ReentrantZkLock(baseLockPath, zkSessionManager);

		lock.lock();
		try {
			System.out.println("~~~~~~~~~~~~~~~~");
		} finally {
			lock.unlock();
		}
	}

	@After
	public void teardown() throws Exception {
//		Stat stat = zk.exists(baseLockPath, false);
//		if (stat != null) {
//			List<String> children = zk.getChildren(baseLockPath, false);
//			for (String child : children) {
//				ZkUtils.safeDelete(zk, baseLockPath + "/" + child, -1);
//			}
//		}
		closeZooKeeper(zk);
	}

	private ZooKeeper newZookeeper() throws Exception {
		ZooKeeper zooKeeper = new ZooKeeper(zkServers, timeout, (watchedEvent) -> {
			System.out.println(">>> " + watchedEvent.toString());
		});
		System.out.println(">>> " + zooKeeper.toString());
		return zooKeeper;
	}

	private void closeZooKeeper(ZooKeeper zk) throws Exception {
		zk.close();
	}
}
