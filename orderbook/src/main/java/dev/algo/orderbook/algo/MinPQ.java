package dev.algo.orderbook.algo;

import java.util.ArrayList;

/**
 * Priority Queue of elements of which a comparator is provided by the client.
 * @param <Key>
 */
public class MinPQ<Key extends Comparable<Key>> {

    // heap-ordered complete binary tree in pq[1..n] with pq[0] unused
    private final ArrayList<Key> pq;
    private int n=0;

    public MinPQ(){
        pq = new ArrayList<>();
        pq.add(0,null);
    }

    public void insert(Key v){
        pq.add(++n, v);
        swim(n);
    }

    public Key delMin(){
        Key min = pq.get(1);
        exch(1, n--);//exchange with last item
        pq.remove(n+1);
        sink(1);
        return min;
    }

    public boolean isEmpty(){
        return n==0;
    }

    public Key min(){
        return pq.get(1);
    }

    public int size(){
        return n;
    }

    private boolean less(int i, int j){
        return pq.get(i).compareTo(pq.get(j))<0;
    }

    private boolean greater(int i, int j) {
        return pq.get(i).compareTo(pq.get(j))>0;
    }

    private void exch(int i, int j){
        Key t = pq.get(i);
        pq.set(i, pq.get(j));
        pq.set(j, t);
    }

    private void swim(int k){
        while (k > 1 && greater(k/2, k))
        {
            exch(k/2, k);
            k = k/2;
        }
    }

    private void sink(int k){
        while (2*k <= n)
        {
            int j = 2*k;
            if (j < n && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }


}


