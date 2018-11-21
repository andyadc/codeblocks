package com.andyadc.perf4j.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author andy.an
 * @since 2018/11/21
 */
public class LogTest {

    private static final Logger logger = LoggerFactory.getLogger(LogTest.class);

    @Test
    public void logger() {
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                logger.error("error " + i);
            }
            if (i % 3 == 0) {
                logger.warn("warn " + i);
            }
            if (i % 4 == 0) {
                logger.info("info " + i);
            }
            if (i % 5 == 0) {
                logger.debug("debug " + i);
            }
            if (i % 2 == 0) {
                logger.trace("trace " + i);
            }
        }
    }
}
