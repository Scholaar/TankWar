package com.zh.model;

import java.util.Set;
import java.util.HashSet;

/**
 * @ClassName UserContainer
 * @Description 坦克生成类，也是socket连接转Tank的类
 * @author @zhangh
 * @Date 2023/10/31 1:05
 * @version 1.0
 */
public class UserContainer {
    private final Set<Tank> tanks;

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

    public void removeTank(Tank tank) {
        tanks.remove(tank);
    }

    public Set<Tank> getTanks() {
        return tanks;
    }
}
