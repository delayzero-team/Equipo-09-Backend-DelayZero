package com.aluraone.delayzero.service.ml;

import ai.onnxruntime.*;
import com.aluraone.delayzero.dto.out.PredictionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

@Component
public class Predictor {

    @Autowired
    private ModelLoader loader;

    private OrtSession session;
    private OrtEnvironment environment;

    public PredictionData processPrediction(float[] features){
       //Contexto global ONNX
        environment = loader.getEnv();
        //Modelo ONNX ya cargado en memoria
        session = loader.getSession();
        //Validacion del input
        if (features == null || features.length == 0) throw new IllegalArgumentException("Features array is empty");
        //Estructurar forma del tensor
        long[] shape = new long[]{1, features.length};
        //Crear el tensor de entreda ONNX
        try(OnnxTensor inputTensor = OnnxTensor
                .createTensor(environment,
                        FloatBuffer.wrap(features),
                        shape)){
            //Ejecuta inferencia
            OrtSession.Result result = session.run(Map.of("float_input", inputTensor));
            //Extrar el label de la clase predicha
            long [] labelArr = (long[]) result.get(0).getValue();
            int prediction = (int) labelArr[0];
            //Convertir prediccion a String
            String predictionString = prediction == 1 ? "Retrasado" : "Puntual";

            // Se optiene una secuencia Onnx:
            OnnxSequence onnxSequence = (OnnxSequence) result.get(1);

            // Se obtiene el OnnxMap:
            OnnxMap onnxMap = (OnnxMap) onnxSequence.getValue().get(0);
            
            float probabilityPrediction = ((Map<Long, Float>) onnxMap.getValue())
                    .getOrDefault( (predictionString.equals("Puntual") ? 0L : 1L) , 0.0f);

            return new PredictionData(predictionString, probabilityPrediction);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
