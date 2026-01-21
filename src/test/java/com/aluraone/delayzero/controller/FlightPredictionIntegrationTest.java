package com.aluraone.delayzero.controller;

import com.aluraone.delayzero.dto.in.PredictionRequest;
import com.aluraone.delayzero.dto.out.PredictionData;
import com.aluraone.delayzero.service.ml.Predictor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime; // Changed from LocalDate

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Use application-test.properties
class FlightPredictionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean // Mock the ML predictor to avoid loading the actual ONNX model
    private Predictor predictor;

    @BeforeEach
    void setup() {
        // Reset the mock before each test to ensure test isolation
        Mockito.reset(predictor);
    }

    @Test
    void shouldSaveNewPredictionAndReturnItOnFirstRequest() throws Exception {
        // Given
        PredictionRequest request = new PredictionRequest("AA", "LAX", "JFK", LocalDateTime.of(2025, 2, 10, 14, 30), 1200);
        PredictionData mlResult = new PredictionData("Puntual", 0.95f);

        when(predictor.processPrediction(any(float[].class))).thenReturn(mlResult);

        // When
        mockMvc.perform(post("/prediction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prevision").value("Puntual"))
                .andExpect(jsonPath("$.probabilidad").value(0.95f));

        // Then
        verify(predictor, times(1)).processPrediction(any(float[].class)); // ML model should be called
    }

    @Test
    void shouldReturnCachedPredictionOnSecondRequest() throws Exception {
        // Given
        PredictionRequest request = new PredictionRequest("DL", "LAX", "JFK", LocalDateTime.of(2025, 3, 20, 8, 0), 800);
        PredictionData mlResult = new PredictionData("Retrasado", 0.80f);

        // First request: simulate ML model being called and prediction saved
        when(predictor.processPrediction(any(float[].class))).thenReturn(mlResult);
        mockMvc.perform(post("/prediction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Reset mock to count calls only for the second request
        Mockito.reset(predictor);

        // Second request: should use cached prediction
        mockMvc.perform(post("/prediction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prevision").value("Retrasado"))
                .andExpect(jsonPath("$.probabilidad").value(0.80f));

        // Then
        // The ML model should NOT be called again for the second request
        verify(predictor, never()).processPrediction(any(float[].class));
    }
}