package com.example.Ez.repository;

import com.example.Ez.model.Contrato;
import com.example.Ez.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    List<Contrato> findByIngeniero(Usuario ingeniero);
    List<Contrato> findByUsuario(Usuario usuario);
    List<Contrato> findByIngenieroOrUsuario(Usuario ingeniero, Usuario usuario);
    Page<Contrato> findByIngenieroOrUsuario(Usuario ingeniero, Usuario usuario, Pageable pageable);
    List<Contrato> findByIngenieroAndEstado(Usuario ingeniero, Contrato.EstadoContrato estado);
    List<Contrato> findByUsuarioAndEstado(Usuario usuario, Contrato.EstadoContrato estado);
    List<Contrato> findByEstado(Contrato.EstadoContrato estado);
    
    @Query("SELECT c FROM Contrato c WHERE c.ingeniero = :usuario OR c.usuario = :usuario")
    List<Contrato> findContratosByUsuario(@Param("usuario") Usuario usuario);
    
    @Query("SELECT c FROM Contrato c WHERE c.ingeniero = :usuario OR c.usuario = :usuario")
    Page<Contrato> findContratosByUsuario(@Param("usuario") Usuario usuario, Pageable pageable);
    
    @Query("SELECT c FROM Contrato c WHERE c.fechaInicio <= :fecha AND c.estado = 'ACTIVO'")
    List<Contrato> findContratosPorFecha(@Param("fecha") LocalDate fecha);
}
