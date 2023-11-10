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
    @Autowired
    private ApplicationContext applicationContext;

    private int direct = 0; // 坦克方向:  0为up, 1为down, 2为left, 3为right

    private int speed = 1;  // 坦克速度

//    private Color color = Color.red;    // 坦克颜色

    private String username;    // 坦克的玩家号

    @Autowired
    public Tank(String username, CoordinateGenerator coordinateGenerator) {
        this.username = username;
        Coordinate uniqueCoordinate = coordinateGenerator.generateUniqueCoordinate();
        setX(uniqueCoordinate.getX());
        setY(uniqueCoordinate.getY());
    }

    public Bullet fire(int speed, int direct) {    // 坦克开火
        Bullet bullet = applicationContext.getBean(Bullet.class);    // 从容器中获取子弹实例

        // 设置X, Y坐标值
        bullet.setX(this.getX());
        bullet.setY(this.getY());

        // 设置速度与方向
        bullet.setSpeed(speed);
        bullet.setDirect(direct);

        return bullet;
    }
}

