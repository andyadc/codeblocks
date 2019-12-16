package com.andyadc.codeblocks.test.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * andy.an
 * 2019/12/16
 */
public class CacheTest {

	// 移除监听器，记录被删除时可以感知到这个事件
	RemovalListener<String, String> listener = (notification) ->
		System.out.println("Removal: " + notification.getKey() + "-" + notification.getValue());

	@Test
	public void testCache() throws Exception {
		Cache<String, String> cache = CacheBuilder.newBuilder()
			.maximumSize(2)
//			.expireAfterWrite(3, TimeUnit.SECONDS) // 对象被写入到缓存后多久过期
			.expireAfterAccess(3, TimeUnit.SECONDS) // 对象多久没有被访问后过期
			.removalListener(listener)
			.build();
		cache.put("k1", "andy");
		cache.put("k2", "andy");
		cache.put("k3", "andy");

//		cache.invalidateAll(); // 批量删除Cache中的全部记录

		cache.invalidate("k3");

		System.out.println(cache.getIfPresent("k1"));
		System.out.println(cache.getIfPresent("k2"));
		System.out.println(cache.getIfPresent("k3"));

		String v = cache.get("k4", () -> {
			Thread.sleep(1000);
			return "load by callable";
		});

		System.out.println(v);

		System.out.println(cache.stats()); // 获取统计信息

	}

	@Test
	public void testLoadingCache() throws Exception {
		CacheLoader<String, String> loader = new CacheLoader<String, String>() {
			@Override
			public String load(String key) throws Exception {
				Thread.sleep(1000);
				System.out.println(key + " is loaded from a cacheLoader!");
				return key + "'s value";
			}
		};

		LoadingCache<String, String> cache = CacheBuilder.newBuilder()
			.maximumSize(2)
			.build(loader);

		System.out.println(cache.get("k1"));
		System.out.println(cache.get("k2"));
	}
}
