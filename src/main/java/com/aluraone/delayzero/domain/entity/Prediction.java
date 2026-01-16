package com.aluraone.delayzero.domain.entity;

import com.aluraone.delayzero.dto.in.PredictionRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "Predicciones")
@Getter
//Crea constructor sin argumentos (por defecto).
@NoArgsConstructor
//Crea constructor con todos los atrbutos de la clase.
@AllArgsConstructor
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prediccionId;

    private String nombreAerolinea;
    private String origenVuelo;
    private String destinoVuelo;
    private Date fechaVuelo;
    private Time horaVuelo;
    private int distanciaKilometros;
    private boolean vueloRetrasado;
    private float probabilidadRetraso;




    public Prediction(PredictionRequest request) {

        var dateTime = request.fechaPartidaVuelo();

        this.nombreAerolinea = request.nombreAerolinea();
        this.origenVuelo = request.origenVuelo();
        this.destinoVuelo = request.destinoVuelo();
        this.fechaVuelo = Date.valueOf(dateTime.toLocalDate());
        this.horaVuelo = Time.valueOf(dateTime.toLocalTime());
        this.distanciaKilometros = request.distanciaKilometros();
        this.vueloRetrasado = false;
        this.probabilidadRetraso = 0;

    }

    public void setResult(boolean atrasado, float probabilidad) {
        this.vueloRetrasado = atrasado;
        this.probabilidadRetraso = probabilidad;
    }
}
