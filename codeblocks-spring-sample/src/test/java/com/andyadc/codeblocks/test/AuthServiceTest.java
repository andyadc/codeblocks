package com.andyadc.codeblocks.test;

import com.andyadc.scaffold.showcase.auth.entity.AuthUser;
import com.andyadc.scaffold.showcase.auth.service.AuthService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author andaicheng
 * @version 2017/4/24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext.xml"})
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    public void testSaveAuthUser() {
        AuthUser authUser = new AuthUser();
        authUser.setId(5L);
        authUser.setUsername("");
        authUser.setNickname("");
        authUser.setPassword("54erwdfge2");
        authUser.setSalt("ssss");
        authUser.setStatus(1);
        authUser.setDeleted(0);
        authUser = authService.saveAuthUser(authUser);
        System.out.println(authUser.getId());
    }

    @Before
    public void before() {
        System.out.println("------------------------------------------------");
    }

    @After
    public void after() {
        System.out.println("------------------------------------------------");
    }
}
