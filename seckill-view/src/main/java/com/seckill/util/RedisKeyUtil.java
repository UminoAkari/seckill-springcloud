package com.seckill.util;

public class RedisKeyUtil {
    public static final String goodsList = "gl";
    public static final int goodsListExpired = 5;
    
    public static final String goodsDetailPrefix = "gd";
    public static final int goodsDetailExpired = 5;
    
    public static final String goodsStockPrefix = "gs";
    public static final int goodsStockExpired = 0; // 0表示永不过期
    
    public static final String orderPrefix = "op";
    public static final int orderExpired = 0; // 0表示永不过期
}
