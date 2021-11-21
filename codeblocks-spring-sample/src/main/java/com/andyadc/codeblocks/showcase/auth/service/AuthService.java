package com.andyadc.codeblocks.showcase.auth.service;

import com.andyadc.codeblocks.showcase.auth.entity.AuthUser;

public interface AuthService {

    AuthUser findAuthUserByUsername(String username);

	AuthUser findAuthUserById(Long id);

    boolean lock(String username);

    AuthUser save(AuthUser authUser);

    AuthUser update(AuthUser authUser);
}
