package com.seckill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seckill.dao.UserDAO;
import com.seckill.entity.User;

@Controller
@ResponseBody
public class UserController {
    @Autowired
    private UserDAO userDAO;
    
    @Value("${server.port}")
    String port;

    @RequestMapping(path="/user/login", method=RequestMethod.POST)
    public boolean login(@RequestParam("username")String username, @RequestParam("password")String password) {
	User user = userDAO.checkUser(username, password);
	return user!=null;
    }
}
