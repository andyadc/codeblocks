package com.andyadc.scaffold.showcase.auth.security;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.stereotype.Component;

/**
 * @author andaicheng
 */
@Component
public class UserTokenGenerator {

    private static final String FIXED_KEY = "QWERTY";
    private static final long TOKEN_EXP_DATE = 60 * 60 * 24L;

    public String generatorToken(long userId) {
        long currentTime = System.currentTimeMillis() / 1000;
        long expDate = currentTime + TOKEN_EXP_DATE;
        String sign = userId + "-" + expDate + "-" + FIXED_KEY;
        sign = new Sha256Hash(sign).toString();
        sign = userId + "-" + expDate + "-" + sign;
        return sign;
    }

    public boolean checkToken(String token) {
        if (StringUtils.isNotBlank(token)) {
            String[] strs = token.split("-");
            if (strs.length == 3) {
                long time = Long.parseLong(strs[1]);
                if (System.currentTimeMillis() / 1000 > time) {
                    return false;
                }
                String sign = strs[0] + "-" + strs[1] + "-" + FIXED_KEY;
                sign = new Sha256Hash(sign).toString();
                return sign.equals(strs[2]);
            }
        }
        return false;
    }

}
