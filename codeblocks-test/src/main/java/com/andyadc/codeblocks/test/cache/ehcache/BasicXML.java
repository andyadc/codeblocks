package com.andyadc.codeblocks.test.cache.ehcache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.core.EhcacheManager;
import org.ehcache.xml.XmlConfiguration;

/**
 * andy.an
 */
public class BasicXML {

	public static void main(String[] args) {
		Configuration configuration = new XmlConfiguration(BasicXML.class.getResource("/ehcache/ehcache.xml"));
		try (CacheManager cacheManager = new EhcacheManager(configuration)) {

			cacheManager.init();
			Cache<Long, String> cache = cacheManager.getCache("basicCache", Long.class, String.class);
			cache.put(1L, "abc");

			System.out.println(cache.get(1L));
		}
	}
}
