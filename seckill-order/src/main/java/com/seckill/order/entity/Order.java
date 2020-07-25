package com.seckill.order.entity;

import java.sql.Timestamp;

public class Order {
    private int goodsId;
    private String username;
    private Timestamp orderTime;
    public Order() {
	
    }
    public Order(int goodsId, String username, Timestamp orderTime) {
	this.goodsId = goodsId;
	this.username = username;
	this.orderTime = orderTime;
    }
    public int getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Timestamp getOrderTime() {
        return orderTime;
    }
    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }
}
