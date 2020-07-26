package com.seckill.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="seckill-cache")
public interface CacheClientFeign {
    @RequestMapping(path="/redis/set", method=RequestMethod.POST)
    public boolean set(@RequestParam("key")String key,@RequestParam("value") String value,@RequestParam("expired") int expired);
    
    @RequestMapping(path="/redis/get", method=RequestMethod.GET)
    public String get(@RequestParam("key")String key);
}
