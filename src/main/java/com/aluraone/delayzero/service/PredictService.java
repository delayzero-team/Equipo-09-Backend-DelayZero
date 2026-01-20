package com.aluraone.delayzero.service;

import com.aluraone.delayzero.dto.in.PredictionRequest;
import com.aluraone.delayzero.dto.out.PredictionData;
import com.aluraone.delayzero.infra.exception.PredictionBusinessException;
import com.aluraone.delayzero.service.ml.FeatureBuilder;
import com.aluraone.delayzero.service.ml.Predictor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PredictService {

    @Autowired
    private FeatureBuilder builder;

    @Autowired
    private Predictor predictor;

    public PredictionData callModel(PredictionRequest request) {

        if (request == null) {
            throw new PredictionBusinessException(
                    PredictionBusinessException.MISSING_REQUIRED_FIELD,
                    "La solicitud de predicción es nula");
        }

        try {
            float[] features = builder.build(request);
            return predictor.processPrediction(features);
        } catch (Exception e) {
            throw e;
        }
    }
}
