package com.andyadc.codeblocks.showcase.sys.service.impl;

import com.andyadc.codeblocks.showcase.sys.entity.SpringTransaction;
import com.andyadc.codeblocks.showcase.sys.handle.SpringTransactionHandle;
import com.andyadc.codeblocks.showcase.sys.mapper.SpringTransactionMapper;
import com.andyadc.codeblocks.showcase.sys.service.SpringTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpringTransactionServiceImpl implements SpringTransactionService {

    @Autowired
    private SpringTransactionMapper mapper;
    @Autowired
    private SpringTransactionHandle handle;

    @Transactional
    @Override
    public void service1() {
        mapper.insertSelective(new SpringTransaction("st1"));

        handle.handle1();

        //int i = 1 / 0;
    }

    @Transactional
    @Override
    public boolean updateIfNumberIsZero(long id, int num) {
        int c = mapper.updateIfNumberIsZero(id, num);
        return c > 0;
    }
}
