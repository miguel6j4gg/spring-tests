-- =====================================================
-- MODELO DE DATOS - FORMULARIOS SKYLIGHT GENERADORES
-- Proyecto académico - Venta, alquiler y mantenimiento
-- =====================================================

-- 1. CLIENTES
CREATE TABLE cliente (
    id_cliente BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tipo_cliente VARCHAR(20) NOT NULL
        CHECK (tipo_cliente IN ('empresa', 'persona')),
    nombre_cliente VARCHAR(100) NOT NULL,
    documento VARCHAR(11) NOT NULL UNIQUE,
    contacto VARCHAR(100),
    correo VARCHAR(150) NOT NULL,
    telefono VARCHAR(9) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. SOLICITUDES DE COTIZACIÓN DE VENTA
CREATE TABLE solicitud_cotizacion (
    id_cotizacion BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_cliente BIGINT NOT NULL,
    tipo_equipo VARCHAR(40) NOT NULL,
    potencia_kw NUMERIC(10,2) NOT NULL CHECK (potencia_kw > 0),
    fase VARCHAR(20) NOT NULL,
    aplicacion VARCHAR(40) NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_cotizacion_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES cliente(id_cliente)
);

-- 3. SOLICITUDES DE ALQUILER
CREATE TABLE solicitud_alquiler (
    id_alquiler BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_cliente BIGINT NOT NULL,
    potencia_kw NUMERIC(10,2) NOT NULL CHECK (potencia_kw > 0),
    tipo_generador VARCHAR(20) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    modalidad VARCHAR(30) NOT NULL,
    ubicacion VARCHAR(200) NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_alquiler_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES cliente(id_cliente),

    CONSTRAINT chk_periodo_alquiler
        CHECK (fecha_fin > fecha_inicio)
);

-- 4. SOLICITUDES DE MANTENIMIENTO
CREATE TABLE solicitud_mantenimiento (
    id_mantenimiento BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_cliente BIGINT NOT NULL,
    tipo_equipo VARCHAR(40) NOT NULL,
    marca VARCHAR(80) NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    tipo_mantenimiento VARCHAR(20) NOT NULL
        CHECK (tipo_mantenimiento IN ('preventivo', 'correctivo')),
    horometro NUMERIC(12,2) NOT NULL CHECK (horometro >= 0),
    fecha_solicitada DATE NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_mantenimiento_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES cliente(id_cliente)
);