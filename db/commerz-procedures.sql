-- SELECCIONAR BASE DE DATOS
USE commerz;

-- ELIMINAR PROCEDURES:
DROP PROCEDURE coz_clientes_insertar;
DROP PROCEDURE coz_clientes_actualizar;
DROP PROCEDURE coz_clientes_eliminar;
DROP PROCEDURE coz_clientes_buscar;
DROP PROCEDURE coz_clientes_buscar_id;
DROP PROCEDURE coz_productos_insertar;
DROP PROCEDURE coz_productos_actualizar;
DROP PROCEDURE coz_productos_eliminar;
DROP PROCEDURE coz_productos_buscar;
DROP PROCEDURE coz_productos_buscar_id;
DROP PROCEDURE coz_ventas_insertar;
DROP PROCEDURE coz_ventas_cancelar;
DROP PROCEDURE coz_ventas_buscar;
DROP PROCEDURE coz_productos_buscar_id;

-- Tabla Clientes
-- INSERTAR nuevo cliente
DELIMITER ##
CREATE PROCEDURE coz_clientes_insertar (
    IN new_nombre VARCHAR(50),
    IN new_apellidop VARCHAR(50),
    IN new_apellidom VARCHAR(50),
    IN new_email VARCHAR(100),
    IN new_telefono VARCHAR(15),
    IN new_edad INT(3),
    IN new_direccion VARCHAR(200),
    OUT msg VARCHAR(20)
)
BEGIN
    INSERT INTO coz_clientes (nombre, apellidop, apellidom, email, telefono, edad, direccion)
    VALUES (new_nombre, new_apellidop, new_apellidom, new_email, new_telefono, new_edad, new_direccion);
    SELECT "OK" INTO msg;
END ##
DELIMITER ;
-- ACTUALIZAR cliente
DELIMITER ##
CREATE PROCEDURE coz_clientes_actualizar (
    IN this_idcliente INT UNSIGNED,
    IN new_nombre VARCHAR(50),
    IN new_apellidop VARCHAR(50),
    IN new_apellidom VARCHAR(50),
    IN new_email VARCHAR(100),
    IN new_telefono VARCHAR(15),
    IN new_edad INT(3),
    IN new_direccion VARCHAR(200),
    OUT msg VARCHAR(20)
)
BEGIN
    UPDATE coz_clientes SET nombre = new_nombre, apellidop = new_apellidop, apellidom = new_apellidom, email = new_email, telefono = new_telefono, edad = new_edad, direccion = new_direccion
    WHERE idcliente = this_idcliente;
    SELECT "OK" INTO msg;
END ##
DELIMITER ;
-- ELIMINAR cliente
DELIMITER ##
CREATE PROCEDURE coz_clientes_eliminar (
    IN this_idcliente INT UNSIGNED,
    OUT msg VARCHAR(20)
)
BEGIN
    DECLARE hay_ventas INT;
    SELECT idventa INTO hay_ventas FROM coz_ventas
    WHERE idcliente = this_idcliente LIMIT 1;
    IF hay_ventas IS NULL THEN
        DELETE FROM coz_clientes WHERE idcliente = this_idcliente;
        SELECT "OK" INTO msg;
    ELSE
        SELECT "ERROR" INTO msg;
    END IF;
END ##
DELIMITER ;
-- BUSCAR cliente
DELIMITER ##
CREATE PROCEDURE coz_clientes_buscar (IN query VARCHAR(255))
BEGIN
    SELECT * FROM coz_clientes WHERE CONCAT(
        idcliente , ' ',
        nombre , ' ',
        apellidop , ' ',
        apellidom , ' ',
        email , ' ',
        telefono , ' ',
        edad , ' ',
        direccion , ' ') LIKE CONCAT('%', query, '%');
END ##
DELIMITER ;
-- BUSCAR cliente POR ID
DELIMITER ##
CREATE PROCEDURE coz_clientes_buscar_id (IN this_idcliente INT)
BEGIN
    SELECT * FROM coz_clientes WHERE idcliente = this_idcliente;
END ##
DELIMITER ;

-- Tabla productos
-- INSERTAR nuevo producto
DELIMITER ##
CREATE PROCEDURE coz_productos_insertar (
    IN new_nombre VARCHAR(50),
    IN new_precio FLOAT(8, 4),
    IN new_fechac DATE,
    IN new_existencias INT(6),
    OUT msg VARCHAR(20)
)
BEGIN
    INSERT INTO coz_productos (nombre, precio, fechac, existencias)
    VALUES (new_nombre, new_precio, new_fechac, new_existencias);
    SELECT "OK" INTO msg;
END ##
DELIMITER ;
-- ACTUALIZAR producto
DELIMITER ##
CREATE PROCEDURE coz_productos_actualizar (
    IN this_idproducto INT UNSIGNED,
    IN new_nombre VARCHAR(50),
    IN new_precio FLOAT(8, 4),
    IN new_fechac DATE,
    IN new_existencias INT(6),
    OUT msg VARCHAR(20)
)
BEGIN
    UPDATE coz_productos SET nombre = new_nombre, precio = new_precio, fechac = new_fechac, existencias = new_existencias
    WHERE idproducto = this_idproducto;
    SELECT "OK" INTO msg;
END ##
DELIMITER ;
-- ELIMINAR producto
DELIMITER ##
CREATE PROCEDURE coz_productos_eliminar (
    IN this_idproducto INT UNSIGNED,
    OUT msg VARCHAR(20)
)
BEGIN
    DECLARE hay_ventas INT;
    SELECT idventa INTO hay_ventas FROM coz_ventas
    WHERE idproducto = this_idproducto LIMIT 1;
    IF hay_ventas IS NULL THEN
        DELETE FROM coz_productos WHERE idproducto = this_idproducto;
        SELECT "OK" INTO msg;
    ELSE
        SELECT "ERROR" INTO msg;
    END IF;
END ##
DELIMITER ;
-- BUSCAR producto
DELIMITER ##
CREATE PROCEDURE coz_productos_buscar (
    IN query VARCHAR(255),
    IN desde DATE,
    IN hasta DATE
)
BEGIN
    SELECT * FROM coz_productos 
    WHERE CONCAT(
        idproducto , ' ',
        nombre , ' ',
        precio , ' ',
        existencias , ' ') LIKE CONCAT('%', query, '%')
    AND (fechac BETWEEN desde AND hasta)    ;
END ##
DELIMITER ;
-- BUSCAR producto POR ID
DELIMITER ##
CREATE PROCEDURE coz_productos_buscar_id (IN this_idproducto INT)
BEGIN
    SELECT * FROM coz_productos WHERE idproducto = this_idproducto;
END ##
DELIMITER ;


-- Tabla Ventas
-- INSERTAR nueva venta
DELIMITER ##
CREATE PROCEDURE coz_ventas_insertar (
    IN new_idcliente INT UNSIGNED,
    IN new_idproducto INT UNSIGNED,
    IN new_cantidad INT(6),
    IN new_subtotal FLOAT(8, 4),
    IN new_iva FLOAT(8, 4),
    IN new_total FLOAT(8, 4),
    IN new_estado VARCHAR(20),
    OUT msg VARCHAR(20)
)
BEGIN
    DECLARE hay_stock INT;
    -- Ver si hay suficiente...
    SELECT existencias INTO hay_stock FROM coz_productos
    WHERE idproducto = new_idproducto AND new_cantidad <= existencias;
    IF hay_stock IS NOT NULL THEN
        -- Insertar venta
        INSERT INTO coz_ventas (idcliente, idproducto, cantidad, subtotal, iva, total, estado)
        VALUES (new_idcliente, new_idproducto, new_cantidad, new_subtotal, new_iva, new_total, new_estado);
        -- Actualizar existencias
        UPDATE coz_productos SET existencias = (hay_stock - new_cantidad)
        WHERE idproducto = new_idproducto;
        -- Mensaje
        SELECT "OK" INTO msg;
    ELSE
        SELECT "ERROR" INTO msg;
    END IF;
END ##
DELIMITER ;
-- CANCELAR venta
DELIMITER ##
CREATE PROCEDURE coz_ventas_cancelar (
    IN this_idventa INT UNSIGNED,
    OUT msg VARCHAR(20)
)
BEGIN
    DECLARE hay_venta INT;
    DECLARE este_producto INT;
    DECLARE existencias_actuales INT;
    DECLARE existencias_devolver INT;
    -- Ver si existe y no ha sido cancelada...
    SELECT idventa INTO hay_venta FROM coz_ventas WHERE idventa = this_idventa AND estado != "cancelado";
    IF hay_venta IS NOT NULL THEN
        -- Ver el id
        SELECT idproducto INTO este_producto FROM coz_ventas WHERE idventa = hay_venta;
        -- Ver cuanto hay de existencias
        SELECT existencias INTO existencias_actuales FROM coz_productos WHERE idproducto = este_producto;
        -- Ver cuanto hay que devolver
        SELECT cantidad INTO existencias_devolver FROM coz_ventas WHERE idventa = hay_venta;
        -- Actualizar estado
        UPDATE coz_ventas SET estado = "cancelado"
        WHERE idventa = this_idventa;
        -- Devolver existencias
        UPDATE coz_productos SET existencias = (existencias_actuales + existencias_devolver)
        WHERE idproducto = este_producto;
        -- Mensaje
        SELECT "OK" INTO msg;
    ELSE
        SELECT "ERROR" INTO msg;
    END IF;
END ##
DELIMITER ;
-- BUSCAR ventas
DELIMITER ##
CREATE PROCEDURE coz_ventas_buscar (
    IN query VARCHAR(255),
    IN desde DATETIME,
    IN hasta DATETIME
)
BEGIN
    SELECT coz_ventas.idventa, CONCAT(coz_clientes.nombre," ",coz_clientes.apellidop," ",coz_clientes.apellidom) AS cliente, coz_productos.nombre as producto, coz_ventas.cantidad, coz_ventas.fechav, coz_ventas.subtotal, coz_ventas.iva, coz_ventas.total, coz_ventas.estado
    FROM coz_productos INNER JOIN (coz_clientes INNER JOIN coz_ventas ON coz_clientes.idcliente = coz_ventas.idcliente) ON coz_productos.idproducto = coz_ventas.idproducto
    WHERE CONCAT(
        idventa , ' ',
        coz_clientes.nombre , ' ',
        coz_clientes.apellidop , ' ',
        coz_clientes.apellidom , ' ',
        coz_productos.nombre , ' ',
        cantidad , ' ',
        fechav , ' ',
        subtotal , ' ',
        iva , ' ',
        total , ' ',
        estado , ' ') LIKE CONCAT('%', query, '%')
        AND (fechav BETWEEN desde AND hasta);
END ##
DELIMITER ;
-- BUSCAR venta POR ID
DELIMITER ##
CREATE PROCEDURE coz_ventas_buscar_id (IN this_idventa INT)
BEGIN
    SELECT * FROM coz_ventas WHERE idventa = this_idventa;
END ##
DELIMITER ;