package com.example.Ez.repository;

import com.example.Ez.model.Transaccion;
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
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    List<Transaccion> findByUsuarioOrderByTimestampDesc(Usuario usuario);

    Page<Transaccion> findByUsuarioOrderByTimestampDesc(Usuario usuario, Pageable pageable);

    List<Transaccion> findByUsuarioAndTimestampBetween(Usuario usuario, LocalDateTime start, LocalDateTime end);

    @Query("SELECT t FROM Transaccion t WHERE t.usuario = :usuario AND t.estado = :estado")
    List<Transaccion> findByUsuarioAndEstado(@Param("usuario") Usuario usuario, @Param("estado") Transaccion.EstadoTransaccion estado);

    @Query("SELECT t FROM Transaccion t WHERE t.usuario = :usuario AND t.estado = com.example.Ez.model.Transaccion$EstadoTransaccion.COMPLETADA")
    List<Transaccion> findByUsuarioCompleted(@Param("usuario") Usuario usuario);
}
