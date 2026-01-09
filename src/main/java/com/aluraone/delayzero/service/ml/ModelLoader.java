package com.aluraone.delayzero.service.ml;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.aluraone.delayzero.infra.exception.PredictionException;

import java.io.InputStream;

@Component
@Getter
public class ModelLoader {

    private OrtSession session;

    private OrtEnvironment env;

    @PostConstruct
    public void init() throws PredictionException {


        try (InputStream is =
                     new ClassPathResource("mlresource/flight_delay_rf.onnx")
                             .getInputStream()) {

            env = OrtEnvironment.getEnvironment();
            session = env.createSession(is.readAllBytes());

        } catch (Exception e) {
            throw new PredictionException(
                PredictionException.MODEL_INITIALIZATION_ERROR, e
            );
        }
    }

}
