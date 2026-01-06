package com.aluraone.delayzero.service;


import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import com.aluraone.delayzero.dto.in.PredictionRequest;
import com.aluraone.delayzero.dto.out.PredictionData;
import com.aluraone.delayzero.service.ml.FeatureBuilder;
import com.aluraone.delayzero.service.ml.ModelLoader;
import com.aluraone.delayzero.service.ml.Predictor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class PredictService {

    @Autowired
    FeatureBuilder builder;

    @Autowired
    Predictor predictor;

    public PredictionData callModel(PredictionRequest request){
        float [] features = builder.build(request);
        return predictor.prediccion(features);
    }
}
