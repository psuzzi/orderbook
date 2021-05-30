package dev.algo.orderbook.view;

/*
 * Interface representing a Level 2 view on an electronic order book (see https://en.wikipedia.org/wiki/Order_book_(trading) ).
 *
 * Instances of the implementing class should listen to market events (new orders, order cancellations/replacements, trades)
 * and provide a Level 2 view (total aggregated order quantity for a given side and price level).
 *
 */

import java.math.BigDecimal;

public interface Level2View {

    /** Buyers bids, and Sellers asks */
    public enum Side { BID, ASK; }

    /** Insert order with given details, and externally provided id*/
    void onNewOrder(Side side, BigDecimal price, long quantity, long orderId);

    /** remove order given external id */
    void onCancelOrder(long orderId);

    /** replace an existing order given its external id */
    void onReplaceOrder(BigDecimal price, long quantity, long orderId);

    /** When an aggressor order crosses the spread, it will be matched with an existing resting order, causing a trade.
     * The aggressor order will NOT cause an invocation of onNewOrder.
     */
    void onTrade(long quantity, long restingOrderId);

    /** total quantity of existing orders on this price level */
    long getSizeForPriceLevel(Side side, BigDecimal price);

    /** get the number of price levels on the specified side */
    long getBookDepth(Side side);

    /** get highest bid or lowest ask, resp. */
    BigDecimal getTopOfBook(Side side);
}

