package com.andyadc.codeblocks.kit.idgen;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author andy.an
 */
public final class UUID {

	public static String fastUUID() {
        return new com.eaio.uuid.UUID().toString();
    }

    /*
	 * 返回使用ThreadLocalRandm的 UUID，比默认的 UUID性能更优
	 */
	public static String randomUUID() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
		return new java.util.UUID(random.nextLong(), random.nextLong()).toString();
    }
}
