package com.andyadc.codeblocks.framework.zookeeper;

/**
 * @author andy.an
 * @since 2019/2/18
 */
public class ConnectionListenerSkeleton implements ConnectionListener {

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
