package com.seckill.controller;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seckill.dao.GoodsDAO;
import com.seckill.entity.Goods;
import com.seckill.entity.SeckillResult;

@ResponseBody
@Controller
public class GoodsController {
    
    @Autowired
    private GoodsDAO goodsDAO;
    
    @Value("${server.port}")
    private String port;
    
    private final Logger logger = LoggerFactory.getLogger(GoodsController.class);
    
    @RequestMapping(path="/goods/list", method = RequestMethod.GET)
    public List<Goods> list(){
	List<Goods> list = goodsDAO.selectAll();
	logger.warn("goods:"+port+"  获得商品列表");
	return list;
    }
    
    @RequestMapping(path="/goods/{goodsId}/detail", method=RequestMethod.GET)
    public Goods goodsDetail(@PathVariable("goodsId") int goodsId) {
	return goodsDAO.selectGoods(goodsId);
    }
    

}
