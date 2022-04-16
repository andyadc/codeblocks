package com.andyadc.bms.redis;

import org.springframework.beans.factory.annotation.Value;
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
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked"})
@Component
public class RedisOperator {

	@Value("${redis.key.prefix:}")
	private String prefix;

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
						Objects.requireNonNull(keySerializer.serialize(buildKey(k))),
						Objects.requireNonNull(valueSerializer.serialize(v))
					);
				});
				// 不需要close, 否则拿不到返回值
				// connection.closePipeline();
				return null;
			}
		});
	}

	private String buildKey(String key) {
		return String.join(":", prefix, key);
	}

	public long exist(Collection<String> keys) {
		List<String> collect = keys.stream().map(this::buildKey).collect(Collectors.toList());
		Long count = redisTemplate.countExistingKeys(collect);
		return count == null ? 0L : count;
	}

	public boolean expire(String key, long timeout, TimeUnit unit) {
		key = buildKey(key);
		Boolean ret = redisTemplate.expire(key, timeout, unit);
		return ret != null && ret;
	}

	public long del(String... keys) {
		return this.del(Arrays.asList(keys));
	}

	public long del(Collection<String> keys) {
		List<String> collect = keys.stream().map(this::buildKey).collect(Collectors.toList());
		Long ret = redisTemplate.delete(collect);
		return ret == null ? 0L : ret;
	}

	public void set(String key, Object value) {
		key = buildKey(key);
		redisTemplate.opsForValue().set(key, value);
	}

	public void set(String key, Object value, long timeout, TimeUnit unit) {
		key = buildKey(key);
		redisTemplate.opsForValue().set(key, value, timeout, unit);
	}

	public Object get(String key) {
		key = buildKey(key);
		return redisTemplate.opsForValue().get(key);
	}

	public List<Object> multiGet(Collection<String> keys) {
		List<String> collect = keys.stream().map(this::buildKey).collect(Collectors.toList());
		return redisTemplate.opsForValue().multiGet(collect);
	}

	// --- hash ---
	public void hput(String key, Map<String, Object> hmap) {
		key = buildKey(key);
		redisTemplate.opsForHash().putAll(key, hmap);
	}

	public void hput(String key, String hkey, Object value) {
		key = buildKey(key);
		redisTemplate.opsForHash().put(key, hkey, value);
	}

	public boolean hasHKey(String key, String hkey) {
		key = buildKey(key);
		return redisTemplate.opsForHash().hasKey(key, hkey);
	}

	public Object hget(String key, String hkey) {
		key = buildKey(key);
		return redisTemplate.opsForHash().get(key, hkey);
	}

	public Map<Object, Object> hget(String key) {
		key = buildKey(key);
		return redisTemplate.opsForHash().entries(key);
	}

	public List<Object> hmget(String key, Collection<Object> hkeys) {
		key = buildKey(key);
		return redisTemplate.opsForHash().multiGet(key, hkeys);
	}

	public long del(String key, Collection<Object> hkeys) {
		key = buildKey(key);
		return redisTemplate.opsForHash().delete(key, hkeys);
	}

	// --- set ---
	public long sadd(String key, Object... values) {
		key = buildKey(key);
		Long ret = redisTemplate.opsForSet().add(key, values);
		return ret == null ? 0L : ret;
	}

	public long sdel(String key, Object... values) {
		key = buildKey(key);
		Long ret = redisTemplate.opsForSet().remove(key, values);
		return ret == null ? 0L : ret;
	}
}
