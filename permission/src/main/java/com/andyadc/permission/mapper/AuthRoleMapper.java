package com.andyadc.permission.mapper;

import com.andyadc.permission.entity.AuthRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insertSelective(AuthRole record);

    AuthRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AuthRole record);
}