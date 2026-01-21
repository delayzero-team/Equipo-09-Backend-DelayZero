package com.aluraone.delayzero.domain.repository;

import com.aluraone.delayzero.domain.entity.Prediction;
import com.aluraone.delayzero.dto.in.PredictionRequest; // Import PredictionRequest
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate; // Keep LocalDate for test setup
import java.time.LocalDateTime; // For PredictionRequest
import java.time.LocalTime; // Add this import
import java.util.Optional;
import java.util.Date; // For repository lookup

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test") // Use application-test.properties for H2
class PredictionRepositoryTest {

    @Autowired
    private PredictionRepository predictionRepository;

    @Autowired
    private TestEntityManager entityManager; // For persisting test data

    @Test
    void shouldFindExistingPredictionByFlightDetailsAndReturnEmptyForNonExisting() {
        // Given
        String origin = "MIA";
        String destination = "ORD";
        LocalDate flightDate = LocalDate.of(2025, 4, 1);
        
        PredictionRequest request = new PredictionRequest("Airline", origin, destination, LocalDateTime.of(flightDate, LocalTime.of(10, 0)), 1000);
        Prediction prediction = new Prediction(request);
        prediction.setResult(false, 0.10f); // Puntual, 10% probability of delay

        entityManager.persist(prediction); // Persist the prediction directly
        entityManager.flush(); // Ensure it's written to the DB

        // When: Lookup existing prediction
        Optional<Prediction> foundPrediction = predictionRepository.lookupPrediction(origin, destination, java.sql.Date.valueOf(flightDate));

        // Then: Assert it's found and correct
        assertTrue(foundPrediction.isPresent());
        assertEquals(origin, foundPrediction.get().getOrigenVuelo());
        assertEquals(destination, foundPrediction.get().getDestinoVuelo());
        assertEquals(flightDate, foundPrediction.get().getFechaVuelo().toLocalDate());
        assertEquals(false, foundPrediction.get().isVueloRetrasado());
        assertEquals(0.10f, foundPrediction.get().getProbabilidadRetraso());

        // When: Lookup non-existing prediction
        Optional<Prediction> notFoundPrediction = predictionRepository.lookupPrediction("LAX", "SFO", java.sql.Date.valueOf(LocalDate.of(2025, 5, 1)));

        // Then: Assert it's not found
        assertFalse(notFoundPrediction.isPresent());
    }
}
