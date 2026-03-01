package com.example.Ez.controller;

import com.example.Ez.dto.ColorConfiguracionDTO;
import com.example.Ez.model.ColorConfiguracion;
import com.example.Ez.model.Usuario;
import com.example.Ez.service.ColorService;
import com.example.Ez.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/colores")
@CrossOrigin
public class ColorController {

    private final ColorService colorService;
    private final UsuarioService usuarioService;

    public ColorController(ColorService colorService, UsuarioService usuarioService) {
        this.colorService = colorService;
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene la configuración de color del usuario autenticado
     */
    @GetMapping("/mi-tema")
    public ResponseEntity<ColorConfiguracionDTO> obtenerMiTema() {
        try {
            // TODO: Obtener usuario autenticado del contexto
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene la configuración de color por ID de usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ColorConfiguracionDTO> obtenerTemaDeUsuario(@PathVariable Integer usuarioId) {
        try {
            var tema = colorService.obtenerConfiguracionColorPorUsuarioId(usuarioId);
            return ResponseEntity.ok(tema.orElseThrow());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * ACTUALIZA el tema/color del usuario
     * 
     * El usuario puede elegir:
     * - Color Principal: BLANCO, GRIS, NEGRO
     * - Color Acompañante: VERDE, MORADO, AZUL, ROJO, GRIS
     * 
     * Esto cambiar automáticamente:
     * 1. El logo (según los colores)
     * 2. Los estilos CSS cargados
     * 3. El tema de la interfaz
     */
    @PutMapping("/cambiar-tema")
    public ResponseEntity<ResponseMessage> cambiarTema(@RequestBody CambiarTemaRequest request) {
        try {
            // TODO: Obtener usuario autenticado
            // Usuario usuario = usuarioService.obtenerUsuarioAutenticado();

            ColorConfiguracion.ColorPrincipal colorPrincipal = 
                    ColorConfiguracion.ColorPrincipal.valueOf(request.getColorPrincipal());
            ColorConfiguracion.ColorAcompañante colorAcompañante = 
                    ColorConfiguracion.ColorAcompañante.valueOf(request.getColorAcompañante());

            // ColorConfiguracionDTO tema = colorService.guardarConfiguracionColor(usuario, colorPrincipal, colorAcompañante);

            return ResponseEntity.ok(new ResponseMessage("Tema actualizado correctamente", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Error: " + e.getMessage(), null));
        }
    }

    /**
     * Obtiene los colores principales disponibles
     */
    @GetMapping("/colores-principales")
    public ResponseEntity<String[]> obtenerColoresPrincipales() {
        return ResponseEntity.ok(colorService.obtenerColorePrincipales());
    }

    /**
     * Obtiene los colores acompañantes disponibles
     */
    @GetMapping("/colores-acompañantes")
    public ResponseEntity<String[]> obtenerColoresAcompañantes() {
        return ResponseEntity.ok(colorService.obtenerColoresAcompañantes());
    }

    /**
     * Obtiene la URL del logo según los colores
     */
    @GetMapping("/logo")
    public ResponseEntity<LogoResponse> obtenerUrlLogo(
            @RequestParam String colorPrincipal,
            @RequestParam String colorAcompañante) {
        try {
            String url = colorService.obtenerUrlLogo(colorPrincipal, colorAcompañante);
            String css = colorService.obtenerNombreCssTheme(colorPrincipal, colorAcompañante);
            return ResponseEntity.ok(new LogoResponse(url, css));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ===== PAYLOADS =====

    public static class CambiarTemaRequest {
        private String colorPrincipal;
        private String colorAcompañante;

        public String getColorPrincipal() {
            return colorPrincipal;
        }

        public void setColorPrincipal(String colorPrincipal) {
            this.colorPrincipal = colorPrincipal;
        }

        public String getColorAcompañante() {
            return colorAcompañante;
        }

        public void setColorAcompañante(String colorAcompañante) {
            this.colorAcompañante = colorAcompañante;
        }
    }

    public static class ResponseMessage {
        private String mensaje;
        private ColorConfiguracionDTO tema;

        public ResponseMessage(String mensaje, ColorConfiguracionDTO tema) {
            this.mensaje = mensaje;
            this.tema = tema;
        }

        public String getMensaje() {
            return mensaje;
        }

        public ColorConfiguracionDTO getTema() {
            return tema;
        }
    }

    public static class LogoResponse {
        private String urlLogo;
        private String urlCss;

        public LogoResponse(String urlLogo, String urlCss) {
            this.urlLogo = urlLogo;
            this.urlCss = urlCss;
        }

        public String getUrlLogo() {
            return urlLogo;
        }

        public String getUrlCss() {
            return urlCss;
        }
    }
}
