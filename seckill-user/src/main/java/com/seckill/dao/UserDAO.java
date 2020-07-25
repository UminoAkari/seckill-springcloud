package com.seckill.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.seckill.entity.User;

@Mapper
public interface UserDAO {
    @Select("select * from user where username=#{username}")
    public User selectUser(String username);
    
    @Select("select * from user where username=#{username} and password=#{password}")
    public User checkUser(String username, String password);
    
    @Insert("insert into user(username,password) values(#{username},#{password})")
    public void insertUser(User user);
}
