package com.andyadc.scaffold.showcase.auth.mapper;

import com.andyadc.codeblocks.mybatis.MyBatisRepository;
import com.andyadc.scaffold.showcase.auth.entity.AuthUser;

@MyBatisRepository
public interface AuthUserMapper {

    int deleteById(Long id);

    int insertSelective(AuthUser record);

    AuthUser selectById(Long id);

    AuthUser selectByUsername(String username);

    int updateByIdSelective(AuthUser record);

    int deleteAuthUserLogic(Long id);

    int lockAuthUserByUsername(String username);
}