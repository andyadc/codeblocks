package com.andyadc.codeblocks.test.cache;

import org.ehcache.Cache;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;

import java.io.File;

/**
 * andy.an
 */
public class EhCacheTest {

    public static void main(String[] args) {
        PersistentCacheManager cacheManager = CacheManagerBuilder
                .newCacheManagerBuilder()
                .with(CacheManagerBuilder.persistence("d:" + File.separator + "opt/cache"))
                .withCache("threeTieredCache",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Integer.class, String.class,
                                ResourcePoolsBuilder.newResourcePoolsBuilder()
                                        .heap(10, EntryUnit.ENTRIES)
                                        .offheap(1, MemoryUnit.MB)
                                        .disk(20, MemoryUnit.GB)))
                .build(true);

        Cache<Integer, String> cache = cacheManager.getCache("threeTieredCache", Integer.class, String.class);

        for (int i = 0; i < 100000; i++) {
            cache.put(i, i + "");
        }

        for (int i = 0; i < 100000; i++) {
            System.out.println(cache.get(i));
        }

        cacheManager.close();
    }
}
