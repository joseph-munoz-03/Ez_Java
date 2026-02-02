package com.example.Ez.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users_ez", indexes = {
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_documento", columnList = "documento_user"),
    @Index(name = "idx_estado", columnList = "estado")
})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_users")
    private Integer id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(name = "documento_user", unique = true)
    private Long documentoUser;

    @Email(message = "El email debe ser válido")
    @NotBlank(message = "El email no puede estar vacío")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(nullable = false, length = 255)
    private String contrasena;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero", columnDefinition = "VARCHAR(20)")
    private Genero genero;

    @Column(length = 20)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "VARCHAR(20)")
    private Estado estado;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "fecha_suspension")
    private LocalDate fechaSuspension;

    @Column(name = "dias_suspension")
    private Integer diasSuspension;

    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinTable(
        name = "usuarios_roles",
        joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "id_users"),
        inverseJoinColumns = @JoinColumn(name = "id_rol", referencedColumnName = "id_rol")
    )
    private Set<Rol> roles = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        if (this.genero == null) {
            this.genero = Genero.No_Especificado;
        }
        if (this.estado == null) {
            this.estado = Estado.activo;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Long getDocumentoUser() {
        return documentoUser;
    }

    public void setDocumentoUser(Long documentoUser) {
        this.documentoUser = documentoUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }

    public enum Genero {
        Femenino,
        Masculino,
        No_Especificado,
        Otro;
    }

    public enum Estado {
        activo,
        inactivo,
        baneado,
        suspendido;
    }

    // Getters y Setters para suspensión
    public LocalDate getFechaSuspension() {
        return fechaSuspension;
    }

    public void setFechaSuspension(LocalDate fechaSuspension) {
        this.fechaSuspension = fechaSuspension;
    }

    public Integer getDiasSuspension() {
        return diasSuspension;
    }

    public void setDiasSuspension(Integer diasSuspension) {
        this.diasSuspension = diasSuspension;
    }
}
