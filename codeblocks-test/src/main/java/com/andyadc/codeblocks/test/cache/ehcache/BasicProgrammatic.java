package com.andyadc.codeblocks.test.cache.ehcache;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;

import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.config.builders.CacheManagerBuilder.newCacheManagerBuilder;


/**
 * andy.an
 */
public class BasicProgrammatic {

	public static void main(String[] args) {
		CacheManager cacheManager = newCacheManagerBuilder()
			.withCache("basicCache", newCacheConfigurationBuilder(Long.class, String.class,
				ResourcePoolsBuilder.newResourcePoolsBuilder()
					.heap(10, EntryUnit.ENTRIES)
					.offheap(1, MemoryUnit.MB)
					.disk(20, MemoryUnit.GB)
			)).build(true);

	}
}
