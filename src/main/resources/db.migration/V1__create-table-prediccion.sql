CREATE TABLE Usuarios(
usuario_id SERIAL PRIMARY KEY,
nombre VARCHAR(100),
nombre_usuario VARCHAR(200),
contrasena VARCHAR(100),
email VARCHAR(320)
);

CREATE TABLE Temas(
tema_id SERIAL PRIMARY KEY,
titulo VARCHAR(120) NOT NULL,
mensaje VARCHAR(500) NOT NULL,
fechaCreacion TIMESTAMPTZ NOT NULL,
status VARCHAR(20) NOT NULL,
autor_id INTEGER REFERENCES Usuarios (usuario_id)
);