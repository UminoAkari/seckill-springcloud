package com.seckill.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.seckill.entity.Goods;



@Mapper
public interface GoodsDAO {
    @Select("select * from goods")
    public List<Goods> selectAll();
    
    @Select("select * from goods where goods_id=#{goodsId}")
    public Goods selectGoods(int goodsId);
    
    
}
