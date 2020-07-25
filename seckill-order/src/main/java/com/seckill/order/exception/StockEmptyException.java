package com.seckill.order.exception;

public class StockEmptyException extends RuntimeException {
    public StockEmptyException(String msg) {
	super(msg);
    }
}
