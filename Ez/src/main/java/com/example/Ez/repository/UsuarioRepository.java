package com.example.Ez.repository;

import com.example.Ez.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByDocumentoUser(Long documento);

    @Query("SELECT u FROM Usuario u WHERE u.email = :email OR u.documentoUser = :documento")
    Optional<Usuario> findByEmailOrDocumento(@Param("email") String email, @Param("documento") Long documento);

    List<Usuario> findByEstado(Usuario.Estado estado);

    @Query("SELECT u FROM Usuario u WHERE u.nombre LIKE %:nombre% OR u.apellido LIKE %:nombre%")
    List<Usuario> buscarPorNombre(@Param("nombre") String nombre);
}
