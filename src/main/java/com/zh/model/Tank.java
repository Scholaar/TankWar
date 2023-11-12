package com.zh.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author @zhangh
 * @Description 坦克属性类
 * @Date 14:51 2023/10/27
 **/
@Getter
@Setter
public class Tank extends Coordinate {
    private int direct = 0; // 坦克方向:  0为up, 1为down, 2为left, 3为right

    private int speed = 1;  // 坦克速度

    private String username;    // 坦克的玩家号

    public Tank(String username, CoordinateGenerator coordinateGenerator) {
        this.username = username;
        Coordinate uniqueCoordinate = coordinateGenerator.generateUniqueCoordinate();
        setX(uniqueCoordinate.getX());
        setY(uniqueCoordinate.getY());
    }

    public Bullet fire(int speed, int direct) {
        Bullet bullet = new Bullet();

        // 设置X, Y坐标值
        bullet.setX(this.getX());
        bullet.setY(this.getY());

        // 设置速度与方向
        bullet.setSpeed(speed);
        bullet.setDirect(direct);

        return bullet;
    }
}

