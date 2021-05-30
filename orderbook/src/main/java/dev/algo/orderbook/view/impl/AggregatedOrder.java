package dev.algo.orderbook.view.impl;

import java.math.BigDecimal;
import java.util.*;

/**
 * Represents an aggregation of orders at the same price point.
 *
 */
public class AggregatedOrder {

    public final BigDecimal price;
    private Map<Long, Order> orders;
    private long totalSize;

    /** Initialize an AggregateOrder at the given price level */
    public AggregatedOrder(BigDecimal price) {
        this.price = price;
        orders = new HashMap<>();
        totalSize = 0;
    }

    public BigDecimal getPrice() {
        return price;
    }

    /** Add an Order to this AggregateOrder and increase the size */
    public void add(Order order){
        orders.put(order.id, order);
        totalSize+=order.getQuantity();
    }

    /** Returns the child Order with the specified (external) orderId */
    public Order get(long orderId){
        return orders.get(orderId);
    }

    /** Removes the Order with the specified (external) orderId */
    public Order remove(long orderId){
        Order order = orders.remove(orderId);
        totalSize-=order.getQuantity();
        return order;
    }

    /**
     * Aggregated total size of all the orders at this price point.
     * @return
     */
    public long getTotalSize() {
        return totalSize;
    }

    /**
     * Updates the total size by removing the given quantity
     * @param quantity
     */
    public void subtractSize(long quantity) {
        totalSize-=quantity;
    }
}
