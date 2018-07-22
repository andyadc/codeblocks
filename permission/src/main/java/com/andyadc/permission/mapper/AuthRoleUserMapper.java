package com.andyadc.permission.mapper;

import com.andyadc.permission.entity.AuthRoleUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthRoleUserMapper {

    int deleteByPrimaryKey(Long id);

    int insertSelective(AuthRoleUser record);

    AuthRoleUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AuthRoleUser record);
}