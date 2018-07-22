package com.andyadc.permission.mapper;

import com.andyadc.permission.entity.AuthAclModule;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthAclModuleMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(AuthAclModule record);

    AuthAclModule selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AuthAclModule record);
}