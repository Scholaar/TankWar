package com.zh;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

@ComponentScan(basePackages = {"com.zh.controller","com.zh.service","com.zh.client","com.zh.factory","com.zh.model","com.zh.game"})
@SpringBootApplication
@RestController
public class WebController {

    @GetMapping("/home")
    public String home(){
        System.out.println("触发一次");
        return "你好，Spring Boot";
    }

    public static void main(String[] args){
        SpringApplication application = new SpringApplication(WebController.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }
}