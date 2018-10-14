package com.andyadc.algo.test;

import com.andyadc.algo.list.SinglyLinkedList;
import org.junit.Test;

/**
 * @author andy.an
 * @since 2018/10/11
 */
public class SinglyLinkedListTest {

    @Test
    public void testAdd() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");

        System.out.println(list);
        System.out.println(list.size());
    }

    @Test
    public void testRemove() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        System.out.println(list);
        list.remove("b");
        System.out.println(list);
    }

    @Test
    public void testContains() {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        list.add("a");

        System.out.println(list.contains("a"));
        System.out.println(list.contains("e"));
    }

    @Test
    public void testReverse() {

    }

    @Test
    public void testIsLoop() {

    }

}
