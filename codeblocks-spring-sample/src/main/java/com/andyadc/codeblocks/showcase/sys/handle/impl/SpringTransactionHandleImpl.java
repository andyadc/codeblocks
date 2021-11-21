package com.andyadc.codeblocks.showcase.sys.handle.impl;

import com.andyadc.codeblocks.showcase.sys.entity.SpringTransaction;
import com.andyadc.codeblocks.showcase.sys.handle.SpringTransactionHandle;
import com.andyadc.codeblocks.showcase.sys.mapper.SpringTransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
