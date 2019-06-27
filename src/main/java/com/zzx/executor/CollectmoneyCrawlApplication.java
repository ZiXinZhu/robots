package com.zzx.executor;

import com.zzx.executor.config.LifeCycleConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


@SpringBootApplication
public class CollectmoneyCrawlApplication {


    public static void main(String[] args) {
        System.setProperty("java.awt.headless","false");

        new AnnotationConfigApplicationContext(LifeCycleConfiguration.class);
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
