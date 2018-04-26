package com.andyadc.codeblocks.lock.zookeeper;

/**
 * Abstract Skeleton implementation of a ConnectionListener interface.
 * <p>
 * <p>All methods in the ConnectionListener are implemented as no-ops, and it is the choice
 * of the concrete implementation to decide which actions to respond to, and which to ignore.
 *
 * @author andy.an
 * @since 2018/4/26
 */
public abstract class ConnectionListenerSkeleton implements ConnectionListener {

    @Override
    public void syncConnected() {
        //default no-op
    }

    @Override
    public void expired() {
        //default no-op
    }

    @Override
    public void disconnected() {
        //default no-op
    }
}
