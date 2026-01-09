package com.aluraone.delayzero.service;


import ai.onnxruntime.OrtSession;
import com.aluraone.delayzero.dto.in.PredictionRequest;
import com.aluraone.delayzero.dto.out.PredictionData;
import com.aluraone.delayzero.infra.exception.PredictionException;
import com.aluraone.delayzero.service.ml.FeatureBuilder;
import com.aluraone.delayzero.service.ml.Predictor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PredictService {

    @Autowired
    FeatureBuilder builder;

    @Autowired
    Predictor predictor;

    public PredictionData callModel(PredictionRequest request) throws PredictionException {
        float [] features = builder.build(request);
        return predictor.prediccion(features);

    }
}
