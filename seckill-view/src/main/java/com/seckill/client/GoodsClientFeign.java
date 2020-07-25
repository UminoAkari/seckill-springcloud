package com.seckill.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.seckill.entity.Goods;
import com.seckill.entity.SeckillResult;

@FeignClient(value="seckill-goods")
public interface GoodsClientFeign {
    @RequestMapping(path="/goods/list", method=RequestMethod.GET)
    public List<Goods> list();
    
    @RequestMapping(path="/goods/{goodsId}/detail", method=RequestMethod.GET)
    public Goods goodsDetail(@PathVariable("goodsId") int goodsId);

}
