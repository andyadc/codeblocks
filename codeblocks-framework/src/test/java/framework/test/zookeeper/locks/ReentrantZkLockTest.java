package framework.test.zookeeper.locks;

import com.andyadc.codeblocks.common.annotation.NotNull;
import com.andyadc.codeblocks.framework.zookeeper.ZkSessionManager;
import com.andyadc.codeblocks.framework.zookeeper.ZkUtils;
import com.andyadc.codeblocks.framework.zookeeper.locks.ReentrantZkLock;
import framework.test.zookeeper.BaseZkSessionManager;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * andy.an
 * 2019/12/25
 */
public class ReentrantZkLockTest {

	private static final String zkServers = "www.qq-server.com:2181";
	private static final String baseLockPath = "/test-locks";
	private static final int timeout = 2000;

	private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2, new ThreadFactory() {
		private int count = 0;

		@Override
		public Thread newThread(@NotNull Runnable r) {
			return new Thread(r, "ReentrantZkLockTest-" + count++);
		}
	});

	private static ZooKeeper zk;
	private static ZkSessionManager zkSessionManager;

	@BeforeAll
	public void setup() throws Exception {
		zk = newZookeeper();
		//be sure that the lock-place is created
		ZkUtils.recursiveSafeDelete(zk, baseLockPath, -1);
		zk.create(baseLockPath, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

		zkSessionManager = new BaseZkSessionManager(zk);
	}

	@Test
	public void testLock() throws Exception {
		Lock lock = new ReentrantZkLock(baseLockPath, zkSessionManager);

		lock.lock();
		try {
			System.out.println("I'm sleeping now...");
			TimeUnit.SECONDS.sleep(30);
			System.out.println("I woke up.");
		} finally {
			lock.unlock();
		}
	}

	@AfterAll
	public void teardown() throws Exception {
		Stat stat = zk.exists(baseLockPath, false);
		if (stat != null) {
			List<String> children = zk.getChildren(baseLockPath, false);
			for (String child : children) {
				ZkUtils.safeDelete(zk, baseLockPath + "/" + child, -1);
			}
		}
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
