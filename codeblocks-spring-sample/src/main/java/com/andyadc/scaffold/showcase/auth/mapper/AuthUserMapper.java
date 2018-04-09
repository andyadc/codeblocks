package com.andyadc.scaffold.showcase.auth.mapper;

import com.andyadc.scaffold.showcase.auth.entity.AuthUser;
import com.andyadc.scaffold.showcase.common.repository.MyBatisRepository;

@MyBatisRepository
public interface AuthUserMapper {

    int deleteByPrimaryKey(Long id);

    int insertSelective(AuthUser record);

    AuthUser selectByPrimaryKey(Long id);

    AuthUser selectByAccount(String account);

    int updateByPrimaryKeySelective(AuthUser record);

    int deleteAuthUserLogic(Long id);

    int lockAuthUserByAccount(String account);
}