/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  fernan
 * Created: 3 ago 2026
 */

CREATE DATABASE cafeteria;
USE cafeteria;

--TABLA PARA LOS EMPLEADOS--

CREATE TABLE empleado(
    dpi VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR (50) NOT NULL,
    rol ENUM ('MESERO', 'COCINA', 'BARISTA','ADMINISTRADOR')NOT NULL,
    jornada ENUM('MATUTINA', 'VESPERTINA', 'NOCTURNA')NOT NULL,
    salario DECIMAL(10,2) NOT NULL CHECK(salario>0),
    fecha_contratacion DATE NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT pk_empleado PRIMARY KEY (dpi)
);

--TABLA PARA NOMINAS--

CREATE TABLE  nomina(
    codigo_nomina INT AUTO_INCREMENT,
    dpi_empleado VARCHAR(50) NOT NULL,
    fecha_emision DATE NOT NULL,
    tipo_de_pago ENUM('QUINCENA','FIN_DE_MES') NOT NULL,
    estado_pago ENUM('PENDIENTE', 'PAGADO') DEFAULT 'PENDIENTE',
    monto DECIMAL(10,2) NOT NULL,

    CONSTRAINT pK_nomina PRIMARY KEY (codigo_nomina),
    CONSTRAINT fk_dpi_empleado FOREIGN KEY (dpi_empleado) REFERENCES empleado(dpi)
);

--TABLA INSUMO --

CREATE TABLE insumo(
    codigo_insumo INT AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    unidad_medida VARCHAR(30) NOT NULL,
    stock_actual DECIMAL (10,2) NOT NULL,
    stock_minimo DECIMAL (10,2) NOT NULL,
    costo DECIMAL (10,2) NOT NULL,

    CONSTRAINT pk_insumo PRIMARY KEY (codigo_insumo)
);

-- TABLA PARA COMPRAR INSUMOS--

CREATE TABLE compra_insumo(
    id_compra INT AUTO_INCREMENT,
    fecha DATE NOT NULL,
    total DECIMAL (10,2) NOT NULL,

    CONSTRAINT pk_compra_insumo PRIMARY KEY (id_compra)
);

-- TABLA PARA DETALLE DE UNA COMPRA-- 

CREATE TABLE detalle_compra(
    id_detalle INT AUTO_INCREMENT,
    id_compra INT NOT NULL,
    codigo_insumo INT NOT NULL,
    cantidad DECIMAL (10,2) NOT NULL,
    precio_unitario DECIMAL (10,2) NOT NULL,
    subtotal DECIMAL (10,2) NOT NULL,
    
    CONSTRAINT pk_detalle_compra PRIMARY KEY (id_detalle),
    CONSTRAINT fk_id_compra FOREIGN KEY (id_compra) REFERENCES compra_insumo(id_compra),
    CONSTRAINT fk_codigo_insumo FOREIGN KEY (codigo_insumo) REFERENCES insumo(codigo_insumo)
);

--TABLA PRODUCTO--
CREATE TABLE producto(
    codigo_producto INT AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    categoria ENUM(
        'BEBIDA_CALIENTE',
        'BEBIDA_FRIA',
        'POSTRE',
        'COMIDA'
    ) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    fotografia VARCHAR(255) NOT NULL,
    
    CONSTRAINT pk_producto PRIMARY KEY (codigo_producto)
);

-- TABLA DE RECETA--
CREATE TABLE receta( 
    id_receta INT AUTO_INCREMENT,
    codigo_producto INT NOT NULL,
    codigo_insumo INT NOT NULL,
    cantidad DECIMAL(10,2) NOT NULL,

    CONSTRAINT pk_receta PRIMARY KEY (id_receta),
    CONSTRAINT fk_codigo_producto FOREIGN KEY(codigo_producto) REFERENCES producto(codigo_producto),
    CONSTRAINT fk_codigo_insumo FOREIGN KEY(codigo_insumo) REFERENCES insumo(codigo_insumo)
);

--TABLA PAR MESA--
CREATE TABLE mesa(
    numero_mesa INT,
    capacidad INT NOT NULL,
    estado ENUM(
        'LIBRE',
        'OCUPADA'
    ) DEFAULT 'LIBRE',
    
    CONSTRAINT pk_mesa PRIMARY KEY (numero_mesa)
);

-- TABLA PARA CUENTA--
CREATE TABLE cuenta(
    id_cuenta INT AUTO_INCREMENT,
    numero_mesa INT NOT NULL,
    dpi_mesero VARCHAR(50) NOT NULL,
    fecha DATE NOT NULL,
    hora_ocupacion TIME NOT NULL,
    hora_liberacion TIME,
    estado ENUM(
        'ABIERTA',
        'PAGADA'
    ) DEFAULT 'ABIERTA',
    propina DECIMAL(10,2) DEFAULT 0,
    total DECIMAL(10,2) DEFAULT 0,
    
    CONSTRAINT pk_cuenta PRIMARY KEY (id_cuenta),
    CONSTRAINT fk_numero_mesa FOREIGN KEY(numero_mesa) REFERENCES mesa(numero_mesa),
    CONSTRAINT fk_dpi_mesero FOREIGN KEY(dpi_mesero) REFERENCES empleado(dpi)
);

-- TABLA PARA DETALLE DE LA CUENTA--
CREATE TABLE detalle_cuenta(
    id_detalle INT AUTO_INCREMENT,
    id_cuenta INT NOT NULL,
    codigo_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT pk_detalle_cuenta PRIMARY KEY (id_detalle),
    CONSTRAINT fk_id_cuenta FOREIGN KEY (id_cuenta) REFERENCES cuenta(id_cuenta),
    CONSTRAINT fk_codigo_producto FOREIGN KEY(codigo_producto) REFERENCES producto(codigo_producto)
);