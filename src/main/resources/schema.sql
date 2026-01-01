-- Script de creación de tablas para el sistema de gestión de pacientes
-- Base de datos: PostgreSQL
-- Esquema: public
-- NOTA: Este script solo crea las tablas si no existen, NO borra datos existentes


-- Eliminar tablas si existen (en orden inverso por dependencias)
-- DROP TABLE IF EXISTS registros_terapia_servicio CASCADE;
-- DROP TABLE IF EXISTS profesionales_pacientes CASCADE;
-- DROP TABLE IF EXISTS profesionales_tipos_terapia CASCADE;
-- DROP TABLE IF EXISTS servicios CASCADE;
-- DROP TABLE IF EXISTS tipos_terapia CASCADE;
-- DROP TABLE IF EXISTS pacientes CASCADE;
-- DROP TABLE IF EXISTS profesionales CASCADE;

-- ============================================
-- TABLA: profesionales
-- ============================================
CREATE TABLE IF NOT EXISTS profesionales (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    profesion VARCHAR(100) NOT NULL,
    numero_cuenta_banco VARCHAR(50) NOT NULL,
    nombre_banco VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP
);

-- Índices para profesionales (solo si no existen)
CREATE INDEX IF NOT EXISTS idx_profesionales_email ON profesionales(email);
CREATE INDEX IF NOT EXISTS idx_profesionales_username ON profesionales(username);

-- ============================================
-- TABLA: pacientes
-- ============================================
CREATE TABLE IF NOT EXISTS pacientes (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    tipo_documento VARCHAR(2) NOT NULL CHECK (tipo_documento IN ('CC', 'TI')),
    documento VARCHAR(50) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP
);

-- Índices para pacientes (solo si no existen)
CREATE INDEX IF NOT EXISTS idx_pacientes_tipo_documento ON pacientes(tipo_documento);
CREATE INDEX IF NOT EXISTS idx_pacientes_documento ON pacientes(documento);
CREATE INDEX IF NOT EXISTS idx_pacientes_tipo_doc_doc ON pacientes(tipo_documento, documento);

-- ============================================
-- TABLA: tipos_terapia
-- ============================================
CREATE TABLE IF NOT EXISTS tipos_terapia (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL UNIQUE,
    valor_unitario DECIMAL(10,2) NOT NULL CHECK (valor_unitario > 0),
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP
);

-- Índices para tipos_terapia (solo si no existen)
CREATE INDEX IF NOT EXISTS idx_tipos_terapia_nombre ON tipos_terapia(nombre);

-- ============================================
-- TABLA: servicios
-- ============================================
CREATE TABLE IF NOT EXISTS servicios (
    id BIGSERIAL PRIMARY KEY,
    nombre_completo VARCHAR(200) NOT NULL,
    abreviatura VARCHAR(20) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP
);

-- Índices para servicios (solo si no existen)
CREATE INDEX IF NOT EXISTS idx_servicios_abreviatura ON servicios(abreviatura);

-- ============================================
-- TABLA INTERMEDIA: profesionales_tipos_terapia
-- Relación muchos a muchos entre profesionales y tipos_terapia
-- ============================================
CREATE TABLE IF NOT EXISTS profesionales_tipos_terapia (
    profesional_id BIGINT NOT NULL,
    tipo_terapia_id BIGINT NOT NULL,
    PRIMARY KEY (profesional_id, tipo_terapia_id),
    CONSTRAINT fk_profesional_tipo_terapia_profesional 
        FOREIGN KEY (profesional_id) 
        REFERENCES profesionales(id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_profesional_tipo_terapia_tipo_terapia 
        FOREIGN KEY (tipo_terapia_id) 
        REFERENCES tipos_terapia(id) 
        ON DELETE CASCADE
);

-- Índices para profesionales_tipos_terapia (solo si no existen)
CREATE INDEX IF NOT EXISTS idx_profesionales_tipos_terapia_profesional ON profesionales_tipos_terapia(profesional_id);
CREATE INDEX IF NOT EXISTS idx_profesionales_tipos_terapia_tipo_terapia ON profesionales_tipos_terapia(tipo_terapia_id);

-- ============================================
-- TABLA: profesionales_pacientes
-- Relación muchos a muchos entre profesionales y pacientes
-- Incluye información sobre quién creó la relación
-- ============================================
CREATE TABLE IF NOT EXISTS profesionales_pacientes (
    id BIGSERIAL PRIMARY KEY,
    profesional_id BIGINT NOT NULL,
    paciente_id BIGINT NOT NULL,
    creado_por_profesional_id BIGINT NOT NULL,
    fecha_asignacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_profesional_paciente_profesional 
        FOREIGN KEY (profesional_id) 
        REFERENCES profesionales(id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_profesional_paciente_paciente 
        FOREIGN KEY (paciente_id) 
        REFERENCES pacientes(id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_profesional_paciente_creado_por 
        FOREIGN KEY (creado_por_profesional_id) 
        REFERENCES profesionales(id) 
        ON DELETE RESTRICT,
    CONSTRAINT uk_profesional_paciente 
        UNIQUE (profesional_id, paciente_id)
);

-- Índices para profesionales_pacientes (solo si no existen)
CREATE INDEX IF NOT EXISTS idx_profesionales_pacientes_profesional ON profesionales_pacientes(profesional_id);
CREATE INDEX IF NOT EXISTS idx_profesionales_pacientes_paciente ON profesionales_pacientes(paciente_id);
CREATE INDEX IF NOT EXISTS idx_profesionales_pacientes_creado_por ON profesionales_pacientes(creado_por_profesional_id);

-- ============================================
-- COMENTARIOS EN TABLAS Y COLUMNAS
-- ============================================
COMMENT ON TABLE profesionales IS 'Tabla que almacena la información de los profesionales del sistema';
COMMENT ON COLUMN profesionales.email IS 'Email del profesional';
COMMENT ON COLUMN profesionales.username IS 'Nombre de usuario único usado para autenticación';
COMMENT ON COLUMN profesionales.password IS 'Contraseña encriptada (BCrypt)';

COMMENT ON TABLE pacientes IS 'Tabla que almacena la información de los pacientes';
COMMENT ON COLUMN pacientes.tipo_documento IS 'Tipo de documento: CC (Cédula de Ciudadanía) o TI (Tarjeta de Identidad)';

COMMENT ON TABLE tipos_terapia IS 'Tabla que almacena los tipos de terapia disponibles';
COMMENT ON COLUMN tipos_terapia.valor_unitario IS 'Valor unitario del tipo de terapia en formato decimal';

COMMENT ON TABLE servicios IS 'Tabla que almacena los servicios disponibles';

COMMENT ON TABLE profesionales_tipos_terapia IS 'Tabla intermedia para la relación muchos a muchos entre profesionales y tipos de terapia';

COMMENT ON TABLE profesionales_pacientes IS 'Tabla intermedia para la relación muchos a muchos entre profesionales y pacientes';
COMMENT ON COLUMN profesionales_pacientes.creado_por_profesional_id IS 'ID del profesional que creó esta relación (puede ser diferente al profesional_id si se asignó a otro profesional)';

-- ============================================
-- TABLA: registros_terapia_servicio
-- Registros de terapias y servicios realizados a pacientes por profesionales
-- ============================================
CREATE TABLE IF NOT EXISTS registros_terapia_servicio (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    profesional_id BIGINT NOT NULL,
    tipo_terapia_id BIGINT NOT NULL,
    servicio_id BIGINT NOT NULL,
    fecha DATE NOT NULL,
    numero_sesiones INTEGER NOT NULL CHECK (numero_sesiones >= 1 AND numero_sesiones <= 2),
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP,
    CONSTRAINT fk_registro_paciente 
        FOREIGN KEY (paciente_id) 
        REFERENCES pacientes(id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_registro_profesional 
        FOREIGN KEY (profesional_id) 
        REFERENCES profesionales(id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_registro_tipo_terapia 
        FOREIGN KEY (tipo_terapia_id) 
        REFERENCES tipos_terapia(id) 
        ON DELETE RESTRICT,
    CONSTRAINT fk_registro_servicio 
        FOREIGN KEY (servicio_id) 
        REFERENCES servicios(id) 
        ON DELETE RESTRICT,
    CONSTRAINT uk_registro_unico 
        UNIQUE (paciente_id, profesional_id, tipo_terapia_id, servicio_id, fecha)
);

-- Índices para registros_terapia_servicio (solo si no existen)
CREATE INDEX IF NOT EXISTS idx_registros_paciente ON registros_terapia_servicio(paciente_id);
CREATE INDEX IF NOT EXISTS idx_registros_profesional ON registros_terapia_servicio(profesional_id);
CREATE INDEX IF NOT EXISTS idx_registros_tipo_terapia ON registros_terapia_servicio(tipo_terapia_id);
CREATE INDEX IF NOT EXISTS idx_registros_servicio ON registros_terapia_servicio(servicio_id);
CREATE INDEX IF NOT EXISTS idx_registros_fecha ON registros_terapia_servicio(fecha);
CREATE INDEX IF NOT EXISTS idx_registros_paciente_fecha ON registros_terapia_servicio(paciente_id, fecha);

-- Comentarios
COMMENT ON TABLE registros_terapia_servicio IS 'Tabla que almacena los registros de terapias y servicios realizados a pacientes';
COMMENT ON COLUMN registros_terapia_servicio.fecha IS 'Fecha específica en que se realizó la terapia/servicio';
COMMENT ON COLUMN registros_terapia_servicio.numero_sesiones IS 'Número de sesiones del tipo de terapia realizadas en esa fecha';
COMMENT ON COLUMN registros_terapia_servicio.fecha_creacion IS 'Fecha de creación del registro';
COMMENT ON COLUMN registros_terapia_servicio.fecha_actualizacion IS 'Fecha de última actualización del registro';

