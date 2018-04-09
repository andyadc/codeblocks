package com.andyadc.scaffold.showcase.sys.service;

/**
 * @author andaicheng
 * @version 2017/6/13
 */
public interface SpringTransactionService {

    void service1();

    boolean updateIfNumberIsZero(long id, int num);
}
