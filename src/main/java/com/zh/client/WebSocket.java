package com.zh.client;

import com.alibaba.fastjson2.JSONObject;
import com.zh.factory.TankFactory;
import com.zh.model.Tank;
import com.zh.model.UserContainer;
import jakarta.websocket.*;
import jakarta.websocket.server.*;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName CoordinateGenerator
 * @Description socket消息生成类及管理类
 * @Author @zhangh
 * @Date 2023/11/9 21:16
 * @Version 1.1
 * @update  1.1 增加“开始游戏”的启动代码
 */
@Component
@Slf4j
@ServerEndpoint("/websocket/{username}")
@Getter
public class WebSocket {

    private static final CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();
    private static final ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();
    private static final AtomicInteger readyUserCount = new AtomicInteger(0);
    // 新增等待队列
    private static final Queue<WebSocket> waitingQueue = new LinkedBlockingQueue<>();
    // 可能会有多个UserContainer实例（即对战人数达到10以上，每局对战5人的情况下）
    // 我只是举个栗子，打个标记在这里，后续不想要可以删了[doge]
    private Map<String, UserContainer> containerMap = new HashMap<>();
    private TankFactory tankFactory;    // 坦克工厂类
    private Session session;
    private String username;
    private ApplicationContext applicationContext;  // 应用上下文类

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setTankFactory(TankFactory tankFactory) {
        this.tankFactory = tankFactory;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        this.session = session;
        this.username = username;
        webSockets.add(this);
        sessionPool.put(username, session);
        log.info("[WebSocket 消息] 有新的连接，总数为: {}", webSockets.size());
    }

    @OnClose
    public void onClose() {
        webSockets.remove(this);
        sessionPool.remove(username);
        waitingQueue.remove(this);
        log.info("[Websocket 消息] 连接断开，总数为: {}", webSockets.size());
    }

    /**
     * 期望的传输格式
     * {
     *     "action": 1, // 0:准备就绪，即将开始比赛   1:移动  2:发射子弹
     * }
     **/
    @OnMessage
    public void onMessage(String message) {
        log.info("[Websocket 消息] 收到客户端消息: {}", message);

        // 将消息转成 JSON 对象
        JSONObject jsonMessage = JSONObject.parseObject(message);

        switch (jsonMessage.getIntValue("action")) {
            case 0:
                // 处理准备就绪的逻辑
                log.info("ID:{}\t准备就绪，即将开始比赛", this.username);
                // 进行计数
                int readyCount = readyUserCount.incrementAndGet();
                log.info("加入第{}号池", readyCount % 5);

                // 将当前 WebSocket 加入等待队列
                waitingQueue.add(this);

                if (waitingQueue.size() >= 5) {
                    // 当等待队列大小达到5时，处理等待者
                    handleWaitingQueue();
                    readyUserCount.set(readyUserCount.get() - 5);   // 减5，继续计数
                }
                break;

            case 1:
                // 处理移动的逻辑
                log.info("ID:{}\t移动", this.username);
                break;

            case 2:
                // 处理发射子弹的逻辑
                log.info("发射子弹");
                break;

            default:
                // 处理未知动作的逻辑
//                log.warn("未知的动作: {}", action);
                break;
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误，原因： {}", error.getMessage());
    }

    public void sendAllMessage(String message) {
        log.info("[websocket消息] 广播消息: {}", message);
        for (WebSocket webSocket : webSockets) {
            sendMessage(webSocket.session, message);
        }
    }

    public void sendOneMessage(String username, String message) {
        Session session = sessionPool.get(username);
        if (session != null && session.isOpen()) {
            log.info("[websocket消息] 单点消息: {}", message);
            sendMessage(session, message);
        }
    }

    public void sendMoreMessage(String[] usernames, String message) {
        for (String username : usernames) {
            Session session = sessionPool.get(username);
            if (session != null && session.isOpen()) {
                log.info("[websocket消息] 单点消息: {}", message);
                sendMessage(session, message);
            }
        }
    }

    private void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("Error sending message to session: {}", e.getMessage());
        }
    }

    private void handleWaitingQueue() {
        // 处理等待队列中的 WebSocket 实例，移入 Container 容器中
        // 这里可以实现具体的逻辑，例如创建与等待者们相等username的Tank类
        log.info("处理等待队列，移入 Container 容器");

        UserContainer container = new UserContainer();
        for (WebSocket webSocket : waitingQueue) {
            Tank tank = tankFactory.createTank(webSocket.getUsername());
            container.addTank(tank);

            // 这里将 container 存储起来，供后续使用
            containerMap.put(webSocket.getUsername(), container);
        }

        // container.getTanks() 返回一个包含五个 Tank 实例的 Set
//        container.getTanks().forEach(tank -> {
//            // 这里可以对每个 Tank 进行一些操作，例如设置坐标等
//        });

        // 这里可以对 containerMap 进行进一步的操作，或者将其存储在其他地方

        waitingQueue.clear();
    }

    public List<String> getUsernames() {
        return List.copyOf(sessionPool.keySet());
    }

    @Bean
    public Map<String, UserContainer> getContainerMap() {
        return this.containerMap;
    }
}
