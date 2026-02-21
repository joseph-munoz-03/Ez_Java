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
     * Verifica si el usuario tiene sesión de admin
     */
    private boolean verificarAdmin(HttpSession session) {
        Object rol_usuario = session.getAttribute("rol_usuario");
        return rol_usuario != null && rol_usuario.toString().equalsIgnoreCase("ADMIN");
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

    /**
     * Retorna el sidebar HTML para ingeniero
     */
    @GetMapping("/sidebar/ingeniero")
    @ResponseBody
    public String getSidebarIngeniero(HttpSession session, @RequestParam(defaultValue = "") String activePage) {
        if (!verificarIngeniero(session)) {
            return "<div class='alert alert-danger'>No autorizado</div>";
        }

        StringBuilder sidebar = new StringBuilder();
        sidebar.append("<div class=\"sidebar\">\n");
        sidebar.append("  <div class=\"logo\">E</div>\n");
        sidebar.append("  <nav class=\"nav flex-column\">\n");
        
        sidebar.append(crearNavLink("/ingeniero/dashboard", "dashboard", activePage, "<i class=\"fas fa-home\"></i> Inicio"));
        sidebar.append(crearNavLink("/ingeniero/chat", "chat", activePage, "<i class=\"fas fa-comments\"></i> Chats"));
        sidebar.append(crearNavLink("/ingeniero/marketplace", "marketplace", activePage, "<i class=\"fas fa-store\"></i> Marketplace"));
        sidebar.append(crearNavLink("/ingeniero/contratos", "contratos", activePage, "<i class=\"fas fa-file-contract\"></i> Contratos"));
        sidebar.append(crearNavLink("/ingeniero/calendario", "calendario", activePage, "<i class=\"fas fa-calendar-alt\"></i> Calendario"));
        sidebar.append(crearNavLink("/ingeniero/proyectos", "proyectos", activePage, "<i class=\"fas fa-project-diagram\"></i> Proyectos"));
        sidebar.append(crearNavLink("/ingeniero/tareas", "tareas", activePage, "<i class=\"fas fa-tasks\"></i> Tareas"));
        
        sidebar.append("    <hr style=\"border-color: rgba(255, 255, 255, 0.2); margin: 15px 10px;\">\n");
        sidebar.append(crearNavLink("/ingeniero/perfil", "perfil", activePage, "<i class=\"fas fa-user\"></i> Perfil"));
        sidebar.append("    <button onclick=\"logout()\" class=\"nav-link\" style=\"border:none; background:none; padding:15px 20px; text-decoration:none; cursor:pointer; text-align:left; width:100%; font-family:inherit; font-size:inherit; color:rgba(255,255,255,0.8);\">\n");
        sidebar.append("      <i class=\"fas fa-sign-out-alt\"></i> Cerrar Sesión\n");
        sidebar.append("    </button>\n");
        sidebar.append("  </nav>\n");
        sidebar.append("</div>\n");

        return sidebar.toString();
    }

    /**
     * Retorna el sidebar HTML para usuario
     */
    @GetMapping("/sidebar/usuario")
    @ResponseBody
    public String getSidebarUsuario(HttpSession session, @RequestParam(defaultValue = "") String activePage) {
        if (!verificarUsuario(session)) {
            return "<div class='alert alert-danger'>No autorizado</div>";
        }

        StringBuilder sidebar = new StringBuilder();
        sidebar.append("<div class=\"sidebar\">\n");
        sidebar.append("  <div class=\"logo\">E</div>\n");
        sidebar.append("  <nav class=\"nav flex-column\">\n");
        
        sidebar.append(crearNavLink("/usuario/dashboard", "dashboard", activePage, "<i class=\"fas fa-home\"></i> Inicio"));
        sidebar.append(crearNavLink("/usuario/chat", "chat", activePage, "<i class=\"fas fa-comments\"></i> Chats"));
        sidebar.append(crearNavLink("/usuario/marketplace", "marketplace", activePage, "<i class=\"fas fa-store\"></i> Marketplace"));
        sidebar.append(crearNavLink("/usuario/contratos", "contratos", activePage, "<i class=\"fas fa-file-contract\"></i> Contratos"));
        sidebar.append(crearNavLink("/usuario/calendario", "calendario", activePage, "<i class=\"fas fa-calendar-alt\"></i> Calendario"));
        sidebar.append(crearNavLink("/usuario/proyectos", "proyectos", activePage, "<i class=\"fas fa-project-diagram\"></i> Proyectos"));
        sidebar.append(crearNavLink("/usuario/tareas", "tareas", activePage, "<i class=\"fas fa-tasks\"></i> Tareas"));
        
        sidebar.append("    <hr style=\"border-color: rgba(255, 255, 255, 0.2); margin: 15px 10px;\">\n");
        sidebar.append(crearNavLink("/usuario/perfil", "perfil", activePage, "<i class=\"fas fa-user\"></i> Perfil"));
        sidebar.append("    <button onclick=\"logout()\" class=\"nav-link\" style=\"border:none; background:none; padding:15px 20px; text-decoration:none; cursor:pointer; text-align:left; width:100%; font-family:inherit; font-size:inherit; color:rgba(255,255,255,0.8);\">\n");
        sidebar.append("      <i class=\"fas fa-sign-out-alt\"></i> Cerrar Sesión\n");
        sidebar.append("    </button>\n");
        sidebar.append("  </nav>\n");
        sidebar.append("</div>\n");

        return sidebar.toString();
    }

    /**
     * Retorna el sidebar HTML para admin
     */
    @GetMapping("/sidebar/admin")
    @ResponseBody
    public String getSidebarAdmin(HttpSession session, @RequestParam(defaultValue = "") String activePage) {
        if (!verificarAdmin(session)) {
            return "<div class='alert alert-danger'>No autorizado</div>";
        }

        StringBuilder sidebar = new StringBuilder();
        sidebar.append("<div class=\"sidebar\">\n");
        sidebar.append("  <div class=\"logo\">E</div>\n");
        sidebar.append("  <nav class=\"nav flex-column\">\n");
        
        sidebar.append(crearNavLink("/admin/dashboard", "dashboard", activePage, "<i class=\"fas fa-home\"></i> Inicio"));
        sidebar.append(crearNavLink("/admin/usuarios", "usuarios", activePage, "<i class=\"fas fa-users\"></i> Usuarios"));
        sidebar.append(crearNavLink("/admin/gestion_usuarios", "gestion_usuarios", activePage, "<i class=\"fas fa-user-cog\"></i> Gestión Usuarios"));
        sidebar.append(crearNavLink("/admin/roles", "roles", activePage, "<i class=\"fas fa-user-tag\"></i> Roles"));
        sidebar.append(crearNavLink("/admin/chat", "chat", activePage, "<i class=\"fas fa-comments\"></i> Chat"));
        sidebar.append(crearNavLink("/admin/contratos", "contratos", activePage, "<i class=\"fas fa-file-contract\"></i> Contratos"));
        sidebar.append(crearNavLink("/admin/marketplace", "marketplace", activePage, "<i class=\"fas fa-store\"></i> Marketplace"));
        sidebar.append(crearNavLink("/admin/reportes", "reportes", activePage, "<i class=\"fas fa-chart-bar\"></i> Reportes"));
        sidebar.append(crearNavLink("/admin/correos", "correos", activePage, "<i class=\"fas fa-envelope\"></i> Correos"));
        
        sidebar.append("    <hr style=\"border-color: rgba(255, 255, 255, 0.2); margin: 15px 10px;\">\n");
        sidebar.append("    <button onclick=\"logout()\" class=\"nav-link\" style=\"border:none; background:none; padding:15px 20px; text-decoration:none; cursor:pointer; text-align:left; width:100%; font-family:inherit; font-size:inherit; color:rgba(255,255,255,0.8);\">\n");
        sidebar.append("      <i class=\"fas fa-sign-out-alt\"></i> Cerrar Sesión\n");
        sidebar.append("    </button>\n");
        sidebar.append("  </nav>\n");
        sidebar.append("</div>\n");

        return sidebar.toString();
    }

    /**
     * Método auxiliar para crear un enlace de navegación con clase active
     */
    private String crearNavLink(String href, String linkId, String activePage, String icon) {
        String isActive = activePage.equalsIgnoreCase(linkId) ? " active" : "";
        return String.format(
            "    <a href=\"%s\" class=\"nav-link%s\" data-page=\"%s\">\n" +
            "      %s\n" +
            "    </a>\n",
            href, isActive, linkId, icon
        );
    }
}
