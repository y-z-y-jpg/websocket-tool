package com.example.websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ServerEndpoint("/websocket")
public class WebSocketEndpoint {

    private ObjectMapper objectMapper = new ObjectMapper();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("New connection: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            // 接收 JSON 消息并转换为对象
            MyMessage myMessage = objectMapper.readValue(message, MyMessage.class);
            System.out.println("Received message: " + myMessage);

            // 发送回显 JSON 消息
            String response = objectMapper.writeValueAsString(myMessage);
            session.getBasicRemote().sendText(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Closed: " + session.getId() + " with reason: " + closeReason);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Error on session " + session.getId() + ": " + throwable.getMessage());
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



