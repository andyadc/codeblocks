package com.andyadc.codeblocks.test.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class FastjsonTests {

	@Test
	public void testString2Map() {
		String jsonStr = "{\"name\":\"adc\",\"alias\":\"aa\",\"age\":\"18\"}";
//		jsonStr = null;
		Map<String, String> map = JSON.parseObject(jsonStr, new TypeReference<Map<String, String>>() {
		});
		System.out.println(map);
	}

	@Test
	public void testMap2String() {
		Map<String, String> map = new HashMap<>();
		map.put("name", "adc");
		map.put("age", "18");
		map.put("alias", "aa");

		String jsonString = JSON.toJSONString(map);
		System.out.println(jsonString);
	}
}
