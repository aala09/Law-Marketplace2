package tn.esprit.cloud_in_mypocket.Config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;
import tn.esprit.cloud_in_mypocket.Handler.ChatWebSocketHandler;

@Configuration
@EnableWebSocket

public class WebSocketHandler implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatWebSocketHandler(), "/ws/chat")
                .setAllowedOrigins("*"); // Accept from all origins for testing
    }

}
