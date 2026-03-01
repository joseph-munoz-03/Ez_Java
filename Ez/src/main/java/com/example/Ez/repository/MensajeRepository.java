package com.example.Ez.repository;

import com.example.Ez.model.Mensaje;
import com.example.Ez.model.Chat;
import com.example.Ez.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByChatOrderByTimestampAsc(Chat chat);
    Page<Mensaje> findByChat(Chat chat, Pageable pageable);
    
    @Query("SELECT m FROM Mensaje m WHERE m.chat = :chat ORDER BY m.timestamp DESC")
    Page<Mensaje> findChatMensajesPaginado(@Param("chat") Chat chat, Pageable pageable);
    
    List<Mensaje> findByRemitenteAndLeidoFalse(Usuario remitente);
    List<Mensaje> findByChat_Usuario1AndLeidoFalseOrChat_Usuario2AndLeidoFalse(Usuario usuario1, Usuario usuario2);
    
    @Query("SELECT COUNT(m) FROM Mensaje m WHERE m.chat = :chat AND m.leido = false")
    int countNoLeidosPorChat(@Param("chat") Chat chat);
    
    @Query("SELECT m FROM Mensaje m WHERE m.chat = :chat AND m.timestamp BETWEEN :inicio AND :fin ORDER BY m.timestamp")
    List<Mensaje> findMensajesPorFecha(@Param("chat") Chat chat, @Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}
