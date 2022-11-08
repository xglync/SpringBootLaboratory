package org.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
@EnableTransactionManagement
@SpringBootApplication //SpringBoot 应用入口类注解
@RestController //控制器类，接收web请求
@MapperScan("org.example.mapper") //扫描mapper接口
public class App
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        SpringApplication.run(App.class);//启动入门应用类app

    }
    @GetMapping("/hello")
    public String hello(){
        return "hello spring boot. 王佳宝，已完成";
    }
}
