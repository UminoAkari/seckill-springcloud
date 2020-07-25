package com.seckill.entity;

public class User {
    private String username;
    private String password;
    public User() {
	
    }
    public User(String n, String p) {
	username=n;
	password=p;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
