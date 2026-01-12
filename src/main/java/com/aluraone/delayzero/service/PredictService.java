package com.aluraone.delayzero.service;


import com.aluraone.delayzero.dto.in.PredictionRequest;
import com.aluraone.delayzero.dto.out.PredictionData;
import com.aluraone.delayzero.service.ml.FeatureBuilder;
import com.aluraone.delayzero.service.ml.Predictor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class PredictService {

    @Autowired
    FeatureBuilder builder;

    @Autowired
    Predictor predictor;

    public PredictionData callModel(PredictionRequest request) throws Exception {
        float [] features = builder.build(request);
        return predictor.processPrediction(features);
    }
}
