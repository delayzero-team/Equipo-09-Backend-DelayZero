package com.aluraone.delayzero.service.ml;

import com.aluraone.delayzero.dto.in.PredictionRequest;
import com.aluraone.delayzero.infra.exception.PredictionException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component

public class FeatureBuilder {
    
    private List<String> featureOrder;
    private Map<String, Map<String, Integer>> encoders;

    @PostConstruct
    public void init() throws PredictionException {
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            // Cargar orden de columnas
            try (InputStream columnsStream = new ClassPathResource("mlresource/feature_columns.json").getInputStream()) {
                featureOrder = mapper.readValue(columnsStream, new TypeReference<List<String>>() {});
            }
            
            // Cargar encoders
            try (InputStream encodersStream = new ClassPathResource("mlresource/label_encoders.json").getInputStream()) {
                encoders = mapper.readValue(encodersStream, new TypeReference<Map<String, Map<String, Integer>>>() {});
            }
        } catch (IOException e) {
            throw new PredictionException(PredictionException.FEATURE_BUILDER_INITIALIZATION_ERROR, e);
        }
    }

    private float encode(String encoderName, String value) throws PredictionException {
        Map<String, Integer> encoder = encoders.get(encoderName);
        
        if (encoder == null) {
            throw new PredictionException(PredictionException.ENCODER_NOT_FOUND + ": " + encoderName);
        }
        
        String key = value != null ? value.toUpperCase() : "nan";
        
        if (!encoder.containsKey(key) && !encoder.containsKey("nan")) {
            String errorMsg = encoderName.equals("IATA_CODE") 
                ? PredictionException.INVALID_AIRLINE_CODE + ": " + value
                : PredictionException.INVALID_AIRPORT_CODE + ": " + value;
            throw new PredictionException(errorMsg);
        }
        
        return encoder.getOrDefault(key, encoder.getOrDefault("nan", 0));
    }

    public float[] build(PredictionRequest request) throws PredictionException {
        if (request == null) {
            throw new PredictionException(PredictionException.INVALID_REQUEST);
        }
        
        float[] features = new float[featureOrder.size()];
        LocalDateTime departure = request.fechaPartidaVuelo();
        
        if (departure == null) {
            throw new PredictionException(PredictionException.INVALID_REQUEST + ": Fecha de partida es requerida");
        }
        
        int idx = 0;
        
        try {
            for (String col : featureOrder) {
                switch (col) {
                    case "AIRLINE" ->
                        features[idx++] = encode("IATA_CODE", request.nombreAerolinea());
                    case "ORIGIN_AIRPORT" ->
                        features[idx++] = encode("ORIGIN_AIRPORT", request.origenVuelo());
                    case "DESTINATION_AIRPORT" ->
                        features[idx++] = encode("DESTINATION_AIRPORT", request.destinoVuelo());
                    case "MONTH" ->
                        features[idx++] = departure.getMonthValue();
                    case "DAY" ->
                        features[idx++] = departure.getDayOfMonth();
                    case "DAY_OF_WEEK" ->
                        features[idx++] = departure.getDayOfWeek().getValue();
                    case "SCHEDULED_DEPARTURE_MIN" ->
                        features[idx++] = departure.getHour() * 60 + departure.getMinute();
                    case "DISTANCE" ->
                        features[idx++] = request.distanciaKilometros();
                    default ->
                        throw new PredictionException(PredictionException.UNKNOWN_FEATURE_COLUMN + ": " + col);
                }
            }
        } catch (PredictionException e) {
            throw e;
        } catch (Exception e) {
            throw new PredictionException(PredictionException.INVALID_REQUEST + ": Error procesando características", e);
        }
        
        return features;
    }
}
