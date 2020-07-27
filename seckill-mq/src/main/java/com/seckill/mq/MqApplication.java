package com.seckill.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import brave.sampler.Sampler;

@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class MqApplication {

    public static void main(String[] args) {
	Logger logger = LoggerFactory.getLogger(MqApplication.class);
	ConfigurableApplicationContext context=new SpringApplicationBuilder(MqApplication.class).run(args);
	logger.warn("seckill-mq启动成功，端口是"+context.getEnvironment().getProperty("server.port"));
    }
    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }
}
