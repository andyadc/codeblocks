package com.andyadc.codeblocks.test.json;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FastjsonTests {

	@Test
	public void testString2Map() {
		String jsonStr = "{\"name\":\"adc\",\"alias\":\"aa\",\"age\":\"18\"}";

		Map<String, String> map = JSON.parseObject(jsonStr, new TypeReference<Map<String, String>>() {
		});
		System.out.println(map);
	}

	@Test
	public void testMap2String() {
		Map<String, Object> map = new HashMap<>();
		map.put("name", "adc");
		map.put("age", "18");
		map.put("alias", "aa");
		map.put("date", LocalDateTime.now());
		map.put("createTime", new Date());

		String jsonString = JSON.toJSONString(map);
		System.out.println(jsonString);
	}
}
