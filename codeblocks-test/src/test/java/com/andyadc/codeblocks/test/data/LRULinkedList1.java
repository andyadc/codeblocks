package com.andyadc.codeblocks.test.data;

/**
 * @author andy.an
 * @since 2018/10/8
 */
public class LRULinkedList1 {

    Node tail;
    private int max = 10;

    public LRULinkedList1() {
    }

    public LRULinkedList1(int max) {
        this.max = max;
    }

    public void show() {
        Node c = tail;
        while (c != null && c.data != null) {
            System.out.println(c.data);
            c = c.next;
        }
        System.out.println();
    }

    public boolean add(Object obj) {
        return addAfter(obj);
    }

    public boolean addBefore(Object obj) {
        Node node = new Node(obj, null);
        if (tail == null) {
            tail = node;
            return true;
        }
        node.next = tail;
        tail = node;

        return true;
    }

    public boolean addAfter(Object obj) {
        Node node = new Node(obj, null);
        if (tail == null) {
            tail = node;
            return true;
        }
        Node c = tail;
        while (c.next != null) {
            c = c.next;
        }
        c.next = node;

        return true;
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
