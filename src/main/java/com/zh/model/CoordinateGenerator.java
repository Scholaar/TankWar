package com.zh.model;

/**
 * @ClassName CoordinateGenerator
 * @Description 随机的、不重复的坐标--生成类
 * @Author @zhangh
 * @Date 2023/11/9 21:16
 * @Version 1.0
 */
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Component
public class CoordinateGenerator {

    private static final int MAX_X = 100; // 最大 X 坐标
    private static final int MAX_Y = 100; // 最大 Y 坐标

    private Set<Coordinate> usedCoordinates;
    private Random random;

    public CoordinateGenerator() {
        this.usedCoordinates = new HashSet<>();
        this.random = new Random();
    }

    public Coordinate generateUniqueCoordinate() {
        int maxAttempts = 1000; // 最大尝试次数，避免无限循环

        for (int i = 0; i < maxAttempts; i++) {
            int x = random.nextInt(MAX_X);
            int y = random.nextInt(MAX_Y);

            Coordinate newCoordinate = new Coordinate(x, y);

            if (!usedCoordinates.contains(newCoordinate)) {
                usedCoordinates.add(newCoordinate);
                return newCoordinate;
            }
        }

        throw new IllegalStateException("Unable to generate unique coordinate after " + maxAttempts + " attempts.");
    }
}

