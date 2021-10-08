package com.andyadc.codeblocks.test.lambda;

import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * andy.an
 */
public class LambdaTest {

	public static void main(String[] args) {
		// a = (s) -> System.out.println("1");
		A a = (msg) -> System.out.println(msg);
		a.print("hello");

		A b = System.out::println;
		b.print("world");

		B b1 = (t) -> t.print("asas");


		B b2 = (A) -> {
			System.out.println();
			A a1 = System.out::println;
			a1.print("2323");
		};
		b2.say(a);
	}

	@Test
	public void testPredicate() {
		Predicate<Integer> atLeast = x -> x > 7;
		boolean test = atLeast.test(1);
		System.out.println(test);

		Predicate<Integer> negate = atLeast.negate();
		boolean test1 = negate.test(1);
		System.out.println(test1);
	}

	@Test
	public void testUnaryOperator() {
		UnaryOperator<Integer> operator = (x) -> -x;
		System.out.println(operator.apply(1));

		Object apply = UnaryOperator.identity().apply(1);
		System.out.println(apply);
	}

	@Test
	public void testBinaryOperator() {
		BinaryOperator<Long> plus = (x, y) -> x + y + 1;

		Long r = plus.apply(15L, 2L);
		System.out.println(r);
	}

	@Test
	public void testFunction() {
		Function<String, Long> fun = (x) -> Long.parseLong(x) + 110L;

		Long apply = fun.apply("123");
		System.out.println(apply);
	}

	@Test
	public void testSupplier() {
		Supplier<String> supplier = () -> "xxx";
		String s = supplier.get();
		System.out.println(s);

		ThreadLocal<DateFormat> threadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("dd-MMM-yyyy"));
	}

	@Test
	public void testRunnable() {
		Runnable runnable = () -> System.out.println("bla bla");

	}

	interface IntPred {
		boolean test(Integer value);
	}

	interface II {
		boolean check(Predicate<Integer> predicate);
	}

	interface III {
		boolean check(IntPred predicate);
	}

	interface A {
		static void testA() {

		}

		void print(String msg);
	}

	interface B {
		void say(A a);
	}

}
