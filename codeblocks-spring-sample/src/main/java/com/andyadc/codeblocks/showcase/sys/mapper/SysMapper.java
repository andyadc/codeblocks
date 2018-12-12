package com.andyadc.codeblocks.showcase.sys.mapper;

import com.andyadc.codeblocks.framework.mybatis.MyBatisRepository;

/**
 * @author andaicheng
 * @version 2017/2/23
 */
@MyBatisRepository
public interface SysMapper {
    String selectNow();
}
