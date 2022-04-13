package com.andyadc.bms.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"unchecked"})
@Component
public class RedisOperator {

	private final RedisTemplate<String, Object> redisTemplate;
	private final RedisSerializer keySerializer;
	private final RedisSerializer valueSerializer;

	public RedisOperator(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
		keySerializer = this.redisTemplate.getKeySerializer();
		valueSerializer = this.redisTemplate.getValueSerializer();
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	/**
	 * by command `scan`
	 *
	 * @param pattern key, like *key*
	 * @param count   scan key number, not result size
	 */
	public Set<String> keys(String pattern, long count) {
		return redisTemplate.execute(new RedisCallback<Set<String>>() {
			@Override
			public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
				ScanOptions options = ScanOptions.scanOptions().match(pattern).count(count).build();
				Set<String> set = new HashSet<>();
				Cursor<byte[]> cursor = connection.scan(options);
				while (cursor.hasNext()) {
					set.add(new String(cursor.next()));
				}
				return set;
			}
		});
	}

	// --- TODO Pipeline ---

	/**
	 * batch set
	 *
	 * @param kvs k-v map
	 */
	public void batchSet(Map<String, Object> kvs) {
		redisTemplate.executePipelined(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				connection.openPipeline();
				kvs.forEach((k, v) -> {
					connection.set(
						Objects.requireNonNull(keySerializer.serialize(k)),
						Objects.requireNonNull(valueSerializer.serialize(v))
					);
				});
				// 不需要close, 否则拿不到返回值
				// connection.closePipeline();
				return null;
			}
		});
	}

	public long exist(Collection<String> keys) {
		Long count = redisTemplate.countExistingKeys(keys);
		return count == null ? 0L : count;
	}

	public boolean expire(String key, long timeout, TimeUnit unit) {
		Boolean ret = redisTemplate.expire(key, timeout, unit);
		return ret != null && ret;
	}

	public long del(String... keys) {
		return this.del(Arrays.asList(keys));
	}

	public long del(Collection<String> keys) {
		Long ret = redisTemplate.delete(keys);
		return ret == null ? 0L : ret;
	}

	public void set(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}

	public void set(String key, Object value, long timeout, TimeUnit unit) {
		redisTemplate.opsForValue().set(key, value, timeout, unit);
	}

	public Object get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	public List<Object> multiGet(Collection<String> keys) {
		return redisTemplate.opsForValue().multiGet(keys);
	}

	// --- hash ---
	public void hput(String key, Map<String, Object> hmap) {
		redisTemplate.opsForHash().putAll(key, hmap);
	}

	public void hput(String key, String hkey, Object value) {
		redisTemplate.opsForHash().put(key, hkey, value);
	}

	public boolean hasHKey(String key, String hkey) {
		return redisTemplate.opsForHash().hasKey(key, hkey);
	}

	public Object hget(String key, String hkey) {
		return redisTemplate.opsForHash().get(key, hkey);
	}

	public Map<Object, Object> hget(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

	public List<Object> hmget(String key, Collection<Object> hkeys) {
		return redisTemplate.opsForHash().multiGet(key, hkeys);
	}

	public long del(String key, Collection<Object> hkeys) {
		return redisTemplate.opsForHash().delete(key, hkeys);
	}

	// --- set ---
	public long sadd(String key, Object... values) {
		Long ret = redisTemplate.opsForSet().add(key, values);
		return ret == null ? 0L : ret;
	}

	public long sdel(String key, Object... values) {
		Long ret = redisTemplate.opsForSet().remove(key, values);
		return ret == null ? 0L : ret;
	}
}
