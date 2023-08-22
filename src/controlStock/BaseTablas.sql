

CREATE DATABASE mundo_computacion;


CREATE TABLE usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  legajo VARCHAR(50) NOT NULL UNIQUE,
  clave VARCHAR(50) NOT NULL
);


CREATE TABLE productos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL,
  precio DECIMAL(10, 2) NOT NULL,
  stock INT NOT NULL
);



INSERT INTO usuarios (legajo,clave) values (1234,1234);

INSERT INTO productos (nombre,precio,stock) values ("MOTHERBOARD",55000.00,1);


SELECT * FROM usuarios;

SELECT * FROM productos;

DELETE FROM productos WHERE id = 2;

DROP TABLE productos;
