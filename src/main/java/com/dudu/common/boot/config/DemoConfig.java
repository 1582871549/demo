package com.dudu.common.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 根据不同环境, 初始化的时候对用户注入不同的参数
 */
@Configuration
public class DemoConfig {

    @Bean
    @Profile("dev")
    public DemoBean devDemoBean() {
        System.out.println("开发环境demobean             : " + this.getClass().getName() + "."
                + Thread.currentThread().getStackTrace()[1].getMethodName());
        return new DemoBean("from dev profile");
    }

    @Bean
    @Profile("prod")
    public DemoBean prodDemoBean() {
        System.out.println("正式环境demobean             : " + this.getClass().getName() + "."
                + Thread.currentThread().getStackTrace()[1].getMethodName());
        return new DemoBean("from prod profile");
    }
}
