package com.andyadc.codeblocks.test.singleton;

/**
 * @author andy.an
 * @since 2018/6/4
 */
public class LazySingleton2 {

    private LazySingleton2() {
    }

    public static LazySingleton2 getInstance() {
        return SingleHolder.instance;
    }

    private static class SingleHolder {
        private static final LazySingleton2 instance = new LazySingleton2();
    }
}
