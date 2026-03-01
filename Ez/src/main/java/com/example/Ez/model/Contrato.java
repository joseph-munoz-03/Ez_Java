package com.example.Ez.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "contrato")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contrato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ingeniero_id", nullable = false)
    private Usuario ingeniero;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "cantidad_dias", nullable = false)
    private Integer cantidadDias;

    @Column(name = "cantidad_pagos", nullable = false)
    private Integer cantidadPagos;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoContrato estado = EstadoContrato.PENDIENTE;

    @Column(name = "especificaciones", columnDefinition = "JSON")
    private String especificaciones;

    @Column(name = "url_contrato_pdf")
    private String urlContratoPdf;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ContratoPago> contratosPagos;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    public enum EstadoContrato {
        ACTIVO, PENDIENTE, COMPLETADO, CANCELADO
    }
}
