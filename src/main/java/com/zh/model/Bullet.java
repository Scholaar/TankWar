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

}
