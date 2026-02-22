package com.example.Ez.controller;

import com.example.Ez.model.Usuario;
import com.example.Ez.service.UsuarioService;
import com.example.Ez.dto.ApiResponse;
import com.example.Ez.dto.LoginRequest;
import com.example.Ez.dto.LoginResponse;
import com.example.Ez.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000", "http://localhost:8888"}, 
             allowCredentials = "true")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Login REST API
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        logger.info("=== REST API LOGIN ===");
        logger.info("Email: {}", loginRequest.getEmail());
        
        try {
            if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty() ||
                loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Email y contraseña son obligatorios", 
                        "Parámetros incompletos"));
            }

            Optional<Usuario> usuarioOpt = usuarioService.autenticar(loginRequest.getEmail(), loginRequest.getPassword());
            
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                logger.info("Usuario autenticado: {} {}", usuario.getNombre(), usuario.getApellido());

                // Obtener rol principal
                String rolPrincipal = "USUARIO";
                if (usuario.getRoles() != null && !usuario.getRoles().isEmpty()) {
                    rolPrincipal = usuario.getRoles().iterator().next().getTipoRol().toString();
                }

                // Guardar en sesión para compatibilidad
                session.setAttribute("id_usuario", usuario.getId());
                session.setAttribute("email_usuario", usuario.getEmail());
                session.setAttribute("nombre_usuario", usuario.getNombre());
                session.setAttribute("apellido_usuario", usuario.getApellido());
                session.setAttribute("nombre_completo", usuario.getNombre() + " " + usuario.getApellido());
                session.setAttribute("rol_usuario", rolPrincipal);

                // Guardar atributos específicos por rol
                if ("ADMIN".equalsIgnoreCase(rolPrincipal)) {
                    session.setAttribute("id_admin", usuario.getId());
                    session.setAttribute("email_admin", usuario.getEmail());
                    session.setAttribute("nombre_completo_admin", usuario.getNombre() + " " + usuario.getApellido());
                } else if ("INGENIERO".equalsIgnoreCase(rolPrincipal)) {
                    session.setAttribute("id_ingeniero", usuario.getId());
                    session.setAttribute("email_ingeniero", usuario.getEmail());
                    session.setAttribute("nombre_completo_ingeniero", usuario.getNombre() + " " + usuario.getApellido());
                } else {
                    session.setAttribute("id_cliente", usuario.getId());
                    session.setAttribute("email_cliente", usuario.getEmail());
                    session.setAttribute("nombre_completo_cliente", usuario.getNombre() + " " + usuario.getApellido());
                }

                LoginResponse response = new LoginResponse(usuario, rolPrincipal);
                return ResponseEntity.ok(
                    new ApiResponse<>(true, "Autenticación exitosa", response));
            } else {
                logger.warn("Login fallido: credenciales inválidas para {}", loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Credenciales inválidas", 
                        "Email o contraseña incorrectos"));
            }
        } catch (Exception e) {
            logger.error("Error al procesar login: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error al procesar autenticación", e.getMessage()));
        }
    }

    /**
     * Logout REST API
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        try {
            session.invalidate();
            return ResponseEntity.ok(
                new ApiResponse<>(true, "Sesión cerrada exitosamente", null));
        } catch (Exception e) {
            logger.error("Error al cerrar sesión: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error al cerrar sesión", e.getMessage()));
        }
    }

    /**
     * Obtener datos del usuario autenticado
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<LoginResponse>> getCurrentUser(HttpSession session) {
        try {
            Long usuarioId = (Long) session.getAttribute("id_usuario");
            String rol = (String) session.getAttribute("rol_usuario");

            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "No autenticado", "Usuario no tiene sesión activa"));
            }

            Optional<Usuario> usuarioOpt = usuarioService.obtenerPorId(usuarioId);
            if (usuarioOpt.isPresent()) {
                LoginResponse response = new LoginResponse(usuarioOpt.get(), rol != null ? rol : "USUARIO");
                return ResponseEntity.ok(
                    new ApiResponse<>(true, "Datos de usuario obtenidos", response));
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "Usuario no encontrado", 
                    "El usuario no existe en el sistema"));
        } catch (Exception e) {
            logger.error("Error al obtener usuario actual: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error al obtener datos", e.getMessage()));
        }
    }

    /**
     * Registro REST API
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponse>> register(@RequestBody RegisterRequest registerRequest) {
        logger.info("=== REST API REGISTRO ===");
        logger.info("Email: {}", registerRequest.getEmail());

        try {
            // Validación de campos
            if (registerRequest.getNombre() == null || registerRequest.getNombre().trim().isEmpty() || 
                registerRequest.getNombre().length() < 2) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Validación fallida", 
                        "El nombre debe tener al menos 2 caracteres"));
            }

            if (registerRequest.getApellido() == null || registerRequest.getApellido().trim().isEmpty() || 
                registerRequest.getApellido().length() < 2) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Validación fallida", 
                        "El apellido debe tener al menos 2 caracteres"));
            }

            if (!usuarioService.emailValido(registerRequest.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Validación fallida", 
                        "El correo debe ser @gmail.com"));
            }

            if (!usuarioService.contrasenaValida(registerRequest.getPassword())) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Validación fallida", 
                        "La contraseña debe tener entre 8 y 16 caracteres"));
            }

            if (registerRequest.getTipoPerfil() == null || 
                (registerRequest.getTipoPerfil() != 1 && registerRequest.getTipoPerfil() != 2 && 
                 registerRequest.getTipoPerfil() != 3)) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Validación fallida", 
                        "Tipo de perfil inválido"));
            }

            if (registerRequest.getGenero() == null || 
                registerRequest.getGenero() < 1 || registerRequest.getGenero() > 4) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Validación fallida", 
                        "Género inválido"));
            }

            LocalDate fecha;
            try {
                fecha = LocalDate.parse(registerRequest.getFechaNacimiento(), 
                    DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (Exception e) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Validación fallida", 
                        "Fecha de nacimiento inválida. Formato: YYYY-MM-DD"));
            }

            // Registrar usuario
            Usuario nuevoUsuario = usuarioService.registrarUsuario(
                registerRequest.getNombre(),
                registerRequest.getApellido(),
                registerRequest.getCedula(),
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                registerRequest.getTipoPerfil(),
                registerRequest.getGenero(),
                fecha
            );

            if (nuevoUsuario == null) {
                return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Registro fallido", 
                        "El correo o cédula ya están registrados"));
            }

            String rol = "USUARIO";
            if (nuevoUsuario.getRoles() != null && !nuevoUsuario.getRoles().isEmpty()) {
                rol = nuevoUsuario.getRoles().iterator().next().getTipoRol().toString();
            }

            LoginResponse response = new LoginResponse(nuevoUsuario, rol);
            logger.info("Usuario registrado exitosamente: {}", nuevoUsuario.getEmail());
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Registro exitoso", response));

        } catch (Exception e) {
            logger.error("Error al registrar usuario: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error al registrar", e.getMessage()));
        }
    }

    /**
     * Verificar si el usuario está autenticado
     */
    @GetMapping("/check")
    public ResponseEntity<ApiResponse<Boolean>> checkAuthentication(HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("id_usuario");
        boolean isAuthenticated = usuarioId != null;
        
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Estado de autenticación", isAuthenticated));
    }
}
