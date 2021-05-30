package dev.algo.orderbook.view.impl;

import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class MinIndexedHeapTest {

    /** For sake of simplicity and direct feedback this logger prints to StdOut */
    private static PrintStream log = System.out;

    static String[] keys = new String[] { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };
    static String[] moreKeys = new String[] { "alpha", "beta", "zeta" };

    @Test
    void testLegalSize(){
        new MinIndexedBinaryHeap<String>(1, Comparator.naturalOrder());
    }

    @Test
    void testContainsInvalidKey(){
        MinIndexedBinaryHeap<String> pq = new MinIndexedBinaryHeap<String>(10, Comparator.naturalOrder());
        pq.insert(5, "abcd");
    }

    @Test
    void testInsertionMinHeap(){
        MinIndexedBinaryHeap<String> pq = new MinIndexedBinaryHeap<String>(10, Comparator.naturalOrder());
        for (int i = 0; i < keys.length; i++) {
            pq.insert(i, keys[i]);
        }

        List<String> sortedKeys = Arrays.asList(keys).stream().sorted(Collections.reverseOrder()).collect(Collectors.toList());;
        int sortedIndex = sortedKeys.size()-1;

        assertEquals(keys.length, pq.size(), "PQ Size");
        assertEquals(sortedKeys.get(sortedIndex), pq.peekMinValue(), "Min Key");
    }

    @Test
    void testInsertionMaxHeap(){
        MinIndexedBinaryHeap<String> pq = new MinIndexedBinaryHeap<String>(10, Comparator.reverseOrder());
        for (int i = 0; i < keys.length; i++) {
            pq.insert(i, keys[i]);
        }

        List<String> sortedKeys = Arrays.asList(keys).stream().sorted().collect(Collectors.toList());;
        int sortedIndex = sortedKeys.size()-1;

        assertEquals(keys.length, pq.size(), "PQ Size");
        assertEquals(sortedKeys.get(sortedIndex), pq.peekMinValue(), "Max Key");

    }

    @Test
    public void testDeletionMaxHeap(){

        MinIndexedBinaryHeap<String> pq = new MinIndexedBinaryHeap<String>(10, Comparator.reverseOrder());
        for (int i = 0; i < keys.length; i++) {
            pq.insert(i, keys[i]);
        }

        List<String> sortedKeys = Arrays.asList(keys).stream().sorted().collect(Collectors.toList());;
        int sortedIndex = sortedKeys.size()-1;

        // delete and print each key
        while (!pq.isEmpty()) {
            String key = pq.peekMinValue();
            int i = pq.pollMinKeyIndex();
            log.printf("%s %s%n", i, key);
            assertEquals(sortedKeys.get(sortedIndex--), key, "Delete in order");
        }
        log.println();
    }

    @Test void testResizeMaxHeap(){
//        IndexMaxPQ<String> pq = new IndexMaxPQ<String>(keys.length, Comparator.naturalOrder());
        MinIndexedBinaryHeap<String> pq = new MinIndexedBinaryHeap<String>(keys.length, Comparator.reverseOrder());

        String[] allKeys = new String[keys.length + moreKeys.length];
        for(int i=0; i< keys.length; i++){
            allKeys[i] = keys[i];
            pq.insert(i, keys[i]);
        }

        // when adding morekeys, the array resizes itself
        int baseIndex = keys.length;
        for(int i=0; i< moreKeys.length; i++){
            allKeys[baseIndex + i] = moreKeys[i];
            pq.insert(baseIndex + i, moreKeys[i]);
        }

        List<String> sortedAllKeys = sorted(allKeys);
    }

    private static List<String> sorted(String [] keys){
        return Arrays.asList(keys).stream().sorted().collect(Collectors.toList());
    }

}