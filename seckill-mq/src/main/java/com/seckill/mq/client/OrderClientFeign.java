package com.seckill.mq.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.seckill.mq.entity.SeckillResult;



@FeignClient(value="seckill-order")
public interface OrderClientFeign {

    @RequestMapping(path="/seckill/{goodsId}/{path}/execution", method=RequestMethod.POST)
    public SeckillResult seckillExection(@PathVariable("goodsId")Integer goodsId, @PathVariable("path")String path, @RequestParam("username")String username);
}
