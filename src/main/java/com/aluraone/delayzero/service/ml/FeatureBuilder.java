package com.aluraone.delayzero.service.ml;

import com.aluraone.delayzero.dto.in.PredictionRequest;

public class FeatureBuilder {

    private PredictionRequest flightData;

    public FeatureBuilder(PredictionRequest flightData){
        this.flightData = flightData;
    }

}
