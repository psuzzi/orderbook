package dev.algo.orderbook.view.impl;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.lang.Math.min;

/**
 *  The {@code IndexMaxPQ} class represents an indexed priority queue of generic keys.
 *  It supports the usual <em>insert</em> and <em>delete-the-maximum</em>
 *  operations, along with <em>delete</em> and <em>change-the-key</em>
 *  methods. In order to let the client refer to items on the priority queue,
 *  an integer between {@code 0} and {@code maxN - 1}
 *  is associated with each key—the client
 *  uses this integer to specify which key to delete or change.
 *  It also supports methods for peeking at a maximum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  <p>
 *  This implementation uses a <em>binary heap</em> along with an
 *  array to associate keys with integers in the given range.
 *  The <em>insert</em>, <em>delete-the-maximum</em>, <em>delete</em>,
 *  <em>change-key</em>, <em>decrease-key</em>, and <em>increase-key</em>
 *  operations take &Theta;(log <em>n</em>) time in the worst case,
 *  where <em>n</em> is the number of elements in the priority queue.
 *  Construction takes time proportional to the specified capacity.
 *  <p>
 *  @see <a href="https://algs4.cs.princeton.edu/24pq">Section 2.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Patrik Suzzi
 *
 *  @param <Key> the generic type of key on this priority queue
 */
public class IndexMaxPQ<Key> {

    private final Comparator<Key> comparator;

    private int maxN;        // maximum number of elements on PQ
    private int n;           // number of elements on PQ
    private int[] pq;        // binary heap using 1-based indexing
    private int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
    private Key[] keys;      // keys[i] = priority of i

    public IndexMaxPQ(Comparator<Key> comparator) {
        this(1,comparator);
    }

    /**
     * Initializes an empty indexed priority queue with indices between {@code 0} and {@code maxN - 1}.
     *
     * @param  maxN the keys on this priority queue are index from {@code 0} to {@code maxN - 1}
     * @throws IllegalArgumentException if {@code maxN < 0}
     */
    public IndexMaxPQ(int maxN, Comparator<Key> comparator) {
        if (maxN < 0) throw new IllegalArgumentException();
        this.comparator = comparator;
        this.maxN = maxN;
        n = 0;
        keys = (Key[]) new Comparable[maxN];
        pq   = new int[maxN];
        qp   = new int[maxN];
        for (int i = 0; i < maxN; i++)
            qp[i] = -1;
    }

    /**
     * Returns true if this priority queue is empty.
     *
     * @return {@code true} if this priority queue is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * Is {@code i} an index on this priority queue?
     *
     * @param  i an index
     * @return {@code true} if {@code i} is an index on this priority queue;
     *         {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     */
    public boolean contains(int i) {
        validateIndex(i);
        return qp[i] != -1;
    }

    /**
     * Returns the number of keys on this priority queue.
     *
     * @return the number of keys on this priority queue
     */
    public int size() {
        return n;
    }

    /**
     * Associate key with index i.
     * When i == maxN, the array is resized to double its size
     *
     * @param  i an index
     * @param  key the key to associate with index {@code i}
     * @throws IllegalArgumentException unless {@code 0 <= i <= maxN}
     * @throws IllegalArgumentException if there already is an item
     *         associated with index {@code i}
     */
    public void insert(int i, Key key) {
        if(n == pq.length-1) {
            // double size of array if necessary
            resize(2 * pq.length);
        }
        validateIndex(i);
        if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
        n++;
        qp[i] = n;
        pq[n] = i;
        keys[i] = key;
        swim(n);
    }

    /**
     * Returns an index associated with a maximum key.
     *
     * @return an index associated with a maximum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public int maxIndex() {
        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
        return pq[1];
    }

    /**
     * Returns a maximum key.
     *
     * @return a maximum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Key maxKey() {
        return keys[maxIndex()];
    }

    /**
     * Removes a maximum key and returns its associated index.
     *
     * @return an index associated with a maximum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public int delMax() {
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

    /**
     * Remove the key on the priority queue associated with index {@code i}.
     *
     * @param  i the index of the key to remove
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException no key is associated with index {@code i}
     */
    public void delete(int i) {
        validateIndex(i);
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        int index = qp[i];
        exch(index, n--);
        swim(index);
        sink(index);
        keys[i] = null;
        qp[i] = -1;

    }

    // throw an IllegalArgumentException if i is an invalid index
    private void validateIndex(int i) {
        if (i < 0) throw new IllegalArgumentException("index is negative: " + i);
        if (i >= maxN) throw new IllegalArgumentException("index >= capacity: " + i);
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
        // new capacity will be new keys.length +1
        Key[] tempKeys = (Key[]) new Object[capacity+1];
        int[] tempPq = new int[capacity+1];
        int[] tempQp = new int[capacity+1];
        for(int i = 0; i <= n; i++){
            tempKeys[i] = keys[i];
            tempPq[i] = pq[i];
            tempQp[i] = qp[i];
        }
        for(int i=n+1; i<=capacity; i++){
            tempKeys[i] = null;//not needed
            tempPq[i] = -1;//not needed
            tempQp[i] = -1;
        }
        keys = tempKeys;
        pq = tempPq;
        qp = tempQp;
        maxN = capacity;
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
     *
     ***************************************************************************/




}