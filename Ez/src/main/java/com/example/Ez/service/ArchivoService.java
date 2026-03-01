package com.example.Ez.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ArchivoService {

    @Value("${archivo.upload.dir:uploads/}")
    private String uploadDir;

    /**
     * Guarda un archivo subido y retorna la ruta relativa
     * 
     * @param archivo Archivo a guardar
     * @param carpeta Carpeta donde guardar (ej: "perfil", "documentos", "imagenes")
     * @return Ruta relativa del archivo
     */
    public String guardarArchivo(MultipartFile archivo, String carpeta) throws IOException {
        if (archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        // Crear carpeta si no existe
        String rutaCarpeta = uploadDir + carpeta;
        Files.createDirectories(Paths.get(rutaCarpeta));

        // Generar nombre único
        String nombreOriginal = archivo.getOriginalFilename();
        String extension = obtenerExtension(nombreOriginal);
        String nombreUnico = UUID.randomUUID() + "." + extension;

        // Guardar archivo
        String rutaCompleta = rutaCarpeta + "/" + nombreUnico;
        archivo.transferTo(new File(rutaCompleta));

        // Retornar ruta relativa
        return "/uploads/" + carpeta + "/" + nombreUnico;
    }

    /**
     * Guarda una foto de perfil
     */
    public String guardarFotoPerfil(MultipartFile archivo, Integer usuarioId) throws IOException {
        String carpeta = "perfil/" + usuarioId;
        return guardarArchivo(archivo, carpeta);
    }

    /**
     * Guarda un documento
     */
    public String guardarDocumento(MultipartFile archivo, Long chatId) throws IOException {
        String carpeta = "documentos/" + chatId;
        return guardarArchivo(archivo, carpeta);
    }

    /**
     * Guarda una imagen de chat
     */
    public String guardarImagenChat(MultipartFile archivo, Long chatId) throws IOException {
        // Validar que sea imagen
        String tipoContenido = archivo.getContentType();
        if (!tipoContenido.startsWith("image/")) {
            throw new IllegalArgumentException("El archivo no es una imagen");
        }

        String carpeta = "chats/" + chatId;
        return guardarArchivo(archivo, carpeta);
    }

    /**
     * Guarda una imagen de publicación
     */
    public String guardarImagenPublicacion(MultipartFile archivo, Long publicacionId) throws IOException {
        // Validar que sea imagen
        String tipoContenido = archivo.getContentType();
        if (!tipoContenido.startsWith("image/")) {
            throw new IllegalArgumentException("El archivo no es una imagen");
        }

        String carpeta = "publicaciones/" + publicacionId;
        return guardarArchivo(archivo, carpeta);
    }

    /**
     * Elimina un archivo
     */
    public boolean eliminarArchivo(String rutaRelativa) {
        try {
            String rutaCompleta = rutaRelativa.replace("/uploads/", uploadDir);
            File archivo = new File(rutaCompleta);
            return archivo.delete();
        } catch (Exception e) {
            System.err.println("Error eliminando archivo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene la extensión de un archivo
     */
    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.contains(".")) {
            return "bin";
        }
        return nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * Valida el tamaño máximo del archivo (5MB)
     */
    public void validarTamañoArchivo(MultipartFile archivo, long maxSize) throws IOException {
        if (archivo.getSize() > maxSize) {
            throw new IllegalArgumentException("El archivo supera el tamaño máximo permitido");
        }
    }

    /**
     * Valida el tipo de archivo
     */
    public boolean validarTipoArchivo(String nombreArchivo, String[] extensionesPermitidas) {
        String extension = obtenerExtension(nombreArchivo);
        for (String ext : extensionesPermitidas) {
            if (extension.equals(ext.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
