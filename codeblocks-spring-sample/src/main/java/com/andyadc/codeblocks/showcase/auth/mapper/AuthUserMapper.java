package com.andyadc.codeblocks.showcase.auth.mapper;

import com.andyadc.codeblocks.framework.mybatis.MyBatisRepository;
import com.andyadc.codeblocks.showcase.auth.entity.AuthUser;

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