package dev.algo.orderbook.algo;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PQTest {


    @Test
    public void testDynamicHeapPqAsMaxHeap(){
        DynamicHeapPQ<Character> maxPq = new DynamicHeapPQ<>(Character::compareTo);
        maxPq.insert('P');
        maxPq.insert('Q');
        maxPq.insert('E');
        assertEquals('Q', maxPq.delTop());
        assertEquals(2, maxPq.size());
        assertTrue(maxPq.isMaxHeap());
        maxPq.insert('X');
        maxPq.insert('A');
        maxPq.insert('M');
        assertEquals('X', maxPq.delTop());
        assertEquals(4, maxPq.size());
        assertTrue(maxPq.isMaxHeap());
        maxPq.insert('P');
        maxPq.insert('L');
        maxPq.insert('E');
        assertEquals('P', maxPq.delTop());
        assertEquals(6, maxPq.size());
        assertTrue(maxPq.isMaxHeap());
    }

    @Test
    public void testDynamicHeapPqAsMinHeap(){
        DynamicHeapPQ<Character> minPq = new DynamicHeapPQ<>( (a,b) -> Character.compare(b,a));
        minPq.insert('P');
        minPq.insert('Q');
        minPq.insert('E');
        assertEquals('E', minPq.delTop());
        assertEquals(2, minPq.size());
        assertTrue(minPq.isMaxHeap());
        minPq.insert('X');
        minPq.insert('A');
        minPq.insert('M');
        assertEquals('A', minPq.delTop());
        assertEquals(4, minPq.size());
        assertTrue(minPq.isMaxHeap());
        minPq.insert('P');
        minPq.insert('L');
        minPq.insert('E');
        assertEquals('E', minPq.delTop());
        assertEquals(6, minPq.size());
        assertTrue(minPq.isMaxHeap());
    }

    @Test
    public void testMinPQ(){
        MinPQ<Character> minPq = new MinPQ<>();
        minPq.insert('P');
        minPq.insert('Q');
        minPq.insert('E');
        assertEquals('E', minPq.delMin());
        assertEquals(2, minPq.size());
        minPq.insert('X');
        minPq.insert('A');
        minPq.insert('M');
        assertEquals('A', minPq.delMin());
        assertEquals(4, minPq.size());
        minPq.insert('P');
        minPq.insert('L');
        minPq.insert('E');
        assertEquals('E', minPq.delMin());
        assertEquals(6, minPq.size());
    }

    @Test
    public void testMyPQ(){
        // default compare for maxPq
        MyPQ<Character> myPqMax = new MyPQ<>(Character::compareTo);
        myPqMax.insert('P');
        myPqMax.insert('Q');
        myPqMax.insert('E');
        assertEquals('Q', myPqMax.delTop());
        assertEquals(2, myPqMax.size());
        myPqMax.insert('X');
        myPqMax.insert('A');
        myPqMax.insert('M');
        assertEquals('X', myPqMax.delTop());
        assertEquals(4, myPqMax.size());
        myPqMax.insert('P');
        myPqMax.insert('L');
        myPqMax.insert('E');
        assertEquals('P', myPqMax.delTop());
        assertEquals(6, myPqMax.size());

        // inverse compare for minPq
        MyPQ<Character> myPqMin = new MyPQ<>( (a,b) -> Character.compare(b,a) );
        myPqMin.insert('P');
        myPqMin.insert('Q');
        myPqMin.insert('E');
        assertEquals('E', myPqMin.delTop());
        assertEquals(2, myPqMin.size());
        myPqMin.insert('X');
        myPqMin.insert('A');
        myPqMin.insert('M');
        assertEquals('A', myPqMin.delTop());
        assertEquals(4, myPqMin.size());
        myPqMin.insert('P');
        myPqMin.insert('L');
        myPqMin.insert('E');
        assertEquals('E', myPqMin.delTop());
        assertEquals(6, myPqMin.size());

    }

}