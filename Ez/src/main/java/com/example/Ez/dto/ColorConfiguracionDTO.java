package com.example.Ez.dto;

import java.time.LocalDateTime;

public class ColorConfiguracionDTO {
    private Long id;
    private Integer usuarioId;
    private String colorPrincipal;
    private String colorAcompañante;
    private String nombreTema;
    private String urlLogo;
    private Boolean activo;
    private LocalDateTime fechaCreacion;

    // Constructores
    public ColorConfiguracionDTO() {
    }

    public ColorConfiguracionDTO(String colorPrincipal, String colorAcompañante) {
        this.colorPrincipal = colorPrincipal;
        this.colorAcompañante = colorAcompañante;
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

    public String getColorPrincipal() {
        return colorPrincipal;
    }

    public void setColorPrincipal(String colorPrincipal) {
        this.colorPrincipal = colorPrincipal;
    }

    public String getColorAcompañante() {
        return colorAcompañante;
    }

    public void setColorAcompañante(String colorAcompañante) {
        this.colorAcompañante = colorAcompañante;
    }

    public String getNombreTema() {
        return nombreTema;
    }

    public void setNombreTema(String nombreTema) {
        this.nombreTema = nombreTema;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
