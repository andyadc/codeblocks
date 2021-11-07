package com.andyadc.codeblocks.test.ehcache;

import com.github.benmanes.caffeine.cache.Policy;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.PooledExecutionServiceConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.EhcacheManager;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.expiry.ExpiryPolicy;
import org.ehcache.impl.config.persistence.CacheManagerPersistenceConfiguration;
import org.ehcache.xml.XmlConfiguration;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.config.builders.CacheManagerBuilder.newCacheManagerBuilder;

/**
 * andy.an
 */
public class EhCacheTest {

	@Test
	public void testPersistenceBuild() {
		CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
			.using(PooledExecutionServiceConfigurationBuilder
				.newPooledExecutionServiceConfigurationBuilder()
				.defaultPool("default", 1, 10)
				.build())
			.with(new CacheManagerPersistenceConfiguration(
				new File("/cache")
			))
			.build(true);

		CacheConfigurationBuilder.newCacheConfigurationBuilder(
			String.class, String.class,
			ResourcePoolsBuilder.newResourcePoolsBuilder()
				.disk(100L, MemoryUnit.MB, true)
		).withDiskStoreThreadPool("defulat", 5)
			.withExpiry(Expirations.timeToLiveExpiration(Duration.of(50, TimeUnit.SECONDS)))
			.withSizeOfMaxObjectGraph(3)
			.withSizeOfMaxObjectSize(1L, MemoryUnit.KB);


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
		CacheManager cacheManager = newCacheManagerBuilder()
			.withCache("basicCache", newCacheConfigurationBuilder(Long.class, String.class,
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
