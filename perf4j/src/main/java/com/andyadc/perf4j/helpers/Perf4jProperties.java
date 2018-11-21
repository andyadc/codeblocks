package com.andyadc.perf4j.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Perf4jProperties {

    public static final Properties INSTANCE;

    static {
        INSTANCE = new PackageParentProperties();
        final InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("perf4j.properties");
        if (is != null) {
            try {
                INSTANCE.load(is);
            } catch (IOException e) {
                System.err.println("Failed to load perf4j.properties " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private Perf4jProperties() {
        // singleton, not instantiable externally
    }
}
