package com.aluraone.delayzero.service;


import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import com.aluraone.delayzero.service.ml.ModelLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class PredictService {

    @Autowired
    private ModelLoader loader;

    private OrtSession session;

    public void callModel(){
        session = loader.loadModel();
    }
}
