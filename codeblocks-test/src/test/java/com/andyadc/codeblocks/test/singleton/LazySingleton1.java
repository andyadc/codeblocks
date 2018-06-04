package com.andyadc.codeblocks.test.singleton;

/**
 * 懒汉式v1
 *
 * @author andy.an
 * @since 2018/6/4
 */
public class LazySingleton1 {

    private static LazySingleton1 INSTANCE;

    private LazySingleton1() {
    }

    //    非线程安全
    public static LazySingleton1 getInstance1() {
        if (INSTANCE == null) {
            INSTANCE = new LazySingleton1();
        }
        return INSTANCE;
    }

    //    线程安全
    public synchronized static LazySingleton1 getInstance2() {
        if (INSTANCE == null) {
            INSTANCE = new LazySingleton1();
        }
        return INSTANCE;
    }

    // 双重校验锁
    public static LazySingleton1 getInstance3() {
        if (INSTANCE == null) {
            synchronized (LazySingleton1.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LazySingleton1();
                }
            }
        }
        return INSTANCE;
    }
}
