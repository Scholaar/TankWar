package com.zh.game;

import com.zh.model.Bullet;
import com.zh.model.Coordinate;

import java.util.HashSet;
import java.util.Set;

/**
 *@ClassName GamePool
 *@Description  游戏池类，装载Bullet类实例，自动运行
 *@Author @zhangh
 *@Date 2023/11/7 22:32
 *@Version 1.0
 */
public class GamePool {

    private static Set<Bullet> gamePool;

    private Coordinate bounds = new Coordinate(50, 50);  // 边界，左边界、下边界默认为0，bounds里的x表示右边界，y表示上边界

    /**
     * @author @zhangh
     * @Description 调用此方法则创建一个新池
     * @Date 22:37 2023/11/7
     * @return void
     **/
    public void createPool() {
        gamePool = new HashSet<>();
    }

    public void addBullet(Bullet bullet) {
        gamePool.add(bullet);
    }

    public void run() {
        while (true) {      // 1、什么时候结束大循环  2、碰撞检测子弹与坦克     3、子弹与墙（障碍物坐标初始化）
             for (Bullet bullet : gamePool) {
                switch (bullet.getDirect()) {   // 坐标更改
                    case 0:
                        bullet.setY(bullet.getY() + bullet.getSpeed());
                        break;
                    case 1:
                        bullet.setY(bullet.getY() - bullet.getSpeed());
                        break;
                    case 2:
                        bullet.setX(bullet.getX() - bullet.getSpeed());
                        break;
                    case 3:
                        bullet.setX(bullet.getX() + bullet.getSpeed());
                        break;
                }

//                if (bullet.getX()) {
                 // 是否脱离地图
            }
        }
    }
}
g