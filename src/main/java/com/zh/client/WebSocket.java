package com.zh.client;

import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName WebSocket
 * @author @zhangh
 * @comment ws操作类
 * @date 2023/10/30 14:27
 * @version 1.0
 */
@Component
@Slf4j
@ServerEndpoint("/websocket/{userId}")  // 接口路径：ws://localhost:8080/websocket/userId
@Getter
@Setter
@EnableAsync
@Scope("prototype") // 为什么加这个注解？详见"https://blog.csdn.net/weixin_46058733/article/details/128496652"
public class WebSocket {
    // 与某个客户端的连接对话，需要凭此对客户端传输数据
    private Session session;

    private String userId;

    private AtomicInteger readyUserCount = new AtomicInteger(0);

    @Autowired
    private ApplicationContext applicationContext;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    //虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，所以可以用一个静态set保存起来。
    //  注：底下WebSocket是当前类名
    private static CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();

    // 用来存在线连接用户信息
    private static ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();

    /**
     * 链接成功调用的方法
     */
    @OnOpen
    @Async("taskExecutor")  // 使用自定义线程池
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        try {
            this.session = session;
            this.userId = userId;
            webSockets.add(this);
            sessionPool.put(userId, session);
            log.info("[WebSocket 消息] 有新的连接，总数为:" + webSockets.size());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 链接关闭调用的方法
     */
    @OnClose
    @Async("taskExecutor")
    public void onClose() {
        try {
            webSockets.remove(this);
            sessionPool.remove(this.userId);
            log.info("[Websocket 消息] 连接断开，总数为:" + webSockets.size());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     */
    @OnMessage
    @Async("taskExecutor")
    public void onMessage(String message) {
        log.info("[Websocket 消息] 收到客户端消息:" + message);
        if ("isReady".equals(message)) {
            int readyCount = readyUserCount.incrementAndGet();
            System.out.println("加入第" + (readyCount % 5) + "号池");

            /*
            if (readyCount % 5 == 0) {
                // 当有 5 个就绪态用户时，将它们加入 Container
                containers.get(containers.size() - 1).addUsers(getReadyUsers(5));
            }
             */
        }
    }

    /** 发送错误时的处理
     * @param session
     * @param error
     */
    @OnError
    @Async("taskExecutor")
    public void onError(Session session, Throwable error) {
        log.error("用户错误，原因：" + error.getMessage());
        error.printStackTrace();
    }

    // 此为广播消息
    public void sendAllMessage(String message) {
        log.info("[websocket消息] 广播消息:"+message);
        for (WebSocket webSocket : webSockets) {
            try {
                if (webSocket.session.isOpen()) {
                    webSocket.session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息
    public void sendOneMessage(String userId, String message) {
        Session session = sessionPool.get(userId);
        if (session != null && session.isOpen()) {
            try {
                log.info("[websocket消息] 单点消息:"+message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息(多人)
    public void sendMoreMessage(String[] userIds, String message) {
        for (String userId : userIds) {
            Session session = sessionPool.get(userId);

            if (session != null && session.isOpen()) {
                try {
                    log.info("[websocket消息] 单点消息:"+message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @author @zhangh
     * @Description 获取在线用户ID
     * @Date 22:46 2023/10/30
     */
    public List<String> getUserIds() {
        List<String> keyList = new ArrayList<>();
        sessionPool.forEachKey(Long.MAX_VALUE, keyList::add);   // 将enumeration类型转成List类型

        return keyList;
    }

}
