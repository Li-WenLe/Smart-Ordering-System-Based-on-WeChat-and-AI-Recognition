package com.wxprogrem.controller.common;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/chat")
@Component
public class ChatEndpoint {

    private static final CopyOnWriteArraySet<Session> sessions = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        try {
            session.getBasicRemote().sendText("通知服务连接成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("收到消息: " + message);

        // 广播消息给所有客户端
        for (Session s : sessions) {
            try {
                s.getBasicRemote().sendText(message);  // 原样返回消息
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);  // 连接关闭时移除
    }
}
