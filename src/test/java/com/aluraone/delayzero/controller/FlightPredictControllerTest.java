package com.aluraone.delayzero.controller;

import com.aluraone.delayzero.dto.in.PredictionRequest;
import com.aluraone.delayzero.dto.out.PredictionData;
import com.aluraone.delayzero.service.PredictService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime; // Use LocalDateTime instead of LocalDate

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never; // Add this import
import static org.mockito.Mockito.verify; // Already present
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightPredictController.class)
class FlightPredictControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PredictService predictService;

    @Test
    void shouldReturn200AndPredictionDataForValidRequest() throws Exception {
        // Given
        PredictionRequest request = new PredictionRequest("AirlineName", "GRU", "SFO", LocalDateTime.of(2025, 1, 15, 10, 0), 1000);
        PredictionData expectedPredictionData = new PredictionData("Retrasado", 0.75f);

        when(predictService.startPrediction(any(PredictionRequest.class)))
                .thenReturn(expectedPredictionData);

        // When & Then
        mockMvc.perform(post("/prediction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prevision").value("Retrasado"))
                .andExpect(jsonPath("$.probabilidad").value(0.75f));
    }

    @Test
    void shouldReturn400ForInvalidRequest() throws Exception {
        // Given
        // An invalid request where 'nombreAerolinea' is null, violating @NotBlank
        PredictionRequest invalidRequest = new PredictionRequest(null, "SFO", "GRU", LocalDateTime.of(2025, 1, 15, 10, 0), 1000);

        // When & Then
        mockMvc.perform(post("/prediction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(predictService, never()).startPrediction(any(PredictionRequest.class)); // Verify service was NOT called
    }
}
