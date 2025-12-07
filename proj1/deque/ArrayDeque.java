package deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int size;
    private int nextfirst;
    private int nextlast;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextfirst = 3;
        nextlast = 4;
    }
    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resize(4 * items.length);
        }
        size++;
        items[nextfirst] = item;
        nextfirst = getFront(nextfirst);
    }
    @Override
    public void addLast(T item) {
        if (size == items.length) {
            resize(4 * items.length);
        }
        size++;
        items[nextlast] = item;
        nextlast = getBack(nextlast);
    }
    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        int index = getBack(nextfirst);
        for (int i = 0; i < size; i++) {
            System.out.println(items[index] + " ");
            index = getBack(index);
        }
        System.out.println();
    }
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        boolean condition = (items.length >= 16) && (items.length / (size - 1) > 4);
        if (condition) {
            resize(items.length / 4);
        }
        size--;
        nextfirst = getBack(nextfirst);
        T value = items[nextfirst];
        items[nextfirst] = null;
        return value;
    }
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        boolean condition = (items.length >= 16) && (items.length / (size - 1) > 4);
        if(condition) {
            resize(items.length / 4);
        }
        size--;
        nextlast = getFront(nextlast);
        T value = items[nextlast];
        items[nextlast] = null;
        return value;
    }
    @Override
    public T get(int index) {
        if (index<0 || index>=size) {
            return null;
        }
        int position = index+getBack(nextfirst);
        if (position >= items.length) {
            position = position - items.length;
        }
        return items[position];
    }
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArrayDeque)) {
            return false;
        }
        if (o == null) { return false; }
        ArrayDeque<?> other = (ArrayDeque<?>) o;
        if (size != other.size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!items[i].equals(other.items[i])) {
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
        public ArrayDequeIterator() {
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
            return items[currentIndex++];
        }
    }

    //resize
    private void resize(int capacity) {
        T[] temp = (T[]) new Object[capacity];
        int index = getBack(nextfirst);

        for(int i = 0; i < size; i++) {
            temp[i] = items[index];
            index = getBack(index);
        }
        this.items = temp;
        nextfirst = capacity -1;
        nextlast = size;
        }

    private int getFront(int index) {
        if (index == 0) {
            return items.length-1;
        } else {
            return index-1;
        }
    }

    private int getBack(int index) {
        if (index == items.length-1) {
            return 0;
        } else {
            return index+1;
        }
    }

}
