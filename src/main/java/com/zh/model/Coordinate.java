package com.zh.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @ClassName Coordinate
 * @Description 子弹类
 * @Author @zhangh
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

    /**
     * @author @zhangh
     * @Description 哈希算法，用来供以哈希表判断坐标是否相同（这将调用坐标类的哈希算法）
     * @Date 18:06 2023/11/7
     * @return int
     **/
    @Override
    public int hashCode() {
        // 自定义哈希算法
        int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    /**
     * @author @zhangh
     * @Description  等值方法，用以确保坐标相等的两个类，在被哈希表的contains()判断时，会返回true
     * @Date 18:07 2023/11/7
     * @param obj
     * @return boolean
     **/
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coordinate other = (Coordinate) obj;
        return x == other.x && y == other.y;
    }
}
