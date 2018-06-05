package com.andyadc.codeblocks.showcase.auth.service;

import com.andyadc.codeblocks.showcase.auth.entity.AuthUser;

/**
 * @author andaicheng
 * @version 2017/1/4
 */
public interface AuthService {

    AuthUser findAuthUserByUsername(String username);

    boolean lock(String username);

    AuthUser save(AuthUser authUser);

    AuthUser update(AuthUser authUser);
}
