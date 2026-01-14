package bstmap;

import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node<K, V> root;
    private int size;

    public BSTMap() {
        root = null;
        size = 0;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> left, right;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

    }

    public void printInOrder() {
        for (K key : this) {
            System.out.print(key + " ");
        }
    }


    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return containsKeyHelp(root, key);
    }

    private boolean containsKeyHelp(Node<K, V> p, K key) {
        if (p == null) {
            return false;
        }
        int cmp = key.compareTo(p.key);
        if(cmp == 0) {
            return true;
        } else if(cmp > 0) {
            return containsKeyHelp(p.right, key);
        } else {
            return containsKeyHelp(p.left, key);
        }
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key)  {
        return getHelp(root, key);
    }

    private V getHelp(Node<K, V> p, K key) {
        if (p == null) {
            return null;
        }
        int cmp = key.compareTo(p.key);
        if (cmp == 0) {
            return p.value;
        } else if (cmp > 0) {
            return getHelp(p.right, key);
        } else {
            return getHelp(p.left, key);
        }
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size()  {
        return size;
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value)  {
        root = putNode(root, key, value);
    }

    private Node<K, V> putNode(Node<K, V> p, K key, V value) {
        if (p == null) {
            size++;
            return new Node<>(key, value);
        }
        int cmp = key.compareTo(p.key);
        if (cmp == 0) {
            p.value = value;
        } else if (cmp > 0) {
            p.right = putNode(p.right, key, value);
        } else {
            p.left = putNode(p.left, key, value);
        }
        return p;
    }

    /* Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    @Override
    public Set<K> keySet()   {
        Set<K> set = new HashSet<>();
        keySetHelp(root, set);
        return set;
    }

    private void keySetHelp(Node<K, V> p, Set<K> set) {
        if (p == null) {
            return;
        }
        set.add(p.key);
        keySetHelp(p.left, set);
        keySetHelp(p.right, set);
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key)  {
        Node<K, V> removed = new Node<>(null, null);
        root = removeHelp(root, key, removed);
        return removed.value;
    }

    private Node<K, V> removeHelp(Node<K, V> p, K key, Node<K, V> removed) {
        if (p == null) {
            return null;
        }
        int cmp = key.compareTo(p.key);
        if (cmp < 0) {
            p.left = removeHelp(p.left, key, removed);
        } else if (cmp > 0) {
            p.right = removeHelp(p.right, key, removed);
        }  else {
            removed.key = p.key;
            removed.value = p.value;
            size--;

            if (p.left == null) {
                return p.right;
            }
            if (p.right == null) {
                return p.left;
            }

            Node<K,V> predecessor = getMax(p.left);
            p.key = predecessor.key;
            p.value = predecessor.value;
            p.left = removePredecessor(p.left);
        }
        return p;
    }

    private Node<K,V> removePredecessor(Node<K, V> p) {
        if (p.right == null) {
            return p.left;
        }
        p.right = removePredecessor(p.right);
        return p;
    }

    private Node<K, V> getMax(Node<K, V> p) {
       while (p.right != null) {
           p = p.right;
       }
       return p;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        Node<K, V> removed = new Node<>(null, null);
        root = removeIfMatch(root, key, value, removed);
        return (V) removed.value;
    }

    private Node<K, V> removeIfMatch(Node<K, V> p, K key, V value,
                                     Node<K, V> removed) {
        if (p == null) {
            return null;
        }

        int cmp = key.compareTo(p.key);
        if (cmp < 0) {
            p.left = removeIfMatch(p.left, key, value, removed);
        } else if (cmp > 0) {
            p.right = removeIfMatch(p.right, key, value, removed);
        } else {
            if (p.value == null && value != null || p.value != null && !p.value.equals(value)) {
                return p;
            }

            removed.key = p.key;
            removed.value = p.value;
            size--;

            if (p.left == null) {
                return p.right;
            }
            if (p.right == null) {
                return p.left;
            }

            Node<K, V> predecessor = getMax(p.left);
            p.key = predecessor.key;
            p.value = predecessor.value;
            p.left = removePredecessor(p.left);
        }
        return p;
    }


    @Override
    public Iterator<K> iterator() {
        return new BSTIterator();
    }

    private class BSTIterator implements Iterator<K> {
        private final Stack<Node<K, V>> stack;
        BSTIterator() {
            stack = new Stack<>();
            pushLeft(root);
        }

        private void pushLeft(Node<K, V> p) {
            while (p != null) {
                stack.push(p);
                p = p.left;
            }
        }
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }
        @Override
        public K next() {
           if (!hasNext()) {
               throw new NoSuchElementException();
           }
           Node<K, V> node = stack.pop();
           pushLeft(node.right);
           return node.key;
        }
    }
}
