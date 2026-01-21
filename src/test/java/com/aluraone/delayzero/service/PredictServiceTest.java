package com.aluraone.delayzero.service;

import com.aluraone.delayzero.domain.entity.Prediction;
import com.aluraone.delayzero.domain.repository.PredictionRepository;
import com.aluraone.delayzero.dto.in.PredictionRequest;
import com.aluraone.delayzero.dto.out.PredictionData;
import com.aluraone.delayzero.infra.exception.PredictionBusinessException;
import com.aluraone.delayzero.service.ml.FeatureBuilder;
import com.aluraone.delayzero.service.ml.Predictor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Date; // For any(Date.class)

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PredictServiceTest {

    @Mock
    private PredictionRepository predictionRepository;

    @Mock
    private FeatureBuilder featureBuilder;

    @Mock
    private Predictor predictor;

    @InjectMocks
    private PredictService predictService;

    @Test
    void shouldReturnCachedPredictionWhenFoundInRepository() {
        // Given
        PredictionRequest request = new PredictionRequest("AirlineName", "GRU", "SFO", LocalDateTime.of(2025, 1, 15, 10, 0), 1000);
        Prediction existingPrediction = new Prediction(request);
        existingPrediction.setResult(true, 0.85f); // Example cached result

        when(predictionRepository.lookupPrediction(anyString(), anyString(), any(Date.class)))
                .thenReturn(Optional.of(existingPrediction));

        // When
        PredictionData result = predictService.startPrediction(request);

        // Then
        assertEquals("Retrasado", result.prevision());
        assertEquals(0.85f, result.probabilidad());
        verify(predictionRepository, times(1)).lookupPrediction(anyString(), anyString(), any(Date.class));
        verify(predictor, never()).processPrediction(any()); // Verify ML model was NOT called
        verify(predictionRepository, never()).save(any(Prediction.class)); // Verify save was NOT called
    }

    @Test
    void shouldCallMLModelAndSavePredictionWhenNotFoundInRepository() {
        // Given
        PredictionRequest request = new PredictionRequest("AirlineName", "GRU", "SFO", LocalDateTime.of(2025, 1, 15, 10, 0), 1000);
        float[] features = {1.0f, 2.0f}; // Example features
        PredictionData mlResult = new PredictionData("Puntual", 0.90f);

        when(predictionRepository.lookupPrediction(anyString(), anyString(), any(Date.class)))
                .thenReturn(Optional.empty()); // Not found in DB
        when(featureBuilder.build(any(PredictionRequest.class))).thenReturn(features);
        when(predictor.processPrediction(any(float[].class))).thenReturn(mlResult);
        when(predictionRepository.save(any(Prediction.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the saved entity

        // When
        PredictionData result = predictService.startPrediction(request);

        // Then
        assertEquals("Puntual", result.prevision());
        assertEquals(0.90f, result.probabilidad());
        verify(predictionRepository, times(1)).lookupPrediction(anyString(), anyString(), any(Date.class));
        verify(featureBuilder, times(1)).build(any(PredictionRequest.class)); // Verify feature builder was called
        verify(predictor, times(1)).processPrediction(any(float[].class)); // Verify ML model was called
        verify(predictionRepository, times(1)).save(any(Prediction.class)); // Verify save was called
    }

    @Test
    void shouldThrowPredictionBusinessExceptionWhenRequestIsNullInCallModel() {
        // When / Then
        PredictionBusinessException exception = assertThrows(PredictionBusinessException.class, () -> {
            predictService.callModel(null);
        });

        assertEquals(PredictionBusinessException.MISSING_REQUIRED_FIELD, exception.getCode());
        verify(featureBuilder, never()).build(any());
        verify(predictor, never()).processPrediction(any());
    }
}
