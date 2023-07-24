package com.ludo.Snake.and.Ladder.Config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/SnakeLadder").setAllowedOriginPatterns("*").withSockJS()
    }

    @Override
    void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app")
        registry.enableSimpleBroker("/startGame","/user","/joinPlayer")
        registry.setUserDestinationPrefix("/user")
    }
}
