package com.wxprogrem.service;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket")
@Component
public class WebSocketServer {

    // 存放每个客户端对应的WebSocketServer对象
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    // 连接建立成功调用的方法
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        System.out.println("有新的连接，当前连接数：" + webSocketSet.size());
    }

    // 连接关闭调用的方法
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        System.out.println("有连接关闭，当前连接数：" + webSocketSet.size());
    }

    // 收到客户端消息后调用的方法
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("收到来自客户端的消息：" + message);
    }

    // 发生错误时调用的方法
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误：" + error.getMessage());
        error.printStackTrace();
    }

    // 向所有客户端发送消息
    public static void sendInfo(String message) throws IOException {
        for (WebSocketServer item : webSocketSet) {
            item.session.getBasicRemote().sendText(message);
        }
    }
}