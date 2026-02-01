package com.example.Ez.controller;

import com.example.Ez.model.Usuario;
import com.example.Ez.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // ===== LOGIN =====
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String email, 
                                @RequestParam String password,
                                Model model,
                                HttpSession session) {
        
        // Validar formato del email
        if (!usuarioService.emailValido(email)) {
            model.addAttribute("error", "El correo debe ser @gmail.com");
            return "principal/login";
        }
        
        // Validar longitud de contraseña
        if (!usuarioService.contrasenaValida(password)) {
            model.addAttribute("error", "La contraseña debe tener entre 8 y 16 caracteres");
            return "principal/login";
        }
        
        // Autenticar usuario
        Optional<Usuario> usuarioOpt = usuarioService.autenticar(email, password);
        
        if (usuarioOpt.isEmpty()) {
            model.addAttribute("error", "Correo o contraseña inválidos");
            return "principal/login";
        }
        
        Usuario usuario = usuarioOpt.get();
        
        // Verificar si es administrador
        if (usuarioService.esAdministrador(usuario)) {
            session.setAttribute("id_admin", usuario.getId());
            session.setAttribute("email_admin", usuario.getEmail());
            session.setAttribute("nombre_completo_admin", usuario.getNombre() + " " + usuario.getApellido());
            session.setAttribute("rol_usuario", "admin");
            return "redirect:/admin/dashboard";
        }
        
        // Verificar si es ingeniero
        if (usuarioService.esIngeniero(usuario)) {
            session.setAttribute("id_ingeniero", usuario.getId());
            session.setAttribute("email_ingeniero", usuario.getEmail());
            session.setAttribute("nombre_completo_ingeniero", usuario.getNombre() + " " + usuario.getApellido());
            session.setAttribute("rol_usuario", "ingeniero");
            return "redirect:/ingeniero/dashboard";
        }
        
        // Usuario común (cliente)
        session.setAttribute("id_cliente", usuario.getId());
        session.setAttribute("email_cliente", usuario.getEmail());
        session.setAttribute("nombre_completo_cliente", usuario.getNombre() + " " + usuario.getApellido());
        session.setAttribute("rol_usuario", "usuario");
        return "redirect:/usuario/dashboard";
    }

    // ===== REGISTRO =====
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
        
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty() || nombre.length() < 2) {
            model.addAttribute("error", "El nombre debe tener al menos 2 caracteres");
            return "principal/sign_in";
        }
        
        if (apellido == null || apellido.trim().isEmpty() || apellido.length() < 2) {
            model.addAttribute("error", "El apellido debe tener al menos 2 caracteres");
            return "principal/sign_in";
        }
        
        if (!usuarioService.emailValido(email)) {
            model.addAttribute("error", "El correo debe ser @gmail.com");
            return "principal/sign_in";
        }
        
        if (!usuarioService.contrasenaValida(password)) {
            model.addAttribute("error", "La contraseña debe tener entre 8 y 16 caracteres");
            return "principal/sign_in";
        }
        
        if (tipoPerfil == null || (tipoPerfil != 1 && tipoPerfil != 2)) {
            model.addAttribute("error", "Tipo de perfil inválido");
            return "principal/sign_in";
        }
        
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
        
        model.addAttribute("success", true);
        return "principal/sign_in";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
