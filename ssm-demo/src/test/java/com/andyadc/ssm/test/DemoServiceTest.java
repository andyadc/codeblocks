package com.andyadc.ssm.test;

import com.andyadc.ssm.persistence.entity.Demo;
import com.andyadc.ssm.service.DemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author andy.an
 * @since 2018/9/29
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext.xml"})
public class DemoServiceTest {

	@Autowired
	private DemoService demoService;

	@Test
	public void testAdd() {
		Demo demo = new Demo();
		demo.setName("u" + ThreadLocalRandom.current().nextInt());
		demo.setStatus(2);
		demo.setType(2);
		demo.setCreateTime(LocalDateTime.now());
		demo.setUpdateTime(LocalDateTime.now());
		demoService.add(demo);
	}
}
