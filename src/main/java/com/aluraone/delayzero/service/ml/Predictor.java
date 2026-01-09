package com.aluraone.delayzero.service.ml;

import ai.onnxruntime.*;
import com.aluraone.delayzero.dto.out.PredictionData;
import com.aluraone.delayzero.infra.exception.PredictionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.FloatBuffer;
import java.util.Map;

@Component
public class Predictor {
    
    @Autowired
    private ModelLoader loader;
    
    private OrtSession session;
    private OrtEnvironment environment;

    public PredictionData processPrediction(float[] features) throws PredictionException {
        if (features == null || features.length == 0) {
            throw new PredictionException(PredictionException.EMPTY_FEATURES_ARRAY);
        }
        
        try {
            environment = loader.getEnv();

            session = loader.getSession();
            
            if (session == null || environment == null) {
                throw new PredictionException(PredictionException.MODEL_INITIALIZATION_ERROR);
            }

            long[] shape = new long[]{1, features.length};

            try (OnnxTensor inputTensor = OnnxTensor.createTensor(
                    environment,
                    FloatBuffer.wrap(features),
                    shape)) {

                OrtSession.Result result = session.run(Map.of("float_input", inputTensor));

                long[] labelArr = (long[]) result.get(0).getValue();
                int prediction = (int) labelArr[0];

                String predictionString = prediction == 1 ? "Retrasado" : "Puntual";

                OnnxSequence sequence = (OnnxSequence) result.get(1);
                
                if (sequence == null || sequence.getValue().isEmpty()) {
                    throw new PredictionException(PredictionException.MODEL_PROCESSING_ERROR + ": Probabilidades no disponibles");
                }

                OnnxMap onnxMap = (OnnxMap) sequence.getValue().get(0);

                float delayProbability = ((Map<Long, Float>) onnxMap.getValue())
                    .getOrDefault(1L, 0.0f);
                
                return new PredictionData(predictionString, delayProbability);
                
            }
        } catch (PredictionException e) {
            throw e; 
        } catch (OrtException e) {
            throw new PredictionException(PredictionException.MODEL_PROCESSING_ERROR + ": Error ONNX Runtime", e);
        } catch (ClassCastException e) {
            throw new PredictionException(PredictionException.MODEL_PROCESSING_ERROR + ": Formato de salida inesperado", e);
        } catch (Exception e) {
            throw new PredictionException(PredictionException.MODEL_PROCESSING_ERROR, e);
        }
    }
}