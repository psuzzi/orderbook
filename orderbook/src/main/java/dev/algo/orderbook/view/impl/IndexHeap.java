package dev.algo.orderbook.view.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class IndexHeap<K,V> extends IndexMaxPQ<K>{

    private Comparator<K> comparator;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private int currentIndex = 0;
    private Map<K,V> kvMap = new HashMap<>();
    private Map<K, Integer> kiMap = new HashMap<>();

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
        int deleteIndex = kiMap.get(price);
        delete(deleteIndex);// removes the key from the heap
        kvMap.remove(price);
        kiMap.remove(price);
        currentIndex--;
    }

    /**
     * Associate the
     * @param price
     * @param aggregatedOrder
     */
    public void put(K price, V aggregatedOrder) {
        kiMap.put(price, currentIndex);
        kvMap.put(price, aggregatedOrder);
        insert(currentIndex, price);
        currentIndex++;
    }

    public K topKey() {
        return maxKey();
    }
}
