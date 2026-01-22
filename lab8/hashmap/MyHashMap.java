package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private Integer size;
    private Double loadFactor = 0.75;
    private Set<K> keySet;

    private static final int DEFAULT_INITIAL_SIZE = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this(DEFAULT_INITIAL_SIZE, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, DEFAULT_LOAD_FACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        loadFactor = maxLoad;
        buckets = createTable(initialSize);
        keySet = new HashSet<>();
        size = 0;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = createBucket();
        }
        return table;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    @Override
    public void clear() {
        buckets = createTable(buckets.length);
        keySet.clear();
        size = 0;

    }

    @Override
    public boolean containsKey(K key) {
        return keySet.contains(key);
    }

    private int getBucketIndex(K key) {
        int hash = key.hashCode();
        return Math.floorMod(hash, buckets.length);
    }

    @Override
    public V get(K key) {
        int hashIndex = getBucketIndex(key);
        for (Node node : buckets[hashIndex]) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        int index = this.getBucketIndex(key);

        for (Node node : buckets[index]) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
        }

        buckets[index].add(new Node(key, value));
        keySet.add(key);
        size++;

        if ((double) size / buckets.length > loadFactor) {
            resize(buckets.length * 2);
        }

    }

    private void resize(int newSize) {
        Collection<Node>[] oldBuckets = buckets;

        buckets = createTable(newSize);
        size = 0;
        keySet.clear();

        for (Collection<Node> bucket : oldBuckets) {
            for (Node node : bucket) {
                put(node.key, node.value);
            }
        }
    }


    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (Collection<Node> bucket : buckets) {
            for (Node node : bucket) {
                keys.add(node.key);
            }
        }
        return keys;
    }

    @Override
    public V remove(K key) {
        int index = getBucketIndex(key);

        Iterator<Node> it = buckets[index].iterator();
        while (it.hasNext()) {
            Node node = it.next();
            if (node.key.equals(key)) {
                V value = node.value;
                it.remove();
                keySet.remove(key);
                size--;
                return value;
            }
        }
        return null;
    }

    @Override
    public V remove(K key, V value) {
        int index = getBucketIndex(key);

        Iterator<Node> it = buckets[index].iterator();
        while (it.hasNext()) {
            Node node = it.next();
            if (node.key.equals(key) && node.value.equals(value)) {
                it.remove();
                keySet.remove(key);
                size--;
                return value;
            }
        }
        return null;
    }

    public Iterator<K> iterator() {
        return new MyHashMapIterator();
    }

    private class MyHashMapIterator implements Iterator<K> {
        private Iterator<Node> bucketIterator;
        private int bucketIndex;
        MyHashMapIterator() {
            bucketIndex = 0;
            bucketIterator = buckets[0].iterator();
            advanceToNextNonEmptyBucket();
        }

        private void advanceToNextNonEmptyBucket() {
            while (bucketIndex < buckets.length && !bucketIterator.hasNext()) {
                bucketIndex++;
                if (bucketIndex < buckets.length) {
                    bucketIterator = buckets[bucketIndex].iterator();
                }
            }
        }

        @Override
        public boolean hasNext() {
            return bucketIndex < buckets.length && bucketIterator.hasNext();
        }

        @Override
        public K next() {
            K key = bucketIterator.next().key;
            advanceToNextNonEmptyBucket();
            return key;
        }
    }

}
