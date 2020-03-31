package com.andyadc.ssm.test;

import com.andyadc.ssm.persistence.entity.Demo;
import com.andyadc.ssm.persistence.mapper.DemoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * andy.an
 * 2020/3/30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext.xml"})
public class MapperTests {

	@Autowired
	private DemoMapper demoMapper;

	@Test
	public void testSelectByChoose() {
		Demo demo = new Demo();
		demo.setId(231L);
		List<Demo> demoList = demoMapper.selectByChoose(demo);
		demoList.forEach(d -> System.out.println(d.getId() + " - " + d.getName()));
	}

	@Test
	public void testSelectByWhere() {
		Demo demo = new Demo();
//		demo.setId(231L);
		List<Demo> demoList = demoMapper.selectByWhere(demo);
		demoList.forEach(d -> System.out.println(d.getId() + " - " + d.getName()));
	}

	@Test
	public void testSelectByArray() {
		Long[] idArray = {1L, 2L, 3L};
		List<Demo> demoList = demoMapper.selectByTypeArray(idArray);
		demoList.forEach(d -> System.out.println(d.getName()));
	}

	@Test
	public void testSelectByList() {
		List<Demo> demoList = demoMapper.selectByTypeList(Arrays.asList(1L, 2L, 3L));
		demoList.forEach(d -> System.out.println(d.getName()));
	}

	@Test
	public void testBatchInsert() {
		List<Demo> demoList = new ArrayList<>();
		Demo d1 = new Demo();
		d1.setName("u" + ThreadLocalRandom.current().nextInt());
		d1.setStatus(1);
		d1.setType(1);

		Demo d2 = new Demo();
		d2.setName("u" + ThreadLocalRandom.current().nextInt());
		d2.setStatus(1);
		d2.setType(1);

		demoList.add(d1);
		demoList.add(d2);

		demoMapper.batchInsert(demoList);
	}

	@Test
	public void testSelectByNameLike() {
		List<Demo> demoList = demoMapper.selectByNameLike("u-");
		demoList.forEach(d -> System.out.println(d.getName() + " - " + d.getCreateTime()));
	}

	@Test
	public void testUpdateByMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", 235L);
		map.put("name", "aaa");
		demoMapper.updateByMap(map);
	}
}
