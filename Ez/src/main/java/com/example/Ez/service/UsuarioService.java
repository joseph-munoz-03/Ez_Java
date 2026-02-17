package com.example.Ez.service;

import com.example.Ez.model.Usuario;
import com.example.Ez.model.Rol;
import com.example.Ez.repository.UsuarioRepository;
import com.example.Ez.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Autentica un usuario por email y contraseña
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @return Optional con el usuario si es válido, vacío en caso contrario
     */
    public Optional<Usuario> autenticar(String email, String password) {
        logger.debug("Iniciando autenticación para email: {}", email);
        
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        
        if (usuario.isPresent()) {
            logger.debug("Usuario encontrado en BD: {}", email);
            Usuario usuarioEncontrado = usuario.get();
            logger.debug("Estado del usuario: {}", usuarioEncontrado.getEstado());
            
            boolean coincideContrasena = passwordEncoder.matches(password, usuarioEncontrado.getContrasena());
            logger.debug("Contraseña correcta: {}", coincideContrasena);
            
            if (coincideContrasena) {
                logger.info("Autenticación exitosa para usuario: {}", email);
                return usuario;
            } else {
                logger.warn("Contraseña incorrecta para usuario: {}", email);
                return Optional.empty();
            }
        } else {
            logger.warn("Usuario no encontrado en BD: {}", email);
            return Optional.empty();
        }
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
        
        try {
            // Verificar si el email ya existe
            if (usuarioRepository.findByEmail(email).isPresent()) {
                return null;
            }
            
            // Verificar si la cédula ya existe
            if (cedula != null && usuarioRepository.findByDocumentoUser(cedula).isPresent()) {
                return null;
            }
            
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setDocumentoUser(cedula);
            usuario.setEmail(email);
            usuario.setContrasena(passwordEncoder.encode(password));
            usuario.setFechaNacimiento(fechaNacimiento);
            
            // Mapear género
            switch (genero) {
                case 1:
                    usuario.setGenero(Usuario.Genero.Masculino);
                    break;
                case 2:
                    usuario.setGenero(Usuario.Genero.Femenino);
                    break;
                case 3:
                    usuario.setGenero(Usuario.Genero.No_Especificado);
                    break;
                case 4:
                    usuario.setGenero(Usuario.Genero.Otro);
                    break;
                default:
                    usuario.setGenero(Usuario.Genero.No_Especificado);
            }
            
            // Asignar rol según tipo de perfil
            Set<Rol> roles = new HashSet<>();
            if (tipoPerfil == 1) {
                // Ingeniero
                Optional<Rol> rolIngeniero = rolRepository.findByTipoRol(Rol.TipoRol.INGENIERO);
                if (rolIngeniero.isPresent()) {
                    roles.add(rolIngeniero.get());
                }
            } else if (tipoPerfil == 2) {
                // Cliente/Usuario
                Optional<Rol> rolUsuario = rolRepository.findByTipoRol(Rol.TipoRol.USUARIO);
                if (rolUsuario.isPresent()) {
                    roles.add(rolUsuario.get());
                }
            } else if (tipoPerfil == 3) {
                // Admin
                Optional<Rol> rolAdmin = rolRepository.findByTipoRol(Rol.TipoRol.ADMIN);
                if (rolAdmin.isPresent()) {
                    roles.add(rolAdmin.get());
                }
            }
            
            // Asignar roles (pueden estar vacíos si no existen en la BD)
            usuario.setRoles(roles);
            usuario.setEstado(Usuario.Estado.activo);
            
            return usuarioRepository.save(usuario);
        } catch (Exception e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Verifica si un usuario es administrador
     * @param usuario Usuario a verificar
     * @return true si es administrador
     */
    public boolean esAdministrador(Usuario usuario) {
        return usuario.getRoles().stream()
                .anyMatch(rol -> rol.getTipoRol() == Rol.TipoRol.ADMIN);
    }

    /**
     * Verifica si un usuario es ingeniero
     * @param usuario Usuario a verificar
     * @return true si es ingeniero
     */
    public boolean esIngeniero(Usuario usuario) {
        return usuario.getRoles().stream()
                .anyMatch(rol -> rol.getTipoRol() == Rol.TipoRol.INGENIERO);
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

    /**
     * Obtiene todos los usuarios
     * @return Lista de todos los usuarios
     */
    public List<Usuario> obtenerTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Filtra usuarios por múltiples criterios
     * @param criterio Texto de búsqueda (nombre, email, cédula)
     * @param filtroTipo Tipo de filtro (nombre, email, cedula, perfil, genero, estado)
     * @return Lista de usuarios filtrados
     */
    public List<Usuario> filtrarUsuarios(String criterio, String filtroTipo) {
        List<Usuario> usuarios = usuarioRepository.findAll();

        if (criterio == null || criterio.isBlank()) {
            return usuarios;
        }

        return usuarios.stream().filter(usuario -> {
            switch (filtroTipo.toLowerCase()) {
                case "nombre":
                    return usuario.getNombre().toLowerCase().contains(criterio.toLowerCase()) ||
                           usuario.getApellido().toLowerCase().contains(criterio.toLowerCase());
                case "email":
                    return usuario.getEmail().toLowerCase().contains(criterio.toLowerCase());
                case "cedula":
                    return usuario.getDocumentoUser() != null && 
                           usuario.getDocumentoUser().toString().contains(criterio);
                case "perfil":
                    return usuario.getRoles().stream()
                           .anyMatch(rol -> rol.getTipoRol().toString().toLowerCase()
                           .contains(criterio.toLowerCase()));
                case "genero":
                    return usuario.getGenero() != null && 
                           usuario.getGenero().toString().toLowerCase().contains(criterio.toLowerCase());
                case "estado":
                    return usuario.getEstado() != null && 
                           usuario.getEstado().toString().toLowerCase().contains(criterio.toLowerCase());
                default:
                    return false;
            }
        }).collect(Collectors.toList());
    }

    /**
     * Suspende un usuario por X días
     * @param usuarioId ID del usuario a suspender
     * @param dias Número de días de suspensión
     * @return Usuario suspendido o null si no existe
     */
    public Usuario suspenderUsuario(Integer usuarioId, Integer dias) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEstado(Usuario.Estado.suspendido);
            usuario.setFechaSuspension(LocalDate.now());
            usuario.setDiasSuspension(dias);
            return usuarioRepository.save(usuario);
        }
        return null;
    }

    /**
     * Reactiva un usuario suspendido
     * @param usuarioId ID del usuario a reactivar
     * @return Usuario reactivado o null si no existe
     */
    public Usuario reactivarUsuario(Integer usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEstado(Usuario.Estado.activo);
            usuario.setFechaSuspension(null);
            usuario.setDiasSuspension(null);
            return usuarioRepository.save(usuario);
        }
        return null;
    }

    /**
     * Banea un usuario
     * @param usuarioId ID del usuario a banear
     * @return Usuario baneado o null si no existe
     */
    public Usuario banearUsuario(Integer usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEstado(Usuario.Estado.baneado);
            return usuarioRepository.save(usuario);
        }
        return null;
    }

    /**
     * Inactiva un usuario (soft delete)
     * @param usuarioId ID del usuario a inactivar
     * @return Usuario inactivado o null si no existe
     */
    public Usuario inactivarUsuario(Integer usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEstado(Usuario.Estado.inactivo);
            return usuarioRepository.save(usuario);
        }
        return null;
    }

    /**
     * Actualiza los datos de un usuario
     * @param usuarioId ID del usuario
     * @param nombre Nuevo nombre
     * @param apellido Nuevo apellido
     * @param email Nuevo email
     * @param telefono Nuevo teléfono
     * @param genero Nuevo género
     * @return Usuario actualizado o null si no existe
     */
    public Usuario actualizarUsuario(Integer usuarioId, String nombre, String apellido, 
                                     String email, String telefono, Usuario.Genero genero) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (nombre != null && !nombre.isBlank()) {
                usuario.setNombre(nombre);
            }
            if (apellido != null && !apellido.isBlank()) {
                usuario.setApellido(apellido);
            }
            if (email != null && !email.isBlank()) {
                usuario.setEmail(email);
            }
            if (telefono != null && !telefono.isBlank()) {
                usuario.setTelefono(telefono);
            }
            if (genero != null) {
                usuario.setGenero(genero);
            }
            return usuarioRepository.save(usuario);
        }
        return null;
    }
}
