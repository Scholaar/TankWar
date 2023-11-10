package com.zh.game;

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

    @Test
    void testCheckTankBulletCollision() {
        // 创建坦克
        Tank tank = new Tank("player1");
        tank.setX(10);
        tank.setY(10);

        // 将坦克添加到坦克池
        UserContainer tankContainer = new UserContainer();
        tankContainer.addTank(tank);
        gamePool.createPool(Map.of("player1", tankContainer));

        // 创建子弹，让其坐标与坦克的坐标相同
        Bullet bullet = tank.fire(10, 1); // 初始位置与坦克相同

        // 将子弹添加到GamePool中
        gamePool.addBullet(bullet);

        // 调用checkTankBulletCollision方法，检测碰撞
        gamePool.checkTankBulletCollision(bullet);

        // 验证坦克是否被移除
        assertTrue(tankContainer.getTanks().isEmpty(), "Tank should be removed");

        // 验证子弹是否被移除
        assertTrue(gamePool.getGamePool().isEmpty(), "Bullet should be removed");
    }
}

