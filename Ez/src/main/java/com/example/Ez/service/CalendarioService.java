package com.example.Ez.service;

import com.example.Ez.model.Contrato;
import com.example.Ez.model.ContratoPago;
import com.example.Ez.model.Usuario;
import com.example.Ez.repository.ContratoRepository;
import com.example.Ez.repository.ContratoPagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CalendarioService {

    private final ContratoRepository contratoRepository;
    private final ContratoPagoRepository contratoPagoRepository;

    public CalendarioService(ContratoRepository contratoRepository,
                            ContratoPagoRepository contratoPagoRepository) {
        this.contratoRepository = contratoRepository;
        this.contratoPagoRepository = contratoPagoRepository;
    }

    /**
     * Obtiene las fechas de pagos del usuario para el calendario
     * Retorna un mapa con fecha como clave y lista de contratos asociados
     */
    public Map<LocalDate, List<ContratoPago>> obtenerFechasPagosDelUsuario(Usuario usuario) {
        List<Contrato> contratos = contratoRepository.findContratosByUsuario(usuario);
        return obtenerFechasPagos(contratos);
    }

    /**
     * Obtiene las fechas de pagos de un contrato específico
     */
    public List<ContratoPago> obtenerFechasPagosPorContrato(Long contratoId) {
        Contrato contrato = contratoRepository.findById(contratoId)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));
        return contratoPagoRepository.findByContratoOrderByNumeroPago(contrato);
    }

    /**
     * Obtiene el contrato vinculado a una fecha específica
     */
    public ContratoPago obtenerPagoPorFecha(LocalDate fecha) {
        // Aquí se buscaría en la BD pero por ahora retorna null
        return null;
    }

    /**
     * Obtiene las fechas de vencimiento próximas
     */
    public List<ContratoPago> obtenerPagosProximos(Usuario usuario, int diasDelante) {
        LocalDate hoy = LocalDate.now();
        LocalDate proximaFecha = hoy.plusDays(diasDelante);
        List<Contrato> contratos = contratoRepository.findContratosByUsuario(usuario);
        
        return contratoPagoRepository.findByContratos(contratos).stream()
                .filter(cp -> cp.getFechaVencimiento().isBefore(proximaFecha) || 
                             cp.getFechaVencimiento().isEqual(proximaFecha))
                .filter(cp -> cp.getEstado() == ContratoPago.EstadoPago.PENDIENTE)
                .toList();
    }

    /**
     * Obtiene los pagos vencidos
     */
    public List<ContratoPago> obtenerPagosVencidos() {
        return contratoPagoRepository.findPagosVencidos(LocalDate.now());
    }

    /**
     * Obtiene los pagos vencidos del usuario
     */
    public List<ContratoPago> obtenerPagosVencidosDelUsuario(Usuario usuario) {
        List<Contrato> contratos = contratoRepository.findContratosByUsuario(usuario);
        return contratoPagoRepository.findByContratos(contratos).stream()
                .filter(cp -> cp.getFechaVencimiento().isBefore(LocalDate.now()))
                .filter(cp -> cp.getEstado() == ContratoPago.EstadoPago.PENDIENTE)
                .toList();
    }

    /**
     * Obtiene el contrato linkeditor de un pago por fecha
     */
    public Contrato obtenerContratoDeUnPago(LocalDate fecha, Usuario usuario) {
        List<Contrato> contratos = contratoRepository.findContratosByUsuario(usuario);
        
        for (Contrato contrato : contratos) {
            List<ContratoPago> pagos = contratoPagoRepository.findByContratoOrderByNumeroPago(contrato);
            for (ContratoPago pago : pagos) {
                if (pago.getFechaVencimiento().isEqual(fecha)) {
                    return contrato;
                }
            }
        }
        return null;
    }

    /**
     * Calcula el siguiente día de pago desde una fecha inicial
     */
    public LocalDate calcularSiguienteDiaPago(LocalDate fechaInicio, Integer cantidadDias, Integer numeroPago) {
        return fechaInicio.plusDays((long) cantidadDias * numeroPago);
    }

    /**
     * Obtiene todas las fechas de pagos en un rango de fechas
     */
    public List<ContratoPago> obtenerPagosEnRango(Usuario usuario, LocalDate inicio, LocalDate fin) {
        List<Contrato> contratos = contratoRepository.findContratosByUsuario(usuario);
        
        return contratoPagoRepository.findByContratos(contratos).stream()
                .filter(cp -> !cp.getFechaVencimiento().isBefore(inicio) && 
                             !cp.getFechaVencimiento().isAfter(fin))
                .toList();
    }

    /**
     * Obtiene un mapa de fechas a contratos
     */
    private Map<LocalDate, List<ContratoPago>> obtenerFechasPagos(List<Contrato> contratos) {
        Map<LocalDate, List<ContratoPago>> mapFechas = new HashMap<>();
        
        for (Contrato contrato : contratos) {
            List<ContratoPago> pagos = contratoPagoRepository.findByContratoOrderByNumeroPago(contrato);
            for (ContratoPago pago : pagos) {
                mapFechas.computeIfAbsent(pago.getFechaVencimiento(), k -> new java.util.ArrayList<>())
                        .add(pago);
            }
        }
        
        return mapFechas;
    }

    /**
     * Valida si la fecha tiene contratos asociados
     */
    public boolean tienePagosEnFecha(LocalDate fecha, Usuario usuario) {
        Map<LocalDate, List<ContratoPago>> mapa = obtenerFechasPagosDelUsuario(usuario);
        return mapa.containsKey(fecha);
    }
}
