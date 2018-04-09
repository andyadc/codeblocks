package com.andyadc.scaffold.showcase.sys.handle.impl;

import com.andyadc.scaffold.showcase.sys.entity.SpringTransaction;
import com.andyadc.scaffold.showcase.sys.handle.SpringTransactionHandle;
import com.andyadc.scaffold.showcase.sys.mapper.SpringTransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author andaicheng
 * @version 2017/6/13
 */
@Service
public class SpringTransactionHandleImpl implements SpringTransactionHandle {

    @Autowired
    private SpringTransactionMapper mapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void handle1() {
        mapper.insertSelective(new SpringTransaction("st2"));
        int i = 1 / 0;
    }

}
