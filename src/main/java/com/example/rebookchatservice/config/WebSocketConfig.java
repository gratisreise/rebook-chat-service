package com.example.rebookchatservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final String[] WHITE_LIST ={"http://localhost:5173", "https://rebookk.click"};

    @Value("amqp.client-login")
    private String clientLogin;

    @Value("amqp.client-passcode")
    private String clientPasscode;

    @Value("amqp.system-login")
    private String systemLogin;

    @Value("amqp.system-passcode")
    private String systemPasscode;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 연결할 엔드포인트 등록
        registry.addEndpoint("/api/ws-chat")
            .setAllowedOrigins(WHITE_LIST)
            .withSockJS()
            .setSuppressCors(true);
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        // STOMP Relay (RabbitMQ STOMP plugin)
        registry.enableStompBrokerRelay("/topic", "/queue")
            .setRelayHost("rabbitmq")
            .setRelayPort(61613)
            .setClientLogin(clientLogin)
            .setClientPasscode(clientPasscode)
            .setSystemLogin(systemLogin)
            .setSystemPasscode(systemPasscode);
    }

}
