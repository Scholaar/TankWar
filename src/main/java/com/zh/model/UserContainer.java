package com.zh.model;

import lombok.Getter;
import java.util.Set;
import java.util.HashSet;

/**
 * @ClassName UserContainer
 * @Description 坦克生成类，也是socket连接转Tank的类
 * @author @zhangh
 * @Date 2023/10/31 1:05
 * @version 1.0
 */
@Getter
public class UserContainer {
    private final Set<Coordinate> tanks;

    public UserContainer() {
        this.tanks = new HashSet<>();
    }

    public void addTank(Tank tank) {
        if (tanks.size() < 5) {
            tanks.add(tank);
        } else {
            // 处理容器已满的逻辑，可以抛出异常或执行其他操作
            System.out.println("Container is full. Cannot add more tanks.");
        }
    }

    /**
     * TODO 坦克移动方法，待实现
     */
    public void moveTank(Tank tank,String direction) {
        switch (direction) {
            case "up":
                tank.setY(tank.getY() + tank.getSpeed());
                break;
            case "down":
                tank.setY(tank.getY() - tank.getSpeed());
                break;
            case "left":
                tank.setX(tank.getX() - tank.getSpeed());
                break;
            case "right":
                tank.setX(tank.getX() + tank.getSpeed());
                break;
            default:
                break;
        }
    }

    public void removeTank(Tank tank) {
        tanks.remove(tank);
    }   // 移除坦克
}
