package com.andyadc.codeblocks.test.mvel;

import org.junit.jupiter.api.Test;
import org.mvel2.MVEL;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MvelTests {

	@Test
	public void testValueCompare() {
		String expr = "(age > 10 && age <=20) || age == 7";
		Serializable compiled = MVEL.compileExpression(expr);

		Map<String, Object> params = new HashMap<>();
		params.put("name", "adc");
		params.put("age", "7");
		Object result = MVEL.executeExpression(compiled, params);

		System.out.println(result);
	}

	@Test
	public void testValueReturn() {
		String expr = "a = 10; b = (a = a * 2) + 10; a;";
		Serializable compiled = MVEL.compileExpression(expr);

		Map<String, Object> params = new HashMap<>();
		Object result = MVEL.executeExpression(compiled, params);

		System.out.println(result);
	}
}
