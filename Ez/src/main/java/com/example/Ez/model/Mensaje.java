package com.example.Ez.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensaje")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "remitente_id", nullable = false)
    private Usuario remitente;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contenido")
    private TipoContenido tipoContenido = TipoContenido.TEXTO;

    @Column(name = "url_archivo")
    private String urlArchivo;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "leido")
    private Boolean leido = false;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    public enum TipoContenido {
        TEXTO, IMAGEN, DOCUMENTO, PDF, FORMULARIO_CONTRATO
    }
}
