package com.andyadc.algo.list;

import java.util.Objects;

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

    public boolean remove(Object o) {
        Node<E> t = head;
        Node<E> preNode = head;
        while (t != null) {
            if (Objects.equals(o, t.e)) {
                preNode.next = t.next;
                return true;
            } else {
                preNode = t;
                t = t.next;
            }
        }
        return false;
    }

    public boolean contains(Object o) {
        Node<E> t = head;
        while (t != null) {
            if (Objects.equals(o, t.e)) {
                return true;
            }
            t = t.next;
        }
        return false;
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
        Node<E> t = head;
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        while (t != null) {
            builder.append(t.e);
            if (t.next != null) {
                builder.append(',').append(' ');
            }
            t = t.next;
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
