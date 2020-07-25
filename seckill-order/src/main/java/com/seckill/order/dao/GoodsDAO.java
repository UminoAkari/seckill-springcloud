package com.seckill.order.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface GoodsDAO {
    @Update("update goods set goods_stock = goods_stock-1 where goods_id=#{goodsId} and goods_stock>0")
    public int reduceStock(int goodsId);
}
