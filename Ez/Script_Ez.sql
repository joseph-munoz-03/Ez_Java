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
    genero ENUM ('Femenino', 'Masculino', 'No_Especificado', 'Otro') DEFAULT 'No_Especificado',
    telefono VARCHAR(20),
    estado ENUM ('activo', 'inactivo', 'bloqueado') DEFAULT 'activo',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
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
('admin', 'Administrador del sistema'),
('ingeniero', 'Ingeniero de sistemas'),
('usuario', 'Usuario - Creador de publicaciones');

-- Insertar usuarios de prueba (uno por cada rol)

-- Usuario ADMINISTRADOR
INSERT IGNORE INTO users_ez 
(nombre, apellido, documento_user, email, contrasena, fecha_nacimiento, genero, telefono, estado) 
VALUES 
('Carlos', 'Administrador', 1001, 'admin@gmail.com', 'Admin1234', '1985-06-15', 'Masculino', '3001234567', 'activo');

-- Usuario INGENIERO
INSERT IGNORE INTO users_ez 
(nombre, apellido, documento_user, email, contrasena, fecha_nacimiento, genero, telefono, estado) 
VALUES 
('Juan', 'Ingeniero', 1002, 'ingeniero@gmail.com', 'Ingeniero1234', '1990-08-22', 'Masculino', '3001234568', 'activo');

-- Usuario CLIENTE/USUARIO
INSERT IGNORE INTO users_ez 
(nombre, apellido, documento_user, email, contrasena, fecha_nacimiento, genero, telefono, estado) 
VALUES 
('María', 'Cliente', 1003, 'cliente@gmail.com', 'Cliente1234', '1995-12-10', 'Femenino', '3001234569', 'activo');

-- Asignar roles a los usuarios
INSERT IGNORE INTO usuarios_roles (id_user, id_rol) 
SELECT u.id_users, r.id_rol FROM users_ez u, roles_ez r 
WHERE u.email = 'admin@gmail.com' AND r.tipo_rol = 'admin';

INSERT IGNORE INTO usuarios_roles (id_user, id_rol) 
SELECT u.id_users, r.id_rol FROM users_ez u, roles_ez r 
WHERE u.email = 'ingeniero@gmail.com' AND r.tipo_rol = 'ingeniero';

INSERT IGNORE INTO usuarios_roles (id_user, id_rol) 
SELECT u.id_users, r.id_rol FROM users_ez u, roles_ez r 
WHERE u.email = 'cliente@gmail.com' AND r.tipo_rol = 'usuario';


