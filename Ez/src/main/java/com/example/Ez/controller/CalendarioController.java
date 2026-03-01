package com.example.Ez.controller;

import com.example.Ez.model.ContratoPago;
import com.example.Ez.model.Usuario;
import com.example.Ez.service.CalendarioService;
import com.example.Ez.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/calendario")
@CrossOrigin
public class CalendarioController {

    private final CalendarioService calendarioService;
    private final UsuarioService usuarioService;

    public CalendarioController(CalendarioService calendarioService, UsuarioService usuarioService) {
        this.calendarioService = calendarioService;
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene todas las fechas de pagos del usuario para el calendario
     * 
     * Retorna un mapa donde:
     * - Key: LocalDate (fecha de vencimiento)
     * - Value: Lista de pagos que vencen en esa fecha
     * 
     * Esto permite marcar en el calendario las fechas con pagos pendientes
     */
    @GetMapping("/fechas-pagos")
    public ResponseEntity<CalendarioResponse> obtenerFechasPagos() {
        try {
            // TODO: Obtener usuario autenticado
            // Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

            // Retornar ejemplo vacío por ahora
            return ResponseEntity.ok(new CalendarioResponse(
                    Map.of(),
                    "Proporcione usuario autenticado"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene las fechas específicas de un contrato
     * 
     * Ejemplo:
     * Contrato iniciado: 5 feb 2025
     * Cada: 15 días
     * Pagos: 3
     * 
     * Retorna fechas: 20 feb, 7 mar, 22 mar
     */
    @GetMapping("/contrato/{contratoId}/fechas-pagos")
    public ResponseEntity<List<FechaPagoResponse>> obtenerFechasPorContrato(
            @PathVariable Long contratoId) {
        try {
            List<ContratoPago> pagos = calendarioService.obtenerFechasPagosPorContrato(contratoId);
            List<FechaPagoResponse> respuesta = pagos.stream()
                    .map(p -> new FechaPagoResponse(
                            p.getFechaVencimiento(),
                            p.getNumeroPago(),
                            p.getEstado().toString(),
                            p.getMonto(),
                            contratoId
                    ))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene los pagos próximos (próximos N días)
     */
    @GetMapping("/pagos-proximos")
    public ResponseEntity<List<PagoProximoResponse>> obtenerPagosProximos(
            @RequestParam(defaultValue = "30") int dias) {
        try {
            // TODO: Obtener usuario autenticado
            // Usuario usuario = usuarioService.obtenerUsuarioAutenticado();
            // List<ContratoPago> pagos = calendarioService.obtenerPagosProximos(usuario, dias);

            return ResponseEntity.ok(List.of());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene los pagos vencidos
     */
    @GetMapping("/pagos-vencidos")
    public ResponseEntity<List<PagoVencidoResponse>> obtenerPagosVencidos() {
        try {
            List<ContratoPago> pagos = calendarioService.obtenerPagosVencidos();
            List<PagoVencidoResponse> respuesta = pagos.stream()
                    .map(p -> new PagoVencidoResponse(
                            p.getId(),
                            p.getFechaVencimiento(),
                            p.getMonto(),
                            p.getContrato().getId(),
                            p.getContrato().getTitulo(),
                            p.getNumeroPago()
                    ))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene los pagos en un rango de fechas
     */
    @GetMapping("/pagos-rango")
    public ResponseEntity<List<PagoRangoResponse>> obtenerPagosRango(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        try {
            // TODO: Obtener usuario autenticado
            // Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

            LocalDate inicio = LocalDate.parse(fechaInicio);
            LocalDate fin = LocalDate.parse(fechaFin);

            // List<ContratoPago> pagos = calendarioService.obtenerPagosEnRango(usuario, inicio, fin);
            
            return ResponseEntity.ok(List.of());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Verifica si hay pagos en una fecha específica
     */
    @GetMapping("/tiene-pagos/{fecha}")
    public ResponseEntity<VerificacionFechaResponse> tienePagosEnFecha(
            @PathVariable String fecha) {
        try {
            // TODO: Obtener usuario autenticado
            // Usuario usuario = usuarioService.obtenerUsuarioAutenticado();
            
            LocalDate date = LocalDate.parse(fecha);
            // boolean tienePagos = calendarioService.tienePagosEnFecha(date, usuario);

            return ResponseEntity.ok(new VerificacionFechaResponse(date, false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene el contrato vinculado a una fecha
     * 
     * Cuando el usuario hace clic en una fecha del calendario,
     * este endpoint retorna el contrato asociado a esa fecha
     */
    @GetMapping("/contrato-por-fecha/{fecha}")
    public ResponseEntity<ContratoFechaResponse> obtenerContratoPorFecha(
            @PathVariable String fecha) {
        try {
            // TODO: Obtener usuario autenticado
            // Usuario usuario = usuarioService.obtenerUsuarioAutenticado();
            
            LocalDate date = LocalDate.parse(fecha);
            // Contrato contrato = calendarioService.obtenerContratoDeUnPago(date, usuario);

            return ResponseEntity.ok(new ContratoFechaResponse(
                    date,
                    null, // contrato
                    "Proporcione usuario autenticado"
            ));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ===== RESPONSE CLASSES =====

    public static class CalendarioResponse {
        private Map<LocalDate, ?> fechasPagos;
        private String mensaje;

        public CalendarioResponse(Map<LocalDate, ?> fechasPagos, String mensaje) {
            this.fechasPagos = fechasPagos;
            this.mensaje = mensaje;
        }

        public Map<LocalDate, ?> getFechasPagos() {
            return fechasPagos;
        }

        public String getMensaje() {
            return mensaje;
        }
    }

    public static class FechaPagoResponse {
        private LocalDate fecha;
        private Integer numeroPago;
        private String estado;
        private Object monto;
        private Long contratoId;

        public FechaPagoResponse(LocalDate fecha, Integer numeroPago, String estado, Object monto, Long contratoId) {
            this.fecha = fecha;
            this.numeroPago = numeroPago;
            this.estado = estado;
            this.monto = monto;
            this.contratoId = contratoId;
        }

        public LocalDate getFecha() {
            return fecha;
        }

        public Integer getNumeroPago() {
            return numeroPago;
        }

        public String getEstado() {
            return estado;
        }

        public Object getMonto() {
            return monto;
        }

        public Long getContratoId() {
            return contratoId;
        }
    }

    public static class PagoProximoResponse {
        private LocalDate fecha;
        private Object monto;
        private Long contratoId;
        private String tituloContrato;
        private Integer diasRestantes;

        // Getters
        public LocalDate getFecha() {
            return fecha;
        }

        public Object getMonto() {
            return monto;
        }

        public Long getContratoId() {
            return contratoId;
        }

        public String getTituloContrato() {
            return tituloContrato;
        }

        public Integer getDiasRestantes() {
            return diasRestantes;
        }
    }

    public static class PagoVencidoResponse {
        private Long id;
        private LocalDate fecha;
        private Object monto;
        private Long contratoId;
        private String tituloContrato;
        private Integer numeroPago;
        private Integer diasVencido;

        public PagoVencidoResponse(Long id, LocalDate fecha, Object monto, Long contratoId, 
                                  String tituloContrato, Integer numeroPago) {
            this.id = id;
            this.fecha = fecha;
            this.monto = monto;
            this.contratoId = contratoId;
            this.tituloContrato = tituloContrato;
            this.numeroPago = numeroPago;
            this.diasVencido = (int) java.time.temporal.ChronoUnit.DAYS.between(fecha, LocalDate.now());
        }

        // Getters
        public Long getId() {
            return id;
        }

        public LocalDate getFecha() {
            return fecha;
        }

        public Object getMonto() {
            return monto;
        }

        public Long getContratoId() {
            return contratoId;
        }

        public String getTituloContrato() {
            return tituloContrato;
        }

        public Integer getNumeroPago() {
            return numeroPago;
        }

        public Integer getDiasVencido() {
            return diasVencido;
        }
    }

    public static class PagoRangoResponse {
        private LocalDate fecha;
        private Long contratoId;
        private String tituloContrato;
        private Object monto;

        // Getters
        public LocalDate getFecha() {
            return fecha;
        }

        public Long getContratoId() {
            return contratoId;
        }

        public String getTituloContrato() {
            return tituloContrato;
        }

        public Object getMonto() {
            return monto;
        }
    }

    public static class VerificacionFechaResponse {
        private LocalDate fecha;
        private Boolean tienePagos;

        public VerificacionFechaResponse(LocalDate fecha, Boolean tienePagos) {
            this.fecha = fecha;
            this.tienePagos = tienePagos;
        }

        public LocalDate getFecha() {
            return fecha;
        }

        public Boolean getTienePagos() {
            return tienePagos;
        }
    }

    public static class ContratoFechaResponse {
        private LocalDate fecha;
        private Object contrato;
        private String mensaje;

        public ContratoFechaResponse(LocalDate fecha, Object contrato, String mensaje) {
            this.fecha = fecha;
            this.contrato = contrato;
            this.mensaje = mensaje;
        }

        public LocalDate getFecha() {
            return fecha;
        }

        public Object getContrato() {
            return contrato;
        }

        public String getMensaje() {
            return mensaje;
        }
    }
}
