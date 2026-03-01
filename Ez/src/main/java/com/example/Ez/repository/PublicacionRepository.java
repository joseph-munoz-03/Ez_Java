package com.example.Ez.repository;

import com.example.Ez.model.Publicacion;
import com.example.Ez.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
    List<Publicacion> findByUsuario(Usuario usuario);
    Page<Publicacion> findByUsuario(Usuario usuario, Pageable pageable);
    
    List<Publicacion> findByEstado(Publicacion.EstadoPublicacion estado);
    Page<Publicacion> findByEstado(Publicacion.EstadoPublicacion estado, Pageable pageable);
    
    List<Publicacion> findByBaseDatos(String baseDatos);
    Page<Publicacion> findByBaseDatos(String baseDatos, Pageable pageable);
    
    List<Publicacion> findByLenguajeProgramacion(String lenguajeProgramacion);
    Page<Publicacion> findByLenguajeProgramacion(String lenguajeProgramacion, Pageable pageable);
    
    @Query("SELECT p FROM Publicacion p WHERE p.usuario = :usuario AND p.estado = 'ACTIVA'")
    List<Publicacion> findPublicacionesActivasDeUsuario(@Param("usuario") Usuario usuario);
    
    @Query("SELECT p FROM Publicacion p WHERE p.estado = 'ACTIVA' ORDER BY p.fechaCreacion DESC")
    Page<Publicacion> findPublicacionesActivas(Pageable pageable);
    
    @Query("SELECT p FROM Publicacion p WHERE (p.baseDatos = :baseDatos OR p.lenguajeProgramacion = :lenguaje) AND p.estado = 'ACTIVA'")
    Page<Publicacion> findPublicacionesPorFiltros(@Param("baseDatos") String baseDatos, 
                                                   @Param("lenguaje") String lenguaje, 
                                                   Pageable pageable);
}
