package com.zh.game;

import com.zh.model.Bullet;
import com.zh.model.Coordinate;
import com.zh.model.UserContainer;
import com.zh.model.Tank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName GamePool
 * @Description  游戏池类，装载Bullet类实例，自动运行
 * @Author @zhangh
 * @Date 2023/11/7 22:32
 * @Version 1.0
 */
@Component
public class GamePool {

    private static Set<Bullet> gamePool;   // 存储子弹的池

    private Set<Coordinate> walls;  // 存储墙体的坐标, 墙体坐标初始化

    // 这是一个多坦克池，存储形式为<K, V>，键为username，值为对应的坦克池
    // 含义为：每个username在xxx坦克池中，坦克池即可具象为一局游戏的容器，这个容器专门存储坦克
    private Map<String, UserContainer> tankPool;    // 存储坦克的池

    private Coordinate bounds = new Coordinate(50, 50);  // 边界，左边界、下边界默认为0，bounds里的x表示右边界，y表示上边界

    /**
     * @Author @zhangh
     * @Description 调用此方法则创建一个新池
     * @Date 22:37 2023/11/7
     * @return void
     **/
    @Autowired
    public void createPool(Map<String, UserContainer> tankPool) {    // 创建池
        gamePool = new HashSet<>();   // 初始化子弹池
        this.tankPool = tankPool;   // 初始化坦克池
        this.walls = new HashSet<>();  // 初始化墙体坐标集合
    }
    public void addBullet(Bullet bullet) {     // 添加子弹
        gamePool.add(bullet);
    }

    public void addWall(Coordinate wallCoordinate) {    // 添加墙体坐标
        walls.add(wallCoordinate);
    }

    public void run() {
        // 要解决的问题： 1、什么时候结束大循环（场上只剩一辆坦克）
        //              2、碰撞检测子弹与坦克（哈希表中子弹与坦克的位置重合）
        //              3、子弹与墙（障碍物坐标初始化）
        while (true) {
            for (Bullet bullet : gamePool) {
                switch (bullet.getDirect()) {
                    case 0:
                        bullet.setY(bullet.getY() + bullet.getSpeed());   // 子弹向上移动
                        break;
                    case 1:
                        bullet.setY(bullet.getY() - bullet.getSpeed());   // 子弹向下移动
                        break;
                    case 2:
                        bullet.setX(bullet.getX() - bullet.getSpeed());   // 子弹向左移动
                        break;
                    case 3:
                        bullet.setX(bullet.getX() + bullet.getSpeed());   // 子弹向右移动
                        break;
                }

                // 子弹是否脱离地图的逻辑
                if (bullet.getX() < 0 || bullet.getX() >= bounds.getX() || bullet.getY() < 0 || bullet.getY() >= bounds.getY()) {
                    gamePool.remove(bullet);
                    continue;
                }

                // 进行坦克和子弹的碰撞检测
                checkTankBulletCollision(bullet);

                // 当子弹和墙碰撞时，子弹消失
                checkWallBulletCollision(bullet);

                // 当只剩一辆坦克时，游戏结束
                if (tankPool.size() == 1) {
                    return;
                }
            }
        }
    }

    // 检测子弹和坦克的碰撞,将子弹和坦克的坐标通过Coordinate中的hashCode计算，将计算后的数据存在在哈希表中，当发现哈希表中存在相同的数据时，即为碰撞
    public void checkTankBulletCollision(Bullet bullet) {
        // 获取子弹的坐标
        Coordinate bulletCoordinate = new Coordinate(bullet.getX(), bullet.getY());

        // 遍历所有坦克池
        for (UserContainer tankContainer : tankPool.values()) {
            // 遍历当前坦克池中的所有坦克
            for (Tank tank : tankContainer.getTanks()) {
                // 获取当前坦克的坐标
                Coordinate tankCoordinate = new Coordinate(tank.getX(), tank.getY());

                // 检查子弹和坦克是否发生碰撞
                if (bulletCoordinate.equals(tankCoordinate)) {
                    // 1. 移除坦克
                    tankContainer.removeTank(tank);
                    // 2. 移除子弹
                    gamePool.remove(bullet);
                }
            }
        }
    }


    // 检测子弹和墙的碰撞
    public void checkWallBulletCollision(Bullet bullet) {
        // 获取子弹的坐标
        Coordinate bulletCoordinate = new Coordinate(bullet.getX(), bullet.getY());
        if (walls.contains(bulletCoordinate)) {
            // 子弹与墙体碰撞逻辑
            gamePool.remove(bullet);
        }
    }

    public Set<Bullet> getGamePool() {    // 获取子弹池
        return gamePool;
    }
}
