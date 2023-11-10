package com.zh;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@ComponentScan(basePackages = {"com.zh.controller", "com.zh.service", "com.zh.config", "com.zh.client", "com.zh.game", "com.zh.model"})
@SpringBootApplication
@RestController
public class WebController {

//    @RequestMapping(value ="/home", method = RequestMethod.GET)
    @GetMapping("/home")
//    @ResponseBody
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