package com.andyadc.codeblocks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author andy.an
 * @since 2018/12/6
 */
public class Test {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 100; i++) {
            logger.info("k-" + i);
        }

        TimeUnit.SECONDS.sleep(100);
    }
}
