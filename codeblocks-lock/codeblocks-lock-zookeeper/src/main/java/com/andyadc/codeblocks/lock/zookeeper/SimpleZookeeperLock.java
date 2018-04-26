package com.andyadc.codeblocks.lock.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author andy.an
 * @since 2018/4/25
 */
public class SimpleZookeeperLock extends ZkPrimitive implements Lock {

    /**
     * A default delimiter to separate a lockPrefix from the sequential elements set by ZooKeeper.
     */
    protected static final char lockDelimiter = '-';
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleZookeeperLock.class);
    private static final String lockPrefix = "lock";
    private ThreadLocal<LockHolder> locks = new ThreadLocal<>();

    /**
     * Constructs a new Lock on the specified node, using Open ACL privileges.
     *
     * @param baseNode         the node to lock on
     * @param zkSessionManager the session manager to use.
     */
    protected SimpleZookeeperLock(String baseNode, ZkSessionManager zkSessionManager) {
        super(baseNode, zkSessionManager, ZooDefs.Ids.OPEN_ACL_UNSAFE);
    }

    /**
     * Creates a new ZkPrimitive with the correct node information.
     *
     * @param baseNode         the base node to use
     * @param zkSessionManager the session manager to use
     * @param privileges       the privileges for this node.
     */
    protected SimpleZookeeperLock(String baseNode, ZkSessionManager zkSessionManager, List<ACL> privileges) {
        super(baseNode, zkSessionManager, privileges);
    }

    /**
     * Acquires the lock.
     * <p><p>
     * If the lock is not available, then the current thread becomes disabled for thread scheduling purposes and
     * lies dormant until the lock as been acquired.
     * <p><p>
     * Note: If the ZooKeeper session expires while this method is waiting, a {@link RuntimeException} will be thrown.
     *
     * @throws RuntimeException wrapping:
     *                          <ul>
     *                          <li> {@link org.apache.zookeeper.KeeperException} if the ZooKeeper server
     *                          encounters a problem
     *                          <li> {@link InterruptedException} if there is a communication problem between
     *                          the ZooKeeper client and server
     *                          <li> If the ZooKeeper session expires
     *                          </ul>
     * @inheritDoc
     * @see java.util.concurrent.locks.Lock#lock()
     */
    @Override
    public final void lock() {
        if (checkReentrancy()) {
            return;
        }

        //set a connection listener to listen for session expiration
        setConnectionListener();

        ZooKeeper zk = zkSessionManager.getZooKeeper();
        String lockNode;
        try {
            lockNode = zk.create(getBaseLockPath(), emptyNode, privileges, CreateMode.EPHEMERAL_SEQUENTIAL);

            while (true) {
                if (broken) {
                    throw new RuntimeException("ZooKeeper Session has expired, invalidating this lock object");
                }
                localLock.lock();
                try {
                    //ask ZooKeeper for the lock

                } finally {
                    localLock.unlock();
                }
            }
        } catch (KeeperException e) {

        } catch (InterruptedException e) {

        }
    }

    @Override
    public void lockInterruptibly() {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
        return false;
    }

    @Override
    public void unlock() {

    }

    @Override
    public Condition newCondition() {
        return null;
    }

    /**
     * Asks ZooKeeper for a lock of a given type.
     * <p>
     * When this method has completed, either the state of the ZooKeeper server is such that this party now
     * holds the lock of the correct type, or, if the {@code watch} parameter is true, any and all necessary
     * Watcher elements have been set.
     * <p>
     * Classes which override this method MUST adhere to the requested watch rule, or else the semantics
     * of the lock interface may be broken. That is, if the {@code watch} parameter is true, then a watch
     * needs to have been set by the end of this method.
     * <p>
     * It is recommended that classes which override this method also override {@link #getBaseLockPath()} and
     * {@link #getLockPrefix()} as well.
     *
     * @param zk       the ZooKeeper client to use
     * @param lockNode the node to lock on
     * @param watch    whether or not to watch other nodes if the lock is behind someone else
     * @return true if the lock has been acquired, false otherwise
     * @throws KeeperException      if Something bad happens on the ZooKeeper server
     * @throws InterruptedException if communication between ZooKeeper and the client fail in some way
     */
    protected boolean tryAcquireDistributed(ZooKeeper zk, String lockNode, boolean watch) {

        return true;
    }

    /**
     * Determines whether or not this party owns the lock.
     *
     * @return true if the current party(i.e. Thread and ZooKeeper client) owns the Lock
     */
    public final boolean hasLock() {
        return locks.get() != null;
    }

    /**
     * Gets the name of the lock which this thread holds.
     * <p>
     * Note: This method will return {@code null} <i>unless</i> the current thread is the lock owner. This method
     * is primarily intended to ease the use of shared lock implementations between threads, and should not be used
     * to manage lock state.
     *
     * @return the name of the lock which this thread owns, or null.
     */
    protected final String getLockName() {
        LockHolder lockHolder = locks.get();
        if (lockHolder == null) return null;
        return lockHolder.lockNode();
    }

    /**
     * Gets the prefix to use for locks of this type.
     *
     * @return the prefix to prepend to all nodes created by this lock.
     */
    protected String getLockPrefix() {
        return lockPrefix;
    }

    /**
     * Gets the base path for a lock node of this type.
     *
     * @return the base lock path(all the way up to a delimiter for sequence elements)
     */
    protected String getBaseLockPath() {
        return baseNode + "/" + getLockPrefix() + lockDelimiter;
    }

    /*
      Checks whether or not this party is re-entering a lock which it already owns.
      If this party already owns the lock, this method increments the lock counter and returns true.
      Otherwise, it return false.
    */
    private boolean checkReentrancy() {
        LockHolder holder = locks.get();
        if (holder != null) {
            holder.incrementLock();
            return true;
        }
        return false;
    }

    /**
     * Holder for information about a specific lock
     */
    private static class LockHolder {
        private final String lockNode;
        private final AtomicInteger numLocks = new AtomicInteger(1);

        public LockHolder(String lockNode) {
            this.lockNode = lockNode;
        }

        public void incrementLock() {
            numLocks.incrementAndGet();
        }

        public int decrementLock() {
            return numLocks.decrementAndGet();
        }

        public String lockNode() {
            return lockNode;
        }
    }
}
