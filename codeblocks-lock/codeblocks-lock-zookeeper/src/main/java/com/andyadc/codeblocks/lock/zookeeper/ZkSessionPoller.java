package com.andyadc.codeblocks.lock.zookeeper;

/**
 * Mechanism for automatically checking and firing Session Expiration events to the application
 * in the event of the client being disconnected from ZooKeeper for longer than the session timeout.
 * <p>
 * This mechanism is in place for when Disconnected events aren't quite enough--you also need to know
 * about Session Expiration events, and you need to know even in the event of total ZooKeeper connection failure.
 * This may include scenarios like Leader-Election as a governing mechanism for running tasks
 *
 * @author andy.an
 * @since 2018/4/25
 */
public class ZkSessionPoller {
}
