package com.andyadc.codeblocks.kit.mapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author andy.an
 * @since 2018/4/23
 */
public class BeanCopier {

    private static final Map<String, net.sf.cglib.beans.BeanCopier> beanCopierMap = new ConcurrentHashMap<>();

    public static void copy(Object source, Object target) {
        if (source == null || target == null) {
            return;
        }
        String beanKey = generateKey(source.getClass(), target.getClass());
        net.sf.cglib.beans.BeanCopier copier = beanCopierMap.get(beanKey);
        if (copier == null) {
            copier = net.sf.cglib.beans.BeanCopier.create(source.getClass(), target.getClass(), false);
            beanCopierMap.put(beanKey, copier);
        }
        copier.copy(source, target, null);
    }

    private static String generateKey(Class clazz1, Class clazz2) {
        return clazz1.toString() + clazz2.toString();
    }
}
