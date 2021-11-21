package com.andyadc.codeblocks.showcase.sys.service.impl;

import com.andyadc.codeblocks.showcase.sys.mapper.SysMapper;
import com.andyadc.codeblocks.showcase.sys.service.SysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysServiceImpl implements SysService {

    @Autowired
    private SysMapper sysMapper;

    @Override
    public String getNowDateTime() {
        return sysMapper.selectNow();
    }
}
