package com.andyadc.perf4j.helpers;

import java.util.Properties;

public class PackageParentProperties extends Properties {

    private static final long serialVersionUID = 4732002255463533934L;

    public PackageParentProperties() {
        super();
    }

    public PackageParentProperties(Properties defaults) {
        super(defaults);
    }

    @Override
    public synchronized Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }

        if (!(key instanceof String)) {
            return super.get(key);
        }

        String keyString = (String) key;

        Object o = super.get(keyString);
        if (o != null) {
            // found at current position
            return o;
        }

        // search parent if exists
        if (keyString.contains(".")) {
            // com.some.package.SomeClass -> com.some.package
            // com.some.package -> com.some
            // com.some -> com
            keyString = keyString.substring(0, keyString.lastIndexOf('.'));
            return get(keyString);
        } else {
            // all parent keys exhausted, look no further
            return null;
        }
    }

    @Override
    public String getProperty(String key) {
        Object oval = get(key);
        String sval = (oval instanceof String) ? (String) oval : null;
        return ((sval == null) && (defaults != null)) ? defaults.getProperty(key) : sval;
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        String val = getProperty(key);
        return (val == null) ? defaultValue : val;
    }
}
