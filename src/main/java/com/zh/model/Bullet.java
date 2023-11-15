package com.zh.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @ClassName Bullet
 * @Description TODO
 * @Author @zhangh
 * @Date 2023/10/27 16:21
 * @Version 1.0
 */
@Getter
@Setter
public class Bullet extends Coordinate {
    private int speed;
    private int direct;

    public Bullet() {
        this.speed = 0;
        this.direct = 0;
    }

    // 由于坦克是一个矩形（我们这里假设坦克是10*10，而子弹就是1*1），所以子弹的坐标就是实际的坐标，而坦克的坐标是中心点的坐标
    // 在这里就是判断子弹是否进入矩形区域，是则返回true，否则返回false
    public boolean checkCollision(Tank tank) {
        return this.getX() < tank.getX() + tank.getWidth() &&
                this.getX() + tank.getWidth() > tank.getX() &&
                this.getY() < tank.getY() + tank.getHeight() &&
                this.getY() + tank.getHeight() > tank.getY();
    }

}

