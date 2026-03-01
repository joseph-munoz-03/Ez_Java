package com.example.Ez.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaccion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransaccion tipo;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal cantidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTransaccion estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago")
    private MetodoPago metodoPago;

    @Column(name = "referencia_externa")
    private String referencia;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    public enum TipoTransaccion {
        RECARGA, RETIRO, PAGO_CONTRATO, COMISION
    }

    public enum EstadoTransaccion {
        PENDIENTE, COMPLETADA, RECHAZADA, CANCELADA
    }

    public enum MetodoPago {
        MERCADOPAGO, BETPLAY, TARJETA, TRANSFERENCIA, OTRO
    }
}
