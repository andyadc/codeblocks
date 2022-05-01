package com.andyadc.codeblocks.test;

import com.andyadc.codeblocks.serialization.binary.KryoSerializer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class KryoTests {

	@Test
	public void testKryo() {
		Bean bean = new Bean();
		bean.setNum(1023543L);
		bean.setFlag(true);
		bean.setHeight(123F);
		bean.setId(12423);
		bean.setName("qwerty");

		System.out.println(bean);

		byte[] bytes = KryoSerializer.serialize(bean);
		System.out.println(bytes.length);
		System.out.println(Arrays.toString(bytes));

		Bean dbean = KryoSerializer.deserialize(bytes, Bean.class);
		System.out.println(dbean);
	}
}
