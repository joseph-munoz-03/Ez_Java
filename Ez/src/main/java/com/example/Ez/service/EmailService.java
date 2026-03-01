package com.example.Ez.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
@Transactional
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envía correo cuando se crea un contrato
     */
    public void enviarCorreoContratoCreado(String emailIngeniero, String emailUsuario, 
                                           String nombreIngeniero, String nombreUsuario,
                                           String tituloContrato, String urlContrato) {
        String asunto = "Nuevo Contrato Creado - " + tituloContrato;
        
        // Correo al ingeniero
        String mensajeIngeniero = "Hola " + nombreIngeniero + ",\n\n" +
                "Se ha creado un nuevo contrato.\n" +
                "Título: " + tituloContrato + "\n" +
                "Contratante: " + nombreUsuario + "\n\n" +
                "Ver contrato: " + urlContrato + "\n\n" +
                "Saludos,\nEquipo Ez";
        
        enviarCorreo(emailIngeniero, asunto, mensajeIngeniero);
        
        // Correo al usuario
        String mensajeUsuario = "Hola " + nombreUsuario + ",\n\n" +
                "El ingeniero " + nombreIngeniero + " ha confirmado el contrato.\n" +
                "Título: " + tituloContrato + "\n\n" +
                "Ver contrato: " + urlContrato + "\n\n" +
                "Saludos,\nEquipo Ez";
        
        enviarCorreo(emailUsuario, asunto, mensajeUsuario);
    }

    /**
     * Envía correo cuando se realiza un pago
     */
    public void enviarCorreoPagoRealizado(String emailUsuario, String nombreUsuario,
                                          String monto, String contrato) {
        String asunto = "Pago Realizado - " + contrato;
        String mensaje = "Hola " + nombreUsuario + ",\n\n" +
                "Se ha realizado un pago exitoso.\n" +
                "Monto: $" + monto + "\n" +
                "Contrato: " + contrato + "\n\n" +
                "Gracias por usar Ez.\n\n" +
                "Saludos,\nEquipo Ez";
        
        enviarCorreo(emailUsuario, asunto, mensaje);
    }

    /**
     * Envía correo cuando se recarga saldo
     */
    public void enviarCorreoRecargaSaldo(String emailUsuario, String nombreUsuario, String monto) {
        String asunto = "Recarga de Saldo Exitosa";
        String mensaje = "Hola " + nombreUsuario + ",\n\n" +
                "Tu saldo ha sido recargado exitosamente.\n" +
                "Monto recargado: $" + monto + "\n\n" +
                "Gracias,\nEquipo Ez";
        
        enviarCorreo(emailUsuario, asunto, mensaje);
    }

    /**
     * Envía correo cuando se retira saldo
     */
    public void enviarCorreoRetiroSaldo(String emailUsuario, String nombreUsuario, 
                                        String monto, String cuenta) {
        String asunto = "Solicitud de Retiro de Saldo";
        String mensaje = "Hola " + nombreUsuario + ",\n\n" +
                "Tu solicitud de retiro ha sido procesada.\n" +
                "Monto a retirar: $" + monto + "\n" +
                "Cuenta destino: " + cuenta + "\n\n" +
                "El dinero se transferirá en los próximos 3-5 días hábiles.\n\n" +
                "Saludos,\nEquipo Ez";
        
        enviarCorreo(emailUsuario, asunto, mensaje);
    }

    /**
     * Envía correo de actualización de perfil
     */
    public void enviarCorreoActualizacionPerfil(String emailUsuario, String nombreUsuario) {
        String asunto = "Perfil Actualizado";
        String mensaje = "Hola " + nombreUsuario + ",\n\n" +
                "Tu perfil ha sido actualizado exitosamente.\n\n" +
                "Si no realizaste este cambio, contacta con soporte.\n\n" +
                "Saludos,\nEquipo Ez";
        
        enviarCorreo(emailUsuario, asunto, mensaje);
    }

    /**
     * Envía correo de cambio de contraseña
     */
    public void enviarCorreoCambioContraseña(String emailUsuario, String nombreUsuario) {
        String asunto = "Contraseña Cambiad";
        String mensaje = "Hola " + nombreUsuario + ",\n\n" +
                "Tu contraseña ha sido cambiada exitosamente.\n\n" +
                "Si no realizaste este cambio, contacta con soporte inmediatamente.\n\n" +
                "Saludos,\nEquipo Ez";
        
        enviarCorreo(emailUsuario, asunto, mensaje);
    }

    /**
     * Envía un correo genérico
     */
    public void enviarCorreo(String destinatario, String asunto, String mensaje) {
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(destinatario);
            email.setSubject(asunto);
            email.setText(mensaje);
            email.setFrom("noreply@ez-platform.com");
            
            mailSender.send(email);
        } catch (Exception e) {
            System.err.println("Error enviando correo: " + e.getMessage());
        }
    }
}
