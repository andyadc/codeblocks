package com.andyadc.codeblocks.framework.zookeeper.locks;

import com.andyadc.codeblocks.framework.zookeeper.ZkPrimitive;
import com.andyadc.codeblocks.framework.zookeeper.ZkSessionManager;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * A ZooKeeper-based implementation of a {@link java.util.concurrent.locks.Condition}.
 * <p><p>
 * This class adheres to the idioms of the {@link java.util.concurrent.locks.Condition} interface wherever possible.
 *
 * @author andy.an
 * @since 2019/2/19
 */
public class ZkCondition extends ZkPrimitive implements Condition {

    private static final String conditionPrefix = "condition";
    private static final char conditionDelimiter = '-';
    private final ReentrantZkLock distributedLock;

    public ZkCondition(String baseNode, ZkSessionManager zkSessionManager, List<ACL> privileges, ReentrantZkLock lock) {
        super(baseNode, zkSessionManager, privileges);
        this.distributedLock = lock;
    }

    @Override
    public void await() throws InterruptedException {
        await(Long.MAX_VALUE, TimeUnit.DAYS);
    }

    @Override
    public void awaitUninterruptibly() {

    }

    @Override
    public long awaitNanos(long nanosTimeout) throws InterruptedException {
        return 0;
    }

    @Override
    public boolean await(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public boolean awaitUntil(Date deadline) throws InterruptedException {
        return false;
    }

    @Override
    public void signal() {

    }

    @Override
    public void signalAll() {

    }

    /*--------------------------------------------------------------------------------------------------------------------*/
    /*private helper methods*/

	private boolean checkTimeout(ZooKeeper zooKeeper, String nodeName, long timeLeft)
            throws InterruptedException, KeeperException {
        if (timeLeft <= 0) {
            //timed out
            zooKeeper.delete(nodeName, -1);
            return true;
        }
        return false;
    }
}
