package dev.algo.orderbook.view.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class IndexMaxPQTest {

    /** For sake of simplicity and direct feedback this logger prints to StdOut */
    private static PrintStream log = System.out;

    static String[] keys = new String[] { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };
    static String[] moreKeys = new String[] { "alpha", "beta", "zeta" };

    private static List<String> sorted(String [] keys){
        return Arrays.asList(keys).stream().sorted().collect(Collectors.toList());
    }

    @Test
    public void testInsertion(){
        IndexMaxPQ<String> pq = new IndexMaxPQ<String>(keys.length, Comparator.naturalOrder());
        for (int i = 0; i < keys.length; i++) {
            pq.insert(i, keys[i]);
        }
        List<String> sortedKeys = sorted(keys);

        int sortedIndex = sortedKeys.size()-1;

        assertEquals(keys.length, pq.size(), "PQ Size");
        assertEquals(sortedKeys.get(sortedIndex), pq.maxKey(), "Max Key");
    }

    @Test
    public void testDeletion(){
        IndexMaxPQ<String> pq = new IndexMaxPQ<String>(keys.length, Comparator.naturalOrder());
        for (int i = 0; i < keys.length; i++) {
            pq.insert(i, keys[i]);
        }
        LinkedList<String> sortedKeys = new LinkedList<>(sorted(keys));

        // delete and print each key

        while (pq.size()>2) {
            String key = pq.maxKey();
            int i = pq.delMax();
            String expect = sortedKeys.removeLast();
            assertEquals(expect, key, "Delete in order");

        }
        log.println();
    }

    @Test void testResize() {
        IndexMaxPQ<String> pq = new IndexMaxPQ<String>(Comparator.naturalOrder());

        for (int i = 0; i < keys.length; i++) {
            pq.insert(i, keys[i]);
        }

        assertEquals(keys.length, pq.size());
        assertEquals("worst", pq.maxKey());

        int baseIndex = keys.length;
        for(int i=0; i< moreKeys.length; i++){
            pq.insert(baseIndex + i, moreKeys[i]);
        }

        assertEquals(keys.length+ moreKeys.length, pq.size());
        assertEquals("zeta", pq.maxKey());

    }

    @Test void testResizeAndRemove(){

        IndexMaxPQ<String> pq = new IndexMaxPQ<String>(Comparator.naturalOrder());

        for (int i = 0; i < keys.length; i++) {
            pq.insert(i, keys[i]);
        }

        assertEquals(keys.length, pq.size());
        assertEquals("worst", pq.maxKey());

        pq.delMax();

        assertEquals(keys.length-1, pq.size());
        assertEquals("was", pq.maxKey());

        int baseIndex = keys.length;
        for(int i=0; i< moreKeys.length; i++){
            pq.insert(baseIndex + i, moreKeys[i]);
        }

        assertEquals(keys.length+ moreKeys.length-1, pq.size());
        assertEquals("zeta", pq.maxKey());

        pq.delMax();

        assertEquals(keys.length+ moreKeys.length-2, pq.size());
        assertEquals("was", pq.maxKey());
    }

}