package com.andyadc.codeblocks.showcase.auth.security;

import com.andyadc.codeblocks.showcase.auth.entity.AuthUser;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

/**
 * @author andaicheng
 */
@Component
public class PasswordHelper {

    private static final RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    private static final String ALGORITHM_NAME = "SHA-256";
    private static final int HASH_ITERATIONS = 1024;

    private static final HashedCredentialsMatcher matcher;

    static {
        matcher = new HashedCredentialsMatcher(ALGORITHM_NAME);
        matcher.setHashIterations(HASH_ITERATIONS);
    }

    /**
     * Encrypt user password
     */
    public void encryptPassword(AuthUser user) {
        user.setSalt(randomNumberGenerator.nextBytes().toHex());
        String password = new SimpleHash(ALGORITHM_NAME,
                user.getPassword(),
                ByteSource.Util.bytes(user.getCredentialsSalt()),
                HASH_ITERATIONS).toHex();
        user.setPassword(password);
    }

    /**
     * Verify user password
     *
     * @return true/false
     */
    public boolean verifyPassword(AuthUser user, String plainPassword) {
        AuthenticationToken token = new UsernamePasswordToken(user.getUsername(), plainPassword);
        AuthenticationInfo info = new SimpleAuthenticationInfo(user.getUsername(),
                user.getPassword(),
                ByteSource.Util.bytes(user.getCredentialsSalt()),
                "verifyPassword");
        return matcher.doCredentialsMatch(token, info);
    }
}
