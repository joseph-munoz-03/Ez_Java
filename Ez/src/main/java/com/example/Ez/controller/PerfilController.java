package com.example.Ez.controller;

import com.example.Ez.model.Usuario;
import com.example.Ez.service.UsuarioService;
import com.example.Ez.service.ArchivoService;
import com.example.Ez.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;

@RestController
@RequestMapping("/api/perfil")
@CrossOrigin
public class PerfilController {

    private final UsuarioService usuarioService;
    private final ArchivoService archivoService;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;

    public PerfilController(UsuarioService usuarioService, ArchivoService archivoService,
                           EmailService emailService, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.archivoService = archivoService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Obtiene los datos del perfil del usuario autenticado
     */
    @GetMapping("/mi-perfil")
    public ResponseEntity<PerfilResponse> obtenerMiPerfil() {
        try {
            // TODO: Obtener usuario autenticado del contexto de seguridad
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene el perfil de otro usuario (por ID)
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<PerfilPublicoResponse> obtenerPerfilUsuario(@PathVariable Integer usuarioId) {
        try {
            Usuario usuario = usuarioService.obtenerPorId((long) usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            PerfilPublicoResponse respuesta = new PerfilPublicoResponse(
                    usuario.getId(),
                    usuario.getNombreUsuario(),
                    usuario.getFotoPerfil(),
                    usuario.getDescripcion(),
                    usuario.getEtiquetas(),
                    usuario.getFechaRegistro() != null ? usuario.getFechaRegistro().toLocalDate() : null
            );
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Actualiza los datos del usuario
     * 
     * Permite actualizar:
     * - Nombre de usuario
     * - Descripción
     * - Etiquetas
     */
    @PutMapping("/actualizar-datos")
    public ResponseEntity<ResponseMessage> actualizarDatos(@RequestBody ActualizarDatosRequest request) {
        try {
            // TODO: Obtener usuario autenticado
            // Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

            return ResponseEntity.ok(new ResponseMessage("Datos actualizados correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    /**
     * Actualiza la foto de perfil
     */
    @PostMapping("/actualizar-foto")
    public ResponseEntity<FotoResponse> actualizarFoto(@RequestParam("archivo") MultipartFile archivo) {
        try {
            // TODO: Obtener usuario autenticado
            // Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

            // Validar que sea imagen
            archivoService.validarTamañoArchivo(archivo, 5 * 1024 * 1024); // 5MB máx

            // Guardar foto
            // String rutaFoto = archivoService.guardarFotoPerfil(archivo, usuario.getId());
            // usuario.setFotoPerfil(rutaFoto);
            // usuarioService.actualizarUsuario(usuario);

            return ResponseEntity.ok(new FotoResponse("Foto actualizada correctamente", null));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(new FotoResponse("Error al cargar foto: " + e.getMessage(), null));
        }
    }

    /**
     * Actualiza la contraseña del usuario
     * 
     * Requiere:
     * - Contraseña antigua (para verificar)
     * - Nueva contraseña
     * - Confirmación de nueva contraseña
     */
    @PutMapping("/seguridad/cambiar-contraseña")
    public ResponseEntity<ResponseMessage> cambiarContraseña(@RequestBody CambiarContraseñaRequest request) {
        try {
            // TODO: Obtener usuario autenticado del contexto de seguridad
            // Usuario usuario = usuarioService.obtenerUsuarioAutenticado();
            
            // Validar que las contraseñas nuevas coincidan
            if (!request.getNuevaContraseña().equals(request.getConfirmarContraseña())) {
                return ResponseEntity.badRequest().body(
                        new ResponseMessage("Las contraseñas no coinciden")
                );
            }

            // Validar que la contraseña tenga mínimo 6 caracteres
            if (request.getNuevaContraseña().length() < 6) {
                return ResponseEntity.badRequest().body(
                        new ResponseMessage("La contraseña debe tener al menos 6 caracteres")
                );
            }

            // TODO: Descomentar cuando se implemente obtenerUsuarioAutenticado()
            // Validar que la contraseña antigua sea correcta
            // if (!passwordEncoder.matches(request.getContraseñaAntigua(), usuario.getContrasena())) {
            //     return ResponseEntity.badRequest().body(
            //             new ResponseMessage("La contraseña antigua es incorrecta")
            //     );
            // }

            // TODO: Descomentar cuando se implemente obtenerUsuarioAutenticado()
            // Actualizar contraseña
            // String contraseñaHasheada = passwordEncoder.encode(request.getNuevaContraseña());
            // usuario.setContrasena(contraseñaHasheada);
            // usuarioService.actualizarUsuario(usuario);
            //
            // Enviar correo de confirmación
            // emailService.enviarCorreoCambioContraseña(usuario.getEmail(), usuario.getNombre());

            return ResponseEntity.ok(new ResponseMessage("Contraseña actualizada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }

    /**
     * Obtiene la información de seguridad (sin mostrar contraseña)
     */
    @GetMapping("/seguridad/info")
    public ResponseEntity<InfoSeguridadResponse> obtenerInfoSeguridad() {
        try {
            // TODO: Obtener usuario autenticado
            // Usuario usuario = usuarioService.obtenerUsuarioAutenticado();
            
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ===== PAYLOADS =====

    public static class PerfilResponse {
        private Integer id;
        private String nombre;
        private String apellido;
        private String nombreUsuario;
        private String email;
        private String fotoPerfil;
        private String descripcion;
        private String etiquetas;

        // Getters
        public Integer getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public String getNombreUsuario() {
            return nombreUsuario;
        }

        public String getEmail() {
            return email;
        }

        public String getFotoPerfil() {
            return fotoPerfil;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public String getEtiquetas() {
            return etiquetas;
        }
    }

    public static class PerfilPublicoResponse {
        private Integer id;
        private String nombreUsuario;
        private String fotoPerfil;
        private String descripcion;
        private String etiquetas;
        private Object fechaRegistro;

        public PerfilPublicoResponse(Integer id, String nombreUsuario, String fotoPerfil,
                                      String descripcion, String etiquetas, Object fechaRegistro) {
            this.id = id;
            this.nombreUsuario = nombreUsuario;
            this.fotoPerfil = fotoPerfil;
            this.descripcion = descripcion;
            this.etiquetas = etiquetas;
            this.fechaRegistro = fechaRegistro;
        }

        // Getters
        public Integer getId() {
            return id;
        }

        public String getNombreUsuario() {
            return nombreUsuario;
        }

        public String getFotoPerfil() {
            return fotoPerfil;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public String getEtiquetas() {
            return etiquetas;
        }

        public Object getFechaRegistro() {
            return fechaRegistro;
        }
    }

    public static class ActualizarDatosRequest {
        private String nombreUsuario;
        private String descripcion;
        private String etiquetas;

        public String getNombreUsuario() {
            return nombreUsuario;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public String getEtiquetas() {
            return etiquetas;
        }
    }

    public static class FotoResponse {
        private String mensaje;
        private String urlFoto;

        public FotoResponse(String mensaje, String urlFoto) {
            this.mensaje = mensaje;
            this.urlFoto = urlFoto;
        }

        public String getMensaje() {
            return mensaje;
        }

        public String getUrlFoto() {
            return urlFoto;
        }
    }

    public static class CambiarContraseñaRequest {
        private String contraseñaAntigua;
        private String nuevaContraseña;
        private String confirmarContraseña;

        public String getContraseñaAntigua() {
            return contraseñaAntigua;
        }

        public String getNuevaContraseña() {
            return nuevaContraseña;
        }

        public String getConfirmarContraseña() {
            return confirmarContraseña;
        }
    }

    public static class InfoSeguridadResponse {
        private String email;
        private Boolean usuarioAutenticado;

        public String getEmail() {
            return email;
        }

        public Boolean getUsuarioAutenticado() {
            return usuarioAutenticado;
        }
    }

    public static class ResponseMessage {
        private String mensaje;

        public ResponseMessage(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getMensaje() {
            return mensaje;
        }
    }
}
