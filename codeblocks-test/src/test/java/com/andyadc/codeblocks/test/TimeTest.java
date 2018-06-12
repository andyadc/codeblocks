package com.andyadc.codeblocks.test;

import org.joda.time.DateTime;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * @author andy.an
 * @since 2018/6/12
 */
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class TimeTest {

    private static final int millis = 24 * 3600 * 1000;

    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder()
                .include(TimeTest.class.getName())
                .forks(1)   // 进行 fork 的次数。如果 fork 数是2的话，则 JMH 会 fork 出两个进程来进行测试
                .warmupIterations(5)    // 预热的迭代次数
                .measurementIterations(5)   //实际测量的迭代次数
                .build();
        new Runner(options).run();
    }

    @Benchmark
    @Threads(5)
    public void runCalendar() {
        Calendar calendar = Calendar.getInstance();
    }

    @Benchmark
    @Threads(5)
    public void runJoda() {
        DateTime dateTime = new DateTime();
    }

    @Benchmark
    @Threads(5)
    public void runSystem() {
        long result = System.currentTimeMillis() / millis;
    }
}
