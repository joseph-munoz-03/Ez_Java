package com.example.Ez.repository;

import com.example.Ez.model.ColorConfiguracion;
import com.example.Ez.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColorConfiguracionRepository extends JpaRepository<ColorConfiguracion, Long> {
    Optional<ColorConfiguracion> findByUsuario(Usuario usuario);
    Optional<ColorConfiguracion> findByUsuarioId(Integer usuarioId);
    Optional<ColorConfiguracion> findByUsuarioAndActivoTrue(Usuario usuario);
}
