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
    private int[] pq;       // binary heap using 1-based indexing
    private int[] qp;       // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
    private Key[] keys;     // keys[i] = priority of i
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
        n = 0;
        keys = (Key[]) new Object[initialCapacity+1];
        pq = new int[initialCapacity+1];
        qp = new int[initialCapacity+1];
        // -1 denote absence of values in the inverse map
        for(int i=0; i<= initialCapacity; i++)
            qp[i] = -1;
    }


    private void resize(int capacity){
        assert capacity>n;
        Key[] tempKeys = (Key[]) new Object[capacity];
        int[] tempPq = new int[capacity];
        int[] tempQp = new int[capacity];
        for(int i=1; i<=n; i++){
            tempKeys[i] = keys[i];
            tempPq[i] = pq[i];
            tempQp[i] = qp[i];
        }
        keys = tempKeys;
        pq = tempPq;
        qp = tempQp;
    }

    /**
     * Adds a new Key associated to the given key index
     * @param ki
     * @param key
     */
    public void insert(int ki, Key key){
        if (contains(ki)) throw new IllegalArgumentException("index is already in the priority queue");
        // double size of the array when needed
        if( n== pq.length-1) resize(2 * pq.length);
        validateIndex(ki);
        n++;
        qp[ki] = n;
        pq[n] = ki;
        keys[ki] = key;
        swim(n);
    }

    /**
     * Tells if exists an item with the given key index
     * @param ki
     * @return
     */
    public boolean contains(int ki){
        return qp[ki] != -1;
    }

    /**
     * Delete the key at the specified key index
     * @param ki
     */
    public void delete(int ki){
        validateIndex(ki);
        if(!contains(ki)){
            throw new NoSuchElementException("Index not in the priority queue");
        }
        int index = qp[ki];
        // move to n-1, and decrease size
        exch(index, n--);
        // reheapify
        swim(index);
        sink(index);
        // delete
        keys[ki] = null;
        qp[ki] = -1;
    }

    private void validateIndex(int i){
        if (i < 0) throw new IllegalArgumentException("index is negative: " + i);
        if (i >= pq.length) throw new IllegalArgumentException("index >= capacity: " + i);
    }

    /**
     * Remove top key and returns its associated index.
     *
     * @return
     */
    public int delTop(){
        if(isEmpty()){
            throw new NoSuchElementException("Priority queue underflow");
        }

        // index of top item
        int top = pq[1];
        exch(1, n--);//exchange with last item
        sink(1);// reheapify

        // remove last item
        qp[top] = -1;
        keys[top] = null;
        pq[n+1] = -1;

        // halve the size of the array when needed
        if( (n>0) && (n==(pq.length-1)/4))
            resize(pq.length/2);
        return top;
    }

    public boolean isEmpty(){
        return n==0;
    }

    public Key topKey(){
        return keys[pq[1]];
    }

    public int size(){
        return n;
    }

    /***************************************************************************
     * Heap helper functions.
     ***************************************************************************/

    private void swim(int k) {
        while (k > 1 && less(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= n) {
            int j = 2*k;
            if (j < n && less(j, j+1)) j++;
            if (!less(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

    /***************************************************************************
     * General helper functions.
     ***************************************************************************/

    private boolean less(int i, int j) {
        Key iK = keys[pq[i]];
        Key jK = keys[pq[j]];
        return comparator.compare(iK, jK) < 0;
    }

    private void exch(int i, int j) {
        int swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }

    /***************************************************************************
     * Testing helper functions.
     ***************************************************************************/

    /**
     * Package-visible method to test that pq[1..n] is a max heap.
     * This method should be used in tests only
     * @return
     */
    boolean isMaxHeap() {
        for (int i = 1; i <= n; i++) {
            if (keys[pq[i]] == null) return false;
        }
        for (int i = n+1; i < pq.length; i++) {
            if (keys[pq[i]] != null) return false;
        }
        if (keys[pq[0]] != null) return false;
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


