package com.aluraone.delayzero.service.ml;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OnnxValue;
import ai.onnxruntime.OrtSession;
import com.aluraone.delayzero.dto.out.PredictionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class Predictor {

    @Autowired
    private ModelLoader loader;

    private OrtSession session;

    public PredictionData processPrediction(float[] features) throws Exception {

        loader.init();
        session = loader.getSession();
        var inputData = OnnxTensor.createTensor(loader.getEnv(), features);
        Map<String, OnnxTensor> inputs = new HashMap<>();
        inputs.put("input", inputData);

        try(var results = session.run(inputs)){
            float value = (float) results.get(1).getValue();

            String resultado;
            resultado = value >= 0.6 ? "Atrasado" : "A tiempo";
            return new PredictionData(resultado, value);
        }

    }
}
