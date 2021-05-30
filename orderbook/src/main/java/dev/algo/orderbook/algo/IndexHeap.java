package dev.algo.orderbook.algo;

import dev.algo.orderbook.view.impl.MinIndexedDHeap;

import java.util.Comparator;
import java.util.NoSuchElementException;

public class IndexHeap<Key, Value> {

    // number of elements on PQ
    private int n;

    private int[] pq;       // binary heap
    private int[] qp;       // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
    private Key[] keys;     // keys[i] = priority of i

    private Comparator<Key> comparator;


    public IndexHeap(Comparator<Key> comparator){
        this(1, comparator);
    }

    public IndexHeap(int initialCapacity, Comparator<Key> comparator)
    {
        this.comparator = comparator;
        n = 0;
        keys = (Key[]) new Object[initialCapacity];
        pq = new int[initialCapacity];
        qp = new int[initialCapacity];
        // -1 denote absence of values in the inverse map
        for(int i=0; i< initialCapacity; i++)
            qp[i] = -1;
    }

    public void insert(int i, Key key) {
        if (n == pq.length)
            resize(2 * pq.length);
        validateIndex(i);
        if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
        n++;
        qp[i] = n;
        pq[n] = i;
        keys[i] = key;
        swim(n);
    }

    public boolean contains(int i) {
        validateIndex(i);
        return qp[i] != -1;
    }

    // throw an IllegalArgumentException if i is an invalid index
    private void validateIndex(int i) {
        if (i < 0) throw new IllegalArgumentException("index is negative: " + i);
//        if (i >= maxN) throw new IllegalArgumentException("index >= capacity: " + i);
    }

    /**
     * Number of keys in the priority queue
     * @return
     */
    public int size(){
        return n;
    }

    public Key topKey(){
        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
        return keys[pq[1]];
    }

    public int deleteTop(){
        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
        int max = pq[1];
        exch(1, n--);
        sink(1);

        assert pq[n+1] == max;
        qp[max] = -1;        // delete
        keys[max] = null;    // to help with garbage collection
        pq[n+1] = -1;        // not needed
        return max;
    }

    /***************************************************************************
     * General helper functions.
     ***************************************************************************/

    private boolean less(int i, int j) {
        return comparator.compare(keys[pq[i]], keys[pq[j]]) < 0;
    }

    private void exch(int i, int j) {
        int swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }

    // resize the underlying array to have the given capacity
    private void resize(int capacity){
        assert capacity>n;
        Key[] tempKeys = (Key[]) new Object[capacity+1];
        int[] tempPq = new int[capacity+1];
        int[] tempQp = new int[capacity+1];
        for(int i=1; i<=n; i++){
            tempKeys[i] = keys[i];
            tempPq[i] = pq[i];
            tempQp[i] = qp[i];
        }
        for(int i=n+1; i<tempQp.length; i++){
            tempKeys[i] = null;
            tempPq[i] = 0;
            tempQp[i] = -1;
        }
        keys = tempKeys;
        pq = tempPq;
        qp = tempQp;
    }

    /***************************************************************************
     * Heap helper functions.
     ***************************************************************************/

    private void swim(int k) {
        while (k > 1 && less((k-1)/2, k)) {
            exch(k, (k-1)/2);
            k = (k-1)/2;
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

    public boolean containsKey(Key price) {
        return false;
    }

    public Value getValue(Key price) {
        return null;
    }

    public void removeKey(Key price) {
    }

    public void put(Key price, Value aggregatedOrder) {
    }
}
