package dev.algo.orderbook.view.impl;

import dev.algo.orderbook.view.Level2View;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Level2ViewImpl implements Level2View {


    /*
     * Buyers bids on a max heap. Top of the heap is the highest bid
     * Sellers asks on a min heap. Top opf the heap is the lowest ask
     * by consequence the bid-ask spread is lowest_ask - highest_bid.
     */
    IndexHeap<BigDecimal, AggregatedOrder> bids = new IndexHeap<>(Comparator.<BigDecimal>naturalOrder());
    IndexHeap<BigDecimal, AggregatedOrder> asks = new IndexHeap<>(Comparator.<BigDecimal>reverseOrder());

    // map order ids with corresponding aggregated order, which can caontain multiple single orders
    Map<Long, AggregatedOrder> orderMap = new HashMap<>();

    public Level2ViewImpl(){
        IndexHeap<BigDecimal, AggregatedOrder> bids = new IndexHeap<>(Comparator.<BigDecimal>naturalOrder());
        IndexHeap<BigDecimal, AggregatedOrder> asks = new IndexHeap<>(Comparator.<BigDecimal>reverseOrder());

        // map order ids with corresponding aggregated order, which can caontain multiple single orders
        Map<Long, AggregatedOrder> orderMap = new HashMap<>();
    }

    @Override
    public void onNewOrder(Side side, BigDecimal price, long quantity, long orderId) {
        addOrder(side, price, quantity, orderId);
    }

    @Override
    public void onCancelOrder(long orderId) {
        cancelOrder(orderId);
    }

    @Override
    public void onReplaceOrder(BigDecimal price, long quantity, long orderId) {
        Order order = cancelOrder(orderId);
        addOrder(order.side, price, quantity, orderId);
    }

    private IndexHeap<BigDecimal, AggregatedOrder> getHeap(Side side){
        return (side==Side.BID) ? bids : asks;
    }

    private void addOrder(Side side, BigDecimal price, long quantity, long orderId){
        IndexHeap<BigDecimal, AggregatedOrder> heap = getHeap(side);

        if(!heap.containsKey(price)) {
            // O(log n) requires reheapify
            heap.put(price, new AggregatedOrder(price));
        }

        AggregatedOrder aggregatedOrder = heap.getValue(price);
        Order order = new Order(side, price, quantity, orderId);
        aggregatedOrder.add(order);
        orderMap.put(orderId, aggregatedOrder);
    }

    private Order cancelOrder(long orderId){
        AggregatedOrder aggregatedOrder = orderMap.get(orderId);
        Order order = aggregatedOrder.remove(orderId);
        if(aggregatedOrder.getTotalSize()==0){
            IndexHeap<BigDecimal, AggregatedOrder> heap = getHeap(order.side);
            // O(log n) requires reheapify
            heap.removeKey(aggregatedOrder.price);
        }
        return order;
    }

    @Override
    public void onTrade(long quantity, long restingOrderId) {
        AggregatedOrder aggregatedOrder = orderMap.get(restingOrderId);
        Order order = aggregatedOrder.get(restingOrderId);

        // Assumption: traded quantity is less or equal than resting order quantity
        assert quantity <= order.getQuantity();

        if(quantity==order.getQuantity()){
            cancelOrder(restingOrderId);
        } else {
            order.setQuantity(order.getQuantity()-quantity);
        }

    }

    @Override
    public long getSizeForPriceLevel(Side side, BigDecimal price) {
        AggregatedOrder aggregatedOrder = getHeap(side).getValue(price);
        return aggregatedOrder.getTotalSize();
    }

    /**
     * Get Book Depth O(1)
     * @param side
     * @return
     */
    @Override
    public long getBookDepth(Side side) {
        return getHeap(side).size();
    }

    /**
     * Get top of the book, O(1)
     * @param side
     * @return
     */
    @Override
    public BigDecimal getTopOfBook(Side side) {
        return getHeap(side).topKey();
    }
}
