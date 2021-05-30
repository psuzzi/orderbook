package dev.algo.orderbook.view.impl;

import java.util.Comparator;

public class MinIndexedBinaryHeap<K> extends MinIndexedDHeap<K> {

    public MinIndexedBinaryHeap(int maxSize, Comparator<K> comparator) {
        super(2, maxSize, comparator);
    }
}
