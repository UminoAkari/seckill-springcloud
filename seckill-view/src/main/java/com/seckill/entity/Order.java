package com.seckill.entity;

import java.sql.Timestamp;

public class Order {
    private int goodsId;
    private String username;
    private Timestamp orderTime;
    private String path;
    public Order() {
	
    }
    public Order(int goodsId, String username, String path, Timestamp orderTime) {
	this.goodsId = goodsId;
	this.username = username;
	this.path = path;
	this.orderTime = orderTime;
    }
    
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
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
    
    @Override
    public String toString() {
	return username+"买"+goodsId+",在"+orderTime;
    }
}
