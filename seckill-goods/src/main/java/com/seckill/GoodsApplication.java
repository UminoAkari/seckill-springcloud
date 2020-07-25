package com.seckill;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;


import brave.sampler.Sampler;

@EnableEurekaClient
@SpringBootApplication
public class GoodsApplication {

    public static void main(String[] args) {
	
	
	Logger logger = LoggerFactory.getLogger(GoodsApplication.class);
	ConfigurableApplicationContext context=new SpringApplicationBuilder(GoodsApplication.class).run(args);
	logger.warn("seckill-goods启动成功，端口是"+context.getEnvironment().getProperty("server.port"));
    }
    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }
}
