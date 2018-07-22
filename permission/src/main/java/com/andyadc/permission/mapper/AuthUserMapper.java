package com.andyadc.permission.mapper;

import com.andyadc.permission.entity.AuthUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthUserMapper {

    int deleteByPrimaryKey(Long id);

    int insertSelective(AuthUser record);

    AuthUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AuthUser record);
}