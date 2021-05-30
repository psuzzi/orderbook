package dev.algo.orderbook.view.impl;

import java.util.*;

public class IndexHeap<K,V> extends IndexMaxPQ<K>{

    private Comparator<K> comparator;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private int currentIndex = 0;
    private Map<K,V> kvMap = new HashMap<>();
    private Map<K, Integer> kiMap = new HashMap<>();
    private LinkedList<Integer> freeIndices = new LinkedList<>();

    public IndexHeap(Comparator<K> comparator) {
        super(DEFAULT_INITIAL_CAPACITY, comparator);
    }

    public IndexHeap(int initialCapacity, Comparator<K> comparator) {
        super(initialCapacity, comparator);
    }

    public boolean containsKey(K price) {
        return kiMap.containsKey(price);
    }

    public V getValue(K price) {
        return kvMap.get(price);
    }

    public void removeKey(K price) {
        int deleteKeyIndex = kiMap.get(price);
        delete(deleteKeyIndex);// removes the key from the heap
        kvMap.remove(price);
        kiMap.remove(price);
        freeIndices.add(deleteKeyIndex);
    }

    /**
     * Associate the
     * @param price
     * @param aggregatedOrder
     */
    public void put(K price, V aggregatedOrder) {
        int keyIndex = (freeIndices.isEmpty()) ? currentIndex++ : freeIndices.remove();
        kiMap.put(price, keyIndex);
        kvMap.put(price, aggregatedOrder);
        insert(keyIndex, price);
    }

    public K topKey() {
        return maxKey();
    }
}
