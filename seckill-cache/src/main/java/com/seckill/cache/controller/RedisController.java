package com.seckill.cache.controller;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.JedisPool;





@Controller
@ResponseBody
public class RedisController {
    /*
     * POST /redis/set   @key @value
     * GET  /redis/get   @key
     * POST /redis/dec   @key 减库存
     * 
     */
    
    
    private final Logger logger = LoggerFactory.getLogger(RedisController.class);
    
    @Autowired
    RedisTemplate<String,String> redisTemplate;
    
    @ResponseBody
    @RequestMapping(path="/redis/set", method=RequestMethod.POST)
    public boolean set(@RequestParam("key")String key,@RequestParam("value") String value,@RequestParam("expired") int expired) {
	logger.warn("seckill-cache redis-set:key="+key+",value="+value);
	if(key==null || key.length()<=0) return false;
	if(expired <= 0) redisTemplate.opsForValue().set(key, value);
	else redisTemplate.opsForValue().set(key, value, expired, TimeUnit.SECONDS);
	return true;
    }
    
    @ResponseBody
    @RequestMapping(path="/redis/get", method=RequestMethod.GET)
    public String get(@RequestParam("key")String key) {
	logger.warn("seckill-cache redis-get:key="+key);
	return redisTemplate.opsForValue().get(key);
    }
    
    @ResponseBody
    @RequestMapping(path="redis/dec", method=RequestMethod.POST)
    public int decrement(@RequestParam("key")String key) {
	RedisAtomicInteger entityIdCounter = new RedisAtomicInteger(key, redisTemplate.getConnectionFactory());
	int afterStock = entityIdCounter.decrementAndGet();
	return afterStock;
    }
    
    
    
}
