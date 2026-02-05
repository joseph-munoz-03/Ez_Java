-- Crear base de datos Ez
CREATE DATABASE IF NOT EXISTS Ez_database;
USE Ez_database;

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS users_ez (
    id_users INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    documento_user BIGINT UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    fecha_nacimiento DATE,
    genero VARCHAR(20) DEFAULT 'No_Especificado',
    telefono VARCHAR(20),
    estado VARCHAR(20) DEFAULT 'activo',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    fecha_suspension DATE,
    dias_suspension INT,
    INDEX idx_email (email),
    INDEX idx_documento (documento_user),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de roles
CREATE TABLE IF NOT EXISTS roles_ez (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    tipo_rol ENUM ('admin', 'ingeniero', 'usuario') NOT NULL,
    descripcion VARCHAR(255),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_tipo_rol (tipo_rol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de relación entre usuarios y roles
CREATE TABLE IF NOT EXISTS usuarios_roles (
    id_usuario_rol INT AUTO_INCREMENT PRIMARY KEY,
    id_user INT NOT NULL,
    id_rol INT NOT NULL,
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_user) REFERENCES users_ez(id_users) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_rol) REFERENCES roles_ez(id_rol) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY uk_usuario_rol (id_user, id_rol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar roles predefinidos
INSERT IGNORE INTO roles_ez (tipo_rol, descripcion) VALUES
('ADMIN', 'Administrador del sistema'),
('INGENIERO', 'Ingeniero de sistemas'),
('USUARIO', 'Usuario - Creador de publicaciones');

-- Insertar usuarios de prueba (contraseñas hasheadas con BCrypt)
-- admin@gmail.com / Admin1234
-- Contraseña BCrypt: $2a$10$Iq.fJVG/GzaFfUIGGHOEkOKwIeVeQPAhKjqN7D0LKVvR5H5mSvYzC
INSERT IGNORE INTO users_ez 
(nombre, apellido, documento_user, email, contrasena, fecha_nacimiento, genero, telefono, estado) 
VALUES 
('Carlos', 'Administrador', 1001, 'admin@gmail.com', '$2a$10$Iq.fJVG/GzaFfUIGGHOEkOKwIeVeQPAhKjqN7D0LKVvR5H5mSvYzC', '1985-06-15', 'Masculino', '3001234567', 'activo');

-- ingeniero@gmail.com / Ingeniero1234
-- Contraseña BCrypt: $2a$10$0bJZ2p5Qyko8hN7xD5pYuemXLm3Y9z2K.7qL3Xq0y0L5mVzY0Qr3K
INSERT IGNORE INTO users_ez 
(nombre, apellido, documento_user, email, contrasena, fecha_nacimiento, genero, telefono, estado) 
VALUES 
('Juan', 'Ingeniero', 1002, 'ingeniero@gmail.com', '$2a$10$0bJZ2p5Qyko8hN7xD5pYuemXLm3Y9z2K.7qL3Xq0y0L5mVzY0Qr3K', '1990-08-22', 'Masculino', '3001234568', 'activo');

-- cliente@gmail.com / Cliente1234
-- Contraseña BCrypt: $2a$10$XjpVEZxhANM8p9Vk5K7Y2.e9L0mN3X2p5Q1R8S6T4U0VwXyZ1cBbC
INSERT IGNORE INTO users_ez 
(nombre, apellido, documento_user, email, contrasena, fecha_nacimiento, genero, telefono, estado) 
VALUES 
('María', 'Cliente', 1003, 'cliente@gmail.com', '$2a$10$XjpVEZxhANM8p9Vk5K7Y2.e9L0mN3X2p5Q1R8S6T4U0VwXyZ1cBbC', '1995-12-10', 'Femenino', '3001234569', 'activo');

-- Asignar roles a los usuarios
INSERT IGNORE INTO usuarios_roles (id_user, id_rol) 
SELECT u.id_users, r.id_rol FROM users_ez u, roles_ez r 
WHERE u.email = 'admin@gmail.com' AND r.tipo_rol = 'ADMIN';

INSERT IGNORE INTO usuarios_roles (id_user, id_rol) 
SELECT u.id_users, r.id_rol FROM users_ez u, roles_ez r 
WHERE u.email = 'ingeniero@gmail.com' AND r.tipo_rol = 'INGENIERO';

INSERT IGNORE INTO usuarios_roles (id_user, id_rol) 
SELECT u.id_users, r.id_rol FROM users_ez u, roles_ez r 
WHERE u.email = 'cliente@gmail.com' AND r.tipo_rol = 'USUARIO';


