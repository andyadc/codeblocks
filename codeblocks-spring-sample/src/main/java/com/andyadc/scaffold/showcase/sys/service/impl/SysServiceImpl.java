package com.andyadc.scaffold.showcase.sys.service.impl;

import com.andyadc.scaffold.showcase.sys.mapper.SysMapper;
import com.andyadc.scaffold.showcase.sys.service.SysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author andaicheng
 * @version 2017/2/23
 */
@Service
public class SysServiceImpl implements SysService {

    @Autowired
    private SysMapper sysMapper;

    @Override
    public String getNowDateTime() {
        return sysMapper.selectNow();
    }
}
