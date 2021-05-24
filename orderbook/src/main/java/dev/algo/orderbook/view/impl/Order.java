package dev.algo.orderbook.view.impl;

import dev.algo.orderbook.view.Level2View;

import java.math.BigDecimal;

public class Order {
    long OrderId;
    BigDecimal price;
    Level2View.Side side;
}
