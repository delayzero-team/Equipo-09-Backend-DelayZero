package com.aluraone.delayzero.service.ml;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
@Getter
public class ModelLoader {

    private Map<String, Map<String, Integer>> labelEncoders;
    private List<String> featureColumns;
    private OrtSession session;
    private OrtEnvironment env;

    @PostConstruct
    public void init() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        labelEncoders = mapper.readValue(
                new ClassPathResource("resources/mlresource/label_encoders.json").getInputStream(),
                new TypeReference<>() {}
        );
        featureColumns = mapper.readValue(
                new ClassPathResource("resources/mlresource/feature_columns.json").getInputStream(),
                new TypeReference<>() {}
        );

        env = OrtEnvironment.getEnvironment();
        session = env.createSession(
                new ClassPathResource("resources/mlresource/flight_delay_rf.onnx")
                        .getFile()
                        .getAbsolutePath(),
                new OrtSession.SessionOptions()
        );
    }

}
