package com.example.Ez.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "chat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario1_id", nullable = false)
    private Usuario usuario1;

    @ManyToOne
    @JoinColumn(name = "usuario2_id", nullable = false)
    private Usuario usuario2;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Mensaje> mensajes;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        ultimaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        ultimaActualizacion = LocalDateTime.now();
    }
}
