package com.example.Ez.service;

import com.example.Ez.dto.SaldoDTO;
import com.example.Ez.model.Saldo;
import com.example.Ez.model.Usuario;
import com.example.Ez.repository.SaldoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class SaldoService {

    private final SaldoRepository saldoRepository;

    public SaldoService(SaldoRepository saldoRepository) {
        this.saldoRepository = saldoRepository;
    }

    /**
     * Obtiene el saldo del usuario
     */
    public Optional<SaldoDTO> obtenerSaldo(Usuario usuario) {
        return saldoRepository.findByUsuario(usuario)
                .map(this::convertToDTO);
    }

    /**
     * Obtiene el saldo por ID de usuario
     */
    public Optional<SaldoDTO> obtenerSaldoPorUsuarioId(Long usuarioId) {
        return saldoRepository.findByUsuarioId(usuarioId)
                .map(this::convertToDTO);
    }

    /**
     * Crea un nuevo saldo para un usuario
     */
    public SaldoDTO crearSaldo(Usuario usuario) {
        Saldo nuevoSaldo = new Saldo();
        nuevoSaldo.setUsuario(usuario);
        nuevoSaldo.setCantidad(BigDecimal.ZERO);
        Saldo guardado = saldoRepository.save(nuevoSaldo);
        return convertToDTO(guardado);
    }

    /**
     * Recarga el saldo (suma el monto)
     */
    public SaldoDTO recargar(Usuario usuario, BigDecimal monto) {
        Saldo saldo = saldoRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Saldo nuevoSaldo = new Saldo();
                    nuevoSaldo.setUsuario(usuario);
                    nuevoSaldo.setCantidad(BigDecimal.ZERO);
                    return nuevoSaldo;
                });
        
        saldo.setCantidad(saldo.getCantidad().add(monto));
        Saldo actualizado = saldoRepository.save(saldo);
        return convertToDTO(actualizado);
    }

    /**
     * Retira dinero del saldo
     */
    public SaldoDTO retirar(Usuario usuario, BigDecimal monto) throws Exception {
        Saldo saldo = saldoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new Exception("Saldo no encontrado"));
        
        if (saldo.getCantidad().compareTo(monto) < 0) {
            throw new Exception("Saldo insuficiente");
        }
        
        saldo.setCantidad(saldo.getCantidad().subtract(monto));
        Saldo actualizado = saldoRepository.save(saldo);
        return convertToDTO(actualizado);
    }

    /**
     * Deduce el saldo por pago de contrato
     */
    public void deducirPorPagoContrato(Usuario usuario, BigDecimal monto) throws Exception {
        retirar(usuario, monto);
    }

    /**
     * Obtiene la cantidad actual del saldo
     */
    public BigDecimal obtenerCantidad(Usuario usuario) throws Exception {
        return saldoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new Exception("Saldo no encontrado"))
                .getCantidad();
    }

    /**
     * Actualiza el saldo directamente
     */
    public SaldoDTO actualizarSaldo(Usuario usuario, BigDecimal cantidad) {
        Saldo saldo = saldoRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Saldo nuevoSaldo = new Saldo();
                    nuevoSaldo.setUsuario(usuario);
                    return nuevoSaldo;
                });
        
        saldo.setCantidad(cantidad);
        Saldo actualizado = saldoRepository.save(saldo);
        return convertToDTO(actualizado);
    }

    /**
     * Convierte la entidad a DTO
     */
    private SaldoDTO convertToDTO(Saldo saldo) {
        SaldoDTO dto = new SaldoDTO();
        dto.setId(saldo.getId());
        dto.setUsuarioId(saldo.getUsuario().getId());
        dto.setNombreUsuario(saldo.getUsuario().getNombreUsuario());
        dto.setCantidad(saldo.getCantidad());
        dto.setUltimaActualizacion(saldo.getUltimaActualizacion());
        dto.setFechaCreacion(saldo.getFechaCreacion());
        return dto;
    }
}
