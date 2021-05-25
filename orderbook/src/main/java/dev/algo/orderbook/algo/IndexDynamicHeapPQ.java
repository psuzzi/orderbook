package dev.algo.orderbook.algo;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * Priority queue of generic keys, using a comparator provided by the client.
 * <p/>
 * This implementation uses a <em>binary heap</em> backed by a <em>dynamic resizing array</em>.
 * Insert and delete takes amortized O(n) time, because of resizing array operations.
 *
 * @param <Key>
 */
public class IndexDynamicHeapPQ<Key> {

    // heap-ordered complete binary tree in pq[1..n] with pq[0] unused
    private int[] pq;//binary heap using 1-based indexing
    private int[] qp;//
    private Key[] keys;

    private int n;
    private final Comparator<Key> comparator;

    /**
     * Initializes an empty priority queue using the given comparator
     * @param comparator
     */
    public IndexDynamicHeapPQ(Comparator<Key> comparator){
        this(1, comparator);
    }

    /**
     * Initializes an empty priority queue with the given initial capacity, using the given comparator.
     * @param initialCapacity
     * @param comparator
     */
    public IndexDynamicHeapPQ(int initialCapacity, Comparator<Key> comparator){
        this.comparator = comparator;
        pq = (Key[]) new Object[initialCapacity+1];
        n = 0;
    }


    private void resize(int capacity){
        assert capacity>n;
        Key[] temp = (Key[]) new Object[capacity];
        for(int i=1; i<=n; i++){
            temp[i] = pq[i];
        }
        pq = temp;
    }

    /**
     * Add a new key to this PQ
     * @param v
     */
    public void insert(Key v){
        // double size of the array when needed
        if( n== pq.length-1) resize(2 * pq.length);
        pq[++n] = v;
        swim(n);
    }

    public Key delTop(){
        if(isEmpty()){
            throw new NoSuchElementException("Priority queue underflow");
        }
        Key top = pq[1];
        exch(1, n--);//exchange with last item
        sink(1);
        pq[n+1] = null;//remove last item
        // halve the size of the array when needed
        if( (n>0) && (n==(pq.length-1)/4))
            resize(pq.length/2);
        return top;
    }

    public boolean isEmpty(){
        return n==0;
    }

    public Key top(){
        return pq[1];
    }

    public int size(){
        return n;
    }

    private void swim(int k){
        while (k > 1 && less(k/2, k))
        {
            exch(k/2, k);
            k = k/2;
        }
    }

    private void sink(int k){
        while (2*k <= n)
        {
            int j = 2*k;
            if (j < n && less(j, j+1)) j++;
            if (!less(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

    private boolean less(int i, int j){
        return comparator.compare(pq[i], pq[j])<0;
    }

    private void exch(int i, int j){
        Key t = pq[i];
        pq[i] = pq[j];
        pq[j] = t;
    }

    // is pq[1..n] a max heap?

    /**
     * Package-visible method to test that pq[1..n] is a max heap.
     * This method should be used in tests only
     * @return
     */
    boolean isMaxHeap() {
        for (int i = 1; i <= n; i++) {
            if (pq[i] == null) return false;
        }
        for (int i = n+1; i < pq.length; i++) {
            if (pq[i] != null) return false;
        }
        if (pq[0] != null) return false;
        return isMaxHeapOrdered(1);
    }

    // is subtree of pq[1..n] rooted at k a max heap?
    private boolean isMaxHeapOrdered(int k) {
        if (k > n) return true;
        int left = 2*k;
        int right = 2*k + 1;
        if (left  <= n && less(k, left))  return false;
        if (right <= n && less(k, right)) return false;
        return isMaxHeapOrdered(left) && isMaxHeapOrdered(right);
    }




}


