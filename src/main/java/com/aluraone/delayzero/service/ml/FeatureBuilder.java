package com.aluraone.delayzero.service.ml;

import com.aluraone.delayzero.dto.in.PredictionRequest;
import com.aluraone.delayzero.infra.exception.PredictionBusinessException;
import com.aluraone.delayzero.infra.exception.PredictionTechnicalException;
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
    public void init() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            try (InputStream columnsStream = new ClassPathResource("ml/feature_columns.json").getInputStream()) {
                featureOrder = mapper.readValue(columnsStream, new TypeReference<>() {
                });
            }
            try (InputStream encodersStream = new ClassPathResource("ml/label_encoders.json").getInputStream()) {
                encoders = mapper.readValue(encodersStream, new TypeReference<>() {
                });
            }
        } catch (IOException e) {
            throw new PredictionTechnicalException(
                    PredictionTechnicalException.FILE_NOT_FOUND,
                    "Error al cargar archivos de configuración ML (feature_columns.json o label_encoders.json)",
                    e);
        }
    }

    private float encode(String encoderName, String value) {
        Map<String, Integer> encoder = encoders.get(encoderName);
        if (encoder == null) {
            throw new PredictionTechnicalException(
                    PredictionTechnicalException.FEATURE_BUILDER_FAILED,
                    "Encoder no encontrado para: " + encoderName);
        }

        if (value == null) {
            Integer nanValue = encoder.get("nan");
            if (nanValue == null) {
                throw new PredictionTechnicalException(
                        PredictionTechnicalException.UNEXPECTED_ERROR,
                        "No se encontró 'nan' en el encoder " + encoderName + " para valor nulo");
            }
            return nanValue.floatValue();
        }

        String key = value.toUpperCase();
        Integer encoded = encoder.get(key);

        if (encoded == null) {
            String errorCode = encoderName.equals("IATA_CODE")
                    ? PredictionBusinessException.INVALID_AIRLINE_CODE
                    : PredictionBusinessException.INVALID_AIRPORT_CODE;
            throw new PredictionBusinessException(
                    errorCode,
                    "Código inválido o no registrado en el dataset: '" + value + "' para " + encoderName +
                            ". Usa solo códigos válidos (ej: AA para aerolínea, JFK para aeropuerto).");
        }

        return encoded.floatValue();
    }

    public float[] build(PredictionRequest request) {
        if (request == null || request.fechaPartidaVuelo() == null) {
            throw new PredictionBusinessException(
                    PredictionBusinessException.MISSING_REQUIRED_FIELD,
                    "Solicitud inválida: faltan datos requeridos (especialmente fechaPartidaVuelo)");
        }

        float[] features = new float[featureOrder.size()];
        LocalDateTime departure = request.fechaPartidaVuelo();
        int idx = 0;

        try {
            for (String col : featureOrder) {
                switch (col) {
                    case "AIRLINE" -> features[idx++] = encode("IATA_CODE", request.nombreAerolinea());
                    case "ORIGIN_AIRPORT" -> features[idx++] = encode("ORIGIN_AIRPORT", request.origenVuelo());
                    case "DESTINATION_AIRPORT" ->
                        features[idx++] = encode("DESTINATION_AIRPORT", request.destinoVuelo());
                    case "MONTH" -> features[idx++] = departure.getMonthValue();
                    case "DAY" -> features[idx++] = departure.getDayOfMonth();
                    case "DAY_OF_WEEK" -> features[idx++] = departure.getDayOfWeek().getValue();
                    case "SCHEDULED_DEPARTURE_MIN" ->
                        features[idx++] = departure.getHour() * 60 + departure.getMinute();
                    case "DISTANCE" -> {
                        if (request.distanciaKilometros() <= 0) {
                            throw new PredictionBusinessException(
                                    PredictionBusinessException.INVALID_DISTANCE,
                                    "La distancia debe ser mayor a 0 km");
                        }
                        features[idx++] = request.distanciaKilometros();
                    }
                    default -> throw new PredictionTechnicalException(
                            PredictionTechnicalException.UNEXPECTED_ERROR,
                            "Columna desconocida en featureOrder: " + col);
                }
            }
        } catch (Exception e) {
            throw e;
        }

        return features;
    }
}