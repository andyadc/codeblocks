package com.andyadc.codeblocks.test;

import com.andyadc.codeblocks.kit.mapper.BeanCopier;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.beanutils.BeanUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author andy.an
 * @since 2018/6/14
 */
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class BeanCopyJmhTest {

    private static A a;

    static {
        a = buildA();
    }

    private static A buildA() {
        A a = new A();
        a.setAge(1);
        a.setName("a");
        a.setNum(123L);
        a.setTime(new Date());
        a.setList(Arrays.asList("apple", "orange"));
        a.setMap(ImmutableMap.of("tiger", 12));
        System.out.println(a);
        return a;
    }

    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder()
                .include(BeanCopyJmhTest.class.getName())
                .forks(1)
                .warmupIterations(5)
                .measurementIterations(5)
                .build();
        new Runner(options).run();
    }

    @Benchmark
    @Threads(5)
    public void runPure() {
        B b = new B();
        b.setAge(a.getAge());
        b.setList(a.getList());
        b.setMap(a.getMap());
        b.setName(a.getName());
        b.setNum(a.getNum());
        b.setTime(a.getTime());
    }

    @Benchmark
    @Threads(5)
    public void runByCglib() {
        B b = new B();
        BeanCopier.copy(a, b);
    }

    @Benchmark
    @Threads(5)
    public void runCommons() throws Exception {
        B b = new B();
        BeanUtils.copyProperties(b, a);
    }
}
