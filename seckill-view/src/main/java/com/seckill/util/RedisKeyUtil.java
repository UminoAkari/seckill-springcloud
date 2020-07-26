package com.seckill.util;

public class RedisKeyUtil {
    public static final String goodsList = "gl";
    public static final int goodsListExpired = 60;
    
    public static final String goodsDetailPrefix = "gd";
    public static final int goodsDetailExpired = 60;
    
    public static final String goodsStockPrefix = "gs";
    public static final int goodsStockExpired = 0; // 0表示永不过期
}
