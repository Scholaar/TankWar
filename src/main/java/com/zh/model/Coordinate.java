package com.zh.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @ClassName Coordinate
 * @Description TODO
 * @Author @O_o
 * @Date 2023/10/27 16:23
 * @Version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@Component
@Scope("prototype")
public class Coordinate {
    private int x;
    private int y;

    public Coordinate() {
        this.x = 0;
        this.y = 0;
    }

    @Override
    public Coordinate clone() throws CloneNotSupportedException {
        return (Coordinate) super.clone();
//        return new Coordinate(this.x, this.y);
    }
}
