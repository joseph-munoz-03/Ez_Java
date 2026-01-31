package com.example.Ez.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioDashboardController {

    /**
     * Verifica si el usuario tiene sesión
     */
    private boolean verificarUsuario(HttpSession session) {
        Object id_cliente = session.getAttribute("id_cliente");
        return id_cliente != null;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!verificarUsuario(session)) {
            return "redirect:/";
        }
        
        Integer id_cliente = (Integer) session.getAttribute("id_cliente");
        String email_cliente = (String) session.getAttribute("email_cliente");
        String nombre_completo_cliente = (String) session.getAttribute("nombre_completo_cliente");
        String rol_usuario = (String) session.getAttribute("rol_usuario");
        
        model.addAttribute("id_cliente", id_cliente);
        model.addAttribute("email_cliente", email_cliente);
        model.addAttribute("nombre_completo_cliente", nombre_completo_cliente);
        model.addAttribute("rol_usuario", rol_usuario);
        
        return "usuario/dashboard";
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        if (!verificarUsuario(session)) {
            return "redirect:/";
        }
        
        Integer id_cliente = (Integer) session.getAttribute("id_cliente");
        String email_cliente = (String) session.getAttribute("email_cliente");
        String nombre_completo_cliente = (String) session.getAttribute("nombre_completo_cliente");
        
        model.addAttribute("id_cliente", id_cliente);
        model.addAttribute("email_cliente", email_cliente);
        model.addAttribute("nombre_completo_cliente", nombre_completo_cliente);
        
        return "usuario/perfil";
    }

    @GetMapping("/chat")
    public String chat(HttpSession session, Model model) {
        if (!verificarUsuario(session)) {
            return "redirect:/";
        }
        
        String nombre_completo_cliente = (String) session.getAttribute("nombre_completo_cliente");
        
        model.addAttribute("nombre_completo_cliente", nombre_completo_cliente);
        
        return "usuario/chat";
    }

    @GetMapping("/marketplace")
    public String marketplace(HttpSession session, Model model) {
        if (!verificarUsuario(session)) {
            return "redirect:/";
        }
        
        return "usuario/marketplace";
    }

    @GetMapping("/contratos")
    public String contratos(HttpSession session, Model model) {
        if (!verificarUsuario(session)) {
            return "redirect:/";
        }
        
        return "usuario/contratos";
    }

    @GetMapping("/calendario")
    public String calendario(HttpSession session, Model model) {
        if (!verificarUsuario(session)) {
            return "redirect:/";
        }
        
        return "usuario/calendario";
    }
}
