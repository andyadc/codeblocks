package com.andyadc.codeblocks.showcase.auth.service.impl;

import com.andyadc.codeblocks.showcase.auth.entity.AuthUser;
import com.andyadc.codeblocks.showcase.auth.mapper.AuthUserMapper;
import com.andyadc.codeblocks.showcase.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author andaicheng
 * @version 2017/1/4
 */
@Service("authService")
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthUserMapper authUserMapper;

    @Override
    public AuthUser findAuthUserByUsername(String username) {
        return authUserMapper.selectByUsername(username);
    }

    @Override
    public boolean lockAuthUser(String username) {
        return authUserMapper.lockAuthUserByUsername(username) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AuthUser saveAuthUser(AuthUser authUser) {
        if (authUser == null) {
            return null;
        }
        if (authUser.getKey() != null && authUser.getKey() > 0) {
            authUser.setUpdatedTime(new Date());
            authUserMapper.updateByIdSelective(authUser);
        } else {
            authUser.setCreatedTime(new Date());
            authUser.setUpdatedTime(new Date());
            authUserMapper.insertSelective(authUser);
        }
        return authUser;
    }

}
