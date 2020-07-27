package com.seckill.mq.entity;

import java.sql.Timestamp;

public class SeckillResult {
    public static final int SUCCESS = 1;//秒杀成功
    public static final int EMPTY = 2;//库存已空
    public static final int REPEAT = 3;//重复秒杀
    public static final int WRONG_PATH = 4;//伪造地址
    
    
    private int status;
    private String statusInfo;
    private int goodsId;
    private String username;
    private Timestamp timestamp;
    
    public SeckillResult() {
	
    }
    public SeckillResult(int goodsId, String username) {
	this.goodsId=goodsId;
	this.username=username;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
        if(status == 1) statusInfo="秒杀成功";
        if(status == 2) statusInfo="库存已空";
        if(status == 3) statusInfo="重复秒杀";
        if(status == 4) statusInfo="伪造地址";
    }
    public String getStatusInfo() {
        return statusInfo;
    }
    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo;
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
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    
}
