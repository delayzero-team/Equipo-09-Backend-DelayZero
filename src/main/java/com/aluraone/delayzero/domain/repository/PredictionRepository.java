package com.aluraone.delayzero.domain.repository;

import com.aluraone.delayzero.domain.entity.Prediction;
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
}
