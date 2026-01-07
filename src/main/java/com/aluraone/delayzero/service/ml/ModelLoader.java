package com.aluraone.delayzero.service.ml;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ModelLoader {

    private OrtSession session;
    private OrtEnvironment env;

    @PostConstruct
    public void init() throws Exception {

        env = OrtEnvironment.getEnvironment();
        session = env.createSession(
                new ClassPathResource("resources/mlresource/flight_delay_rf.onnx")
                        .getFile()
                        .getAbsolutePath(),
                new OrtSession.SessionOptions()
        );
    }

}
