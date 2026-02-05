package com.example.Ez.repository;

import com.example.Ez.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

    Optional<Rol> findByTipoRol(Rol.TipoRol tipoRol);
}
