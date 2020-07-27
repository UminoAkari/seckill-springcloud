package com.seckill.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seckill.client.CacheClientFeign;
import com.seckill.client.GoodsClientFeign;
import com.seckill.client.MqClientFeign;
import com.seckill.client.OrderClientFeign;
import com.seckill.util.RedisKeyUtil;

@Controller
public class TestController {
    @Value("${salt}")
    private String salt;

    private final Logger logger = LoggerFactory.getLogger(TestController.class);

    Map<Integer, Boolean> overFlag = new ConcurrentHashMap<Integer, Boolean>();

    @Autowired
    private OrderClientFeign orderClientFeign;
    @Autowired
    private GoodsClientFeign goodsClientFeign;
    @Autowired
    private CacheClientFeign cacheClientFeign;
    @Autowired
    private MqClientFeign mqClientFeign;

    @ResponseBody
    @RequestMapping(path = "/test/{username}/{goodsId}/kill", method = RequestMethod.GET)
    public String testKill(@PathVariable("username") String username, @PathVariable("goodsId") int goodsId) {
	logger.warn("开始测试kill接口,username="+username+",goodsId="+goodsId);

	// 在内存hashmap中查看库存是否为空标记
	Boolean isOver = overFlag.get(goodsId);
	if (isOver != null && isOver == true) {
	    return "该商品库存已空";
	}

	// 判断是否有库存
	int stock = Integer.parseInt(cacheClientFeign.get(RedisKeyUtil.goodsStockPrefix + goodsId));
	if (stock <= 0) {
	    return "该商品库存已空";
	}

	// 根据订单表判断是否重复秒杀
	String orderExist = cacheClientFeign.get(RedisKeyUtil.goodsStockPrefix + username + goodsId);
	if (orderExist != null && !orderExist.equals("f")) {
	    return "重复秒杀";
	}

	// 减库存
	stock = cacheClientFeign.decrement(RedisKeyUtil.goodsStockPrefix + goodsId);
	if (stock < 0) {
	    return "减库存后小于0";
	}

	// 消息队列下单 TODO
	mqClientFeign.seckillExection(goodsId, getPath(goodsId), username);
	return "成功秒杀,用户" + username + "买到了" + goodsId;

    }
    
    private String getPath(int goodsId) {
	String base = goodsId + "/" + salt;
	return DigestUtils.md5DigestAsHex(base.getBytes());
    }
}
