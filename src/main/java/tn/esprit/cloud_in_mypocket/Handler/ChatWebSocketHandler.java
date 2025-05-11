package tn.esprit.cloud_in_mypocket.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tn.esprit.cloud_in_mypocket.entity.ChatMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("New WebSocket connection established: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received message: " + payload);

        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);

        // Handle different message types
        if ("IDENTIFICATION".equals(chatMessage.getType())) {
            // Store the session with the user ID
            sessions.put(chatMessage.getSenderId(), session);
            System.out.println("User identified: " + chatMessage.getSenderId());
        } else if ("CHAT".equals(chatMessage.getType())) {
            // Broadcast the message to the specific receiver
            WebSocketSession receiverSession = sessions.get(chatMessage.getReceiverId());
            if (receiverSession != null && receiverSession.isOpen()) {
                System.out.println("Sending message to receiver: " + chatMessage.getReceiverId());
                receiverSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
            }

            // Also send the message back to the sender for confirmation
            if (session.isOpen()) {
                System.out.println("Sending message back to sender: " + chatMessage.getSenderId());
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        // Remove the session when connection is closed
        sessions.entrySet().removeIf(entry -> entry.getValue().equals(session));
        System.out.println("WebSocket connection closed: " + session.getId());
    }
}