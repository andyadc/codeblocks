package com.andyadc.codeblocks.test;

import com.andyadc.codeblocks.kit.RandomUtil;
import com.andyadc.codeblocks.showcase.auth.entity.AuthUser;
import com.andyadc.codeblocks.showcase.auth.service.AuthService;
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
	public void testGet() throws Exception {
		AuthUser authUser = authService.findAuthUserById(43L);
		System.out.println(authUser);
	}

    @Test
	public void testUpdate() throws Exception {
        AuthUser authUser = new AuthUser();
		authUser.setId(21L);
        authService.update(authUser);
    }

    @Test
    public void testSave() {
        AuthUser authUser = new AuthUser();
		authUser.setUsername(RandomUtil.genRandomStr(7));
		authUser.setNickname(authUser.getUsername().substring(0, 5));
		authUser.setNickname("😉emojis!~");
		authUser.setPassword(RandomUtil.genRandomStr(11));
		authUser.setSalt(RandomUtil.genRandomNum(9));
        authUser.setStatus(1);
        authUser.setDeleted(0);
        authUser = authService.save(authUser);
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
