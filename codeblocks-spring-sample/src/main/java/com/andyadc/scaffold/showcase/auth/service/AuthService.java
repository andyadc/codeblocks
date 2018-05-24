package com.andyadc.scaffold.showcase.auth.service;

import com.andyadc.scaffold.showcase.auth.entity.AuthUser;

/**
 * @author andaicheng
 * @version 2017/1/4
 */
public interface AuthService {

    AuthUser findAuthUserByUsername(String username);

    boolean lockAuthUser(String username);

    AuthUser saveAuthUser(AuthUser authUser);
}
