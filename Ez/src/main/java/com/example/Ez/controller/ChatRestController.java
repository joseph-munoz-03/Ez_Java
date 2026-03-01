package com.example.Ez.controller;

import com.example.Ez.dto.ChatDTO;
import com.example.Ez.dto.MensajeDTO;
import com.example.Ez.model.Mensaje;
import com.example.Ez.model.Usuario;
import com.example.Ez.service.ChatService;
import com.example.Ez.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatRestController {

    private final ChatService chatService;
    private final UsuarioService usuarioService;

    public ChatRestController(ChatService chatService, UsuarioService usuarioService) {
        this.chatService = chatService;
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene todos los chats del usuario autenticado
     */
    @GetMapping("/mis-chats")
    public ResponseEntity<List<ChatDTO>> obtenerMisChats() {
        // TODO: Obtener usuario autenticado del contexto de seguridad
        // Usuario usuarioActual = usuarioService.obtenerUsuarioAutenticado();
        return ResponseEntity.ok(List.of());
    }

    /**
     * Obtiene los chats del usuario paginado
     */
    @GetMapping("/mis-chats/paginado")
    public ResponseEntity<Page<ChatDTO>> obtenerMisChatsPaginado(Pageable pageable) {
        // TODO: Obtener usuario autenticado del contexto de seguridad
        return ResponseEntity.ok(Page.empty());
    }

    /**
     * Obtiene un chat específico
     */
    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDTO> obtenerChat(@PathVariable Long chatId) {
        try {
            ChatDTO chat = chatService.obtenerChat(chatId);
            return ResponseEntity.ok(chat);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea o obtiene un chat entre dos usuarios
     */
    @PostMapping("/crear-o-obtener")
    public ResponseEntity<ChatDTO> crearOObtenerChat(@RequestBody CreateChatRequest request) {
        try {
            // TODO: Obtener usuario autenticado
            // Usuario usuario1 = usuarioService.obtenerUsuarioAutenticado();
            Usuario usuario2 = usuarioService.obtenerPorId((long) request.getUsuario2Id())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Por ahora retorna error
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene los mensajes de un chat
     */
    @GetMapping("/{chatId}/mensajes")
    public ResponseEntity<List<MensajeDTO>> obtenerMensajes(@PathVariable Long chatId) {
        try {
            List<MensajeDTO> mensajes = chatService.obtenerMensajesDelChat(chatId);
            return ResponseEntity.ok(mensajes);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene los mensajes paginados
     */
    @GetMapping("/{chatId}/mensajes/paginado")
    public ResponseEntity<Page<MensajeDTO>> obtenerMensajesPaginado(
            @PathVariable Long chatId,
            Pageable pageable) {
        try {
            Page<MensajeDTO> mensajes = chatService.obtenerMensajesPaginado(chatId, pageable);
            return ResponseEntity.ok(mensajes);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Guarda un mensaje manualmente (para archivos adjuntos, etc)
     */
    @PostMapping("/{chatId}/mensaje")
    public ResponseEntity<MensajeDTO> guardarMensaje(
            @PathVariable Long chatId,
            @RequestBody GuardarMensajeRequest request) {
        try {
            // TODO: Obtener usuario autenticado
            // Usuario remitente = usuarioService.obtenerUsuarioAutenticado();
            
            // Por ahora retorna error
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Marca los mensajes como leídos
     */
    @PutMapping("/{chatId}/marcar-leido")
    public ResponseEntity<Void> marcarComoLeido(@PathVariable Long chatId) {
        try {
            chatService.marcarComoLeido(chatId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene la cantidad de mensajes no leídos
     */
    @GetMapping("/{chatId}/no-leidos")
    public ResponseEntity<Integer> obtenerNoLeleidos(@PathVariable Long chatId) {
        try {
            int noLeidos = chatService.obtenerMensajesNoLeeidos(chatId);
            return ResponseEntity.ok(noLeidos);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina un chat
     */
    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> eliminarChat(@PathVariable Long chatId) {
        try {
            chatService.eliminarChat(chatId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ===== PAYLOADS =====

    public static class CreateChatRequest {
        private Integer usuario2Id;

        public Integer getUsuario2Id() {
            return usuario2Id;
        }

        public void setUsuario2Id(Integer usuario2Id) {
            this.usuario2Id = usuario2Id;
        }
    }

    public static class GuardarMensajeRequest {
        private String contenido;
        private String tipoContenido;
        private String urlArchivo;

        public String getContenido() {
            return contenido;
        }

        public void setContenido(String contenido) {
            this.contenido = contenido;
        }

        public String getTipoContenido() {
            return tipoContenido;
        }

        public void setTipoContenido(String tipoContenido) {
            this.tipoContenido = tipoContenido;
        }

        public String getUrlArchivo() {
            return urlArchivo;
        }

        public void setUrlArchivo(String urlArchivo) {
            this.urlArchivo = urlArchivo;
        }
    }
}
