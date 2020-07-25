package com.seckill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import brave.sampler.Sampler;
import cn.hutool.core.util.NetUtil;


@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class FeignApplication {
   

    public static void main(String[] args) {
	
	Logger logger = LoggerFactory.getLogger(FeignApplication.class);
	
	
	
	ConfigurableApplicationContext context=new SpringApplicationBuilder(FeignApplication.class).run(args);
	logger.warn("seckill-view启动成功，端口是"+context.getEnvironment().getProperty("server.port"));
    }
    
    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }  

}
