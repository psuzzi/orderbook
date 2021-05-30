package dev.algo.orderbook.view.impl;

import dev.algo.orderbook.view.Level2View;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static dev.algo.orderbook.view.Level2View.Side.*;
import static org.junit.jupiter.api.Assertions.*;

class Level2ViewImplTest {

    Level2ViewImpl view;

    @BeforeEach
    void setup(){
        view = new Level2ViewImpl();
    }


    @Test
    void onNewOrder() {
        view.onNewOrder(BID, new BigDecimal(20), 10, 120L);
        view.onNewOrder(BID, new BigDecimal(21), 15, 121L);
        view.onNewOrder(BID, new BigDecimal(22), 10, 122L);
        view.onNewOrder(ASK, new BigDecimal(23), 100, 123L);
        view.onNewOrder(ASK, new BigDecimal(24), 50, 124L);

        assertEquals(3, view.getBookDepth(BID));
        assertEquals(2, view.getBookDepth(ASK));

        assertEquals(new BigDecimal(22), view.getTopOfBook(BID), "Highest bid");
        assertEquals(new BigDecimal(23), view.getTopOfBook(ASK), "Lowest ask");
    }

    @Test
    void onCancelOrder() {
        onNewOrder();

        view.onCancelOrder(122L);//highest bid
        view.onCancelOrder(123L);//lowest ask

        assertEquals(2, view.getBookDepth(BID));
        assertEquals(1, view.getBookDepth(ASK));

        assertEquals(new BigDecimal(21), view.getTopOfBook(BID), "Highest bid");
        assertEquals(new BigDecimal(24), view.getTopOfBook(ASK), "Lowest ask");
    }

    @Test
    void onReplaceOrder() {
        onNewOrder();

        view.onReplaceOrder(new BigDecimal(22.5), 20, 122L);
        view.onReplaceOrder(new BigDecimal(23.5), 20, 123L);

        assertEquals(new BigDecimal(22.5), view.getTopOfBook(BID), "Highest bid");
        assertEquals(new BigDecimal(23.5), view.getTopOfBook(ASK), "Lowest ask");
    }

    @Test
    void onTrade() {
        view.onNewOrder(BID, new BigDecimal(20), 10, 120L);
        view.onNewOrder(BID, new BigDecimal(21), 15, 121L);
        view.onNewOrder(BID, new BigDecimal(22), 10, 122L);
        view.onNewOrder(ASK, new BigDecimal(23), 100, 123L);
        view.onNewOrder(ASK, new BigDecimal(24), 50, 124L);

        assertEquals(3, view.getBookDepth(BID));
        assertEquals(1, view.getBookDepth(ASK));
        assertEquals(new BigDecimal(22), view.getTopOfBook(BID), "Highest bid");

        view.onReplaceOrder(new BigDecimal(22.5), 20, 122L);
        assertEquals(new BigDecimal(22.5), view.getTopOfBook(BID), "Highest bid");
    }
//
//    @Test
//    void getSizeForPriceLevel() {
//    }
//
//    @Test
//    void getBookDepth() {
//    }
//
//    @Test
//    void getTopOfBook() {
//    }
}