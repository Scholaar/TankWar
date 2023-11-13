package com.zh.game;

import com.zh.factory.TankFactory;
import com.zh.game.GamePool;
import com.zh.model.Bullet;
import com.zh.model.CoordinateGenerator;
import com.zh.model.UserContainer;
import com.zh.model.Tank;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class GamePoolTest {
    @MockBean
    private ServerEndpointExporter serverEndpointExporter;

    @Autowired
    private GamePool gamePool;

    @Autowired
    private CoordinateGenerator coordinateGenerator;

    @Autowired
    private TankFactory tankFactory;

    @Test
    void testCheckTankBulletCollision() {
        // 创建坦克1
        Tank tank1 = tankFactory.createTank("player1");
        tank1.setX(10);
        tank1.setY(10);

        // 创建坦克2
        Tank tank2 = tankFactory.createTank("player2");
        tank2.setX(20);
        tank2.setY(20);

        // 将坦克添加到坦克池
        UserContainer tankContainer = new UserContainer();
        tankContainer.addTank(tank1);
        tankContainer.addTank(tank2);
        gamePool.createPool(Map.of("player1", tankContainer));

        // 创建子弹，让其坐标与坦克的坐标相同
        Bullet bullet1 = tank1.fire(10, 1); // 初始位置与坦克相同
        Bullet bullet2 = tank2.fire(10, 1); // 初始位置与坦克相同

        // 将子弹添加到GamePool中
        gamePool.addBullet(bullet1);
        gamePool.addBullet(bullet2);

        // 调用checkTankBulletCollision方法，检测碰撞
        gamePool.checkTankBulletCollision(bullet1);
        gamePool.checkTankBulletCollision(bullet2);

        // 验证坦克是否被移除
        System.out.println("Tanks after collision: " + tankContainer.getTanks());
        assertTrue(tankContainer.getTanks().isEmpty(), "Tank should be removed");

        // 验证子弹是否被移除
        System.out.println("Bullets after collision: " + gamePool.getGamePool());
        assertTrue(gamePool.getGamePool().isEmpty(), "Bullet should be removed");
    }
    @Test
    void testGenerateWalls() {
        // 调用generateWalls方法生成墙体坐标
        gamePool.generateWalls();

        // 获取生成的墙体坐标集合
        Set<Coordinate> walls = gamePool.getWalls();

        // 验证生成的墙体坐标集合是否符合预期
        System.out.println("Generated walls: " + walls);
        assertEquals(10, walls.size(), "Number of generated walls should be 10");
    }
}
