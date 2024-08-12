package com.example.websocket;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import com.fasterxml.jackson.databind.ObjectMapper;

@ClientEndpoint
public class WebSocketClient {

    private Session session;
    private ObjectMapper objectMapper = new ObjectMapper();

    public void connect(String uri) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(uri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("Connected to endpoint: " + session.getBasicRemote());
    }

    @OnMessage
    public void onMessage(String message) {
        try {
            // 接收 JSON 消息并转换为对象
            MyMessage myMessage = objectMapper.readValue(message, MyMessage.class);
            System.out.println("Received message: " + myMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(MyMessage myMessage) {
        try {
            // 将对象转换为 JSON 消息发送
            String message = objectMapper.writeValueAsString(myMessage);
            this.session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Closed with reason: " + closeReason);
    }

    // 定义一个内部类表示消息
    public static class MyMessage {
        public String type;
        public String content;

        @Override
        public String toString() {
            return "MyMessage{type='" + type + "', content='" + content + "'}";
        }
    }
}

