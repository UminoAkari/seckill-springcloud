package com.seckill.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import brave.sampler.Sampler;




@EnableTransactionManagement
@EnableEurekaClient
@SpringBootApplication
public class OrderApplication {

    public static void main(String[] args) {
	Logger logger = LoggerFactory.getLogger(OrderApplication.class);
	ConfigurableApplicationContext context=new SpringApplicationBuilder(OrderApplication.class).run(args);
	logger.warn("seckill-order启动成功，端口是"+context.getEnvironment().getProperty("server.port"));
    }
    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }
}
