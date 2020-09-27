package com.andyadc.codeblocks.test.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class CaffeineTest {

	@Test
	public void testCache() {
		RemovalListener removalListener = (key, value, removalCause) -> {
			System.out.println("key:" + key + ", value:" + value + ", cause:" + removalCause.toString());
		};

		Cache<Object, Object> cache = Caffeine.newBuilder()
			.maximumSize(10)
			.expireAfterWrite(5, TimeUnit.MINUTES)
			.weakKeys()
			.weakValues()
			.removalListener(removalListener)
			.build();

		cache.put("username", "afei");
		cache.put("password", "123456");

		System.out.println(cache.getIfPresent("username"));
		System.out.println(cache.getIfPresent("password"));

		cache.invalidate("username");
	}
}
