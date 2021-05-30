package dev.algo.orderbook.view.impl;

import dev.algo.orderbook.view.Level2View;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static dev.algo.orderbook.view.Level2View.Side.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the public methods of the {@code Level2ViewImplTest}
 */
class Level2ViewImplTest {

    Level2ViewImpl view;

    @BeforeEach
    void setup(){
        view = new Level2ViewImpl();
    }


    /**
     * Create new orders and test the correctness based on book depth and top of the book
     */
    @Test
    void onNewOrder() {
        view.onNewOrder(BID, new BigDecimal(20), 10, 1001L);
        view.onNewOrder(BID, new BigDecimal(21), 15, 1002L);
        view.onNewOrder(BID, new BigDecimal(22), 10, 1003L);
        view.onNewOrder(ASK, new BigDecimal(23), 100, 2001L);
        view.onNewOrder(ASK, new BigDecimal(24), 50, 2002L);

        assertEquals(3, view.getBookDepth(BID));
        assertEquals(2, view.getBookDepth(ASK));

        assertEquals(new BigDecimal(22), view.getTopOfBook(BID), "Highest bid");
        assertEquals(new BigDecimal(23), view.getTopOfBook(ASK), "Lowest ask");
    }

    /**
     * Create new orders, delete some of thems, and test the correctness based on book depth and top of the book
     */
    @Test
    void onCancelOrder() {
        view.onNewOrder(BID, new BigDecimal(20), 10, 1001L);
        view.onNewOrder(BID, new BigDecimal(21), 15, 1002L);
        view.onNewOrder(BID, new BigDecimal(22), 10, 1003L);
        view.onNewOrder(ASK, new BigDecimal(23), 100, 2001L);
        view.onNewOrder(ASK, new BigDecimal(24), 50, 2002L);

        view.onCancelOrder(1003L);//highest bid
        view.onCancelOrder(2001L);//lowest ask

        assertEquals(2, view.getBookDepth(BID));
        assertEquals(1, view.getBookDepth(ASK));

        assertEquals(new BigDecimal(21), view.getTopOfBook(BID), "Highest bid");
        assertEquals(new BigDecimal(24), view.getTopOfBook(ASK), "Lowest ask");
    }

    /**
     * Create new orders, replace some of thems, and test the correctness based on book depth and top of the book.
     */
    @Test
    void onReplaceOrder() {
        view.onNewOrder(BID, new BigDecimal(20), 10, 1001L);
        view.onNewOrder(BID, new BigDecimal(21), 15, 1002L);
        view.onNewOrder(BID, new BigDecimal(22), 10, 1003L);
        view.onNewOrder(ASK, new BigDecimal(23), 100, 2001L);
        view.onNewOrder(ASK, new BigDecimal(24), 50, 2002L);

        view.onReplaceOrder(new BigDecimal(22.5), 20, 1003L);
        view.onReplaceOrder(new BigDecimal(23.5), 50, 2001L);

        assertEquals(new BigDecimal(22.5), view.getTopOfBook(BID), "Highest bid");
        assertEquals(new BigDecimal(23.5), view.getTopOfBook(ASK), "Lowest ask");
    }

    /**
     * Create new orders, perform some trades, and test the correctness based on book depth, top of the book, and
     * size for price level.
     */
    @Test
    void onTrade() {
        view.onNewOrder(BID, new BigDecimal(20), 10, 1001L);
        view.onNewOrder(BID, new BigDecimal(21), 15, 1002L);
        view.onNewOrder(BID, new BigDecimal(22), 10, 1003L);
        view.onNewOrder(ASK, new BigDecimal(23), 100, 2001L);
        view.onNewOrder(ASK, new BigDecimal(24), 50, 2002L);

        view.onTrade(10, 1003);

        assertEquals(2, view.getBookDepth(BID));
        assertEquals(2, view.getBookDepth(ASK));
        assertEquals(new BigDecimal(21), view.getTopOfBook(BID), "Highest bid");

        view.onTrade(5, 1002);
        long size21 = view.getSizeForPriceLevel(BID, new BigDecimal(21));
        assertEquals(10, size21, "Remaining size at price 21 after trade");
    }

    /**
     * Create new orders, and test the correctness of getSizeForPriceLevel().
     */
    @Test
    void getSizeForPriceLevel() {
        view.onNewOrder(BID, new BigDecimal(20), 10, 1001L);
        view.onNewOrder(BID, new BigDecimal(22), 15, 1002L);
        view.onNewOrder(BID, new BigDecimal(22), 10, 1003L);
        view.onNewOrder(BID, new BigDecimal(22), 10, 1004L);
        view.onNewOrder(ASK, new BigDecimal(23), 100, 2001L);
        view.onNewOrder(ASK, new BigDecimal(24), 50, 2002L);
        view.onNewOrder(ASK, new BigDecimal(24), 50, 2003L);
        view.onNewOrder(ASK, new BigDecimal(24), 50, 2004L);

        assertEquals(35, view.getSizeForPriceLevel(BID, new BigDecimal(22)), "Size for price level");
        assertEquals(150, view.getSizeForPriceLevel(ASK, new BigDecimal(24)), "Size for price level");
    }

    /**
     * Create new orders, and test the correctness of getBookDepth().
     */
    @Test
    void getBookDepth() {
        view.onNewOrder(BID, new BigDecimal(20), 10, 1001L);
        view.onNewOrder(BID, new BigDecimal(22), 15, 1002L);
        view.onNewOrder(BID, new BigDecimal(22), 10, 1003L);
        view.onNewOrder(BID, new BigDecimal(22), 10, 1004L);
        view.onNewOrder(ASK, new BigDecimal(23), 100, 2001L);
        view.onNewOrder(ASK, new BigDecimal(24), 50, 2002L);
        view.onNewOrder(ASK, new BigDecimal(24), 50, 2003L);
        view.onNewOrder(ASK, new BigDecimal(24), 50, 2004L);

        assertEquals(2, view.getBookDepth(BID), "Book depth");
        assertEquals(2, view.getBookDepth(ASK), "Book depth");
    }

    /**
     * Create new orders, then cancel and modify some of them. Then, verify the correctness od getTopOfBook().
     */
    @Test
    void getTopOfBook() {
        view.onNewOrder(BID, new BigDecimal(20), 10, 1001L);
        view.onNewOrder(ASK, new BigDecimal(23), 100, 2001L);

        assertEquals(new BigDecimal(20), view.getTopOfBook(BID), "Highest bid");
        assertEquals(new BigDecimal(23), view.getTopOfBook(ASK), "Lowest ask");

        view.onNewOrder(BID, new BigDecimal(21), 15, 1002L);
        view.onNewOrder(ASK, new BigDecimal(22), 10, 1003L);

        assertEquals(new BigDecimal(21), view.getTopOfBook(BID), "Highest bid");
        assertEquals(new BigDecimal(22), view.getTopOfBook(ASK), "Lowest ask");

        view.onCancelOrder(1002L);
        assertEquals(new BigDecimal(20), view.getTopOfBook(BID), "Highest bid");
        view.onCancelOrder(2001L);
        assertEquals(new BigDecimal(22), view.getTopOfBook(ASK), "Lowest ask");

        view.onNewOrder(BID, new BigDecimal(22.5), 10, 1004L);
        view.onNewOrder(ASK, new BigDecimal(21), 50, 2002L);

        assertEquals(new BigDecimal(22.5), view.getTopOfBook(BID), "Highest bid");
        assertEquals(new BigDecimal(21), view.getTopOfBook(ASK), "Lowest ask");

        view.onTrade(10, 1004L);
        view.onTrade(50, 2002L);

        assertEquals(new BigDecimal(20), view.getTopOfBook(BID), "Highest bid");
        assertEquals(new BigDecimal(22), view.getTopOfBook(ASK), "Lowest ask");
    }
}