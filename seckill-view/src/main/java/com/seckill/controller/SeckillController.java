package com.seckill.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.seckill.client.CacheClientFeign;
import com.seckill.client.GoodsClientFeign;
import com.seckill.client.MqClientFeign;
import com.seckill.client.OrderClientFeign;
import com.seckill.entity.Goods;
import com.seckill.util.RedisKeyUtil;



@Controller
public class SeckillController implements InitializingBean {
    /*
     * 
     * GET  /goods/list  商品列表
     * GET  /goods/{商品id}/detail 商品详情 
     * POST /seckill/{商品id}/{path}/exection 执行秒杀
     * 
     * 
     */
    
    @Value("${salt}")
    private String salt;
 
    
    private final Logger logger = LoggerFactory.getLogger(SeckillController.class);
    
    Map<Integer, Boolean> overFlag = new ConcurrentHashMap<Integer, Boolean>();
    
    @Autowired
    private OrderClientFeign orderClientFeign;
    @Autowired
    private GoodsClientFeign goodsClientFeign;
    @Autowired
    private CacheClientFeign cacheClientFeign;
    @Autowired
    private MqClientFeign mqClientFeign;
    
    //初始化前先把库存数量存入redis
    @Override
    public void afterPropertiesSet() throws Exception {
	List<Goods> goods = goodsClientFeign.list();
	if(goods == null) return;
	logger.warn("初始化前先把库存数量存入redis");
	for(Goods g : goods) {
	    int goodsId = g.getGoodsId();
	    int stock = g.getGoodsStock();
	    logger.warn("初始化前先把库存数量存入redis:"+RedisKeyUtil.goodsStockPrefix+goodsId+","+Integer.toString(stock));
	    cacheClientFeign.set(RedisKeyUtil.goodsStockPrefix+goodsId, Integer.toString(stock), RedisKeyUtil.goodsStockExpired);
	}
	
    }
    
    
    @RequestMapping(path="/seckill/{goodsId}/{path}/execution", method=RequestMethod.GET)
    public String seckillExecution(Model model, @PathVariable("goodsId")Integer goodsId, @PathVariable("path")String path, HttpServletRequest request) {

	
	//检查是否登录，没登录不会执行秒杀操作
	
	HttpSession session = request.getSession();
	if(session == null) {
	    model.addAttribute("info", "session不存在");
	    return "info_page";
	}
	String username = (String)session.getAttribute("username");
	if(username == null) {
	    model.addAttribute("info", "session中找不到登录信息");
	    return "info_page";
	}

	
	
	
	
	
	
	//判断地址
	if( ! path.equals(getPath(goodsId)) ) {
	    model.addAttribute("info", "地址错误");
	    return "info_page";
	}
	

	
	
	//在内存hashmap中查看库存是否为空标记
	Boolean isOver = overFlag.get(goodsId);
	if(isOver!=null && isOver==true) {
	    model.addAttribute("info", "该商品库存已空");
	    return "info_page";
	}
	
	//判断是否有库存
	int stock = Integer.parseInt(cacheClientFeign.get(RedisKeyUtil.goodsStockPrefix+goodsId));
	if( stock <= 0 ) {
	    overFlag.put(goodsId, true);
	    model.addAttribute("info", "该商品库存已空");
	    return "info_page";
	}
	
	//根据订单表判断是否重复秒杀
	String orderExist = cacheClientFeign.get(RedisKeyUtil.goodsStockPrefix+username+goodsId);
	if(orderExist!=null &&  !orderExist.equals("f")) {
	    model.addAttribute("info", "重复秒杀");
	    return "info_page";
	}
	
	//减库存
	stock = cacheClientFeign.decrement(RedisKeyUtil.goodsStockPrefix+goodsId);
	if(stock < 0) {
	    model.addAttribute("info", "减库存后小于0");
	    return "info_page";
	}
	
	// 消息队列下单 TODO
	mqClientFeign.seckillExection(goodsId, path, username);
	model.addAttribute("info", "成功秒杀,用户"+username+"买到了"+goodsId);
	return "info_page";
	/*
	//返回秒杀操作的结果
	SeckillResult result= orderClientFeign.seckillExection(goodsId, path, username);
	model.addAttribute("info", result.getStatusInfo());
	return "info_page";
	*/
    }

    private String getPath(int goodsId) {
	String base = goodsId + "/" + salt;
	return DigestUtils.md5DigestAsHex(base.getBytes());
    }
    

    
}
