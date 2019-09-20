package com.andyadc.codeblocks.test;

import com.andyadc.codeblocks.kit.idgen.UUID;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * andy.an
 */
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class UUIDTest {

	public static void main(String[] args) throws Exception {
		Options options = new OptionsBuilder()
			.include(UUIDTest.class.getName())
			.forks(1)
			.warmupIterations(5)
			.measurementIterations(5)
			.build();
		new Runner(options).run();
	}

	@Benchmark
	@Threads(5)
	public void test01() {
		UUID.fastUUID();
	}

	@Benchmark
	@Threads(5)
	public void test02() {
		UUID.randomUUID();
	}
}
