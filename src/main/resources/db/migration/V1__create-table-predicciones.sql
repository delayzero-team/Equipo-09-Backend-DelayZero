CREATE TABLE Predicciones(
prediccion_id SERIAL PRIMARY KEY,
nombre_aerolinea VARCHAR(100),
origen_vuelo VARCHAR(100),
destino_vuelo VARCHAR(100),
fecha_vuelo DATE,
hora_vuelo TIME,
distancia_kilometros INT,
vuelo_retrasado BOOL,
probabilidad_retraso FLOAT
);