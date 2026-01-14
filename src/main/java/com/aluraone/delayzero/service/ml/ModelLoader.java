package com.aluraone.delayzero.service.ml;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.aluraone.delayzero.infra.exception.PredictionTechnicalException;

import java.io.InputStream;

@Component
@Getter
public class ModelLoader {

    private OrtSession session;

    private OrtEnvironment env;

    @PostConstruct
    public void init() {
        try (InputStream is = new ClassPathResource("ml/flight_delay_rf.onnx").getInputStream()) {
            env = OrtEnvironment.getEnvironment();
            session = env.createSession(is.readAllBytes());
        } catch (Exception e) {
            throw new PredictionTechnicalException(
                PredictionTechnicalException.MODEL_LOAD_FAILED,
                "No se pudo cargar el modelo ONNX flight_delay_rf.onnx",
                e
            );
        }
    }
}
