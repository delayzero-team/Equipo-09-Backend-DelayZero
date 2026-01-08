package com.aluraone.delayzero.service.ml;

import com.aluraone.delayzero.dto.in.PredictionRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class FeatureBuilder {

    private List<String> featureOrder;
    private Map<String, Map<String, Integer>> encoders;

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Cargar orden de columnas
        try (InputStream columnsStream = new ClassPathResource("mlresource/feature_columns.json").getInputStream()) {
            featureOrder = mapper.readValue(columnsStream, new TypeReference<List<String>>() {});
        }

        // Cargar encoders
        try (InputStream encodersStream = new ClassPathResource("mlresource/label_encoders.json").getInputStream()) {
            encoders = mapper.readValue(encodersStream, new TypeReference<Map<String, Map<String, Integer>>>() {});
        }
    }

    // Método auxiliar privado
    private float encode(String encoderName, String value) {
        Map<String, Integer> encoder = encoders.get(encoderName);
        if (encoder == null) {
            throw new IllegalStateException("Encoder no encontrado: " + encoderName);
        }
        String key = value.toUpperCase();
        return encoder.getOrDefault(key, encoder.getOrDefault("nan", 0)); // fallback a "nan" o 0
    }

    // ÚNICO método público
    public float[] build(PredictionRequest request) {
        float[] features = new float[featureOrder.size()];
        LocalDateTime departure = request.fechaPartidaVuelo();

        int idx = 0;
        for (String col : featureOrder) {
            switch (col) {
                case "AIRLINE" ->
                    features[idx++] = encode("IATA_CODE", request.nombreAerolinea()); // Usa IATA_CODE para códigos como "AA", "AZ"
                case "ORIGIN_AIRPORT" ->
                    features[idx++] = encode("ORIGIN_AIRPORT", request.origenVuelo());
                case "DESTINATION_AIRPORT" ->
                    features[idx++] = encode("DESTINATION_AIRPORT", request.destinoVuelo());
                case "MONTH" ->
                    features[idx++] = departure.getMonthValue();
                case "DAY" ->
                    features[idx++] = departure.getDayOfMonth();
                case "DAY_OF_WEEK" ->
                    features[idx++] = departure.getDayOfWeek().getValue(); // 1=Lunes ... 7=Domingo
                case "SCHEDULED_DEPARTURE_MIN" ->
                    features[idx++] = departure.getHour() * 60 + departure.getMinute();
                case "DISTANCE" ->
                    features[idx++] = request.distanciaKilometros();
                default ->
                    throw new IllegalArgumentException("Columna desconocida: " + col);
            }
        }

        return features;
    }
}