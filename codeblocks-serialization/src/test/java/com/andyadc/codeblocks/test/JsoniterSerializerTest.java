package com.andyadc.codeblocks.test;

import com.andyadc.codeblocks.serialization.json.JsoniterSerializer;
import org.junit.Test;

import java.util.Date;

/**
 * @author andaicheng
 * @since 2018/4/6
 */
public class JsoniterSerializerTest {

    @Test
    public void toJson() {
        JsonDemo demo = new JsonDemo();
        demo.setDatetime(new Date());
        demo.setId(1);
        demo.setName("summer");
        demo.setNum(123456545L);

        System.out.println(JsoniterSerializer.toJson(demo));

    }
}
