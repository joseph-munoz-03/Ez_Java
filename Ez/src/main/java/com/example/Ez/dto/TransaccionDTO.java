package com.example.Ez.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransaccionDTO {
    private Long id;
    private Integer usuarioId;
    private String tipo;
    private BigDecimal cantidad;
    private String estado;
    private String metodoPago;
    private String referencia;
    private String descripcion;
    private LocalDateTime timestamp;

    // Constructores
    public TransaccionDTO() {
    }

    public TransaccionDTO(Long id, Integer usuarioId, String tipo, BigDecimal cantidad, String estado) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.estado = estado;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
