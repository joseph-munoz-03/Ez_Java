package com.example.Ez.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "color_configuracion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColorConfiguracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "color_principal", nullable = false)
    private ColorPrincipal colorPrincipal = ColorPrincipal.BLANCO;

    @Enumerated(EnumType.STRING)
    @Column(name = "color_acompañante", nullable = false)
    private ColorAcompañante colorAcompañante = ColorAcompañante.VERDE;

    @Column(name = "activo")
    private Boolean activo = true;

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

    public enum ColorPrincipal {
        BLANCO, GRIS, NEGRO
    }

    public enum ColorAcompañante {
        VERDE, MORADO, AZUL, ROJO, GRIS
    }
}
