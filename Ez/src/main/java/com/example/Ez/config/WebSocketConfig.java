package com.example.Ez.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configura el message broker para WebSocket
     * 
     * Esto permite enviar mensajes a través de STOMP protocol
     * Los mensajes se envían a través de tópicos que los clientes pueden suscribirse
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Habilitar message broker simple en memoria
        config.enableSimpleBroker("/topic", "/queue");
        
        // Prefijo para mensajes enviados por el cliente al servidor
        config.setApplicationDestinationPrefixes("/app");
        
        // Opcional: Para producción, usar un message broker externo (RabbitMQ, etc)
        // config.enableStompBrokerRelay("/topic", "/queue")
        //     .setRelayHost("localhost")
        //     .setRelayPort(61613);
    }

    /**
     * Registra los endpoints WebSocket
     * 
     * Los clientes se conectarán a /ws para establecer la conexión WebSocket
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint para conexión WebSocket
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .withSockJS();
        
        // STOMP endpoint sin SockJS (opcional, para clientes nativos)
        registry.addEndpoint("/ws-stomp")
                .setAllowedOrigins("*");
    }

    /**
     * ESTRUCTURA DE TÓPICOS Y COLAS
     * 
     * PARA CHAT:
     * - Enviar mensajes: /app/chat/{chatId}/mensaje
     * - Suscribirse a mensajes: /topic/chat/{chatId}
     * - Notificaciones privadas: /user/queue/notifications
     * 
     * PARA NOTIFICACIONES:
     * - /topic/notificaciones/{usuarioId}
     * - /user/queue/alerts
     * 
     * FLUJO DE EJEMPLO:
     * 1. Cliente se conecta a /ws
     * 2. Cliente envía mensaje a /app/chat/123/mensaje
     * 3. Servidor procesa y envía a /topic/chat/123
     * 4. Todos los clientes suscritos a /topic/chat/123 reciben el mensaje
     */
}
