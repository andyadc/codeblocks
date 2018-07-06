package com.andyadc.codeblocks.config;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Configuration;

/**
 * @author andy.an
 * @since 2018/7/6
 */
@Configuration
public class ShiroConfig {

    public ShiroFilterFactoryBean buildShiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();


        return shiroFilterFactoryBean;
    }

    public SecurityManager buildSecurityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        return securityManager;
    }
}
