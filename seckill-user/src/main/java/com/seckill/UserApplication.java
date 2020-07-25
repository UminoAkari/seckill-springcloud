package com.seckill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import brave.sampler.Sampler;
import cn.hutool.core.util.NetUtil;
@SpringBootApplication
@EnableEurekaClient
public class UserApplication {

    public static void main(String[] args) {

	Logger logger = LoggerFactory.getLogger(UserApplication.class);
	
	ConfigurableApplicationContext context=new SpringApplicationBuilder(UserApplication.class).run(args);
	logger.warn("seckill-user启动成功，端口是"+context.getEnvironment().getProperty("server.port"));
    }
    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }

}
