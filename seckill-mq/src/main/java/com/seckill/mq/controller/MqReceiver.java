package com.seckill.mq.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.seckill.mq.client.OrderClientFeign;
import com.seckill.mq.entity.Order;

@Component
@RabbitListener(queues = "OrderInfoQueue")//监听的队列名称 TestDirectQueue
public class MqReceiver {
    private final Logger logger = LoggerFactory.getLogger(MqReceiver.class);
    @Autowired
    private OrderClientFeign orderClientFeign;
    @RabbitHandler
    public void process(Order order) {
        logger.warn("RabbitMQ消费者收到消息  : " + order.toString());
        orderClientFeign.seckillExection(order.getGoodsId(), order.getPath(), order.getUsername());
    }
 
}

