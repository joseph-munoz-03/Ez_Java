package com.example.Ez.dto;

import java.time.LocalDateTime;

public class MensajeDTO {
    private Long id;
    private Long chatId;
    private Integer remitentId;
    private String remitenteNombre;
    private String fotoPerfil;
    private String contenido;
    private String tipoContenido;
    private String urlArchivo;
    private LocalDateTime timestamp;
    private Boolean leido;

    // Constructores
    public MensajeDTO() {
    }

    public MensajeDTO(Long id, Long chatId, Integer remitentId, String contenido, String tipoContenido) {
        this.id = id;
        this.chatId = chatId;
        this.remitentId = remitentId;
        this.contenido = contenido;
        this.tipoContenido = tipoContenido;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Integer getRemitentId() {
        return remitentId;
    }

    public void setRemitentId(Integer remitentId) {
        this.remitentId = remitentId;
    }

    public String getRemitenteNombre() {
        return remitenteNombre;
    }

    public void setRemitenteNombre(String remitenteNombre) {
        this.remitenteNombre = remitenteNombre;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getTipoContenido() {
        return tipoContenido;
    }

    public void setTipoContenido(String tipoContenido) {
        this.tipoContenido = tipoContenido;
    }

    public String getUrlArchivo() {
        return urlArchivo;
    }

    public void setUrlArchivo(String urlArchivo) {
        this.urlArchivo = urlArchivo;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getLeido() {
        return leido;
    }

    public void setLeido(Boolean leido) {
        this.leido = leido;
    }
}
