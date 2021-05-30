package dev.algo.orderbook.view.impl;

import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class IndexHeapTest {

    /** For sake of simplicity and direct feedback this logger prints to StdOut */
    private static PrintStream log = System.out;

    static String[] keys = new String[] { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };
    static String[] moreKeys = new String[] { "alpha", "beta", "zeta" };

    private static List<String> sorted(String [] keys){
        return Arrays.asList(keys).stream().sorted().collect(Collectors.toList());
    }

    @Test
    public void testInsertion(){
        IndexHeap<String, Object> pq = new IndexHeap<>(1, Comparator.<String>naturalOrder());
        List<String> sortedKeys = sorted(keys);

        int sortedIndex = sortedKeys.size()-1;

        assertEquals(keys.length, pq.size(), "PQ Size");
        assertEquals(sortedKeys.get(sortedIndex), pq.topKey(), "Max Key");
    }

}