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

            /*
            System.out.println(result.get(1).getValue() instanceof Map<?,?> ? "es map" : "no es map");
            System.out.println(result.get(1).getValue() instanceof List<?> ? "es lista" : "no es lista");

            System.out.println(result.get(1).getValue());
            System.out.println("Valor Secuencia Onnx: " + result.get(1));
            */

            // Se optiene una secuencia Onnx:
            OnnxSequence map = (OnnxSequence) result.get(1);

            System.out.println("Secuencia Onnx: " + map);
            System.out.println("Valor ONNXMap: " + map.getValue().get(0));

            //Se obtiene el OnnxMap:
            OnnxMap mape = (OnnxMap) map.getValue().get(0);

            System.out.println("Onnx Map: " + mape);
            System.out.println(mape.getValue()); //Se obtienen los valores.

            float delayProbability;
            delayProbability = ((Map<Long, Float>) mape.getValue())
                    .getOrDefault(1L, 0.0f);
            System.out.println(delayProbability);

            return new PredictionData(predictionString, delayProbability);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
