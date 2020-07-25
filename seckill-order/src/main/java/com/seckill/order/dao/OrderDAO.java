package com.seckill.order.dao;

import java.sql.Timestamp;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDAO {
    @Insert("insert IGNORE into `order`(goods_id, username, order_time) values(#{goodsId}, #{username}, #{orderTime})")
    public int insertOrder(int goodsId, String username, Timestamp orderTime);
}
