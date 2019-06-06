package com.andyadc.codeblocks.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * andy.an
 */
public class LogTest {

    private static final Logger logger = LoggerFactory.getLogger(LogTest.class);

    public static void main(String[] args) {
        logger.info("info");

        String[] ss = {"a", "b", "c"};
        logger.info("{}-{}", ss);
    }
}
