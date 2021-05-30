package dev.algo.orderbook.view.impl;

import java.util.*;

/**
 * Dynamic Indexed Heap. Each key inserted is associated to a value. 
 * The key is inserted into an internal implementation of a PQ.
 * <p>
 * This heap can be used both as max and min heap, depending on the comparator parameter.
 * <ul>
 *     <li>Max Heap</li> : Comparator.naturalOrder()
 *     <li>Min Heap</li> : Comparator.reverseOrder()
 * </ul>
 * <p>
 * The heap can expand, when the number of the elements increases.
 *
 * @param <Key>
 * @param <Value>
 */
public class IndexHeap<Key, Value> extends IndexMaxPQ<Key>{

    private Comparator<Key> comparator;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private int currentIndex = 0;
    private Map<Key, Value> kvMap = new HashMap<>();
    private Map<Key, Integer> kiMap = new HashMap<>();
    private LinkedList<Integer> freeIndices = new LinkedList<>();

    /** Initialize the heap with the default initial capacity and the given comparator */
    public IndexHeap(Comparator<Key> comparator) {
        super(DEFAULT_INITIAL_CAPACITY, comparator);
    }

    /** Initialize the heap with the given initial capacity and comparator */
    public IndexHeap(int initialCapacity, Comparator<Key> comparator) {
        super(initialCapacity, comparator);
    }

    /** Returns true if the key is contained in the heap */
    public boolean containsKey(Key price) {
        return kiMap.containsKey(price);
    }

    /** Returns the value associated to the given key */
    public Value getValue(Key price) {
        return kvMap.get(price);
    }

    /** Remove the given key from the heap, and invalidate the corresponding PQ key index */
    public void removeKey(Key price) {
        int deleteKeyIndex = kiMap.get(price);
        delete(deleteKeyIndex);// removes the key from the heap
        kvMap.remove(price);
        kiMap.remove(price);
        freeIndices.add(deleteKeyIndex);
    }

    /**
     * Associate the key to its value and insert the key into the PQ, making sure of getting an available key index.
     * @param price
     * @param aggregatedOrder
     */
    public void put(Key price, Value aggregatedOrder) {
        int keyIndex = (freeIndices.isEmpty()) ? currentIndex++ : freeIndices.remove();
        kiMap.put(price, keyIndex);
        kvMap.put(price, aggregatedOrder);
        insert(keyIndex, price);
    }

    /** Return the top key: the max (min) value for a Max PQ (Min PQ) */ 
    public Key topKey() {
        return maxKey();
    }
}
