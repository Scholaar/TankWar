package com.zh.factory;

import com.zh.model.CoordinateGenerator;
import com.zh.model.Tank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName TankFactory
 * @Description 坦克工厂类（该类用于解决Tank想要自动装配某些属性，但又不想直接被注册的情况）
 * @Author @zhangh
 * @Date 2023/11/10 20:33
 * @Version 1.0
 */
@Component
public class TankFactory {
    private final CoordinateGenerator coordinateGenerator;

    @Autowired
    public TankFactory(CoordinateGenerator coordinateGenerator) {
        this.coordinateGenerator = coordinateGenerator;
    }

    public Tank createTank(String username) {
        return new Tank(username, coordinateGenerator);
    }
}
