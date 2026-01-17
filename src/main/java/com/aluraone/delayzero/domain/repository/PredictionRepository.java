package com.aluraone.delayzero.domain.repository;

import com.aluraone.delayzero.domain.entity.Prediction;
import com.aluraone.delayzero.domain.entity.StatisticsAirline;
import com.aluraone.delayzero.domain.entity.StatisticsHour;
import com.aluraone.delayzero.domain.entity.StatisticsOrigin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {

    @Query(
            """
            SELECT p FROM Prediction p
            WHERE p.origenVuelo = :origen
            AND p.destinoVuelo = :destino
            AND p.fechaVuelo = :dia
            """
            )
    public Optional<Prediction> lookupPrediction(
            @Param("origen") String origen,
            @Param("destino") String destino,
            @Param("dia") Date dia);


    @Query("SELECT COUNT(*) FROM Prediction")
    public int totalPredictions();

    @Query("SELECT COUNT(*) FROM Prediction WHERE vueloRetrasado = false")
    public int totalPunctualPredictions();

    @Query("SELECT COUNT(*) FROM Prediction WHERE vueloRetrasado = true")
    public int totalDelayedPredictions();

    @Query("""
            SELECT (
            ROUND (100.0 * SUM(
            CASE
            	WHEN vueloRetrasado = true THEN 1
            	ELSE 0
            END
            ) / COUNT(*), 2
            ))
            FROM Prediction
            """)
    public float percentageDelayed();

    @Query("""
            SELECT (
            ROUND (100.0 * SUM(
            CASE
            	WHEN vueloRetrasado = true THEN 1
            	ELSE 0
            END
            ) / COUNT(*), 2
            )) AS delayedPorcentage, nombreAerolinea AS nameAirline
            FROM Prediction GROUP BY nombreAerolinea
            """)
    public List<StatisticsAirline> percentageDelayedAirline();

    @Query("""
            SELECT (
            ROUND (100.0 * SUM(
            CASE
            	WHEN vueloRetrasado  = true THEN 1
            	ELSE 0
            END
            ) / COUNT(*), 2
            )) AS delayedPorcentage, origenVuelo AS nameOrigin
            FROM Prediction GROUP BY origenVuelo
            """)
    public  List<StatisticsOrigin> percentageDelayedOrigin();

    @Query("""
            SELECT (
            AVG(
            CASE
            	WHEN vueloRetrasado = true THEN probabilidadRetraso
            	ELSE NULL
            END
            )) AS delayedAverage, horaVuelo AS hour
            FROM Prediction GROUP BY horaVuelo
            """)
    public List<StatisticsHour> delayedAverageHour();
}
