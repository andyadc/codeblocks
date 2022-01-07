package com.andyadc.codeblocks.kit.idgen;

import java.util.concurrent.ThreadLocalRandom;

public final class UUID {

	/**
	 * 返回使用 ThreadLocalRandm 的 UUID，比默认的 UUID性能更优
	 */
	public static String randomUUID() {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		return new java.util.UUID(random.nextLong(), random.nextLong()).toString();
	}
}
