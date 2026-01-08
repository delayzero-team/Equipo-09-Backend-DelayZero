package com.aluraone.delayzero.service.ml;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Getter
public class ModelLoader {

    private OrtSession session;
    private OrtEnvironment env;

    @PostConstruct
    public void init() throws Exception {


        try (InputStream is =
                     new ClassPathResource("mlresource/flight_delay_rf.onnx")
                             .getInputStream()) {

            OrtEnvironment env = OrtEnvironment.getEnvironment();
            session = env.createSession(is.readAllBytes());

        } catch (Exception e) {
            // 🔥 esto es clave
            throw new IllegalStateException(
                    "No se pudo inicializar el modelo ONNX", e
            );
        }
    }

}
