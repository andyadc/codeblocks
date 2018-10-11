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

        System.out.println(list.toString());
        System.out.println(list.size());
    }
}
