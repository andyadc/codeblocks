package com.andyadc.scaffold.showcase.auth.mapper;

import com.andyadc.codeblocks.mybatis.MyBatisRepository;
import com.andyadc.scaffold.showcase.auth.entity.AuthUser;

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