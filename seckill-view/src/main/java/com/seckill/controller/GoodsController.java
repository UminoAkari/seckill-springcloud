package com.seckill.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.seckill.client.GoodsClientFeign;
import com.seckill.entity.Goods;



@Controller
public class GoodsController {
    /*
     * 
     * GET  /goods/list  商品列表
     * GET  /goods/{商品id}/detail 商品详情 
     * POST /seckill/{商品id}/{md5}/exection 执行秒杀
     * 
     * 
     */
    
    @Value("${salt}")
    private String salt;
    
    private final Logger logger = LoggerFactory.getLogger(GoodsController.class);
    
    @Autowired
    private GoodsClientFeign goodsClientFeign;
    
    
    @RequestMapping(path="/goods/list", method=RequestMethod.GET)
    public String list(Model model) {
	List<Goods> list = goodsClientFeign.list();
	model.addAttribute("list", list);
	return "goods_list";
    }
    
    @RequestMapping(path="/goods/{goodsId}/detail", method=RequestMethod.GET)
    public String goodsDetail(@PathVariable("goodsId") Integer goodsId, Model model) {
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
	String path = "";
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
	    path = getPath(goods.getGoodsId());
	}
	
	model.addAttribute("seckillStatus", seckillStatus);
	model.addAttribute("remainSeconds", remainSeconds);
	model.addAttribute("path", path);
	
	return "goods_detail";
	
    }
    
   // @RequestMapping(path="/goods/{goodsId}/{md5}/exection", method=RequestMethod.GET)
    
    
    private String getPath(int goodsId) {
	String base = goodsId + "/" + salt;
	return DigestUtils.md5DigestAsHex(base.getBytes());
    }

}
