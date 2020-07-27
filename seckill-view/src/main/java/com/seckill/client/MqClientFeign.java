package com.seckill.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="seckill-mq")
public interface MqClientFeign {
    @RequestMapping(path="/seckill/{goodsId}/{path}/sendOrder", method=RequestMethod.POST)
    public void seckillExection(@PathVariable("goodsId")Integer goodsId, @PathVariable("path")String path, @RequestParam("username")String username);
}
