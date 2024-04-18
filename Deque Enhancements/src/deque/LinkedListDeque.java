package deque;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LinkedListDeque<T> implements Deque<T> {

    @Override
    public Iterator<T> iterator() {
        return new LLDequeIterator();
    }
    private class LLDequeIterator implements Iterator<T> {
        private Node pos;

        public LLDequeIterator() {
            pos = sentinel.next;
        }
        public boolean hasNext() {
            return pos != sentinel;
        }

        public T next() {
            T returnitem = pos.item;
            pos = pos.next;
            return returnitem;
        }
    }

    private class Node {
        T item;
        Node next;
        Node prev;

        public Node(Node p, T i, Node n) {
            item = i;
            next = n;
            prev = p;
        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T x) {
        size++;
        Node newNode = new Node(sentinel, x, sentinel.next);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
    }

    @Override
    public void addLast(T x) {
        size++;
        Node newNode = new Node(sentinel.prev, x, sentinel);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        Node current = sentinel;
        while (current.next != sentinel) {
            returnList.add(current.next.item);
            current = current.next;
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T returnV = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size--;
        return returnV;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T returnV = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size--;
        return returnV;
    }

    @Override
    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        Node current = sentinel;
        while (index >= 0) {
            current = current.next;
            index--;
        }
        return current.item;
    }

    @Override
    public T getRecursive(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        return helper(index, sentinel.next);
    }

    private T helper(int index, Node next) {
        if (index == 0) {
            return next.item;
        }
        return helper(--index, next.next);
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof Deque otherDeque) {
            return this.toList().equals(otherDeque.toList());
        }
        return false;
    }

    @Override
    public String toString() {
        return this.toList().toString();
    }
    public static void main(String[] args) {
        Deque<Integer> lld = new LinkedListDeque<>();
    }
}



