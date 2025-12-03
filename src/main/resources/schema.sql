-- =====================================================
-- Script SQL para crear la base de datos completa
-- Sistema de Gestión de Pacientes
-- =====================================================

-- Crear la base de datos (ejecutar como superusuario)
-- CREATE DATABASE gestion_pacientes;
-- \c gestion_pacientes;

-- =====================================================
-- TABLA: roles
-- =====================================================
CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    CONSTRAINT uk_role_name UNIQUE (name)
);

-- =====================================================
-- TABLA: users (Usuarios del sistema)
-- =====================================================
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(100),
    activo BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT uk_user_username UNIQUE (username),
    CONSTRAINT uk_user_email UNIQUE (email)
);

-- =====================================================
-- TABLA: user_roles (Relación muchos a muchos entre usuarios y roles)
-- =====================================================
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- =====================================================
-- TABLA: profesionales
-- =====================================================
CREATE TABLE IF NOT EXISTS profesionales (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    profesion VARCHAR(100) NOT NULL,
    tipo_terapia VARCHAR(100) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT uk_profesional_nombre_usuario UNIQUE (nombre_usuario)
);

-- Eliminar columna valor_por_terapia si existe (migración)
DO $$ 
BEGIN
    IF EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_name = 'profesionales' 
        AND column_name = 'valor_por_terapia'
    ) THEN
        ALTER TABLE profesionales DROP COLUMN valor_por_terapia;
    END IF;
END $$;

-- =====================================================
-- TABLA: tipos_documento
-- =====================================================
CREATE TABLE IF NOT EXISTS tipos_documento (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT uk_tipo_documento_nombre UNIQUE (nombre)
);

-- =====================================================
-- TABLA: servicios_departamentos
-- =====================================================
CREATE TABLE IF NOT EXISTS servicios_departamentos (
    id BIGSERIAL PRIMARY KEY,
    abreviacion VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    activo BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT uk_servicio_abreviacion UNIQUE (abreviacion),
    CONSTRAINT uk_servicio_nombre UNIQUE (nombre)
);

-- =====================================================
-- TABLA: pacientes
-- =====================================================
CREATE TABLE IF NOT EXISTS pacientes (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    tipo_documento_id BIGINT NOT NULL,
    numero_documento VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT uk_paciente_numero_documento UNIQUE (numero_documento),
    CONSTRAINT fk_paciente_tipo_documento FOREIGN KEY (tipo_documento_id) REFERENCES tipos_documento(id) ON DELETE RESTRICT
);

-- =====================================================
-- TABLA: tipos_terapia
-- =====================================================
CREATE TABLE IF NOT EXISTS tipos_terapia (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    valor_unitario DECIMAL(10, 2) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT uk_tipo_terapia_nombre UNIQUE (nombre),
    CONSTRAINT ck_valor_unitario_positivo CHECK (valor_unitario >= 0)
);

-- =====================================================
-- TABLA: terapias
-- =====================================================
CREATE TABLE IF NOT EXISTS terapias (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    profesional_id BIGINT NOT NULL,
    fecha TIMESTAMP NOT NULL,
    servicio_departamento_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_terapia_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id) ON DELETE CASCADE,
    CONSTRAINT fk_terapia_profesional FOREIGN KEY (profesional_id) REFERENCES profesionales(id) ON DELETE CASCADE,
    CONSTRAINT fk_terapia_servicio_departamento FOREIGN KEY (servicio_departamento_id) REFERENCES servicios_departamentos(id) ON DELETE RESTRICT
);

-- =====================================================
-- ÍNDICES para mejorar el rendimiento
-- =====================================================

-- Índices para usuarios
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_activo ON users(activo);

-- Índices para roles
CREATE INDEX IF NOT EXISTS idx_roles_name ON roles(name);

-- Índices para profesionales
CREATE INDEX IF NOT EXISTS idx_profesionales_nombre_usuario ON profesionales(nombre_usuario);

-- Índices para tipos de documento
CREATE INDEX IF NOT EXISTS idx_tipos_documento_nombre ON tipos_documento(nombre);
CREATE INDEX IF NOT EXISTS idx_tipos_documento_activo ON tipos_documento(activo);

-- Índices para servicios/departamentos
CREATE INDEX IF NOT EXISTS idx_servicios_departamentos_abreviacion ON servicios_departamentos(abreviacion);
CREATE INDEX IF NOT EXISTS idx_servicios_departamentos_nombre ON servicios_departamentos(nombre);
CREATE INDEX IF NOT EXISTS idx_servicios_departamentos_activo ON servicios_departamentos(activo);

-- Índices para pacientes
CREATE INDEX IF NOT EXISTS idx_pacientes_numero_documento ON pacientes(numero_documento);
CREATE INDEX IF NOT EXISTS idx_pacientes_tipo_documento_id ON pacientes(tipo_documento_id);
CREATE INDEX IF NOT EXISTS idx_pacientes_nombre_apellido ON pacientes(nombre, apellido);

-- Índices para terapias
CREATE INDEX IF NOT EXISTS idx_terapias_paciente_id ON terapias(paciente_id);
CREATE INDEX IF NOT EXISTS idx_terapias_profesional_id ON terapias(profesional_id);
CREATE INDEX IF NOT EXISTS idx_terapias_servicio_departamento_id ON terapias(servicio_departamento_id);
CREATE INDEX IF NOT EXISTS idx_terapias_fecha ON terapias(fecha);

-- =====================================================
-- DATOS INICIALES: Roles del sistema
-- =====================================================

-- Insertar roles predefinidos
INSERT INTO roles (name, description) VALUES
    ('ADMIN', 'Administrador del sistema con acceso completo'),
    ('PROFESIONAL', 'Profesional de la salud que atiende pacientes')
ON CONFLICT (name) DO NOTHING;

-- =====================================================
-- USUARIO ADMIN POR DEFECTO
-- =====================================================
-- Nota: La contraseña debe ser encriptada con BCrypt antes de insertar
-- Contraseña por defecto: "admin123" (debe ser cambiada en producción)
-- Hash BCrypt: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
INSERT INTO users (username, email, password, nombre_completo, activo) VALUES
    ('admin', 'admin@gestionpacientes.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Administrador del Sistema', true)
ON CONFLICT (username) DO NOTHING;

-- Asignar rol ADMIN al usuario admin
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ADMIN'
ON CONFLICT DO NOTHING;

-- =====================================================
-- DATOS INICIALES: Los datos de tipos de documento, servicios/departamentos 
-- y tipos de terapia deben ser creados desde la aplicación por el administrador
-- =====================================================

-- =====================================================
-- COMENTARIOS FINALES
-- =====================================================
-- Este script crea todas las tablas necesarias para el sistema
-- Asegúrate de:
-- 1. Cambiar la contraseña del usuario admin después de la primera conexión
-- 2. Configurar las credenciales correctas en application.properties
-- 3. Verificar que todas las foreign keys están correctamente referenciadas

