package com.andyadc.codeblocks.kit.idgen;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author andy.an
 * @since 2018/10/10
 */
public final class IDGen {

    public static String uuid() {
        return new com.eaio.uuid.UUID().toString();
    }

    /*
     * 返回使用ThreadLocalRandm的UUID，比默认的UUID性能更优
     */
    public static UUID fastUUID() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return new UUID(random.nextLong(), random.nextLong());
    }
}
