package com.aluraone.delayzero.service;


import ai.onnxruntime.OrtSession;
import com.aluraone.delayzero.dto.in.PredictionRequest;
import com.aluraone.delayzero.service.ml.FeatureBuilder;
import com.aluraone.delayzero.service.ml.ModelLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PredictService {

    @Autowired
    private ModelLoader loader;

    private OrtSession session;

    public void getPrediction(PredictionRequest flightData){

        var builder = new FeatureBuilder(flightData);


        session = loader.loadModel();
    }
}
