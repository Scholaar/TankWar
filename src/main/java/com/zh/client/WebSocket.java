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
 * WebSocket服务器端，处理Tank游戏中的实时通信
 */

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

    // 存储所有WebSocket实例，保证线程安全
    private static final CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();

    // 存储用户和其对应的WebSocket会话
    private static final ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();

    // 记录准备就绪的用户数量
    private static final AtomicInteger readyUserCount = new AtomicInteger(0);

    // 等待队列，用于存储等待进入游戏的WebSocket实例
    private static final Queue<WebSocket> waitingQueue = new LinkedBlockingQueue<>();

    // 用户和其对应的UserContainer实例的映射
    private Map<String, UserContainer> containerMap = new HashMap<>();

    // 坦克工厂类
    private TankFactory tankFactory;

    // WebSocket会话
    private Session session;

    // 用户名
    private String username;

    // 应用上下文类
    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setTankFactory(TankFactory tankFactory) {
        this.tankFactory = tankFactory;
    }

    /**
     * WebSocket连接建立时触发
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        this.session = session;
        this.username = username;
        webSockets.add(this);
        sessionPool.put(username, session);
        log.info("[WebSocket 消息] 有新的连接，总数为: {}", webSockets.size());
    }

    /**
     * WebSocket连接关闭时触发
     */
    @OnClose
    public void onClose() {
        webSockets.remove(this);
        sessionPool.remove(username);
        waitingQueue.remove(this);
        log.info("[Websocket 消息] 连接断开，总数为: {}", webSockets.size());
    }

    /**
     * 接收客户端消息时触发
     * 期望的传输格式:
     * {
     *     "action": 1, // 0:准备就绪，即将开始比赛   1:移动  2:发射子弹
     * }
     *
     * 前端返回的json内容
     * {
     *   "tankPosition": {
     *     "myTank": {
     *       "x": 100,
     *       "y": 300,
     *       "direction": "right"
     *     },
     *     "enemyTank": [
     *       // ... (其他敌方坦克的位置信息)
     *     ]
     *   },
     *   "bullets": [
     *     // ... (所有子弹的位置信息)
     *   ]
     * }
     */
    @OnMessage
    public void onMessage(String message) throws IOException {
        log.info("[Websocket 消息] 收到客户端消息: {}", message);

        // 将消息转成 JSON 对象
        JSONObject jsonMessage = JSONObject.parseObject(message);

        switch (jsonMessage.getIntValue("action")) {
//            case 0:
            case 1:
                // 处理移动的逻辑
                log.info("ID:{}\t移动", this.username);

                // 从jsonMessage中获取移动方向和坦克的名称，如direction = "up"
                String direction = jsonMessage.getString("direction");
                String tankName = jsonMessage.getString("tankName");
                // 坦克移动


                break;

            case 2:
                // 处理发射子弹的逻辑
                log.info("发射子弹");

                // 从jsonMessage中获取子弹方向，如direction = "up"
                String bulletDirection = jsonMessage.getString("direction");

                // 根据方向发射子弹
                break;

            default:
                // 处理准备就绪的逻辑
                log.info("ID:{}\t准备就绪，即将开始比赛", this.username);
                // 进行计数
                int readyCount = readyUserCount.incrementAndGet();
                log.info("加入第{}号池", readyCount % 5);

                // 将当前 WebSocket 加入等待队列
                waitingQueue.add(this);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("num", waitingQueue.size());
                String jsonString = jsonObject.toJSONString();
                for (WebSocket session : waitingQueue) {
                    session.session.getBasicRemote().sendText(jsonString);
                }

                if (waitingQueue.size() >= 5) {
                    // 当等待队列大小达到5时，处理等待者
                    handleWaitingQueue();
                    readyUserCount.set(readyUserCount.get() - 5);   // 减5，继续计数
                }
                break;
        }
    }

    /**
     * WebSocket错误时触发
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误，原因： {}", error.getMessage());
    }

    /**
     * 向所有连接的WebSocket实例广播消息
     */
    public void sendAllMessage(String message) {
        log.info("[websocket消息] 广播消息: {}", message);
        for (WebSocket webSocket : webSockets) {
            sendMessage(webSocket.session, message);
        }
    }

    /**
     * 向指定用户发送消息
     */
    public void sendOneMessage(String username, String message) {
        Session session = sessionPool.get(username);
        if (session != null && session.isOpen()) {
            log.info("[websocket消息] 单点消息: {}", message);
            sendMessage(session, message);
        }
    }

    /**
     * 向多个指定用户发送消息
     */
    public void sendMoreMessage(String[] usernames, String message) {
        for (String username : usernames) {
            Session session = sessionPool.get(username);
            if (session != null && session.isOpen()) {
                log.info("[websocket消息] 单点消息: {}", message);
                sendMessage(session, message);
            }
        }
    }

    /**
     * 发送消息给指定会话
     */
    private void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("Error sending message to session: {}", e.getMessage());
        }
    }

    /**
     * 处理等待队列中的WebSocket实例，将其移入UserContainer容器中，并进行一些具体的逻辑处理
     */
    private void handleWaitingQueue() {
        log.info("处理等待队列，移入 Container 容器");

        // 创建一个新的UserContainer实例
        UserContainer container = new UserContainer();

        // 遍历等待队列中的WebSocket实例，为每个用户创建坦克，并加入UserContainer
        for (WebSocket webSocket : waitingQueue) {
            Tank tank = tankFactory.createTank(webSocket.getUsername());
            container.addTank(tank);

            // 将 container 存储起来，供后续使用
            containerMap.put(webSocket.getUsername(), container);
        }

        // 清空等待队列
        waitingQueue.clear();
    }

    /**
     * 获取所有用户的用户名列表
     */
    public List<String> getUsernames() {
        return List.copyOf(sessionPool.keySet());
    }

    /**
     * 获取UserContainer实例的映射
     */
    @Bean
    public Map<String, UserContainer> getContainerMap() {
        return this.containerMap;
    }
}
