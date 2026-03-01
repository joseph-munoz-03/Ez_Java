package com.example.Ez.service;

import com.example.Ez.dto.TransaccionDTO;
import com.example.Ez.model.Transaccion;
import com.example.Ez.model.Usuario;
import com.example.Ez.repository.TransaccionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;

    public TransaccionService(TransaccionRepository transaccionRepository) {
        this.transaccionRepository = transaccionRepository;
    }

    /**
     * Crea una nueva transacción
     */
    public TransaccionDTO crearTransaccion(Usuario usuario, 
                                           Transaccion.TipoTransaccion tipo,
                                           BigDecimal cantidad,
                                           Transaccion.MetodoPago metodoPago,
                                           String descripcion) {
        Transaccion transaccion = new Transaccion();
        transaccion.setUsuario(usuario);
        transaccion.setTipo(tipo);
        transaccion.setCantidad(cantidad);
        transaccion.setMetodoPago(metodoPago);
        transaccion.setDescripcion(descripcion);
        transaccion.setEstado(Transaccion.EstadoTransaccion.PENDIENTE);

        Transaccion guardada = transaccionRepository.save(transaccion);
        return convertToDTO(guardada);
    }

    /**
     * Obtiene el historial de transacciones del usuario
     */
    public List<TransaccionDTO> obtenerHistorialTransacciones(Usuario usuario) {
        return transaccionRepository.findByUsuarioOrderByTimestampDesc(usuario)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el historial paginado
     */
    public Page<TransaccionDTO> obtenerHistorialPaginado(Usuario usuario, Pageable pageable) {
        Page<Transaccion> transacciones = transaccionRepository.findByUsuarioOrderByTimestampDesc(usuario, pageable);
        List<TransaccionDTO> dtos = transacciones.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, transacciones.getTotalElements());
    }

    /**
     * Actualiza el estado de la transacción
     */
    public TransaccionDTO actualizarEstado(Long transaccionId, Transaccion.EstadoTransaccion nuevoEstado, String referencia) {
        Transaccion transaccion = transaccionRepository.findById(transaccionId)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));
        
        transaccion.setEstado(nuevoEstado);
        if (referencia != null) {
            transaccion.setReferencia(referencia);
        }
        
        Transaccion actualizada = transaccionRepository.save(transaccion);
        return convertToDTO(actualizada);
    }

    /**
     * Obtiene transacciones completadas
     */
    public List<TransaccionDTO> obtenerTransaccionesCompletadas(Usuario usuario) {
        return transaccionRepository.findByUsuarioAndEstado(usuario, Transaccion.EstadoTransaccion.COMPLETADA)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene transacciones por tipo
     */
    public List<TransaccionDTO> obtenerTransaccionesPorTipo(Usuario usuario, Transaccion.TipoTransaccion tipo) {
        return obtenerHistorialTransacciones(usuario).stream()
                .filter(t -> t.getTipo().equals(tipo.toString()))
                .collect(Collectors.toList());
    }

    /**
     * Convierte la entidad a DTO
     */
    private TransaccionDTO convertToDTO(Transaccion transaccion) {
        TransaccionDTO dto = new TransaccionDTO();
        dto.setId(transaccion.getId());
        dto.setUsuarioId(transaccion.getUsuario().getId());
        dto.setTipo(transaccion.getTipo().toString());
        dto.setCantidad(transaccion.getCantidad());
        dto.setEstado(transaccion.getEstado().toString());
        dto.setMetodoPago(transaccion.getMetodoPago().toString());
        dto.setReferencia(transaccion.getReferencia());
        dto.setDescripcion(transaccion.getDescripcion());
        dto.setTimestamp(transaccion.getTimestamp());
        return dto;
    }
}
