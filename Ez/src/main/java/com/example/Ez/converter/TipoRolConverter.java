package com.example.Ez.converter;

import com.example.Ez.model.Rol;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoRolConverter implements AttributeConverter<Rol.TipoRol, String> {

    @Override
    public String convertToDatabaseColumn(Rol.TipoRol attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.toString();
    }

    @Override
    public Rol.TipoRol convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        
        try {
            // Intenta convertir directamente (mayúsculas)
            return Rol.TipoRol.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Si falla, intenta con el valor original
            return Rol.TipoRol.valueOf(dbData);
        }
    }
}
