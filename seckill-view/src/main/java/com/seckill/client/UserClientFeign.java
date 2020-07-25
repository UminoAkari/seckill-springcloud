package com.seckill.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



@FeignClient(value="seckill-user")
public interface UserClientFeign {
    /*
    @RequestMapping(path="/getUser")
    public User getUser(@RequestParam("username")String username);
    */
    
    @RequestMapping(path="/user/login", method=RequestMethod.POST)
    public boolean login(@RequestParam("username")String username, @RequestParam("password")String password);
}
