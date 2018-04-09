package com.andyadc.scaffold.showcase.auth.security;

import com.andyadc.scaffold.showcase.auth.entity.AuthUser;
import com.andyadc.scaffold.showcase.auth.enums.AuthUserState;
import com.andyadc.scaffold.showcase.auth.service.AuthService;
import com.andyadc.scaffold.showcase.common.enums.DeletionEnum;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author andaicheng
 */
public class ShiroDbRealm extends AuthorizingRealm {

    private AuthService authService;

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole("user");
        return info;
    }

    /**
     * 认证回调函数,登录时调用.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        AuthUser authUser = authService.findAuthUserByAccount(usernamePasswordToken.getUsername());
        if (authUser == null) {
            throw new UnknownAccountException();
        }
        if (DeletionEnum.DELETED.getState() == authUser.getIsDeleted()) {
            throw new UnknownAccountException();
        }
        if (AuthUserState.BLOCKED.getState() == authUser.getState()) {
            throw new LockedAccountException();
        }

        return new SimpleAuthenticationInfo(new ShiroUser(authUser.getId(), authUser.getAccount()), authUser.getPassword(),
                ByteSource.Util.bytes(authUser.getCredentialsSalt()), "Shiro Db Realm");
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    public static class ShiroUser implements Serializable {

        private static final long serialVersionUID = 4343812234185635487L;
        private Long id;
        private String account;

        public ShiroUser(Long id, String account) {
            this.id = id;
            this.account = account;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }


        @Override
        public int hashCode() {
            return Objects.hashCode(account);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ShiroUser other = (ShiroUser) obj;
            if (account == null) {
                return other.account == null;
            } else return account.equals(other.account);
        }
    }
}
