package com.andyadc.codeblocks.test.data;

import org.junit.Test;

import java.util.LinkedList;

/**
 * @author andy.an
 * @since 2018/10/8
 */
public class DataTest {

    @Test
    public void testLRULinkedList1() {
        LRULinkedList1 list = new LRULinkedList1();
        list.add("abc");
        list.add("wdc");
        list.add("qwe");
        list.add("rty");

        list.show();
    }

    @Test
    public void testLinkedList() {
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("a");
        linkedList.add("b");
        linkedList.add("c");

        System.out.println(linkedList);
    }
}
