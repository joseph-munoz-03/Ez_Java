package com.example.Ez.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ContratoDTO {
    private Long id;
    private Integer ingenieroId;
    private String nombreIngeniero;
    private Integer usuarioId;
    private String nombreUsuario;
    private String titulo;
    private String descripcion;
    private BigDecimal valor;
    private LocalDate fechaInicio;
    private Integer cantidadDias;
    private Integer cantidadPagos;
    private String estado;
    private String especificaciones;
    private String urlContratoPdf;
    private LocalDateTime fechaCreacion;
    private List<String> pagosState;

    // Constructores
    public ContratoDTO() {
    }

    public ContratoDTO(Long id, String titulo, BigDecimal valor, String estado) {
        this.id = id;
        this.titulo = titulo;
        this.valor = valor;
        this.estado = estado;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIngenieroId() {
        return ingenieroId;
    }

    public void setIngenieroId(Integer ingenieroId) {
        this.ingenieroId = ingenieroId;
    }

    public String getNombreIngeniero() {
        return nombreIngeniero;
    }

    public void setNombreIngeniero(String nombreIngeniero) {
        this.nombreIngeniero = nombreIngeniero;
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

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEspecificaciones() {
        return especificaciones;
    }

    public void setEspecificaciones(String especificaciones) {
        this.especificaciones = especificaciones;
    }

    public String getUrlContratoPdf() {
        return urlContratoPdf;
    }

    public void setUrlContratoPdf(String urlContratoPdf) {
        this.urlContratoPdf = urlContratoPdf;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<String> getPagosState() {
        return pagosState;
    }

    public void setPagosState(List<String> pagosState) {
        this.pagosState = pagosState;
    }
}
