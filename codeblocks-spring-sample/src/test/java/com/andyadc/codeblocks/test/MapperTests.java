package com.andyadc.codeblocks.test;

import com.andyadc.codeblocks.showcase.sys.entity.SpringTransaction;
import com.andyadc.codeblocks.showcase.sys.mapper.SpringTransactionMapper;
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

/**
 * andy.an
 * 2020/3/27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext.xml"})
public class MapperTests {

	@Autowired
	private SpringTransactionMapper springTransactionMapper;

	@Test
	public void testBatchInsert() {
		List<SpringTransaction> list = new ArrayList<>();
		list.add(new SpringTransaction("qqqqww", 22, 119L));
		list.add(new SpringTransaction("eww", 22, 119L));
		list.add(new SpringTransaction("fdss", 22, 119L));
		list.add(new SpringTransaction("yttg", 22, 119L));
		springTransactionMapper.batchInsert(list);
	}

	@Test
	public void testSelectByList() {
		List<Long> idList = Arrays.asList(1L, 2L, 3L, 4L, 5L);
		List<SpringTransaction> list = springTransactionMapper.selectByIdList(idList);
		list.forEach(s -> System.out.println(s.getName()));
	}

	@Test
	public void testSelectByArray() {
		Long[] idArray = {1L, 2L, 3L, 4L, 5L};
		List<SpringTransaction> list = springTransactionMapper.selectByIdArray(idArray);
		list.forEach(s -> System.out.println(s.getName()));
	}

	@Test
	public void testUpdateByMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", 1L);
		map.put("name", "map");
		map.put("age", 123);
		springTransactionMapper.updateByMap(map);
	}

	@Test
	public void testSelectByName() {
		List<SpringTransaction> list = springTransactionMapper.selectByName("a");
		list.forEach(s -> System.out.println(s.getName()));
	}
}
