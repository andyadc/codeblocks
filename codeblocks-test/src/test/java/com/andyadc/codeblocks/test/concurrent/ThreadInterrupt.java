package com.andyadc.codeblocks.test.concurrent;

/**
 * @author andy.an
 * @since 2018/5/29
 */
public class ThreadInterrupt {
    public static void main(String[] args) throws InterruptedException {
        Interrupt interrupt = new Interrupt();
        interrupt.start();

        Thread.sleep(1000);
        System.out.println(interrupt.isInterrupted());
        // 让线程变为中断状态
        interrupt.interrupt();
        System.out.println(interrupt.isInterrupted());
    }
}

class Interrupt extends Thread {

    @Override
    public void run() {
        try {
            System.out.println("before sleep");
            Thread.sleep(10000);
            System.out.println("after sleep");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //检查并清除中断状态
//        System.out.println("Thread.interrupted: " + Thread.interrupted());
    }
}
