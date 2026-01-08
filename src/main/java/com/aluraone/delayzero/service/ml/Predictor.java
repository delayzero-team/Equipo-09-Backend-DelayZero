package com.aluraone.delayzero.service.ml;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import com.aluraone.delayzero.dto.out.PredictionData;
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
            OrtSession.Result result = session.run(Map.of("input", inputTensor));
            //Extrar el label de la clase predicha
            long [] labelArr = (long[]) result.get(0).getValue();
            int prediction = (int) labelArr[0];
            //Convertir prediccion a String
            String predictionString = prediction == 1 ? "Retrasado" : "Puntual";


            float delayProbability =
                    ((Map<Long, Float>[]) result.get(1).getValue())[0]
                            .getOrDefault(1L, 0.0f);

            return new PredictionData(predictionString, delayProbability);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
