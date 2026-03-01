package com.example.Ez.controller;

import com.example.Ez.dto.PublicacionDTO;
import com.example.Ez.model.Usuario;
import com.example.Ez.service.PublicacionService;
import com.example.Ez.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/publicaciones")
@CrossOrigin
public class PublicacionController {

    private final PublicacionService publicacionService;
    private final UsuarioService usuarioService;

    public PublicacionController(PublicacionService publicacionService, UsuarioService usuarioService) {
        this.publicacionService = publicacionService;
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene todas las publicaciones activas
     * 
     * Para INGENIERO: Ve todas las publicaciones del marketplace
     * Para USUARIO: Ve todas las publicaciones
     */
    @GetMapping("/activas")
    public ResponseEntity<Page<PublicacionDTO>> obtenerPublicacionesActivas(Pageable pageable) {
        try {
            Page<PublicacionDTO> publicaciones = publicacionService.obtenerPublicacionesActivas(pageable);
            return ResponseEntity.ok(publicaciones);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Crea una nueva publicación
     * 
     * Solo USUARIOS pueden crear publicaciones
     */
    @PostMapping("/crear")
    public ResponseEntity<ResponseMessage> crearPublicacion(@RequestBody CrearPublicacionRequest request) {
        try {
            // TODO: Obtener usuario autenticado (debe ser USUARIO)
            // Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

            PublicacionDTO publicacion = publicacionService.crearPublicacion(
                    null, // usuario,
                    request.getTitulo(),
                    request.getDescripcion(),
                    request.getBaseDatos(),
                    request.getLenguajeProgramacion(),
                    new BigDecimal(request.getValor())
            );

            return ResponseEntity.ok(new ResponseMessage("Publicación creada exitosamente", publicacion));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Error: " + e.getMessage(), null));
        }
    }

    /**
     * Obtiene una publicación específica
     */
    @GetMapping("/{publicacionId}")
    public ResponseEntity<PublicacionDTO> obtenerPublicacion(@PathVariable Long publicacionId) {
        try {
            PublicacionDTO publicacion = publicacionService.obtenerPublicacion(publicacionId);
            return ResponseEntity.ok(publicacion);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Filtra publicaciones por base de datos
     */
    @GetMapping("/filtro/base-datos")
    public ResponseEntity<Page<PublicacionDTO>> filtrarPorBaseDatos(
            @RequestParam String baseDatos,
            Pageable pageable) {
        try {
            Page<PublicacionDTO> publicaciones = publicacionService.filtrarPorBaseDatos(baseDatos, pageable);
            return ResponseEntity.ok(publicaciones);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Filtra publicaciones por lenguaje de programación
     */
    @GetMapping("/filtro/lenguaje")
    public ResponseEntity<Page<PublicacionDTO>> filtrarPorLenguaje(
            @RequestParam String lenguaje,
            Pageable pageable) {
        try {
            Page<PublicacionDTO> publicaciones = publicacionService.filtrarPorLenguaje(lenguaje, pageable);
            return ResponseEntity.ok(publicaciones);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene las publicaciones del usuario autenticado
     * 
     * Para USUARIO: Sus propias publicaciones
     */
    @GetMapping("/mis-publicaciones")
    public ResponseEntity<Page<PublicacionDTO>> obtenerMisPublicaciones(Pageable pageable) {
        // TODO: Obtener usuario autenticado
        return ResponseEntity.badRequest().build();
    }

    /**
     * Actualiza una publicación
     */
    @PutMapping("/{publicacionId}")
    public ResponseEntity<PublicacionDTO> actualizarPublicacion(
            @PathVariable Long publicacionId,
            @RequestBody ActualizarPublicacionRequest request) {
        try {
            PublicacionDTO publicacion = publicacionService.actualizarPublicacion(
                    publicacionId,
                    request.getTitulo(),
                    request.getDescripcion(),
                    request.getBaseDatos(),
                    request.getLenguajeProgramacion(),
                    new BigDecimal(request.getValor())
            );
            return ResponseEntity.ok(publicacion);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina una publicación
     */
    @DeleteMapping("/{publicacionId}")
    public ResponseEntity<Void> eliminarPublicacion(@PathVariable Long publicacionId) {
        try {
            publicacionService.eliminarPublicacion(publicacionId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ===== PAYLOADS =====

    public static class CrearPublicacionRequest {
        private String titulo;
        private String descripcion;
        private String baseDatos;
        private String lenguajeProgramacion;
        private String valor;

        // Getters
        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getBaseDatos() {
            return baseDatos;
        }

        public void setBaseDatos(String baseDatos) {
            this.baseDatos = baseDatos;
        }

        public String getLenguajeProgramacion() {
            return lenguajeProgramacion;
        }

        public void setLenguajeProgramacion(String lenguajeProgramacion) {
            this.lenguajeProgramacion = lenguajeProgramacion;
        }

        public String getValor() {
            return valor;
        }

        public void setValor(String valor) {
            this.valor = valor;
        }
    }

    public static class ActualizarPublicacionRequest extends CrearPublicacionRequest {
    }

    public static class ResponseMessage {
        private String mensaje;
        private PublicacionDTO publicacion;

        public ResponseMessage(String mensaje, PublicacionDTO publicacion) {
            this.mensaje = mensaje;
            this.publicacion = publicacion;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public PublicacionDTO getPublicacion() {
            return publicacion;
        }

        public void setPublicacion(PublicacionDTO publicacion) {
            this.publicacion = publicacion;
        }
    }
}
