package dev.algo.orderbook.view.impl;

import dev.algo.orderbook.view.Level2View;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Level2ViewImpl implements Level2View {

    PriorityQueue<AggregatedOrder> minSell = new PriorityQueue<>(Comparator.comparing(AggregatedOrder::getPrice));
    PriorityQueue<AggregatedOrder> maxBuy = new PriorityQueue<>(Comparator.comparing(AggregatedOrder::getPrice).reversed());

    PriorityQueue<Integer> maxPqInt = new PriorityQueue<>();

    Map<Long, Order> orderMap = new HashMap<>();

    @Override
    public void onNewOrder(Side side, BigDecimal price, long quantity, long orderId) {

    }

    @Override
    public void onCancelOrder(long orderId) {

    }

    @Override
    public void onReplaceOrder(BigDecimal price, long quantity, long orderId) {

    }

    @Override
    public void onTrade(long quantity, long restingOrderId) {

    }

    @Override
    public long getSizeForPriceLevel(Side side, BigDecimal price) {
        return 0;
    }

    /**
     * Get Book Depth O(1)
     * @param side
     * @return
     */
    @Override
    public long getBookDepth(Side side) {
        if(side == Side.ASK){
            return maxBuy.size();
        }
        return minSell.size();
    }

    /**
     * Get top of the book, O(1)
     * @param side
     * @return
     */
    @Override
    public BigDecimal getTopOfBook(Side side) {
        if(side == Side.ASK){
            assert !maxBuy.isEmpty();
            return maxBuy.peek().price;
        }
        assert !minSell.isEmpty();
        return minSell.peek().price;
    }
}
