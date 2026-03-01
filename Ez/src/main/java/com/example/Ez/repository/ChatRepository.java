package com.example.Ez.repository;

import com.example.Ez.model.Chat;
import com.example.Ez.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByUsuario1OrUsuario2(Usuario usuario1, Usuario usuario2);
    Page<Chat> findByUsuario1OrUsuario2(Usuario usuario1, Usuario usuario2, Pageable pageable);
    
    @Query("SELECT c FROM Chat c WHERE (c.usuario1 = :usuario1 AND c.usuario2 = :usuario2) OR (c.usuario1 = :usuario2 AND c.usuario2 = :usuario1)")
    Optional<Chat> findChatBetweenUsers(@Param("usuario1") Usuario usuario1, @Param("usuario2") Usuario usuario2);
    
    @Query("SELECT c FROM Chat c WHERE c.usuario1 = :usuario OR c.usuario2 = :usuario ORDER BY c.ultimaActualizacion DESC")
    List<Chat> findChatsConUsuario(@Param("usuario") Usuario usuario);
}
