package com.seckill.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;


import brave.sampler.Sampler;

//@EnableCaching
@EnableEurekaClient
@SpringBootApplication
public class CacheApplication {

    public static void main(String[] args) {
	
	
	Logger logger = LoggerFactory.getLogger(CacheApplication.class);
	ConfigurableApplicationContext context=new SpringApplicationBuilder(CacheApplication.class).run(args);
	logger.warn("seckill-cache启动成功，端口是"+context.getEnvironment().getProperty("server.port"));
    }
    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }
}
