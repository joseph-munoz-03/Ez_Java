package com.example.Ez.service;

import com.example.Ez.dto.UsuarioDTO;
import com.example.Ez.model.Usuario;
import com.example.Ez.model.Rol;
import com.example.Ez.repository.UsuarioRepository;
import com.example.Ez.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    /**
     * Autentica un usuario por email y contraseña
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @return Optional con el usuario si es válido, vacío en caso contrario
     */
    public Optional<Usuario> autenticar(String email, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        
        if (usuario.isPresent() && usuario.get().getContrasena().equals(password)) {
            return usuario;
        }
        
        return Optional.empty();
    }

    /**
     * Registra un nuevo usuario
     * @param nombre Nombre del usuario
     * @param apellido Apellido del usuario
     * @param cedula Cédula del usuario
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @param tipoPerfil Tipo de perfil (1=ingeniero, 2=cliente)
     * @param genero Género (1=masculino, 2=femenino, 3=prefiero no decirlo, 4=otro)
     * @param fechaNacimiento Fecha de nacimiento
     * @return Usuario registrado o null si falla
     */
    public Usuario registrarUsuario(String nombre, String apellido, Long cedula, String email, 
                                    String password, Integer tipoPerfil, Integer genero, 
                                    LocalDate fechaNacimiento) {
        
        // Verificar si el email ya existe
        if (usuarioRepository.findByEmail(email).isPresent()) {
            return null;
        }
        
        // Verificar si la cédula ya existe
        if (cedula != null && usuarioRepository.findByCedula(cedula).isPresent()) {
            return null;
        }
        
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDocumentoUser(cedula);
        usuario.setEmail(email);
        usuario.setContrasena(password);
        usuario.setFechaNacimiento(fechaNacimiento);
        
        // Mapear género
        switch (genero) {
            case 1:
                usuario.setGenero(Usuario.Genero.MASCULINO);
                break;
            case 2:
                usuario.setGenero(Usuario.Genero.FEMENINO);
                break;
            case 3:
                usuario.setGenero(Usuario.Genero.NO_ESPECIFICADO);
                break;
            case 4:
                usuario.setGenero(Usuario.Genero.OTRO);
                break;
            default:
                usuario.setGenero(Usuario.Genero.NO_ESPECIFICADO);
        }
        
        // Asignar rol según tipo de perfil
        Set<Rol> roles = new HashSet<>();
        if (tipoPerfil == 1) {
            // Ingeniero
            Optional<Rol> rolIngeniero = rolRepository.findByTipoRol("ingeniero");
            rolIngeniero.ifPresent(roles::add);
        } else if (tipoPerfil == 2) {
            // Cliente/Usuario
            Optional<Rol> rolUsuario = rolRepository.findByTipoRol("usuario");
            rolUsuario.ifPresent(roles::add);
        }
        
        usuario.setRoles(roles);
        usuario.setEstado(Usuario.Estado.ACTIVO);
        
        return usuarioRepository.save(usuario);
    }

    /**
     * Verifica si un usuario es administrador
     * @param usuario Usuario a verificar
     * @return true si es administrador
     */
    public boolean esAdministrador(Usuario usuario) {
        return usuario.getRoles().stream()
                .anyMatch(rol -> rol.getTipo_rol().equals("admin"));
    }

    /**
     * Verifica si un usuario es ingeniero
     * @param usuario Usuario a verificar
     * @return true si es ingeniero
     */
    public boolean esIngeniero(Usuario usuario) {
        return usuario.getRoles().stream()
                .anyMatch(rol -> rol.getTipo_rol().equals("ingeniero"));
    }

    /**
     * Obtiene un usuario por email
     * @param email Email del usuario
     * @return Optional con el usuario si existe
     */
    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Obtiene un usuario por ID
     * @param id ID del usuario
     * @return Optional con el usuario si existe
     */
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(Math.toIntExact(id));
    }

    /**
     * Valida si el email es válido (debe ser @gmail.com)
     * @param email Email a validar
     * @return true si el email es válido
     */
    public boolean emailValido(String email) {
        return email != null && email.endsWith("@gmail.com");
    }

    /**
     * Valida si la contraseña cumple los requisitos (8-16 caracteres)
     * @param password Contraseña a validar
     * @return true si la contraseña es válida
     */
    public boolean contrasenaValida(String password) {
        return password != null && password.length() >= 8 && password.length() <= 16;
    }
}
