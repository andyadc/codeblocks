package com.andyadc.codeblocks.test.ehcache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.PooledExecutionServiceConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.EhcacheManager;
import org.ehcache.impl.config.persistence.CacheManagerPersistenceConfiguration;
import org.ehcache.xml.XmlConfiguration;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;

/**
 * andy.an
 */
public class EhCacheTest {

	private static final String cache_path = "/opt/ehcache";

	/**
	 * offheap cache
	 * 使用堆外缓存， 需要添加 JVM 启动参数， 如 -XX：MaxDirectMemorySize=10G
	 */
	@Test
	public void testOffheapCache() {
		CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);

		CacheConfigurationBuilder<String, String> configuration = CacheConfigurationBuilder.newCacheConfigurationBuilder(
			String.class, String.class,
			ResourcePoolsBuilder.newResourcePoolsBuilder()
				.offheap(100L, MemoryUnit.MB)
		)
			.withDispatcherConcurrency(4)
			.withExpiry(
				ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(10L))
			)
			.withSizeOfMaxObjectGraph(3)
			.withSizeOfMaxObjectSize(1, MemoryUnit.KB);

		Cache<String, String> mycache = cacheManager.createCache("mycache", configuration);
	}

	/**
	 * heap cache
	 */
	@Test
	public void testHeapCache() {
		CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);

		CacheConfigurationBuilder<String, String> configuration = CacheConfigurationBuilder.newCacheConfigurationBuilder(
			String.class, String.class,
			ResourcePoolsBuilder.newResourcePoolsBuilder()
				.heap(100L, EntryUnit.ENTRIES) // 设置缓存的条目数量，当超出此数量时按 LRU 进行缓存回收
		)
			.withDispatcherConcurrency(4)
			.withExpiry(
				ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(10L))
			);

		Cache<String, String> mycache = cacheManager.createCache("mycache", configuration);
	}

	/**
	 * disk cache
	 */
	@Test
	public void testThreadPoolAndPersistence() {
		String thread_pool_alias = "default";
		CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
			.using(PooledExecutionServiceConfigurationBuilder
				.newPooledExecutionServiceConfigurationBuilder()
				.defaultPool(thread_pool_alias, 1, 10)
				.build())
			.with(new CacheManagerPersistenceConfiguration(
				new File(cache_path)
			))
			.build(true);

		CacheConfiguration<String, String> configuration = CacheConfigurationBuilder.newCacheConfigurationBuilder(
			String.class, String.class,
			ResourcePoolsBuilder.newResourcePoolsBuilder()
				.disk(100L, MemoryUnit.MB, true)
		).withDiskStoreThreadPool(thread_pool_alias, 5)
			.withExpiry(
				ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(10L))
			)
			.withSizeOfMaxObjectGraph(3L)
			.withSizeOfMaxObjectSize(1L, MemoryUnit.KB)
			.build();

		Cache<String, String> cache = cacheManager.createCache("mycache", configuration);
		cache.put("name", "adc");

		System.out.println(cache.get("name"));

		cacheManager.close();
	}

	@Test
	public void basicXML() {
		Configuration configuration = new XmlConfiguration(EhCacheTest.class.getResource("/ehcache/ehcache-basic.xml"));
		try (CacheManager cacheManager = new EhcacheManager(configuration)) {

			cacheManager.init();
			Cache<Long, String> cache = cacheManager.getCache("basicCache", Long.class, String.class);
			cache.put(1L, "abc");

			System.out.println(cache.get(1L));
		}
	}

	@Test
	public void basicProgrammatic() {
		CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
			.withCache("basicCache", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
				ResourcePoolsBuilder.newResourcePoolsBuilder()
					.heap(10, EntryUnit.ENTRIES)
					.offheap(1, MemoryUnit.MB)
				)
			).build(true);

		Cache<Long, String> cache = cacheManager.getCache("basicCache", Long.class, String.class);
		cache.put(1L, "ABC");
		System.out.println(cache.get(1L));

		cacheManager.close();
	}

	@Test
	public void persistenceProgrammatic() {
		String storagePath = "d:" + File.separator + "opt/cache";
		PersistentCacheManager cacheManager = CacheManagerBuilder
			.newCacheManagerBuilder()
			.with(CacheManagerBuilder.persistence(storagePath))
			.withCache("persistenceCache",
				CacheConfigurationBuilder.newCacheConfigurationBuilder(Integer.class, String.class,
					ResourcePoolsBuilder.newResourcePoolsBuilder()
						.heap(10, EntryUnit.ENTRIES)
						.offheap(1, MemoryUnit.MB)
						.disk(20, MemoryUnit.GB, true)
				)
			).build(true);

		Cache<Integer, String> cache = cacheManager.getCache("persistenceCache", Integer.class, String.class);

		for (int i = 0; i < 100000; i++) {
			cache.put(i, i + "");
		}

		for (int i = 0; i < 10000; i++) {
			System.out.println(cache.get(i));
		}

		cacheManager.close();
	}
}
