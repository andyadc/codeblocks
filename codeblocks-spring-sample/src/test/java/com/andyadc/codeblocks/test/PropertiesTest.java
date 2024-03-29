package com.andyadc.codeblocks.test;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesTest {

    @Test
    public void testRead() throws Exception {
        InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream("META-INF/app.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        System.out.println(properties.getProperty("app.id"));
    }
}
