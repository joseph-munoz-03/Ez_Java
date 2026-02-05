package com.example.Ez.controller;

import com.example.Ez.model.Usuario;
import com.example.Ez.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    private boolean verificarAdmin(HttpSession session) {
        Object rol_usuario = session.getAttribute("rol_usuario");
        return rol_usuario != null && rol_usuario.toString().equalsIgnoreCase("ADMIN");
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

    // ==================== ENDPOINTS API CRUD ====================

    /**
     * Obtiene todos los usuarios (JSON API)
     */
    @GetMapping("/api/usuarios")
    @ResponseBody
    public ResponseEntity<List<Usuario>> obtenerTodosUsuarios(HttpSession session) {
        if (!verificarAdmin(session)) {
            return ResponseEntity.status(403).build();
        }
        List<Usuario> usuarios = usuarioService.obtenerTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Filtra usuarios (JSON API)
     */
    @GetMapping("/api/usuarios/filtrar")
    @ResponseBody
    public ResponseEntity<List<Usuario>> filtrarUsuarios(
            @RequestParam(required = false) String criterio,
            @RequestParam(defaultValue = "nombre") String filtroTipo,
            HttpSession session) {
        if (!verificarAdmin(session)) {
            return ResponseEntity.status(403).build();
        }
        List<Usuario> usuarios = usuarioService.filtrarUsuarios(criterio, filtroTipo);
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtiene un usuario por ID (JSON API)
     */
    @GetMapping("/api/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Integer id, HttpSession session) {
        if (!verificarAdmin(session)) {
            return ResponseEntity.status(403).build();
        }
        Optional<Usuario> usuario = usuarioService.obtenerPorId(Long.valueOf(id));
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Actualiza un usuario (JSON API)
     */
    @PutMapping("/api/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable Integer id,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String estado,
            HttpSession session) {
        if (!verificarAdmin(session)) {
            return ResponseEntity.status(403).build();
        }

        Usuario.Genero generoEnum = null;
        if (genero != null && !genero.isBlank()) {
            try {
                generoEnum = Usuario.Genero.valueOf(genero);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, nombre, apellido, email, telefono, generoEnum);
        
        if (estado != null && !estado.isBlank()) {
            try {
                Usuario.Estado estadoEnum = Usuario.Estado.valueOf(estado);
                Optional<Usuario> usuarioOpt = usuarioService.obtenerPorId(Long.valueOf(id));
                if (usuarioOpt.isPresent()) {
                    Usuario usuario = usuarioOpt.get();
                    usuario.setEstado(estadoEnum);
                    usuarioActualizado = usuarioService.actualizarUsuario(id, usuario.getNombre(), 
                                                                           usuario.getApellido(), 
                                                                           usuario.getEmail(), 
                                                                           usuario.getTelefono(), 
                                                                           usuario.getGenero());
                }
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        return usuarioActualizado != null ? ResponseEntity.ok(usuarioActualizado) 
                                           : ResponseEntity.notFound().build();
    }

    /**
     * Suspende un usuario por X días (JSON API)
     */
    @PostMapping("/api/usuarios/{id}/suspender")
    @ResponseBody
    public ResponseEntity<Usuario> suspenderUsuario(
            @PathVariable Integer id,
            @RequestParam Integer dias,
            HttpSession session) {
        if (!verificarAdmin(session)) {
            return ResponseEntity.status(403).build();
        }
        Usuario usuario = usuarioService.suspenderUsuario(id, dias);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    /**
     * Reactiva un usuario suspendido (JSON API)
     */
    @PostMapping("/api/usuarios/{id}/reactivar")
    @ResponseBody
    public ResponseEntity<Usuario> reactivarUsuario(@PathVariable Integer id, HttpSession session) {
        if (!verificarAdmin(session)) {
            return ResponseEntity.status(403).build();
        }
        Usuario usuario = usuarioService.reactivarUsuario(id);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    /**
     * Banea un usuario (JSON API)
     */
    @PostMapping("/api/usuarios/{id}/banear")
    @ResponseBody
    public ResponseEntity<Usuario> banearUsuario(@PathVariable Integer id, HttpSession session) {
        if (!verificarAdmin(session)) {
            return ResponseEntity.status(403).build();
        }
        Usuario usuario = usuarioService.banearUsuario(id);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    /**
     * Inactiva un usuario (soft delete) (JSON API)
     */
    @DeleteMapping("/api/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<Usuario> inactivarUsuario(@PathVariable Integer id, HttpSession session) {
        if (!verificarAdmin(session)) {
            return ResponseEntity.status(403).build();
        }
        Usuario usuario = usuarioService.inactivarUsuario(id);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }
}
