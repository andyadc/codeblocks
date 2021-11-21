package com.andyadc.codeblocks.showcase.sys.mapper;

import com.andyadc.codeblocks.framework.mybatis.MyBatisRepository;

@MyBatisRepository
public interface SysMapper {
    String selectNow();
}
