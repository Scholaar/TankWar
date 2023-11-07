package com.zh.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.*;

/**
 * @Author @zhangh
 * @Description 坦克属性类
 * @Date 14:51 2023/10/27
 **/
@Getter
@Setter
@Component
@Scope("prototype")     // 配置为原型作用域的Bean, 这意味着每次请求 Tank Bean 时都会创建一个新的实例。
public class Tank extends Coordinate {
//    @Autowired
//    private Coordinate coordinate;  // 坦克坐标

    @Autowired
    private ApplicationContext applicationContext;

    private int direct = 0; // 坦克方向:  0为up, 1为down, 2为left, 3为right

    private int speed = 0;  // 坦克速度

    private Color color = Color.red;    // 坦克颜色

    private int uid;    // 坦克的玩家uid号

    public Tank(int uid) {
        this.uid = uid;
    }

    public Bullet fire(int speed, int direct) throws CloneNotSupportedException {
        Bullet bullet = applicationContext.getBean(Bullet.class, this.clone());
        bullet.setSpeed(speed);
        bullet.setDirect(direct);

        return bullet;
    }
}

