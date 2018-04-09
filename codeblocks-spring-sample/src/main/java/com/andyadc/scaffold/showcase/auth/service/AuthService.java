package com.andyadc.scaffold.showcase.auth.service;

import com.andyadc.scaffold.showcase.auth.entity.AuthUser;

/**
 * @author andaicheng
 * @version 2017/1/4
 */
public interface AuthService {

    AuthUser findAuthUserByAccount(String account);

    boolean lockAuthUser(String account);

    AuthUser saveAuthUser(AuthUser authUser);
}
