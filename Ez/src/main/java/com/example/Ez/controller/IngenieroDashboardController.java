package com.example.Ez.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/ingeniero")
public class IngenieroDashboardController {

    /**
     * Verifica si el usuario tiene sesión de ingeniero
     */
    private boolean verificarIngeniero(HttpSession session) {
        Object rol_usuario = session.getAttribute("rol_usuario");
        return rol_usuario != null && (rol_usuario.equals("ingeniero") || rol_usuario.equals("admin"));
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!verificarIngeniero(session)) {
            return "redirect:/";
        }
        
        Integer id_ingeniero = (Integer) session.getAttribute("id_ingeniero");
        String email_ingeniero = (String) session.getAttribute("email_ingeniero");
        String nombre_completo_ingeniero = (String) session.getAttribute("nombre_completo_ingeniero");
        
        model.addAttribute("id_ingeniero", id_ingeniero);
        model.addAttribute("email_ingeniero", email_ingeniero);
        model.addAttribute("nombre_completo_ingeniero", nombre_completo_ingeniero);
        
        return "ingeniero/dashboard";
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        if (!verificarIngeniero(session)) {
            return "redirect:/";
        }
        
        Integer id_ingeniero = (Integer) session.getAttribute("id_ingeniero");
        String email_ingeniero = (String) session.getAttribute("email_ingeniero");
        String nombre_completo_ingeniero = (String) session.getAttribute("nombre_completo_ingeniero");
        
        model.addAttribute("id_ingeniero", id_ingeniero);
        model.addAttribute("email_ingeniero", email_ingeniero);
        model.addAttribute("nombre_completo_ingeniero", nombre_completo_ingeniero);
        
        return "ingeniero/perfil";
    }

    @GetMapping("/chat")
    public String chat(HttpSession session, Model model) {
        if (!verificarIngeniero(session)) {
            return "redirect:/";
        }
        
        String nombre_completo_ingeniero = (String) session.getAttribute("nombre_completo_ingeniero");
        
        model.addAttribute("nombre_completo_ingeniero", nombre_completo_ingeniero);
        
        return "ingeniero/chat";
    }

    @GetMapping("/marketplace")
    public String marketplace(HttpSession session, Model model) {
        if (!verificarIngeniero(session)) {
            return "redirect:/";
        }
        
        return "ingeniero/marketplace";
    }

    @GetMapping("/contratos")
    public String contratos(HttpSession session, Model model) {
        if (!verificarIngeniero(session)) {
            return "redirect:/";
        }
        
        return "ingeniero/contratos";
    }

    @GetMapping("/calendario")
    public String calendario(HttpSession session, Model model) {
        if (!verificarIngeniero(session)) {
            return "redirect:/";
        }
        
        return "ingeniero/calendario";
    }
}
