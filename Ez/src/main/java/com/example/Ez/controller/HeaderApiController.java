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
     * Retorna el menú desplegable HTML para ingeniero
     */
    @GetMapping("/sidebar/ingeniero")
    @ResponseBody
    public String getSidebarIngeniero(HttpSession session, @RequestParam(defaultValue = "") String activePage) {
        if (!verificarIngeniero(session)) {
            return "<div class='alert alert-danger'>No autorizado</div>";
        }

        StringBuilder menu = new StringBuilder();
        menu.append("<div class=\"dropdown-menu-container\">\n");
        menu.append("  <button class=\"dropdown-toggle-btn\" id=\"dropdownToggle\" onclick=\"toggleDropdownMenu()\">\n");
        menu.append("    <i class=\"fas fa-bars\"></i>\n");
        menu.append("  </button>\n");
        menu.append("  <div class=\"dropdown-content\" id=\"dropdownContent\">\n");
        menu.append("    <div class=\"dropdown-logo\">E</div>\n");
        menu.append("    <nav class=\"dropdown-nav\">\n");
        
        menu.append(crearDropdownLink("/ingeniero/dashboard", "dashboard", activePage, "<i class=\"fas fa-home\"></i> Inicio"));
        menu.append(crearDropdownLink("/ingeniero/chat", "chat", activePage, "<i class=\"fas fa-comments\"></i> Chats"));
        menu.append(crearDropdownLink("/ingeniero/marketplace", "marketplace", activePage, "<i class=\"fas fa-store\"></i> Marketplace"));
        menu.append(crearDropdownLink("/ingeniero/contratos", "contratos", activePage, "<i class=\"fas fa-file-contract\"></i> Contratos"));
        menu.append(crearDropdownLink("/ingeniero/calendario", "calendario", activePage, "<i class=\"fas fa-calendar-alt\"></i> Calendario"));
        menu.append(crearDropdownLink("/ingeniero/proyectos", "proyectos", activePage, "<i class=\"fas fa-project-diagram\"></i> Proyectos"));
        menu.append(crearDropdownLink("/ingeniero/tareas", "tareas", activePage, "<i class=\"fas fa-tasks\"></i> Tareas"));
        
        menu.append("      <hr style=\"border-color: rgba(100, 100, 100, 0.2); margin: 10px 0;\">\n");
        menu.append(crearDropdownLink("/ingeniero/perfil", "perfil", activePage, "<i class=\"fas fa-user\"></i> Perfil"));
        menu.append("      <button onclick=\"logout()\" class=\"dropdown-link\" style=\"border:none; background:none; padding:12px 16px; text-decoration:none; cursor:pointer; text-align:left; width:100%; font-family:inherit; font-size:inherit; color:#333;\">\n");
        menu.append("        <i class=\"fas fa-sign-out-alt\"></i> Cerrar Sesión\n");
        menu.append("      </button>\n");
        menu.append("    </nav>\n");
        menu.append("  </div>\n");
        menu.append("</div>\n");

        return menu.toString();
    }

    /**
     * Retorna el menú desplegable HTML para usuario
     */
    @GetMapping("/sidebar/usuario")
    @ResponseBody
    public String getSidebarUsuario(HttpSession session, @RequestParam(defaultValue = "") String activePage) {
        if (!verificarUsuario(session)) {
            return "<div class='alert alert-danger'>No autorizado</div>";
        }

        StringBuilder menu = new StringBuilder();
        menu.append("<div class=\"dropdown-menu-container\">\n");
        menu.append("  <button class=\"dropdown-toggle-btn\" id=\"dropdownToggle\" onclick=\"toggleDropdownMenu()\">\n");
        menu.append("    <i class=\"fas fa-bars\"></i>\n");
        menu.append("  </button>\n");
        menu.append("  <div class=\"dropdown-content\" id=\"dropdownContent\">\n");
        menu.append("    <div class=\"dropdown-logo\">E</div>\n");
        menu.append("    <nav class=\"dropdown-nav\">\n");
        
        menu.append(crearDropdownLink("/usuario/dashboard", "dashboard", activePage, "<i class=\"fas fa-home\"></i> Inicio"));
        menu.append(crearDropdownLink("/usuario/chat", "chat", activePage, "<i class=\"fas fa-comments\"></i> Chats"));
        menu.append(crearDropdownLink("/usuario/marketplace", "marketplace", activePage, "<i class=\"fas fa-store\"></i> Marketplace"));
        menu.append(crearDropdownLink("/usuario/contratos", "contratos", activePage, "<i class=\"fas fa-file-contract\"></i> Contratos"));
        menu.append(crearDropdownLink("/usuario/calendario", "calendario", activePage, "<i class=\"fas fa-calendar-alt\"></i> Calendario"));
        menu.append(crearDropdownLink("/usuario/proyectos", "proyectos", activePage, "<i class=\"fas fa-project-diagram\"></i> Proyectos"));
        menu.append(crearDropdownLink("/usuario/tareas", "tareas", activePage, "<i class=\"fas fa-tasks\"></i> Tareas"));
        
        menu.append("      <hr style=\"border-color: rgba(100, 100, 100, 0.2); margin: 10px 0;\">\n");
        menu.append(crearDropdownLink("/usuario/perfil", "perfil", activePage, "<i class=\"fas fa-user\"></i> Perfil"));
        menu.append("      <button onclick=\"logout()\" class=\"dropdown-link\" style=\"border:none; background:none; padding:12px 16px; text-decoration:none; cursor:pointer; text-align:left; width:100%; font-family:inherit; font-size:inherit; color:#333;\">\n");
        menu.append("        <i class=\"fas fa-sign-out-alt\"></i> Cerrar Sesión\n");
        menu.append("      </button>\n");
        menu.append("    </nav>\n");
        menu.append("  </div>\n");
        menu.append("</div>\n");

        return menu.toString();
    }

    /**
     * Retorna el menú desplegable HTML para admin
     */
    @GetMapping("/sidebar/admin")
    @ResponseBody
    public String getSidebarAdmin(HttpSession session, @RequestParam(defaultValue = "") String activePage) {
        if (!verificarAdmin(session)) {
            return "<div class='alert alert-danger'>No autorizado</div>";
        }

        StringBuilder menu = new StringBuilder();
        menu.append("<div class=\"dropdown-menu-container\">\n");
        menu.append("  <button class=\"dropdown-toggle-btn\" id=\"dropdownToggle\" onclick=\"toggleDropdownMenu()\">\n");
        menu.append("    <i class=\"fas fa-bars\"></i>\n");
        menu.append("  </button>\n");
        menu.append("  <div class=\"dropdown-content\" id=\"dropdownContent\">\n");
        menu.append("    <div class=\"dropdown-logo\">E</div>\n");
        menu.append("    <nav class=\"dropdown-nav\">\n");
        
        menu.append(crearDropdownLink("/admin/dashboard", "dashboard", activePage, "<i class=\"fas fa-home\"></i> Inicio"));
        menu.append(crearDropdownLink("/admin/usuarios", "usuarios", activePage, "<i class=\"fas fa-users\"></i> Usuarios"));
        menu.append(crearDropdownLink("/admin/gestion_usuarios", "gestion_usuarios", activePage, "<i class=\"fas fa-user-cog\"></i> Gestión Usuarios"));
        menu.append(crearDropdownLink("/admin/roles", "roles", activePage, "<i class=\"fas fa-user-tag\"></i> Roles"));
        menu.append(crearDropdownLink("/admin/chat", "chat", activePage, "<i class=\"fas fa-comments\"></i> Chat"));
        menu.append(crearDropdownLink("/admin/contratos", "contratos", activePage, "<i class=\"fas fa-file-contract\"></i> Contratos"));
        menu.append(crearDropdownLink("/admin/marketplace", "marketplace", activePage, "<i class=\"fas fa-store\"></i> Marketplace"));
        menu.append(crearDropdownLink("/admin/reportes", "reportes", activePage, "<i class=\"fas fa-chart-bar\"></i> Reportes"));
        menu.append(crearDropdownLink("/admin/correos", "correos", activePage, "<i class=\"fas fa-envelope\"></i> Correos"));
        
        menu.append("      <hr style=\"border-color: rgba(100, 100, 100, 0.2); margin: 10px 0;\">\n");
        menu.append("      <button onclick=\"logout()\" class=\"dropdown-link\" style=\"border:none; background:none; padding:12px 16px; text-decoration:none; cursor:pointer; text-align:left; width:100%; font-family:inherit; font-size:inherit; color:#333;\">\n");
        menu.append("        <i class=\"fas fa-sign-out-alt\"></i> Cerrar Sesión\n");
        menu.append("      </button>\n");
        menu.append("    </nav>\n");
        menu.append("  </div>\n");
        menu.append("</div>\n");

        return menu.toString();
    }

    /**
     * Método auxiliar para crear un enlace de navegación en dropdown con clase active
     */
    private String crearDropdownLink(String href, String linkId, String activePage, String icon) {
        String isActive = activePage.equalsIgnoreCase(linkId) ? " active" : "";
        return String.format(
            "      <a href=\"%s\" class=\"dropdown-link%s\" data-page=\"%s\">\n" +
            "        %s\n" +
            "      </a>\n",
            href, isActive, linkId, icon
        );
    }

    /**
     * Método auxiliar para crear un enlace de navegación con clase active (DEPRECADO - usar crearDropdownLink)
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
