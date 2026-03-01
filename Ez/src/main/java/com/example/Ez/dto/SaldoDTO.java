package com.example.Ez.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SaldoDTO {
    private Long id;
    private Integer usuarioId;
    private String nombreUsuario;
    private BigDecimal cantidad;
    private LocalDateTime ultimaActualizacion;
    private LocalDateTime fechaCreacion;

    // Constructores
    public SaldoDTO() {
    }

    public SaldoDTO(Long id, Integer usuarioId, BigDecimal cantidad) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.cantidad = cantidad;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
