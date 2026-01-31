package com.example.Ez.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private boolean verificarAdmin(HttpSession session) {
        Object rol_usuario = session.getAttribute("rol_usuario");
        return rol_usuario != null && rol_usuario.equals("admin");
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!verificarAdmin(session)) {
            return "redirect:/";
        }
        
        Integer id_admin = (Integer) session.getAttribute("id_admin");
        String email_admin = (String) session.getAttribute("email_admin");
        String nombre_completo_admin = (String) session.getAttribute("nombre_completo_admin");
        
        model.addAttribute("id_admin", id_admin);
        model.addAttribute("email_admin", email_admin);
        model.addAttribute("nombre_completo_admin", nombre_completo_admin);
        
        return "admin/dashboard";
    }

    @GetMapping("/gestion-usuarios")
    public String gestionUsuarios(HttpSession session, Model model) {
        if (!verificarAdmin(session)) {
            return "redirect:/";
        }
        
        return "admin/gestion_usuarios";
    }

    @GetMapping("/chat")
    public String chat(HttpSession session, Model model) {
        if (!verificarAdmin(session)) {
            return "redirect:/";
        }
        
        return "admin/chat";
    }

    @GetMapping("/marketplace")
    public String marketplace(HttpSession session, Model model) {
        if (!verificarAdmin(session)) {
            return "redirect:/";
        }
        
        return "admin/marketplace";
    }

    @GetMapping("/contratos")
    public String contratos(HttpSession session, Model model) {
        if (!verificarAdmin(session)) {
            return "redirect:/";
        }
        
        return "admin/contratos";
    }

    @GetMapping("/correos")
    public String correos(HttpSession session, Model model) {
        if (!verificarAdmin(session)) {
            return "redirect:/";
        }
        
        return "admin/correos";
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(HttpSession session, Model model) {
        if (!verificarAdmin(session)) {
            return "redirect:/";
        }
        
        return "admin/usuarios";
    }

    @GetMapping("/roles")
    public String gestionarRoles(HttpSession session, Model model) {
        if (!verificarAdmin(session)) {
            return "redirect:/";
        }
        
        return "admin/roles";
    }

    @GetMapping("/reportes")
    public String verReportes(HttpSession session, Model model) {
        if (!verificarAdmin(session)) {
            return "redirect:/";
        }
        
        return "admin/reportes";
    }
}
