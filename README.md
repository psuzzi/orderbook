# orderbook

This repo contains the implementation of an Electronic Order Book

## Interface 

The interface `Level2View` represents a Level 2 view on an electronic order book, see https://en.wikipedia.org/wiki/Order_book_(trading).

```java
public interface Level2View {
    public enum Side { BID, ASK; }
    void onNewOrder(Side side, BigDecimal price, long quantity, long orderId);
    void onCancelOrder(long orderId);
    void onReplaceOrder(BigDecimal price, long quantity, long orderId);
    void onTrade(long quantity, long restingOrderId);
    long getSizeForPriceLevel(Side side, BigDecimal price); 
    long getBookDepth(Side side);
    BigDecimal getTopOfBook(Side side);
}
```

## Analysis

Practically, we can implement the two sides of the order book by priority queues. 
However, a simple priority would implement deletion of a key in linear time. 
Therefore, an optimal implementation would need to use an indexed PQ. 

The rest of the implementation is simple and requires simple problem-solving abilities.

## Implementation 

The implementing class `Level2ViewImpl` uses two indexed priority queues, for _Bid_ and _Ask_ sides.
The implementation uses the classes  `IndexHeap` and `IndexMaxPQ`. 
The latter is based on work by R. Sedgewick, K.Wayne), and modified by me in order to dynamically expand the heap when needed and simplify the codebase.

## Testing

Here are the most meaningful test classes:

* `Level2ViewImplTest` to verify the electronic book implementation. 
* `IndexMaxPQTest` to verify the dynamic index max pq works.


## Further Work

I wish I had more time for implementing my solution, as I see a few improvements I can make to the application, such as: 

* evaluate solutions to dynamically shrinking the heap
* add a Swing GUI to represent the order book, and a test application

## References

* [IndexMaxPQ](https://algs4.cs.princeton.edu/24pq/IndexMaxPQ.java.html) and [MaxPQ](https://algs4.cs.princeton.edu/24pq/MaxPQ.java.html) from Algorithms 4th Edition : [§2.4 Priority Queues](https://algs4.cs.princeton.edu/24pq/) 
* [Introduction to Binary Heaps (MaxHeaps)](https://www.youtube.com/watch?v=WCm3TqScBM8)
* [Indexed Priority Queue | Data Structures](https://www.youtube.com/watch?v=jND_WJ8r7FE&t=771s)
* [Indexed Priority Queue | Data Structure | Source Code](https://www.youtube.com/watch?v=wuuiiGg_2x0)
* [What is an efficient data structure to model order book?](https://quant.stackexchange.com/questions/3783/what-is-an-efficient-data-structure-to-model-order-book)
* [How to Build a Fast Limit Order Book](https://web.archive.org/web/20110219163448/http://howtohft.wordpress.com/2011/02/15/how-to-build-a-fast-limit-order-book/)
* [limit orderbook - quick implementation](https://news.ycombinator.com/item?id=333814)