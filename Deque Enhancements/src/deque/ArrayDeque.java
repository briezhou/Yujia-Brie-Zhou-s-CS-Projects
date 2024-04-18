package deque;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayDeque<T> implements Deque<T> {
    private T[] array;
    private int size;
    private int nextFirst;
    private int nextLast;

    private final int MAGICNUM = 16;
    public ArrayDeque() {
        array = (T[]) new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }


    @Override
    public void addFirst(T x) {
        if (size == array.length) {
            resizeAdd(array.length * 2);
        }
        array[nextFirst] = x;
        if (nextFirst != 0) {
            nextFirst -= 1;
        } else {
            nextFirst = array.length - 1;
        }
        size += 1;
    }

    @Override
    public void addLast(T x) {
        if (size == array.length) {
            resizeAdd(array.length * 2);
        }
        array[nextLast] = x;
        nextLast = (nextLast + 1) % array.length;
        size += 1;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        int i = 0;
        while (i < size) {
            returnList.add(get(i));
            i++;
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

    private void resize(int size1) {
        T[] smaller = (T[]) new Object[size1];
        for (int i = 0; i < nextLast; i++) {
            smaller[i] = array[i];
        }
        for (int i = nextLast; i < size; i++) {
            smaller[i + size1 / 2] = array[i];
        }
        if (nextFirst > nextLast) {
            nextFirst = nextFirst + size1 / 2;
        }
        array = smaller;
    }

    private void resizeAdd(int size1) {
        T[] bigger = (T[]) new Object[size1];
        for (int i = nextLast; i < size; i++) {
            bigger[i - nextLast] = array[i];
        }
        for (int i = 0; i < nextLast; i++) {
            bigger[size - nextLast + i] = array[i];
        }
        nextLast = size;
        nextFirst = size1 - 1;
        array = bigger;
    }

    private void resizeRemove(int size1) {
        T[] smaller = (T[]) new Object[size1];
        int index = 0;
        if (nextFirst > nextLast) {
            for (int i = nextFirst + 1; i < array.length; i++) {
                smaller[index] = array[i];
                index++;
            }
            for (int i = 0; i < nextLast; i++) {
                smaller[index] = array[i];
                index++;
            }
        } else {
            System.arraycopy(array, nextFirst + 1, smaller, 0, size);
        }
        nextLast = size;
        nextFirst = size1 - 1;
        array = smaller;
    }
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        } else {
            if (array.length > MAGICNUM && size <= 0.25 * array.length) {
                resizeRemove(array.length / 2);
            }
            int num = (nextFirst + 1) % array.length;
            T item = array[num];
            array[num] = null;
            nextFirst = num;
            size -= 1;
            return item;
        }
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        } else {
            if (array.length > MAGICNUM && size <= 0.25 * array.length) {
                resizeRemove(array.length / 2);
            }
            int num;
            if (nextLast < 1) {
                num = array.length - 1;
            } else {
                num = nextLast - 1;
            }
            T item = array[num];
            array[num] = null;
            nextLast = num;
            size -= 1;
            return item;
        }
    }

    @Override
    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        } else {
            int i = (nextFirst + index + 1) % array.length;
            return array[i];
        }
    }

    @Override
    public T getRecursive(int index) {
        return get(index);
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int pos;
        private int num;

        public ArrayDequeIterator() {
            pos = (nextFirst + 1) % array.length;
            num = 0;
        }

        public boolean hasNext() {
            return num < size;
        }

        public T next() {
            T returnItem = array[pos];
            pos = (pos + 1) % array.length;
            num += 1;
            return returnItem;
        }
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof Deque otherDeque) {
            return (this.toList()).equals(otherDeque.toList());
        }
        return false;
    }
    @Override
    public String toString() {
        return this.toList().toString();
    }

    public static void main(String[] args) {
        Deque<Integer> ad = new ArrayDeque<>();
    }
}

