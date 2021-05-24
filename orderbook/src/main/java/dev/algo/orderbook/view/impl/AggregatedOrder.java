package dev.algo.orderbook.view.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Collection of orders with the same price
 */
public class AggregatedOrder {

    public final BigDecimal price;
    private List<Order> orders = new ArrayList<>();

    public AggregatedOrder(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
