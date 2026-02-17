package com.example.Ez.controller;

import com.example.Ez.dto.LoginDTO;
import com.example.Ez.model.Usuario;
import com.example.Ez.model.Rol;
import com.example.Ez.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/")
public class PrincipalController {

    private static final Logger logger = LoggerFactory.getLogger(PrincipalController.class);

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String index() {
        return "principal/index";
    }

    @GetMapping("/login")
    public String login(HttpSession session, Model model) {
        // Si ya está autenticado, redirigir al dashboard
        if (session.getAttribute("id_usuario") != null) {
            String rol = (String) session.getAttribute("rol_usuario");
            return "redirect:/dashboard/" + rol.toLowerCase();
        }
        return "principal/login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String email, @RequestParam String password, 
                               HttpSession session, Model model) {
        
        logger.info("=== INTENTO DE LOGIN ===");
        logger.info("Email: {}", email);
        
        try {
            logger.debug("Buscando usuario con email: {}", email);
            Optional<Usuario> usuarioOpt = usuarioService.autenticar(email, password);
            
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                logger.info("Usuario autenticado: {} {}", usuario.getNombre(), usuario.getApellido());
                logger.debug("Roles del usuario: {}", usuario.getRoles());
                
                // Obtener el primer rol del usuario (si existen roles)
                String rolPrincipal = "USUARIO"; // Rol por defecto
                if (usuario.getRoles() != null && !usuario.getRoles().isEmpty()) {
                    rolPrincipal = usuario.getRoles().iterator().next().getTipoRol().toString();
                    logger.debug("Rol principal asignado: {}", rolPrincipal);
                } else {
                    logger.warn("Usuario {} no tiene roles asignados, se asigna USUARIO por defecto", email);
                }
                
                // Guardar información en sesión
                session.setAttribute("id_usuario", usuario.getId());
                session.setAttribute("email_usuario", usuario.getEmail());
                session.setAttribute("nombre_usuario", usuario.getNombre());
                session.setAttribute("apellido_usuario", usuario.getApellido());
                session.setAttribute("nombre_completo", usuario.getNombre() + " " + usuario.getApellido());
                session.setAttribute("rol_usuario", rolPrincipal);
                
                logger.info("Información de sesión guardada para usuario ID: {}", usuario.getId());
                
                // Guardar atributos específicos por rol para compatibilidad con controladores existentes
                if ("ADMIN".equalsIgnoreCase(rolPrincipal)) {
                    session.setAttribute("id_admin", usuario.getId());
                    session.setAttribute("email_admin", usuario.getEmail());
                    session.setAttribute("nombre_completo_admin", usuario.getNombre() + " " + usuario.getApellido());
                    logger.info("Redirigiendo ADMIN a /admin/dashboard");
                    return "redirect:/admin/dashboard";
                } else if ("INGENIERO".equalsIgnoreCase(rolPrincipal)) {
                    session.setAttribute("id_ingeniero", usuario.getId());
                    session.setAttribute("email_ingeniero", usuario.getEmail());
                    session.setAttribute("nombre_completo_ingeniero", usuario.getNombre() + " " + usuario.getApellido());
                    logger.info("Redirigiendo INGENIERO a /ingeniero/dashboard");
                    return "redirect:/ingeniero/dashboard";
                } else {
                    session.setAttribute("id_cliente", usuario.getId());
                    session.setAttribute("email_cliente", usuario.getEmail());
                    session.setAttribute("nombre_completo_cliente", usuario.getNombre() + " " + usuario.getApellido());
                    logger.info("Redirigiendo USUARIO a /usuario/dashboard");
                    return "redirect:/usuario/dashboard";
                }
            } else {
                logger.warn("Intento de login fallido: credenciales inválidas para email {}", email);
                model.addAttribute("error", "Email o contraseña incorrectos");
                return "principal/login";
            }
        } catch (Exception e) {
            logger.error("Error al procesar login para email {}: {}", email, e.getMessage(), e);
            model.addAttribute("error", "Error al procesar login: " + e.getMessage());
            return "principal/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/signin")
    public String signin() {
        return "principal/sign_in";
    }

    @PostMapping("/signin")
    public String procesarRegistro(@RequestParam String nombre,
                                   @RequestParam String apellido,
                                   @RequestParam Long cedula,
                                   @RequestParam String email,
                                   @RequestParam String password,
                                   @RequestParam Integer tipoPerfil,
                                   @RequestParam Integer genero,
                                   @RequestParam String fechaNacimiento,
                                   Model model) {
        
        try {
            // Validar nombre
            if (nombre == null || nombre.trim().isEmpty() || nombre.length() < 2) {
                model.addAttribute("error", "El nombre debe tener al menos 2 caracteres");
                return "principal/sign_in";
            }
            
            // Validar apellido
            if (apellido == null || apellido.trim().isEmpty() || apellido.length() < 2) {
                model.addAttribute("error", "El apellido debe tener al menos 2 caracteres");
                return "principal/sign_in";
            }
            
            // Validar email
            if (!usuarioService.emailValido(email)) {
                model.addAttribute("error", "El correo debe ser @gmail.com");
                return "principal/sign_in";
            }
            
            // Validar contraseña
            if (!usuarioService.contrasenaValida(password)) {
                model.addAttribute("error", "La contraseña debe tener entre 8 y 16 caracteres");
                return "principal/sign_in";
            }
            
            // Validar tipo de perfil
            if (tipoPerfil == null || (tipoPerfil != 1 && tipoPerfil != 2 && tipoPerfil != 3)) {
                model.addAttribute("error", "Tipo de perfil inválido");
                return "principal/sign_in";
            }
            
            // Validar género
            if (genero == null || genero < 1 || genero > 4) {
                model.addAttribute("error", "Género inválido");
                return "principal/sign_in";
            }
            
            // Parsear fecha
            LocalDate fecha;
            try {
                fecha = LocalDate.parse(fechaNacimiento, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (Exception e) {
                model.addAttribute("error", "Fecha de nacimiento inválida");
                return "principal/sign_in";
            }
            
            // Registrar usuario
            Usuario nuevoUsuario = usuarioService.registrarUsuario(
                nombre, apellido, cedula, email, password, tipoPerfil, genero, fecha
            );
            
            if (nuevoUsuario == null) {
                model.addAttribute("error", "El correo o cédula ya están registrados");
                return "principal/sign_in";
            }
            
            // Redirigir al login después del registro exitoso
            model.addAttribute("success", true);
            model.addAttribute("message", "¡Registro exitoso! Por favor inicia sesión");
            return "principal/login";
            
        } catch (Exception e) {
            System.err.println("Error al registrar: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al registrar el usuario. Por favor intenta más tarde.");
            return "principal/sign_in";
        }
    }

    @GetMapping("/usuarios-lista")
    public String listarUsuarios(Model model) {
        try {
            List<Usuario> usuarios = usuarioService.obtenerTodosUsuarios();
            model.addAttribute("usuarios", usuarios);
            return "usuarios-lista";
        } catch (Exception e) {
            model.addAttribute("error", "Error al obtener usuarios: " + e.getMessage());
            return "error";
        }
    }
}
