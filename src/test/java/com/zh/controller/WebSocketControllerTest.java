package com.zh.controller;


import com.alibaba.fastjson.JSONObject;
import com.zh.client.WebSocket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ComponentScan("com.zh.client")
class WebSocketControllerTest {
    @Autowired
    private WebSocket webSocket;

    @Test
    public void testWebSocketOptions() {
        // 创建业务消息信息
        JSONObject obj = new JSONObject();
        obj.put("cmd", "topic");// 业务类型
        obj.put("msgId", "yourMsgId");// 消息id
        obj.put("msgTxt", "Your Message Content");// 消息内容

        // 全体发送
        webSocket.sendAllMessage(obj.toJSONString());

        // 单个用户发送 (userId为用户id)
        String userId = "someUserId";
        webSocket.sendOneMessage(userId, obj.toJSONString());

        // 多个用户发送 (userIds为多个用户id，逗号‘,’分隔)
        String[] userIds = {"user1", "user2", "user3"};
        webSocket.sendMoreMessage(userIds, obj.toJSONString());
    }
}