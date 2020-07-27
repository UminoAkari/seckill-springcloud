package com.seckill.mq.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seckill.mq.entity.Order;




@Controller
public class MqController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private MqReceiver receiver;
    
    private final Logger logger = LoggerFactory.getLogger(MqController.class);
    
    @ResponseBody
    @RequestMapping(path="/seckill/{goodsId}/{path}/sendOrder", method=RequestMethod.POST)
    public void seckillExection(@PathVariable("goodsId")Integer goodsId, @PathVariable("path")String path, @RequestParam("username")String username) {
	Order order = new Order(goodsId, username,path, new Timestamp(System.currentTimeMillis()));
	
	rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting",order);
	logger.warn("向mq发送了信息："+order);
    }

}
