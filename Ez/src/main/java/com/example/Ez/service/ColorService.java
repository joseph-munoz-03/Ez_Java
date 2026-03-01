package com.example.Ez.service;

import com.example.Ez.dto.ColorConfiguracionDTO;
import com.example.Ez.model.ColorConfiguracion;
import com.example.Ez.model.Usuario;
import com.example.Ez.repository.ColorConfiguracionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ColorService {

    private final ColorConfiguracionRepository colorRepository;

    public ColorService(ColorConfiguracionRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    /**
     * Obtiene la configuración de color del usuario
     */
    public Optional<ColorConfiguracionDTO> obtenerConfiguracionColor(Usuario usuario) {
        return colorRepository.findByUsuarioAndActivoTrue(usuario)
                .map(this::convertToDTO);
    }

    /**
     * Obtiene la configuración de color por ID de usuario
     */
    public Optional<ColorConfiguracionDTO> obtenerConfiguracionColorPorUsuarioId(Integer usuarioId) {
        return colorRepository.findByUsuarioId(usuarioId)
                .map(this::convertToDTO);
    }

    /**
     * Guarda o actualiza la configuración de color
     */
    public ColorConfiguracionDTO guardarConfiguracionColor(Usuario usuario, 
                                                            ColorConfiguracion.ColorPrincipal colorPrincipal,
                                                            ColorConfiguracion.ColorAcompañante colorAcompañante) {
        ColorConfiguracion config = colorRepository.findByUsuario(usuario)
                .orElse(new ColorConfiguracion());

        config.setUsuario(usuario);
        config.setColorPrincipal(colorPrincipal);
        config.setColorAcompañante(colorAcompañante);
        config.setActivo(true);

        ColorConfiguracion guardada = colorRepository.save(config);
        return convertToDTO(guardada);
    }

    /**
     * Obtiene la ruta del logo basado en los colores
     */
    public String obtenerUrlLogo(String colorPrincipal, String colorAcompañante) {
        return "/assets/logos/" + colorPrincipal.toLowerCase() + "_" + 
               colorAcompañante.toLowerCase() + ".svg";
    }

    /**
     * Obtiene el nombre del archivo CSS del tema
     */
    public String obtenerNombreCssTheme(String colorPrincipal, String colorAcompañante) {
        return "/assets/estilos/colores/" + colorPrincipal.toLowerCase() + "_" + 
               colorAcompañante.toLowerCase() + ".css";
    }

    /**
     * Convierte la entidad a DTO
     */
    private ColorConfiguracionDTO convertToDTO(ColorConfiguracion config) {
        ColorConfiguracionDTO dto = new ColorConfiguracionDTO();
        dto.setId(config.getId());
        dto.setUsuarioId(config.getUsuario().getId());
        dto.setColorPrincipal(config.getColorPrincipal().toString());
        dto.setColorAcompañante(config.getColorAcompañante().toString());
        String nombreTema = config.getColorPrincipal().toString().toLowerCase() + "_" + 
                           config.getColorAcompañante().toString().toLowerCase();
        dto.setNombreTema(nombreTema);
        dto.setUrlLogo(obtenerUrlLogo(config.getColorPrincipal().toString(), 
                                      config.getColorAcompañante().toString()));
        dto.setActivo(config.getActivo());
        dto.setFechaCreacion(config.getFechaCreacion());
        return dto;
    }

    /**
     * Obtiene los colores principales disponibles
     */
    public String[] obtenerColorePrincipales() {
        return new String[]{"BLANCO", "GRIS", "NEGRO"};
    }

    /**
     * Obtiene los colores acompañantes disponibles
     */
    public String[] obtenerColoresAcompañantes() {
        return new String[]{"VERDE", "MORADO", "AZUL", "ROJO", "GRIS"};
    }
}
