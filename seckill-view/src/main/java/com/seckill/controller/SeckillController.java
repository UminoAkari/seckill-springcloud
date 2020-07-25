package com.seckill.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.seckill.client.GoodsClientFeign;
import com.seckill.client.OrderClientFeign;
import com.seckill.entity.Goods;
import com.seckill.entity.SeckillResult;

@Controller
public class SeckillController {
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
    
    @Autowired
    private OrderClientFeign orderClientFeign;
    
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
	
	//返回秒杀操作的结果
	SeckillResult result= orderClientFeign.seckillExection(goodsId, path, username);
	model.addAttribute("info", result.getStatusInfo());
	return "info_page";
	
    }
    
    
}
