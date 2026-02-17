package com.example.Ez.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/headers")
public class HeaderApiController {

    /**
     * Verifica si el usuario tiene sesión de ingeniero
     */
    private boolean verificarIngeniero(HttpSession session) {
        Object rol_usuario = session.getAttribute("rol_usuario");
        return rol_usuario != null && (rol_usuario.toString().equalsIgnoreCase("INGENIERO") || rol_usuario.toString().equalsIgnoreCase("ADMIN"));
    }

    /**
     * Verifica si el usuario tiene sesión de usuario/cliente
     */
    private boolean verificarUsuario(HttpSession session) {
        Object rol_usuario = session.getAttribute("rol_usuario");
        return rol_usuario != null && rol_usuario.toString().equalsIgnoreCase("USUARIO");
    }

    /**
     * Retorna el header HTML para ingeniero con datos del usuario
     */
    @GetMapping("/ingeniero")
    @ResponseBody
    public String getHeaderIngeniero(HttpSession session) {
        if (!verificarIngeniero(session)) {
            return "<div class='alert alert-danger'>No autorizado</div>";
        }

        String nombre_completo = (String) session.getAttribute("nombre_completo_ingeniero");
        if (nombre_completo == null) {
            nombre_completo = "Ingeniero";
        }

        return String.format(
            "<!-- HEADER INGENIERO -->\n" +
            "<div class=\"topbar\">\n" +
            "    <h1><i class=\"fas fa-cog\"></i> <span id=\"page-title\">Dashboard</span></h1>\n" +
            "    <div class=\"user-info\">\n" +
            "        <span><i class=\"fas fa-user-circle\"></i> <span id=\"user-name-header\">%s</span></span>\n" +
            "        <a href=\"/ingeniero/perfil\" class=\"profile-btn\" title=\"Mi Perfil\">\n" +
            "            <i class=\"fas fa-user\"></i>\n" +
            "        </a>\n" +
            "    </div>\n" +
            "</div>",
            nombre_completo
        );
    }

    /**
     * Retorna el header HTML para usuario/cliente con datos del usuario
     */
    @GetMapping("/usuario")
    @ResponseBody
    public String getHeaderUsuario(HttpSession session) {
        if (!verificarUsuario(session)) {
            return "<div class='alert alert-danger'>No autorizado</div>";
        }

        String nombre_completo = (String) session.getAttribute("nombre_completo_usuario");
        if (nombre_completo == null) {
            nombre_completo = "Usuario";
        }

        return String.format(
            "<!-- HEADER USUARIO/CLIENTE -->\n" +
            "<div class=\"topbar\">\n" +
            "    <h1><i class=\"fas fa-cog\"></i> <span id=\"page-title\">Dashboard</span></h1>\n" +
            "    <div class=\"user-info\">\n" +
            "        <span><i class=\"fas fa-user-circle\"></i> <span id=\"user-name-header\">%s</span></span>\n" +
            "        <a href=\"/usuario/perfil\" class=\"profile-btn\" title=\"Mi Perfil\">\n" +
            "            <i class=\"fas fa-user\"></i>\n" +
            "        </a>\n" +
            "    </div>\n" +
            "</div>",
            nombre_completo
        );
    }

    /**
     * Retorna el header HTML para admin con datos del usuario
     */
    @GetMapping("/admin")
    @ResponseBody
    public String getHeaderAdmin(HttpSession session) {
        if (!verificarIngeniero(session)) {
            return "<div class='alert alert-danger'>No autorizado</div>";
        }

        String nombre_completo = (String) session.getAttribute("nombre_completo_ingeniero");
        if (nombre_completo == null) {
            nombre_completo = "Admin";
        }

        return String.format(
            "<!-- HEADER ADMIN -->\n" +
            "<div class=\"topbar\">\n" +
            "    <h1><i class=\"fas fa-cog\"></i> <span id=\"page-title\">Dashboard</span></h1>\n" +
            "    <div class=\"user-info\">\n" +
            "        <span><i class=\"fas fa-user-circle\"></i> <span id=\"user-name-header\">%s</span></span>\n" +
            "        <a href=\"/admin/perfil\" class=\"profile-btn\" title=\"Mi Perfil\">\n" +
            "            <i class=\"fas fa-user\"></i>\n" +
            "        </a>\n" +
            "    </div>\n" +
            "</div>",
            nombre_completo
        );
    }
}
