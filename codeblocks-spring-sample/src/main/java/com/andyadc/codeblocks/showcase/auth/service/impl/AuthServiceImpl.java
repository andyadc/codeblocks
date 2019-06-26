package com.andyadc.codeblocks.showcase.auth.service.impl;

import com.andyadc.codeblocks.framework.aspect.Loggable;
import com.andyadc.codeblocks.kit.Assert;
import com.andyadc.codeblocks.showcase.auth.entity.AuthUser;
import com.andyadc.codeblocks.showcase.auth.mapper.AuthUserMapper;
import com.andyadc.codeblocks.showcase.auth.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author andaicheng
 * @version 2017/1/4
 */
@Service("authService")
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private AuthUserMapper authUserMapper;

    @Override
    public AuthUser findAuthUserByUsername(String username) {
        return authUserMapper.selectByUsername(username);
    }

    @Override
    public boolean lock(String username) {
        return authUserMapper.lockAuthUserByUsername(username) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AuthUser save(AuthUser authUser) {
        if (authUser == null) {
            return null;
        }
        authUser.setCreateTime(LocalDateTime.now());
        authUser.setUpdateTime(LocalDateTime.now());
        authUser.setVersion(1);
        authUserMapper.insertSelective(authUser);
        return authUser;
    }

    @Loggable
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AuthUser update(AuthUser authUser) {
        if (authUser == null) {
            return null;
        }
        Assert.notNull(authUser.getId(), "UserId is null");
        authUser.setUpdateTime(LocalDateTime.now());
        int changed = authUserMapper.updateByIdSelective(authUser);
        logger.info("Update authUser userId={}, result={}", authUser.getId(), changed > 0);
        return authUser;
    }

}
