package deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {

    /*creat IntNode class*/
    private class StuffNode {

        private StuffNode prev;
        private T item;
        private StuffNode next;

        StuffNode(T item, StuffNode prev, StuffNode next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }

    }

    private StuffNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new StuffNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }


    @Override
    public void addFirst(T value) {
        if (value == null) {
            return;
        }
        size += 1;
        sentinel.next = new StuffNode(value, sentinel, sentinel.next);
        if (size == 1) {
            sentinel.prev = sentinel.next;
        }
    }
    @Override
    public  void addLast(T value) {
        if (value == null) {
            return;
        }
        size += 1;
        sentinel.prev = new StuffNode(value, sentinel.prev, sentinel);
        sentinel.prev.prev.next = sentinel.prev;
    }
    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        StuffNode temp = sentinel.next;
        for (int i = 0; i < size; i++) {
            System.out.print(temp.item + " ");
            temp = temp.next;
        }
        System.out.println();

    }
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T first = sentinel.next.item;
        if (size == 1) {
            sentinel.prev = sentinel;
            sentinel.next = sentinel;
        } else  {
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
        }
        size--;
        return first;
    }
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T last = sentinel.prev.item;
        if (size == 1) {
            sentinel.prev = sentinel;
            sentinel.next = sentinel;
        } else  {
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
        }
        size--;
        return last;
    }
    @Override
    public T get(int index) {
        StuffNode temp = sentinel.next;
        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }
        return temp.item;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        if (o == null) {
            return false;
        }
        Deque<T> other = (Deque<T>) o;
        if (size != other.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!this.get(i).equals(other.get(i))) {
                return false;
            }
        }
        return true;
    }

    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private StuffNode current;

        LinkedListDequeIterator() {
            current = sentinel.next;
        }

        @Override
        public boolean hasNext() {
            return current.next != sentinel;
        }
        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T val = current.item;
            current = current.next;
            return val;
        }
    }

    public T getRecursive(int index) {
        return getItem(sentinel.next, index);
    }

    private T getItem(StuffNode p, int index) {
        if (index == 0) {
            return p.item;
        } else {
            return getItem(p.next, index - 1);
        }
    }

}
