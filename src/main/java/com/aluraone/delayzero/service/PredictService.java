package com.aluraone.delayzero.service;


import com.aluraone.delayzero.domain.entity.Prediction;
import com.aluraone.delayzero.domain.repository.PredictionRepository;
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

    @Autowired
    PredictionRepository pRepo;

    public PredictionData startPrediction(PredictionRequest request){

        Prediction newPred = new Prediction(request);
        var found = pRepo.lookupPrediction(newPred.getOrigenVuelo(),newPred.getDestinoVuelo(),newPred.getFechaVuelo());

        PredictionData returnData;

        if (found.isPresent()){
            var foundPrediction = found.get();
            returnData = new PredictionData(foundPrediction.isVueloRetrasado() ? "Retrasado" : "Puntual", foundPrediction.getProbabilidadRetraso());
        } else {
            returnData = callModel(request);
            newPred.setResult(returnData.prevision().equals("Retrasado"), returnData.probabilidad());
            pRepo.save(newPred);
        }

        return returnData;
    }

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
