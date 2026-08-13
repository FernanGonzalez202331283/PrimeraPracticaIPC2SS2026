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
    CONSTRAINT fk_receta_producto FOREIGN KEY(codigo_producto) REFERENCES producto(codigo_producto),
    CONSTRAINT fk_receta_insumo FOREIGN KEY(codigo_insumo) REFERENCES insumo(codigo_insumo)
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

INSERT INTO empleado
(dpi, nombre, rol, jornada, salario, fecha_contratacion, estado)
VALUES
('1234567890101', 'Carlos López', 'MESERO', 'MATUTINA', 3500.00, '2026-08-01', TRUE),
('1234567890102', 'María García', 'MESERO', 'VESPERTINA', 3500.00, '2026-08-01', TRUE),
('1234567890103', 'José Martínez', 'BARISTA', 'MATUTINA', 4200.00, '2026-08-01', TRUE),
('1234567890104', 'Ana Pérez', 'COCINA', 'VESPERTINA', 4000.00, '2026-08-01', TRUE);

INSERT INTO insumo
(nombre, unidad_medida, stock_actual, stock_minimo, costo)
VALUES
('Café en grano', 'LIBRA', 20.00, 5.00, 65.00),
('Leche entera', 'LITRO', 25.00, 8.00, 12.00),
('Azúcar', 'LIBRA', 15.00, 5.00, 6.00),
('Chocolate en polvo', 'LIBRA', 10.00, 3.00, 28.00),
('Hielo', 'LIBRA', 20.00, 8.00, 3.00),
('Harina', 'LIBRA', 20.00, 5.00, 5.50),
('Huevos', 'UNIDAD', 50.00, 15.00, 1.25),
('Pan artesanal', 'UNIDAD', 30.00, 10.00, 4.50);

INSERT INTO compra_insumo
(fecha, total)
VALUES
('2026-08-01', 650.00),
('2026-08-10', 350.00);

INSERT INTO detalle_compra
(id_compra, codigo_insumo, cantidad, precio_unitario, subtotal)
VALUES
(1, 1, 5.00, 65.00, 325.00),
(1, 2, 20.00, 12.00, 240.00),
(1, 3, 10.00, 6.00, 60.00),
(2, 4, 5.00, 28.00, 140.00);

INSERT INTO producto
(nombre, categoria, precio, fotografia)
VALUES
(
    'Café Americano',
    'BEBIDA_CALIENTE',
    18.00,
    'https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?q=80&w=1000&auto=format&fit=crop'
),
(
    'Café Latte',
    'BEBIDA_CALIENTE',
    25.00,
    'https://images.unsplash.com/photo-1572449043416-55f4685c9bb7?q=80&w=1000&auto=format&fit=crop'
),
(
    'Cappuccino',
    'BEBIDA_CALIENTE',
    27.00,
    'https://images.unsplash.com/photo-1509042239860-f550ce710b93?q=80&w=1000&auto=format&fit=crop'
),
(
    'Chocolate Caliente',
    'BEBIDA_CALIENTE',
    25.00,
    'https://images.unsplash.com/photo-1542990253-0d0f5be5f0ed?q=80&w=1000&auto=format&fit=crop'
),
(
    'Iced Coffee',
    'BEBIDA_FRIA',
    28.00,
    'https://images.unsplash.com/photo-1461023058943-07fcbe16d735?q=80&w=1000&auto=format&fit=crop'
),
(
    'Sándwich de Jamón y Queso',
    'COMIDA',
    38.00,
    'https://images.unsplash.com/photo-1528735602780-2552fd46c7af?q=80&w=1000&auto=format&fit=crop'
);

INSERT INTO receta
(codigo_producto, codigo_insumo, cantidad)
VALUES
(1, 1, 0.05),
(1, 3, 0.01);

INSERT INTO receta
(codigo_producto, codigo_insumo, cantidad)
VALUES
(2, 1, 0.05),
(2, 2, 0.20),
(2, 3, 0.01);

INSERT INTO receta
(codigo_producto, codigo_insumo, cantidad)
VALUES
(3, 1, 0.05),
(3, 2, 0.15),
(3, 3, 0.01);

INSERT INTO receta
(codigo_producto, codigo_insumo, cantidad)
VALUES
(4, 2, 0.25),
(4, 4, 0.04),
(4, 3, 0.01);

INSERT INTO receta
(codigo_producto, codigo_insumo, cantidad)
VALUES
(5, 1, 0.05),
(5, 2, 0.10),
(5, 5, 0.15),
(5, 3, 0.01);

INSERT INTO mesa
(numero_mesa, capacidad, estado)
VALUES
(1, 2, 'LIBRE'),
(2, 4, 'LIBRE'),
(3, 4, 'OCUPADA'),
(4, 6, 'LIBRE');

INSERT INTO cuenta
(numero_mesa, dpi_mesero, fecha, hora_ocupacion,
 hora_liberacion, estado, propina, total)
VALUES
(3, '1234567890101', '2026-08-13', '10:00:00',
 NULL, 'ABIERTA', 0.00, 52.00),

(1, '1234567890102', '2026-08-12', '09:00:00',
 '10:00:00', 'PAGADA', 5.00, 48.00),

(2, '1234567890102', '2026-08-12', '11:30:00',
 '12:30:00', 'PAGADA', 8.00, 73.00);

INSERT INTO detalle_cuenta
(id_cuenta, codigo_producto, cantidad, precio, subtotal)
VALUES
(1, 2, 1, 25.00, 25.00),
(1, 3, 1, 27.00, 27.00);

INSERT INTO detalle_cuenta
(id_cuenta, codigo_producto, cantidad, precio, subtotal)
VALUES
(2, 1, 1, 18.00, 18.00),
(2, 2, 1, 25.00, 25.00);