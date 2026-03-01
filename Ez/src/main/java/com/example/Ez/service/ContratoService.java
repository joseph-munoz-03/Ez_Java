package com.example.Ez.service;

import com.example.Ez.dto.ContratoDTO;
import com.example.Ez.model.Contrato;
import com.example.Ez.model.ContratoPago;
import com.example.Ez.model.Usuario;
import com.example.Ez.repository.ContratoRepository;
import com.example.Ez.repository.ContratoPagoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContratoService {

    private final ContratoRepository contratoRepository;
    private final ContratoPagoRepository contratoPagoRepository;

    public ContratoService(ContratoRepository contratoRepository, 
                          ContratoPagoRepository contratoPagoRepository) {
        this.contratoRepository = contratoRepository;
        this.contratoPagoRepository = contratoPagoRepository;
    }

    /**
     * Crea un nuevo contrato y genera automáticamente los pagos
     */
    public ContratoDTO crearContrato(Usuario ingeniero, Usuario usuario, String titulo,
                                     String descripcion, BigDecimal valor, LocalDate fechaInicio,
                                     Integer cantidadDias, Integer cantidadPagos, String especificaciones) {
        Contrato contrato = new Contrato();
        contrato.setIngeniero(ingeniero);
        contrato.setUsuario(usuario);
        contrato.setTitulo(titulo);
        contrato.setValor(valor);
        contrato.setFechaInicio(fechaInicio);
        contrato.setCantidadDias(cantidadDias);
        contrato.setCantidadPagos(cantidadPagos);
        contrato.setDescripcion(descripcion);
        contrato.setEspecificaciones(especificaciones);
        contrato.setEstado(Contrato.EstadoContrato.ACTIVO);

        Contrato contratoGuardado = contratoRepository.save(contrato);

        // Generar automáticamente los pagos
        generarPagos(contratoGuardado, cantidadDias, cantidadPagos, valor);

        return convertToDTO(contratoGuardado);
    }

    /**
     * Genera automáticamente los pagos del contrato
     */
    private void generarPagos(Contrato contrato, Integer cantidadDias, Integer cantidadPagos, BigDecimal valorTotal) {
        BigDecimal montoPago = valorTotal.divide(BigDecimal.valueOf(cantidadPagos), 2, BigDecimal.ROUND_HALF_UP);

        for (int i = 1; i <= cantidadPagos; i++) {
            LocalDate fechaVencimiento = contrato.getFechaInicio().plusDays((long) cantidadDias * i);
            ContratoPago pago = new ContratoPago();
            pago.setContrato(contrato);
            pago.setNumeroPago(i);
            pago.setFechaVencimiento(fechaVencimiento);
            pago.setMonto(montoPago);
            pago.setEstado(ContratoPago.EstadoPago.PENDIENTE);
            contratoPagoRepository.save(pago);
        }
    }

    /**
     * Obtiene contratos del usuario (ingeniero o cliente)
     */
    public List<ContratoDTO> obtenerContratosDelUsuario(Usuario usuario) {
        return contratoRepository.findContratosByUsuario(usuario)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene contratos del usuario paginado
     */
    public Page<ContratoDTO> obtenerContratosPaginado(Usuario usuario, Pageable pageable) {
        Page<Contrato> contratos = contratoRepository.findContratosByUsuario(usuario, pageable);
        List<ContratoDTO> dtos = contratos.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, contratos.getTotalElements());
    }

    /**
     * Obtiene un contrato específico
     */
    public ContratoDTO obtenerContrato(Long contratoId) {
        return contratoRepository.findById(contratoId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));
    }

    /**
     * Actualiza el estado del contrato
     */
    public ContratoDTO actualizarEstado(Long contratoId, Contrato.EstadoContrato nuevoEstado) {
        Contrato contrato = contratoRepository.findById(contratoId)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));
        contrato.setEstado(nuevoEstado);
        Contrato actualizado = contratoRepository.save(contrato);
        return convertToDTO(actualizado);
    }

    /**
     * Obtiene los contratos con pagos pendientes
     */
    public List<ContratoDTO> obtenerContratosConPagosPendientes(Usuario usuario) {
        return obtenerContratosDelUsuario(usuario).stream()
                .filter(c -> c.getEstado().equals("ACTIVO"))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene contratos por rango de fechas
     */
    public List<ContratoDTO> obtenerContratosPorFecha(LocalDate fecha) {
        return contratoRepository.findContratosPorFecha(fecha)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convierte la entidad a DTO
     */
    private ContratoDTO convertToDTO(Contrato contrato) {
        ContratoDTO dto = new ContratoDTO();
        dto.setId(contrato.getId());
        dto.setIngenieroId(contrato.getIngeniero().getId());
        dto.setNombreIngeniero(contrato.getIngeniero().getNombre() + " " + contrato.getIngeniero().getApellido());
        dto.setUsuarioId(contrato.getUsuario().getId());
        dto.setNombreUsuario(contrato.getUsuario().getNombre() + " " + contrato.getUsuario().getApellido());
        dto.setTitulo(contrato.getTitulo());
        dto.setDescripcion(contrato.getDescripcion());
        dto.setValor(contrato.getValor());
        dto.setFechaInicio(contrato.getFechaInicio());
        dto.setCantidadDias(contrato.getCantidadDias());
        dto.setCantidadPagos(contrato.getCantidadPagos());
        dto.setEstado(contrato.getEstado().toString());
        dto.setEspecificaciones(contrato.getEspecificaciones());
        dto.setUrlContratoPdf(contrato.getUrlContratoPdf());
        dto.setFechaCreacion(contrato.getFechaCreacion());
        return dto;
    }
}
