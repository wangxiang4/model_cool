package com.cool.model_cool_main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = "com.cool", exclude = {SecurityAutoConfiguration.class})
public class ModelCoolMainApplication  extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ModelCoolMainApplication.class, args);
    }

    //打War包|在外置容器启动初始化入口
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ModelCoolMainApplication.class);
    }

}
