package com.andyadc.codeblocks.lock.redis;

import com.andyadc.codeblocks.lock.DLock;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;

/**
 * <URL>http://wudashan.com/2017/10/23/Redis-Distributed-Lock-Implement/</URL>
 * <p>只考虑Redis服务端单机部署的场景</p>
 *
 * @author andaicheng
 * @since 2018/4/22
 */
public class SimpleRedisLock implements DLock {

    private static final String LOCK_SUCCESS = "OK";
    private static final Long RELEASE_SUCCESS = 1L;
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    private static String LOCK_KEY_PREFIX = "lock:";

    private JedisPool jedisPool;

    public SimpleRedisLock(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    private boolean tryLockInner(Jedis jedis,
                                 String lockKey,
                                 int expireTime,
                                 String requestId) {

        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        return LOCK_SUCCESS.equals(result);
    }

    private boolean releaseLockInner(Jedis jedis,
                                     String lockKey,
                                     String requestId) {

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        return RELEASE_SUCCESS.equals(result);
    }
}
