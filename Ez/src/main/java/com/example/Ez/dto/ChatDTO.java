package com.example.Ez.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ChatDTO {
    private Long id;
    private Integer usuario1Id;
    private String usuario1Nombre;
    private String usuario1FotoPerfil;
    private Integer usuario2Id;
    private String usuario2Nombre;
    private String usuario2FotoPerfil;
    private LocalDateTime createdAt;
    private LocalDateTime ultimaActualizacion;
    private String ultimoMensaje;
    private Integer mensajesNnLleidos;
    private List<MensajeDTO> mensajes;

    // Constructores
    public ChatDTO() {
    }

    public ChatDTO(Long id, Integer usuario1Id, Integer usuario2Id) {
        this.id = id;
        this.usuario1Id = usuario1Id;
        this.usuario2Id = usuario2Id;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUsuario1Id() {
        return usuario1Id;
    }

    public void setUsuario1Id(Integer usuario1Id) {
        this.usuario1Id = usuario1Id;
    }

    public String getUsuario1Nombre() {
        return usuario1Nombre;
    }

    public void setUsuario1Nombre(String usuario1Nombre) {
        this.usuario1Nombre = usuario1Nombre;
    }

    public String getUsuario1FotoPerfil() {
        return usuario1FotoPerfil;
    }

    public void setUsuario1FotoPerfil(String usuario1FotoPerfil) {
        this.usuario1FotoPerfil = usuario1FotoPerfil;
    }

    public Integer getUsuario2Id() {
        return usuario2Id;
    }

    public void setUsuario2Id(Integer usuario2Id) {
        this.usuario2Id = usuario2Id;
    }

    public String getUsuario2Nombre() {
        return usuario2Nombre;
    }

    public void setUsuario2Nombre(String usuario2Nombre) {
        this.usuario2Nombre = usuario2Nombre;
    }

    public String getUsuario2FotoPerfil() {
        return usuario2FotoPerfil;
    }

    public void setUsuario2FotoPerfil(String usuario2FotoPerfil) {
        this.usuario2FotoPerfil = usuario2FotoPerfil;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    public void setUltimoMensaje(String ultimoMensaje) {
        this.ultimoMensaje = ultimoMensaje;
    }

    public Integer getMensajesNoLleidos() {
        return mensajesNnLleidos;
    }

    public void setMensajesNoLleidos(Integer mensajesNoLleidos) {
        this.mensajesNnLleidos = mensajesNoLleidos;
    }

    public List<MensajeDTO> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<MensajeDTO> mensajes) {
        this.mensajes = mensajes;
    }
}
