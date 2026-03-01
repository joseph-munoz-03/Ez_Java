package com.example.Ez.service;

import com.example.Ez.dto.ChatDTO;
import com.example.Ez.dto.MensajeDTO;
import com.example.Ez.model.Chat;
import com.example.Ez.model.Mensaje;
import com.example.Ez.model.Usuario;
import com.example.Ez.repository.ChatRepository;
import com.example.Ez.repository.MensajeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatService {

    private final ChatRepository chatRepository;
    private final MensajeRepository mensajeRepository;

    public ChatService(ChatRepository chatRepository, MensajeRepository mensajeRepository) {
        this.chatRepository = chatRepository;
        this.mensajeRepository = mensajeRepository;
    }

    /**
     * Obtiene o crea un chat entre dos usuarios
     */
    public ChatDTO obtenerOCrearChat(Usuario usuario1, Usuario usuario2) {
        Optional<Chat> chatExistente = chatRepository.findChatBetweenUsers(usuario1, usuario2);
        
        Chat chat;
        if (chatExistente.isPresent()) {
            chat = chatExistente.get();
        } else {
            chat = new Chat();
            chat.setUsuario1(usuario1);
            chat.setUsuario2(usuario2);
            chat = chatRepository.save(chat);
        }
        
        return convertToDTO(chat);
    }

    /**
     * Obtiene un chat específico
     */
    public ChatDTO obtenerChat(Long chatId) {
        return chatRepository.findById(chatId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));
    }

    /**
     * Obtiene todos los chats del usuario
     */
    public List<ChatDTO> obtenerChatsDelUsuario(Usuario usuario) {
        return chatRepository.findChatsConUsuario(usuario)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los chats paginados
     */
    public Page<ChatDTO> obtenerChatsPaginado(Usuario usuario, Pageable pageable) {
        Page<Chat> chats = chatRepository.findByUsuario1OrUsuario2(usuario, usuario, pageable);
        List<ChatDTO> dtos = chats.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, chats.getTotalElements());
    }

    /**
     * Guarda un mensaje en el chat
     */
    public MensajeDTO guardarMensaje(Long chatId, Usuario remitente, String contenido, 
                                     Mensaje.TipoContenido tipoContenido, String urlArchivo) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));
        
        Mensaje mensaje = new Mensaje();
        mensaje.setChat(chat);
        mensaje.setRemitente(remitente);
        mensaje.setContenido(contenido);
        mensaje.setTipoContenido(tipoContenido);
        mensaje.setUrlArchivo(urlArchivo);
        Mensaje guardado = mensajeRepository.save(mensaje);
        
        // Actualizar último acceso del chat
        chat.setUltimaActualizacion(java.time.LocalDateTime.now());
        chatRepository.save(chat);
        
        return convertMensajeToDTO(guardado);
    }

    /**
     * Obtiene los mensajes de un chat
     */
    public List<MensajeDTO> obtenerMensajesDelChat(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));
        
        return mensajeRepository.findByChatOrderByTimestampAsc(chat)
                .stream()
                .map(this::convertMensajeToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los mensajes paginados
     */
    public Page<MensajeDTO> obtenerMensajesPaginado(Long chatId, Pageable pageable) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));
        
        Page<Mensaje> mensajes = mensajeRepository.findChatMensajesPaginado(chat, pageable);
        List<MensajeDTO> dtos = mensajes.getContent()
                .stream()
                .map(this::convertMensajeToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, mensajes.getTotalElements());
    }

    /**
     * Marca los mensajes como leídos
     */
    public void marcarComoLeido(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));
        
        mensajeRepository.findByChatOrderByTimestampAsc(chat).forEach(m -> m.setLeido(true));
    }

    /**
     * Obtiene la cantidad de mensajes no leídos
     */
    public int obtenerMensajesNoLeeidos(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));
        return mensajeRepository.countNoLeidosPorChat(chat);
    }

    /**
     * Elimina un chat
     */
    public void eliminarChat(Long chatId) {
        chatRepository.deleteById(chatId);
    }

    /**
     * Convierte Chat entidad a DTO
     */
    private ChatDTO convertToDTO(Chat chat) {
        ChatDTO dto = new ChatDTO();
        dto.setId(chat.getId());
        dto.setUsuario1Id(chat.getUsuario1().getId());
        dto.setUsuario1Nombre(chat.getUsuario1().getNombreUsuario());
        dto.setUsuario1FotoPerfil(chat.getUsuario1().getFotoPerfil());
        dto.setUsuario2Id(chat.getUsuario2().getId());
        dto.setUsuario2Nombre(chat.getUsuario2().getNombreUsuario());
        dto.setUsuario2FotoPerfil(chat.getUsuario2().getFotoPerfil());
        dto.setCreatedAt(chat.getCreatedAt());
        dto.setUltimaActualizacion(chat.getUltimaActualizacion());
        dto.setMensajesNoLleidos(mensajeRepository.countNoLeidosPorChat(chat));
        return dto;
    }

    /**
     * Convierte Mensaje entidad a DTO
     */
    private MensajeDTO convertMensajeToDTO(Mensaje mensaje) {
        MensajeDTO dto = new MensajeDTO();
        dto.setId(mensaje.getId());
        dto.setChatId(mensaje.getChat().getId());
        dto.setRemitentId(mensaje.getRemitente().getId());
        dto.setRemitenteNombre(mensaje.getRemitente().getNombreUsuario());
        dto.setFotoPerfil(mensaje.getRemitente().getFotoPerfil());
        dto.setContenido(mensaje.getContenido());
        dto.setTipoContenido(mensaje.getTipoContenido().toString());
        dto.setUrlArchivo(mensaje.getUrlArchivo());
        dto.setTimestamp(mensaje.getTimestamp());
        dto.setLeido(mensaje.getLeido());
        return dto;
    }
}
