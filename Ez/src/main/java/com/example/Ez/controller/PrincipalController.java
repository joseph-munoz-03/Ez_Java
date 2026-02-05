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

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/")
public class PrincipalController {

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
        
        Optional<Usuario> usuarioOpt = usuarioService.autenticar(email, password);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            
            // Obtener el primer rol del usuario
            String rolPrincipal = "USUARIO"; // Rol por defecto
            if (usuario.getRoles() != null && !usuario.getRoles().isEmpty()) {
                rolPrincipal = usuario.getRoles().iterator().next().getTipoRol().toString();
            }
            
            // Guardar información en sesión
            session.setAttribute("id_usuario", usuario.getId());
            session.setAttribute("email_usuario", usuario.getEmail());
            session.setAttribute("nombre_usuario", usuario.getNombre());
            session.setAttribute("apellido_usuario", usuario.getApellido());
            session.setAttribute("nombre_completo", usuario.getNombre() + " " + usuario.getApellido());
            session.setAttribute("rol_usuario", rolPrincipal);
            
            // Guardar atributos específicos por rol para compatibilidad con controladores existentes
            if ("ADMIN".equalsIgnoreCase(rolPrincipal)) {
                session.setAttribute("id_admin", usuario.getId());
                session.setAttribute("email_admin", usuario.getEmail());
                session.setAttribute("nombre_completo_admin", usuario.getNombre() + " " + usuario.getApellido());
                return "redirect:/admin/dashboard";
            } else if ("INGENIERO".equalsIgnoreCase(rolPrincipal)) {
                session.setAttribute("id_ingeniero", usuario.getId());
                session.setAttribute("email_ingeniero", usuario.getEmail());
                session.setAttribute("nombre_completo_ingeniero", usuario.getNombre() + " " + usuario.getApellido());
                return "redirect:/ingeniero/dashboard";
            } else {
                session.setAttribute("id_cliente", usuario.getId());
                session.setAttribute("email_cliente", usuario.getEmail());
                session.setAttribute("nombre_completo_cliente", usuario.getNombre() + " " + usuario.getApellido());
                return "redirect:/usuario/dashboard";
            }
        } else {
            model.addAttribute("error", "Email o contraseña incorrectos");
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
}
