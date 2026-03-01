package com.example.Ez.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "contrato_pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContratoPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    @Column(name = "numero_pago", nullable = false)
    private Integer numeroPago;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estado = EstadoPago.PENDIENTE;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    public enum EstadoPago {
        PENDIENTE, PAGADO, VENCIDO, CANCELADO
    }
}
