-- Usar la base de datos comun
CREATE DATABASE commerz DEFAULT CHARACTER SET utf8;
USE commerz;
-- Creación del usuario limitado, con permisos de procedimientos y de inicio de sesión en PERSE
CREATE USER commerz_user IDENTIFIED BY "commerz";
GRANT EXECUTE ON commerz.* TO 'commerz_user'@'%' IDENTIFIED BY "commerz";
GRANT SELECT, DELETE, UPDATE ON `mysql`.`proc` TO 'commerz_user'@'%';
FLUSH PRIVILEGES;

-- Eliminar tablas
DROP TABLE coz_ventas;
DROP TABLE coz_clientes;
DROP TABLE coz_productos;

-- Crear tabla de clientes
CREATE TABLE coz_clientes (
    idcliente INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellidop VARCHAR(50) NOT NULL,
    apellidom VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefono VARCHAR(15) NOT NULL,
    edad INT(3) NOT NULL,
    direccion VARCHAR(200) NOT NULL
);

-- Crear tabla de productos
CREATE TABLE coz_productos (
    idproducto INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    precio FLOAT(8, 4) NOT NULL,
    fechac DATE,
    existencias INT(6) NOT NULL
);

-- Crear tabla de ventas
CREATE TABLE coz_ventas (
    idventa INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    idcliente INT UNSIGNED NOT NULL,
    idproducto INT UNSIGNED NOT NULL,
    cantidad INT(6) NOT NULL,
    fechav DATETIME DEFAULT NOW(),
    subtotal FLOAT(8, 4) NOT NULL,
    iva FLOAT(8, 4) NOT NULL,
    total FLOAT(8, 4) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT "vigente"
);

-- Asignación de claves foraneas
ALTER TABLE coz_ventas ADD CONSTRAINT coz_ventas_x_clientes FOREIGN KEY (idcliente) REFERENCES coz_clientes (idcliente);
ALTER TABLE coz_ventas ADD CONSTRAINT coz_ventas_x_productos FOREIGN KEY (idproducto) REFERENCES coz_productos (idproducto);
