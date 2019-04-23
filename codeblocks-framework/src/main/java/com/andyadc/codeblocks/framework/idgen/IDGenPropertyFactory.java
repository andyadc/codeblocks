package com.andyadc.codeblocks.framework.idgen;

import java.io.IOException;
import java.util.Properties;

/**
 * andy.an
 */
public class IDGenPropertyFactory {

    private static final String PROPS_NAME = "idgen.properties";
    private static final Properties prop = new Properties();

    static {
        try {
            prop.load(IDGenPropertyFactory.class.getClassLoader().getResourceAsStream(PROPS_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Properties getProperties() {
        return prop;
    }
}
