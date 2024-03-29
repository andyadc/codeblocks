package com.andyadc.codeblocks.kit.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanCopier {

    private static final Map<String, net.sf.cglib.beans.BeanCopier> beanCopierMap = new ConcurrentHashMap<>();

    private BeanCopier() {
    }

    public static void copy(Object source, Object target) {
        if (source == null || target == null) {
            return;
        }
        String beanKey = generateKey(source.getClass(), target.getClass());

        beanCopierMap.computeIfAbsent(beanKey, k ->
                net.sf.cglib.beans.BeanCopier.create(source.getClass(), target.getClass(), false)
        );

        net.sf.cglib.beans.BeanCopier copier = beanCopierMap.get(beanKey);

        copier.copy(source, target, null);
    }

    private static String generateKey(Class<?> clazz1, Class<?> clazz2) {
		return clazz1.toString() + clazz2.toString();
	}
}
