package com.aluraone.delayzero.service.ml;

import ai.onnxruntime.*;
import com.aluraone.delayzero.dto.out.PredictionData;
import com.aluraone.delayzero.infra.exception.PredictionBusinessException;
import com.aluraone.delayzero.infra.exception.PredictionTechnicalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.FloatBuffer;
import java.util.Map;

@Component
public class Predictor {

    @Autowired
    private ModelLoader loader;

    public PredictionData processPrediction(float[] features) {


        if (features == null || features.length == 0) {
            throw new PredictionBusinessException(
                PredictionBusinessException.MISSING_REQUIRED_FIELD,
                "El arreglo de características está vacío o nulo. No se puede realizar la predicción."
            );
        }

        OrtSession session = loader.getSession();
        OrtEnvironment environment = loader.getEnv();

        if (session == null || environment == null) {
            throw new PredictionTechnicalException(
                PredictionTechnicalException.MODEL_LOAD_FAILED,
                "El modelo ONNX no está inicializado correctamente. Verifica la carga en ModelLoader."
            );
        }

        try {
            long[] shape = new long[]{1, features.length};
            try (OnnxTensor inputTensor = OnnxTensor.createTensor(
                    environment,
                    FloatBuffer.wrap(features),
                    shape)) {

                OrtSession.Result result = session.run(Map.of("float_input", inputTensor));

                long[] labelArr = (long[]) result.get(0).getValue();
                int prediction = (int) labelArr[0];
                String prevision = (prediction == 1) ? "Retrasado" : "Puntual";

                OnnxSequence sequence = (OnnxSequence) result.get(1);
                if (sequence == null || sequence.getValue().isEmpty()) {
                    throw new PredictionTechnicalException(
                        PredictionTechnicalException.ONNX_RUNTIME_ERROR,
                        "No se obtuvieron probabilidades del modelo ONNX"
                    );
                }

                OnnxMap onnxMap = (OnnxMap) sequence.getValue().get(0);
                Map<Long, Float> probMap = (Map<Long, Float>) onnxMap.getValue();
                float delayProbability = probMap.getOrDefault(1L, 0.0f);

                return new PredictionData(prevision, delayProbability);
            }
        } catch (OrtException e) {
            throw new PredictionTechnicalException(
                PredictionTechnicalException.ONNX_RUNTIME_ERROR,
                "Error durante la ejecución del modelo ONNX",
                e
            );
        } catch (ClassCastException e) {
            throw new PredictionTechnicalException(
                PredictionTechnicalException.UNEXPECTED_ERROR,
                "Formato inesperado en la salida del modelo ONNX (posible incompatibilidad con el modelo exportado)",
                e
            );
        } catch (Exception e) {
            throw new PredictionTechnicalException(
                PredictionTechnicalException.UNEXPECTED_ERROR,
                "Error inesperado al procesar la predicción",
                e
            );
        }
    }
}