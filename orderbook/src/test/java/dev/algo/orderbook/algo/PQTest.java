package dev.algo.orderbook.algo;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PQTest {



    @Test
    public void testHeap(){
        ArrayList<Integer> heap = new ArrayList<>();
        heap.add(0,null);
        heap.add(1,1);
        heap.add(2, 2);
        assertNotNull(heap);
        System.out.println(heap);
    }

    @Test
    public void testDynamicHeapPqAsMaxHeap(){
        DynamicHeapPQ<Character> dynamicHeapPq = new DynamicHeapPQ<>(Character::compareTo);
        dynamicHeapPq.insert('P');
        dynamicHeapPq.insert('Q');
        dynamicHeapPq.insert('E');
        assertEquals('Q', dynamicHeapPq.delTop());
        assertEquals(2, dynamicHeapPq.size());
        assertTrue(dynamicHeapPq.isMaxHeap());
        dynamicHeapPq.insert('X');
        dynamicHeapPq.insert('A');
        dynamicHeapPq.insert('M');
        assertEquals('X', dynamicHeapPq.delTop());
        assertEquals(4, dynamicHeapPq.size());
        assertTrue(dynamicHeapPq.isMaxHeap());
        dynamicHeapPq.insert('P');
        dynamicHeapPq.insert('L');
        dynamicHeapPq.insert('E');
        assertEquals('P', dynamicHeapPq.delTop());
        assertEquals(6, dynamicHeapPq.size());
        assertTrue(dynamicHeapPq.isMaxHeap());
    }

    @Test
    public void testDynamicHeapPqAsMinHeap(){
        DynamicHeapPQ<Character> dynamicHeapPq = new DynamicHeapPQ<>( (a,b) -> Character.compare(b,a));
        dynamicHeapPq.insert('P');
        dynamicHeapPq.insert('Q');
        dynamicHeapPq.insert('E');
        assertEquals('E', dynamicHeapPq.delTop());
        assertEquals(2, dynamicHeapPq.size());
        assertTrue(dynamicHeapPq.isMaxHeap());
        dynamicHeapPq.insert('X');
        dynamicHeapPq.insert('A');
        dynamicHeapPq.insert('M');
        assertEquals('A', dynamicHeapPq.delTop());
        assertEquals(4, dynamicHeapPq.size());
        assertTrue(dynamicHeapPq.isMaxHeap());
        dynamicHeapPq.insert('P');
        dynamicHeapPq.insert('L');
        dynamicHeapPq.insert('E');
        assertEquals('E', dynamicHeapPq.delTop());
        assertEquals(6, dynamicHeapPq.size());
        assertTrue(dynamicHeapPq.isMaxHeap());
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