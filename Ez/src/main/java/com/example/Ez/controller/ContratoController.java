package com.example.Ez.controller;

import com.example.Ez.dto.ContratoDTO;
import com.example.Ez.model.Usuario;
import com.example.Ez.service.ContratoService;
import com.example.Ez.service.UsuarioService;
import com.example.Ez.service.EmailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/contratos")
@CrossOrigin
public class ContratoController {

    private final ContratoService contratoService;
    private final UsuarioService usuarioService;
    private final EmailService emailService;

    public ContratoController(ContratoService contratoService, UsuarioService usuarioService,
                            EmailService emailService) {
        this.contratoService = contratoService;
        this.usuarioService = usuarioService;
        this.emailService = emailService;
    }

    /**
     * Crea un nuevo contrato
     * 
     * Cuando se crea un contrato:
     * 1. Se registra en la BD
     * 2. Se generan automáticamente los pagos
     * 3. Se envía correo a ambos usuarios
     * 4. Aparece en la sección de contratos
     */
    @PostMapping("/crear")
    public ResponseEntity<ContratoResponse> crearContrato(@RequestBody CreateContratoRequest request) {
        try {
            // TODO: Obtener usuario autenticado como ingeniero
            // Usuario ingeniero = usuarioService.obtenerUsuarioAutenticado();
            Usuario usuario = usuarioService.obtenerPorId((long) request.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            ContratoDTO contrato = contratoService.crearContrato(
                    null, // ingeniero
                    usuario,
                    request.getTitulo(),
                    request.getDescripcion(),
                    new BigDecimal(request.getValor()),
                    request.getFechaInicio(),
                    request.getCantidadDias(),
                    request.getCantidadPagos(),
                    request.getEspecificaciones()
            );

            // Enviar correos
            // emailService.enviarCorreoContratoCreado(...);

            return ResponseEntity.ok(new ContratoResponse("Contrato creado exitosamente", contrato));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ContratoResponse("Error: " + e.getMessage(), null));
        }
    }

    /**
     * Obtiene los contratos del usuario autenticado
     */
    @GetMapping("/mis-contratos")
    public ResponseEntity<List<ContratoDTO>> obtenerMisContratos() {
        // TODO: Obtener usuario autenticado
        return ResponseEntity.badRequest().build();
    }

    /**
     * Obtiene los contratos paginados
     */
    @GetMapping("/mis-contratos/paginado")
    public ResponseEntity<Page<ContratoDTO>> obtenerMisContratosPaginado(Pageable pageable) {
        // TODO: Obtener usuario autenticado
        return ResponseEntity.badRequest().build();
    }

    /**
     * Obtiene un contrato específico
     */
    @GetMapping("/{contratoId}")
    public ResponseEntity<ContratoDTO> obtenerContrato(@PathVariable Long contratoId) {
        try {
            ContratoDTO contrato = contratoService.obtenerContrato(contratoId);
            return ResponseEntity.ok(contrato);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Actualiza el estado del contrato
     */
    @PutMapping("/{contratoId}/estado")
    public ResponseEntity<ContratoDTO> actualizarEstado(@PathVariable Long contratoId,
                                                        @RequestBody CambiarEstadoRequest request) {
        try {
            ContratoDTO contrato = contratoService.actualizarEstado(contratoId, 
                    com.example.Ez.model.Contrato.EstadoContrato.valueOf(request.getNuevoEstado()));
            return ResponseEntity.ok(contrato);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene contratos con pagos pendientes
     */
    @GetMapping("/mis-contratos/pagos-pendientes")
    public ResponseEntity<List<ContratoDTO>> obtenerContratosPendientes() {
        // TODO: Obtener usuario autenticado
        return ResponseEntity.badRequest().build();
    }

    // ===== PAYLOADS =====

    public static class CreateContratoRequest {
        private Integer usuarioId;
        private String titulo;
        private String descripcion;
        private String valor;
        private LocalDate fechaInicio;
        private Integer cantidadDias;
        private Integer cantidadPagos;
        private String especificaciones;

        // Getters y Setters
        public Integer getUsuarioId() {
            return usuarioId;
        }

        public void setUsuarioId(Integer usuarioId) {
            this.usuarioId = usuarioId;
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getValor() {
            return valor;
        }

        public void setValor(String valor) {
            this.valor = valor;
        }

        public LocalDate getFechaInicio() {
            return fechaInicio;
        }

        public void setFechaInicio(LocalDate fechaInicio) {
            this.fechaInicio = fechaInicio;
        }

        public Integer getCantidadDias() {
            return cantidadDias;
        }

        public void setCantidadDias(Integer cantidadDias) {
            this.cantidadDias = cantidadDias;
        }

        public Integer getCantidadPagos() {
            return cantidadPagos;
        }

        public void setCantidadPagos(Integer cantidadPagos) {
            this.cantidadPagos = cantidadPagos;
        }

        public String getEspecificaciones() {
            return especificaciones;
        }

        public void setEspecificaciones(String especificaciones) {
            this.especificaciones = especificaciones;
        }
    }

    public static class CambiarEstadoRequest {
        private String nuevoEstado;

        public String getNuevoEstado() {
            return nuevoEstado;
        }

        public void setNuevoEstado(String nuevoEstado) {
            this.nuevoEstado = nuevoEstado;
        }
    }

    public static class ContratoResponse {
        private String mensaje;
        private ContratoDTO contrato;

        public ContratoResponse(String mensaje, ContratoDTO contrato) {
            this.mensaje = mensaje;
            this.contrato = contrato;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public ContratoDTO getContrato() {
            return contrato;
        }

        public void setContrato(ContratoDTO contrato) {
            this.contrato = contrato;
        }
    }
}
