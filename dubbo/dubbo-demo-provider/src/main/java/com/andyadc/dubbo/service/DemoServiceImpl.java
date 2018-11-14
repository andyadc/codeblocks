package com.andyadc.dubbo.service;

import com.andyadc.dubbo.api.DemoService;

/**
 * @author andy.an
 * @since 2018/11/14
 */
public class DemoServiceImpl implements DemoService {

    @Override
    public String hello(String message) {
        return "Hello " + message;
    }
}
