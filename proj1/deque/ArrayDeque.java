package deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }
    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resize(2 * items.length);
        }
        items[nextFirst] = item;
        nextFirst = getFront(nextFirst);
        size++;
    }
    @Override
    public void addLast(T item) {
        if (size == items.length) {
            resize(2 * items.length);
        }
        items[nextLast] = item;
        nextLast = getBack(nextLast);
        size++;
    }
    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        int index = getBack(nextFirst);
        for (int i = 0; i < size; i++) {
            System.out.print(items[index] + " ");
            index = getBack(index);
        }
        System.out.println();
    }
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        boolean condition = (items.length >= 16) && (size - 1 < items.length / 4);
        if (condition) {
            resize(items.length / 2);
        }
        nextFirst = getBack(nextFirst);
        T value = items[nextFirst];
        items[nextFirst] = null;
        size--;
        return value;
    }
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        boolean condition = (items.length >= 16) && (size - 1 < items.length / 4);
        if (condition) {
            resize(items.length / 2);
        }
        nextLast = getFront(nextLast);
        T value = items[nextLast];
        items[nextLast] = null;
        size--;
        return value;
    }
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        int position = (index + getBack(nextFirst)) % items.length;
        return items[position];
    }
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> other = (Deque<T>) o;
        if (size != other.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!get(i).equals(other.get(i))) {
                return false;
            }
        }
        return true;
    }

    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int currentIndex;
        ArrayDequeIterator() {
            currentIndex = 0;
        }

        @Override
        public boolean hasNext() {

            return currentIndex < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return get(currentIndex++);
        }
    }

    //resize
    private void resize(int capacity) {
        T[] temp = (T[]) new Object[capacity];
        int index = getBack(nextFirst);

        for (int i = 0; i < size; i++) {
            temp[i] = items[index];
            index = getBack(index);
        }
        this.items = temp;
        nextFirst = capacity - 1;
        nextLast = size;
    }

    private int getFront(int index) {
        if (index == 0) {
            return items.length - 1;
        } else {
            return index - 1;
        }
    }

    private int getBack(int index) {
        if (index == items.length - 1) {
            return 0;
        } else {
            return index + 1;
        }
    }

}
