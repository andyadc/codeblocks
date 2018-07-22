package com.andyadc.permission.mapper;

import com.andyadc.permission.entity.AuthAcl;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthAclMapper {

    int deleteByPrimaryKey(Long id);

    int insertSelective(AuthAcl record);

    AuthAcl selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AuthAcl record);
}