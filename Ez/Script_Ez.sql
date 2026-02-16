-- =========================
-- CREAR BASE DE DATOS
-- =========================
CREATE DATABASE IF NOT EXISTS ez_dtabse
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE ez_dtabse;

-- =========================
-- ELIMINAR TABLAS SI EXISTEN
-- =========================
DROP TABLE IF EXISTS usuarios_roles;
DROP TABLE IF EXISTS users_ez;
DROP TABLE IF EXISTS roles_ez;

-- =========================
-- TABLA DE USUARIOS
-- =========================
CREATE TABLE users_ez (
    id_users INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    documento_user BIGINT UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    fecha_nacimiento DATE,
    genero VARCHAR(20) DEFAULT 'No_Especificado',
    telefono VARCHAR(20),
    estado ENUM('activo','inactivo','suspendido') DEFAULT 'activo',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    fecha_suspension DATE,
    dias_suspension INT,
    INDEX idx_email (email),
    INDEX idx_documento (documento_user),
    INDEX idx_estado (estado)
) ENGINE=InnoDB;

-- =========================
-- TABLA DE ROLES
-- =========================
CREATE TABLE roles_ez (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    tipo_rol ENUM('ADMIN','INGENIERO','USUARIO') NOT NULL,
    descripcion VARCHAR(255),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_tipo_rol (tipo_rol)
) ENGINE=InnoDB;

-- =========================
-- TABLA RELACIÓN USUARIO-ROL
-- =========================
CREATE TABLE usuarios_roles (
    id_usuario_rol INT AUTO_INCREMENT PRIMARY KEY,
    id_user INT NOT NULL,
    id_rol INT NOT NULL,
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_user) REFERENCES users_ez(id_users) ON DELETE CASCADE,
    FOREIGN KEY (id_rol) REFERENCES roles_ez(id_rol) ON DELETE CASCADE,
    UNIQUE KEY uk_usuario_rol (id_user, id_rol)
) ENGINE=InnoDB;

-- =========================
-- INSERTAR ROLES
-- =========================
INSERT INTO roles_ez (tipo_rol, descripcion) VALUES
('ADMIN', 'Administrador del sistema'),
('INGENIERO', 'Ingeniero de sistemas'),
('USUARIO', 'Usuario estándar');

-- =========================
-- INSERTAR USUARIOS (BCrypt)
-- =========================
INSERT INTO users_ez 
(nombre, apellido, documento_user, email, contrasena, fecha_nacimiento, genero, telefono)
VALUES 
('Carlos', 'Administrador', 1001, 'admin@gmail.com',
'$2a$10$Iq.fJVG/GzaFfUIGGHOEkOKwIeVeQPAhKjqN7D0LKVvR5H5mSvYzC',
'1985-06-15', 'Masculino', '3001234567'),

('Juan', 'Ingeniero', 1002, 'ingeniero@gmail.com',
'$2a$10$0bJZ2p5Qyko8hN7xD5pYuemXLm3Y9z2K.7qL3Xq0y0L5mVzY0Qr3K',
'1990-08-22', 'Masculino', '3001234568'),

('Maria', 'Cliente', 1003, 'cliente@gmail.com',
'$2a$10$XjpVEZxhANM8p9Vk5K7Y2.e9L0mN3X2p5Q1R8S6T4U0VwXyZ1cBbC',
'1995-12-10', 'Femenino', '3001234569'),

('Esteban', 'Admin', 1004, 'esteban@gmail.com',
'$2a$10$dXJ3SW6G7P50eexKGGyKK.N7Yz7n.3Yk4h3DKKW9p0J6xN0.K0vW',
'1988-03-20', 'Masculino', '3001234570'),

('Joseph', 'Admin', 1005, 'joseph@gmail.com',
'$2a$10$slYQmyNdGzin7olVN84COuYvatvCHF5.pVl0MN1f9sGHVWZsJLxJO',
'1992-05-10', 'Masculino', '3001234571');

-- =========================
-- ASIGNAR ROLES
-- =========================
INSERT INTO usuarios_roles (id_user, id_rol)
SELECT u.id_users, r.id_rol
FROM users_ez u
JOIN roles_ez r ON r.tipo_rol = 'ADMIN'
WHERE u.email = 'admin@gmail.com';

INSERT INTO usuarios_roles (id_user, id_rol)
SELECT u.id_users, r.id_rol
FROM users_ez u
JOIN roles_ez r ON r.tipo_rol = 'INGENIERO'
WHERE u.email = 'ingeniero@gmail.com';

INSERT INTO usuarios_roles (id_user, id_rol)
SELECT u.id_users, r.id_rol
FROM users_ez u
JOIN roles_ez r ON r.tipo_rol = 'USUARIO'
WHERE u.email = 'cliente@gmail.com';

INSERT INTO usuarios_roles (id_user, id_rol)
SELECT u.id_users, r.id_rol
FROM users_ez u
JOIN roles_ez r ON r.tipo_rol = 'ADMIN'
WHERE u.email = 'esteban@gmail.com';

INSERT INTO usuarios_roles (id_user, id_rol)
SELECT u.id_users, r.id_rol
FROM users_ez u
JOIN roles_ez r ON r.tipo_rol = 'ADMIN'
WHERE u.email = 'joseph@gmail.com';
