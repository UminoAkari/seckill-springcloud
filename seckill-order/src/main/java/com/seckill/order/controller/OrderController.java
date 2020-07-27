package com.seckill.order.controller;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.seckill.order.dao.GoodsDAO;
import com.seckill.order.dao.OrderDAO;
import com.seckill.order.entity.SeckillResult;
import com.seckill.order.exception.RepeatKillException;
import com.seckill.order.exception.StockEmptyException;



@Controller
@ResponseBody
public class OrderController {
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Value("${salt}")
    private String salt;
    
    @Autowired
    private GoodsDAO goodsDAO;
    
    @Autowired
    private OrderDAO orderDAO;
    
    @Transactional
    @RequestMapping(path="/seckill/{goodsId}/{path}/execution", method=RequestMethod.POST)
    public SeckillResult seckillExection(@PathVariable("goodsId")Integer goodsId, @PathVariable("path")String path, @RequestParam("username")String username) {
	SeckillResult res = new SeckillResult(goodsId, username);
	logger.warn("order controller goodsId="+goodsId+",path="+path+",username="+username);
	//先验证path是否正确
	if( ! path.equals(getPath(goodsId)) ) {
	    res.setStatus(SeckillResult.WRONG_PATH);
	}
	else {
	    try {
		//seckillSQL(goodsId, username);
		
		
		//尝试减库存
		int updateRes = goodsDAO.reduceStock(goodsId);
		if(updateRes != 1) throw new StockEmptyException("库存为空");
		
		//logger.warn("order controller减库存 goodsId="+goodsId+",path="+path+",username="+username+", res="+updateRes);
		
		//保存订单
		int insertRes = orderDAO.insertOrder(goodsId, username, new Timestamp(System.currentTimeMillis()));
		if(insertRes != 1) throw new RepeatKillException("重复秒杀");
		logger.warn("order controller保存订单 goodsId="+goodsId+",path="+path+",username="+username+", res="+insertRes);
		
		res.setStatus(SeckillResult.SUCCESS);
	    }catch(RepeatKillException e) {
		logger.warn("order controller goodsId="+goodsId+",username="+username+", 重复秒杀，回滚");
		res.setStatus(SeckillResult.REPEAT);
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	    }catch(StockEmptyException e) {
		logger.warn("order controller goodsId="+goodsId+",username="+username+", 库存已经为空，回滚");
		res.setStatus(SeckillResult.EMPTY);
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	    }
	}
	
	
	
	return res;
    }
    /*
    @Transactional
    private void seckillSQL(int goodsId, String username) throws RepeatKillException,StockEmptyException {
	//尝试减库存
	int updateRes = goodsDAO.reduceStock(goodsId);
	if(updateRes != 1) throw new StockEmptyException("库存为空");
	
	//保存订单
	int insertRes = orderDAO.insertOrder(goodsId, username, new Timestamp(System.currentTimeMillis()));
	if(insertRes != 1) throw new RepeatKillException("重复秒杀");
    }
    */
    
    private String getPath(int goodsId) {
	String base = goodsId + "/" + salt;
	return DigestUtils.md5DigestAsHex(base.getBytes());
    }
}
