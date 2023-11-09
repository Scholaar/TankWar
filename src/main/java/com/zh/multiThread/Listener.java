package com.zh.multiThread;

import com.zh.client.WebSocket;
import com.zh.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @ClassName Listener
 * @Description 监听器类
 * @Author @zhangh
 * @Date 2023/10/30 22:20
 * @Version 1.0
 */
@Component
@Slf4j
public class Listener {
    @DubboReference // 用于在消费者端声明对Dubbo服务提供者的引用
    private GameService gameService;

    @Autowired
    private WebSocket webSocket;

    private final int user_num = 5;

    /**
     * 匹配用户
     *
     */
//    @Async("taskExecutor")
    @Async
    public void matchUser() {
        // 1.获取待匹配用户
        // 2.判断用户人数是否满足匹配条件 满足后进行下一步 不满足重新循环
        // 3.随机选取一名用户 调用匹配方法 匹配
        // 4.匹配成功后继续循环至步骤1
        while (true) {
            List<String> userIds = webSocket.getUsernames();
            if (CollectionUtils.isEmpty(userIds)) {
                if (userIds.size() < user_num) {
                    continue;
                } else {
                    // 匹配用户
                    Random random = new Random();
                }
            }
        }
    }
}
