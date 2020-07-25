package com.seckill.eureka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ConfigurableApplicationContext;


 
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {
     /*
      * 
      * http://127.0.0.1:8761/
      * java -jar zipkin-server-2.10.1-exec.jar
      */
    public static void main(String[] args) {
	Logger logger = LoggerFactory.getLogger(EurekaApplication.class);
	

	ConfigurableApplicationContext context=new SpringApplicationBuilder(EurekaApplication.class).run(args);
	logger.warn("seckill-eureka启动成功，端口是"+context.getEnvironment().getProperty("server.port"));
    }
}
