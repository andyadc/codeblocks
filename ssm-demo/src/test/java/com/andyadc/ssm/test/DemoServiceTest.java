package com.andyadc.ssm.test;

import com.andyadc.ssm.persistence.entity.Demo;
import com.andyadc.ssm.service.DemoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author andy.an
 * @since 2018/9/29
 */
@ExtendWith(SpringExtension.class)
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
