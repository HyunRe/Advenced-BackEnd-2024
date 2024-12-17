package com.lion.demo.websocket;


import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class EchoWebSocketHandler extends TextWebSocketHandler {
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    // connect start
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println();
        sessions.add(session);
    }

    // connect 설정
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received message: " + payload);

        // 모든 클라이언트에게 메세지를 전송
        for (WebSocketSession s: sessions) {
            if (s.isOpen())
                s.sendMessage(new TextMessage("Echo: " + payload));
        }
    }

    // connect end
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }
}
