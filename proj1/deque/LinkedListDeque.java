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
        StuffNode newNode = new StuffNode(value, sentinel, sentinel.next);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
        size = size + 1;
    }
    @Override
    public  void addLast(T value) {
        StuffNode newNode = new StuffNode(value, sentinel.prev, sentinel);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size = size + 1;
    }
    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        StuffNode p = sentinel;
        while (p.next != sentinel) {
            System.out.print(p.next.item + " ");
            p = p.next;
        }
        System.out.println();
    }
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return  null;
        } else  {
            T item = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            size = size - 1;
            return item;
        }
    }
    @Override
    public T removeLast() {
        if (isEmpty())  {
            return  null;
        } else {
            T item = sentinel.prev.item;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            size = size - 1;
            return item;
        }
    }
    @Override
    public T get(int index) {
        StuffNode temp = sentinel.next;
        if (index > size - 1) {
            return null;
        } else {
            int i = 0;
            while(i < index) {
                temp = temp.next;
                i = i + 1;
            }
            return temp.item;
        }
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
        if (index > size - 1) {
            return null;
        } else {
            return getItem(sentinel.next, index);
        }
    }

    private T getItem(StuffNode p, int index) {
        if (index == 0) {
            return p.item;
        } else {
            return getItem(p.next, index - 1);
        }
    }

}
