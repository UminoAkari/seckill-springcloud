package com.seckill.order.entity;

import java.sql.Timestamp;

public class Goods {
    private int goodsId;
    private String goodsName;
    private String goodsImg;
    private double goodsPrice;
    private int goodsStock;
    private Timestamp startTime;
    private Timestamp endTime;
    
    
    
    
    
    
    
    public Goods() {
	
    }







    public int getGoodsId() {
        return goodsId;
    }







    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }







    public String getGoodsName() {
        return goodsName;
    }







    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }







    public String getGoodsImg() {
        return goodsImg;
    }







    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }







    public double getGoodsPrice() {
        return goodsPrice;
    }







    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }







    public int getGoodsStock() {
        return goodsStock;
    }







    public void setGoodsStock(int goodsStock) {
        this.goodsStock = goodsStock;
    }







    public Timestamp getStartTime() {
        return startTime;
    }







    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }







    public Timestamp getEndTime() {
        return endTime;
    }







    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
    
    
}
