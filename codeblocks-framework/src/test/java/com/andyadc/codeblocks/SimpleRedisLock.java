package com.andyadc.codeblocks;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <URL>http://wudashan.com/2017/10/23/Redis-Distributed-Lock-Implement/</URL>
 * <p>只考虑Redis服务端单机部署的场景</p>
 *
 * @author andaicheng
 * @since 2018/4/22
 */
public class SimpleRedisLock {

    private static final String LOCK_SUCCESS = "OK";
    private static final Long RELEASE_SUCCESS = 1L;
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    private static final String LOCK_KEY_PREFIX = "lock:";
    private static final String LOCK_VALUE_PREFIX = "lock:v:";

    private final UUID uuid = UUID.randomUUID();
    /**
     * A local mutex lock for managing inter-thread synchronization
     */
    private final ReentrantLock localLock = new ReentrantLock(false);
    private String lockValue;
    private Jedis jedis;

    public SimpleRedisLock(Jedis jedis) {
        this.jedis = jedis;
        this.lockValue = this.getLockValue();
    }

    /**
     * Acquires the lock only if it is free at the time of invocation.
     */
    public boolean tryLock(String lockKey,
                           int expireTime) {
        lockKey = LOCK_KEY_PREFIX + lockKey;
        return this.tryLockInner(jedis, lockKey, expireTime, lockValue);
    }

    public boolean unlock(String lockKey) {
        lockKey = LOCK_KEY_PREFIX + lockKey;
        return releaseLockInner(jedis, lockKey, lockValue);
    }

    /**
     * 生成唯一的 value
     */
    private String getLockValue() {
        return LOCK_VALUE_PREFIX + uuid + ":" + Thread.currentThread().getId();
    }

    private boolean tryLockInner(Jedis jedis,
                                 String lockKey,
                                 int expireTime,
                                 String lockValue) {
        String result = setNxPx(jedis, lockKey, lockValue, expireTime);
        return LOCK_SUCCESS.equals(result);
    }

    /**
     * @param lockKey    key
     * @param expireTime milliseconds
     * @param lockValue  value
     * @return nil or OK
     */
    private String setNxPx(Jedis jedis,
                           String lockKey,
                           String lockValue,
                           long expireTime) {
        SetParams params = new SetParams();
        params.nx();
        params.px(expireTime);
        return jedis.set(lockKey, lockValue, params);
    }

    private void lockInner(Jedis jedis,
                           String lockKey,
                           String lockValue,
                           int expireTime) {
        for (; ; ) {
            localLock.lock();
            try {
                String result = setNxPx(jedis, lockKey, lockValue, expireTime);
                if (LOCK_SUCCESS.equals(result)) {
                    return;
                }
            } finally {
                localLock.unlock();
            }
        }
    }

    private boolean releaseLockInner(Jedis jedis,
                                     String lockKey,
                                     String lockValue) {

        // lua script
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(lockValue));
        return RELEASE_SUCCESS.equals(result);
    }
}
