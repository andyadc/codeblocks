package com.andyadc.scaffold.showcase.sys.mapper;

import com.andyadc.scaffold.showcase.common.repository.MyBatisRepository;

/**
 * @author andaicheng
 * @version 2017/2/23
 */
@MyBatisRepository
public interface SysMapper {
    String selectNow();
}
