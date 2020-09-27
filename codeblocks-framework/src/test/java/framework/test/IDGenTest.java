package framework.test;

import com.andyadc.codeblocks.framework.idgen.IDGen;
import com.andyadc.codeblocks.framework.idgen.Result;
import com.andyadc.codeblocks.framework.idgen.snowflake.SnowflakeIDGenImpl;
import org.junit.jupiter.api.Test;

/**
 * andy.an
 */
public class IDGenTest {

    @Test
    public void testSnowflakeZkIDGen() {
        IDGen idGen = new SnowflakeIDGenImpl("www.qq-server.com:2181", 8080);
        Result result = idGen.gen("test");
        System.out.println(result.getId());
    }
}
