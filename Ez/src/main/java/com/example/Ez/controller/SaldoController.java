package com.example.Ez.controller;

import com.example.Ez.dto.SaldoDTO;
import com.example.Ez.dto.TransaccionDTO;
import com.example.Ez.model.Transaccion;
import com.example.Ez.model.Usuario;
import com.example.Ez.service.SaldoService;
import com.example.Ez.service.TransaccionService;
import com.example.Ez.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/saldo")
@CrossOrigin
public class SaldoController {

    private final SaldoService saldoService;
    private final TransaccionService transaccionService;
    private final UsuarioService usuarioService;

    public SaldoController(SaldoService saldoService, TransaccionService transaccionService,
                         UsuarioService usuarioService) {
        this.saldoService = saldoService;
        this.transaccionService = transaccionService;
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene el saldo del usuario autenticado
     */
    @GetMapping("/mi-saldo")
    public ResponseEntity<SaldoDTO> obtenerMiSaldo() {
        // TODO: Obtener usuario autenticado del contexto
        return ResponseEntity.badRequest().build();
    }

    /**
     * Obtiene el saldo por ID de usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<SaldoDTO> obtenerSaldoPorUsuario(@PathVariable Long usuarioId) {
        try {
            var saldo = saldoService.obtenerSaldoPorUsuarioId(usuarioId);
            return ResponseEntity.ok(saldo.orElseThrow());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Recarga el saldo
     */
    @PostMapping("/recargar")
    public ResponseEntity<ResponseMessage> recargar(@RequestBody RecargarRequest request) {
        try {
            // TODO: Obtener usuario autenticado
            // Usuario usuario = usuarioService.obtenerUsuarioAutenticado();
            
            BigDecimal monto = new BigDecimal(request.getMonto());
            
            // Crear transacción inicial
            // El micropago debe ser verificado en el webhook de Mercado Pago o Betplay
            var transaccion = transaccionService.crearTransaccion(
                    null, // usuario, 
                    Transaccion.TipoTransaccion.RECARGA,
                    monto,
                    Transaccion.MetodoPago.valueOf(request.getMetodoPago()),
                    "Recarga de saldo"
            );

            return ResponseEntity.ok(new ResponseMessage("Recarga iniciada. ID transacción: " + transaccion.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    /**
     * ESPACIO RESERVADO: Retirar saldo
     * 
     * Este endpoint iniciará el proceso de retiro
     * Si usa Mercado Pago, se debe configura la URL de retiro del MercadoPago
     */
    @PostMapping("/retirar")
    public ResponseEntity<ResponseMessage> retirar(@RequestBody RetirarRequest request) {
        try {
            // TODO: Obtener usuario autenticado
            // Usuario usuario = usuarioService.obtenerUsuarioAutenticado();
            
            BigDecimal monto = new BigDecimal(request.getMonto());
            
            // Crear transacción  de retiro
            var transaccion = transaccionService.crearTransaccion(
                    null, // usuario,
                    Transaccion.TipoTransaccion.RETIRO,
                    monto,
                    Transaccion.MetodoPago.MERCADOPAGO,
                    "Retiro a cuenta: " + request.getNumeroCuenta()
            );

            return ResponseEntity.ok(new ResponseMessage("Retiro procesado. ID transacción: " + transaccion.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    /**
     * Obtiene el historial de transacciones
     */
    @GetMapping("/historial")
    public ResponseEntity<List<TransaccionDTO>> obtenerHistorial() {
        // TODO: Obtener usuario autenticado
        return ResponseEntity.badRequest().build();
    }

    /**
     * Obtiene el historial paginado
     */
    @GetMapping("/historial/paginado")
    public ResponseEntity<Page<TransaccionDTO>> obtenerHistorialPaginado(Pageable pageable) {
        // TODO: Obtener usuario autenticado
        return ResponseEntity.badRequest().build();
    }

    /**
     * Obtiene transacciones completadas
     */
    @GetMapping("/transacciones-completadas")
    public ResponseEntity<List<TransaccionDTO>> obtenerTransaccionesCompletadas() {
        // TODO: Obtener usuario autenticado
        return ResponseEntity.badRequest().build();
    }

    // ===== PAYLOADS =====

    public static class RecargarRequest {
        private String monto;
        private String metodoPago; // MERCADOPAGO, BETPLAY

        public String getMonto() {
            return monto;
        }

        public void setMonto(String monto) {
            this.monto = monto;
        }

        public String getMetodoPago() {
            return metodoPago;
        }

        public void setMetodoPago(String metodoPago) {
            this.metodoPago = metodoPago;
        }
    }

    public static class RetirarRequest {
        private String monto;
        private String numeroCuenta;

        public String getMonto() {
            return monto;
        }

        public void setMonto(String monto) {
            this.monto = monto;
        }

        public String getNumeroCuenta() {
            return numeroCuenta;
        }

        public void setNumeroCuenta(String numeroCuenta) {
            this.numeroCuenta = numeroCuenta;
        }
    }

    public static class ResponseMessage {
        private String mensaje;

        public ResponseMessage(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }
    }
}
