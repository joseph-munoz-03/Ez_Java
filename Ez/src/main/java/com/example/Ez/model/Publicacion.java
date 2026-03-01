package com.example.Ez.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "publicacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "base_datos")
    private String baseDatos;

    @Column(name = "lenguaje_programacion")
    private String lenguajeProgramacion;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPublicacion estado = EstadoPublicacion.ACTIVA;

    @Column(name = "imagenes", columnDefinition = "JSON")
    private String imagenes;

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

    public enum EstadoPublicacion {
        ACTIVA, INACTIVA, VENDIDA, ELIMINADA
    }
}
