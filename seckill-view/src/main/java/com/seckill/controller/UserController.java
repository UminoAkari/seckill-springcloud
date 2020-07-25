package com.seckill.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.math.stat.inference.TTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.seckill.client.UserClientFeign;




@Controller
public class UserController {
    
    /*
     * 
     * 
     * GET  /user/login 跳转到登录页面
     * POST /user/login 提交登录信息
     * GET  /user/logout 退出登录
     */
    
    
    @Autowired
    private UserClientFeign userClientFeign;
    
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Value("${server.port}")
    String port;
    
    @RequestMapping(path="/user/login",method=RequestMethod.GET)
    public String login_page() {
	//logger.warn("GET /login  跳转到/resources/templates/login.html");
	return "login";
    }
    
    @RequestMapping(path="/user/login",method=RequestMethod.POST)
    public String login_submit(Model m,
	    @RequestParam("username")String username, 
	    @RequestParam("password")String password,
	    HttpServletRequest request) {
	boolean successfulLogin = userClientFeign.login(username, password);
	//logger.warn("从userClientFeign得到用户名为"+username+"的登录验证结果"+successfulLogin);
	if(successfulLogin) {
	    request.getSession(true).setAttribute("username", username);
	    return "redirect:/goods/list";
	}
	else {
	    m.addAttribute("info", "登录失败");
	    return "info_page";
	}

	
    }
    
    @RequestMapping("/user/logout")
    public String tt(HttpServletRequest request) {
	request.getSession().setAttribute("username", null);
	return "redirect:/user/login";
    }
    
    
}
