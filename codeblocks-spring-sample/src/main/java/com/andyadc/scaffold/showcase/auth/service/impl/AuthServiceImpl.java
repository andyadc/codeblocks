package com.andyadc.scaffold.showcase.auth.service.impl;

import com.andyadc.scaffold.showcase.auth.entity.AuthUser;
import com.andyadc.scaffold.showcase.auth.mapper.AuthUserMapper;
import com.andyadc.scaffold.showcase.auth.service.AuthService;
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
    public AuthUser findAuthUserByAccount(String account) {
        return authUserMapper.selectByAccount(account);
    }

    @Override
    public boolean lockAuthUser(String account) {
        return authUserMapper.lockAuthUserByAccount(account) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AuthUser saveAuthUser(AuthUser authUser) {
        if (authUser == null) {
            return null;
        }
        if (authUser.getId() != null && authUser.getId() > 0) {
            authUser.setUpdateTime(new Date());
            authUserMapper.updateByPrimaryKeySelective(authUser);
        } else {
            authUser.setCreateTime(new Date());
            authUser.setUpdateTime(new Date());
            authUserMapper.insertSelective(authUser);
        }
        return authUser;
    }

}
