package dev.algo.orderbook.view.impl;

import dev.algo.orderbook.view.Level2View;

import java.math.BigDecimal;

/**
 * Represents a single order, with Side, price, quantity and external order id.
 * <p>
 * Orders are usually grouped by price before being inserted in the book
 *
 */
public class Order {

    public final Level2View.Side side;
    public final long id;
    private BigDecimal price;
    private long quantity;

    /** Build an Order with given Side, price, quantity and external order id */
    public Order(Level2View.Side side, BigDecimal price, long quantity, long orderId){
        this.side = side;
        this.price = price;
        this.quantity = quantity;
        this.id = orderId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
