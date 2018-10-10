package com.andyadc.codeblocks.test;

import com.fasterxml.uuid.Generators;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author andy.an
 * @since 2018/9/29
 */
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class UUIDTest {

    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder()
                .include(UUIDTest.class.getName())
                .forks(1)   // 进行 fork 的次数。如果 fork 数是2的话，则 JMH 会 fork 出两个进程来进行测试
                .warmupIterations(5)    // 预热的迭代次数
                .measurementIterations(5)   //实际测量的迭代次数
                .build();
        new Runner(options).run();
    }

    @Benchmark
    @Threads(5)
    public void internalUUID() {
        String uuid = UUID.randomUUID().toString();
    }

    @Benchmark
    @Threads(5)
    public void fastxmlUUID() {
        String uuid = Generators.randomBasedGenerator().generate().toString();
    }

    @Benchmark
    @Threads(5)
    public void eaioUUID() {
        String uuid = new com.eaio.uuid.UUID().toString();
    }

}
