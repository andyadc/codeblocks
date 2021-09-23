package com.andyadc.codeblocks.test.concurrent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andy.an
 * @since 2018/5/25
 */
public class WriteReadThread {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
//        List<Integer> list = Collections.synchronizedList(new ArrayList<>());
//        List<Integer> list = new CopyOnWriteArrayList<>();
        new WriteThread(list).start();
        System.out.println("main" + list);
        new ReadThread(list).start();
    }
}

class ReadThread extends Thread {

    private final List<Integer> list;

    public ReadThread(List<Integer> list) {
        super("ReadThread");
        this.list = list;
    }

    @Override
    public void run() {
        for (; ; ) {
            System.out.println("reader before: " + list);
            for (Integer i : list) {
                System.out.println("reader: " + i);
            }
        }

    }
}

class WriteThread extends Thread {

    private final List<Integer> list;

    public WriteThread(List<Integer> list) {
        super("WriteThread");
        this.list = list;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            list.add(i);
            System.out.println(list);
            list.remove(0);
            System.out.println(list);
            System.out.println("\r\n");
        }
    }
}
