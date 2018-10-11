package com.andyadc.algo.list;

/**
 * Singly-linked list
 * <p>
 * <pre>
 *              head
 *        +-------------+       +-------------+       +-------------+
 *       | data | next | ----> | data | next | ----> | data | next | ----> NULL
 *      +-------------+       +-------------+       +-------------+
 * </pre>
 *
 * @author andy.an
 * @since 2018/10/11
 */
public class SinglyLinkedList<E> {

    private int size = 0;

    private Node<E> head;

    public SinglyLinkedList() {
    }

    public boolean add(E e) {
        addLast(e);
        return true;
    }

    void addFirst(E e) {
        final Node<E> newNode = new Node<>(e, null);
        if (head == null) {
            head = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
        size++;
    }

    void addLast(E e) {
        final Node<E> newNode = new Node<>(e, null);
        if (head == null) {
            head = newNode;
        } else {
            Node<E> t = head;
            while (t.next != null) {
                t = t.next;
            }
            t.next = newNode;
        }
        size++;
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        if (head == null) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        while (head != null) {
            builder.append(head.e);
            if (head.next != null) {
                builder.append(',').append(' ');
            }
            head = head.next;
        }
        return builder.append(']').toString();
    }

    private static class Node<E> {
        E e;
        Node<E> next;

        Node(E e, Node<E> next) {
            this.e = e;
            this.next = next;
        }
    }
}
