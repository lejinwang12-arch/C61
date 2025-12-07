package deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListDeque<T> implements Deque<T> {

    /*creat IntNode class*/
    private class stuffNode {

        public stuffNode prev;
        public T item;
        public stuffNode next;

        public stuffNode(T item, stuffNode prev, stuffNode next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }

    }

    private stuffNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new stuffNode(null,null,null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }


    @Override
    public void addFirst(T value) {
        size += 1;
        sentinel.next = new stuffNode(value,sentinel,sentinel.next);
        if (size == 1) {
            sentinel.prev = sentinel.next;
        }
    }
    @Override
    public  void addLast(T value) {
        size += 1;
        sentinel.prev = new stuffNode(value,sentinel.prev,sentinel);
        sentinel.prev.prev.next = sentinel.prev;
    }
    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        stuffNode temp = sentinel.next;
        for (int i = 0; i < size; i++) {
            System.out.print(temp.item+" ");
            temp = temp.next;
        }
        System.out.println();

    }
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        } else {
            size -= 1;
            T first = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            return first;
        }

    }
    @Override
    public T removeLast() {
        if(size == 0) {
            return null;
        } else {
            size -= 1;
            T last = sentinel.prev.item;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            return last;
        }
    }
    @Override
    public T get(int index) {
        stuffNode temp = sentinel.next;
        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }
        return temp.item;
    }

    public boolean equals(Object o) {
        if(this == o) return true;
        if (!(o instanceof LinkedListDeque)) {
            return false;
        }
        if (o == null)  {
            return false;
        }
        LinkedListDeque<?> other = (LinkedListDeque<?>) o;
        LinkedListDeque<?> other = (LinkedListDeque<?>) o;
        if (size != other.size) {
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
        private stuffNode current;

        public LinkedListDequeIterator() {
            current = sentinel.next;
        }

        @Override
        public boolean hasNext() {
            return current.next != sentinel;
        }
        @Override
        public T next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            T val = current.item;
            current=current.next;
            return val;
        }
    }

    public T getRecursive(int index) { return getItem(sentinel.next,index); }

    private T getItem(stuffNode p, int index) {
        if(index == 0) {
            return p.item;
        } else {
            return getItem(p.next,index - 1);
        }
    }

}
