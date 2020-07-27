package com.seckill.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import com.seckill.client.CacheClientFeign;
import com.seckill.client.GoodsClientFeign;
import com.seckill.entity.Goods;
import com.seckill.util.RedisKeyUtil;



@Controller
public class GoodsController {
    /*
     * 
     * GET  /goods/list  商品列表
     * GET  /goods/{商品id}/detail 商品详情 
     * GET  /goods/{商品id}/getPath  获取秒杀接口
     * POST /seckill/{商品id}/{md5}/exection 执行秒杀
     * 
     * 
     */
    
    @Value("${salt}")
    private String salt;
    
    private final Logger logger = LoggerFactory.getLogger(GoodsController.class);
    
    @Autowired
    private GoodsClientFeign goodsClientFeign;
    
    @Autowired
    private CacheClientFeign cacheClientFeign;
    
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;
    

    @ResponseBody
    @RequestMapping(path="/goods/list", method=RequestMethod.GET)
    public String list(Model model, HttpServletRequest request, HttpServletResponse response) {
	String html = cacheClientFeign.get(RedisKeyUtil.goodsList);
	
	if(html == null || html.length()<=0) {
	    // 商品列表信息
	    List<Goods> list = goodsClientFeign.list();
	    model.addAttribute("list", list);
	    // 用户登录信息
	    HttpSession session = request.getSession();
	    if(session == null) model.addAttribute("username", "无session");
	    else {
		String username = (String)session.getAttribute("username");
		if(username == null) model.addAttribute("username", "session中无登录信息");
		else model.addAttribute("username", "当前登录用户："+username);
	    }
	    // 手动渲染
	    WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
	    html = thymeleafViewResolver.getTemplateEngine().process("goods_list", context);
	    if(html==null||html.length()<=0) logger.error("seckill-view中的GoodsController手动渲染页面得到null");
	    //放入redis
	    cacheClientFeign.set(RedisKeyUtil.goodsList, html, RedisKeyUtil.goodsListExpired);
	    logger.warn("将商品列表手动渲染后放入redis");
	}
	
	return html;
    }
    
    @ResponseBody
    @RequestMapping(path="/goods/{goodsId}/detail", method=RequestMethod.GET)
    public String goodsDetail(@PathVariable("goodsId") Integer goodsId, Model model, HttpServletRequest request, HttpServletResponse response) {
	
	String html = cacheClientFeign.get(RedisKeyUtil.goodsDetailPrefix+goodsId);
	
	if(html!=null && html.length()>0) {
	    return html;
	}
	
	
	if(goodsId == null) {
	    return "redirect:/goods/list";
	}
	Goods goods = goodsClientFeign.goodsDetail(goodsId);
	if(goods == null) {
	    return "redirect:/goods/list";
	}
	
	model.addAttribute("goods", goods);
	
	long now = System.currentTimeMillis();
	long start = goods.getStartTime().getTime();
	long end = goods.getEndTime().getTime();
	int seckillStatus = 0; //未开始=0，正在进行=1，已结束=2
	int remainSeconds = 0;
	if(now < start) {
	    seckillStatus = 0;
	    remainSeconds = (int)((start-now)/1000);
	}
	else if(now > end) {
	    seckillStatus = 2;
	    remainSeconds = -1;
	}
	else if(now >= start && now <= end) {
	    seckillStatus = 1;
	    remainSeconds = 0;
	}
	
	model.addAttribute("seckillStatus", seckillStatus);
	model.addAttribute("remainSeconds", remainSeconds);
	
	// 手动渲染
	WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
	html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", context);
	if(html==null||html.length()<=0) logger.error("seckill-view中的GoodsController手动渲染详情页面得到null");
	//放入redis
	cacheClientFeign.set(RedisKeyUtil.goodsDetailPrefix+goodsId, html, RedisKeyUtil.goodsDetailExpired);
	logger.warn("将id为"+goodsId+"的商品详情页手动渲染后放入redis");
	
	return html;
	
    }
    
    
    @RequestMapping(path="/goods/{goodsId}/getPath", method=RequestMethod.GET)
    public String getPath(@PathVariable("goodsId")Integer goodsId) {
	
	Goods goods = goodsClientFeign.goodsDetail(goodsId);
	long now = System.currentTimeMillis();
	
	if(now>=goods.getStartTime().getTime() && now <= goods.getEndTime().getTime()) {
	    
	    return "forward:/seckill/"+goodsId+"/"+  getMD5(goodsId) +"/execution";
	}
	else return "redirect:/goods/"+goodsId+"/detail";
    }
    
    
    private String getMD5(int goodsId) {
	String base = goodsId + "/" + salt;
	return DigestUtils.md5DigestAsHex(base.getBytes());
    }
}
