package com.example.Ez.service;

import com.example.Ez.model.Transaccion;
import com.example.Ez.model.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@Transactional
public class MercadoPagoService {

    private final TransaccionService transaccionService;
    private final SaldoService saldoService;

    /**
     * INTEGRACIÓN DE MERCADOPAGO - ESPACIO RESERVADO
     * 
     * Aquí se debe integrar el SDK oficial de Mercado Pago:
     * - Dependencia Maven a agregar: com.mercadopago:sdk-java
     * - Configurar API key en application.properties
     * 
     * Métodos pendientes a implementar:
     * 1. crearPago(usuario, monto, descripcion) -> String (paymentId)
     * 2. verificarEstadoPago(paymentId) -> String (estado)
     * 3. procesarWebhookIPN(requestBody) -> verificar y actualizar BD
     * 4. obtenerDetallesPago(paymentId) -> PaymentDetails
     */

    public MercadoPagoService(TransaccionService transaccionService, SaldoService saldoService) {
        this.transaccionService = transaccionService;
        this.saldoService = saldoService;
    }

    /**
     * ESPACIO RESERVADO: Crear un pago en Mercado Pago
     * 
     * @param usuario Usuario que realiza el pago
     * @param monto Cantidad a pagar
     * @param descripcion Descripción del pago (ej: "Recarga de saldo")
     * @return ID de la transacción de Mercado Pago
     */
    public String crearPago(Usuario usuario, BigDecimal monto, String descripcion) {
        // TODO: Integrar SDK de Mercado Pago
        // Pasos:
        // 1. Crear MercadoPagoClient con API key
        // 2. Crear PaymentRequest con los datos
        // 3. Ejecutar pago
        // 4. Registrar transacción en BD

        // Por ahora retorna null
        return null;
    }

    /**
     * ESPACIO RESERVADO: Verificar el estado de un pago
     * 
     * @param paymentId ID del pago en Mercado Pago
     * @return Estado del pago (PENDING, APPROVED, REJECTED, CANCELLED)
     */
    public String verificarEstadoPago(String paymentId) {
        // TODO: Integrar SDK de Mercado Pago
        // Pasos:
        // 1. Obtener detalles del pago
        // 2. Retornar estado
        return null;
    }

    /**
     * ESPACIO RESERVADO: Procesar Webhook IPN de Mercado Pago
     * 
     * Llamada automática cuando el estado del pago cambia
     * 
     * @param requestBody JSON del webhook
     */
    public void procesarWebhookIPN(String requestBody) {
        // TODO: Integrar SDK de Mercado Pago
        // Pasos:
        // 1. Parsear JSON del webhook
        // 2. Verificar firma digital
        // 3. Obtener ID de transacción
        // 4. Buscar la transacción en BD
        // 5. Actualizar estado
        // 6. Si APPROVED:
        //    - Actualizar saldo del usuario
        //    - Enviar correo de confirmación
        // 7. Si REJECTED:
        //    - Actualizar estado de transacción
        //    - Enviar correo de rechazo
    }

    /**
     * ESPACIO RESERVADO: Obtener detalles completos de un pago
     */
    public Object obtenerDetallesPago(String paymentId) {
        // TODO: Integrar SDK de Mercado Pago
        return null;
    }

    /**
     * Simula una transaccion de prueba (para testing sin API real)
     * 
     * @param usuario Usuario
     * @param monto Monto
     * @return ID de transacción
     */
    public String crearPagoSimulado(Usuario usuario, BigDecimal monto, String descripcion) {
        // Crear transacción inicial
        var transaccion = transaccionService.crearTransaccion(
                usuario,
                Transaccion.TipoTransaccion.RECARGA,
                monto,
                Transaccion.MetodoPago.MERCADOPAGO,
                descripcion
        );

        // Retornar ID simulado
        return "MP-SIM-" + transaccion.getId();
    }

    /**
     * Simula la confirmación de un pago (para testing sin API real)
     */
    public void confirmarPagoSimulado(Long transaccionId, Usuario usuario, BigDecimal monto) {
        try {
            // Actualizar transacción
            transaccionService.actualizarEstado(transaccionId, Transaccion.EstadoTransaccion.COMPLETADA, "SIM-APPROVED");
            
            // Recargar saldo
            saldoService.recargar(usuario, monto);
        } catch (Exception e) {
            System.err.println("Error en pago simulado: " + e.getMessage());
        }
    }
}
