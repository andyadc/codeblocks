package com.andyadc.codeblocks.test.netty.im;

import io.netty.channel.Channel;
import io.netty.util.Attribute;

/**
 * @author andy.an
 * @since 2018/10/16
 */
public class LoginUtil {

    public static void markAsLogin(Channel channel) {
        channel.attr(Attributes.LOGIN).set(true);
    }

    public static boolean hasLogin(Channel channel) {
        Attribute<Boolean> loginAttr = channel.attr(Attributes.LOGIN);
        return loginAttr.get() != null;
    }
}
