package com.joy;

import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableFileStorage
@EnableCaching
@MapperScan("com.joy.mapper")
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
        System.out.println("系统启动成功！");
    }

}
