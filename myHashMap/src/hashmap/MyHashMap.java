package hashmap;

import java.util.*;

/**
 * A hash table-backed Map implementation. Provides amortized constant time
 * access to elements via get(), remove(), and put() in the best case.
 * <p>
 * Assumes null keys will never be inserted, and does not resize down upon remove().
 *
 * @author Brie Zhou
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
    private double loadFactor1;
    private int numItems;
    // You should probably define some more!


    /**
     * Constructors
     */
    public MyHashMap() {
        buckets = createTable(16);
        //buckets = new Collection<>[8];
        numItems = 0;
        loadFactor1 = 0.75;
    }

    public MyHashMap(int initialCapacity) {
        buckets = createTable(initialCapacity);
        //buckets = new Collection<>[8];
        numItems = 0;
        loadFactor1 = 0.75;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialCapacity.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialCapacity initial size of backing array
     * @param loadFactor      maximum load factor
     */
    public MyHashMap(int initialCapacity, double loadFactor) {
        buckets = createTable(initialCapacity);
        numItems = 0;
        this.loadFactor1 = loadFactor;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     * <p>
     * The only requirements of a hash table bucket are that we can:
     * 1. Insert items (`add` method)
     * 2. Remove items (`remove` method)
     * 3. Iterate through items (`iterator` method)
     * <p>
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     * <p>
     * Override this method to use different data structures as
     * the underlying bucket type
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<Node>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    @Override
    public void put(K key, V value) {
        if (((double) numItems) / buckets.length > loadFactor1) {
            resize(buckets.length * 2);
        }
        int index = Math.floorMod(key.hashCode(), buckets.length);
        Node n = createNode(key, value);
        if (buckets[index] == null) {
            buckets[index] = createBucket();
            buckets[index].add(n);
            numItems += 1;
        } else {
            if (containsKey(key)) {
                for (Node i : buckets[index]) {
                    if (i.key.equals(key)) {
                        i.value = value;
                    }
                }
            } else {
                buckets[index].add(n);
                numItems += 1;
            }
        }
    }


    private void resize(int newSize) {
        Collection<Node>[] returnTable = createTable(newSize);
        for (Collection<Node> i : buckets) {
            if (i != null) {
                for (Node j : i) {
                    int newIndex = Math.floorMod(j.key.hashCode(), newSize);
                    if (returnTable[newIndex] != null) {

                        returnTable[newIndex].add(j);
                    } else {
                        returnTable[newIndex] = createBucket();
                        returnTable[newIndex].add(j);
                    }
                }
                buckets = returnTable;
            }
        }
    }

    @Override
    public V get(K key) {
        int index = Math.floorMod(key.hashCode(), buckets.length);
        if (buckets[index] == null) {
            return null;
        }
        if (buckets[index] != null) {
            for (Node n : buckets[index]) {
                if (n.key.equals(key)) {
                    return n.value;
                }
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        int index = Math.floorMod(key.hashCode(), buckets.length);
        if (buckets[index] == null) {
            return false;
        }
        if (buckets[index] != null) {
            for (Node n : buckets[index]) {
                if (n.key.equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int size() {
        return numItems;
    }

    @Override
    public void clear() {
        buckets = createTable(buckets.length);
        numItems = 0;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}

// Your code won't compile until you do so!
