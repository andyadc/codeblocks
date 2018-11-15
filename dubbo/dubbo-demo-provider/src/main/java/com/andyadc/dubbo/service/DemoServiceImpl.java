package com.andyadc.dubbo.service;

import com.andyadc.dubbo.api.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author andy.an
 * @since 2018/11/14
 */
public class DemoServiceImpl implements DemoService {

    private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Override
    public String hello(String message) {
        logger.info("message={}", message);
        return "Hello " + message;
    }
}
