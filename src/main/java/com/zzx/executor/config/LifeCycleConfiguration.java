package com.zzx.executor.config;


import com.zzx.executor.util.RobotStudy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@ComponentScan(value = "com.zzx.executor")
public class LifeCycleConfiguration {
    @Autowired
    DataSourceProperties properties;
    @Autowired
    RobotStudy study;
    @Autowired
    private ExecutorService threadPool;

    @Bean
    public RestTemplate restTemplate(){

        //TODO 生产环境数据库
        properties.setUrl("jdbc:mysql://rm-wz930kd8byhie319iao.mysql.rds.aliyuncs.com:3306/liansen_bank?serverTimezone=GMT%2B8&characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&autoReconnect=true");
        properties.setUsername("boot");
        properties.setPassword("liansen@2019618");

        //TODO mysql驱动
        properties.setDriverClassName("com.mysql.cj.jdbc.Driver");

        //TODO Other Properties
        return new RestTemplate();
    }


    @Bean
    public ExecutorService getThreadPool(){
        return Executors.newFixedThreadPool(5);
    }

    @Bean
    @Order(2111111111)
    public void initial() {
        threadPool.submit(study);

    }
}
