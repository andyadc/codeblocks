package com.andyadc.codeblocks.test;

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

    interface A {
        void print(String msg);
    }

    interface B {
        void say(A a);
    }

}
