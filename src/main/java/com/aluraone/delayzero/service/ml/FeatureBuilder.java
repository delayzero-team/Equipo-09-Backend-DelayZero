package com.aluraone.delayzero.service.ml;

<<<<<<< HEAD
import org.springframework.stereotype.Component;

@Component
public class FeatureBuilder {

=======
import com.aluraone.delayzero.dto.in.PredictionRequest;

public class FeatureBuilder {

    private PredictionRequest flightData;

    public FeatureBuilder(PredictionRequest flightData){
        this.flightData = flightData;
    }

>>>>>>> bceeafbbcba7cdc3a4c3020f2cdd0d05c1c22740
}
