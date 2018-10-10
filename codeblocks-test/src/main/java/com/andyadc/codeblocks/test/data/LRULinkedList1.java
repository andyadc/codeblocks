package com.andyadc.codeblocks.test.data;

/**
 * @author andy.an
 * @since 2018/10/8
 */
public class LRULinkedList1 {

    Node current;
    private int max = 10;

    public LRULinkedList1() {
    }

    public LRULinkedList1(int max) {
        this.max = max;
    }

    public void add(Object obj) {

        if (current == null) {

        }
    }

    private static class Node {
        Object data;
        Node next;

        public Node(Object data, Node next) {
            this.data = data;
            this.next = next;
        }
    }
}
