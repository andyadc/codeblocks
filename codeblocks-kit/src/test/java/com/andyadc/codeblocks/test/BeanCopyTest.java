package com.andyadc.codeblocks.test;

import com.andyadc.codeblocks.kit.bean.BeanCopier;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;

/**
 * @author andy.an
 * @since 2018/4/23
 */
public class BeanCopyTest {

    private static final A a;

    static {
        a = new A();
        a.setAge(1);
        a.setName("a");
        a.setNum(123L);
        a.setTime(new Date());
        a.setList(Arrays.asList("apple", "orange"));
        a.setMap(ImmutableMap.of("tiger", 12));

        System.out.println(a);
    }

    @Test
    public void testPure() {
        B b = new B();
        b.setAge(a.getAge());
        b.setList(a.getList());
        b.setMap(a.getMap());
        b.setName(a.getName());
        b.setNum(a.getNum());
        b.setTime(a.getTime());

        System.out.println(b);
    }

    @Test
    public void testCglib() {
        B b = new B();
        BeanCopier.copy(a, b);
        System.out.println(b);
    }

}
