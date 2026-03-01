package com.example.Ez.service;

import com.example.Ez.dto.PublicacionDTO;
import com.example.Ez.model.Publicacion;
import com.example.Ez.model.Usuario;
import com.example.Ez.repository.PublicacionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;

    public PublicacionService(PublicacionRepository publicacionRepository) {
        this.publicacionRepository = publicacionRepository;
    }

    /**
     * Crea una nueva publicación
     */
    public PublicacionDTO crearPublicacion(Usuario usuario, String titulo, String descripcion,
                                           String baseDatos, String lenguajeProgramacion, BigDecimal valor) {
        Publicacion publicacion = new Publicacion();
        publicacion.setUsuario(usuario);
        publicacion.setTitulo(titulo);
        publicacion.setBaseDatos(baseDatos);
        publicacion.setLenguajeProgramacion(lenguajeProgramacion);
        publicacion.setDescripcion(descripcion);
        publicacion.setValor(valor);
        publicacion.setEstado(Publicacion.EstadoPublicacion.ACTIVA);

        Publicacion guardada = publicacionRepository.save(publicacion);
        return convertToDTO(guardada);
    }

    /**
     * Obtiene todas las publicaciones activas
     */
    public Page<PublicacionDTO> obtenerPublicacionesActivas(Pageable pageable) {
        Page<Publicacion> publicaciones = publicacionRepository.findPublicacionesActivas(pageable);
        List<PublicacionDTO> dtos = publicaciones.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, publicaciones.getTotalElements());
    }

    /**
     * Obtiene las publicaciones del usuario
     */
    public List<PublicacionDTO> obtenerPublicacionesDelUsuario(Usuario usuario) {
        return publicacionRepository.findPublicacionesActivasDeUsuario(usuario)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una publicación específica
     */
    public PublicacionDTO obtenerPublicacion(Long publicacionId) {
        return publicacionRepository.findById(publicacionId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));
    }

    /**
     * Filtra publicaciones por base de datos
     */
    public Page<PublicacionDTO> filtrarPorBaseDatos(String baseDatos, Pageable pageable) {
        Page<Publicacion> publicaciones = publicacionRepository.findByBaseDatos(baseDatos, pageable);
        List<PublicacionDTO> dtos = publicaciones.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, publicaciones.getTotalElements());
    }

    /**
     * Filtra publicaciones por lenguaje de programación
     */
    public Page<PublicacionDTO> filtrarPorLenguaje(String lenguaje, Pageable pageable) {
        Page<Publicacion> publicaciones = publicacionRepository.findByLenguajeProgramacion(lenguaje, pageable);
        List<PublicacionDTO> dtos = publicaciones.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, publicaciones.getTotalElements());
    }

    /**
     * Filtra publicaciones por usuario
     */
    public Page<PublicacionDTO> filtrarPorUsuario(Usuario usuario, Pageable pageable) {
        Page<Publicacion> publicaciones = publicacionRepository.findByUsuario(usuario, pageable);
        List<PublicacionDTO> dtos = publicaciones.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, publicaciones.getTotalElements());
    }

    /**
     * Actualiza una publicación
     */
    public PublicacionDTO actualizarPublicacion(Long publicacionId, String titulo, String descripcion,
                                                String baseDatos, String lenguajeProgramacion, BigDecimal valor) {
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        publicacion.setTitulo(titulo);
        publicacion.setDescripcion(descripcion);
        publicacion.setBaseDatos(baseDatos);
        publicacion.setLenguajeProgramacion(lenguajeProgramacion);
        publicacion.setValor(valor);

        Publicacion actualizada = publicacionRepository.save(publicacion);
        return convertToDTO(actualizada);
    }

    /**
     * Cambia el estado de la publicación
     */
    public PublicacionDTO cambiarEstado(Long publicacionId, Publicacion.EstadoPublicacion nuevoEstado) {
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        publicacion.setEstado(nuevoEstado);
        Publicacion actualizada = publicacionRepository.save(publicacion);
        return convertToDTO(actualizada);
    }

    /**
     * Elimina una publicación
     */
    public void eliminarPublicacion(Long publicacionId) {
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));
        
        publicacion.setEstado(Publicacion.EstadoPublicacion.ELIMINADA);
        publicacionRepository.save(publicacion);
    }

    /**
     * Convierte la entidad a DTO
     */
    private PublicacionDTO convertToDTO(Publicacion publicacion) {
        PublicacionDTO dto = new PublicacionDTO();
        dto.setId(publicacion.getId());
        dto.setUsuarioId(publicacion.getUsuario().getId());
        dto.setNombreUsuario(publicacion.getUsuario().getNombreUsuario());
        dto.setFotoPerfil(publicacion.getUsuario().getFotoPerfil());
        dto.setTitulo(publicacion.getTitulo());
        dto.setDescripcion(publicacion.getDescripcion());
        dto.setBaseDatos(publicacion.getBaseDatos());
        dto.setLenguajeProgramacion(publicacion.getLenguajeProgramacion());
        dto.setValor(publicacion.getValor());
        dto.setEstado(publicacion.getEstado().toString());
        dto.setImagenes(publicacion.getImagenes());
        dto.setFechaCreacion(publicacion.getFechaCreacion());
        return dto;
    }
}
