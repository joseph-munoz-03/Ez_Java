package com.example.Ez.controller;

import com.example.Ez.dto.MensajeDTO;
import com.example.Ez.model.Mensaje;
import com.example.Ez.model.Usuario;
import com.example.Ez.service.ChatService;
import com.example.Ez.service.UsuarioService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin
public class ChatWebSocketController {

    private final ChatService chatService;
    private final UsuarioService usuarioService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatWebSocketController(ChatService chatService, UsuarioService usuarioService,
                                  SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.usuarioService = usuarioService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * NOTA: Este controlador maneja los mensajes WebSocket en tiempo real
     * 
     * FLUJO:
     * 1. Cliente envía mensaje a /app/chat/{chatId}/mensaje
     * 2. Este método es invocado con los datos del mensaje
     * 3. Se guarda en la BD
     * 4. Se envía a todos los clientes suscritos a /topic/chat/{chatId}
     */

    /**
     * Maneja los mensajes enviados en un chat
     * 
     * Endpoint: /app/chat/{chatId}/mensaje
     * Broadcast: /topic/chat/{chatId}
     */
    @MessageMapping("/chat/{chatId}/mensaje")
    @SendTo("/topic/chat/{chatId}")
    public MensajeDTO handleChatMessage(
            @DestinationVariable Long chatId,
            @Payload MensajeRequestPayload payload) {

        try {
            // Obtener el usuario remitente (en producción, obtener del contexto de seguridad)
            Usuario remitente = usuarioService.obtenerPorId((long) payload.getRemitenteId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Guardar el mensaje
            MensajeDTO mensajeGuardado = chatService.guardarMensaje(
                    chatId,
                    remitente,
                    payload.getContenido(),
                    Mensaje.TipoContenido.TEXTO,
                    payload.getUrlArchivo()
            );

            return mensajeGuardado;
        } catch (Exception e) {
            System.err.println("Error en handleChatMessage: " + e.getMessage());
            return null;
        }
    }

    /**
     * Maneja la notificación de "está escribiendo"
     * 
     * Endpoint: /app/chat/{chatId}/typing
     * Broadcast: /topic/chat/{chatId}/typing
     */
    @MessageMapping("/chat/{chatId}/typing")
    @SendTo("/topic/chat/{chatId}/typing")
    public TypingNotification handleTypingNotification(
            @DestinationVariable Long chatId,
            @Payload TypingNotificationPayload payload) {

        return new TypingNotification(payload.getRemitenteId(), payload.getIsTyping());
    }

    /**
     * Maneja las notificaciones de lectura
     * 
     * Endpoint: /app/chat/{chatId}/read
     * Broadcast: /topic/chat/{chatId}/read
     */
    @MessageMapping("/chat/{chatId}/read")
    @SendTo("/topic/chat/{chatId}/read")
    public ReadNotification handleReadNotification(
            @DestinationVariable Long chatId,
            @Payload ReadNotificationPayload payload) {

        // Marcar mensajes como leídos
        chatService.marcarComoLeido(chatId);

        return new ReadNotification(payload.getRemitenteId(), true);
    }

    /**
     * Payload para enviar mensajes
     */
    public static class MensajeRequestPayload {
        private Integer remitenteId;
        private String contenido;
        private String urlArchivo;

        public Integer getRemitenteId() {
            return remitenteId;
        }

        public void setRemitenteId(Integer remitenteId) {
            this.remitenteId = remitenteId;
        }

        public String getContenido() {
            return contenido;
        }

        public void setContenido(String contenido) {
            this.contenido = contenido;
        }

        public String getUrlArchivo() {
            return urlArchivo;
        }

        public void setUrlArchivo(String urlArchivo) {
            this.urlArchivo = urlArchivo;
        }
    }

    /**
     * Notificación de "está escribiendo"
     */
    public static class TypingNotification {
        private Integer usuarioId;
        private Boolean isTyping;

        public TypingNotification(Integer usuarioId, Boolean isTyping) {
            this.usuarioId = usuarioId;
            this.isTyping = isTyping;
        }

        public Integer getUsuarioId() {
            return usuarioId;
        }

        public void setUsuarioId(Integer usuarioId) {
            this.usuarioId = usuarioId;
        }

        public Boolean getIsTyping() {
            return isTyping;
        }

        public void setIsTyping(Boolean isTyping) {
            this.isTyping = isTyping;
        }
    }

    /**
     * Payload para notificación de "está escribiendo"
     */
    public static class TypingNotificationPayload {
        private Integer remitenteId;
        private Boolean isTyping;

        public Integer getRemitenteId() {
            return remitenteId;
        }

        public void setRemitenteId(Integer remitenteId) {
            this.remitenteId = remitenteId;
        }

        public Boolean getIsTyping() {
            return isTyping;
        }

        public void setIsTyping(Boolean isTyping) {
            this.isTyping = isTyping;
        }
    }

    /**
     * Notificación de lectura
     */
    public static class ReadNotification {
        private Integer usuarioId;
        private Boolean allRead;

        public ReadNotification(Integer usuarioId, Boolean allRead) {
            this.usuarioId = usuarioId;
            this.allRead = allRead;
        }

        public Integer getUsuarioId() {
            return usuarioId;
        }

        public void setUsuarioId(Integer usuarioId) {
            this.usuarioId = usuarioId;
        }

        public Boolean getAllRead() {
            return allRead;
        }

        public void setAllRead(Boolean allRead) {
            this.allRead = allRead;
        }
    }

    /**
     * Payload para notificación de lectura
     */
    public static class ReadNotificationPayload {
        private Integer remitenteId;

        public Integer getRemitenteId() {
            return remitenteId;
        }

        public void setRemitenteId(Integer remitenteId) {
            this.remitenteId = remitenteId;
        }
    }
}
