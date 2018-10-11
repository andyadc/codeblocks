package com.andyadc.codeblocks.showcase.auth.security;

import com.andyadc.codeblocks.common.enums.Deletion;
import com.andyadc.codeblocks.showcase.auth.entity.AuthUser;
import com.andyadc.codeblocks.showcase.auth.enums.UserStatus;
import com.andyadc.codeblocks.showcase.auth.service.AuthService;
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
        AuthUser authUser = authService.findAuthUserByUsername(usernamePasswordToken.getUsername());
        if (authUser == null) {
            throw new UnknownAccountException();
        }
        if (Deletion.DELETED.getState() == authUser.getDeleted()) {
            throw new UnknownAccountException();
        }
        if (UserStatus.LOCKED.getStatus() == authUser.getStatus()) {
            throw new LockedAccountException();
        }

        return new SimpleAuthenticationInfo(new ShiroUser(authUser.getId(), authUser.getUsername()), authUser.getPassword(),
                ByteSource.Util.bytes(authUser.getCredentialsSalt()), "ShiroDbRealm");
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    public static final class ShiroUser implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long id;
        private String username;

        ShiroUser(Long id, String username) {
            this.id = id;
            this.username = username;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(username);
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
            if (username == null) {
                return other.username == null;
            } else return username.equals(other.username);
        }
    }
}
