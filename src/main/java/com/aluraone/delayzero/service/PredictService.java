package com.aluraone.delayzero.service;


import ai.onnxruntime.OrtSession;
import com.aluraone.delayzero.dto.in.PredictionRequest;
<<<<<<< HEAD
import com.aluraone.delayzero.dto.out.PredictionData;
=======
>>>>>>> bceeafbbcba7cdc3a4c3020f2cdd0d05c1c22740
import com.aluraone.delayzero.service.ml.FeatureBuilder;
import com.aluraone.delayzero.service.ml.ModelLoader;
import com.aluraone.delayzero.service.ml.Predictor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PredictService {

    @Autowired
    FeatureBuilder builder;

    @Autowired
    Predictor predictor;

<<<<<<< HEAD
    public PredictionData callModel(PredictionRequest request){
        float [] features = builder.build(request);
        return predictor.prediccion(features);
=======
    public void getPrediction(PredictionRequest flightData){

        var builder = new FeatureBuilder(flightData);


        session = loader.loadModel();
>>>>>>> bceeafbbcba7cdc3a4c3020f2cdd0d05c1c22740
    }
}
