package com.andyadc.permission.mapper;

import com.andyadc.permission.entity.AuthLogWithBLOBs;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthLogMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(AuthLogWithBLOBs record);

    AuthLogWithBLOBs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AuthLogWithBLOBs record);
}